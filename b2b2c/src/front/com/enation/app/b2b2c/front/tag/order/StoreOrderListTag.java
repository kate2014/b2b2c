package com.enation.app.b2b2c.front.tag.order;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.b2b2c.core.order.service.IStoreOrderManager;
import com.enation.eop.processor.core.UrlNotFoundException;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
/**
 * 获取店铺订单<br>
 * 是获取卖家的订单
 * @author LiFenLong
 *
 */
@Component
public class StoreOrderListTag extends BaseFreeMarkerTag{
	@Autowired
	private IStoreOrderManager storeOrderManager;
	@Autowired
	private IStoreMemberManager storeMemberManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request=ThreadContextHolder.getHttpRequest();
		//session中获取会员信息,判断用户是否登陆
		StoreMember member=storeMemberManager.getStoreMember();
		if(member==null){
			HttpServletResponse response= ThreadContextHolder.getHttpResponse();
			try {
				response.sendRedirect("login.html");
			} catch (IOException e) {
				throw new UrlNotFoundException();
			}
		}
		//获取订单列表参数
		int pageSize=10;
		String page = request.getParameter("page")==null?"1": request.getParameter("page");
		String goods=request.getParameter("goods");
		String order_state = request.getParameter("order_state");
		String keyword=request.getParameter("keyword");
		String buyerName=request.getParameter("buyerName");
		String startTime=request.getParameter("startTime");
		String endTime=request.getParameter("endTime");
		//在该处进行测试输出，是否取到商品名称
        //System.out.println("前台传值:"+goods);
 
		
		Map result=new HashMap();
		result.put("keyword", keyword);
		result.put("order_state", order_state);
		result.put("buyerName", buyerName);
		result.put("startTime", startTime);
		result.put("endTime", endTime);
		result.put("goods", goods);
		
		Page orderList=storeOrderManager.storeOrderList(Integer.parseInt(page), pageSize,member.getStore_id(), result);
		//获取总记录数
		Long totalCount = orderList.getTotalCount();
		
		result.put("page", page);
		result.put("pageSize", pageSize);
		result.put("totalCount", totalCount);
		result.put("storeOrder", orderList);
		return result;
	}
	
}
