package com.enation.app.b2b2c.front.api.store.activity;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.b2b2c.core.store.model.activity.StoreActivityGift;
import com.enation.app.b2b2c.core.store.service.activity.IStoreActivityGiftManager;
import com.enation.app.shop.core.other.service.IActivityGiftManager;
import com.enation.eop.SystemSetting;
import com.enation.eop.sdk.context.EopSetting;
import com.enation.framework.action.JsonResult;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.JsonResultUtil;
import com.enation.framework.util.StringUtil;

/**
 * 店铺促销活动赠品管理Api
 * @author DMRain 2016年3月9日 版本改造
 * @version v2.0 改为spring mvc
 * @since v6.0
 */
@Controller
@Scope("prototype")
@RequestMapping("/api/b2b2c/store-act-gift")
public class StoreActivityGiftApiController {

	protected final Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private IStoreActivityGiftManager storeActivityGiftManager;
	
	@Autowired
	private IStoreMemberManager storeMemberManager;
	
	@Autowired
	private IActivityGiftManager activityGiftManager;
	
	/**
	 * 保存新增赠品信息
	 * @param storeActivityGift 新增赠品信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/save-add", produces=MediaType.APPLICATION_JSON_VALUE)
	public JsonResult saveAdd(StoreActivityGift storeActivityGift){
		try {
			//赠品名称不能为空
			if (StringUtil.isEmpty(storeActivityGift.getGift_name())) {
				return JsonResultUtil.getErrorJson("请填写赠品名称");
			}
			
			//赠品价格不能为空也不能为0
			if (storeActivityGift.getGift_price() == null || storeActivityGift.getGift_price() == 0) {
				return JsonResultUtil.getErrorJson("赠品价格不能为空也不能为0");
			}
			
			//赠品价格不能为空也不能为0
			if (storeActivityGift.getActual_store() == null || storeActivityGift.getActual_store() == 0) {
				return JsonResultUtil.getErrorJson("赠品库存不能为空也不能为0");
			}
			
			//判断是否上传了赠品图片
			if (StringUtil.isEmpty(storeActivityGift.getGift_img())) {
				return JsonResultUtil.getErrorJson("请上传赠品图片");
			}
			
			//转化图片路径
			String img_url = this.transformPath(storeActivityGift.getGift_img());
			//将原图片路径替换
			storeActivityGift.setGift_img(img_url);
			
			//设置赠品可用库存(添加时可用库存=实际库存)
			storeActivityGift.setEnable_store(storeActivityGift.getActual_store());
			
			//添加赠品创建时间
			storeActivityGift.setCreate_time(DateUtil.getDateline());
			
			//设置赠品类型(1.0版本赠品类型默认为0：普通赠品)
			storeActivityGift.setGift_type(0);
			
			//默认不禁用
			storeActivityGift.setDisabled(0);
			
			//设置赠品所属店铺id
			storeActivityGift.setStore_id(storeMemberManager.getStoreMember().getStore_id());
			
			this.storeActivityGiftManager.add(storeActivityGift);
			return JsonResultUtil.getSuccessJson("添加成功！");
		} catch (Exception e) {
			this.logger.error("添加失败：", e);
			return JsonResultUtil.getErrorJson("添加失败！");
		}
		
	}
	
	/**
	 * 保存修改赠品信息
	 * @param storeActivityGift 修改后的赠品信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/save-edit", produces=MediaType.APPLICATION_JSON_VALUE)
	public JsonResult saveEdit(StoreActivityGift storeActivityGift){
		try {
			StoreActivityGift gift = this.storeActivityGiftManager.get(storeActivityGift.getGift_id());
			//增加权限
			StoreMember member = storeMemberManager.getStoreMember();
			if(gift == null || !gift.getStore_id().equals(member.getStore_id())){
				return JsonResultUtil.getErrorJson("您没有权限");
			}
			
			//赠品名称不能为空
			if (StringUtil.isEmpty(storeActivityGift.getGift_name())) {
				return JsonResultUtil.getErrorJson("请填写赠品名称");
			}
			
			//赠品价格不能为空也不能为0
			if (storeActivityGift.getGift_price() == null || storeActivityGift.getGift_price() == 0) {
				return JsonResultUtil.getErrorJson("赠品价格不能为空也不能为0");
			}
			
			//赠品价格不能为空也不能为0
			if (storeActivityGift.getActual_store() == null || storeActivityGift.getActual_store() == 0) {
				return JsonResultUtil.getErrorJson("赠品库存不能为空也不能为0");
			}
			
			//判断是否上传了赠品图片
			if (StringUtil.isEmpty(storeActivityGift.getGift_img())) {
				return JsonResultUtil.getErrorJson("请上传赠品图片");
			}
			
			//转化图片路径
			String img_url = this.transformPath(storeActivityGift.getGift_img());
			
			//将原图片路径替换
			storeActivityGift.setGift_img(img_url);
			
			
			//获取赠品原实际库存和原可用库存的差
			int differ = gift.getActual_store() - gift.getEnable_store();
			
			//设置赠品修改后的可用库存
			storeActivityGift.setEnable_store(storeActivityGift.getActual_store() - differ);
			
			this.storeActivityGiftManager.edit(storeActivityGift);
			return JsonResultUtil.getSuccessJson("修改成功！");
		} catch (Exception e) {
			this.logger.error("修改失败：", e);
			return JsonResultUtil.getErrorJson("修改失败！");
		}
		
	}
	
	/**
	 * 删除赠品信息
	 * @param gift_id 赠品id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/delete", produces=MediaType.APPLICATION_JSON_VALUE)
	public JsonResult delete(Integer gift_id){
		try {
			//增加权限
			StoreActivityGift gift = this.storeActivityGiftManager.get(gift_id);
			StoreMember member = storeMemberManager.getStoreMember();
			if(gift == null || !gift.getStore_id().equals(member.getStore_id())){
				return JsonResultUtil.getErrorJson("您没有权限");
			}
			
			int result = this.activityGiftManager.checkGiftInAct(gift_id);
			
			//如果此赠品没有参与促销活动，可以删除，如过参与了，不可删除
			if (result == 0) {
				this.activityGiftManager.delete(gift_id);
				return JsonResultUtil.getSuccessJson("赠品删除成功！");
			} else {
				return JsonResultUtil.getErrorJson("此赠品已经参与了促销活动，不可删除！");
			}
			
		} catch (Exception e) {
			this.logger.error("删除失败：", e);
			e.printStackTrace();
			return JsonResultUtil.getErrorJson("赠品删除失败！");
		}
		
	}
	
	/**
	 * 页面中传递过来的图片地址为:http://<staticserver>/<image path>
	 * 如:http://static.enationsoft.com/attachment/goods/1.jpg
	 * 存在库中的为fs:/attachment/goods/1.jpg 生成fs式路径
	 * @param path 原地址路径
	 * @return
	 */
	private String transformPath(String path) {
		String static_server_domain= SystemSetting.getStatic_server_domain();

		String regx =static_server_domain;
		path = path.replace(regx, EopSetting.FILE_STORE_PREFIX);
		return path;
	}
}
