package com.enation.app.b2b2c.front.tag.store.storesite;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.b2b2c.core.store.service.INavigationManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;


@Component
public class StoreNavigationTag extends BaseFreeMarkerTag {
	@Autowired
	private INavigationManager navigationManager;
	@Autowired
	private IStoreMemberManager storeMemberManager;
	@SuppressWarnings("rawtypes")
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Integer storeid=(Integer) params.get("store_id");
		if(storeid==null){
			StoreMember storeMember = storeMemberManager.getStoreMember();
			storeid = storeMember.getStore_id();
		}
		List list = this.navigationManager.getNavicationList(storeid);
		return list;
	}
}
