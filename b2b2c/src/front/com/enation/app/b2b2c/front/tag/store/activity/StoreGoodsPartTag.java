package com.enation.app.b2b2c.front.tag.store.activity;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.store.service.activity.IStoreActivityManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 获取参加当前促销活动的商品集合Tag
 * @author DMRain
 * @date 2015年12月30日
 * @version v1.0
 * @since v1.0
 */
@Component
public class StoreGoodsPartTag extends BaseFreeMarkerTag{
	@Autowired
	private IStoreActivityManager storeActivityManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		Integer activity_id = Integer.parseInt(request.getParameter("activity_id"));
		
		List list = this.storeActivityManager.partGoodsList(activity_id);
		
		return list;
	}
	
}
