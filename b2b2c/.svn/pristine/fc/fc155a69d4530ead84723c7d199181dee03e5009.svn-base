package com.enation.app.b2b2c.front.tag.store;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.b2b2c.core.store.service.IStoreSildeManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
@Component
/**
 * 幻灯片列表
 * @author LiFenLong
 *
 */
public class StoreSlideListTag extends BaseFreeMarkerTag{
	@Autowired
	private IStoreSildeManager storeSildeManager;
	@Autowired
	private IStoreMemberManager storeMemberManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		
		Integer storeid = (Integer) params.get("storeid");
		if(storeid==null){
			StoreMember member=storeMemberManager.getStoreMember();;
			storeid = member.getStore_id();
		}
		return storeSildeManager.list(storeid);
	}
}
