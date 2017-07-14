package com.enation.app.b2b2c.front.tag.store;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.store.service.IStoreManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
public class StoreManagementGoodsCategoryTag extends BaseFreeMarkerTag{

	@Autowired
	private IStoreManager storeManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Integer store_id=(Integer)params.get("store_id");
		List list = storeManager.getStoreManagementGoodsType(store_id);
		return list;
	}

}
