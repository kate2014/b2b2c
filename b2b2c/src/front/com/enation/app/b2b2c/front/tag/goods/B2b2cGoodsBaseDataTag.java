package com.enation.app.b2b2c.front.tag.goods;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.shop.core.goods.plugin.GoodsPluginBundle;
import com.enation.app.shop.core.goods.plugin.search.GoodsDataFilterBundle;
import com.enation.app.shop.core.goods.service.IGoodsManager;
import com.enation.eop.processor.core.UrlNotFoundException;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.ObjectNotFoundException;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
@Component
public class B2b2cGoodsBaseDataTag extends BaseFreeMarkerTag{
	@Autowired
	private IGoodsManager goodsManager;
	@Autowired
	private GoodsDataFilterBundle goodsDataFilterBundle;
	@Autowired
	private GoodsPluginBundle goodsPluginBundle;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		try{ 
			HttpServletRequest request=ThreadContextHolder.getHttpRequest();
			 Integer goods_id=Integer.parseInt(request.getParameter("goodsid"));
			 if(goods_id==null){
				 throw new UrlNotFoundException();
			 }
			 Map goodsMap = goodsManager.get(goods_id);
			  
			 /**
			  * 如果商品不存在抛出页面找不到异常 
			  */
			 if(goodsMap==null){
				 throw new UrlNotFoundException();
			 }
			 /**
			  * 如果已下架抛出页面找不到异常 
			  */
			 if(goodsMap.get("market_enable").toString().equals("0")){
				 throw new UrlNotFoundException();
			 }
			 /**
			  * 如果已删除（到回收站）抛出页面找不到异常 
			  */
			 if(goodsMap.get("disabled").toString().equals("1")){
				 throw new UrlNotFoundException();
			 }
			 
			 List<Map> goodsList  = new ArrayList<Map>();
			 goodsList.add(goodsMap);
			 this.goodsDataFilterBundle.filterGoodsData(goodsList);
			 
			 this.getRequest().setAttribute("goods", goodsMap);
			 goodsPluginBundle.onVisit(goodsMap);
			 
			 return goodsMap;
			
		}catch(ObjectNotFoundException e){
			 throw new UrlNotFoundException();
		}
	}
}
