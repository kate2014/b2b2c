package com.enation.app.b2b2c.front.tag.goods;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.shop.core.goods.service.IGoodsCatManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
@Component
public class GoodsParentsTypeTag extends BaseFreeMarkerTag{

	@Autowired
	private IGoodsCatManager goodsCatManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		List list = goodsCatManager.getGoodsParentsType(); 
		return list;
	}

}
