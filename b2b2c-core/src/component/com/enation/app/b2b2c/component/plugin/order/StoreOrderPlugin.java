package com.enation.app.b2b2c.component.plugin.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.order.model.StoreOrder;
import com.enation.app.b2b2c.core.order.model.StoreRefund;
import com.enation.app.b2b2c.core.order.service.IStoreOrderManager;
import com.enation.app.shop.core.order.model.Order;
import com.enation.app.shop.core.order.model.SellBackGoodsList;
import com.enation.app.shop.core.order.plugin.order.IAfterOrderSellBackEvent;
import com.enation.app.shop.core.order.plugin.order.IOrderCanelEvent;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.plugin.AutoRegisterPlugin;

/**
 * 
 * b2b2c订单插件
 * @author	Chopper
 * @version	v1.0, 2016-1-5 上午11:38:57
 * @since v6.0
 * @version v1.1 Kanon 2016-7-15 修改申请售后事件，添加店铺名称 
 * @version v1.2 Kanon 2016-7-17 添加取消订单事件实现方法，记录店铺信息
 */
@Component
public class StoreOrderPlugin extends AutoRegisterPlugin implements IAfterOrderSellBackEvent,IOrderCanelEvent{

	@Autowired
	private IStoreOrderManager storeOrderManager;
	
	@Autowired
	private IDaoSupport daoSupport;
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.core.plugin.order.IAfterOrderSellBackEvent#returnOrder(java.util.List, com.enation.app.shop.core.model.Order)
	 */
	@Override
	public void returnOrder(List<SellBackGoodsList> sellBackGoodsList,
			Order order) {
		//主要目的在于 b2b2c 处理store_id
		StoreOrder storeOrder=storeOrderManager.get( order.getOrder_id());
		this.daoSupport.execute("update es_sellback_list set store_id = ?,bill_status=0,store_name=?  where orderid = ?", storeOrder.getStore_id(),storeOrder.getStore_name(),order.getOrder_id());
	}
	
	
	@Override
	public void canel(Order order) {
		
		StoreOrder storeOrder=storeOrderManager.get(order.getOrder_id());
		this.daoSupport.execute("update es_refund set store_id = ?,store_name=?  where order_id =? ", storeOrder.getStore_id(),storeOrder.getStore_name(),storeOrder.getOrder_id());
	}
	
	
	
}
