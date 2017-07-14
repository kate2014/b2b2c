package com.enation.app.b2b2c.front.api.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.util.SystemOutLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enation.app.b2b2c.component.bonus.model.StoreBonusType;
import com.enation.app.b2b2c.component.bonus.service.IB2b2cBonusManager;
import com.enation.app.b2b2c.component.plugin.order.StoreCartPluginBundle;
import com.enation.app.b2b2c.core.goods.service.StoreCartContainer;
import com.enation.app.b2b2c.core.goods.service.StoreCartKeyEnum;
import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.b2b2c.core.order.model.StoreOrder;
import com.enation.app.b2b2c.core.order.service.IStoreOrderManager;
import com.enation.app.b2b2c.core.order.service.cart.IStoreCartManager;
import com.enation.app.b2b2c.core.store.service.activity.IStoreActivityGiftManager;
import com.enation.app.base.core.service.IRegionsManager;
import com.enation.app.shop.component.bonus.model.Bonus;
import com.enation.app.shop.component.bonus.model.MemberBonus;
import com.enation.app.shop.component.bonus.service.IBonusManager;
import com.enation.app.shop.core.member.model.MemberAddress;
import com.enation.app.shop.core.member.service.IMemberAddressManager;
import com.enation.app.shop.core.order.model.Order;
import com.enation.app.shop.core.order.model.support.OrderPrice;
import com.enation.app.shop.core.order.service.ICartManager;
import com.enation.app.shop.core.order.service.IOrderFlowManager;
import com.enation.app.shop.core.order.service.IOrderManager;
import com.enation.app.shop.core.order.service.IOrderPrintManager;
import com.enation.app.shop.core.order.service.IOrderReportManager;
import com.enation.app.shop.core.order.service.IPaymentManager;
import com.enation.app.shop.core.other.model.ActivityDetail;
import com.enation.app.shop.core.other.service.IActivityDetailManager;
import com.enation.framework.action.GridController;
import com.enation.framework.action.JsonResult;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.CurrencyUtil;
import com.enation.framework.util.JsonResultUtil;
import com.enation.framework.util.StringUtil;
import com.enation.framework.util.TestUtil;

/**
 * 店铺订单API
 * 
 * @author LiFenlong SpringMVC改造
 * @date 2016-03-04
 * @since 6.0
 * @author chopper
 * @author Kanon 2016年7月6日；增加审核取消订单申请方法
 */
@Controller
@RequestMapping("/api/store/store-order")
public class StoreOrderApiController extends GridController {
	@Autowired
	private IOrderManager orderManager;
	@Autowired
	private IStoreOrderManager storeOrderManager;
	@Autowired
	private IOrderFlowManager orderFlowManager;
	@Autowired
	private IMemberAddressManager memberAddressManager;
	@Autowired
	private IOrderPrintManager orderPrintManager;
	@Autowired
	private IStoreCartManager storeCartManager;
	@Autowired
	private IStoreMemberManager storeMemberManager;
	@Autowired
	private ICartManager cartManager;
	@Autowired
	private StoreCartPluginBundle storeCartPluginBundle;
	@Autowired
	private IBonusManager bonusManager;
	@Autowired
	private IOrderReportManager orderReportManager;
	
	/** 行政区划管理接口 add by DMRain 2016-4-26 */
	@Autowired
	private IRegionsManager regionsManager;

	/** 支付方式管理接口 add by DMRain 2016-4-26 */
	@Autowired
	private IPaymentManager paymentManager;

	/** 促销活动优惠详细管理接口 add by DMRain 2016-4-26 */
	@Autowired
	private IActivityDetailManager activityDetailManager;

	/** 促销活动赠品管理接口 add by DMRain 2016-4-26 */
	@Autowired
	private IStoreActivityGiftManager storeActivityGiftManager;

