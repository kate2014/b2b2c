package com.enation.app.b2b2c.front.tag.goods;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.goods.model.StoreCat;
import com.enation.app.b2b2c.core.goods.service.IStoreGoodsCatManager;
import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;


@Component
public class StoreCatTag extends BaseFreeMarkerTag {
	@Autowired
	private IStoreGoodsCatManager storeGoodsCatManager;
	@Autowired
	private IStoreMemberManager storeMemberManager;

	@SuppressWarnings("unchecked")
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Integer catid = (Integer) params.get("catid");
		Map map = new HashMap();
		StoreCat storeCat = new StoreCat();
		try {
			StoreMember storeMember = storeMemberManager.getStoreMember();;
			map.put("storeid", storeMember.getStore_id());
			map.put("store_catid", catid);
			storeCat = this.storeGoodsCatManager.getStoreCat(map);
			
		} catch (Exception e) {
			this.logger.error("商品分类查询错误", e);
		}
		return storeCat;
	}
}
