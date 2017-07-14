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
import com.enation.app.b2b2c.core.order.service.IStoreSellBackManager;
import com.enation.app.shop.core.order.model.Order;
import com.enation.app.shop.core.order.model.OrderGift;
import com.enation.app.shop.core.order.service.IOrderGiftManager;
import com.enation.app.shop.core.order.service.ISellBackManager;
import com.enation.framework.action.GridController;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.action.JsonResult;
import com.enation.framework.database.Page;
import com.enation.framework.util.JsonResultUtil;

/**
 * 店铺售后申请Controller
 * @author Kanon
 * @version 1.0 2016-7-15
 * @since 6.1
 *
 */
@Controller
@RequestMapping("/b2b2c/admin/store-sell-back")
public class StoreSellBackController extends GridController {
	
	@Autowired
	private IStoreSellBackManager storeSellBackManager;
	
	@Autowired
	private IStoreOrderManager storeOrderManager;
	
	@Autowired
	private ISellBackManager sellBackManager;
	
	@Autowired
	private IOrderGiftManager orderGiftManager;
	/**
	 * 售后申请列表
	 * @return
	 */
	@RequestMapping(value="/refund-list")
	public ModelAndView refundList(Integer type,Integer store_id){
		ModelAndView view=this.getGridModelAndView();
		view.addObject("type", type);
		view.addObject("store_id", store_id);
		view.setViewName("/b2b2c/admin/orderReport/refund_application");
		return view;
	}
	
	/**
	 * 售后申请列表
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/refund-list-json")
	public GridJsonResult refundListJson(Integer type,Integer store_id){
		HashMap map=new HashMap();
		map.put("type", type);
		Page page=storeSellBackManager.list(this.getPage(), this.getPageSize(),store_id,0 ,map);
		return JsonResultUtil.getGridJson(page);
	}
	/**
	 * 填写商家退款金额
	 * @return
	 */ 
	@RequestMapping(value = "/returned")
	public ModelAndView returned(Integer id) {
		ModelAndView view = new ModelAndView();
		try {
			Map sellBackList = this.storeSellBackManager.get(id);// 退货详细
			Order orderinfo = storeOrderManager.get(Integer.parseInt(sellBackList.get("orderid").toString()));// 订单详细
			List goodsList = this.sellBackManager.getGoodsList(id);// 退货商品列表
			List logList = this.sellBackManager.sellBackLogList(id);// 退货操作日志
			
			OrderGift gift = new OrderGift();
			
			//如果退货单中有赠品id add_by DMRain 2016-7-19
			if (sellBackList.get("gift_id") != null) {
				gift = this.orderGiftManager.getOrderGift(Integer.parseInt(sellBackList.get("gift_id").toString()), Integer.parseInt(sellBackList.get("orderid").toString()));
			}
			view.addObject("gift", gift);
			
			view.addObject("sellBackList", sellBackList);
			view.addObject("orderinfo", orderinfo);
			view.addObject("goodsList", goodsList);
			view.addObject("logList", logList);
			//view.addObject("depot_name", depot_name);
			view.setViewName("/b2b2c/admin/orderReport/sellback_refund"); 
		}catch (Exception e) {
			this.logger.error("显示退货出错", e); 
			view.setViewName("/b2b2c/admin/orderReport/sellback_refund");  
		}
		return view;
	}
	/**
	 * 填写退款金额
	 * @param id 售后申请单Id
	 * @param alltotal_pay 退款金额
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/refund")
	public JsonResult refund(Integer id,Double alltotal_pay){
		try {
			storeSellBackManager.refund(id, alltotal_pay);
			return JsonResultUtil.getSuccessJson("操作成功");
		} catch (Exception e) {
			return JsonResultUtil.getErrorJson("操作失败");
		} 
		
	}
}
