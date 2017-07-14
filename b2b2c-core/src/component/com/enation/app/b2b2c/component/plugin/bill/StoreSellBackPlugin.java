package com.enation.app.b2b2c.component.plugin.bill;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.store.service.bill.impl.BillManager;
import com.enation.app.shop.core.order.model.Order;
import com.enation.app.shop.core.order.model.Refund;
import com.enation.app.shop.core.order.plugin.order.IOrderRefundEvent;
import com.enation.framework.plugin.AutoRegisterPlugin;

/**
 * 店铺申请售后插件
 * @author Kanon
 *
 */
@Component
public class StoreSellBackPlugin extends AutoRegisterPlugin implements IOrderRefundEvent {

	@Autowired
	private BillManager billManager;


	@Override
	public void onRefund(Order order, Refund refund) {
		// TODO Auto-generated method stub
		billManager.add_bill(order.getOrder_id(), (0-refund.getRefund_money()));
	}


}
