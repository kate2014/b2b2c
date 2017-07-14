package com.enation.app.b2b2c.core.order.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.b2b2c.core.order.model.StoreOrder;
import com.enation.app.shop.core.order.model.Order;
import com.enation.app.shop.core.order.model.PayCfg;
import com.enation.app.shop.core.order.model.PaymentDetail;
import com.enation.app.shop.core.order.plugin.order.OrderPluginBundle;
import com.enation.app.shop.core.order.plugin.payment.IPaySuccessProcessor;
import com.enation.app.shop.core.order.service.IOrderFlowManager;
import com.enation.app.shop.core.order.service.IOrderReportManager;
import com.enation.app.shop.core.order.service.IPaymentManager;
import com.enation.app.shop.core.order.service.OrderStatus;
import com.enation.framework.database.IDaoSupport;
/**
 * 店铺订单支付成功处理器
 * @author LiFenLong
 *
 */
@Service("b2b2cOrderPaySuccessProcessor")
public class B2b2cOrderPaySuccessProcessor implements IPaySuccessProcessor {
	
	@Autowired
	private IOrderFlowManager orderFlowManager;
	
	
	@Autowired
	private IOrderReportManager orderReportManager;
	
	@Autowired
	private IDaoSupport daoSupport;
	
	@Autowired
	private IStoreOrderManager storeOrderManager;
	
	@Autowired
	private IPaymentManager paymentManager;
	
	@Autowired
	private OrderPluginBundle orderPluginBundle;
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.core.plugin.payment.IPaySuccessProcessor#paySuccess(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void paySuccess(String ordersn, String tradeno,String payment_account, String ordertype,String pluginId) {
		StoreOrder order=storeOrderManager.get(ordersn);
		if (order.getPay_status() == OrderStatus.PAY_NO) {
			if( order.getPay_status().intValue()== OrderStatus.PAY_YES ){ //如果是已经支付的，不要再支付
				return ;
			}
			this.payConfirmOrder(order,payment_account,pluginId);
			if(order.getParent_id()==null){
				//获取子订单列表
				List<StoreOrder> cOrderList= storeOrderManager.storeOrderList(order.getOrder_id());
				for (StoreOrder storeOrder : cOrderList) {
					this.payConfirmOrder(storeOrder,payment_account,pluginId);
				}
			}
			orderPluginBundle.confirm(order.getOrder_id(),order.getNeed_pay_money());
		}
	}
	
	/**
	 * 订单确认收款
	 * @param order 订单
	 * @param pluginId 支付方式type
	 */
	private void payConfirmOrder(StoreOrder order,String payment_account,String pluginId){
		this.orderFlowManager.payConfirm(order.getOrder_id());
		Double needPayMoney= order.getNeed_pay_money(); //在线支付的金额
		int paymentid = orderReportManager.getPaymentLogId(order.getOrder_id());
		
		PaymentDetail paymentdetail=new PaymentDetail();
		
		paymentdetail.setAdmin_user("系统");
		paymentdetail.setPay_date(new Date().getTime());
		paymentdetail.setPay_money(needPayMoney);
		paymentdetail.setPayment_id(paymentid);
		orderReportManager.addPayMentDetail(paymentdetail);
		
		PayCfg payCfg = this.paymentManager.get(pluginId);
		
		//修改订单状态为已付款付款
		this.daoSupport.execute("update es_payment_logs set paymoney=paymoney+? , pay_method=? where payment_id=?",
				needPayMoney,payCfg.getName(),paymentid);
		
		//如果订单为子订单，则改变子订单中的paymoney的值 add by DMRain 2016-7-12
		if (this.daoSupport.queryForInt("select count(0) from es_order where parent_id = ?", order.getOrder_id()) == 0) {
			//更新订单的已付金额
			//增加更新支付方式	 By xulipeng 2016年08月03日
			this.daoSupport.execute("update es_order set paymoney=paymoney+?,payment_account=?, payment_id=?,payment_name=?,payment_type=? where order_id=?",
					needPayMoney,payment_account,payCfg.getId(),payCfg.getName(),payCfg.getType(),order.getOrder_id());
		}
	}
}
