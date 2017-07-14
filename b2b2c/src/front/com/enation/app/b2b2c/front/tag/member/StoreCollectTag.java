package com.enation.app.b2b2c.front.tag.member;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreCollectManager;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 收藏店铺标签
 * @author xulipeng
 *	2014年12月9日17:38:55
 */

@Component
@SuppressWarnings("unchecked")
public class StoreCollectTag extends BaseFreeMarkerTag {
	@Autowired
	private IStoreCollectManager storeCollectManager;
	@Autowired
	private IStoreMemberManager storeMemberManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		StoreMember storeMember =storeMemberManager.getStoreMember() ;
		Integer memberid = storeMember.getMember_id();
		Page webpage = this.storeCollectManager.getList(memberid,this.getPage(),this.getPageSize());
		
		Map result = new HashMap();
		//获取总记录数
		Long totalCount = webpage.getTotalCount();
		result.put("page", this.getPage());
		result.put("pageSize", this.getPageSize());
		result.put("totalCount", totalCount);
		result.put("storelist", webpage);
		return result;
	}
	
}
