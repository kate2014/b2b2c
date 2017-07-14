package com.enation.app.b2b2c.core.order.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.b2b2c.core.order.service.IStoreOrderManager;
import com.enation.app.base.core.model.PluginTab;
import com.enation.app.base.core.service.IRegionsManager;
import com.enation.app.shop.ShopApp;
import com.enation.app.shop.core.order.model.DlyCenter;
import com.enation.app.shop.core.order.model.DlyType;
import com.enation.app.shop.core.order.model.Order;
import com.enation.app.shop.core.order.model.PayCfg;
import com.enation.app.shop.core.order.plugin.order.OrderPluginBundle;
import com.enation.app.shop.core.order.service.IDlyCenterManager;
import com.enation.app.shop.core.order.service.IDlyTypeManager;
import com.enation.app.shop.core.order.service.IOrderManager;
import com.enation.app.shop.core.order.service.IPaymentManager;
import com.enation.app.shop.core.order.service.OrderStatus;
import com.enation.framework.action.GridController;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.util.JsonResultUtil;
import com.enation.framework.util.StringUtil;

import net.sf.json.JSONArray;

/**
 * 店铺订单管理
 * @author LiFenLong
 * @version v2.0,2016年3月8日 版本改造  by Sylow
 * @since v6.0
 */
@Controller
@RequestMapping("/b2b2c/admin/store-order")
public class StoreOrderController extends GridController{
	 
	 
	@Autowired
	private IDlyTypeManager dlyTypeManager;
	@Autowired
	private IPaymentManager paymentManager;
	@Autowired
	private IRegionsManager regionsManager;
	@Autowired
	private IOrderManager orderManager;
	@Autowired
	private IStoreOrderManager storeOrderManager;
	//选择发货地点
	@Autowired
	private IDlyCenterManager dlyCenterManager;
	@Autowired
	private OrderPluginBundle orderPluginBundle;
	
	/**
	  * 跳转至订单详细页面
	  * @param ship_name 收货人姓名,String
	  * @param orderId 订单号,Integer
	  * @param ord 订单,Order
	  * @param provinceList 省列表
	  * @param pluginTabs 订单详细页的选项卡
	  * @param pluginHtmls 订单详细页的内容
	  * @param dlycenterlist 发货信息列表
	  * @return 订单详细页面
	  */
	@RequestMapping(value="/order-detail")
	public ModelAndView orderDetail(String ship_name,Integer orderId,Integer status){
		ModelAndView view = new ModelAndView();
		if(ship_name!=null ) ship_name = StringUtil.toUTF8(ship_name);
		Order ord = this.orderManager.get(orderId);
		List provinceList = this.regionsManager.listProvince();
		 
		List<DlyCenter> dlycenterlist= dlyCenterManager.list();
		view.addObject("provinceList", provinceList);
		view.addObject("dlycenterlist", dlycenterlist);
		view.addObject("orderId", orderId); 
		view.addObject("ship_name", ship_name); 
		view.addObject("status", status); 
		view.addObject("sn", ord.getSn());
		view.addObject("ord", ord); 
		List<PluginTab> pluginTabs = this.orderPluginBundle.getDetailHtml(ord);
		view.addObject("pluginTabs",pluginTabs); 
		view.setViewName("/b2b2c/admin/order/order_detail"); 
		return view;
	}
	/**
	 * 分页读取订单列表
	 * 根据订单状态 state 检索，如果未提供状态参数，则检索所有
	 * @param statusMap 订单状态集合,Map
	 * @param payStatusMap 订单付款状态集合,Map
	 * @param shipMap,订单配送人状态集合,Map
	 * @param shipTypeList 配送方式集合,List
	 * @param payTypeList 付款方式集合,List
	 * @return 订单列表
	 */
	@RequestMapping(value="list")
	public ModelAndView list(){

		
		ModelAndView view=getGridModelAndView();
		
		
		List statusList=OrderStatus.getOrderStatus();
		
		List payStatusList = OrderStatus.getPayStatus();
			
		List shipList = OrderStatus.getShipStatus();
		
		
		List<DlyType> shipTypeList = dlyTypeManager.list();
		List<PayCfg> payTypeList = paymentManager.list();

		view.addObject("statusList", statusList);
		view.addObject("payStatusList", payStatusList);
		view.addObject("shipList", shipList);
		view.addObject("shipTypeList", shipTypeList);
		view.addObject("payTypeList", payTypeList);
		
		view.addObject("status_Json", JSONArray.fromObject(statusList).toString());
		view.addObject("payStatus_Json", JSONArray.fromObject(payStatusList).toString() );
		view.addObject("ship_Json", JSONArray.fromObject(shipList).toString());
		view.setViewName("/b2b2c/admin/order/store_order_list");
		return view;
	}
	
	
	
	
	
