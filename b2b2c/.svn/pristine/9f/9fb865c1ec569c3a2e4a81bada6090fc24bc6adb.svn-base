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
 * 获取买家的订单<br>
 * 在会员中心使用
 * @author kingaepx
 *
 */
@Component
public class BuyerOrderListTag extends BaseFreeMarkerTag{
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
		String status = request.getParameter("status");
		String keyword = request.getParameter("keyword");
		
		Page ordersPage = storeOrderManager.pageBuyerOrders(Integer.valueOf(page), pageSize,status,keyword);
		List<Object> list = (List<Object>) ordersPage.getResult();
		for (Object object : list) {
			Order order = (Order) object;
			Integer orderid = order.getOrder_id();
			List<OrderItem> itemList  =orderManager.listGoodsItems(orderid);
			//遍历订单项，查询没有评论过的商品列表
			List<OrderItem> orderItemList = new ArrayList<OrderItem>();
			for(int i=0;i<itemList.size();i++){
				Integer order_id = itemList.get(i).getOrder_id();
				Integer product_id =itemList.get(i).getProduct_id();
				MemberOrderItem memberOrderItem = memberOrderItemManager.getMemberOrderItem(order_id, product_id, 0);
				if(memberOrderItem!=null){
					orderItemList.add(itemList.get(i));
				}
			}
			if(orderItemList.size()>0){
				order.setIs_comment(1);
			}
			
			//获取数据判断订单是否存在快照，存在快照则显示快照按钮
			String items_json = order.getItems_json();
			JSONArray jsonArray = JSONArray.fromObject(items_json);
			for (Object json : jsonArray) {
				JSONObject jsonObject=(JSONObject) json;
				Object goods_id = jsonObject.get("goods_id");
				String sql = "select * from es_goods where goods_id=?";
				Map map = daoSupport.queryForMap(sql, goods_id);
				Object last_modify = map.get("last_modify");
				Object snapshot_id = jsonObject.get("snapshot_id");
				sql = "select * from es_goods_snapshot where snapshot_id=?";
				List list_snapshot = daoSupport.queryForList(sql, snapshot_id);
				ComponentView componentView= this.getComponentView("orderCoreComponent");
				if(componentView.getEnable_state() == 1){
					if(list_snapshot.size()!=0){
						Map map_snapshot = (Map) list_snapshot.get(0);
						Object  last_modify_snapshot= map_snapshot.get("last_modify");
						if(!last_modify_snapshot.equals(last_modify)){				
							jsonObject.put("snapshot_switch", 1);
						}else{
							jsonObject.put("snapshot_switch", 0);
						}
					}
				}
			}
			order.setItems_json(jsonArray.toString());
		}
		Long totalCount = ordersPage.getTotalCount();
		
		result.put("totalCount", totalCount);
		result.put("pageSize", pageSize);
		result.put("page", page);
		result.put("ordersList", ordersPage);

		//Author LiFenLong
		Map<String,Object> orderstatusMap=OrderStatus.getOrderStatusMap();
		for (String orderStatus: orderstatusMap.keySet()) {
			result.put(orderStatus, orderstatusMap.get(orderStatus));
		}
		
		if(status!=null){
			result.put("status",status );
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
	/**
	 * 
	 * @param componentid 组件
	 * @return 组件视图
	 */
	private ComponentView getComponentView(String componentid) {
		List<ComponentView> componentList = ComponentContext.getComponents();
		for (ComponentView componentView : componentList) {
			if (componentView.getComponentid().equals(componentid)) {
				return componentView;
			}
		}
		return null;
	}
}
