package com.enation.app.b2b2c.component.plugin.order;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.enation.app.shop.core.order.model.support.OrderPrice;
import com.enation.app.shop.core.order.plugin.cart.ICartItemFilter;
import com.enation.app.shop.core.order.plugin.cart.ICountPriceEvent;
import com.enation.framework.plugin.AutoRegisterPluginsBundle;
import com.enation.framework.plugin.IPlugin;

/**
 * 店铺购物车插件桩
 * @author kingapex
 * @version 1.0
 * 2015年8月21日下午3:16:21
 */
@Service("storeCartPluginBundle")
public class StoreCartPluginBundle extends AutoRegisterPluginsBundle {

	/**
	 * 激发子订单价格计算事件
	 * @param orderpice
	 * @return
	 */
	public OrderPrice countChildPrice(OrderPrice orderpice){
		try{
			List<IPlugin> plugins = this.getPlugins();
			
			if (plugins != null) {
				for (IPlugin plugin : plugins) {
					if (plugin instanceof ICountChildOrderPriceEvent ) {
						if (loger.isDebugEnabled()) {
							loger.debug("调用插件 : " + plugin.getClass()+ "storecart.countPrice开始...");
						}
						ICountChildOrderPriceEvent event = (ICountChildOrderPriceEvent) plugin;
						orderpice =event.countChildPrice(orderpice);
						if (loger.isDebugEnabled()) {
							loger.debug("调用插件 : " + plugin.getClass()+ " storecart.countPrice结束.");
						}
					}else{
						if (loger.isDebugEnabled()) {
							loger.debug(" no,skip...");
						}
					}
				}
			}
		
			
		}catch(Exception e){
			e.printStackTrace();
			 
		}
		 
 		return orderpice;
	}
	
	
	
	@Override
	public String getName() {
		
		return "多店铺购物车插件桩";
	}



	public void filterGoodsList(List list, String sessionid) {
		try{
			List<IPlugin> plugins = this.getPlugins();
			
			if (plugins != null) {
				for (IPlugin plugin : plugins) {
					if (plugin instanceof ICartItemFilter) {
						if (loger.isDebugEnabled()) {
							loger.debug("调用插件 : " + plugin.getClass() + "cart.add开始...");
						}
						ICartItemFilter event = (ICartItemFilter) plugin;
						event.filter(list, sessionid);
						if (loger.isDebugEnabled()) {
							loger.debug("调用插件 : " + plugin.getClass() + " cart.add结束.");
						}
					}else{
						if (loger.isDebugEnabled()) {
							loger.debug(" no,skip...");
						}
					}
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
			 
		}
	}



	public void coutPrice(OrderPrice orderPrice, Map map) {
		try{
			List<IPlugin> plugins = this.getPlugins();
			
			if (plugins != null) {
				for (IPlugin plugin : plugins) {
					if (plugin instanceof ICountStorePriceEvent ) {
						if (loger.isDebugEnabled()) {
							loger.debug("调用插件 : " + plugin.getClass()+ "cart.countPrice开始...");
						}
						ICountStorePriceEvent event = (ICountStorePriceEvent) plugin;
						orderPrice =event.countPrice(orderPrice,map);
						if (loger.isDebugEnabled()) {
							loger.debug("调用插件 : " + plugin.getClass()+ " cart.countPrice结束.");
						}
					}else{
						if (loger.isDebugEnabled()) {
							loger.debug(" no,skip...");
						}
					}
				}
			}
		
			
		}catch(Exception e){
			e.printStackTrace();
			 
		}
	}


}
