package com.enation.app.b2b2c.front.tag.store;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.order.service.IStoreOrderManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 通过店铺ID，获得该店铺 下的商品个数
 * @author wanghongjun
 * 2015-04-20
 */

@Component
@Scope("prototype")
public class StoreGoodsNumTag extends BaseFreeMarkerTag{

	@Autowired
	private IStoreOrderManager storeOrderManager;
	
	@Override
	public Object exec(Map params) throws TemplateModelException {
		Integer storeid=(Integer) params.get("storeid");
		int count = this.storeOrderManager.getStoreGoodsNum(storeid);
		return count;
	}

	
	
}
