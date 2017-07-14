package com.enation.app.b2b2c.core.store.service.bill.impl;
 

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.b2b2c.core.order.model.StoreOrder;
import com.enation.app.b2b2c.core.store.model.Store;
import com.enation.app.b2b2c.core.store.model.bill.Bill;
import com.enation.app.b2b2c.core.store.model.bill.BillDetail;
import com.enation.app.b2b2c.core.store.model.bill.BillStatusEnum;
import com.enation.app.b2b2c.core.store.service.IStoreManager;
import com.enation.app.b2b2c.core.store.service.bill.IBillManager;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.CurrencyUtil;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;

/**
 * 店铺结算管理类
 * @version 1.1 Kanon 修改类结构
 *
 */
@Service("billManager")
public class BillManager implements IBillManager {
	
	@Autowired
	private IDaoSupport daoSupport;
	
	@Autowired
	private IStoreManager storeManager;
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.bill.IBillManager#bill_list(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Page bill_list(Integer pageNo, Integer pageSize) {
		
		return this.daoSupport.queryForPage("SELECT * FROM es_bill order by bill_id desc", pageNo, pageSize);
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.bill.IBillManager#bill_detail_list(java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Page bill_detail_list(Integer pageNo, Integer pageSize,Integer bill_id,String keyword) {
		String sql = "SELECT * FROM es_bill_detail WHERE bill_id = ?";
		if(keyword != null || !StringUtil.isEmpty(keyword)){
			sql += " and store_name like '%"+keyword+"%'";
		}
		sql += " order by bill_id desc";
		return this.daoSupport.queryForPage(sql, pageNo, pageSize,bill_id);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.bill.IBillManager#bill_detail_list(java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Page store_bill_detail_list(Integer pageNo, Integer pageSize,Integer store_id) {
		return this.daoSupport.queryForPage("SELECT * FROM es_bill_detail WHERE store_id=? order by bill_time desc", pageNo, pageSize,store_id);
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.bill.IBillManager#add_bill(com.enation.app.b2b2c.core.model.bill.Bill)
	 */ 
	@Override
	public void add_bill(Integer order_id,double price) {
		//如果是父id
		if(this.daoSupport.queryForInt("select count(0) from es_order where parent_id = ?", order_id)>0){
			List<StoreOrder> orders = this.daoSupport.queryForList("select * from es_order where parent_id = ?",StoreOrder.class, order_id);
			for (StoreOrder order : orders) {
				//获取店铺id
				int storeid = order.getStore_id();
				Bill bill = (Bill) this.daoSupport.queryForObject("select * from es_bill where start_time = ?", Bill.class, DateUtil.getCurrentMonth()[0]);
				//如果结算单为空
				if(bill==null){ 
					this.editBill(order.getStore_id(),order.getNeed_pay_money(),order);
				}else{  
					this.editPortionBill(storeid,order.getNeed_pay_money(),order);
				}
			}
		}else{
			StoreOrder order = (StoreOrder) this.daoSupport.queryForObject("select * from es_order where order_id = ?",StoreOrder.class, order_id);
			//获取店铺id 
			int storeid = order.getStore_id();
			Bill bill = (Bill) this.daoSupport.queryForObject("select * from es_bill where start_time = ?", Bill.class, DateUtil.getCurrentMonth()[0]);
			//如果结算单为空
			if(bill==null){ 
				this.editBill(storeid,price,order);
			}else{  
				this.editPortionBill(storeid,price,order);
			}
		} 
	}
	

	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.store.service.bill.IBillManager#add_bill_detail(com.enation.app.b2b2c.core.store.model.bill.BillDetail)
	 */
	@Override
	public void add_bill_detail(BillDetail billDetail) {
		this.daoSupport.insert("es_bill_detail", billDetail);  
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.store.service.bill.IBillManager#get_bill_detail(java.lang.Integer)
	 */
	@Override
	public BillDetail get_bill_detail(Integer id) {
		return (BillDetail) this.daoSupport.queryForObject("select * from es_bill_detail where id=?", BillDetail.class,id);
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.store.service.bill.IBillManager#edit_billdetail_status(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)  
	public void edit_billdetail_status(Integer id, Integer status) {
		String sql="update es_bill_detail set status=? where id=?";
		this.daoSupport.execute(sql, status,id);
		if(status==BillStatusEnum.PAY.getIndex()){
			this.bill(id);
		}
	}
	

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.store.service.bill.IBillManager#bill_order_list(java.lang.Integer, java.lang.Integer, java.lang.String)
	 */
	@Override
	public Page bill_order_list(Integer pageNo,Integer pageSize,String sn) {
		String sql="select * from es_order where bill_sn = ? order by create_time desc";
		return this.daoSupport.queryForPage(sql, pageNo, pageSize, sn);
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.store.service.bill.IBillManager#bill_sell_back_list(java.lang.Integer, java.lang.Integer, java.lang.String)
	 */
	@Override
	public Page bill_sell_back_list(Integer pageNo,Integer pageSize,String sn){
		String sql="select * from es_sellback_list where bill_sn=? order by id desc";
		return this.daoSupport.queryForPage(sql, pageNo, pageSize, sn);
	}
	
	/**
	 * 结算订单
	 */
	@Transactional(propagation = Propagation.REQUIRED)  
	private void bill(Integer id){
		BillDetail billDetail=this.get_bill_detail(id);
	}
	
	/**
	 * 修改订单信息
	 * @param bill_sn 结算编号
	 * @param store_id 店铺Id
	 * @param end_time 结束时间
	 */
	private void update_order_info(String bill_sn,Integer store_id,Long end_time){
		this.daoSupport.execute("update es_order set bill_status=1,bill_sn=? where store_id=? and create_time<? and bill_status=0", bill_sn,store_id,end_time);
		this.daoSupport.execute("update es_sellback_list set bill_status=1, bill_sn=? where store_id=? and regtime<? and bill_status=0", bill_sn,store_id,end_time);
	}
	/**
	 * 添加结算详细单
	 * @param billDetail
	 */
	private void getBillDetail(BillDetail billDetail,double payprice,StoreOrder order){

		//如果没有这个结算单详情 那么添加
		if(billDetail.getId()==null){
			this.add_bill_detail(billDetail); 
			billDetail.setId(daoSupport.getLastId("es_bill_detail"));
		}
		Store store=storeManager.getStore(billDetail.getStore_id());
		//纪录订单的结算信息
		this.update_order_info(billDetail.getSn(), store.getStore_id(), billDetail.getEnd_time());
		
		/**这里要注意 
		 * 查询结果只包含本月操作，例如：上月的订单，这个月签收的，
		 * 那么结算订单的结果将设置在这个月的出账单里，
		 * 如果这个月的商品，下个月退货了，同样也只出现在下月的退款单里
		 */
//		如果钱大于0 代表是 付款，小于 等于0 代表是退款
		if(payprice>0){
			
			if(order.getPayment_type().equals("cod")){

				//收款，修改佣金，修改总价，修改订单价格
				String sql = "update es_bill_detail set cod_price=cod_price+?, price = price+? , bill_price = bill_price+? ,commi_price = commi_price+? where id = ?";
				this.daoSupport.execute(sql, 
						payprice,
						payprice,
						CurrencyUtil.mul(-payprice, store.getCommission()/100),
						CurrencyUtil.mul(payprice, store.getCommission()/100),
						billDetail.getId());
			}else{
			
			//	收款，修改佣金，修改总价，修改订单价格
				String sql = "update es_bill_detail set price = price+? , bill_price = bill_price+?+? ,commi_price = commi_price+? where id = ?";
				this.daoSupport.execute(sql, payprice,
						CurrencyUtil.mul(-payprice, store.getCommission()/100),
						payprice,
						CurrencyUtil.mul(payprice, store.getCommission()/100),
						billDetail.getId());
			}
		}else{
			if(order.getPayment_type().equals("cod")){
				//退款，修改 总金额 店铺金额，退还金额，退还佣金
				String sql = "update es_bill_detail set cod_price=cod_price+?, bill_price = bill_price+? , " +
						"returned_price=returned_price+? ,returned_commi_price = returned_commi_price+? where id = ?";
				this.daoSupport.execute(sql, 
						payprice,
						CurrencyUtil.mul(-payprice, store.getCommission()/100),
						-payprice,
						CurrencyUtil.mul(-payprice, store.getCommission()/100),
						billDetail.getId());
			}else{
				
				//退款，修改 总金额 店铺金额，退还金额，退还佣金
				String sql = "update es_bill_detail set  bill_price = bill_price+?+? , " +
						"returned_price=returned_price+? ,returned_commi_price = returned_commi_price+? where id = ?";
				this.daoSupport.execute(sql, 
						CurrencyUtil.mul(-payprice, store.getCommission()/100),
						payprice,
						-payprice,
						CurrencyUtil.mul(-payprice, store.getCommission()/100),
						billDetail.getId());
			}
		}
 
	}
	
	/**
	 * 创建结算单
	 * 循环店铺表算出每家店铺的结算信息，创建出结算的详细单
	 * 然后进行相加算出此期的结算金额信息.
	 * @param bill 结算单
	 */
	private void editBill(Integer store_id,double price,StoreOrder order){
		 
		Bill bill = new Bill();
		bill.setCommi_price(0.0);
		bill.setOrder_price(0.0);
		bill.setPrice(0.0);
		bill.setReturned_commi_price(0.0);
		bill.setReturned_price(0.0); 
		bill.setCod_price(0.0); 
		Long[] time=DateUtil.getCurrentMonth();
		Long start_time=time[0];
		Long end_time=time[1]; 
		bill.setName(DateUtil.getDateline()+"");
		bill.setStart_time(start_time);
		bill.setEnd_time(end_time); 
		this.daoSupport.insert("es_bill", bill);
		bill.setBill_id(this.daoSupport.getLastId("es_bill"));
 
		this.editPortionBill(store_id,price,order);
	}
	
	/**
	 * 结算单
	 * 循环店铺表算出每家店铺的结算信息，创建出结算的详细单
	 * 然后进行相加算出此期的结算金额信息.
	 * @param bill 结算单
	 */
	private void editPortionBill(Integer store_id,double price,StoreOrder order){  
		//获取最新一期的结算单
		Bill bill= (Bill) this.daoSupport.queryForObject("select * from  es_bill where start_time = ?", Bill.class, DateUtil.getCurrentMonth()[0]);
		
		//结算详细单价格信息 
			//创建结算详细
		Store store = storeManager.getStore(store_id); 
		BillDetail billDetail = (BillDetail) daoSupport .queryForObject(
						"select * from es_bill_detail where store_id = ? and bill_id = ?",
						BillDetail.class, store_id,bill.getBill_id());
		//如果 订单详情为空
		if (billDetail == null) {
			billDetail = new BillDetail();
			billDetail.setStore_name(store.getStore_name());
			billDetail.setBill_id(bill.getBill_id());
			billDetail.setSn(bill.getBill_id()+"-"+store_id );
			billDetail.setStore_id(store.getStore_id());
			billDetail.setStatus(0);
			billDetail.setStart_time(bill.getStart_time());
			billDetail.setEnd_time(bill.getEnd_time());
			billDetail.setBill_price(0.0);
			billDetail.setPrice(0.0);
			billDetail.setReturned_commi_price(0.0);
			billDetail.setReturned_price(0.0);  
			billDetail.setCommi_price(0.0); 
			billDetail.setCod_price(0.0); 
		} 

		billDetail.setBill_time(DateUtil.getDateline()); 
		// 创建结算详细单
		getBillDetail(billDetail,price,order); 
		
		//如果金额大于0 则是收款
		if(price>0){
			//如果是货到付款
			if(order.getPayment_type().equals("cod")){
				//收款，修改佣金，修改总价，修改订单价格
						String sql = "update es_bill set price = price+? ,cod_price = cod_price+?, order_price = order_price+? ,commi_price = commi_price+? where bill_id = ?";
						this.daoSupport.execute(sql, 
								-CurrencyUtil.mul(price, store.getCommission()/100),//佣金
								price,//货到付款金额
								price,//订单金额
								CurrencyUtil.mul(price, store.getCommission()/100),//佣金
								bill.getBill_id());
					
			}else{
				//收款，修改佣金，修改总价，修改订单价格
				String sql = "update es_bill set price = price+?+? , order_price = order_price+? ,commi_price = commi_price+? where bill_id = ?";
				this.daoSupport.execute(sql, 
						price,
						-CurrencyUtil.mul(price, store.getCommission()/100),//订单金额-佣金
						price,//订单金额
						CurrencyUtil.mul(price, store.getCommission()/100),//佣金
						bill.getBill_id());
			}
			
			
		 }else{

			if (order.getPayment_type().equals("cod")) {
				// 退款，修改 总金额 退还金额，退还佣金
				String sql = "update es_bill set price = price+? , returned_price=returned_price+? ,returned_commi_price = returned_commi_price+? where bill_id = ?";
				this.daoSupport.execute(sql,
						CurrencyUtil.mul(price, store.getCommission() / 100),//增加收取的佣金
						-price,//退款金额增加
						CurrencyUtil.mul(-price, store.getCommission() / 100),//退款佣金
						bill.getBill_id());
			} else {
				// 退款，修改 总金额 退还金额，退还佣金
				String sql = "update es_bill set price = price+?+? , returned_price=returned_price+? ,returned_commi_price = returned_commi_price+? where bill_id = ?";
				this.daoSupport.execute(sql,
						price,
						CurrencyUtil.mul(price, store.getCommission() / 100),
						-price,
						CurrencyUtil.mul(-price, store.getCommission() / 100),
						bill.getBill_id());
			}
		 
		 } 
	}

	/**
	 * 更新状态
	 */
	@Override
	public void editBillStatus(Integer status) {
		this.daoSupport.execute("update es_bill_detail set status=1 where status=0 and end_time<="+ DateUtil.getCurrentMonth()[1] + "and start_time>="+DateUtil.getCurrentMonth()[0]);
	}
}
