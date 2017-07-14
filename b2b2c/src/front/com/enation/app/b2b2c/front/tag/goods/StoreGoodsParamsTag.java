package com.enation.app.b2b2c.front.tag.goods;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.shop.core.goods.model.Cat;
import com.enation.app.shop.core.goods.model.GoodsType;
import com.enation.app.shop.core.goods.service.IGoodsCatManager;
import com.enation.app.shop.core.goods.service.IGoodsTypeManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.StringUtil;

import freemarker.template.TemplateModelException;

/**
 * 商品属性标签
 * @author fenlongli
 *
 */
@Component
public class StoreGoodsParamsTag extends BaseFreeMarkerTag {
	@Autowired
	private IGoodsCatManager goodsCatManager;
	@Autowired
	private IGoodsTypeManager goodsTypeManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Map result=new HashMap();
		Integer catid =Integer.parseInt(params.get("catid").toString());
		Cat cat =goodsCatManager.getById(catid);
		int typeid = cat.getType_id();
		GoodsType goodsType = goodsTypeManager.get(typeid);
		
		List attrList = this.goodsTypeManager.getAttrListByTypeId(typeid);
		
		if(goodsType.getJoin_brand()==1){
			List brandList = this.goodsTypeManager.listByTypeId(typeid);
			result.put("brandList", brandList);
		}
		result.put("attrList", attrList);
		return result;
	}
}
