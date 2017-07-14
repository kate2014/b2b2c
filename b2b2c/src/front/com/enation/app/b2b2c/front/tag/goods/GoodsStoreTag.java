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
 * 获取商品库存
 * @author LiFenLong
 *
 */
public class GoodsStoreTag extends BaseFreeMarkerTag{
	@Autowired
	private IStoreGoodsManager storeGoodsManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Map result=new HashMap();
		result.put("store", storeGoodsManager.getGoodsStore((Integer)params.get("goods_id")));
		return result;
	}
}
