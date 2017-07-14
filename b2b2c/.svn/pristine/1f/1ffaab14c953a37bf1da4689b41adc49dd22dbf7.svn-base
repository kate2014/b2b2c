package com.enation.app.b2b2c.front.tag.order;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.b2b2c.core.order.model.StoreOrder;
import com.enation.app.b2b2c.core.order.service.IStoreOrderManager;
import com.enation.eop.processor.core.UrlNotFoundException;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.StringUtil;

import freemarker.template.TemplateModelException;

/**
 * 店铺订单访问权限判断标签
 * @author DMRain
 * @date 2016-8-24
 */
@Component
public class StoreOrderAccessAuthTag extends BaseFreeMarkerTag{

	@Autowired
	private IStoreOrderManager storeOrderManager;
	
	@Autowired
	private IStoreMemberManager storeMemberManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		String order_id = params.get("order_id").toString();
		
		StoreOrder storeOrder = null;
		
		if (!StringUtil.isEmpty(order_id)) {
			storeOrder = storeOrderManager.get(Integer.parseInt(order_id));
		} else {
			throw new UrlNotFoundException();
		}

		HttpServletResponse response = ThreadContextHolder.getHttpResponse();
		String ctx = this.getRequest().getContextPath();
		if ("/".equals(ctx)) {
			ctx = "";
		}
		String url = ctx + "/new_store/pages/transaction/order.html?order_state=all";

		StoreMember member = storeMemberManager.getStoreMember();

		// 如果订单的店铺ID与当前登录店铺会员的店铺ID不相同
		if (!storeOrder.getStore_id().equals(member.getStore_id())) {
			try {
				response.sendRedirect(url);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return storeOrder;
	}

}
