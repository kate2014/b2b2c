package com.enation.app.b2b2c.component.plugin.order;

import java.util.Map;

import com.enation.app.shop.core.order.model.support.OrderPrice;
/**
 * 
 * 计算店铺价格事件
 * 〈功能详细描述〉
 * @author    jianghongyan
 * @version   1.0.0,2016年7月6日
 * @since     v6.1
 */
public interface ICountStorePriceEvent {

	/**
	 * 计算价格
	 * @param orderPrice
	 * @param map
	 * @return
	 */
	OrderPrice countPrice(OrderPrice orderPrice, Map map);

}
