package com.enation.app.b2b2c.front.tag.store.activity;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.goods.model.StoreGoods;
import com.enation.app.b2b2c.core.goods.service.IStoreGoodsManager;
import com.enation.app.b2b2c.core.store.model.activity.StoreActivity;
import com.enation.app.b2b2c.core.store.service.activity.IStoreActivityManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 检查商品当前是否参加了促销活动Tag
 * @author DMRain
 * @date 2016年1月28日
 * @version v1.0
 * @since v1.0
 */
@Component
public class CheckStoreGoodsPartActTag extends BaseFreeMarkerTag{
	
	@Autowired
	private IStoreActivityManager storeActivityManager;
	
	@Autowired
	private IStoreGoodsManager storeGoodsManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Integer goods_id = (Integer) params.get("goods_id");
		StoreGoods storeGoods = this.storeGoodsManager.getGoods(goods_id);
		Integer store_id = storeGoods.getStore_id();
		
		StoreActivity activity = this.storeActivityManager.getCurrentAct(store_id);
		
		Map result = new HashMap();
		
		//如果促销活动信息为空
		if(activity != null){
			
			//如果促销活动为全部商品参加（1：全部商品参加，2：部分商品参加）
			if(activity.getRange_type() == 1){
				result.put("activity", activity);
			}else if(activity.getRange_type() == 2){
				int num = this.storeActivityManager.checkGoodsAct(goods_id, activity.getActivity_id());
				
				//如果商品参加了促销活动
				if(num == 1){
					result.put("activity", activity);
				}
			}
		}
		
		return result;
	}
}
