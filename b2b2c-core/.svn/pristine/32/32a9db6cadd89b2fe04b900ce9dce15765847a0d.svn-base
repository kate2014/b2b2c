package com.enation.app.b2b2c.core.order.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.b2b2c.core.order.model.StoreRefund;
import com.enation.app.b2b2c.core.order.service.IStoreSellBackManager;
import com.enation.app.b2b2c.core.store.model.Store;
import com.enation.app.b2b2c.core.store.service.IStoreManager;
import com.enation.app.shop.core.order.model.Order;
import com.enation.app.shop.core.order.model.OrderItem;
import com.enation.app.shop.core.order.model.SellBackStatus;
import com.enation.app.shop.core.order.service.IOrderGiftManager;
import com.enation.app.shop.core.order.service.IOrderManager;
import com.enation.app.shop.core.order.service.IRefundManager;
import com.enation.app.shop.core.order.service.ISellBackManager;
import com.enation.app.shop.core.order.service.OrderItemStatus;
import com.enation.app.shop.core.order.service.OrderStatus;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.annotation.Log;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.log.LogType;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;

/**
 * 店铺退货申请管理类
 * @author Kanon 2016-3-2；6.0版本改造
 * @version 1.1 Kanon 2016-7-15 修改退货流程
 *
 */
@Service("storeSellBackManager")
public class StoreSellBackManager implements IStoreSellBackManager {

	@Autowired
	private IDaoSupport daoSupport;

	@Autowired
	private IOrderManager orderManager;

	@Autowired
	private IStoreManager storeManager;

	@Autowired
	private IRefundManager refundManager;

	/** 订单赠品管理 add_by DMRain 2016-7-21 */
	@Autowired
	private IOrderGiftManager orderGiftManager;
	
	@Autowired
	private ISellBackManager sellBackManager;

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.order.service.IStoreSellBackManager#list(int, int, java.lang.Integer, java.lang.Integer, java.util.Map)
	 */
	@Override
	public Page list(int page, int pageSize, Integer store_id,Integer status,Map map) {
		Long start_time= (Long)map.get("start_time");
		Long end_time= (Long)map.get("end_time");
		StringBuffer sql = new  StringBuffer("select * from es_sellback_list");
		String condition=" WHERE ";
		if(store_id!=null){
			sql.append(condition+" store_id = "+store_id);
			condition=" AND ";
		}
		if(status!=null){
			sql.append(condition+"tradestatus="+status);
			condition=" AND ";
		}
		if(start_time!=null){
			sql.append(condition+"regtime>"+start_time);
			condition=" AND ";
		}
		if(end_time!=null){
			sql.append(condition+"regtime<"+end_time);
			condition=" AND ";
		}
		if(map.size()>0){
			Integer type=(Integer)map.get("type");
			if(type!=null){
				sql.append(condition+"type="+type);
				condition=" AND ";
			}
			if(map.get("order_sn")!=null){
				//订单号
				sql.append(condition+"ordersn='"+map.get("order_sn").toString()+"'");
				condition=" AND ";
			}
			//状态
			if(map.get("tradestatus")!=null){
				sql.append(condition+"tradestatus="+map.get("tradestatus").toString());
				condition=" AND ";
			}
			//退货单号
			if(map.get("tradeno")!=null){
				sql.append(condition+"tradeno="+map.get("tradeno").toString());
			}
		}

		sql.append(" order by id desc ");
		Page webpage = this.daoSupport.queryForPage(sql.toString(), page, pageSize);
		return webpage;
	}


