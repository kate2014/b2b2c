package com.enation.app.b2b2c.core.order.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.b2b2c.component.bonus.model.StoreBonusType;
import com.enation.app.b2b2c.component.bonus.service.B2b2cBonusSession;
import com.enation.app.b2b2c.component.bonus.service.IB2b2cBonusManager;
import com.enation.app.b2b2c.core.goods.service.StoreCartContainer;
import com.enation.app.b2b2c.core.goods.service.StoreCartKeyEnum;
import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.b2b2c.core.order.model.StoreOrder;
import com.enation.app.b2b2c.core.order.service.IStoreOrderManager;
import com.enation.app.b2b2c.core.order.service.cart.IStoreCartManager;
import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.service.IMemberManager;
import com.enation.app.base.core.service.ISettingService;
import com.enation.app.shop.ShopApp;
import com.enation.app.shop.component.bonus.model.MemberBonus;
import com.enation.app.shop.core.order.model.Order;
import com.enation.app.shop.core.order.model.support.CartItem;
import com.enation.app.shop.core.order.model.support.OrderPrice;
import com.enation.app.shop.core.order.plugin.cart.CartPluginBundle;
import com.enation.app.shop.core.order.plugin.order.OrderPluginBundle;
import com.enation.app.shop.core.order.service.ICartManager;
import com.enation.app.shop.core.order.service.IOrderFlowManager;
import com.enation.app.shop.core.order.service.OrderStatus;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;

/**
 * 多店铺订单管理类<br>
 * 负责多店铺订单的创建、查询
 * 
 * @author kingapex
 * @version 2.0: 对价格逻辑进行改造 2015年8月21日下午1:49:27
 * 
 * @author xulipeng 2016年03月03日 改造springMVC
 * @version 1.1 Kanon 2016年07月06日 修改会员订单列表查询方法
 */

@SuppressWarnings("rawtypes")
@Service("storeOrderManager")
public class StoreOrderManager implements IStoreOrderManager {

	@Autowired
	private IDaoSupport daoSupport;
	@Autowired
	private ICartManager cartManager;
	@Autowired
	private IStoreMemberManager storeMemberManager;
	@Autowired
	private IMemberManager memberManager;
	@Autowired
	private ISettingService settingService;
	@Autowired
	private CartPluginBundle cartPluginBundle;
	@Autowired
	private IOrderFlowManager OrderFlowManager;
	@Autowired
	private OrderPluginBundle orderPluginBundle;// 订单插件桩
	@Autowired
	private IB2b2cBonusManager b2b2cBonusManager;
	
