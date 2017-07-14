package com.enation.app.b2b2c.front.tag.store;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.b2b2c.core.store.model.StoreDlyCenter;
import com.enation.app.b2b2c.core.store.service.IStoreDlyCenterManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 店铺发货地址标签
 * 根据id查单个
 * @author xulipeng
 *
 */
@Component
public class StoreDlyCenterTag extends BaseFreeMarkerTag {
	
	@Autowired
	private IStoreMemberManager storeMemberManager;
	
	@Autowired
	private IStoreDlyCenterManager storeDlyCenterManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		StoreMember member=storeMemberManager.getStoreMember();
		Integer dly_id = (Integer) params.get("dly_id");
		StoreDlyCenter dlyCenter = this.storeDlyCenterManager.getDlyCenter(member.getStore_id(), dly_id);
		return dlyCenter;
	}
	
	

}
