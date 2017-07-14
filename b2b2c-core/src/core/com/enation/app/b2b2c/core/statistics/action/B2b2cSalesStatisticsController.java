package com.enation.app.b2b2c.core.statistics.action;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.b2b2c.core.statistics.service.IB2b2cSalesStatisticsManager;
import com.enation.app.b2b2c.core.store.model.Store;
import com.enation.app.b2b2c.core.store.service.IStoreManager;
import com.enation.app.shop.core.order.service.IOrderManager;
import com.enation.app.shop.core.order.service.OrderStatus;
import com.enation.app.shop.core.statistics.service.ISalesStatisticsManager;
import com.enation.framework.action.GridController;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.util.CurrencyUtil;
import com.enation.framework.util.JsonMessageUtil;
import com.enation.framework.util.JsonResultUtil;

@SuppressWarnings({ "unchecked", "rawtypes" })
@Controller
@Scope("prototype")
@RequestMapping("/b2b2c/admin/salesStatis")
public class B2b2cSalesStatisticsController  extends GridController{

	@Autowired
	private IB2b2cSalesStatisticsManager b2b2cSalesStatisticsManager;
	
	@Autowired
	private IStoreManager storeManager;
	
	
	/**
	 * 跳转到订单统计页面
	 * @param cycle_type 周期模式	1为月，反之则为年
	 * @param order_status 订单状态
	 * @param year 年
	 * @param month 月
	 * @param statusMap 订单状态的集合
	 * @param status_Json 订单状态的json
	 * @param order_statis_type 下单统计类型，1为下单金额，反之为为下单量
	 * @return
	 */
	@RequestMapping(value="/order-statis")
	public ModelAndView orderStatis(Integer cycle_type, Integer order_status, Integer year, Integer month,
			Map statusMap, String status_Json, Integer order_statis_type,Integer store_id){
		ModelAndView view = new ModelAndView();
		List<Store> storeList= this.storeManager.listAll();
		view.addObject("storeList", storeList);
		if(store_id != null ){
			view.addObject("store_id",store_id);
		}else{
			view.addObject("store_id",0);
		}
		
		if(cycle_type==null){
			cycle_type=1;
		}
		view.addObject("cycle_type", cycle_type);
		
		if(order_status==null){
			order_status=99;
		}
		view.addObject("order_status", order_status);
		
		Calendar cal = Calendar.getInstance();
		if(year==null){
			year = cal.get(Calendar.YEAR);
		}
		view.addObject("year", year);
		
		if(month==null){
			month = cal.get(Calendar.MONTH )+1;
		}
		view.addObject("month", month);
		
		if(statusMap.size() == 0){
			statusMap = new HashMap();
			statusMap = getStatusJson();
			String p = JSONArray.fromObject(statusMap).toString();
			status_Json = p.replace("[", "").replace("]", "");
		}
		view.addObject("statusMap", statusMap);
		view.addObject("status_Json", status_Json);
		
		if(order_statis_type==null){
			order_statis_type=1;
		}
		view.addObject("order_statis_type", order_statis_type);
		
		view.addObject("pageSize", this.getPageSize());
		
		if(order_statis_type.intValue()==1){
			view.setViewName("/b2b2c/admin/statistics/sales/order_money");
		}else{
			view.setViewName("/b2b2c/admin/statistics/sales/order_num");
		}
		
		return view;
	}
	