	/**
	 * @author LiFenLong
	 * @param stype 搜索状态, Integer
	 * @param keyword 搜索关键字,String
	 * @param start_time 开始时间,String
	 * @param end_time 结束时间,String
	 * @param sn 订单编号,String
	 * @param ship_name 订单收货人姓名,String
	 * @param status 订单状态,Integer
	 * @param paystatus 订单付款状态,Integer
	 * @param shipstatus 订单配送状态,Integer
	 * @param shipping_type 配送方式,Integer
	 * @param payment_id 付款方式,Integer
	 * @param order_state 订单状态_从哪个页面进来搜索的(未付款、代发货、等),String
	 * @param complete 是否订单为已完成,String 
	 * @return 订单列表 json
	 */
	@ResponseBody
	@RequestMapping("list-json")
	public GridJsonResult listJson(Integer stype,String keyword,String start_time,String end_time,String status,String sn,String ship_name,
			String paystatus,String shipstatus,String shipping_type,String payment_id,String order_state,
			String complete,String store_name,String store_id,String parent_sn){
		HttpServletRequest requst = ThreadContextHolder.getHttpRequest();
		Map orderMap = new HashMap();
		orderMap.put("stype", stype);
		orderMap.put("keyword", keyword);
		orderMap.put("start_time", start_time);
		orderMap.put("end_time", end_time);
		orderMap.put("status", status);
		orderMap.put("sn", sn);
		orderMap.put("ship_name", ship_name);
		orderMap.put("paystatus", paystatus);
		orderMap.put("shipstatus", shipstatus);
		orderMap.put("shipping_type", shipping_type);
		orderMap.put("payment_id", payment_id);
		orderMap.put("order_state", order_state);
		orderMap.put("complete", complete);
		orderMap.put("store_name", store_name);
		orderMap.put("store_id", store_id);
		orderMap.put("parent_sn", parent_sn);
		this.webpage = this.storeOrderManager.listOrder(orderMap, this.getPage(),this.getPageSize(), this.getSort(),this.getOrder());
		return JsonResultUtil.getGridJson(webpage); 
	}
	
	
	/**
	 * 未付款订单
	 * @author LiFenLong
	 * @param statusMap 订单状态集合,Map
	 * @param payStatusMap 订单付款状态集合,Map
	 * @param shipMap,订单配送人状态集合,Map
	 * @param shipTypeList 配送方式集合,List
	 * @param payTypeList 付款方式集合,List
	 * @return 未付款订单列表
	 */
	@RequestMapping(value="not-pay-order")
	public ModelAndView notPayOrder(){
		ModelAndView view=getGridModelAndView();
		
		List statusList=OrderStatus.getOrderStatus();
		
		List payStatusList = OrderStatus.getPayStatus();
			
		List shipList = OrderStatus.getShipStatus();
		
		List<DlyType> shipTypeList = dlyTypeManager.list();
		List<PayCfg> payTypeList = paymentManager.list();

		view.addObject("statusList", statusList);
		view.addObject("payStatusList", payStatusList);
		view.addObject("shipList", shipList);
		view.addObject("shipTypeList", shipTypeList);
		view.addObject("payTypeList", payTypeList);
		
		//xulipeng 读取传递的店铺参数  2016年11月08日14:02:23
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String storeId = request.getParameter("storeId");
		if(!StringUtil.isEmpty(storeId)){
			view.addObject("store_id", storeId);
		}
		view.addObject("status_Json", JSONArray.fromObject(statusList).toString());
		view.addObject("payStatus_Json", JSONArray.fromObject(payStatusList).toString() );
		view.addObject("ship_Json", JSONArray.fromObject(shipList).toString());
		view.setViewName("/b2b2c/admin/order/not_pay");  
		return view;
	} 
	