	/** 店铺促销优惠券管理接口 add by xulipeng 2017年01月05日 */
	@Autowired
	private IB2b2cBonusManager b2b2cBonusManager;

	
	/**
	 * 创建订单，需要购物车中有商品
	 * 
	 * @param address_id
	 *            :收货地址id.int型，必填项
	 * @param payment_id
	 *            :支付方式id，int型，必填项
	 * @param shipDay
	 *            ：配送时间，String型 ，可选项
	 * @param shipTime
	 *            ，String型 ，可选项
	 * @param remark
	 *            ，String型 ，可选项
	 * 
	 * @return 返回json串 result 为1表示添加成功0表示失败 ，int型 message 为提示信息
	 * 
	 */
	@ResponseBody
	@RequestMapping(value = "create")
	public JsonResult create() {
		try {
			HttpServletRequest request = ThreadContextHolder.getHttpRequest();
			// 获取用户选择的支付方式ID
			Integer paymentId = StringUtil.toInt(request.getParameter("paymentId"), 0);
			
			// 通过支付ID获取支付方式类型
			String payType = "";
			if(paymentId.intValue()==0){	//支付方式为0，是在线支付。
				payType ="online";
			}else{
				payType = this.paymentManager.get(paymentId).getType();
			}
			// 获取用户选中的收货地址
			MemberAddress address = StoreCartContainer.getUserSelectedAddress();

			// 判断收货地址不能为空
			if (address == null) {
				return JsonResultUtil.getErrorJson("收货地址不能为空");
			}

			// 如果用户选择的是货到付款
			if (payType.equals("cod")) {
				// 如果用户选择的收货地区不支持货到付款(对省、市、区三级都要做判断)
				if (regionsManager.get(address.getProvince_id()).getCod() != 1) {
					return JsonResultUtil.getErrorJson("您选择的收货地址不支持货到付款！");
				}

				if (regionsManager.get(address.getCity_id()).getCod() != 1) {
					return JsonResultUtil.getErrorJson("您选择的收货地址不支持货到付款！");
				}

				if (regionsManager.get(address.getRegion_id()).getCod() != 1) {
					return JsonResultUtil.getErrorJson("您选择的收货地址不支持货到付款！");
				}
			}

			Order order = this.innerCreateOrder();
//			// 获取红包，使用红包
//			Map<Integer, MemberBonus> map = (Map) ThreadContextHolder.getSession().getAttribute(BonusSession.B2B2C_SESSIONKEY);
//			if (map != null) {
//				for (MemberBonus mb : map.values()) {
//					if (mb != null) {
//						bonusManager.use(mb.getBonus_id(), order.getMember_id(), order.getOrder_id(), order.getSn(),mb.getBonus_type_id());
//					}
//				}
//			}
			
			return JsonResultUtil.getObjectJson(order);
		} catch (RuntimeException e) {
			this.logger.error("创建订单出错", e);
			return JsonResultUtil.getErrorJson("创建订单出错:" + e.getMessage());

		}

	}

