package com.enation.app.b2b2c.front.tag.store.dlyCenter;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.b2b2c.core.store.service.IStoreDlyCenterManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
/**
 * 发货设置列表标签
 * @author fenlongli
 *
 */
@Component
public class DlyCenterListTag extends BaseFreeMarkerTag{
	@Autowired
	private IStoreDlyCenterManager storeDlyCenterManager;
	@Autowired
	private IStoreMemberManager storeMemberManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		StoreMember member=storeMemberManager.getStoreMember();
		return storeDlyCenterManager.getDlyCenterList(member.getStore_id());
	}
}
