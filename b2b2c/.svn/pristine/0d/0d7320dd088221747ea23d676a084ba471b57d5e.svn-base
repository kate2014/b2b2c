package com.enation.app.b2b2c.front.tag.store.activity;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.store.model.activity.StoreActivity;
import com.enation.app.b2b2c.core.store.service.activity.IStoreActivityManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 获取店铺当前进行的促销活动信息Tag
 * @author DMRain
 * @date 2016年3月4日
 * @version v1.0
 * @since v1.0
 */
@Component
public class CheckStoreCurrActTag extends BaseFreeMarkerTag{

	@Autowired
	private IStoreActivityManager storeActivityManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Integer store_id = (Integer) params.get("store_id");
		StoreActivity activity = this.storeActivityManager.getCurrentAct(store_id);
		
		Map map = new HashMap();
		map.put("activity", activity);
		
		return map;
	}

}
