package com.enation.app.b2b2c.component.plugin.store.certification;

import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.component.plugin.store.IStoreTabShowEvent;
import com.enation.app.b2b2c.core.store.model.Store;
import com.enation.eop.processor.core.freemarker.FreeMarkerPaser;
import com.enation.framework.plugin.AutoRegisterPlugin;
/**
 * 店铺认证信息Tab插件
 * @author Kanon
 *
 */
@Component
public class StoreCertificationDataPlugin extends AutoRegisterPlugin implements IStoreTabShowEvent{

	@Override
	public String getTabName() {
		// TODO Auto-generated method stub
		return "认证信息";
	}

	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return 2;
	}


	@Override
	public String getEditTabHtml(Store store) {
		FreeMarkerPaser freeMarkerPaser = FreeMarkerPaser.getInstance();
		freeMarkerPaser.setPageName("certification");
		freeMarkerPaser.putData("store",store);
		
		return freeMarkerPaser.proessPageContent();
	}
	@Override
	public String getAddTabHtml() {
		FreeMarkerPaser freeMarkerPaser = FreeMarkerPaser.getInstance();
		freeMarkerPaser.setPageName("add_certification");
		
		return freeMarkerPaser.proessPageContent();
	}

}
