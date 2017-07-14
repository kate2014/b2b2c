package com.enation.app.b2b2c.front.tag.order.cart;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.goods.service.StoreCartContainer;
import com.enation.app.b2b2c.core.order.service.cart.IStoreCartManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * @author LiFenLong
 *
 */
@Component
public class StoreCartGoodsTag extends BaseFreeMarkerTag{
	@Autowired
	private IStoreCartManager storeCartManager;
	 
	/**
	 * 返回购物车中的购物列表
	 * @param 无 
	 * @return 购物列表 类型List<CartItem>
	 * {@link storeGoodsList}
	 */
	protected Object exec(Map params) throws TemplateModelException {
		
		//是否计算运费
		String isCountShip = (String)params.get("countship");
		this.storeCartManager.countPrice(isCountShip);
		
		return StoreCartContainer.getStoreCartListFromSession();
	}
 
 
	 
	
}