	/**
	 * 未付款订单
	 * @author LiFenLong
	 * @param statusMap 订单状态集合,Map
	 * @param payStatusMap 订单付款状态集合,Map
	 * @param shipMap,订单配送人状态集合,Map
	 * @param shipTypeList 配送方式集合,List
	 * @param payTypeList 付款方式集合,List
	 * @return 未付款订单列表
	 */
	@RequestMapping(value="/not-ship-order")
	public ModelAndView notShipOrder(){
		ModelAndView view=getGridModelAndView();
		
		List statusList=OrderStatus.getOrderStatus();
		
		List payStatusList = OrderStatus.getPayStatus();
			
		List shipList = OrderStatus.getShipStatus();
		
		
		List<DlyType> shipTypeList = dlyTypeManager.list();
		List<PayCfg> payTypeList = paymentManager.list();

		view.addObject("statusList", statusList);
		view.addObject("payStatusList", payStatusList);
		view.addObject("shipList", shipList);
		view.addObject("shipTypeList", shipTypeList);
		view.addObject("payTypeList", payTypeList);
		
		view.addObject("status_Json", JSONArray.fromObject(statusList).toString());
		view.addObject("payStatus_Json", JSONArray.fromObject(payStatusList).toString() );
		view.addObject("ship_Json", JSONArray.fromObject(shipList).toString());
		view.setViewName("/b2b2c/admin/order/not_ship");  
		return view;
	} 
	
	/**
	 * 查询自营店的订单 
	 * @return
	 */
	@RequestMapping(value="list-self")
	public ModelAndView listSelf(){

		
		ModelAndView view=getGridModelAndView();
		
		
		List statusList=OrderStatus.getOrderStatus();
		
		List payStatusList = OrderStatus.getPayStatus();
			
		List shipList = OrderStatus.getShipStatus();
		
		
		List<DlyType> shipTypeList = dlyTypeManager.list();
		List<PayCfg> payTypeList = paymentManager.list();

		view.addObject("statusList", statusList);
		view.addObject("payStatusList", payStatusList);
		view.addObject("shipList", shipList);
		view.addObject("shipTypeList", shipTypeList);
		view.addObject("payTypeList", payTypeList);
		
		view.addObject("status_Json", JSONArray.fromObject(statusList).toString());
		view.addObject("payStatus_Json", JSONArray.fromObject(payStatusList).toString() );
		view.addObject("ship_Json", JSONArray.fromObject(shipList).toString());
		
		view.setViewName("/b2b2c/admin/self/order/self_order_list");
		
		return view;
	}
	

	/**
	 * 查询自营店订单列表json
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="list-self-json")
	public GridJsonResult listSelfJson(String stype,String keyword,String start_time,String end_time,String status,String sn,String ship_name,
			String paystatus,String shipstatus,String shipping_type,String payment_id,String order_state,
			String complete,String store_name,String parent_sn){
		HttpServletRequest requst = ThreadContextHolder.getHttpRequest();
		Map orderMap = new HashMap();
		orderMap.put("stype", stype);
		orderMap.put("keyword", keyword);
		orderMap.put("start_time", start_time);
		orderMap.put("end_time", end_time);
		orderMap.put("status", status);
		orderMap.put("sn", sn);
		orderMap.put("ship_name", ship_name);
		orderMap.put("paystatus", paystatus);
		orderMap.put("shipstatus", shipstatus);
		orderMap.put("shipping_type", shipping_type);
		orderMap.put("payment_id", payment_id);
		orderMap.put("order_state", order_state);
		orderMap.put("complete", complete);
		orderMap.put("store_name", store_name);
		orderMap.put("store_id", ShopApp.self_storeid);
		orderMap.put("parent_sn", parent_sn);
		this.webpage = this.storeOrderManager.listOrder(orderMap, this.getPage(),this.getPageSize(), this.getSort(),this.getOrder());
		return JsonResultUtil.getGridJson(webpage); 
	}
	
	public Map<String, String> getGridUrl() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 取消订单申请列表
	 * @return
	 */
	@RequestMapping(value="/cancel-application-list")
	public ModelAndView cancelApplicationList(){
		ModelAndView view=this.getGridModelAndView();
		List statusList=OrderStatus.getOrderStatus();
		view.addObject("status_Json", JSONArray.fromObject(statusList).toString());
		view.setViewName("/b2b2c/admin/orderReport/cancel_application");
		return view;
	}
	

	/**
	 * 取消订单申请JSON列表
	 */
	@ResponseBody
	@RequestMapping(value="/cancel-application-list-json")
	public GridJsonResult cancelApplicationListJson(){
		Page page=storeOrderManager.cancelApplicationList(this.getPage(),this.getPageSize(),0);
		return JsonResultUtil.getGridJson(page);
	}
	
}