	/**
	 * 获取销售量统计的甘特图json
	 * @param cycle_type 周期模式	1为月，反之则为年
	 * @param order_status 订单状态
	 * @param year 年
	 * @param month 月
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/get-sale-num-json")
	public Object getSaleNumJson(Integer cycle_type, Integer order_status, Integer year, Integer month,String store_id){
		String message ="[";
		if(cycle_type.intValue()==1){
			List<Map> list= b2b2cSalesStatisticsManager.statisticsMonth_Amount(order_status, year, month,store_id);
			message += getMessage(cycle_type, "t_num", list);
		}else{
			List<Map> list= b2b2cSalesStatisticsManager.statisticsYear_Amount(order_status, year,store_id);
			message += getMessage(cycle_type, "t_num", list);
		}
		message=message.substring(0, message.length()-1)+"]";
		
		return "{\"result\":1,\"message\":"+message+"}";
	}
	
	/**
	 * 获取销售金额统计的甘特图json
	 * @param cycle_type 周期模式	1为月，反之则为年
	 * @param order_status 订单状态
	 * @param year 年
	 * @param month 月
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/get-sale-money-json")
	public Object getSaleMoneyJson(Integer cycle_type, Integer order_status, Integer year, Integer month,String store_id){
		
		String message ="[";
		if(cycle_type.intValue()==1){
			List<Map> list= b2b2cSalesStatisticsManager.statisticsMonth_Amount(order_status, year, month,store_id);
			message += getMessage(cycle_type, "t_money", list);
		}else{
			List<Map> list= b2b2cSalesStatisticsManager.statisticsYear_Amount(order_status, year,store_id);
			message += getMessage(cycle_type, "t_money", list);
		}
		message=message.substring(0, message.length()-1)+"]";
		
		return "{\"result\":1,\"message\":"+message+"}";
	}
	
	/**
	 * 销售收入统计
	 * @param year 年
	 * @param month 月
	 * @param receivables 收款金额
	 * @param refund 退款金额
	 * @param paid 实收金额
	 * @return
	 */
	@RequestMapping(value="/sale-income")
	public ModelAndView saleIncome(Integer year, Integer month, Double receivables, Double refund, Double paid){
		ModelAndView view = new ModelAndView();
		Calendar now = Calendar.getInstance();  
		if(year==null||year==0){
			year=now.get(Calendar.YEAR);
		}
		if(month==null||month==0){
			month=Integer.parseInt(now.get(Calendar.MONTH) + 1 + "");
		}
		receivables =  this.b2b2cSalesStatisticsManager.getReceivables(year, month, null,null);
		view.addObject("receivables", receivables);
		
		List<Store> storeList=this.storeManager.listAll();
		view.addObject("storeList", storeList);
		
		refund = this.b2b2cSalesStatisticsManager.getRefund(year, month, null,null);
		view.addObject("refund", refund);
		
		paid = CurrencyUtil.sub(receivables, refund);
		view.addObject("paid", paid);
		
		view.addObject("pageSize", this.getPageSize());
		view.setViewName("/b2b2c/admin/statistics/sales/sales_list");
		return view;
	}
	