	/**
	 * 改变店铺的配送方式以及红包<br>
	 * 调用此api时必须已经访问过购物车列表<br>
	 * @return 含有价格信息的json串
	 */
	@SuppressWarnings("rawtypes")
	@ResponseBody
	@RequestMapping(value = "change-args-type")
	public JsonResult changeArgsType(Integer regionid, Integer store_id, Integer type_id) {

		// 修改优惠券
		// changeBonus(bonus_id, store_id);

		// 由购物车列表中获取此店铺的相关信息
		Map storeData = StoreCartContainer.getStoreData(store_id);

		// 获取此店铺的购物列表
		List list = (List) storeData.get(StoreCartKeyEnum.goodslist.toString());

		// 配送地区
		String regionid_str = regionid == null ? "" : regionid + "";

		// 计算此配送方式时的店铺相关价格
		OrderPrice orderPrice = this.cartManager.countPrice(list, type_id, regionid_str);

		// 激发计算子订单价格事件
		orderPrice = storeCartPluginBundle.countChildPrice(orderPrice);
		
		// 获取购物车中已经选择的商品的订单价格 by_DMRain 2016-6-28
		OrderPrice storePrice = (OrderPrice) storeData.get(StoreCartKeyEnum.storeprice.toString());
		Double act_discount = storePrice.getActDiscount();
		
		// 如果促销活动优惠的现金不为空 by_DMRain 2016-6-28
		if (act_discount != null && act_discount != 0) {
			orderPrice.setActDiscount(act_discount);
			orderPrice.setNeedPayMoney(orderPrice.getNeedPayMoney() - act_discount);
		}

		Integer activity_id = (Integer) storeData.get("activity_id");

		// 如果促销活动id不为空 by_DMRain 2016-6-28
		if (activity_id != null) {
			ActivityDetail detail = this.activityDetailManager.getDetail(activity_id);
			// 如果促销活动包含了免运费的优惠内容 by_DMRain 2016-6-28
			if (detail.getIs_free_ship() == 1) {
				orderPrice.setIs_free_ship(1);
				orderPrice.setAct_free_ship(orderPrice.getShippingPrice());
				orderPrice.setShippingPrice(0d);
			}

			// 如果促销含有送积分的活动
			if (detail.getIs_send_point() == 1) {
				orderPrice.setPoint(detail.getPoint_value());
				
				//xulipeng 修复促销活动赠送积分
				orderPrice.setActivity_point(detail.getPoint_value());
			}

			// 如果促销含有送赠品的活动
			if (detail.getIs_send_gift() == 1) {
				// 获取赠品的可用库存
				Integer enable_store = this.storeActivityGiftManager.get(detail.getGift_id()).getEnable_store();

				// 如果赠品的可用库存大于0
				if (enable_store > 0) {
					orderPrice.setGift_id(detail.getGift_id());
				}
			}

			// 如果促销含有送优惠券的活动
			if (detail.getIs_send_bonus() == 1) {
				// 获取店铺优惠券信息
				StoreBonusType bonus = this.b2b2cBonusManager.getBonus(detail.getBonus_id());

				// 优惠券发行量
				int createNum = bonus.getCreate_num();

				// 获取优惠券已被领取的数量
				int count = this.b2b2cBonusManager.getCountBonus(detail.getBonus_id());

				// 如果优惠券的发行量大于已经被领取的优惠券数量
				if (createNum > count) {
					orderPrice.setBonus_id(detail.getBonus_id());
				}
			}
		}
		
		//切换配送方式时，减去当前店铺已使用的优惠券金额
		orderPrice.setNeedPayMoney(CurrencyUtil.sub(orderPrice.getNeedPayMoney(), storePrice.getDiscountPrice()));
		orderPrice.setDiscountPrice(storePrice.getDiscountPrice());
		
		// 重新压入此店铺的订单价格和配送方式id
		storeData.put(StoreCartKeyEnum.storeprice.toString(), orderPrice);
		storeData.put(StoreCartKeyEnum.shiptypeid.toString(), type_id);

		return JsonResultUtil.getObjectJson(orderPrice, "storeprice");
	}
	
	

