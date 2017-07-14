package com.enation.app.b2b2c.front.tag.store;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.goods.service.IStoreGoodsManager;
import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberCommentManager;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.b2b2c.core.order.service.IStoreOrderManager;
import com.enation.app.shop.core.order.model.SellBackStatus;
import com.enation.app.shop.core.order.service.OrderStatus;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
@Component
/**
 * 我的店铺其他信息Tag
 * @author LiFenLong
 *
 */
public class MyStoreDetailOtherTag extends BaseFreeMarkerTag{
	@Autowired
	private IStoreOrderManager storeOrderManager;
	@Autowired
	private IStoreGoodsManager storeGoodsManager;
	@Autowired
	private IStoreMemberCommentManager storeMemberCommentManager;
	@Autowired
	private IStoreMemberManager storeMemberManager;
	
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Map result=new HashMap();
		//店铺订单数量
		int storeAllOrder= storeOrderManager.getStoreOrderNum(-999);	//店铺全部订单
//		int orderNotPay=storeOrderManager.getStoreOrderNum(OrderStatus.ORDER_NOT_PAY);			//货到付款订单
		int orderConfirm=storeOrderManager.getStoreOrderNum(OrderStatus.ORDER_CONFIRM);				//待付款订单
		int orderPay=storeOrderManager.getStoreOrderNum(OrderStatus.ORDER_PAY,OrderStatus.ORDER_NOT_PAY);			//等待发货
		int orderShip=storeOrderManager.getStoreOrderNum(OrderStatus.ORDER_SHIP);			//等待收货    2016-7-22 16:37:47 修正获取待收货订单数量 
		int orderComplete=storeOrderManager.getStoreOrderNum(OrderStatus.ORDER_COMPLETE);		//订单已完成
		int orderSellback=storeOrderManager.getStoreSellbackOrder();	//获取店铺退货单数量 add by DMRain 2016-4-18

		//店铺仓库中商品数量
		int notMarket=storeGoodsManager.getStoreGoodsNum(0);
		
		//店铺中出售中的商品数量
		int ingMarket=storeGoodsManager.getStoreGoodsNum(1);
		
		//卖家未处理得商品留言
		StoreMember member=storeMemberManager.getStoreMember();
		int notReply=storeMemberCommentManager.getCommentCount(2,member.getStore_id());
		
		result.put("storeAllOrder", storeAllOrder);
//		result.put("orderNotPay", orderNotPay);
		result.put("orderConfirm", orderConfirm);
		result.put("orderPay", orderPay);
		result.put("orderShip", orderShip);
		result.put("orderComplete", orderComplete);
		result.put("orderSellback", orderSellback);
		
		result.put("notMarket", notMarket);
		result.put("notReply", notReply);
		result.put("ingMarket", ingMarket);
		return result;
	}
	
	
	
}