package com.enation.app.b2b2c.core.store.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import com.enation.app.b2b2c.core.store.model.StoreSetting;
import com.enation.app.base.core.service.ISettingService;
import com.enation.app.base.core.service.solution.IInstaller;
import com.enation.framework.database.IDaoSupport;
/**
 * 店铺设置安装器
 * @author Kanon
 *
 */
@Component
public class StoreSettingInstaller implements IInstaller{

	@Autowired
	private IDaoSupport daoSupport;
	
	@Autowired
	private ISettingService settingService;
	
	@Override
	public void install(String productId, Node fragment) {
		if(!"b2b2c".equals(productId)){
			return ;
		}
		daoSupport.execute("insert into es_settings (cfg_group) values ( '"+StoreSetting.setting_key+"')");
		
		Map settings = settingService.getSetting();
		Map systemSetting = new HashMap();
		systemSetting.put("auth", "0");
		systemSetting.put("edit_auth", "0");
		systemSetting.put("self_auth", "0");
		systemSetting.put("store_themes", "/store_themes/themes");
		settings.put(StoreSetting.setting_key, systemSetting);		 
		settingService.save(settings); //保存系统设置
		 
	}

}
