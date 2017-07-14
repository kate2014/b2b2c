package com.enation.app.b2b2c.core.order.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.b2b2c.core.order.service.IStoreOrderManager;
import com.enation.app.shop.ShopApp;
import com.enation.app.shop.core.order.model.OrderGridUrlKeyEnum;
import com.enation.app.shop.core.order.service.OrderStatus;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.database.Page;
import com.enation.framework.util.JsonResultUtil;

import net.sf.json.JSONArray;



/**
 * 自营店订单管理action
 * @author [kingapex]
 * @version [2.0]  2016-03-14 6.0版本改造 by Sylow
 * @since [v.6.0]
 * 2015年11月6日下午3:15:41
 */
@Controller
@RequestMapping("/b2b2c/admin/self-store-order")
public class SelfStoreOrderController extends StoreOrderController {
	
	@Autowired
	private IStoreOrderManager storeOrderManager;
	
	/**
	 * 覆写父类的此方法，以改变grid视图中的数据源url
	 */
	@Override
	@RequestMapping(value="get-grid-url")
	public Map<String, String> getGridUrl() {
		
		Map<String,String> urlMap  = new HashMap<String,String>();
		urlMap.put(OrderGridUrlKeyEnum.not_pay.toString(), "selfStoreOrder!listJson.do?order_state=wait_pay");
		urlMap.put(OrderGridUrlKeyEnum.not_ship.toString(), "selfStoreOrder!listJson.do?order_state=wait_ship");
		urlMap.put(OrderGridUrlKeyEnum.detail.toString(), "/shop/admin/order!detail.do?self_store=yes");

		return urlMap;
	}


	@Override
	public ModelAndView notPayOrder() {
		return super.notPayOrder().addObject("store_id", 1);
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
		view.setViewName("/b2b2c/admin/self/orderReport/cancel_application");
		return view;
	}
	

	/**
	 * 取消订单申请JSON列表
	 */
	@ResponseBody
	@RequestMapping(value="/cancel-application-list-json")
	public GridJsonResult cancelApplicationListJson(){
		Page page=storeOrderManager.cancelApplicationList(this.getPage(),this.getPageSize(),1);
		return JsonResultUtil.getGridJson(page);
	}
	
	/**
	 * 售后申请列表
	 * @return
	 */
	@RequestMapping(value="/refund-list")
	public ModelAndView refundList(Integer type){
		ModelAndView view=this.getGridModelAndView();
		view.addObject("type", type);
		view.addObject("store_id", ShopApp.self_storeid);
		view.setViewName("/b2b2c/admin/orderReport/refund_application");
		return view;
	}
}
