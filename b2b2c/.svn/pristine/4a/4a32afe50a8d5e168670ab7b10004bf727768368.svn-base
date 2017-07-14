package com.enation.app.b2b2c.front.tag.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DaoSupport;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.b2b2c.core.order.service.IStoreOrderManager;
import com.enation.app.base.core.model.Member;
import com.enation.app.shop.core.member.model.MemberOrderItem;
import com.enation.app.shop.core.member.service.IMemberOrderItemManager;
import com.enation.app.shop.core.order.model.Order;
import com.enation.app.shop.core.order.model.OrderItem;
import com.enation.app.shop.core.order.service.IOrderManager;
import com.enation.app.shop.core.order.service.OrderStatus;
import com.enation.framework.component.ComponentView;
import com.enation.framework.component.context.ComponentContext;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import freemarker.template.TemplateModelException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * (获取买家待评价订单标签) 
 * @author zjp
 * @version v1.0
 * @since v6.2
 * 2017年1月5日 上午2:07:52
 */
@Component
public class MemberCommentOrderListTag extends BaseFreeMarkerTag{
	@Autowired
	private IStoreOrderManager storeOrderManager;
	@Autowired
	private IStoreMemberManager storeMemberManager;
	@Autowired
	private IDaoSupport daoSupport;
	@Autowired
	private IOrderManager orderManager;
	@Autowired
	private IMemberOrderItemManager memberOrderItemManager;
	@SuppressWarnings("unchecked")
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		Member member = storeMemberManager.getStoreMember();
		if(member==null){
			throw new TemplateModelException("未登陆不能使用此标签[MemberOrderListTag]");
		}
		Map result = new HashMap();
		String page = request.getParameter("page");
		page = (page == null || page.equals("")) ? "1" : page;
		int pageSize = 10;
		
		Page ordersPage = storeOrderManager.pageCommentOrders(Integer.valueOf(page), pageSize);

		Long totalCount = ordersPage.getTotalCount();
		
		result.put("totalCount", totalCount);
		result.put("pageSize", pageSize);
		result.put("page", page);
		result.put("ordersList", ordersPage);

		Map<String,Object> orderstatusMap=OrderStatus.getOrderStatusMap();
		for (String orderStatus: orderstatusMap.keySet()) {
			result.put(orderStatus, orderstatusMap.get(orderStatus));
		}
		return result;
	}
	/**
	 * 获取组件列表
	 * @return 组件列表
	 */
	private List<ComponentView> getDbList() {
		String sql = "select * from es_component ";
		return this.daoSupport.queryForList(sql, ComponentView.class);
	}
	
}
