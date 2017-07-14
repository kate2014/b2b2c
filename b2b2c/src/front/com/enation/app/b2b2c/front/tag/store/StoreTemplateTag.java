package com.enation.app.b2b2c.front.tag.store;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.b2b2c.core.store.service.IStoreTemplateManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 店铺模板标签
 * @author xulipeng
 *
 */
@Component
public class StoreTemplateTag extends BaseFreeMarkerTag {
	@Autowired
	private IStoreMemberManager storeMemberManager;
	@Autowired
	private IStoreTemplateManager storeTemplateManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		StoreMember member=storeMemberManager.getStoreMember();
		List list = this.storeTemplateManager.getTemplateList(member.getStore_id());
		return list;
	}
}
