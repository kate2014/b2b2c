package com.enation.app.b2b2c.core.order.service.cart;

import com.enation.app.b2b2c.core.goods.model.StoreProduct;

/**
 * 店铺商品管理类
 * @author LiFenLong
 *
 */
public interface IStoreProductManager {
	/**
	 * 读取某个商品的货品，一般用于无规格商品或捆绑商品
	 * @param goodsid
	 * @return StoreProduct
	 */
	public StoreProduct getByGoodsId(Integer goodsid);

	/**
	 * 读取货品详细
	 * @param productid
	 * @return
	 */
	public StoreProduct get(Integer productid);

	/**
	 * 读取店铺id
	 * @param productid 产品id
	 * @return 店铺id
	 */
	public Integer	getStoreByProductId(Integer productid);
	
}