	/**
	 * 改变收货地址<br>
	 * 调用此api时会更改session中的用户选中的地址
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "change-address")
	public JsonResult changeAddress(Integer address_id) {
		try {
			// 根据id得到地址后压入session
			MemberAddress address = this.memberAddressManager.getAddress(address_id);
			StoreCartContainer.putSelectedAddress(address);

			// 重新计算价格
			this.storeCartManager.countPrice("yes");

			//要重新计算一遍购物车中已选择结算的商品费用，并将各种信息put进session中 add_by DMRain 2016-8-8
			this.storeCartManager.countSelectPrice("yes");
			
			// 由session中获取店铺购物车数据，已经是计算过费用的了
			List<Map> storeCartList = StoreCartContainer.getSelectStoreCartListFromSession();
			
			List newList = new ArrayList();
			for (Map map : storeCartList) {
				//add by jianghongyan 增加region_id字段
				map.put("region_id", address.getRegion_id());
				Map jsonMap = new HashMap();
				jsonMap.putAll(map);
				jsonMap.remove(StoreCartKeyEnum.goodslist.toString());
				newList.add(jsonMap);
				
				OrderPrice storeOrderPrice  =(OrderPrice) map.get(StoreCartKeyEnum.storeprice.toString());
				if(storeOrderPrice!=null){
					storeOrderPrice.setNeedPayMoney(CurrencyUtil.sub(storeOrderPrice.getNeedPayMoney(), storeOrderPrice.getDiscountPrice()));
				}
				
				//在切换收货地址并保存时，要将session中的店铺优惠券信息去掉，这样是为了防止订单价格混乱 add_by DMRain 2016-8-9
				Integer store_id = (Integer) map.get("store_id");
				if (store_id != null) {
					//BonusSession.cancelB2b2cBonus(store_id);
				}
				
			}
			
			return JsonResultUtil.getObjectJson(newList);
		} catch (Exception e) {
			return JsonResultUtil.getErrorJson(e.getMessage());
		}
	}

	/**
	 * 订单确认
	 * 
	 * @param orderId
	 *            订单Id,Integer
	 * @return 返回json串 result 为1表示调用成功0表示失败
	 */
	@ResponseBody
	@RequestMapping(value = "confirm")
	public JsonResult confirm(Integer orderId) {
		try {
			//增加权限校验
			StoreMember member = storeMemberManager.getStoreMember();
			if(member==null){
				return JsonResultUtil.getErrorJson("请登录！订单确认失败！");
			}
			StoreOrder order = storeOrderManager.get(orderId);
			if(order==null||!order.getStore_id().equals(member.getStore_id())){
				return JsonResultUtil.getErrorJson("您没有权限");
			}
			
			this.orderFlowManager.confirmOrder(orderId);
			// this.orderFlowManager.addCodPaymentLog(order);
			return JsonResultUtil.getSuccessJson("'订单[" + order.getSn() + "]成功确认'");
		} catch (RuntimeException e) {
			if (logger.isDebugEnabled()) {
				logger.debug(e);
			}
			return JsonResultUtil.getErrorJson("订单确认失败" + e.getMessage());
		}
	}

	/**
	 * 订单支付
	 * 
	 * @param orderId
	 *            订单Id,Integer
	 * @param member
	 *            店铺会员,StoreMember
	 * @param paymentId
	 *            结算单Id,Integer
	 * @param payMoney
	 *            付款金额,Double
	 * @return 返回json串 result 为1表示调用成功0表示失败
	 */
	@ResponseBody
	@RequestMapping(value = "pay")
	public JsonResult pay(Integer orderId, Double payMoney) {
		try {
			// 获取当前操作者
			StoreMember member = storeMemberManager.getStoreMember();
			Order order = this.orderManager.get(orderId);
			Integer paymentId = orderReportManager.getPaymentLogId(orderId);

			// 调用执行添加收款详细表
			if (orderFlowManager.pay(paymentId, orderId, payMoney, member.getUname())) {
				return JsonResultUtil.getSuccessJson("订单[" + order.getSn() + "]收款成功");
			} else {
				return JsonResultUtil.getErrorJson("订单[" + order.getSn() + "]收款失败,您输入的付款金额合计大于应付金额");
			}
		} catch (RuntimeException e) {
			if (logger.isDebugEnabled()) {
				logger.debug(e);
			}
			return JsonResultUtil.getErrorJson("确认付款失败:" + e.getMessage());
		}

	}

	/**
	 * 订单发货
	 * 
	 * @param order_id
	 *            订单Id,Integer[]
	 * @return 返回json串 result 为1表示调用成功0表示失败
	 */
	@ResponseBody
	@RequestMapping(value = "ship")
	public JsonResult ship(Integer[] order_id, String[] shipNos, Integer[] logi_id, String[] logi_name) {
		try {
			//增加权限校验
			StoreMember member = storeMemberManager.getStoreMember();
			if(member==null){
				return JsonResultUtil.getErrorJson("请登录！订单发货失败！");
			}
			StoreOrder order = storeOrderManager.get(order_id[0]);
			if(order==null||!order.getStore_id().equals(member.getStore_id())){
				return JsonResultUtil.getErrorJson("您没有权限");
			}
			
			storeOrderManager.saveShipNo(order_id, logi_id[0], logi_name[0], shipNos[0]);
			String is_ship = orderPrintManager.ship(order_id);
			if (is_ship.equals("true")) {
				return JsonResultUtil.getSuccessJson("发货成功");
			} else {
				return JsonResultUtil.getErrorJson(is_ship);
			}
		} catch (Exception e) {
			this.logger.error("发货出错", e);
			return JsonResultUtil.getErrorJson(e.getMessage());
		}

	}

