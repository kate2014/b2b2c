package com.enation.app.b2b2c.component.plugin.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.enation.app.shop.core.order.model.Order;
import com.enation.app.shop.core.order.model.OrderItem;
import com.enation.app.shop.core.order.plugin.order.IOrderRogconfirmEvent;
import com.enation.app.shop.core.order.service.IOrderManager;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.plugin.AutoRegisterPlugin;
@Component
/***
 * b2b2c收货后添加商品购买数量
 * @author LiFenLong
 *
 */
public class B2b2cRogGoodsPlugin extends AutoRegisterPlugin implements IOrderRogconfirmEvent{
	private IOrderManager orderManager;
	private IDaoSupport daoSupport;
	@Override
	public void rogConfirm(Order order) {
		List<OrderItem> orderItemList=orderManager.listGoodsItems(order.getOrder_id());
		
		for (OrderItem orderItem : orderItemList) {
			String sql="update es_goods set buy_num=buy_num+? where goods_id=?";
			this.daoSupport.execute(sql,orderItem.getNum(),orderItem.getGoods_id());
			/**
			 * 更新orderItem ship_num为统计商品下单量提供数据
			 */
			orderItem.setShip_num(orderItem.getNum());
			Map<String,Object> paramMap=new HashMap<String,Object>();
			paramMap.put("item_id", orderItem.getItem_id());
			this.daoSupport.update("es_order_items", orderItem, paramMap);
		}
		
	}
	public IOrderManager getOrderManager() {
		return orderManager;
	}
	public void setOrderManager(IOrderManager orderManager) {
		this.orderManager = orderManager;
	}
	public IDaoSupport getDaoSupport() {
		return daoSupport;
	}
	public void setDaoSupport(IDaoSupport daoSupport) {
		this.daoSupport = daoSupport;
	}
}
