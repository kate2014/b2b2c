package com.enation.app.b2b2c.front.tag.order;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.order.service.IStoreOrderManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 取消订单申请列表
 * @author Kanon
 *
 */
@Component
public class OrderCancelApplicationTag extends BaseFreeMarkerTag{
	@Autowired
	private IStoreOrderManager storeOrderManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		
		return storeOrderManager.getCancelApplicationList(this.getPage(), this.getPageSize());
	}

}
