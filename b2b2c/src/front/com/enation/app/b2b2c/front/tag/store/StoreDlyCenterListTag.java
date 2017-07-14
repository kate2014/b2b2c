package com.enation.app.b2b2c.front.tag.store;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.b2b2c.core.store.service.IStoreDlyCenterManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 店铺发货地址标签
 * 根据店铺id查集合
 * @author xulipeng
 */
@Component
public class StoreDlyCenterListTag extends BaseFreeMarkerTag {
	
	@Autowired
	private IStoreMemberManager storeMemberManager;
	
	@Autowired
	private IStoreDlyCenterManager storeDlyCenterManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		StoreMember member=storeMemberManager.getStoreMember();
		List list = this.storeDlyCenterManager.getDlyCenterList(member.getStore_id());
		return list;
	}
	
	//set get
	

}
