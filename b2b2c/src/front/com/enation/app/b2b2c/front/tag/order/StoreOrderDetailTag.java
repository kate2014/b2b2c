package com.enation.app.b2b2c.front.tag.order;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.b2b2c.core.order.model.StoreOrder;
import com.enation.app.b2b2c.core.order.service.IStoreOrderManager;
import com.enation.app.shop.core.order.model.Order;
import com.enation.eop.processor.core.UrlNotFoundException;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.StringUtil;

import freemarker.template.TemplateModelException;

/**
 * 查看店铺订单详细标签
 * @author LiFenLong
 *
 */
@Component
@Scope("prototype")
public class StoreOrderDetailTag extends BaseFreeMarkerTag{
	@Autowired
	private IStoreOrderManager storeOrderManager;
	
	@Autowired
	private IStoreMemberManager storeMemberManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String ordersn =  request.getParameter("ordersn");
		String orderid  = request.getParameter("orderid");
		
		StoreOrder order  =null;
		if(!StringUtil.isEmpty(orderid) ){
			order=storeOrderManager.get(Integer.valueOf(orderid));
			
		}else if(!StringUtil.isEmpty(ordersn)){
			order=	storeOrderManager.get(ordersn);
			
		}else{
			throw new UrlNotFoundException();
			
		}
		
		//父订单逻辑要根据子订单应付金额而实时变动
		if(order!=null){
			if(order.getParent_id()==null||order.getParent_id()==0){
				List<Order> childOrderList=this.storeOrderManager.getByParentId(order.getOrder_id());
				Double needpaymoney=0d;
				Double order_amount=0d;
				for (Order childOrder : childOrderList) {
					needpaymoney += childOrder.getNeed_pay_money();
					order_amount += childOrder.getOrder_amount();
				}
				order.setNeed_pay_money(needpaymoney);
				order.setOrder_amount(order_amount);
				this.storeOrderManager.update(order);
			}
		}
		if(order==null){
			throw new UrlNotFoundException();
		}
		
		//校验是否本会员的操作
		StoreMember member = storeMemberManager.getStoreMember();
		
		if(member!=null && order.getStore_id().equals(member.getStore_id())){
			
		}else{
			throw new UrlNotFoundException();
		}
		
		return order;
	}
	
}
