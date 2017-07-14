package com.enation.app.b2b2c.front.tag.store.activity;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.b2b2c.core.store.model.activity.StoreActivity;
import com.enation.app.b2b2c.core.store.service.activity.IStoreActivityManager;
import com.enation.eop.processor.core.UrlNotFoundException;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 获取促销活动信息Tag
 * @author DMRain
 * @date 2015年12月30日
 * @version v1.0
 * @since v1.0
 */
@Component
public class StoreActivityTag extends BaseFreeMarkerTag{
	
	@Autowired
	private IStoreActivityManager storeActivityManager;
	@Autowired
	private IStoreMemberManager storeMemberManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String activity_id = request.getParameter("activity_id");
		StoreActivity activity = this.storeActivityManager.get(Integer.parseInt(activity_id));
		//增加权限
		StoreMember member = storeMemberManager.getStoreMember();
		if(activity==null || !activity.getStore_id().equals(member.getStore_id())){
			throw new UrlNotFoundException();
		}
		
		return activity;
	}
	
}
