package com.enation.app.b2b2c.front.tag.goods;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.goods.service.IStoreGoodsManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
/**
 * b2b2c获取商品库存标签
 * @author jianghongyan
 *
 */
public class B2b2cGoodsStoreTag  extends BaseFreeMarkerTag{
	@Autowired
	private IStoreGoodsManager storeGoodsManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Map result=new HashMap();
		result.put("store", storeGoodsManager.getB2b2cGoodsStore((Integer)params.get("goods_id")));
		return result;
	}
}