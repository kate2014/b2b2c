package com.enation.app.b2b2c.front.tag.store.dlyCenter;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.b2b2c.core.store.model.StoreDlyCenter;
import com.enation.app.b2b2c.core.store.service.IStoreDlyCenterManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 发货信息标签
 * @author fenlongli
 *
 */
@Component
public class DlyCenterTag extends BaseFreeMarkerTag{
	@Autowired
	private IStoreDlyCenterManager storeDlyCenterManager;
	@Autowired
	private IStoreMemberManager storeMemberManager;
	
	@Override
	protected Object exec(Map param) throws TemplateModelException {
		Integer dlyid=Integer.parseInt(param.get("dlyid").toString());
		StoreDlyCenter s= storeDlyCenterManager.getDlyCenter(storeMemberManager.getStoreMember().getStore_id(),dlyid);
		return s;
	}
}
