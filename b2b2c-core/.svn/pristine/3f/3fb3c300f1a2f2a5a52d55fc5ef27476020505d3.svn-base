package com.enation.app.b2b2c.component.bonus.plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.b2b2c.component.bonus.service.B2b2cBonusSession;
import com.enation.app.b2b2c.component.bonus.service.IB2b2cBonusManager;
import com.enation.app.shop.component.bonus.model.MemberBonus;
import com.enation.app.shop.core.order.model.Order;
import com.enation.app.shop.core.order.model.OrderMeta;
import com.enation.app.shop.core.order.model.support.CartItem;
import com.enation.app.shop.core.order.model.support.OrderPrice;
import com.enation.app.shop.core.order.plugin.order.IAfterOrderCreateEvent;
import com.enation.app.shop.core.order.plugin.order.IOrderCanelEvent;
import com.enation.app.shop.core.order.service.IOrderMetaManager;
import com.enation.framework.plugin.AutoRegisterPlugin;

/**
 * B2b2c优惠券插件<br>
 * @author xulipeng
 * @version v1.0
 * @since v6.2.1
 * 2017年01月06日
 */
@Component
@Scope("prototype")
public class B2b2cOrderBonusPlugin extends AutoRegisterPlugin  implements  IAfterOrderCreateEvent,IOrderCanelEvent {

	private  final String discount_key ="bonusdiscount";
	@Autowired
	private IOrderMetaManager orderMetaManager;
	@Autowired
	private IB2b2cBonusManager b2b2cBonusManager;
	
	/**
	 * 订单取消退换优惠券
	 */
	@Override
	public void canel(Order order) {
		//退回红包
		this.b2b2cBonusManager.returned(order.getOrder_id());
	}

	/**
	 * 订单创建完成后执行
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void onAfterOrderCreate(Order order, List<CartItem> itemList, String sessionid) {
		
		
		String sn = order.getSn();
		//目前只能用这种方法判断是否是子订单
		if(sn.indexOf("-") < 0){	//小于0则是主订单
			
			//读取所有的优惠券
			List<MemberBonus> bonusList =B2b2cBonusSession.get();
			if(bonusList==null || bonusList.isEmpty()){
				bonusList=new ArrayList<MemberBonus>();	
			}
			
			for (MemberBonus memberBonus : bonusList) {
				int bonusid =memberBonus.getBonus_id();
				int bonusTypeid= memberBonus.getBonus_type_id();
				this.b2b2cBonusManager.use(bonusid, order.getMember_id(), order.getOrder_id(),order.getSn(),bonusTypeid);
			}
			
		}else{
			//获取不到子订单的店铺id，获取某一个
			return;
		}
		
		OrderPrice orderPrice  = order.getOrderprice();
		Map disItems = orderPrice.getDiscountItem();
		
		Double bonusdiscount =(Double) disItems.get(discount_key);
		
		OrderMeta orderMeta = new OrderMeta();
		orderMeta.setOrderid(order.getOrder_id());
		
		if(bonusdiscount!=null){
			orderMeta.setMeta_key(discount_key);
			orderMeta.setMeta_value( String.valueOf( bonusdiscount) );
			this.orderMetaManager.add(orderMeta);
		}
	}

}