	/** 店铺购物车管理接口 add_by DMRain 2016-7-13 */
	@Autowired
	private IStoreCartManager storeCartManager;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.b2b2c.core.service.order.IStoreOrderManager#createOrder(
	 * com.enation.app.shop.core.model.Order, java.lang.String,
	 * java.lang.String[])
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Order createOrder(Order order, String sessionid) {
		// 读取所有的购物项，用于创建主订单
		List<CartItem> cartItemList = this.cartManager.listGoods(sessionid);

		if (cartItemList == null || cartItemList.size() == 0) {
			throw new RuntimeException("购物车不能为空");
		}

		// 商家能否购买自己的商品
		final String CAN_BY_SELF = settingService.getSetting("store", "buy_self_auth");
		if (!"1".equals(CAN_BY_SELF)) {
			Member nowaMember = UserConext.getCurrentMember();
			if (nowaMember != null) {
				for (CartItem cartItem : cartItemList) {
					if (cartItem != null && cartItem.getIs_check() == 1) {
						Member belongTo = memberManager.getByGoodsId(cartItem.getGoods_id());
						if (belongTo != null && belongTo.getMember_id().equals(nowaMember.getMember_id())) {
							throw new RuntimeException("抱歉！您不能购买自己的商品：" + cartItem.getName() + "。");
						}
					}
				}
			}
		}

		// 调用核心api计算总订单的价格，商品价：所有商品，商品重量：
		OrderPrice orderPrice = cartManager.countPrice(cartItemList, order.getShipping_id(), "" + order.getRegionid());

		// 激发总订单价格事件
		orderPrice = this.cartPluginBundle.coutPrice(orderPrice);

		// 设置订单价格，自动填充好各项价格，商品价格，运费等
		order.setOrderprice(orderPrice);
		order.setWeight(orderPrice.getWeight());

		// 调用核心api创建主订单
		Order mainOrder = this.OrderFlowManager.add(order, new ArrayList<CartItem>(), sessionid);

		// 创建子订单
		this.createChildOrder(mainOrder, sessionid);

		// 创建完子订单再清空session
		cartManager.clean(sessionid);
		StoreCartContainer.cleanSession();
		//清空所有已使用的优惠券
		B2b2cBonusSession.cleanAll();

		// 返回主订单
		return mainOrder;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.app.b2b2c.core.service.order.IStoreOrderManager#
	 * storeOrderList(java.lang.Integer, java.lang.Integer, java.util.Map)
	 */
	@Override
	public Page storeOrderList(Integer pageNo, Integer pageSize,Integer storeid,Map map) {
		String order_state=String.valueOf(map.get("order_state"));
		String keyword=String.valueOf(map.get("keyword"));
		String buyerName=String.valueOf(map.get("buyerName"));
		String startTime=String.valueOf(map.get("startTime"));
		String endTime=String.valueOf(map.get("endTime"));
		//在该处获取订单中包含商品
		String goods=String.valueOf(map.get("goods"));	
		//由于需要多关联一张表，故在此对sql语句进行判断，判断依据为商品名称是否为空
		//定义初始变量sql，并分别进行赋值
		StringBuffer sql=null;
		if(!StringUtil.isEmpty(goods)&&!goods.equals("null")){
			sql=new StringBuffer("select o.*,m.uname from es_order o left join es_member m on o.member_id = m.member_id left join es_order_items  s on s.order_id=o.order_id  left join es_goods g on s.goods_id = g.goods_id where o.store_id =" + storeid + 
					" and o.disabled=0 and m.disabled!=1  and g.name like '%"+goods+"%' ");
		}else{
			sql=new StringBuffer("select o.*,m.uname from es_order o left join es_member m on o.member_id = m.member_id where o.store_id =" + storeid + " and o.disabled=0 and m.disabled!=1 ");
		}
//		StringBuffer sql =new StringBuffer("select o.*,m.uname from es_order o left join es_member m on o.member_id = m.member_id where o.store_id =" + storeid + " and o.disabled=0 and m.disabled!=1 "
//				+ "left join es_order_items  s on s.order_id=o.order_id and ");
		if(!StringUtil.isEmpty(order_state)&&!order_state.equals("all")){
			if(order_state.equals("wait_ship") ){ //对待发货的处理
				sql.append(" and ( ( payment_type!='cod' and payment_id!=8  and  o.status="+OrderStatus.ORDER_PAY +") ");//非货到付款的，要已结算才能发货
				sql.append(" or ( payment_type='cod' and  status="+OrderStatus.ORDER_NOT_PAY +")) ");//货到付款的，新订单（已确认的）就可以发货
			//等待收款 （货到付款）
			}else if(order_state.equals("wait_pay") ){
				sql.append(" and ( status="+OrderStatus.ORDER_ROG+" and payment_type='cod')");//货到付款的要发货或收货后才能结算 
			//等待收货
			}else if(order_state.equals("wait_rog") ){ 
				sql.append(" and status="+OrderStatus.ORDER_SHIP  ); 
			}else{
				sql.append(" and status="+order_state);
			}
		}
		if(!StringUtil.isEmpty(keyword)&&!keyword.equals("null")){
			sql.append(" AND o.sn like '%" + keyword + "%'");
		}
		if(!StringUtil.isEmpty(buyerName)&&!buyerName.equals("null")){
			sql.append(" AND m.uname like '%"+buyerName+"%'");
		}
		if(!StringUtil.isEmpty(startTime)&&!startTime.equals("null")){
			sql.append(" AND o.create_time >"+DateUtil.getDateline(startTime));
		}
		if(!StringUtil.isEmpty(endTime)&&!endTime.equals("null")){
			
			//2015-11-04 add by sylow 
			endTime += " 23:59:59";
			sql.append(" AND o.create_time <"+DateUtil.getDateline(endTime, "yyyy-MM-dd HH:mm:ss"));
		}
		sql.append (" order by o.create_time desc");
		
		//在该处输出sql进行测试
		
        //System.out.println(sql.toString());
		Page rpage = this.daoSupport.queryForPage(sql.toString(),pageNo, pageSize, Order.class);
		 
		return rpage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.app.b2b2c.core.service.order.IStoreOrderManager#
	 * storeOrderList(java.lang.Integer)
	 */
	@Override
	public List storeOrderList(Integer parent_id) {
		StringBuffer sql = new StringBuffer("SELECT * from es_order WHERE  disabled=0 AND parent_id=" + parent_id);
		return this.daoSupport.queryForList(sql.toString(), StoreOrder.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.b2b2c.core.service.order.IStoreOrderManager#get(java.lang
	 * .Integer)
	 */
	@Override
	public StoreOrder get(Integer orderId) {
		String sql = "select * from es_order where order_id=?";
		StoreOrder order = (StoreOrder) this.daoSupport.queryForObject(sql, StoreOrder.class, orderId);
		return order;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.b2b2c.core.service.order.IStoreOrderManager#saveShipInfo(
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, int)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean saveShipInfo(String remark, String ship_day, String ship_name, String ship_tel, String ship_mobile,
			String ship_zip, int orderid) {
		Order order = this.get(orderid);
		try {
			if (ship_day != null && !StringUtil.isEmpty(ship_day) && !ship_day.equals(order.getShip_day())) {
				this.daoSupport.execute("update es_order set ship_day=?  where order_id=?", ship_day, orderid);
			}
			if (remark != null && !remark.equals("undefined")
					&& !remark.equals(order.getRemark())) {
				this.daoSupport.execute("update es_order set remark= ?  where order_id=?", remark, orderid);
			}
			if (ship_name != null && !StringUtil.isEmpty(ship_name) && !ship_name.equals(order.getShip_name())) {
				this.daoSupport.execute("update es_order set ship_name=?  where order_id=?", ship_name, orderid);
			}
			if (ship_tel != null && !StringUtil.isEmpty(ship_tel) && !ship_tel.equals(order.getShip_tel())) {
				this.daoSupport.execute("update es_order set ship_tel=?  where order_id=?", ship_tel, orderid);
			}
			if (ship_mobile != null && !StringUtil.isEmpty(ship_mobile)
					&& !ship_mobile.equals(order.getShip_mobile())) {
				this.daoSupport.execute("update es_order set ship_mobile=?  where order_id=?", ship_mobile, orderid);
			}
			if (ship_zip != null && !StringUtil.isEmpty(ship_zip) && !ship_zip.equals(order.getShip_zip())) {
				this.daoSupport.execute("update es_order set ship_zip=?  where order_id=?", ship_zip, orderid);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.app.b2b2c.core.order.service.IStoreOrderManager#
	 * pageBuyerOrders(int, int, java.lang.String, java.lang.String)
	 */
	@Override
	public Page pageBuyerOrders(int pageNo, int pageSize, String status, String keyword) {

		/**
		 * 准备查询的参数，将来要转换为Object[] 传给jdbc
		 */
		List argsList = new ArrayList();

		/**
		 * 查询当前会员的订单
		 */
		StoreMember member = storeMemberManager.getStoreMember();
		StringBuffer sql = new StringBuffer("SELECT * FROM es_order o where o.parent_id is NOT NULL and  member_id=?");
		argsList.add(member.getMember_id());

		/**
		 * 按状态查询
		 */
		if (!StringUtil.isEmpty(status)) {
			// 等待收款 （货到付款）
			if (status.equals("wait_pay")) {
				sql.append(" and ( ( payment_type!='cod' and  status=" + OrderStatus.ORDER_CONFIRM + ") ");// 非货到付款的，未付款状态的可以结算
				sql.append(" or ( payment_type='cod' and   status=" + OrderStatus.ORDER_ROG + "  ) )");// 货到付款的要发货或收货后才能结算
				// 等待收货
			} else if (status.equals("wait_rog")) {
				sql.append(" and status=" + OrderStatus.ORDER_SHIP);
			} else {
				if("2".equals(status)){
					sql.append(" and (payment_type='cod' and status=1) or status=" + status);
				}else{
					sql.append(" and status=" + status);
				}
			}
		}

		/**
		 * 按关键字查询
		 */
		if (!StringUtil.isEmpty(keyword)) {
			sql.append(
					" AND order_id in (SELECT i.order_id FROM es_order_items i INNER JOIN es_order o ON i.order_id=o.order_id WHERE o.member_id=?"
							+ " AND (i.name like ? OR o.sn LIKE ?))");

			argsList.add(member.getMember_id());
			argsList.add("%" + keyword + "%"); // 将关键字做为name参数查询 压入参数list
			argsList.add("%" + keyword + "%"); // 将关键字做为sn参数查询 压入参数list

		}

		sql.append(" order by o.create_time desc");

		/**
		 * 将参数list 转为Object[]
		 */
		int size = argsList.size();
		Object[] args = argsList.toArray(new Object[size]);
		/**
		 * 分页查询买家订单
		 */
		Page webPage = this.daoSupport.queryForPage(sql.toString(), pageNo, pageSize, Order.class, args);

		return webPage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.b2b2c.core.service.order.IStoreOrderManager#pageOrders(
	 * int, int, java.lang.String, java.lang.String)
	 */
	@Override
	public Page pageChildOrders(int pageNo, int pageSize, String status, String keyword) {
		StoreMember member = storeMemberManager.getStoreMember();

		StringBuffer sql = new StringBuffer(
				"select * from es_order where member_id = '" + member.getMember_id() + "' and disabled=0");
		if (!StringUtil.isEmpty(status)) {
			int statusNumber = -999;
			statusNumber = StringUtil.toInt(status);
			// 等待付款的订单 按付款状态查询
			if (statusNumber == 0) {
				sql.append(" AND status!=" + OrderStatus.ORDER_CANCELLATION + " AND pay_status=" + OrderStatus.PAY_NO);
			} else {
				sql.append(" AND status='" + statusNumber + "'");
			}
		}
		if (!StringUtil.isEmpty(keyword)) {
			sql.append(
					" AND order_id in (SELECT i.order_id FROM es_order_items i LEFT JOIN es_order o ON i.order_id=o.order_id WHERE o.member_id='"
							+ member.getMember_id() + "' AND i.name like '%" + keyword + "%')");
		}
		sql.append(" AND parent_id is NOT NULL order by create_time desc");
		Page rpage = this.daoSupport.queryForPage(sql.toString(), pageNo, pageSize, Order.class);
		return rpage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.app.b2b2c.core.service.order.IStoreOrderManager#
	 * getStoreOrderNum(int)
	 */
	@Override
	public int getStoreOrderNum(Integer[] struts) {
		StoreMember member = storeMemberManager.getStoreMember();
		String sql ="select count(order_id) from es_order o where o.store_id ="+member.getStore_id()+" and o.disabled=0";
		System.out.println("");
		List<Integer> statusList=new ArrayList<Integer>(Arrays.asList(struts));
		if(statusList.size()>0){
			if(statusList.contains(-999)){
				sql += " AND o.status != ?";
			}else{
				sql +=" AND (";
				int i=0;
				for (int status : statusList) {
					sql +=" o.status=?";
					if(i==struts.length-1){
						sql+=")";
					}else{
						sql += " OR ";
					}
					i++;
				}
			}
		}
		
		if( statusList.contains(-999)){
			return this.daoSupport.queryForInt(sql,OrderStatus.ORDER_COMPLETE);
			
		}else
		{
			return this.daoSupport.queryForInt(sql,struts);

		}
	

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.b2b2c.core.service.order.IStoreOrderManager#get(java.lang
	 * .String)
	 */
	@Override
	public StoreOrder get(String ordersn) {
		String sql = "select * from es_order where sn='" + ordersn + "'";
		StoreOrder order = (StoreOrder) this.daoSupport.queryForObject(sql, StoreOrder.class);
		return order;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.b2b2c.core.service.order.IStoreOrderManager#listOrder(
	 * java.util.Map, int, int, java.lang.String, java.lang.String)
	 */
	@Override
	public Page listOrder(Map map, int page, int pageSize, String other, String order) {

		String sql = createTempSql(map, other, order);
		Page webPage = this.daoSupport.queryForPage(sql, page, pageSize);

		orderPluginBundle.filterOrderPage(webPage);// 对订单查询结果进行过滤

		return webPage;
	}

	@Override
	public Map getStatusJson() {
		Map orderStatus = new HashMap();

		orderStatus.put("" + OrderStatus.ORDER_NOT_PAY, OrderStatus.getOrderStatusText(OrderStatus.ORDER_NOT_PAY));
		orderStatus.put("" + OrderStatus.ORDER_PAY, OrderStatus.getOrderStatusText(OrderStatus.ORDER_PAY));
		orderStatus.put("" + OrderStatus.ORDER_SHIP, OrderStatus.getOrderStatusText(OrderStatus.ORDER_SHIP));
		orderStatus.put("" + OrderStatus.ORDER_ROG, OrderStatus.getOrderStatusText(OrderStatus.ORDER_ROG));
		orderStatus.put("" + OrderStatus.ORDER_COMPLETE, OrderStatus.getOrderStatusText(OrderStatus.ORDER_COMPLETE));
		orderStatus.put("" + OrderStatus.ORDER_CANCELLATION,
				OrderStatus.getOrderStatusText(OrderStatus.ORDER_CANCELLATION));

		return orderStatus;
	}

	@Override
	public Map getpPayStatusJson() {
		Map pmap = new HashMap();
		pmap.put("" + OrderStatus.PAY_NO, OrderStatus.getPayStatusText(OrderStatus.PAY_NO));
		pmap.put("" + OrderStatus.PAY_YES, OrderStatus.getPayStatusText(OrderStatus.PAY_YES));
		pmap.put("" + OrderStatus.PAY_PARTIAL_PAYED, OrderStatus.getPayStatusText(OrderStatus.PAY_PARTIAL_PAYED));

		return pmap;
	}

	@Override
	public Map getShipJson() {
		Map map = new HashMap();
		map.put("" + OrderStatus.SHIP_NO, OrderStatus.getShipStatusText(OrderStatus.SHIP_NO));
		map.put("" + OrderStatus.SHIP_YES, OrderStatus.getShipStatusText(OrderStatus.SHIP_YES));
		map.put("" + OrderStatus.SHIP_ROG, OrderStatus.getShipStatusText(OrderStatus.SHIP_ROG));
		return map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.app.b2b2c.core.order.service.IStoreOrderManager#
	 * orderStatusNum(java.lang.Integer)
	 */
	@Override
	public Integer orderStatusNum(Integer status) {
		StoreMember member = storeMemberManager.getStoreMember();
		if (status == 99) {
			String sql = "select count(0) from es_order where member_id=? and parent_id is not null";
			return this.daoSupport.queryForInt(sql, member.getMember_id());
		} else if(status == 1){
			String sql = "select count(0) from es_order where status =? and member_id=? and parent_id is not null and is_online=1";
			return this.daoSupport.queryForInt(sql, status, member.getMember_id());
		}else {
			String sql = "select count(0) from es_order where status =? and member_id=? and parent_id is not null";
			return this.daoSupport.queryForInt(sql, status, member.getMember_id());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.app.b2b2c.core.order.service.IStoreOrderManager#
	 * getStoreGoodsNum(int)
	 */
	@Override
	public Integer getStoreGoodsNum(int store_id) {
		String sql = "select count(0) from es_goods where store_id=?";
		return this.daoSupport.queryForInt(sql, store_id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.b2b2c.core.order.service.IStoreOrderManager#saveShipNo(
	 * java.lang.Integer[], java.lang.Integer, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void saveShipNo(Integer[] order_id, Integer logi_id, String logi_name, String shipNo) {
		Map map = new HashMap();
		map.put("ship_no", shipNo);
		map.put("logi_id", logi_id);
		map.put("logi_name", logi_name);

		this.daoSupport.update("es_order", map, "order_id=" + order_id[0]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.b2b2c.core.order.service.IStoreOrderManager#censusState()
	 */
	@Override
	public Map censusState() {
		// 构造一个返回值Map，并将其初始化：各种订单状态的值皆为0
		Map<String, Integer> stateMap = new HashMap<String, Integer>(7);
		String[] states = { "cancel_ship", "cancel_pay", "pay", "ship", "complete", "allocation_yes" };
		for (String s : states) {
			stateMap.put(s, 0);
		}

		// 分组查询、统计订单状态
		String sql = "select count(0) num,status  from es_order where disabled = 0 AND parent_id is NOT NULL group by status";
		List<Map<String, Integer>> list = this.daoSupport.queryForList(sql, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, Integer> map = new HashMap<String, Integer>();
				map.put("status", rs.getInt("status"));
				map.put("num", rs.getInt("num"));
				return map;
			}
		});
		//
		// // 将list转为map
		for (Map<String, Integer> state : list) {
			stateMap.put(this.getStateString(state.get("status")), state.get("num"));
		}

		sql = "select count(0) num  from es_order where disabled = 0  and status=0 AND parent_id is NOT NULL ";
		int count = this.daoSupport.queryForInt(sql);
		stateMap.put("wait", count);

		sql = "select count(0) num  from es_order where disabled = 0  AND parent_id is NOT NULL ";
		sql += " and ( ( payment_type!='cod' and  status=" + OrderStatus.ORDER_CONFIRM + ") ";// 非货到付款的，未付款状态的可以结算
		// sql+=" or ( status!="+OrderStatus.ORDER_NOT_PAY+" and
		// pay_status!="+OrderStatus.PAY_NO +")" ; //此语句会影响待结算订单的查询，暂时先注释掉 add
		// by DMRain 2016-7-14
		sql += " or ( payment_type='cod' and  (status=" + OrderStatus.ORDER_ROG
				+ " )  ) )";// 货到付款的要发货或收货后才能结算
		count = this.daoSupport.queryForInt(sql);
		stateMap.put("not_pay", count);

		sql = "select count(0) from es_order where disabled=0  and ( ( payment_type!='cod' and payment_id!=8  and  status=2)  or ( payment_type='cod' and  status=1)) AND parent_id is NOT NULL ";
		count = this.daoSupport.queryForInt(sql);
		stateMap.put("allocation_yes", count);

		this.putSelfStoreStateMap(stateMap);
		return stateMap;
	}

	private void putSelfStoreStateMap(Map<String, Integer> stateMap) {
		
		Map<String, Integer> newStateMap = new HashMap<String, Integer>(7);
		String[] states = { "self_cancel_ship", "self_cancel_pay", "self_pay", "self_ship", "self_complete", "self_allocation_yes" };
		for (String s : states) {
			newStateMap.put(s, 0);
		}

		// 分组查询、统计订单状态
		String sql = "select count(0) num,status  from es_order where disabled = 0 AND parent_id is NOT NULL AND store_id ="+ShopApp.self_storeid+" group by status";
		List<Map<String, Integer>> list = this.daoSupport.queryForList(sql, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, Integer> map = new HashMap<String, Integer>();
				map.put("status", rs.getInt("status"));
				map.put("num", rs.getInt("num"));
				return map;
			}
		});
		//
		// // 将list转为map
		for (Map<String, Integer> state : list) {
			newStateMap.put(this.getSelfStateString(state.get("status")), state.get("num"));
		}

		sql = "select count(0) num  from es_order where disabled = 0  and status=0 AND parent_id is NOT NULL AND store_id ="+ShopApp.self_storeid;
		int count = this.daoSupport.queryForInt(sql);
		newStateMap.put("self_wait", count);

		sql = "select count(0) num  from es_order where disabled = 0  AND parent_id is NOT NULL AND store_id ="+ShopApp.self_storeid;
		sql += " and ( ( payment_type!='cod' and  status=" + OrderStatus.ORDER_CONFIRM + ") ";// 非货到付款的，未付款状态的可以结算
		// sql+=" or ( status!="+OrderStatus.ORDER_NOT_PAY+" and
		// pay_status!="+OrderStatus.PAY_NO +")" ; //此语句会影响待结算订单的查询，暂时先注释掉 add
		// by DMRain 2016-7-14
		sql += " or ( payment_type='cod' and  (status=" + OrderStatus.ORDER_ROG
				+ " )  ) )";// 货到付款的要发货或收货后才能结算
		count = this.daoSupport.queryForInt(sql);
		newStateMap.put("self_not_pay", count);

		sql = "select count(0) from es_order where disabled=0  and ( ( payment_type!='cod' and payment_id!=8  and  status=2)  or ( payment_type='cod' and  status=1)) AND parent_id is NOT NULL AND store_id ="+ShopApp.self_storeid;
		count = this.daoSupport.queryForInt(sql);
		newStateMap.put("self_allocation_yes", count);
		
		stateMap.putAll(newStateMap);
		
	}

	
	
	private String getSelfStateString(Integer state) {
		String str = null;
		switch (state.intValue()) {
		case 0:
			str = "self_not_pay";
			break;
		case 1:
			str = "self_confirm";
			break;
		case 2:
			str = "self_pay";
			break;
		case 3:
			str = "self_ship";
			break;
		case 4:
			str = "self_allocation_yes";
			break;
		case 5:
			str = "self_complete";
			break;
		case 6:
			str = "self_order_cancel";
			break;
		case 7:
			str = "self_order_maintanance";
			break;
		default:
			str = null;
			break;
		}
		return str;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.app.b2b2c.core.order.service.IStoreOrderManager#
	 * getStoreSellbackOrder()
	 */
	@Override
	public int getStoreSellbackOrder(Integer[] statusArray) {
		StoreMember member = storeMemberManager.getStoreMember();
		//String sql = "select count(0) from es_sellback_list where store_id = ?";
		StringBuffer sqlsb=new StringBuffer("select count(0) from es_sellback_list where store_id = ? ");
		List paramlist=new ArrayList();
		paramlist.add(member.getStore_id());
		if(statusArray.length!=0){
			for (int i=0;i<statusArray.length;i++) {
				if (i==0){
					sqlsb.append(" and ( ");
				}else{
					sqlsb.append(" or ");
				}
				sqlsb.append(" tradestatus = ?");
				if(i==statusArray.length-1){
					sqlsb.append(" ) ");
				}
				paramlist.add(statusArray[i]);
			}
		}
		
		return this.daoSupport.queryForInt(sqlsb.toString(),paramlist.toArray());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.app.b2b2c.core.order.service.IStoreOrderManager#
	 * getCancelApplicationList(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Page getCancelApplicationList(Integer pageNo, Integer pageSize) {
		StoreMember member = storeMemberManager.getStoreMember();
		String sql = "select * from es_order where is_cancel=1 AND parent_id is NOT NULL AND store_id = ? order by order_id desc ";
		return this.daoSupport.queryForPage(sql, pageNo, pageSize, member.getStore_id());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.app.b2b2c.core.order.service.IStoreOrderManager#
	 * cancelApplicationList(java.lang.Integer, java.lang.Integer,
	 * java.lang.Integer)
	 */
	@Override
	public Page cancelApplicationList(Integer pageNo, Integer pageSize, Integer is_self) {
		if (is_self == 1) {
			String sql = "select * from es_order where is_cancel=1 AND parent_id is NOT NULL AND store_id = ? order by order_id desc ";
			return this.daoSupport.queryForPage(sql, pageNo, pageSize, ShopApp.self_storeid);
		} else {
			String sql = "select * from es_order where is_cancel=1 AND parent_id is NOT NULL  order by order_id desc ";
			return this.daoSupport.queryForPage(sql, pageNo, pageSize);
		}
	}

	/**
	 * 根据订单状态值获取状态字串，如果状态值不在范围内反回null。
	 * 
	 * @param state
	 * @return
	 */
	private String getStateString(Integer state) {
		String str = null;
		switch (state.intValue()) {
		case 0:
			str = "not_pay";
			break;
		case 1:
			str = "confirm";
			break;
		case 2:
			str = "pay";
			break;
		case 3:
			str = "ship";
			break;
		case 4:
			str = "allocation_yes";
			break;
		case 5:
			str = "complete";
			break;
		case 6:
			str = "order_cancel";
			break;
		case 7:
			str = "order_maintanance";
			break;
		default:
			str = null;
			break;
		}
		return str;
	}

	/**
	 * 创建店铺子订单
	 * 
	 * @param order
	 *            主订单
	 * @param sessionid
	 *            用户sessionid
	 * @param shippingIds
	 *            配送方式数组,是按在结算页中的店铺顺序形成
	 */
	private void createChildOrder(Order order, String sessionid) {

		
		// 获取以店铺id分类的购物车列表
		List<Map> storeGoodsList = StoreCartContainer.getSelectStoreCartListFromSession();

		int num = 1;

		// 以店铺分单位循环购物车列表
		for (Map map : storeGoodsList) {
			// 当前店铺的配送方式
			Integer shippingId = StringUtil.toInt(map.get(StringUtil.toString(StoreCartKeyEnum.shiptypeid)),false);

			// 先将主订单的信息copy一份
			StoreOrder storeOrder = this.copyOrder(order);
			
			// 如果copy属性异常，则抛出异常
			if (storeOrder == null) {
				throw new RuntimeException("创建子订单出错，原因为：beanutils copy属性出错。");
			}

			// 获取此店铺id
			int store_id = (Integer) map.get(StoreCartKeyEnum.store_id.toString());

			// 获取店铺名称
			String store_name = (String) map.get(StoreCartKeyEnum.store_name.toString());

			// 设置订单为未结算
			storeOrder.setBill_status(0);

			// 设置店铺的id
			storeOrder.setStore_id(store_id);

			// 店铺名称
			storeOrder.setStore_name(store_name);

			// 配送方式id
			storeOrder.setShipping_id(shippingId);

			// 设置父订id
			storeOrder.setParent_id(order.getOrder_id());

			// 取得此店铺的购物列表
			List itemlist = (List) map.get(StoreCartKeyEnum.goodslist.toString());

			// 调用核心api计算总订单的价格，商品价：所有商品，商品重量：
			OrderPrice orderPrice = (OrderPrice) map.get(StoreCartKeyEnum.storeprice.toString());
			
			//获取优惠券张数
			StoreBonusType bonus = this.b2b2cBonusManager.get(orderPrice.getBonus_id());
			if(bonus != null){
				if(bonus.getCreate_num() > bonus.getReceived_num()){
					orderPrice.setBonus_id(bonus.getType_id());
				}else{
					//如果领取的优惠券大于创建的优惠券
					orderPrice.setBonus_id(0);
				}
			}
			
			//从session中读取此店铺已使用的优惠券的金额
			MemberBonus memberBonus = B2b2cBonusSession.getB2b2cBonus(store_id);
			if(memberBonus!=null){
				orderPrice.setDiscountPrice(memberBonus.getType_money());
			}
			
			//如果优惠金额后订单价格小于0	by_xulipeng  2017年01月18日 
			if(orderPrice.getNeedPayMoney()<=0){
				orderPrice.setNeedPayMoney(0d);
			}
			
			// 设置订单价格，自动填充好各项价格，商品价格，运费等
			storeOrder.setOrderprice(orderPrice);

			// 设置为子订单
			storeOrder.setIs_child_order(true);
			storeOrder.setSn(order.getSn() + "-" + num);
			// 调用订单核心类创建子订单
			this.OrderFlowManager.add(storeOrder, itemlist, sessionid);

			num++;
		}

	}

	/**
	 * copy一个订单的属性 生成新的订单
	 * 
	 * @param order
	 *            主订单
	 * @return 新的子订单
	 */
	private StoreOrder copyOrder(Order order) {
		StoreOrder store_order = new StoreOrder();
		try {
			BeanUtils.copyProperties(store_order, order);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return store_order;
	}

	/**
	 * 生成查询sql
	 * 
	 * @param map
	 * @param sortField
	 * @param sortType
	 * @return
	 */
	private String createTempSql(Map map, String sortField, String sortType) {
		Integer stype = map.get("stype") == null ? null : Integer.parseInt(map.get("stype").toString());

		String keyword = (String) map.get("keyword");

		String sn = (String) map.get("sn");

		String orderstate = (String) map.get("order_state");// 订单状态特殊查询

		String start_time = (String) map.get("start_time");

		String end_time = (String) map.get("end_time");

		Integer status = map.get("status") == null || StringUtil.isEmpty(map.get("status").toString()) ? null
				: Integer.parseInt(map.get("status").toString());

		String ship_name = (String) map.get("ship_name");

		Integer paystatus = map.get("paystatus") == null || StringUtil.isEmpty(map.get("paystatus").toString()) ? null
				: Integer.parseInt(map.get("paystatus").toString());

		Integer shipstatus = map.get("shipstatus") == null || StringUtil.isEmpty(map.get("shipstatus").toString())
				? null : Integer.parseInt(map.get("shipstatus").toString());

		Integer shipping_type = map.get("shipping_type") == null
				|| StringUtil.isEmpty(map.get("shipping_type").toString()) ? null
						: Integer.parseInt(map.get("shipping_type").toString());

		Integer payment_id = map.get("payment_id") == null || StringUtil.isEmpty(map.get("payment_id").toString())
				? null : Integer.parseInt(map.get("payment_id").toString());

		Integer depotid = map.get("depotid") == null || StringUtil.isEmpty(map.get("depotid").toString()) ? null
				: Integer.parseInt(map.get("depotid").toString());

		String complete = map.get("complete") == null || StringUtil.isEmpty(map.get("complete").toString()) ? null
				: (String) map.get("complete").toString();

		String store_name = map.get("store_name") == null || StringUtil.isEmpty(map.get("store_name").toString()) ? null
				: map.get("store_name").toString();

		Integer store_id = map.get("store_id") == null || StringUtil.isEmpty(map.get("store_id").toString()) ? null
				: Integer.parseInt(map.get("store_id").toString());

		String parent_sn = map.get("parent_sn") == null || StringUtil.isEmpty(map.get("parent_sn").toString()) ? null
				: (String) map.get("parent_sn").toString();

		StringBuffer sql = new StringBuffer();

		sql.append("select * from es_order o where disabled=0 and parent_id is NOT NULL "); // 只查询出子订单

		if (stype != null && keyword != null) {
			if (stype == 0) {
				sql.append(" and (sn like '%" + keyword + "%'");
				sql.append(" or ship_name like '%" + keyword + "%')");
			}
		}

		if (status != null) {
			sql.append("and status=" + status);
		}

		if (sn != null && !StringUtil.isEmpty(sn)) {
			sql.append(" and sn like '%" + sn + "%'");
		}

		if (ship_name != null && !StringUtil.isEmpty(ship_name)) {
			sql.append(" and ship_name like '" + ship_name + "'");
		}

		if (paystatus != null) {
			sql.append(" and pay_status=" + paystatus);
		}

		if (shipstatus != null) {
			sql.append(" and ship_status=" + shipstatus);
		}

		if (shipping_type != null) {
			sql.append(" and shipping_id=" + shipping_type);
		}

		if (payment_id != null) {
			sql.append(" and payment_id=" + payment_id);
		}

		if (depotid != null && depotid > 0) {
			sql.append(" and depotid=" + depotid);
		}

		if (start_time != null && !StringUtil.isEmpty(start_time)) {
			long stime = com.enation.framework.util.DateUtil.getDateline(start_time + " 00:00:00");
			sql.append(" and create_time>" + stime);
		}
		if (end_time != null && !StringUtil.isEmpty(end_time)) {
			long etime = com.enation.framework.util.DateUtil.getDateline(end_time + " 23:59:59");
			sql.append(" and create_time<" + etime);
		}
		if (!StringUtil.isEmpty(orderstate)) {
			if (orderstate.equals("wait_ship")) { // 对待发货的处理
				sql.append(" and ( ( payment_type!='cod' and  status=" + OrderStatus.ORDER_PAY + ") ");// 非货到付款的，要已结算才能发货
				sql.append(" or ( payment_type='cod' and  status=" + OrderStatus.ORDER_CONFIRM + ")) ");// 货到付款的，新订单（已确认的）就可以发货
			} else if (orderstate.equals("wait_pay")) {
				sql.append(" and ( ( payment_type != 'cod' and  status=" + OrderStatus.ORDER_CONFIRM + ") ");// 非货到付款的，未付款状态的可以结算
				sql.append(" or ( payment_type='cod' and  (status=" + OrderStatus.ORDER_SHIP + " or status="
						+ OrderStatus.ORDER_ROG + " )  ) )");// 货到付款的要发货或收货后才能结算

			} else if (orderstate.equals("wait_rog")) {
				sql.append(" and status=" + OrderStatus.ORDER_SHIP);
			} else {
				sql.append(" and status=" + orderstate);
			}

		}

		if (!StringUtil.isEmpty(complete)) {
			sql.append(" and status=" + OrderStatus.ORDER_COMPLETE);
		}
		if (!StringUtil.isEmpty(store_name)) {
			sql.append(
					" and o.store_id in(select store_id from es_store where store_name like '%" + store_name + "%')");
		}
		if (store_id != null) {
			sql.append(" and o.store_id=" + store_id);
		}
		if (!StringUtil.isEmpty(parent_sn)) {
			sql.append(" AND parent_id=(SELECT order_id FROM es_order WHERE sn='" + parent_sn + "')");
		}
		sql.append(" ORDER BY " + sortField + " " + sortType);

		return sql.toString();
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.order.service.IStoreOrderManager#getSubOrderPayStatus(java.lang.Integer)
	 */
	@Override
	public int getSubOrderPayStatus(Integer orderid) {
		String sql = "select order_id from es_order where parent_id=? and status=?";
		List list = this.daoSupport.queryForList(sql, orderid,OrderStatus.ORDER_PAY);
		if(list.isEmpty()){
			return 0;
		}
		return list.size();
	}
	
	@Override
	public List<Order> getByParentId(Integer order_id) {
		String sql="select * from es_order where parent_id = ?";
		return this.daoSupport.queryForList(sql, Order.class, order_id);
	}

	@Override
	public void update(Order order) {
		this.daoSupport.update("es_order", order, " order_id = "+order.getOrder_id());
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.order.service.IStoreOrderManager#getSubOrderPayStatus(java.lang.Integer)
	 */
	@Override
	public Page pageCommentOrders(int pageNo, int pageSize) {

		/**
		 * 查询当前会员的订单
		 */
		StoreMember member = storeMemberManager.getStoreMember();
		StringBuffer sql = new StringBuffer("SELECT * FROM es_order o where o.parent_id is NOT NULL and  member_id=? "
							+" AND order_id in ( SELECT i.order_id FROM es_member_order_item i LEFT JOIN es_order o "
							+" ON i.order_id=o.order_id WHERE o.member_id= ?  and i.commented=0 ) " );

		sql.append(" order by o.create_time desc");

		/**
		 * 分页查询买家订单
		 */
		Page webPage = this.daoSupport.queryForPage(sql.toString(), pageNo, pageSize, Order.class, member.getMember_id(),member.getMember_id());

		return webPage;
	}

}