	/**
	 * 修改配送费用
	 * 
	 * @param orderId
	 *            订单Id,Integer
	 * @param currshipamount
	 *            修改前价格,Double
	 * @param member
	 *            店铺会员,StoreMember
	 * @return 返回json串 result 为1表示调用成功0表示失败
	 */
	@ResponseBody
	@RequestMapping(value = "save-ship-price")
	public JsonResult saveShipPrice(Integer orderId, Double shipmoney) {
		try {
			//增加权限校验
			StoreMember member = storeMemberManager.getStoreMember();
			if(member==null){
				return JsonResultUtil.getErrorJson("请登录！修改费用失败！");
			}
			StoreOrder order = storeOrderManager.get(orderId);
			if(order==null||!order.getStore_id().equals(member.getStore_id())){
				return JsonResultUtil.getErrorJson("您没有权限");
			}

			// 修改前价格
			double currshipamount = orderManager.get(orderId).getShipping_amount();
			double price = this.orderManager.saveShipmoney(shipmoney, orderId);
			// 获取操作人，记录日志
			this.orderManager.addLog(orderId, "运费从" + currshipamount + "修改为" + price, member.getUname());
			return JsonResultUtil.getSuccessJson("保存成功");
		} catch (RuntimeException e) {
			this.logger.error(e.getMessage(), e);
			return JsonResultUtil.getErrorJson("保存失败");
		}
	}

	/**
	 * 修改订单金额
	 * 
	 * @param orderId
	 *            订单Id,Integer
	 * @param amount
	 *            修改前价格,Double
	 * @param member
	 *            店铺会员,StoreMember
	 * @return 返回json串 result 为1表示调用成功0表示失败
	 */
	@ResponseBody
	@RequestMapping(value = "save-price")
	public JsonResult savePrice(Integer orderId, Double payMoney) {
		try {
			//增加权限校验
			StoreMember member = storeMemberManager.getStoreMember();
			if(member==null){
				return JsonResultUtil.getErrorJson("请登录！修改订单金额失败！");
			}
			StoreOrder order = storeOrderManager.get(orderId);
			if(order==null||!order.getStore_id().equals(member.getStore_id())){
				return JsonResultUtil.getErrorJson("您没有权限");
			}

			// 修改前价格
			double amount = orderManager.get(orderId).getOrder_amount();
			this.orderManager.savePrice(payMoney, orderId);
			// 获取操作人，记录日志
			orderManager.addLog(orderId, "运费从" + amount + "修改为" + payMoney, member.getUname());
			return JsonResultUtil.getSuccessJson("修改订单价格成功");
		} catch (Exception e) {
			this.logger.error(e);
			return JsonResultUtil.getErrorJson("修改订单价格失败");
		}
	}

