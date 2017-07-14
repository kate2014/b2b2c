package com.enation.app.b2b2c.component.plugin.shortmsg;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.ShortMsg;
import com.enation.app.base.core.plugin.shortmsg.IShortMessageEvent;
import com.enation.app.base.core.service.auth.IPermissionManager;
import com.enation.app.base.core.service.auth.impl.PermissionConfig;
import com.enation.app.shop.ShopApp;
import com.enation.app.shop.core.order.model.SellBackStatus;
import com.enation.app.shop.core.order.service.OrderStatus;
import com.enation.eop.resource.model.AdminUser;
import com.enation.eop.sdk.context.EopSetting;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.plugin.AutoRegisterPlugin;
/**
 * 	网店短消息提醒插件
 * 	@author Kanon
 *	@author Kanon 2016-7-14 增加自营店取消订单、取消订单、申请退货、申请退款消息提醒
 */
@Component
public class B2b2cShortMsgPlugin extends AutoRegisterPlugin implements IShortMessageEvent {
	
	@Autowired
	private IDaoSupport daoSupport;
	
	@Autowired
	private IPermissionManager permissionManager;
	
	@Override
	public List<ShortMsg> getMessage() {
		if(EopSetting.PRODUCT.equals("b2b2c")){
			List<ShortMsg> msgList  = new ArrayList<ShortMsg>();
			AdminUser adminUser  = UserConext.getCurrentAdminUser();
			
			boolean haveDepotAdmin = this.permissionManager.checkHaveAuth( PermissionConfig.getAuthId("depot_admin")  ); //库存管理 权限
			boolean haveFinance = this.permissionManager.checkHaveAuth( PermissionConfig.getAuthId("finance")  ); 	//财务 权限
			boolean haveOrder = this.permissionManager.checkHaveAuth(PermissionConfig.getAuthId("order"));	 //订单管理员权限
			boolean haveCustomerService = this.permissionManager.checkHaveAuth(PermissionConfig.getAuthId("customer_service"));//客服权限
			
			ShortMsg msg= new ShortMsg();
			//判断是否拥有客服、订单管理员权限
			if(haveOrder||haveCustomerService){
				//取消订单
				msg=getCancelOrder();
				if(msg!=null){
					msgList.add(msg);
				}
				
				//退货申请
				msg=getSellBackApply();
				if(msg!=null){
					msgList.add(msg);
				}
				
				//退款申请
				msg=getRefundApply();
				if(msg!=null){
					msgList.add(msg);
				}
			}
			
			
			//判断是否有库管权限
			if(haveDepotAdmin){
				msg= this.getShipMessage(adminUser);
				if(msg!=null)
				msgList.add(msg);
			}
			
			//判断是否拥有财务权限
			if(haveFinance){
				
				//待结算订单
				msg= this.getPayConfirmMessage();
				if(msg!=null)
				msgList.add(msg);
				
				//退款
				msg=getRefundList();
				if(msg!=null){
					msgList.add(msg);
				}
			}
			
			return msgList;
		}
		return null;

	}
	
	
	/**
	 * 获取发货任务消息 
	 * 只显示自营店待发货订单消息
	 * @return
	 */
	private ShortMsg getShipMessage(AdminUser adminUser){
		int count=0;
		StringBuffer sql=new StringBuffer("select count(0) from es_order WHERE store_id=? AND parent_id is NOT NULL AND is_cancel=0 AND ( ( payment_type!='cod'  and  status="+OrderStatus.ORDER_PAY +") ");//非货到付款的，要已结算才能发货
		sql.append(" or ( payment_type='cod' and  status="+OrderStatus.ORDER_CONFIRM +")) ");//货到付款的，已确认就可以发货
			
		count = this.daoSupport.queryForInt(sql.toString(),ShopApp.self_storeid);
		
		if(count>0){
			ShortMsg msg  = new ShortMsg();
			msg.setUrl("/b2b2c/admin/self-store-order/not-ship-order.do");
			msg.setTitle("待发货订单");
			msg.setTarget("ajax");
			msg.setContent("自营店有"+count +"个订单需要您发货");
			return msg;
		}
		return null;
	}
	
	
	/**
	 * 获取待确认付款任务消息
	 * @return
	 */
	private ShortMsg getPayConfirmMessage(){
		
		String sql = "select count(0) num  from es_order where disabled = 0 AND store_id=?  AND is_cancel=0 AND parent_id is NOT NULL  and ( ( payment_type!='cod' and  status=1) or ( payment_type='cod' and  (status="+OrderStatus.ORDER_ROG+")  ) )";
		int count = this.daoSupport.queryForInt(sql,ShopApp.self_storeid);
		if(count>0){
			ShortMsg msg  = new ShortMsg();
			msg.setUrl("/b2b2c/admin/store-order/not-pay-order.do?storeId="+ShopApp.self_storeid);
			msg.setTitle("待结算订单");
			msg.setTarget("ajax");
			msg.setContent("自营店有"+count +"个新订单需要您结算");
			return msg;
		}
		return null;
	}
	
	
	/**
	 * 取消订单申请
	 * @return
	 */
	private ShortMsg getCancelOrder(){
		String sql = "select count(0) from es_order WHERE is_cancel=1 AND store_id=? AND parent_id IS NOT NULL ";
		int count = this.daoSupport.queryForInt(sql,ShopApp.self_storeid);
		if(count>0){
			ShortMsg msg  = new ShortMsg();
			msg.setUrl("/b2b2c/admin/self-store-order/cancel-application-list.do");
			msg.setContent("自营店有"+count +"个取消订单申请需要完成");
			msg.setTitle("取消订单申请");
			msg.setTarget("ajax");
			return msg;
		}
		return null;
	}
	
	/**
	 * 退货申请
	 * @return
	 */
	private ShortMsg getSellBackApply(){
		String sql="select count(0) from es_sellback_list WHERE store_id=? AND type=? AND tradestatus=?";
		int count = this.daoSupport.queryForInt(sql,ShopApp.self_storeid,2,SellBackStatus.apply.getValue());
		if(count>0){
			ShortMsg msg  = new ShortMsg();
			msg.setUrl("/shop/admin/order-report/returned-list.do");
			msg.setContent("自营店有"+count +"个退货订单需要完成");
			msg.setTitle("退货订单");
			msg.setTarget("ajax");
			return msg;
		}
		return null;
	} 
	
	/**
	 * 退款申请
	 * @return
	 */
	private ShortMsg getRefundApply(){
		String sql= "select count(0) from es_sellback_list WHERE store_id=? AND type=? AND tradestatus=?";
		int count = this.daoSupport.queryForInt(sql,ShopApp.self_storeid,1,SellBackStatus.wait.getValue());
		if(count>0){
			ShortMsg msg  = new ShortMsg();
			msg.setUrl("/b2b2c/admin/store-sell-back/refund-list.do?type=1");
			msg.setTitle("退款申请");
			msg.setContent("自营店有"+count +"个退款申请需要完成");
			msg.setTarget("ajax");
			return msg;
		}
		return null;
	} 
	
	
	/**
	 * 退款单（未处理）
	 * @return
	 */
	private ShortMsg getRefundList(){
		String sql="select count(0) from es_refund where status=?";
		int count = this.daoSupport.queryForInt(sql,0);
		if(count>0){
			ShortMsg msg  = new ShortMsg();
			msg.setUrl("/b2b2c/admin/store-order-report/refund-list.do");
			msg.setContent("有"+count +"个退款单需要完成");
			msg.setTitle("退款单");
			msg.setTarget("ajax");
			return msg;
		}
		return null;
	}
}
