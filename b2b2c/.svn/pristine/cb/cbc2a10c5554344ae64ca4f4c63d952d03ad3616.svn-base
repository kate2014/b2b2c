package com.enation.app.b2b2c.front.tag.goods;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.goods.service.IStoreGoodsManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
/**
 * 
 * (预警商品规格库存标签) 
 * @author zjp
 * @version v1.0
 * @since v6.2
 * 2016年12月12日 上午1:13:09
 */
@Component
@Scope("prototype")
public class WarningGoodsSpecStoreTag extends BaseFreeMarkerTag {
	@Autowired
	private IStoreGoodsManager storeGoodsManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Integer goods_id = (Integer) params.get("goods_id");
		Integer store_id = (Integer) params.get("store_id");
		if(goods_id==null){
			throw new TemplateModelException("请传入参数goods_id");
		}
		List list = this.storeGoodsManager.getWarningGoodsSpecStore(goods_id,store_id);
		return list;
	}
}
