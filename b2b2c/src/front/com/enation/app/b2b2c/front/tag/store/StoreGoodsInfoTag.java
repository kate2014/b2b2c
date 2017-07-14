package com.enation.app.b2b2c.front.tag.store;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.shop.component.gallery.model.GoodsGallery;
import com.enation.app.shop.component.gallery.service.IGoodsGalleryManager;
import com.enation.app.shop.core.goods.service.IGoodsManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.StringUtil;

import freemarker.template.TemplateModelException;
/**
 * 店铺商品信息
 * @author LiFenLong
 *
 */
@Component
public class StoreGoodsInfoTag extends BaseFreeMarkerTag{
	
	@Autowired
	private IGoodsManager goodsManager;
	
	@Autowired
	private IGoodsGalleryManager goodsGalleryManager;
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Map result=new HashMap();
		List<GoodsGallery> galleryList = goodsGalleryManager.list(Integer.valueOf(params.get("goods_id").toString()));
		
		result.put("galleryList", galleryList);
		
		Map goods=goodsManager.get(Integer.valueOf(params.get("goods_id").toString()));
		/**
		 * add by jianghongyan 如果不是本店商品 转发到404页面
		 */
		if(params.containsKey("store_id")){
			Integer store_id= Integer.valueOf(params.get("store_id").toString());
			if(store_id==null || !store_id.equals(StringUtil.toInt(goods.get("store_id").toString(),true)) ) {
				this.redirectTo404Html();
			}
		}
		//goods.put("original",UploadUtil.replacePath(goods.get("original").toString()));
		this.getRequest().setAttribute("goods", goods);
		result.put("goods",goods);
		return result;
	}
}
