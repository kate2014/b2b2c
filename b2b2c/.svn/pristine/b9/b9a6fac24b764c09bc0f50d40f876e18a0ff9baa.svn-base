package com.enation.app.b2b2c.front.tag.goods;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.goods.service.IStoreGoodsManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
/**
 * 
 * (b2b2c获取预警商品库存标签) 
 * @author zjp
 * @version v1.0
 * @since v6.2
 * 2016年12月12日 上午12:33:18
 */
@Component

public class B2b2cWarningGoodsStoreTag  extends BaseFreeMarkerTag{
	@Autowired
	private IStoreGoodsManager storeGoodsManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Map result=new HashMap();
		result.put("store", storeGoodsManager.getB2b2cWarningGoodsStore((Integer)params.get("goods_id"),(Integer)params.get("store_id")));
		return result;
	}
}