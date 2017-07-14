package com.enation.app.b2b2c.component.bonus.api;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enation.app.b2b2c.component.bonus.model.StoreBonusType;
import com.enation.app.b2b2c.component.bonus.service.B2b2cBonusSession;
import com.enation.app.b2b2c.component.bonus.service.IB2b2cBonusManager;
import com.enation.app.b2b2c.core.goods.service.StoreCartContainer;
import com.enation.app.b2b2c.core.goods.service.StoreCartKeyEnum;
import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.shop.component.bonus.model.Bonus;
import com.enation.app.shop.component.bonus.model.MemberBonus;
import com.enation.app.shop.core.order.model.support.OrderPrice;
import com.enation.framework.action.GridController;
import com.enation.framework.action.JsonResult;
import com.enation.framework.util.CurrencyUtil;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.JsonResultUtil;
import com.enation.framework.util.TestUtil;


/**
 * b2b2c优惠券api
 * @author xulipeng
 * @version v1.0
 * @since v6.2.1
 */
@Controller
@Scope("prototype")
@RequestMapping("/api/b2b2c/bonus")
public class B2b2cBonusApiController extends GridController {

	//log4j日志对象
	protected final Logger logger = Logger.getLogger(getClass());

	@Autowired
	private IB2b2cBonusManager b2b2cBonusManager;

	@Autowired
	private IStoreMemberManager storeMemberManager;

