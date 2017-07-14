package com.enation.app.b2b2c.component.plugin.order;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DaoSupport;
import org.springframework.stereotype.Component;

import com.ctc.wstx.sw.BaseNsStreamWriter;
import com.enation.app.b2b2c.core.goods.model.B2b2cGoodsSnapshot;
import com.enation.app.b2b2c.core.goods.service.StoreCartContainer;
import com.enation.app.b2b2c.core.goods.service.StoreCartKeyEnum;
import com.enation.app.b2b2c.core.order.model.StoreOrder;
import com.enation.app.b2b2c.core.order.model.cart.StoreCartItem;
import com.enation.app.shop.core.goods.model.GoodsSnapshot;
import com.enation.app.shop.core.order.model.Order;
import com.enation.app.shop.core.order.model.support.CartItem;
import com.enation.app.shop.core.order.model.support.OrderPrice;
import com.enation.app.shop.core.order.plugin.cart.ICountPriceEvent;
import com.enation.app.shop.core.order.plugin.order.IAfterOrderCreateEvent;
import com.enation.app.shop.core.order.plugin.order.IBeforeOrderCreateEvent;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.plugin.AutoRegisterPlugin;
import com.enation.framework.util.CurrencyUtil;
/**
 * 
 * (b2b2c订单快照插件) 
 * @author zjp
 * @version v1.0
 * @since v6.1
 * 2016年12月6日 下午1:53:46
 */
@Component
public class B2b2cOrderSnapshotPlugin extends AutoRegisterPlugin implements IAfterOrderCreateEvent {
	@Autowired
	private IDaoSupport daoSupport;
	/**
	 * b2b2c快照表新加字段
	 */
	@Override
	public void onAfterOrderCreate(Order order, List<CartItem> itemList, String sessionid) {
		if(itemList.size()!=0){
			for (CartItem cartItem : itemList) {
				String sql = "select * from es_goods where goods_id=?";
				B2b2cGoodsSnapshot b2b2cGoodsSnapshot = daoSupport.queryForObject(sql, B2b2cGoodsSnapshot.class, cartItem.getGoods_id());
				b2b2cGoodsSnapshot.setMarket_enable(2);
				daoSupport.update("es_goods_snapshot", b2b2cGoodsSnapshot, "snapshot_id="+cartItem.getSnapshot_id());
			}
		}	
	}
}
