package com.enation.app.b2b2c.core.store.model.activity;

import com.enation.app.shop.core.other.model.Activity;

/**
 * 多店铺促销活动实体类
 * @author DMRain
 * @date 2016年01月07日
 * @version v1.0
 * @since v1.0
 */
public class StoreActivity extends Activity{

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
