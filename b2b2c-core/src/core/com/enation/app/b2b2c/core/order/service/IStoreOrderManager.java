package com.enation.app.b2b2c.core.order.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.b2b2c.core.order.model.StoreOrder;
import com.enation.app.shop.core.order.model.Order;
import com.enation.framework.database.Page;

/**
 * 店铺订单管理类
 * @author LiFenLong
 *@version v2.0 modify by kingapex-2015-08-21
 *v2.0 修改了以下内容：
 *增加创建订单的接口，因为要将单店和多店创建订单分开
 */
public interface IStoreOrderManager {
	

	/**
	 * 创建订单<br>
	 * 在这里首先要通过order核心api来创建主订单，然后创建子订单。<br>
	 * 和单店系统另外的区别是子订单价格计算事件调用另外的事件
	 * @param order 要创建的订单
	 *            订单实体:<br/>
	 *            <li>shipping_id(配送方式id):需要填充用户选择的配送方式id</li> <li>
	 *            regionid(收货地区id)</li> <li>是否保价is_protect</li>
	 *            shipping_area(配送地区):需要填充以下格式数据：北京-北京市-昌平区 </li>
	 * 
	 *            <li>
	 *            payment_id(支付方式id):需要填充为用户选择的支付方式</li>
	 * 
	 *            <li>填充以下收货信息：</br> ship_name(收货人姓名)</br> ship_addr(收货地址)</br>
	 *            ship_zip(收货人邮编)</br> ship_email(收货人邮箱 ) ship_mobile( 收货人手机)
	 *            ship_tel (收货人电话 ) ship_day (送货日期 ) ship_time ( 送货时间 )
	 * 
	 *            </li> <li>remark为买家附言</li>
	 *            
	 * @param shippingIds 
	 *   		配送方式id数据，根据购物车中店铺顺序形成           
	 * @param sessionid
	 *  	  会员的sessionid
	 * @return
	 *        创建的新订单实体，已经赋予order id
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public Order createOrder(Order order,String sessionid) ;
	
	
	/**
	 * 查看店铺订单<br>
	 * 获取卖家的订单
	 * @param pageNo
	 * @param pageSize
	 * @param storeid
	 * @param map
	 * @return 店铺订单列表
	 */
	public Page storeOrderList(Integer pageNo,Integer pageSize,Integer storeid,Map map);
	/**
	 * 查看店铺子订单
	 * @param parent_id
	 * @return 店铺子订单列表
	 */
	public List<StoreOrder> storeOrderList(Integer parent_id);
	/**
	 * 获取一个订单
	 * @param orderId
	 * @return StoreOrder
	 */
	public StoreOrder get(Integer orderId);
	/**
	 * 修改收货人信息
	 * @param remark
	 * @param ship_day
	 * @param ship_name
	 * @param ship_tel
	 * @param ship_mobile
	 * @param ship_zip
	 * @param orderid
	 * @return boolean
	 */
	public boolean saveShipInfo(String remark,String ship_day, String ship_name,String ship_tel, String ship_mobile, String ship_zip, int orderid);
	 
	
	
	/**
	 * 查询买家订单<br>
	 * 只查子订单，订单商品项通过order.itemList来显示<br>
	 * @author kingapex
	 * 2015-11-15
	 * @see Order#getItemList()
	 * @param pageNo 当前页码
	 * @param pageSize 页大小
	 * @param status 订单状态 {@link OrderStatus}
	 * @param keyword 关键字
	 * @return 订单分页列表
	 */
	public Page pageBuyerOrders(int pageNo, int pageSize,String status, String keyword);
	
	
	/**
	 * 根据订单编号查看订单
	 * @param ordersn
	 * @return StoreOrder
	 */
	public StoreOrder get(String ordersn);
	/**
	 * 根据订单状态获取店铺订单数量
	 * @param struts
	 * @author LiFenLong
	 * @return
	 */
	public int getStoreOrderNum(Integer... struts);
	
	
	
	
	/**
	 * 查询所有商家的订单列表<br>
	 * 只查询子订单
	 * @author LiFenLong 
	 * @param map 过滤条件<br>
	 * <li>stype:搜索类型(integer,0为基本搜索)</li>
	 * <li>keyword:关键字(String)</li>
	 * <li>order_state:订单状态特殊查询(String型，可以是如下的值：
	 * wait_ship:待发货
	 * wait_pay:待付款
	 * wait_rog:待收货
	 * )</li>
	 * <li>start_time:(开始时间,String型，如2015-10-10 )</li>
	 * <li>end_time(结束时间,String型，如2015-10-10 )</li>
	 * <li>status:订单状态(int型，对应status字段，{@link OrderStatus})</li>
	 * <li>paystatus:付款状态(int型，对应pay_status字段，{@link OrderStatus})</li>
	 * <li>shipstatus发货状态(int型，对应ship_status字段，{@link OrderStatus})</li>
	 * <li>sn:订单编号(String)</li>
	 * <li>ship_name:收货人(String 对应ship_name字段)</li>
	 * <li>shipping_type:配送方式(Integer，对应shipping_id字段)</li>
	 * <li>payment_id:支付方式(Integer 对应payment_id字段)</li>
	 * <li>depotid:仓库id(Integer 对应depotid字段)</li>
	 * <li>store_name:店铺名称(String 会联合es_store表查询)</li> 
	 * <li>store_id:店铺id(Integer 对应store_id字段)</li> 
	 * @param page
	 * @param pageSize
	 * @param sortField 排序字段
	 * @param sortType 排序方式
	 * @return 订单的分页列表
	 */
	public Page listOrder(Map map,int page,int pageSize,String sortField,String sortType);
	
	
 
	
	