	/**
	 * 添加优惠卷
	 * @param storeBonus 添加的优惠券信息
	 * @param useTimeStart 优惠券使用开始时间
	 * @param useTimeEnd 优惠券使用结束时间
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/add-full-subtract", produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResult add_fullSubtract(StoreBonusType storeBonus, String useTimeStart, String useTimeEnd) {
		// 获取当前登陆的会员信息
		StoreMember member = this.storeMemberManager.getStoreMember();

		StoreBonusType bonus = new StoreBonusType();

		bonus.setType_money(storeBonus.getType_money());
		bonus.setType_name(storeBonus.getType_name());
		bonus.setMin_goods_amount(storeBonus.getMin_goods_amount());
		bonus.setUse_start_date(DateUtil.getDateline(useTimeStart + " 00:00:00", "yyyy-MM-dd HH:mm:ss"));
		bonus.setUse_end_date(DateUtil.getDateline(useTimeEnd + " 23:59:59", "yyyy-MM-dd HH:mm:ss"));
		bonus.setStore_id(member.getStore_id());
		bonus.setCreate_num(storeBonus.getCreate_num());
		bonus.setLimit_num(storeBonus.getLimit_num());
		bonus.setBelong(2);
		
		//优惠券已被领取的数量。默认为0 by_DMRain 2016-6-24
		bonus.setReceived_num(0);
		
		// bonus.setIs_given(is_given); 页面已经被注销，不明原因。 本处注销，如果需要，请优先处理本处。whj
		// 2015-05-22

		if (bonus.getUse_end_date() <= bonus.getUse_start_date()) {
			return JsonResultUtil.getErrorJson("结束时间不得大于开始时间");
		}

		try {
			this.b2b2cBonusManager.add_FullSubtract(bonus);
			return JsonResultUtil.getSuccessJson("添加成功");
		} catch (Exception e) {
			this.logger.error("添加失败：", e);
			return JsonResultUtil.getErrorJson("添加失败");
		}
	}

	/**
	 * 修改优惠劵
	 * @param storeBonus 修改的优惠券信息
	 * @param useTimeStart 优惠券使用开始时间
	 * @param useTimeEnd 优惠券使用结束时间
	 * @param type_id 优惠券ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/edit-full-subtract", produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResult edit_fullSubtract(StoreBonusType storeBonus, String useTimeStart, String useTimeEnd, Integer type_id) {
		// 获取当前登陆的会员信息
		StoreMember member = this.storeMemberManager.getStoreMember();
		//增加权限
		StoreBonusType sbonus = this.b2b2cBonusManager.getBonus(type_id);
		if(sbonus == null || !sbonus.getStore_id().equals(member.getStore_id())){
			return JsonResultUtil.getErrorJson("您没有权限");
		}
		
		int num = this.b2b2cBonusManager.getCountBonus(type_id);
		if(num>0){
			return JsonResultUtil.getErrorJson("已经有会员领取优惠券，不能修改");
		}
		
		StoreBonusType bonus = new StoreBonusType();

		bonus.setType_money(storeBonus.getType_money());
		bonus.setType_name(storeBonus.getType_name());
		bonus.setMin_goods_amount(storeBonus.getMin_goods_amount());
		bonus.setUse_start_date(DateUtil.getDateline(useTimeStart + " 00:00:00", "yyyy-MM-dd HH:mm:ss"));
		bonus.setUse_end_date(DateUtil.getDateline(useTimeEnd + " 23:59:59", "yyyy-MM-dd HH:mm:ss"));
		bonus.setStore_id(member.getStore_id());
		bonus.setCreate_num(storeBonus.getCreate_num());
		bonus.setLimit_num(storeBonus.getLimit_num());
		bonus.setType_id(type_id);
		bonus.setBelong(2);

		if (bonus.getUse_end_date() <= bonus.getUse_start_date()) {
			return JsonResultUtil.getErrorJson("结束时间不得大于开始时间");
		}

		try {
			this.b2b2cBonusManager.edit_FullSubtract(bonus);
			return JsonResultUtil.getSuccessJson("修改成功");
		} catch (Exception e) {
			this.logger.error("修改失败：", e);
			return JsonResultUtil.getErrorJson("修改失败");
		}
	}

	/**
	 * 用户领取优惠卷
	 * 对用户领取优惠券做各种验证 chang by DMRain 2016-3-17
	 * @param store_id 店铺ID
	 * @param type_id 优惠券ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/receive-bonus", produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResult receiveBonus(Integer store_id, Integer type_id) {
		StoreMember member = this.storeMemberManager.getStoreMember();
		
		//判断当前会员是否登陆，如果没有登陆不可领取优惠券
		if (member == null) {
			return JsonResultUtil.getErrorJson("请登陆后再领取优惠券！");
		}
		
		StoreBonusType bonus =	this.b2b2cBonusManager.getBonus(type_id);
		
		int limit = bonus.getLimit_num();	//限领数量
		int createNum = bonus.getCreate_num();	//优惠券发行量
		
		//已经不需要下面的方法来查询优惠券已被领取的数量了，所以注释掉。by_DMRain 2016-6-24
//		int count = this.storePromotionManager.getCountBonus(type_id);
		
		//如果优惠券已经过期，是不可以被领取的 add_by DMRain 2016-7-28
		if (bonus.getUse_end_date() < DateUtil.getDateline()) {
			return JsonResultUtil.getErrorJson("此优惠券已过期，不可领取！");
		}
		
		//判断优惠券是否已被领完
		if(bonus.getReceived_num() >= createNum){
			return JsonResultUtil.getErrorJson("此优惠券已经被领完！");
		}
		
		int num = this.b2b2cBonusManager.getmemberBonus(type_id,member.getMember_id());
		
		//判断会员领取的优惠券数量是否超出了限领数
		if (num >= limit) {
			return JsonResultUtil.getErrorJson("此优惠劵限领"+limit+"个!");
		}
		
		try {
			if (member.getStore_id()==null || !member.getStore_id().equals(bonus.getStore_id())) {
				this.b2b2cBonusManager.receive_bonus(member.getMember_id(), store_id, type_id);
				return JsonResultUtil.getSuccessJson("领取成功!");
			} else {
				return JsonResultUtil.getErrorJson("您不能领自己店铺的优惠劵!");
			}
		} catch (Exception e) {
			this.logger.error("领取失败：", e);
			return JsonResultUtil.getErrorJson("领取失败！");
		}

	}

	/**
	 * 删除优惠劵
	 * @param type_id 优惠券ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delete-bonus", produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResult deleteBonus(Integer type_id) {
		try {
			//增加权限
			StoreMember member = this.storeMemberManager.getStoreMember();
			StoreBonusType sbonus = this.b2b2cBonusManager.getBonus(type_id);
			if(sbonus == null || !sbonus.getStore_id().equals(member.getStore_id())){
				return JsonResultUtil.getErrorJson("您没有权限");
			}
			this.b2b2cBonusManager.deleteBonus(type_id);
			return JsonResultUtil.getSuccessJson("删除成功!");
		} catch (RuntimeException e) {
			return JsonResultUtil.getErrorJson(e.getMessage());
		} catch (Exception e) {
			return JsonResultUtil.getErrorJson("删除失败!");
		}
	}
	
	
	/**
	 * 更改店铺使用的优惠券——订单结算
	 * @author xulipeng
	 * @param store_id	店铺id
	 * @param bonus_id	为0则是取消此店铺已使用的优惠券
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@ResponseBody
	@RequestMapping(value = "/use-bonus",produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResult useBonus(Integer store_id,Integer bonus_id){
		try {
			StoreMember member = this.storeMemberManager.getStoreMember();
			if(member==null){
				return JsonResultUtil.getErrorJson("未登录，不能使用");
			}
			
			// 由购物车列表中获取此店铺的相关信息
			Map storeData = StoreCartContainer.getStoreData(store_id);
			OrderPrice orderPrice = (OrderPrice) storeData.get(StoreCartKeyEnum.storeprice.toString());
			
			// 新增如果选择不使用优惠券，就将已经放进session中的店铺优惠券信息删除 add_by DMRain 2016-7-14
			if (bonus_id!=null && bonus_id.intValue() != 0) {
				MemberBonus bonus = b2b2cBonusManager.getOneMyBonus(member.getMember_id(), store_id, bonus_id);
				changeBonus(orderPrice, bonus, store_id);
			} else {
				//如果不使用优惠券，店铺价格恢复原价
				orderPrice.setDiscountPrice(0.0);
				orderPrice.setNeedPayMoney(orderPrice.getOrderPrice());
				B2b2cBonusSession.cancelB2b2cBonus(store_id);
			}
			storeData.put(StoreCartKeyEnum.storeprice.toString(), orderPrice);//存放到session中
			return JsonResultUtil.getObjectJson(orderPrice, "storeprice");
			
		} catch (Exception e) {
			e.printStackTrace();
			TestUtil.print(e);
			return JsonResultUtil.getErrorJson("出现问题，请稍后重试");
		}
	}
	
	
	/**
	 * 修改优惠券选项
	 * @author liushuai
	 * @version v1.0,2015年9月22日18:21:33
	 * @param bonuss
	 * @since v1.0
	 */
	private OrderPrice changeBonus(OrderPrice orderprice, MemberBonus bonus, Integer storeid) {

		// set 红包
		B2b2cBonusSession.useBonus(storeid, bonus);

		// 如果优惠券面额大于商品优惠价格的话 那么优惠价格为商品价格
		if (orderprice.getNeedPayMoney() <= bonus.getType_money()) {
			orderprice.setDiscountPrice(orderprice.getNeedPayMoney());
			orderprice.setNeedPayMoney(0.0);
		} else {
			// 计算需要支付的金额		xulipeng 修改为订单价格减优惠券金额  2016年12月28日
			orderprice.setNeedPayMoney(CurrencyUtil.add(orderprice.getOrderPrice(), -bonus.getType_money()));
			orderprice.setDiscountPrice(bonus.getType_money());
		}
		return orderprice;

	}
	
}
