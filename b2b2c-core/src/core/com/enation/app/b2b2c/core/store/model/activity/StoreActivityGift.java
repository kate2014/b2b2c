package com.enation.app.b2b2c.core.store.model.activity;

import com.enation.app.shop.core.other.model.ActivityGift;

/**
 * 店铺促销活动赠品实体类
 * 
 * @author DMRain
 * @date 2016年1月13日
 * @version v1.0
 * @since v1.0
 */
public class StoreActivityGift extends ActivityGift{

	/**
	 * 店铺ID
	 */
	private Integer store_id;
	
	public Integer getStore_id() {
		return store_id;
	}
	
	public void setStore_id(Integer store_id) {
		this.store_id = store_id;
	}
}
