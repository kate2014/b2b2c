package com.enation.app.b2b2c.front.tag.order.cart;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.goods.service.StoreCartContainer;
import com.enation.app.b2b2c.core.goods.service.StoreCartKeyEnum;
import com.enation.app.b2b2c.core.order.service.cart.IStoreCartManager;
import com.enation.app.shop.core.order.model.support.OrderPrice;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.CurrencyUtil;

import freemarker.template.TemplateModelException;

/**
 * @author LiFenLong
 *
 */
@Component
public class StoreCartSelectGoodsTag extends BaseFreeMarkerTag{
	@Autowired
	private IStoreCartManager storeCartManager;
	 
	/**
	 * 返回购物车中的购物列表
	 * @param 无 
	 * @return 购物列表 类型List<CartItem>
	 * {@link storeGoodsList}
	 */
	protected Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request  = ThreadContextHolder.getHttpRequest();
		
		//sessionid
		String sessionid = request.getSession().getId();
		//是否计算运费
		String isCountShip = (String)params.get("countship");
		this.storeCartManager.countSelectPrice(isCountShip);
		
		
		List<Map> storeCartList = StoreCartContainer.getSelectStoreCartListFromSession();
		
		for (Map map : storeCartList) {
			OrderPrice storeOrderPrice  =(OrderPrice) map.get(StoreCartKeyEnum.storeprice.toString());
			if(storeOrderPrice!=null){
				storeOrderPrice.setNeedPayMoney(CurrencyUtil.sub(storeOrderPrice.getNeedPayMoney(), storeOrderPrice.getDiscountPrice()));
			}
		}
		
		return StoreCartContainer.getSelectStoreCartListFromSession();
	}
 
 
	 
	
}
