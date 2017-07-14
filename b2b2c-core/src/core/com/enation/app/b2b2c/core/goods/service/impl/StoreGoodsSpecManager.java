package com.enation.app.b2b2c.core.goods.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.b2b2c.core.goods.model.StoreGoods;
import com.enation.app.b2b2c.core.goods.service.IStoreGoodsManager;
import com.enation.app.b2b2c.core.goods.service.IStoreGoodsSpecManager;
import com.enation.app.shop.core.goods.model.Product;
import com.enation.app.shop.core.goods.service.IProductManager;
import com.enation.eop.processor.core.freemarker.FreeMarkerPaser;
import com.enation.framework.database.IDaoSupport;

/**
 * 店铺规格商品管理类
 * @author Kanon 2016-3-2;6.0版本改造
 *
 */
@Service("storeGoodsSpecManager")
public class StoreGoodsSpecManager  implements IStoreGoodsSpecManager {
	
	@Autowired
	private IProductManager productManager;
	@Autowired
	private IStoreGoodsManager storeGoodsManager;
	@Autowired
	private IDaoSupport daoSupport;
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.goods.service.IStoreGoodsSpecManager#getStoreHtml(java.lang.Integer)
	 */
	@Override
	public String getStoreHtml(Integer goodsid) {
		List<String> specNameList = productManager.listSpecName(goodsid);

		FreeMarkerPaser freeMarkerPaser = FreeMarkerPaser.getInstance();
		freeMarkerPaser.setClz(this.getClass());
		freeMarkerPaser.putData("specNameList", specNameList);
		freeMarkerPaser.putData("productList", this.listGoodsStore(goodsid));
		freeMarkerPaser.setPageName("store_info");
		return  freeMarkerPaser.proessPageContent();
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.goods.service.IStoreGoodsSpecManager#listGoodsStore(int)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<Map> listGoodsStore(int goodsid) {
		/**
		 * 首先取出这个商品的所有货品以及规格信息
		 */
		List<Product> prolist= productManager.list(goodsid);
		
		//根据商品id查询出店铺id
		StoreGoods goods=this.storeGoodsManager.getGoods(goodsid);
		
		String sql ="select * from es_product_store where goodsid=?";
		List<Map> storeList = this.daoSupport.queryForList(sql, goodsid);
		
		List<Map> pList = new ArrayList<Map>();
		
		for(Product product:prolist){
			Map pro = new HashMap();
			
			pro.put("specList",product.getSpecList());
			pro.put("sn", product.getSn());
			pro.put("name",product.getName());
			pro.put("product_id", product.getProduct_id());
			pro.put("storeid", 0);
			pro.put("store", 0);
			//店铺id
			pro.put("store_id", goods.getStore_id());
			for(Map store:storeList){
				
				//找到此仓库、此货品
				if(1 == ((Integer)store.get("depotid")).intValue() 
					&&  product.getProduct_id().intValue()== ((Integer)store.get("productid")).intValue() 
						
				){
					pro.put("storeid",(Integer)store.get("storeid"));
					pro.put("store", (Integer)store.get("store") );
					
				} 
				
			}
			
			pList.add(pro);
		}
		
		return pList;
	}
}
