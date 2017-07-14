package com.enation.app.b2b2c.component.plugin.store.bill;

import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.component.plugin.store.IStoreTabShowEvent;
import com.enation.app.b2b2c.core.store.model.Store;
import com.enation.eop.processor.core.freemarker.FreeMarkerPaser;
import com.enation.framework.plugin.AutoRegisterPlugin;

/**
 * 店铺结算信息Tab页插件
 * @author Kanon
 *
 */
@Component
public class StoreBillDataPlugin extends AutoRegisterPlugin implements IStoreTabShowEvent{

	@Override
	public String getTabName() {
		return "账户信息";
	}

	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return 3;
	}

	@Override
	public String getEditTabHtml(Store store) {
		FreeMarkerPaser freeMarkerPaser = FreeMarkerPaser.getInstance();
		freeMarkerPaser.setPageName("bill");
		freeMarkerPaser.putData("store",store);
		
		return freeMarkerPaser.proessPageContent();
	}

	@Override
	public String getAddTabHtml() {
		FreeMarkerPaser freeMarkerPaser = FreeMarkerPaser.getInstance();
		freeMarkerPaser.setPageName("add_bill");
		
		return freeMarkerPaser.proessPageContent();
	}

}
