package com.enation.app.b2b2c.front.tag.store;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.goods.service.IStoreGoodsManager;
import com.enation.app.b2b2c.core.order.service.cart.IStoreCartManager;
import com.enation.app.b2b2c.core.store.service.IStoreDlyTypeManager;
import com.enation.app.b2b2c.core.store.service.IStoreTemplateManager;
import com.enation.app.shop.core.order.service.IDlyTypeManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 店铺配送方式标签
 * @author xulipeng
 *	2015年1月13日15:46:16
 */

@Component
public class StoreDlytypeTag extends BaseFreeMarkerTag {
	@Autowired
	private IDlyTypeManager dlyTypeManager;
	
	@Autowired
	private IStoreDlyTypeManager storeDlyTypeManager;
	
	@Autowired
	private IStoreTemplateManager storeTemplateManager;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Integer storeid= (Integer) params.get("storeid");
		Integer regionid = (Integer) params.get("regionid");
		String originalPrice = (String) params.get("originalPrice");
		String weight = (String) params.get("weight");
		
		List<Map> list = new ArrayList<Map>();
		if(Double.valueOf(weight)!=0d){
			Integer tempid = this.storeTemplateManager.getDefTempid(storeid);
			list = this.storeDlyTypeManager.getDlyTypeList(tempid);
			for(Map maps:list){
				Integer typeid = (Integer) maps.get("type_id");
				Double[] priceArray = this.dlyTypeManager.countPrice(typeid, Double.valueOf(weight), Double.valueOf(originalPrice), regionid+"");
				Double dlyPrice = priceArray[0];//配送费用
				maps.put("dlyPrice", dlyPrice);
			}
		}
		
		return list;
	}
	
	


}
