package com.enation.app.b2b2c.front.tag.store;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.order.service.IStoreOrderManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;



/**
 * 获得该会员订单在各个状体下的个数
 * @author wanghongjun    2015-04-17
 *
 */
@Component
@Scope("prototype")
public class storeMemberOrderNumTag extends BaseFreeMarkerTag{
	@Autowired
	private IStoreOrderManager storeOrderManager;
	
	@Override
	public Object exec(Map params) throws TemplateModelException {
		Integer status=(Integer) params.get("status");
		int count = this.storeOrderManager.orderStatusNum(status);
		return count;
	}

}
