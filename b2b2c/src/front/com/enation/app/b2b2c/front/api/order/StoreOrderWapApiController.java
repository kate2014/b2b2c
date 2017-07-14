package com.enation.app.b2b2c.front.api.order;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.b2b2c.core.order.model.StoreOrder;
import com.enation.app.b2b2c.core.order.service.IStoreOrderManager;
import com.enation.app.b2b2c.core.order.service.IStoreOrderPrintManager;
import com.enation.app.base.core.model.Member;
import com.enation.app.shop.component.receipt.Receipt;
import com.enation.app.shop.component.receipt.service.IReceiptManager;
import com.enation.app.shop.core.order.model.Order;
import com.enation.app.shop.core.order.model.OrderItem;
import com.enation.app.shop.core.order.service.IOrderManager;
import com.enation.eop.processor.core.UrlNotFoundException;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.action.GridController;
import com.enation.framework.action.JsonResult;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.util.JsonMessageUtil;
import com.enation.framework.util.JsonResultUtil;
import com.google.gson.JsonObject;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * (店铺交易API) 
 * @author zjp
 * @version v1.0
 * @since v6.2
 * 2017年2月21日 下午3:59:02
 */
@Controller
@Scope("prototype")
@RequestMapping("/api/b2b2c/store-order")
public class StoreOrderWapApiController extends GridController{
	@Autowired
	private IStoreOrderManager storeOrderManager;
	@Autowired
	private IStoreMemberManager storeMemberManager;
	@Autowired
	private IOrderManager orderManager;
	@Autowired
	private IReceiptManager receiptManager;
	
	/**
	 * 条件查询订单列表
	 * @param page 页数
	 * @param goods 订单包含商品
	 * @param order_state 订单状态
	 * @param keyword 订单编号
	 * @param buyerName 买家
	 * @param startTime 开始时间
	 * @param endTime 结束时间	
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/order-list")
	public JsonResult orderList(Integer page,String goods,String order_state,String keyword,String buyerName,String startTime,String endTime) {
		HttpServletRequest request=ThreadContextHolder.getHttpRequest();
		//session中获取会员信息,判断用户是否登陆
		StoreMember member=storeMemberManager.getStoreMember();
		if (member == null) {
        	return JsonResultUtil.getErrorJson("您没有登录或登录过期！");
        }
		
		//获取订单列表参数
		int pageSize=10;
		if(page==null){
			page = 1;
		}	
		Map result=new HashMap();
		result.put("keyword", keyword);
		result.put("order_state", order_state);
		result.put("buyerName", buyerName);
		result.put("startTime", startTime);
		result.put("endTime", endTime);
		result.put("goods", goods);
	
		Page orderList=storeOrderManager.storeOrderList(page, pageSize,member.getStore_id(), result);
		//获取总记录数
		Long totalCount = orderList.getTotalCount();
		List storeOrder = (List) orderList.getResult();

		result.put("page", page);
		result.put("pageSize", pageSize);
		result.put("totalCount", totalCount);
		JSONArray fromObject = JSONArray.fromObject(storeOrder);
		result.put("storeOrder", fromObject);
		return JsonResultUtil.getObjectJson(result);
	}
	/**
	 * 获取订单详细
	 * 
	 * @param orderId
	 *            订单Id,Integer
	 * @return 返回JsonResult result 为1表示调用成功0表示失败 data订单信息
	 */
	@ResponseBody
	@RequestMapping(value = "order_detail")
	public JsonResult orderDetail(Integer orderId) {
		try {
			//增加权限校验
			StoreMember member = storeMemberManager.getStoreMember();
			if(member==null){
				return JsonResultUtil.getErrorJson("请登录！获取订单信息失败！");
			}
			StoreOrder order = storeOrderManager.get(orderId);
			if(order==null||!order.getStore_id().equals(member.getStore_id())){
				return JsonResultUtil.getErrorJson("您没有权限");
			}
			JSONArray order_detail = JSONArray.fromObject(order);
			Map result=new HashMap();
			result.put("order_detail", order_detail);
			return JsonResultUtil.getObjectJson(result);
		} catch (RuntimeException e) {
			if (logger.isDebugEnabled()) {
				logger.debug(e);
			}
			return JsonResultUtil.getErrorJson("获取订单信息失败" + e.getMessage());
		}
	}
	/**
	 * 获取订单发票详细
	 * 
	 * @param orderId
	 *            订单Id,Integer
	 * @return 返回JsonResult result 为1表示调用成功0表示失败 data订单信息
	 */
	@ResponseBody
	@RequestMapping(value = "order_receipt")
	public JsonResult orderReceipt(Integer orderId) {
		try {
			//增加权限校验
			StoreMember member = storeMemberManager.getStoreMember();
			if(member==null){
				return JsonResultUtil.getErrorJson("请登录！获取订单发票信息失败！");
			}
			StoreOrder order = storeOrderManager.get(orderId);
			if(order==null||!order.getStore_id().equals(member.getStore_id())){
				return JsonResultUtil.getErrorJson("您没有权限");
			}
			Receipt order_receipt = receiptManager.getByOrderid(orderId);
			JSONArray order_detail = JSONArray.fromObject(order_receipt);
			Map result=new HashMap();
			result.put("order_receipt", order_receipt);
			return JsonResultUtil.getObjectJson(result);
		} catch (RuntimeException e) {
			if (logger.isDebugEnabled()) {
				logger.debug(e);
			}
			return JsonResultUtil.getErrorJson("获取发票信息失败" + e.getMessage());
		}
	}
	
}
