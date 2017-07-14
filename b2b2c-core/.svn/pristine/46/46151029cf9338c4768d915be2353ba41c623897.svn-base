package com.enation.app.b2b2c.core.order.service.cart.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.b2b2c.core.goods.model.StoreProduct;
import com.enation.app.b2b2c.core.order.service.cart.IStoreProductManager;
import com.enation.framework.database.IDaoSupport;
@Service("storeProductManager")
public class StoreProductManager  implements IStoreProductManager {

	final Logger logger = Logger.getLogger(getClass()); 
	@Autowired
	private IDaoSupport daoSupport;
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.order.service.cart.IStoreProductManager#getByGoodsId(java.lang.Integer)
	 */
	@Override
	public StoreProduct getByGoodsId(Integer goodsid) {
		String sql ="select * from es_product where goods_id=?";
		List<StoreProduct> proList  =this.daoSupport.queryForList(sql, StoreProduct.class, goodsid);
		if(proList==null || proList.isEmpty()){
			return null;
		}
		return proList.get(0);
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.order.service.cart.IStoreProductManager#get(java.lang.Integer)
	 */
	public StoreProduct get(Integer productid) {
		String sql ="select * from es_product where product_id=?";
		return (StoreProduct) this.daoSupport.queryForObject(sql, StoreProduct.class, productid);
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.order.service.cart.IStoreProductManager#getStoreByProductId(java.lang.Integer)
	 */
	@Override
	public Integer getStoreByProductId(Integer productid) {
		try {
			return this.daoSupport.queryForInt("select store_id from es_goods g inner join es_product p on p.goods_id = g.goods_id where product_id = ?", productid);
		} catch (Exception e) {
			logger.error("错误的产品id"+productid, e);
			return 0;
		}
	}

}