	/**
	 * 销售收入统计json数据
	 * @param year 年
	 * @param month 月
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/sale-income-json")
	public GridJsonResult saleIncomeJson(Integer year, Integer month,String store_id){
		
		Calendar cal = Calendar.getInstance();
		if(year==null){
			year = cal.get(Calendar.YEAR);
		}
		if(month==null){
			month = cal.get(Calendar.MONTH )+1;
		}
		Page list = this.b2b2cSalesStatisticsManager.getSalesIncome(year, month, this.getPage(), this.getPageSize(), null,store_id);
		return JsonResultUtil.getGridJson(list);
	}
	
	/**
	 * 销售收入统计总览json
	 * @param year 年
	 * @param month 月
	 * @param receivables 收款金额
	 * @param refund 退款金额
	 * @param paid 实收金额
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/sale-income-totle-json")
	public Object saleIncomeTotleJson(Integer year, Integer month, Double receivables, Double refund, Double paid,String store_id){
		receivables =  this.b2b2cSalesStatisticsManager.getReceivables(year, month, null,store_id);
		refund = this.b2b2cSalesStatisticsManager.getRefund(year, month, null,store_id);
		paid = CurrencyUtil.sub(receivables, refund);
		
		Map map = new HashMap();
		map.put("receivables", receivables);
		map.put("refund", refund);
		map.put("paid", paid);
		
		return JsonMessageUtil.getObjectJson(map);
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
	@RequestMapping(value="/list-json")
	public GridJsonResult listJson( String sn,String start_time,String end_time,String ship_name,Integer status,Integer paystatus,Integer shipstatus,Integer shipping_type,Integer payment_id,Integer stype,String keyword,String complete,String store_id){
		HttpServletRequest requst = ThreadContextHolder.getHttpRequest();
		Map orderMap = new HashMap();
		orderMap.put("stype", stype);
		orderMap.put("keyword", keyword);
		orderMap.put("start_time", start_time);
		orderMap.put("end_time", end_time);
		orderMap.put("sn", sn);
		orderMap.put("ship_name", ship_name);
		orderMap.put("status", status);
		orderMap.put("paystatus", paystatus);
		orderMap.put("shipstatus", shipstatus);
		orderMap.put("shipping_type", shipping_type);
		orderMap.put("payment_id", payment_id);
		orderMap.put("order_state", requst.getParameter("order_state"));
		orderMap.put("complete", complete);
		String storeWhere="";
		if(store_id != null && !"0".equals(store_id)){
			storeWhere = " and o.store_id = " +store_id;
		}
		
		orderMap.put("storeWhere", storeWhere);
		
		webpage = this.b2b2cSalesStatisticsManager.listOrder(orderMap, this.getPage(),this.getPageSize(), this.getSort(),this.getOrder());
		return JsonResultUtil.getGridJson(webpage);

	}
	
	
	
	/**
	 * 判断周期模式（按年或者按月），并返回相应的字串
	 * @author xulipeng
	 * @param cycle_type 	周期模式
	 * @param param		t_num：总订单数，t_money：总金额
	 * @param list	数据集合
	 * @return
	 */
	private String getMessage(int cycle_type,String param,List<Map> list){
		int num = 0;
		if(cycle_type==1){
			num=31;
		}else{
			num=12;
		}

		String message = "";
		for (int i = 1; i <= num; i++) {
			boolean flag = true;
			for (int j =0;j<list.size();j++) {
				Map map = list.get(j);
				if(!map.get("month").toString().equals("0") && i==Integer.parseInt(map.get("month").toString())){
					message = message+map.get(param).toString()+",";
					flag = false;
				}
			}
			if(flag){
				message = message+"0,";
			}
		}
		return message;
	}
	
	/**
	 * 订单状态集合
	 * @author xulipeng
	 * @return 
	 */
	private Map getStatusJson(){
		Map orderStatus = new  HashMap();
		
		orderStatus.put(""+OrderStatus.ORDER_NOT_PAY, OrderStatus.getOrderStatusText(OrderStatus.ORDER_NOT_PAY));
		orderStatus.put(""+OrderStatus.ORDER_CONFIRM, OrderStatus.getOrderStatusText(OrderStatus.ORDER_CONFIRM));
		orderStatus.put(""+OrderStatus.ORDER_PAY, OrderStatus.getOrderStatusText(OrderStatus.ORDER_PAY));
		
		orderStatus.put(""+OrderStatus.ORDER_SHIP, OrderStatus.getOrderStatusText(OrderStatus.ORDER_SHIP));
		orderStatus.put(""+OrderStatus.ORDER_ROG, OrderStatus.getOrderStatusText(OrderStatus.ORDER_ROG));
		orderStatus.put(""+OrderStatus.ORDER_COMPLETE, OrderStatus.getOrderStatusText(OrderStatus.ORDER_COMPLETE));
		orderStatus.put(""+OrderStatus.ORDER_CANCELLATION, OrderStatus.getOrderStatusText(OrderStatus.ORDER_CANCELLATION));
		orderStatus.put(""+OrderStatus.ORDER_MAINTENANCE, OrderStatus.getOrderStatusText(OrderStatus.ORDER_MAINTENANCE));
		//暂停使用的订单状态
		//orderStatus.put(""+OrderStatus.ORDER_PAY, OrderStatus.getOrderStatusText(OrderStatus.ORDER_PAY));
		//orderStatus.put(""+OrderStatus.ORDER_NOT_CONFIRM, OrderStatus.getOrderStatusText(OrderStatus.ORDER_NOT_CONFIRM)); 
		//orderStatus.put(""+OrderStatus.ORDER_ALLOCATION_YES, OrderStatus.getOrderStatusText(OrderStatus.ORDER_ALLOCATION_YES));
		return orderStatus;
	}
}
