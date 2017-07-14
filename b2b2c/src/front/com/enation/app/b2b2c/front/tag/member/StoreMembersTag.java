package com.enation.app.b2b2c.front.tag.member;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 获取本店铺人员标签
 * @author fk
 * @version v1.0
 * @since v6.2
 * 2016年12月13日 下午3:09:18
 */
@Component
public class StoreMembersTag extends BaseFreeMarkerTag{

	@Autowired
	private IStoreMemberManager storeMemberManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		
		StoreMember storeMember = storeMemberManager.getStoreMember();
		List list =  storeMemberManager.getMyStoreMembers(storeMember.getStore_id());
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("memberlist", list);
		return result;
	}

}
