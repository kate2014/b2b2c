package com.enation.app.b2b2c.front.tag.store;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.b2b2c.core.store.model.Store;
import com.enation.app.b2b2c.core.store.model.StoreSetting;
import com.enation.app.b2b2c.core.store.model.StoreThemes;
import com.enation.app.b2b2c.core.store.service.IStoreManager;
import com.enation.app.b2b2c.core.store.service.IStoreThemesManager;
import com.enation.eop.resource.model.EopSite;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 店铺模板列表标签
 * @author Kanon
 *
 */
@Component
public class StoreThemesInfoTag extends BaseFreeMarkerTag{
	@Autowired
	private IStoreThemesManager storeThemesManager;
	
	@Autowired
	private IStoreManager storeManager;
	
	@Autowired
	private IStoreMemberManager storeMemberManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		 
		//!!!!!!!!!这里缺少 获取当前店铺信息的server
		Map map=new HashMap();
		Store store=storeManager.getStoreByMember(storeMemberManager.getStoreMember().getMember_id());
		//获取模板目录
		String previewBasePath = "/themes/"+EopSite.getInstance().getThemepath()+StoreSetting.store_themes;
		//当前客户选择模板
		StoreThemes storeThemes=storeThemesManager.getStorethThemes(store.getThemes_id());
		
		//店铺选择模板图片
		map.put("previewpath", previewBasePath);
		
		//店铺选择模板图片
		map.put("storeThemes", storeThemes);
		
		//店铺模板列表
		map.put("storeThemesList", storeThemesManager.list(this.getPage(), this.getPageSize()));
		
		return map;
	}
}
