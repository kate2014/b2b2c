package com.enation.app.b2b2c.front.api.store.activity;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.b2b2c.core.store.model.activity.StoreActivity;
import com.enation.app.b2b2c.core.store.service.activity.IStoreActivityManager;
import com.enation.app.shop.core.other.model.ActivityDetail;
import com.enation.framework.action.JsonResult;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.JsonResultUtil;
import com.enation.framework.util.StringUtil;

/**
 * 促销活动API
 * @author DMRain 2016年3月8日 版本改造
 * @version v2.0 改为spring mvc
 * @since v6.0
 */
@Controller
@Scope("prototype")
@RequestMapping("/api/b2b2c/store-activity")
public class StoreActivityApiController {

	protected final Logger logger = Logger.getLogger(getClass());

	@Autowired
	private IStoreActivityManager storeActivityManager;

	@Autowired
	private IStoreMemberManager storeMemberManager;

	/**
	 * 保存添加促销活动
	 * @param activity 促销活动信息
	 * @param detail 促销活动优惠详细信息
	 * @param startTime 促销活动开始时间
	 * @param endTime 促销活动结束时间
	 * @param goods_id 商品ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/save-add", produces=MediaType.APPLICATION_JSON_VALUE)
	public JsonResult saveAdd(StoreActivity activity, ActivityDetail detail, String startTime, String endTime, Integer[] goods_id) {
		try {
			
			//获取当前登陆的店铺ID
			Integer store_id = storeMemberManager.getStoreMember().getStore_id();
			
			//促销活动名称不能为空
			if (StringUtil.isEmpty(activity.getActivity_name())) {
				return JsonResultUtil.getErrorJson("请填写促销活动名称");
			}
			
			//促销活动的生效日期不能为空
			if (StringUtil.isEmpty(startTime) || StringUtil.isEmpty(endTime)) {
				return JsonResultUtil.getErrorJson("请填写促销活动生效时间");
			}
			
			Long start_time = DateUtil.getDateline(startTime, "yyyy-MM-dd HH:mm:ss");
			Long end_time = DateUtil.getDateline(endTime, "yyyy-MM-dd HH:mm:ss");
			
			if (end_time < start_time) {
				return JsonResultUtil.getErrorJson("开始时间不能大于结束时间");
			}
			
			//判断促销活动在同一时间段是否重复添加 0:不重复，1:重复
			if (this.storeActivityManager.checkActByDate(start_time, end_time, store_id, activity.getActivity_id()) == 1) {
				return JsonResultUtil.getErrorJson("同一时间段不可重复添加促销活动");
			}
			
			//促销活动的优惠门槛不能为空也不能为0
			if (detail.getFull_money() == null || detail.getFull_money() == 0) {
				return JsonResultUtil.getErrorJson("优惠门槛不能为空或不能为0");
			}
			
			//促销活动的优惠方式不能全部为空，至少要选择一项
			if (detail.getIs_full_minus() == null && detail.getIs_send_point() == null && detail.getIs_free_ship() == null 
					&& detail.getIs_send_gift() == null && detail.getIs_send_bonus() == null) {
				return JsonResultUtil.getErrorJson("请选择优惠方式");
			}
			
			//如果促销活动优惠详细是否包含满减不为空
			if (detail.getIs_full_minus() != null) {
				//如果促销活动包含了满减现金活动
				if (detail.getIs_full_minus() == 1) {
					//减少的现金不能为空也不能为0
					if (detail.getMinus_value() == null || detail.getMinus_value() == 0) {
						return JsonResultUtil.getErrorJson("减少的现金不能为空或不能为0");
					}
					
					//减少的现金必须小于优惠门槛
					if (detail.getMinus_value() > detail.getFull_money()) {
						return JsonResultUtil.getErrorJson("减少的现金不能多于" + detail.getFull_money() + "元");
					}
				}
			}
			
			//如果促销活动优惠详细是否包含满送积分不为空
			if (detail.getIs_send_point() != null) {
				//如果促销活动包含了满送积分活动
				if (detail.getIs_send_point() == 1) {
					//赠送的积分不能为空也不能为0
					if (detail.getPoint_value() == null || detail.getPoint_value() == 0) {
						return JsonResultUtil.getErrorJson("赠送的积分不能为空或不能为0");
					}
				}
			}
			
			//如果促销活动优惠详细是否包含满送赠品不为空
			if (detail.getIs_send_gift() != null) {
				//如果促销活动包含了满送赠品活动
				if (detail.getIs_send_gift() == 1) {
					//赠品id不能为0
					if (detail.getGift_id() == 0) {
						return JsonResultUtil.getErrorJson("请选择赠品");
					}
				}
			}
			
			//如果促销活动优惠详细是否包含满送优惠券不为空
			if (detail.getIs_send_bonus() != null) {
				//如果促销活动包含了满送优惠券活动
				if (detail.getIs_send_bonus() == 1) {
					//优惠券id不能为0
					if (detail.getBonus_id() == 0) {
						return JsonResultUtil.getErrorJson("请选择优惠券");
					}
				}
			}
			
			//如果促销活动选择的是部分商品参加活动
			if (activity.getRange_type() == 2) {
				//商品id组不能为空
				if (goods_id == null) {
					return JsonResultUtil.getErrorJson("请选择要参与活动的商品");
				}
			}
			
			activity.setStore_id(store_id);
			activity.setStart_time(start_time);
			activity.setEnd_time(end_time);
			activity.setDisabled(0);
			
			this.storeActivityManager.add(activity, detail, goods_id);
			return JsonResultUtil.getSuccessJson("新增促销活动成功");
			
		} catch (Exception e) {
			this.logger.error("新增促销活动失败:", e);
			e.printStackTrace();
			return JsonResultUtil.getErrorJson("新增促销活动失败");
		}
	}
	
	/**
	 * 保存修改促销活动
	 * @param activity 促销活动信息
	 * @param detail 促销活动优惠详细信息
	 * @param startTime 促销活动开始时间
	 * @param endTime 促销活动结束时间
	 * @param goods_id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/save-edit", produces=MediaType.APPLICATION_JSON_VALUE)
	public JsonResult saveEdit(StoreActivity activity, ActivityDetail detail, String startTime, String endTime, Integer[] goods_id){
		try {
			//获取当前登陆的店铺ID
			Integer store_id = storeMemberManager.getStoreMember().getStore_id();
			
			//增加权限
			if(activity.getStore_id()==null || !activity.getStore_id().equals(store_id)){
				return JsonResultUtil.getErrorJson("您没有权限");
			}
			
			//促销活动名称不能为空
			if (StringUtil.isEmpty(activity.getActivity_name())) {
				return JsonResultUtil.getErrorJson("请填写促销活动名称");
			}
			
			//促销活动的生效日期不能为空
			if (StringUtil.isEmpty(startTime) || StringUtil.isEmpty(endTime)) {
				return JsonResultUtil.getErrorJson("请填写促销活动生效时间");
			}
			
			Long start_time = DateUtil.getDateline(startTime, "yyyy-MM-dd HH:mm:ss");
			Long end_time = DateUtil.getDateline(endTime, "yyyy-MM-dd HH:mm:ss");
			
			if (end_time < start_time) {
				return JsonResultUtil.getErrorJson("开始时间不能大于结束时间");
			}
			
			//判断促销活动在同一时间段是否重复添加 0:不重复，1:重复
			if (this.storeActivityManager.checkActByDate(start_time, end_time, store_id, activity.getActivity_id()) == 1) {
				return JsonResultUtil.getErrorJson("同一时间段不可重复添加促销活动");
			}
			
			//促销活动的优惠门槛不能为空也不能为0
			if (detail.getFull_money() == null || detail.getFull_money() == 0) {
				return JsonResultUtil.getErrorJson("优惠门槛不能为空或不能为0");
			}
			
			//促销活动的优惠方式不能全部为空，至少要选择一项
			if (detail.getIs_full_minus() == null && detail.getIs_send_point() == null && detail.getIs_free_ship() == null 
					&& detail.getIs_send_gift() == null && detail.getIs_send_bonus() == null) {
				return JsonResultUtil.getErrorJson("请选择优惠方式");
			}
			
			//如果促销活动优惠详细是否包含满减不为空
			if (detail.getIs_full_minus() != null) {
				//如果促销活动包含了满减现金活动
				if (detail.getIs_full_minus() == 1) {
					//减少的现金不能为空也不能为0
					if (detail.getMinus_value() == null || detail.getMinus_value() == 0) {
						return JsonResultUtil.getErrorJson("减少的现金不能为空或不能为0");
					}
					
					//减少的现金必须小于优惠门槛
					if (detail.getMinus_value() > detail.getFull_money()) {
						return JsonResultUtil.getErrorJson("减少的现金不能多于" + detail.getFull_money() + "元");
					}
				}
			}
			
			//如果促销活动优惠详细是否包含满送积分不为空
			if (detail.getIs_send_point() != null) {
				//如果促销活动包含了满送积分活动
				if (detail.getIs_send_point() == 1) {
					//赠送的积分不能为空也不能为0
					if (detail.getPoint_value() == null || detail.getPoint_value() == 0) {
						return JsonResultUtil.getErrorJson("赠送的积分不能为空或不能为0");
					}
				}
			}
			
			//如果促销活动优惠详细是否包含满送赠品不为空
			if (detail.getIs_send_gift() != null) {
				//如果促销活动包含了满送赠品活动
				if (detail.getIs_send_gift() == 1) {
					//赠品id不能为0
					if (detail.getGift_id() == 0) {
						return JsonResultUtil.getErrorJson("请选择赠品");
					}
				}
			}
			
			//如果促销活动优惠详细是否包含满送优惠券不为空
			if (detail.getIs_send_bonus() != null) {
				//如果促销活动包含了满送优惠券活动
				if (detail.getIs_send_bonus() == 1) {
					//优惠券id不能为0
					if (detail.getBonus_id() == 0) {
						return JsonResultUtil.getErrorJson("请选择优惠券");
					}
				}
			}
			
			//如果促销活动选择的是部分商品参加活动
			if (activity.getRange_type() == 2) {
				//商品id组不能为空
				if (goods_id == null) {
					return JsonResultUtil.getErrorJson("请选择要参与活动的商品");
				}
			}
			
			activity.setStart_time(start_time);
			activity.setEnd_time(end_time);
			this.storeActivityManager.edit(activity, detail, goods_id);
			return JsonResultUtil.getSuccessJson("修改促销活动成功！");
		} catch (Exception e) {
			this.logger.error("修改促销活动失败:", e);
			return JsonResultUtil.getErrorJson("修改促销活动失败！");
		}
		
	}
	
	/**
	 * 删除促销活动
	 * @param activity_id 促销活动id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/delete", produces=MediaType.APPLICATION_JSON_VALUE)
	public JsonResult delete(Integer activity_id){
		try {
			StoreActivity activity = storeActivityManager.get(activity_id);
			Integer store_id = storeMemberManager.getStoreMember().getStore_id();
			//增加权限
			if(activity == null || !activity.getStore_id().equals(store_id)){
				return JsonResultUtil.getErrorJson("您没有权限");
			}
			
			//如果促销活动id等于空
			if (activity_id == null) {
				return JsonResultUtil.getErrorJson("请选择要删除的数据");
			}
			
			this.storeActivityManager.delete(activity_id);
			return JsonResultUtil.getSuccessJson("删除成功！");
		} catch (Exception e) {
			this.logger.error("删除失败：", e);
			return JsonResultUtil.getErrorJson("删除失败！");
		}
	}

}
