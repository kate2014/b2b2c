package com.enation.app.b2b2c.component.plugin.store.setting;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.goods.service.IStoreGoodsManager;
import com.enation.app.b2b2c.core.store.model.StoreSetting;
import com.enation.app.base.core.plugin.setting.IOnSettingInputShow;
import com.enation.app.base.core.plugin.setting.IOnSettingSaveEnvent;
import com.enation.app.base.core.service.ISettingService;
import com.enation.framework.context.spring.SpringContextHolder;
import com.enation.framework.plugin.AutoRegisterPlugin;

/**
 * 店铺设置插件
 * @author Kanon
 *
 */
@Component
public class StoreSettingPlugin extends AutoRegisterPlugin implements IOnSettingInputShow,IOnSettingSaveEnvent{

	@Autowired
	private IStoreGoodsManager storeGoodsManager;
	
	@Override
	public String onShow() {
		
		return "store-setting";
	}

	@Override
	public String getSettingGroupName() {
		
		return StoreSetting.setting_key;
	}

	@Override
	public String getTabName() {
		
		return "店铺设置";
	}

	@Override
	public int getOrder() {
		
		return 3;
	}

	@Override
	public void onSave() {
		
		ISettingService settingService= SpringContextHolder.getBean("settingService");
		Map<String,String> settings = settingService.getSetting(StoreSetting.setting_key);
		if(settings==null){
			return ;
		}
		Integer auth = Integer.parseInt(settings.get("auth"));
		Integer edit_auth = Integer.parseInt(settings.get("edit_auth"));
		Integer self_auth = Integer.parseInt(settings.get("self_auth"));

		//商家不需要审核
		if(auth.intValue()==0 ){
			this.storeGoodsManager.editAllGoodsAuthAndMarketenable(0);
		
		}else if(self_auth.intValue()==0 && edit_auth.intValue()==0){  //自营不需要审核 && 修改不需要审核 
			this.storeGoodsManager.editAllGoodsAuthAndMarketenable(1);
		}
		
		StoreSetting.load();
		
	}


}