	/**
	 * 修改收货人信息
	 * 
	 * @param orderId
	 *            订单Id,Integer
	 * @param member
	 *            店铺会员,StoreMember
	 * @param oldShip_day
	 *            修改前收货日期,String
	 * @param oldship_name
	 *            修改前收货人姓名,String
	 * @param oldship_tel
	 *            修改前收货人电话
	 * @param oldship_mobile
	 *            修改前收货人手机号
	 * @param oldship_zip
	 *            修改前邮编           
	 * 
	 * @param remark
	 *            订单备注,String
	 * @param ship_day
	 *            收货时间,String
	 * @param ship_name
	 *            收货人姓名,String
	 * @param ship_tel
	 *            收货人电话,String
	 * @param ship_mobile
	 *            收货人手机号,String
	 * @param ship_zip
	 *            邮政编号
	 * @return 返回json串 result 为1表示调用成功0表示失败
	 */
	@ResponseBody
	@RequestMapping(value = "save-consigee")
	public JsonResult saveConsigee(Integer orderId, String remark, String ship_day, String ship_name, String ship_tel,
			String ship_mobile, String ship_zip, String addr) {
		try {
			//增加权限校验
			StoreMember member = storeMemberManager.getStoreMember();
			if(member==null){
				return JsonResultUtil.getErrorJson("请登录！");
			}
			StoreOrder order = storeOrderManager.get(orderId);
			if(order==null||!order.getStore_id().equals(member.getStore_id())){
				return JsonResultUtil.getErrorJson("您没有权限");
			}

			String oldship_day = order.getShip_day();
			String oldship_name = order.getShip_name();
			String oldship_tel = order.getShip_tel();
			String oldship_mobile = order.getShip_mobile();
			String oldship_zip = order.getShip_zip();

			// 判断是否修改、收货日期、收件人姓名、收件人电话、收件人手机、收件人邮编
			if (ship_day != null && !StringUtil.isEmpty(ship_day) && !ship_day.equals(oldship_day)) {
				this.orderManager.addLog(orderId, "收货日期从['" + oldship_day + "']修改为['" + ship_day + "']",member.getUname());
			}
			if (ship_name != null && !StringUtil.isEmpty(ship_name) && !ship_name.equals(oldship_name)) {
				this.orderManager.addLog(orderId, "收货人姓名从['" + oldship_name + "']修改为['" + ship_name + "']",member.getUname());
			}
			if (ship_tel != null && !StringUtil.isEmpty(ship_tel) && !ship_tel.equals(oldship_tel)) {
				this.orderManager.addLog(orderId, "收货人电话从['" + oldship_tel + "']修改为['" + ship_tel + "']",member.getUname());
			}
			if (ship_mobile != null && !StringUtil.isEmpty(ship_mobile) && !ship_mobile.equals(oldship_mobile)) {
				this.orderManager.addLog(orderId, "收货人手机从['" + oldship_mobile + "']修改为['" + ship_mobile + "']",member.getUname());
			}
			if (ship_zip != null && !StringUtil.isEmpty(ship_zip) && !ship_zip.equals(oldship_zip)) {
				this.orderManager.addLog(orderId, "收货人邮编从['" + oldship_zip + "']修改为['" + ship_zip + "']",member.getUname());
			}
			this.saveAddr(orderId, addr);
			//保存收货人信息
			this.storeOrderManager.saveShipInfo(remark, ship_day, ship_name, ship_tel, ship_mobile, ship_zip, orderId);
			return JsonResultUtil.getSuccessJson("修改成功");
		} catch (Exception e) {
			logger.error(e);
			return JsonResultUtil.getSuccessJson("修改失败");
		}

	}
	

