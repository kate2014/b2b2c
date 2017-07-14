package com.enation.app.b2b2c.front.tag.order.orderReport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.shop.core.order.model.Order;
import com.enation.app.shop.core.order.model.SellBack;
import com.enation.app.shop.core.order.service.IOrderManager;
import com.enation.app.shop.core.order.service.ISellBackManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 退货申请标签
 * @author fenlongli
 *
 */
@Component
public class StoreSellBackTag extends BaseFreeMarkerTag {
	@Autowired
	private ISellBackManager sellBackManager;
	@Autowired
	private IOrderManager orderManager;
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Map map = new HashMap();
		Integer id = Integer.parseInt(params.get("id").toString());
		SellBack sellBackList = this.sellBackManager.get(id);
		Order orderInfo = orderManager.get(sellBackList.getOrdersn());
		List goodsList = this.sellBackManager.getGoodsList(id);
		List return_child_list = new ArrayList();
		
		for (int i = 0; i < goodsList.size(); i++) {
			Map mapTemp = (Map) goodsList.get(i);
			if(mapTemp.get("is_pack")==null){
				mapTemp.put("is_pack", "0");
			}
			int isPack = Integer.parseInt(mapTemp.get("is_pack").toString());
			if(isPack == 1){
				List list = this.sellBackManager.getSellbackChilds(orderInfo.getOrder_id(),Integer.parseInt(mapTemp.get("goodsId").toString()));
				if (list != null) {
					return_child_list.addAll(list);
				}
			}
		}

		map.put("sellBack", sellBackList);  //退货详细
		map.put("orderInfo",orderInfo);//订单详细
		map.put("goodsList",goodsList);//退货商品列表
		map.put("childGoodsList", return_child_list);//子商品详情列表
		return map;
	}
}