	@Override
	@Log(type=LogType.ORDER,detail="维权，退货单号为${id}退款")
	public void refund(Integer id, Double alltotal_pay) {
		//修改退货价格
		this.daoSupport.execute("update es_sellback_list set alltotal_pay= ?,tradestatus=? where id=?",alltotal_pay,SellBackStatus.application.getValue(), id);

		Map map = this.get(id);

		/**
		 * 创建退款单
		 */
		StoreRefund refund=new StoreRefund();
		refund.setSn(DateUtil.toString(DateUtil.getDateline(),"yyMMddhhmmss"));
		refund.setMember_id(Integer.parseInt(map.get("member_id").toString()));
		refund.setRefund_way(map.get("refund_way").toString());
		if(map.get("return_account")!=null){
			refund.setReturn_account(map.get("return_account").toString());
		}
		refund.setRefund_money(Double.parseDouble(map.get("alltotal_pay").toString()));
		refund.setSellback_id(Integer.parseInt(map.get("id").toString()));
		refund.setSndto(map.get("sndto").toString());
		refund.setCreate_time(DateUtil.getDateline());
		refund.setOrder_id(Integer.parseInt(map.get("orderid").toString()));
		refund.setStatus(0);
		refund.setStore_id(Integer.parseInt(map.get("store_id").toString()));
		refund.setStore_name(map.get("store_name").toString());
		refundManager.addRefund(refund);

		Order order=this.orderManager.get(refund.getOrder_id());

		/**
		 * 记录订单日志
		 */
		//增加店铺为空判断，如果店铺为空，则是后台操作，获取后台管理员名字存储日志
		Store store = storeManager.getStore();
		String optionName = "";
		if(store==null){
			optionName = UserConext.getCurrentAdminUser()==null?"":UserConext.getCurrentAdminUser().getUsername();
		}else{
			optionName = store.getStore_name();
		}
		
		orderManager.addLog(order.getOrder_id(), "等待退款，退款金额："+alltotal_pay,optionName);

	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.order.service.IStoreSellBackManager#refundList(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Page refundList(Integer page, Integer pageSize) {

		return daoSupport.queryForPage("SELECT r.* FROM es_refund r ORDER BY id DESC",page, pageSize);
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.order.service.IStoreSellBackManager#getRefund(java.lang.Integer)
	 */
	@Override
	public Map getRefund(Integer id) {
		return daoSupport.queryForMap("SELECT  r.* FROM es_refund r WHERE id=?", id);
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.order.service.IStoreSellBackManager#authRetund(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public void authRetund(Integer id,Integer status,String seller_remark) {

		//获取退货单或退款单信息
		Map sellBackMap = this.get(id);

		if(sellBackMap==null){
			throw new RuntimeException("获取不到售后申请单!");
		}
		//获取类型 1：退款单，2：退货单 add_by DMRain 2016-7-21
		Integer type = StringUtil.toInt(sellBackMap.get("type").toString(), true);

		//日志信息 add_by DMRain 2016-7-21
		String log = "";

		//获取退货单中的赠品id add_by DMRain 2016-7-21
		Integer gift_id = StringUtil.toInt(sellBackMap.get("gift_id"), false);
		//兼容数据库
		if(gift_id==0){
			gift_id=null;
		}
		
		//获取订单Id
		Integer order_id=StringUtil.toInt(sellBackMap.get("orderid").toString(),true);
		//审核通过创建退款单
		if(sellBackMap!=null){
			if(status.equals(1)){
				if(UserConext.getCurrentMember()!=null){

					//判断单据的类型 1：退款单，2：退货单
					if (type.equals(1)) {
						log = "申请退款，通过";
					} else if (type.equals(2)) {
						log = "申请退货，通过";
					}

					orderManager.addLog(order_id, log, storeManager.getStore().getStore_name());

					/**
					 * 判断退货单中的赠品id是否为空,如果不为空，就改变订单赠品状态为已完成退货
					 * add_by DMRain 2016-7-21
					 */
					if (gift_id != null) {
						this.orderGiftManager.updateGiftStatus(gift_id, order_id, 2);
					}
				}
			}else{
				//判断单据的类型 1：退款单，2：退货单
				if (type.equals(1)) {
					log = "申请退款，拒绝通过";
				} else if (type.equals(2)) {
					log = "申请退货，拒绝通过";
				}

				//修改订单状态为可申请退款
				this.daoSupport.execute("update es_order set status=? where order_id=?",OrderStatus.ORDER_COMPLETE,order_id);

				//根据订单id获取订单商品货物集合 add_by DMRain 2016-7-26
				List<OrderItem> itemList = this.orderManager.listGoodsItems(order_id);

				/** 遍历订单商品货物集合，在审核退货拒绝时，将已经申请退货的货物状态改为正常，以便再次申请退货  add_by DMRain 2016-7-26*/
				for (OrderItem item : itemList) {
					if (item.getState() == 1) {
						this.daoSupport.execute("update es_order_items set state = ? where item_id = ?", OrderItemStatus.NORMAL, item.getItem_id());
					}
				}

				orderManager.addLog(order_id, log, storeManager.getStore().getStore_name());

				/**
				 * 判断退货单中的赠品id是否为空,如果不为空，就改变订单赠品状态为正常
				 * add_by DMRain 2016-7-21
				 */
				if (gift_id != null) {
					this.orderGiftManager.updateGiftStatus(gift_id, order_id, 0);
				}
			}
			sellBackManager.saveLog(id, log,UserConext.getCurrentMember().getName());
		}
		//判断处理退货申请还是退款申请
		this.daoSupport.execute("update es_sellback_list set tradestatus=?,seller_remark=? where id=?",status,seller_remark,id);

	}


	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.order.service.IStoreSellBackManager#refundByStoreIdList(java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Page refundByStoreIdList(Integer page, Integer pageSize, Integer store_id) {
		return daoSupport.queryForPage("SELECT r.* FROM es_refund r where store_id=?",page, pageSize,store_id);
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.order.service.IStoreSellBackManager#get(java.lang.Integer)
	 */
	public Map get(Integer id){
		return this.daoSupport.queryForMap("select * from es_sellback_list where id=?", id);

	}


}
