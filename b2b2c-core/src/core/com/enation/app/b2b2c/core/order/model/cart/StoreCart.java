package com.enation.app.b2b2c.core.order.model.cart;

import com.enation.app.shop.core.order.model.Cart;

/**
 * 店铺购物车实体
 * @author LiFenLong
 *
 */
public class StoreCart extends Cart{
	private Integer store_id;

	public Integer getStore_id() {
		return store_id;
	}

	public void setStore_id(Integer store_id) {
		this.store_id = store_id;
	}
}