	/**
	 * 审核取消订单申请 判断申请是否属于当前操作店铺如果是当前店铺才可进行操作
	 * 
	 * @author Kanon
	 * @param order_id 订单Id
	 * @param status 状态 0,拒绝 1,通过
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/auth-cancel-application", produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResult authCancelApplication(Integer order_id, Integer status) {
		try {
			StoreOrder order = storeOrderManager.get(order_id);
			if (!order.getStore_id().equals( storeMemberManager.getStoreMember().getStore_id()) ) {
				return JsonResultUtil.getErrorJson("审核失败:当前操作订单非本店铺订单");
			}
			orderManager.authCancelApplication(order_id, status);
			return JsonResultUtil.getSuccessJson("审核成功");
		} catch (Exception e) {
			this.logger.error("审核取消订单申请出错", e);
			return JsonResultUtil.getErrorJson("审核失败:" + e.getMessage());
		}
	}

	/**
	 * 支付
	 * @param orderId 订单Id,Integer
	 * @param payment_id 付款单Id,Integer
	 * @param paymoney 付款金额,Double
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/cod-order-pay", produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResult pay(Integer orderId,Double paymoney,Integer payment_id) {
		try{
			//获取操作用户
			String username=storeMemberManager.getStoreMember().getUname();
			// 调用执行添加收款详细表
			if (orderFlowManager.pay(payment_id, orderId, paymoney,username)) {
				return JsonResultUtil.getSuccessJson("订单收款成功");
			} else {
				return JsonResultUtil.getErrorJson("订单收款失败,您输入的付款金额合计大于应付金额");
			}
		}catch(RuntimeException e){
			e.printStackTrace();
			if(logger.isDebugEnabled()){
				logger.debug(e);
			}
			return JsonResultUtil.getErrorJson("确认付款失败:"+e.getMessage());
		}
	}
	
	/**
	 * 修改配送地区
	 * 
	 * @param province
	 *            省,String
	 * @param city
	 *            城市,String
	 * @param region
	 *            区,String
	 * @param Attr
	 *            详细地址,String
	 * 
	 * @param province_id
	 *            省Id,String
	 * @param city_id
	 *            城市Id,String
	 * @param region_id
	 *            区Id,String
	 * 
	 * @param oldAddr
	 *            修改前详细地址,String
	 * @param orderId
	 *            订单Id,Integer
	 * @return void
	 */
	private void saveAddr(Integer orderId, String addr) {
		// 获取地区
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String province = request.getParameter("province");
		String city = request.getParameter("city");
		String region = request.getParameter("region");
		String town = request.getParameter("town");
		String Attr = province + "-" + city + "-" + region;
		if(town!=null && !"".equals(town)){
			Attr+="-"+town;
		}
		// 获取地区Id
		String province_id = request.getParameter("province_id");
		String city_id = request.getParameter("city_id");
		String region_id = request.getParameter("region_id");
		String town_id = request.getParameter("town_id");

		// 记录日志，获取当前操作人
		if(town_id!=null&&!"".equals(town_id)){//不确定原来的方法参数修改后是否会有问题，所以增加了看起来多余的方法
			this.orderManager.saveAddrAndTown(orderId,
					StringUtil.toInt(province_id, true),
					StringUtil.toInt(city_id, true),
					StringUtil.toInt(region_id, true),
					StringUtil.toInt(town_id, true),Attr);
		}else{
			this.orderManager.saveAddr(orderId,
					StringUtil.toInt(province_id, true),
					StringUtil.toInt(city_id, true),
					StringUtil.toInt(region_id, true),Attr);
		}
		this.orderManager.saveAddrDetail(addr, orderId);

	}

	/**
	 * 创建订单
	 * 
	 * @return
	 */
	private Order innerCreateOrder() {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();

		Integer shippingId = 0; // 主订单没有配送方式

		Integer paymentId = StringUtil.toInt(request.getParameter("paymentId"), 0);

		Order order = new Order();
		order.setShipping_id(shippingId); // 配送方式
		order.setPayment_id(paymentId);// 支付方式
		if (paymentId == 0){	//如果支付方式为0，是在线支付
			order.setIs_online(1);
		}

		// 用户选中的地址
		MemberAddress address = StoreCartContainer.getUserSelectedAddress();
		if (address == null) {
			throw new RuntimeException("收货地址不能为空");
		}

		order.setShip_provinceid(address.getProvince_id());
		order.setShip_cityid(address.getCity_id());
		order.setShip_regionid(address.getRegion_id());
		//增加四级地区
		if(address.getTown_id()!=null && !address.getTown_id().equals(-1)){
			order.setShip_townid(address.getTown_id());
		}
		
		order.setShip_addr(address.getAddr());
		order.setShip_mobile(address.getMobile());
		order.setShip_tel(address.getTel());
		order.setShip_zip(address.getZip());
		
		if(StringUtil.isEmpty(address.getTown())){
			order.setShipping_area(address.getProvince() + "-" + address.getCity() + "-" + address.getRegion());
		}else{
			order.setShipping_area(address.getProvince() + "-" + address.getCity() + "-" + address.getRegion() + "-" + address.getTown());
		}
		
		order.setShip_name(address.getName());
		order.setRegionid(address.getRegion_id());

		order.setMemberAddress(address);
		order.setShip_day(request.getParameter("shipDay"));
		order.setShip_time(request.getParameter("shipTime"));
		order.setRemark(request.getParameter("remark"));
		order.setAddress_id(address.getAddr_id());
		String sessionid = request.getSession().getId();
		order = this.storeOrderManager.createOrder(order, sessionid);
		return order;
	}
}
