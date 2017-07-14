package com.enation.app.b2b2c.front.tag.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.component.bonus.service.B2b2cBonusSession;
import com.enation.app.b2b2c.component.bonus.service.IB2b2cBonusManager;
import com.enation.app.b2b2c.core.goods.service.StoreCartContainer;
import com.enation.app.b2b2c.core.goods.service.StoreCartKeyEnum;
import com.enation.app.b2b2c.core.order.service.cart.IStoreCartManager;
import com.enation.app.shop.component.bonus.model.MemberBonus;
import com.enation.app.shop.component.bonus.service.BonusSession;
import com.enation.app.shop.core.order.model.support.OrderPrice;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.StringUtil;

import freemarker.template.TemplateModelException;

/**
 * b2b2c订单结算检测。是否因修改了购物车的数量等而导致优惠券、实体券等的可用性<br>
 * 检测信息有：<br>1.检测已使用的优惠券<br>2.检测已使用的实体券<br>
 * @author xulipeng
 * @version v1.0
 * @since	v6.2.1
 */
@Component
@Scope("prototype")
public class B2b2cOrderSettleCheckTag extends BaseFreeMarkerTag {

	@Autowired
	private IB2b2cBonusManager b2b2cBonusManager;
	@Autowired
	private IStoreCartManager storeCartManager;
	
	/**
	 * 
	 * @return result：1检测有问题，0则为无问题 ， errorMessager：错误提示
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Map errormap = new HashMap();
		errormap.put("result", 0);
		
		//由session中获取已选中购物车列表
		List<Map> storeGoodsList = StoreCartContainer.getSelectStoreCartListFromSession();
		
		//购物车没有商品或者进入过购物车界面
		if(storeGoodsList==null){
			return null;
		}
		
		StringBuffer errorMessage = new StringBuffer();
		
		// 以店铺分单位循环购物车列表
		for (Map map : storeGoodsList) {
			
			// 获取此店铺id
			int store_id = (Integer) map.get(StoreCartKeyEnum.store_id.toString());
			
			// 获取店铺名称
			String store_name = (String) map.get(StoreCartKeyEnum.store_name.toString());
			// 调用核心api计算总订单的价格，商品价：所有商品，商品重量：
			OrderPrice orderPrice = (OrderPrice) map.get(StoreCartKeyEnum.storeprice.toString());
			
			//获得此店铺已使用的优惠券
			MemberBonus memberBonus = B2b2cBonusSession.getB2b2cBonus(store_id);
			//判断不为空 如果商品总价小于优惠券最低使用限额，则清空已使用的优惠券
			if(memberBonus!=null && orderPrice.getGoodsPrice().doubleValue()<memberBonus.getMin_goods_amount()){
				B2b2cBonusSession.cancelB2b2cBonus(store_id);
				errorMessage.append( "["+store_name+"]店铺优惠券不可用，请重新选择。\\n");
			}
			
		}
		
		//检测是否有问题
		if(!StringUtil.isEmpty(errorMessage.toString())){
			errormap.put("errorMessage", errorMessage);
			errormap.put("result", 1);
		}
		
		return errormap;
	}

}
