package com.enation.app.b2b2c.front.tag.store.storesite;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.b2b2c.core.store.model.Navigation;
import com.enation.app.b2b2c.core.store.service.INavigationManager;
import com.enation.eop.processor.core.UrlNotFoundException;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
public class NavigationTag extends BaseFreeMarkerTag {
	
	@Autowired
	private INavigationManager navigationManager;
	
	@Autowired
	private IStoreMemberManager storeMemberManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Integer nav_id = (Integer) params.get("nav_id");
		Navigation navigation =this.navigationManager.getNavication(nav_id);
		//增加权限
		StoreMember member = storeMemberManager.getStoreMember();
		if(navigation==null || !navigation.getStore_id().equals(member.getStore_id())){
			throw new UrlNotFoundException();
		}
		
		return navigation;
	}
}