	/**
	 * 获取订单状态的json
	 * @return 订单状态Json
	 */
	public Map getStatusJson();
	/**
	 * 获取付款状态的json
	 * @return 付款状态Json
	 */
	public Map getpPayStatusJson();
	/**
	 * 获取配送状态的json
	 * @return 配送状态Json
	 */
	public Map getShipJson();
	/**
	 * 发货
	 * @param order_id 订单ID
	 * @param logi_id 物流公司id
	 * @param logi_name 物流公司名称
	 * @param shipNo 运单号
	 */
	public void saveShipNo(Integer[] order_id,Integer logi_id,String logi_name,String shipNo);
	
	/**
	 * 获得该会员订单在各个状态的个数
	 * 
	 */
	public Integer orderStatusNum(Integer status);
	
	/**
	 * 通过商铺ID，获得该商铺下的商品个数
	 * @param store_id
	 * @return
	 */
	public Integer getStoreGoodsNum(int store_id);


	/**
	 * 获取会员所有子订单
	 * @param pageNo
	 * @param pageSize
	 * @param status
	 * @param keyword
	 * @return
	 */
	public Page pageChildOrders(int pageNo, int pageSize, String status, String keyword);
	
	
	/**
	 * 统计订单状态
	 * @return key为状态值 ，value为此状态订单的数量
	 */
	public Map  censusState();

	/**
	 * 获取店铺退货单数量 add by DMRain 2016-4-18
	 * **rebuild by jianghongyan **
	 * **增加参数status**
	 * **根据退货的状态进行查询**
	 * **传入-999为查询全部**
	 * @return
	 */
	public int getStoreSellbackOrder(Integer... statusArray);
	
	/**
	 * 获取取消订单分页
	 * @param pageNo 页数
	 * @param pageSize 每页显示数量
	 * @return 取消订单分耶列表
	 */
	public Page getCancelApplicationList(Integer pageNo,Integer pageSize);
	
	/**
	 * 获取取消订单申请
	 * 如果is_self 是1则为查询自营店取消订单申请
	 * @author Kanon 
	 * @param pageNo 分页数
	 * @param pageSize 每页显示数量
	 * @param is_self 是否查询是自营店取消订单申请 1是,0否
	 * @return 取消订单申请分页列表
	 */
	public Page cancelApplicationList(Integer pageNo,Integer pageSize,Integer is_self);
	
	/**
	 * 获取子订单付款状态
	 * @param orderid
	 * @return
	 */
	public int getSubOrderPayStatus(Integer orderid);
	
	/**
	 * 获取子订单集合
	 * @param order_id 父订单id
	 * @return
	 */
	public List<Order> getByParentId(Integer order_id);

	/**
	 * 更新订单信息
	 * @param order
	 */
	public void update(Order order);
	/**
	 * 查询买家待评论订单<br>
	 * @author kingapex
	 * 2015-11-15
	 * @see Order#getItemList()
	 * @param pageNo 当前页码
	 * @param pageSize 页大小
	 * @return 订单分页列表
	 */
	public Page pageCommentOrders(int pageNo, int pageSize);
}
