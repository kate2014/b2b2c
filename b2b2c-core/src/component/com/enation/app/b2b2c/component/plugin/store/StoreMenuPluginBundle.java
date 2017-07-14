package com.enation.app.b2b2c.component.plugin.store;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.store.model.Store;
import com.enation.app.base.core.model.PluginTab;
import com.enation.eop.processor.core.freemarker.FreeMarkerPaser;
import com.enation.framework.plugin.AutoRegisterPluginsBundle;
import com.enation.framework.plugin.IPlugin;
/**
 * 店铺菜单插件桩
 * @author zh
 * @version v1.0
 * @since v6.1
 * 2016年11月8日 下午5:41:13
 */
@Component
public class StoreMenuPluginBundle extends AutoRegisterPluginsBundle{

	@Override
	public String getName() {
		return "店铺菜单插件桩";
	}

	public void onStoreMenuShow(List<Map<String, Object>> lowerMenuList){
		try{
			List<IPlugin> plugins = this.getPlugins();

			if (plugins != null) {
				for (IPlugin plugin : plugins) {
					if (plugin instanceof IStoreMenuShowEvent) {
						if (loger.isDebugEnabled()) {
							loger.debug("调用插件 : " +plugin.getClass() + " onAfterPass 开始...");
						}
						IStoreMenuShowEvent event = (IStoreMenuShowEvent) plugin;
						event.onStoreMenuShow(lowerMenuList);
						if (loger.isDebugEnabled()) {
							loger.debug("调用插件 : " +plugin.getClass() + " onAfterPass 结束.");
						}
					} 
				}
			}
		}catch(RuntimeException  e){
			if(this.loger.isErrorEnabled())
				this.loger.error("调用店铺插件[店铺通过]事件错误", e);
			throw e;
		}
	}
}
