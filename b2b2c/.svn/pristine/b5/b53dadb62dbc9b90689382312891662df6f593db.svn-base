package com.enation.app.b2b2c.front.tag.order;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.order.service.IStoreOrderManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 检查子订单付款状态
 * @author xulipeng
 * @version v1.0
 * @since v6.2
 * 2016年10月17日13:58:14
 */
@Component
@Scope("prototype")
public class CheckSubOrderPayStatusTag extends BaseFreeMarkerTag {

	@Autowired
	private IStoreOrderManager storeOrderManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Integer orderid = (Integer) params.get("orderid");
		int num = this.storeOrderManager.getSubOrderPayStatus(orderid);
		return num;
	}

}
