package com.enation.app.b2b2c.core.order.action;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.b2b2c.core.order.service.IB2B2cOrderReportManager;
import com.enation.app.b2b2c.core.order.service.IStoreOrderManager;
import com.enation.app.b2b2c.core.order.service.IStoreSellBackManager;
import com.enation.app.shop.core.order.service.IDlyTypeManager;
import com.enation.app.shop.core.order.service.IPaymentManager;
import com.enation.framework.action.GridController;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.util.JsonResultUtil;

import net.sf.json.JSONArray;

/**
 * 收款单和退货单管理
 * @author DMRain 2016年3月17日 版本改造
 * @version v2.0 改为spring mvc
 * @since v6.0
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
@Controller
@RequestMapping("/b2b2c/admin/store-order-report")
public class StoreOrderReportController extends GridController {

	@Autowired
	private IStoreOrderManager storeOrderManager;

	@Autowired
	private IB2B2cOrderReportManager b2B2cOrderReportManager;

	@Autowired
	private IStoreSellBackManager storeSellBackManager;

	@Autowired
	private IDlyTypeManager dlyTypeManager;

	@Autowired
	private IPaymentManager paymentManager;

	/**
	 * 跳转至收款单列表
	 * 
	 * @param statusMap
	 *            订单状态,Map
	 * @param payStatusMap
	 *            付款状态,Map
	 * @param shipMap
	 *            发货状态,Map
	 * @param shipTypeList
	 *            配送方式列表,List
	 * @param payTypeList
	 *            付款方式列表,List
	 * @return
	 */
	@RequestMapping("/payment-list")
	public ModelAndView paymentList() {
		ModelAndView view = new ModelAndView();
		view.addObject("pageSize", this.getPageSize());

		Map statusMap = new HashMap();
		statusMap = this.storeOrderManager.getStatusJson();
		
		view.addObject("statusMap", statusMap);
		String p1 = JSONArray.fromObject(statusMap).toString();
		view.addObject("status_Json", p1.replace("[", "").replace("]", ""));

		Map payStatusMap = new HashMap();
		payStatusMap = this.storeOrderManager.getpPayStatusJson();
		view.addObject("payStatusMap", payStatusMap);
		String p2 = JSONArray.fromObject(payStatusMap).toString();
		view.addObject("payStatus_Json", p2.replace("[", "").replace("]", ""));

		Map shipMap = new HashMap();
		shipMap = this.storeOrderManager.getShipJson();
		view.addObject("shipMap", shipMap);
		String p3 = JSONArray.fromObject(shipMap).toString();
		view.addObject("ship_Json", p3.replace("[", "").replace("]", ""));

		view.addObject("shipTypeList", dlyTypeManager.list());
		view.addObject("payTypeList", paymentManager.list());
		view.setViewName("/b2b2c/admin/orderReport/payment_list");
		return view;
	}

	/**
	 * 获取收款单列表Json
	 * 
	 * @param stype
	 *            搜索类型,Integer
	 * @param keyword
	 *            搜索关键字,String
	 * @param start_time
	 *            下单时间,String
	 * @param end_time
	 *            结束时间,Stirng
	 * @param sn
	 *            编号,String
	 * @param paystatus
	 *            付款状态,Integer
	 * @param payment_id
	 *            付款方式Id,Integer
	 * @param order
	 *            排序
	 * @return 付款单列表Json
	 */
	@ResponseBody
	@RequestMapping("/payment-list-json")
	public GridJsonResult paymentListJson(Integer stype, String keyword, String start_time, String end_time, String sn,
			Integer paystatus, Integer payment_id, String store_name, String order) {
		Map orderMap = new HashMap();
		orderMap.put("stype", stype);
		orderMap.put("keyword", keyword);
		orderMap.put("start_time", start_time);
		orderMap.put("end_time", end_time);
		orderMap.put("sn", sn);
		orderMap.put("paystatus", paystatus);
		orderMap.put("payment_id", payment_id);
		orderMap.put("store_name", store_name);

		this.webpage = this.b2B2cOrderReportManager.listPayment(orderMap, this.getPage(), this.getPageSize(), order);

		return JsonResultUtil.getGridJson(webpage);
	}

	/**
	 * 跳转至退货单列表
	 * @return
	 */
	@RequestMapping("/returned-list")
	public ModelAndView returnedList() {
		ModelAndView view = new ModelAndView();
		view.addObject("pageSize", this.getPageSize());
		view.setViewName("/b2b2c/admin/orderReport/returned_list");
		return view;
	}

	/**
	 * 获取退货单列表JSON
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/returned-list-json")
	public GridJsonResult returnedListJson(String keyword, String order, Integer state) {
		Map orderMap = new HashMap();
		orderMap.put("keyword", keyword);
		orderMap.put("state", state);
		this.webpage = this.b2B2cOrderReportManager.listRefund(this.getPage(), this.getPageSize(), order, orderMap);
		return JsonResultUtil.getGridJson(webpage);
	}
	
	/**
	 * 跳转至退款单列表
	 * @return
	 */
	@RequestMapping(value="/refund-list")
	public ModelAndView refundList(){
		
		ModelAndView view=this.getGridModelAndView();
		view.setViewName("/b2b2c/admin/orderReport/refund_list");
		return view;
	}
	
	/**
	 * 退款单JSON列表
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/refund-list-json")
	public GridJsonResult refundListJson(){
		return JsonResultUtil.getGridJson(storeSellBackManager.refundList(this.getPage(), this.getPageSize()));
	}
	
	/**
	 * 退款单详细
	 * @param id 退款单id
	 * @return
	 */
	@RequestMapping(value="/refund-detail")
	public ModelAndView refundDetail(Integer id){
		ModelAndView view=new ModelAndView();
		view.addObject("refund", storeSellBackManager.getRefund(id));
		view.setViewName("/b2b2c/admin/orderReport/refund_detail");
		return view;
	}
}
