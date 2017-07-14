package com.enation.app.b2b2c.core.order.model;

import com.enation.app.shop.core.order.model.Refund;

/**
 * 店铺退款单
 * @author Kanon
 * @version v1.0,2016-6-27
 * @since v3.1
 */
public class StoreRefund extends Refund{

	private Integer store_id;	//店铺Id
	private String store_name;	//店铺名称
	
	
	
	public Integer getStore_id() {
		return store_id;
	}

	public void setStore_id(Integer store_id) {
		this.store_id = store_id;
	}

	public String getStore_name() {
		return store_name;
	}

	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}

}
