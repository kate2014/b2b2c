package com.enation.app.b2b2c.front.tag.store;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberAddressManager;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 查询默认会员地址
 * @author xulipeng
 * 2014年12月13日16:33:53
 *
 */
@Component
public class MemberAdressRegionTag extends BaseFreeMarkerTag {
	
	@Autowired
	private IStoreMemberManager storeMemberManager;
	
	@Autowired
	private IStoreMemberAddressManager storeMemberAddressManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		StoreMember storeMember = storeMemberManager.getStoreMember(); 
		if(storeMember!=null){
			return storeMemberAddressManager.getRegionid(storeMember.getMember_id());
		}
		return 0;
	}
	

}
