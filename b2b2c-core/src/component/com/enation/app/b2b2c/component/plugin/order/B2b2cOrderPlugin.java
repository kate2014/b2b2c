package com.enation.app.b2b2c.component.plugin.order;

import java.util.List;
import java.util.Map;


import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.component.bonus.service.B2b2cBonusSession;
import com.enation.app.b2b2c.core.goods.service.StoreCartContainer;
import com.enation.app.b2b2c.core.goods.service.StoreCartKeyEnum;
import com.enation.app.shop.core.order.model.support.OrderPrice;
import com.enation.app.shop.core.order.plugin.cart.ICountPriceEvent;
import com.enation.framework.plugin.AutoRegisterPlugin;
import com.enation.framework.util.CurrencyUtil;
/**
 * @author LiFenLong
 *b2b2c订单插件
 */
@Component
public class B2b2cOrderPlugin extends AutoRegisterPlugin implements ICountPriceEvent{
	
	 
	@Override
	public OrderPrice countPrice(OrderPrice orderprice) {
		
		//购物车列表，按店铺分类的。
		List<Map> list  = StoreCartContainer.getSelectStoreCartListFromSession();
		
		if(list==null) return orderprice;
		
		//订单总计
		double orderTotal =0D;
		
		//商品总计
		double goodsTotal =0D;
		
		//运费总计
		double shipTotal=0D;
		
		//优惠总计
		double disTotal=0D;
		
		/** 促销总计 add by DMRain 2016-1-11 */
        double promotionTotal=0D;
		
        /** 促销免邮总计 add by DMRain 2016-6-28 */
        double freeShipTotal=0D;
        
		//应付总计
		double payTotal =0D;
		
		for (Map map : list) {
			OrderPrice storeOrderPrice  =(OrderPrice) map.get(StoreCartKeyEnum.storeprice.toString());
			
			//累计订单总价
			Double storeOrderTotal = storeOrderPrice.getOrderPrice();
			orderTotal=CurrencyUtil.add(orderTotal, storeOrderTotal);
			
			//累计商品总价
			Double storeGoodsTotal = storeOrderPrice.getGoodsPrice();
			goodsTotal= CurrencyUtil.add(goodsTotal, storeGoodsTotal);
			
			//累计运费总价
			Double orderShipTotal = storeOrderPrice.getShippingPrice();
			shipTotal= CurrencyUtil.add(shipTotal, orderShipTotal);
			
//			//累计优惠总价		修改为从优惠券session中读取总优惠金额  by_xulipeng 2017年01月06日
//			Double storeDisTotal = storeOrderPrice.getDiscountPrice();
//			disTotal=CurrencyUtil.add(disTotal, storeDisTotal);
			
			//累计促销总价  add by DMRain 2016-1-11
            Double storePromotionTotal = storeOrderPrice.getActDiscount();
            //如果店铺促销活动减价为空 add by DMRain 2016-1-11
            if (storePromotionTotal == null) {
            	storePromotionTotal = 0D;
            }
            promotionTotal = CurrencyUtil.add(promotionTotal, storePromotionTotal);
			/**
			 * 这里注释掉 解决免邮费活动的问题
			 */
            //促销活动免运费总计 add by DMRain 2016-6-28
            //Double storeFreeShipTotal = storeOrderPrice.getAct_free_ship();
            //如果店铺促销活动免运费为空 add by DMRain 2016-6-28
//            if (storeFreeShipTotal == null) {
//				storeFreeShipTotal = 0D;
//			}
//            freeShipTotal = CurrencyUtil.add(freeShipTotal, storeFreeShipTotal); //促销活动免运费总计 add by DMRain 2016-6-28
//            payTotal = CurrencyUtil.sub(payTotal, storeFreeShipTotal); //应付总计应该减去店铺促销活动减免的运费 add by DMRain 2016-6-28
//            shipTotal = CurrencyUtil.add(shipTotal, storeFreeShipTotal); //运费总计应该加上店铺促销活动减免的运费 add by DMRain 2016-6-28
            
			//累计应付总价
			Double storePayTotal = storeOrderPrice.getNeedPayMoney();
			payTotal=CurrencyUtil.add(payTotal, storePayTotal);
		}
		
		//如果优惠金额后订单价格小于0	by_xulipeng  2017年01月18日 
		if(payTotal<=0){
			payTotal = 0d;
		}
		
		orderprice.setDiscountPrice(B2b2cBonusSession.getUseMoney());	//修改为从优惠券session中读取总优惠金额  by_xulipeng 2017年01月06日
		orderprice.setActDiscount(promotionTotal); // add by DMRain 2016-1-11
		orderprice.setAct_free_ship(freeShipTotal); //add by DMRain 2016-6-28
		orderprice.setGoodsPrice(goodsTotal);
		orderprice.setNeedPayMoney(payTotal);
		orderprice.setOrderPrice(orderTotal);
		orderprice.setShippingPrice(shipTotal);
		return orderprice;
	}
	
}
