package com.enation.app.b2b2c.front.api.order.publicmethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.goods.model.StoreProduct;
import com.enation.app.b2b2c.core.goods.service.StoreCartContainer;
import com.enation.app.b2b2c.core.order.model.cart.StoreCart;
import com.enation.app.b2b2c.core.order.model.cart.StoreCartItem;
import com.enation.app.b2b2c.core.order.service.cart.IStoreProductManager;
import com.enation.app.b2b2c.core.store.model.activity.StoreActivity;
import com.enation.app.b2b2c.core.store.service.activity.IStoreActivityManager;
import com.enation.app.base.core.service.IMemberManager;
import com.enation.app.base.core.service.ISettingService;
import com.enation.app.shop.core.goods.model.Goods;
import com.enation.app.shop.core.goods.service.IGoodsManager;
import com.enation.app.shop.core.goods.service.IProductManager;
import com.enation.app.shop.core.order.model.Cart;
import com.enation.app.shop.core.order.plugin.cart.CartPluginBundle;
import com.enation.app.shop.core.order.service.ICartManager;
import com.enation.app.shop.core.other.service.IActivityManager;
import com.enation.framework.action.JsonResult;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.JsonMessageUtil;
import com.enation.framework.util.JsonResultUtil;



/**
 * 将商品加入购物车 方法 抽出
 * @author Chopper
 * @version v1.0
 * @since v6.1.1
 * 2016年10月19日 下午3:34:04 
 *
 */
@Component
public class StoreCartPublicMethod {

	protected final Logger logger = Logger.getLogger(getClass());
	@Autowired
	private ICartManager cartManager;
	@Autowired
	private IProductManager productManager;
	@Autowired
	private IMemberManager memberManager;
	@Autowired
	private ISettingService settingService;
	@Autowired
	private IGoodsManager goodsManager;
	@Autowired
	private CartPluginBundle cartPluginBundle;
	@Autowired
	private IActivityManager activityManager;
	@Autowired
	private IStoreActivityManager storeActivityManager; 
	@Autowired
	private IStoreProductManager storeProductManager;
	
	/**
	 * 加入购物车
	 * @param product 产品
	 * @param num 数量
	 * @param showCartData 是否显示数量
	 * @return api结果集
	 */
	public JsonResult addCart(StoreProduct product,Integer num,Integer showCartData){
		String sessionid =ThreadContextHolder.getHttpRequest().getSession().getId();
		
		if(product!=null){
			try{
				//库存判定
				if(product.getStore()==null || product.getStore()<num){
					throw new RuntimeException("抱歉！您所选择的货品库存不足。");
				}
				//如果自己的购物车中有了，需要判断一下商品库存小于<购物车种的库存量+要购买的库存量
				Integer cartnum = 0;

				Cart dbcart = this.cartManager.getCartByProductId(product.getProduct_id(), sessionid);
				if(dbcart != null){
					cartnum+=dbcart.getNum();
				}
				if(product.getStore()<(num+cartnum)){
					throw new RuntimeException("抱歉！您所选择的货品库存不足。");
				}
				
				//上下架判定
				Goods good=goodsManager.getGoods(product.getGoods_id());
				if(good.getMarket_enable()!=1){
					return JsonResultUtil.getErrorJson("抱歉！您所选择的货品已经下架");
				}
				
				StoreCart cart = new StoreCart();
				cart.setGoods_id(product.getGoods_id());
				cart.setProduct_id(product.getProduct_id());
				cart.setSession_id(sessionid);
				cart.setNum(num);
				cart.setItemtype(0); //0为product和产品 ，当初是为了考虑有赠品什么的，可能有别的类型。
				cart.setWeight(product.getWeight());
				cart.setPrice( product.getPrice() );
				cart.setName(product.getName());
				cart.setStore_id(product.getStore_id());
				cart.setIs_check(1);
				Integer store_id = storeProductManager.getStoreByProductId(product.getProduct_id());
				//有效参数
				if(store_id!=0){
					//活动判定 使其可以过期
					StoreActivity act = storeActivityManager.getCurrentAct(store_id);
					if(act!=null){
						if(storeActivityManager.checkGoodsAct(product.getGoods_id(), act.getActivity_id())==1){
							cart.setActivity_end_time(act.getEnd_time());
							cart.setActivity_id(act.getActivity_id());
						}
					}
				}
				
				this.cartManager.add(cart); 

				//需要同时显示购物车信息
				if(showCartData!=null && showCartData==1){
					this.getCartData();
				}

				return JsonResultUtil.getSuccessJson("货品成功添加到购物车");
			}catch(RuntimeException e){
				this.logger.error("将货品添加至购物车出错",e);
				return JsonResultUtil.getErrorJson(e.getMessage());
			}
			
		}else{
			return JsonResultUtil.getErrorJson("该货品不存在，未能添加到购物车"); 
		}
	}
	
	/**
	 * 获取购物车数量
	 * @return 返回购物车数量
	 */
	public String getCartData(){
		try{
			String sessionid =ThreadContextHolder.getHttpRequest().getSession().getId();
			
			Double goodsTotal  =cartManager.countGoodsTotal( sessionid );
			int count = this.cartManager.countItemNum(sessionid);
			
			java.util.Map<String, Object> data =new HashMap();
			data.put("count", count);//购物车中的商品数量
			data.put("total", goodsTotal);//总价
			
			return JsonMessageUtil.getObjectJson(data);
			
		}catch(Throwable e ){
			this.logger.error("获取购物车数据出错",e);
			return JsonMessageUtil.getErrorJson("获取购物车数据出错");
		}
		 
	}
	
	
	
}
