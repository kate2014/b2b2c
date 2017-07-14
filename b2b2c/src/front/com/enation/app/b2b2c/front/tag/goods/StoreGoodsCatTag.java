package com.enation.app.b2b2c.front.tag.goods;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.shop.core.goods.model.Cat;
import com.enation.app.shop.core.goods.service.IGoodsCatManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.StringUtil;

import freemarker.template.TemplateModelException;
/**
 * 店铺商品分类标签
 * @author LiFenLong
 *
 */
@Component
public class StoreGoodsCatTag extends BaseFreeMarkerTag{
	@Autowired
	private IGoodsCatManager goodsCatManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Map result=new HashMap();
		int catid = StringUtil.toInt(params.get("catid").toString(),true);
		
		List<Cat> parentList=this.goodsCatManager.getParents(catid);
		//找到当前的父，以便确定商品类型id
		Cat currentCat = parentList.get(parentList.size()-1);
		result.put("typeid", currentCat.getType_id());
		result.put("catid", catid);
		result.put("parentList", parentList);
		result.put("cat", goodsCatManager.getById(catid));
		return result;
	}
}
