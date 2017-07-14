package com.enation.app.b2b2c.core.order.service.cart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.b2b2c.component.bonus.model.StoreBonusType;
import com.enation.app.b2b2c.component.bonus.service.B2b2cBonusSession;
import com.enation.app.b2b2c.component.bonus.service.IB2b2cBonusManager;
import com.enation.app.b2b2c.component.plugin.order.StoreCartPluginBundle;
import com.enation.app.b2b2c.core.goods.service.StoreCartContainer;
import com.enation.app.b2b2c.core.goods.service.StoreCartKeyEnum;
import com.enation.app.b2b2c.core.order.model.cart.StoreCart;
import com.enation.app.b2b2c.core.order.model.cart.StoreCartItem;
import com.enation.app.b2b2c.core.store.model.activity.StoreActivity;
import com.enation.app.b2b2c.core.store.service.IStoreDlyTypeManager;
import com.enation.app.b2b2c.core.store.service.IStoreTemplateManager;
import com.enation.app.b2b2c.core.store.service.activity.IStoreActivityGiftManager;
import com.enation.app.b2b2c.core.store.service.activity.IStoreActivityManager;
import com.enation.app.b2b2c.front.api.order.publicmethod.StoreCartPublicMethod;
import com.enation.app.base.core.model.Member;
import com.enation.app.shop.component.bonus.model.MemberBonus;
import com.enation.app.shop.core.member.model.MemberAddress;
import com.enation.app.shop.core.member.service.IMemberAddressManager;
import com.enation.app.shop.core.order.model.Cart;
import com.enation.app.shop.core.order.model.support.OrderPrice;
import com.enation.app.shop.core.order.plugin.cart.CartPluginBundle;
import com.enation.app.shop.core.order.service.ICartManager;
import com.enation.app.shop.core.order.service.IDlyTypeManager;
import com.enation.app.shop.core.order.service.impl.CartManager;
import com.enation.app.shop.core.other.model.ActivityDetail;
import com.enation.app.shop.core.other.service.IActivityDetailManager;
import com.enation.app.shop.core.other.service.IActivityManager;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.util.CurrencyUtil;
import com.enation.framework.util.DateUtil;

@Service("storeCartManager")
public class StoreCartManager implements IStoreCartManager {

	protected final Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private CartPluginBundle cartPluginBundle;

	@Autowired
	private IDlyTypeManager dlyTypeManager;

	@Autowired
	private IStoreDlyTypeManager storeDlyTypeManager;

	@Autowired
	private IStoreTemplateManager storeTemplateManager;

	@Autowired
	private IMemberAddressManager memberAddressManager;

	@Autowired
	private ICartManager cartManager;

	/** 店铺促销活动管理接口 by_DMRain 2016-6-20 */
	@Autowired
	private IStoreActivityManager storeActivityManager;

	/** 促销活动优惠详细管理接口 by_DMRain 2016-6-20 */
	@Autowired
	private IActivityDetailManager activityDetailManager;

	/** 店铺促销活动赠品管理接口 by_DMRain 2016-6-20 */
	@Autowired
	private IStoreActivityGiftManager storeActivityGiftManager;

	/** 店铺优惠券管理接口 by_xulipeng 2017年01月05日 */
	@Autowired
	private IB2b2cBonusManager b2b2cBonusManager;

	/**店铺购物车插件桩  add by jianghongyan 2016-7-1*/
	@Autowired
	private StoreCartPluginBundle storeCartPluginBundle;
	
	/** 促销活动管理接口 by_DMRain 2016-7-13 */
	@Autowired
	private IActivityManager activityManager;
	@Autowired
	private StoreCartPublicMethod storeCartPublicMethod;
	@Autowired
	private IStoreProductManager storeProductManager;
	@Autowired
	private IDaoSupport daoSupport;
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.order.service.cart.IStoreCartManager#countPrice(java.lang.String)
	 */
	@Override
	public void countPrice(String isCountShip) {

		HttpServletRequest request  = ThreadContextHolder.getHttpRequest();

		//sessionid
		String sessionid = request.getSession().getId();

		//获取各个店铺购物车列表
		List<Map> storeGoodsList=storeListGoods(sessionid);

		//用户的默认地区（自己建立的或者根据ip得到的）
		Integer regionid = this.memberAddressManager.getMemberDefaultRegionId();

		//如果计算价格说明进入结算页了，那么要将用户的默认地址压入session
		if("yes".equals(isCountShip)){

			/**
			 * 获取结算时的用户地址，优先级为：
			 * 1.用户选择过的
			 * 2.用户建立的默认地址
			 * 3.根据用户所在ip的到
			 */
			MemberAddress address=this.getCheckoutAddress();
			if(address!=null){
				regionid=address.getTown_id();
				if(regionid==null){
					regionid=address.getRegion_id();
				}
				if(regionid==null){
					regionid=address.getCity_id();
				}
				if(regionid==null){
					regionid=address.getProvince_id();
				}
			}

		}



		//循环每个店铺，计算各种费用，如果要求计算运费还要根据配送方式和地区计算运费
		for(Map map : storeGoodsList){ 
			
			List list = (List) map.get(StoreCartKeyEnum.goodslist.toString());

			//获取促销活动id add by DMRain 2016-7-13
			Integer activity_id = (Integer) map.get("activity_id");

			//计算店铺价格，不计算运费
			OrderPrice orderPrice =   this.cartManager.countPrice(list, null, null);

			/**
			 * 打入计算价格插件桩  add by jianghongyan 2016-07-04
			 */
			this.storeCartPluginBundle.coutPrice(orderPrice, map);
			
			
			//为店铺信息压入店铺的各种价格
			map.put(StoreCartKeyEnum.storeprice.toString(), orderPrice);

			//如果指定计算运费，则计算每个店的的运费，并设置好配送方式列表，在结算页中此参数应该设置为yes
			if("yes".equals(isCountShip)){
				orderPrice =this.countShipPrice(map,regionid);
			}
			
			//如果促销活动id不为空
			if (activity_id != null) {
				//如果促销活动有效或者还未结束 add by DMRain 2016-7-13
				if (this.activityManager.checkActivity(activity_id) == 0) {
					//获取促销活动优惠详细
					StoreActivity activity = this.storeActivityManager.get(activity_id);
						
					//获取参加促销活动商品价格总计
					Double actTotalPrice = this.getTotalPrice(list);

					//如果促销活动信息不为空
					if(activity != null){

						ActivityDetail detail = this.activityDetailManager.getDetail(activity.getActivity_id());

						Double op = orderPrice.getOrderPrice();
						Double need = orderPrice.getNeedPayMoney();

						//如果参加促销活动商品价格总计大于或者等于促销活动一系列优惠的基础钱数
						if(actTotalPrice >= detail.getFull_money()){
							//如果促销含有减现金的活动
							if (detail.getIs_full_minus() == 1) {
								orderPrice.setActDiscount(detail.getMinus_value());
								op = CurrencyUtil.sub(op, detail.getMinus_value());
								need = CurrencyUtil.sub(need, detail.getMinus_value());
								orderPrice.setOrderPrice(op);
								orderPrice.setNeedPayMoney(need);
							}

							//如果促销含有送积分的活动
							if(detail.getIs_send_point() == 1){
								orderPrice.setActivity_point(detail.getPoint_value());
							}

							//如果促销含有送赠品的活动
							if(detail.getIs_send_gift() == 1){
								//获取赠品的可用库存
								Integer enable_store = this.storeActivityGiftManager.get(detail.getGift_id()).getEnable_store();

								//如果赠品的可用库存大于0
								if (enable_store > 0) {
									orderPrice.setGift_id(detail.getGift_id());
								}
							}

							//如果促销含有送优惠券的活动
							if(detail.getIs_send_bonus() == 1){
								//获取店铺优惠券信息
								StoreBonusType bonus =	this.b2b2cBonusManager.getBonus(detail.getBonus_id());

								//优惠券发行量
								int createNum = bonus.getCreate_num();	

								//获取优惠券已被领取的数量
								int count = this.b2b2cBonusManager.getCountBonus(detail.getBonus_id());

								//如果优惠券的发行量大于已经被领取的优惠券数量
								if (createNum > count) {
									orderPrice.setBonus_id(detail.getBonus_id());
								}
							}
						}
					}
				}
			}
		} 
		//向session中压入购物车列表
		StoreCartContainer.putStoreCartListToSession(storeGoodsList);
	}

	@Override
	public void countSelectPrice(String isCountShip) {

		HttpServletRequest request  = ThreadContextHolder.getHttpRequest();

		//sessionid
		String sessionid = request.getSession().getId();

		//获取各个店铺购物车列表
		List<Map> storeGoodsList=storeCheckListGoods(sessionid);

		//用户的默认地区（自己建立的或者根据ip得到的）
		Integer regionid = this.memberAddressManager.getMemberDefaultRegionId();

		//如果计算价格说明进入结算页了，那么要将用户的默认地址压入session
		if("yes".equals(isCountShip)){

			/**
			 * 获取结算时的用户地址，优先级为：
			 * 1.用户选择过的
			 * 2.用户建立的默认地址
			 * 3.根据用户所在ip的到
			 */
			MemberAddress address=this.getCheckoutAddress();
			if(address!=null){
				regionid=address.getTown_id();
				if(regionid==null || regionid<0){
					regionid=null;
				}
				if(regionid==null){
					regionid=address.getRegion_id();
				}
				if(regionid==null){
					regionid=address.getCity_id();
				}
				if(regionid==null){
					regionid=address.getProvince_id();
				}
			}
		}


		//循环每个店铺，计算各种费用，如果要求计算运费还要根据配送方式和地区计算运费
		for(Map map : storeGoodsList){ 
			List list = (List) map.get(StoreCartKeyEnum.goodslist.toString());

			//获取促销活动id add by DMRain 2016-7-13
			Integer activity_id = (Integer) map.get("activity_id");
			
			//计算店铺价格，不计算运费
			OrderPrice orderPrice =   this.cartManager.countPrice(list, null, null);
 
			//店铺金额追加优惠券优惠
			int store_id=Integer.parseInt(map.get("store_id").toString());
			MemberBonus memberbonus = B2b2cBonusSession.getB2b2cBonus(store_id);
			if (memberbonus != null) { 
				selectBonus(orderPrice, memberbonus, store_id);
			}
			
			/**
			 * 打入计算价格插件桩  add by jianghongyan 2016-07-04
			 */
			this.storeCartPluginBundle.coutPrice(orderPrice, map);
			
			//为店铺信息压入店铺的各种价格
			map.put(StoreCartKeyEnum.storeprice.toString(), orderPrice);

			//如果指定计算运费，则计算每个店的的运费，并设置好配送方式列表，在结算页中此参数应该设置为yes
			if("yes".equals(isCountShip)){
				orderPrice =this.countShipPrice(map,regionid);
			}
			
			//如果促销活动id不为空	--判断促销活动 start--
			if (activity_id != null) {
				//如果促销活动有效或者还未结束 add by DMRain 2016-7-13
				if (this.activityManager.checkActivity(activity_id) == 0) {
					//获取促销活动优惠详细
					StoreActivity activity = this.storeActivityManager.get(activity_id);
					
					//获取参加促销活动商品价格总计
					Double actTotalPrice = this.getTotalPrice(list);

					//如果促销活动信息不为空
					if(activity != null){

						ActivityDetail detail = this.activityDetailManager.getDetail(activity.getActivity_id());
						
						Double op = orderPrice.getOrderPrice();
						Double need = orderPrice.getNeedPayMoney();

						//如果参加促销活动商品价格总计大于或者等于促销活动一系列优惠的基础钱数
						if(actTotalPrice >= detail.getFull_money()){
							//如果促销含有减现金的活动
							if (detail.getIs_full_minus() == 1) {
								orderPrice.setActDiscount(detail.getMinus_value());
								op = CurrencyUtil.sub(op, detail.getMinus_value());
								need = CurrencyUtil.sub(need, detail.getMinus_value());
								orderPrice.setOrderPrice(op);
								orderPrice.setNeedPayMoney(need);
							}

							//如果促销含有送积分的活动
							if(detail.getIs_send_point() == 1){
								orderPrice.setActivity_point(detail.getPoint_value());
							}

							//如果促销含有送赠品的活动
							if(detail.getIs_send_gift() == 1){
								//获取赠品的可用库存
								Integer enable_store = this.storeActivityGiftManager.get(detail.getGift_id()).getEnable_store();

								//如果赠品的可用库存大于0
								if (enable_store > 0) {
									orderPrice.setGift_id(detail.getGift_id());
								}
							}

							//如果促销含有送优惠券的活动
							if(detail.getIs_send_bonus() == 1){
								//获取店铺优惠券信息
								StoreBonusType bonus =	this.b2b2cBonusManager.getBonus(detail.getBonus_id());

								//优惠券发行量
								int createNum = bonus.getCreate_num();	

								//获取优惠券已被领取的数量
								int count = this.b2b2cBonusManager.getCountBonus(detail.getBonus_id());

								//如果优惠券的发行量大于已经被领取的优惠券数量
								if (createNum > count) {
									orderPrice.setBonus_id(detail.getBonus_id());
								}
							}
						}
					}
				}
			}	//--判断促销活动 end--
		} 
		//向session中压入购物车列表
		StoreCartContainer.putSelectStoreCartListToSession(storeGoodsList);
	}


	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.cart.IStoreCartManager#listGoods(java.lang.String)
	 */
	@Override
	public List<StoreCartItem> listGoods(String sessionid) {
		StringBuffer sql = new StringBuffer();

		sql.append("select ");
		/**
		 * 打入插件桩 cartPluginBundle 的filterListGoodsSql方法  add by jianghongyan 2016-06-23
		 */
		this.cartPluginBundle.filterListGoodsSql(sql);
		sql.append(" c.is_check,g.cat_id as catid,s.store_id as store_id,s.store_name as store_name,p.weight AS weight,c.cart_id as id,g.goods_id,g.goods_type as goods_type,g.thumbnail as image_default,g.goods_transfee_charge as goods_transfee_charge,c.name ,  p.sn, p.specs  ,g.mktprice,g.unit,g.point,p.product_id,c.price,c.cart_id as cart_id,c.num as num,c.itemtype,c.addon,c.activity_id, c.price  as coupPrice from es_cart c,es_product p,es_goods g ,es_store s ");
		sql.append("where c.itemtype=0 and c.product_id=p.product_id and p.goods_id= g.goods_id  AND c.store_id=s.store_id ");
		
		List list  =null;


		/**
		 * 获取当前登陆会员
		 * 如果已经登陆过则通过会员id来读取
		 */
		Member member  =UserConext.getCurrentMember();
		if(member==null){
			sql.append(" and c.session_id=?");
			list = this.daoSupport.queryForList(sql.toString(),StoreCartItem.class, sessionid);
		}else{
			sql.append(" and c.member_id=?");
			list = this.daoSupport.queryForList(sql.toString(), StoreCartItem.class, member.getMember_id());
		}
		//循环判断购物车中的货品是否参加了促销活动，如果参加了促销活动，判断促销活动是否已结束或已失效，如果已结束或已失效，就将此购物项中的促销活动id置为空
		//add_by DMRain 2016-7-12
		for (Object object : list) {
			StoreCartItem cart = (StoreCartItem) object;
			Integer act_id = cart.getActivity_id();
			if (act_id != null && act_id != 0) {
				int result = this.activityManager.checkActivity(act_id);
				if (result == 1) {
					this.daoSupport.execute("update es_cart set activity_id = null where cart_id = ?", cart.getId());
					cart.setActivity_id(0);
				}
			}
		}
		
		this.cartPluginBundle.filterList(list, sessionid);
		
		/**
		 * 打入插件桩storeCartPluginBundle 的filterListGoods方法  add by jianghongyan 2016-06-23
		 */
		this.storeCartPluginBundle.filterGoodsList(list,sessionid);
		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.order.service.cart.IStoreCartManager#storeListGoods(java.lang.String)
	 */
	@Override
	public List<Map> storeListGoods(String sessionid) {
		
		//更新需要同步的商品
		this.reReorganization();
		
		List<Map> storeGoodsList= new ArrayList<Map>();
		List<StoreCartItem> goodsList =new ArrayList();

		goodsList= this.listGoods(sessionid); //商品列表
		for (StoreCartItem item : goodsList) {
			findStoreMap(storeGoodsList, item);
		}
		return storeGoodsList;
	}


	/**
	 * 获取店铺商品列表
	 * @param storeGoodsList
	 * @param map
	 * @param StoreCartItem
	 * @return list<Map>
	 */
	private void findStoreMap(List<Map> storeGoodsList,StoreCartItem item){
		int is_store=0;
		if (storeGoodsList.isEmpty()){
			addGoodsList(item, storeGoodsList);
		}else{
			for (Map map: storeGoodsList) {
				if(map.get("store_id").equals(item.getStore_id())){
					List list=(List) map.get(StoreCartKeyEnum.goodslist.toString());
					list.add(item);
					is_store=1;
				}
			}
			if(is_store==0){
				addGoodsList(item, storeGoodsList);
			}
		}
	}

	/**
	 * 添加至店铺列表
	 * @param item
	 * @param storeGoodsList
	 */
	private void addGoodsList(StoreCartItem item,List<Map> storeGoodsList){
		Map map=new HashMap();
		List list=new ArrayList();
		list.add(item);
		map.put(StoreCartKeyEnum.store_id.toString(), item.getStore_id());
		map.put(StoreCartKeyEnum.store_name.toString(), item.getStore_name());
		map.put(StoreCartKeyEnum.goodslist.toString(), list);

		StoreActivity storeActivity = new StoreActivity();
		storeActivity = this.storeActivityManager.getCurrentAct(item.getStore_id());

		//若促销活动详细不为空
		if(storeActivity != null){
			map.put(StoreCartKeyEnum.activity_id.toString(), storeActivity.getActivity_id());
			map.put(StoreCartKeyEnum.activity_name.toString(), storeActivity.getActivity_name());
		}

		storeGoodsList.add(map);
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.order.service.cart.IStoreCartManager#clean(java.lang.String)
	 */
	@Override
	public void  clean(String sessionid){
		String sql ="delete from es_cart where session_id=?";

		this.daoSupport.execute(sql, sessionid);
	}


	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.order.service.cart.IStoreCartManager#updatePriceByProductid(java.lang.Integer, java.lang.Double)
	 */
	@Override
	public void updatePriceByProductid(Integer product_id,Double price) {
		String sql = "update es_cart set price=? where product_id=?";
		this.daoSupport.execute(sql, price,product_id);
	}

	@Override
	public void checkStoreAll(String sessionid, boolean checked, Integer store_id) {
		Member member  =UserConext.getCurrentMember();
		StringBuffer sql=new StringBuffer("update es_cart set is_check=? where store_id=?");

		if(member==null){
			sql.append(" and session_id=?");
			this.daoSupport.execute(sql.toString(), checked==true?1:0,store_id,sessionid);
		}else{
			sql.append(" and member_id=?");
			this.daoSupport.execute(sql.toString(), checked==true?1:0, store_id,member.getMember_id());
		}
	}
	/**
	 * 获取参加促销活动商品价格总计
	 * add by DMRain 2016-1-12
	 * @param cartItemList
	 * @return
	 */
	private Double getTotalPrice(List<StoreCartItem> cartItemList){
		Double actTotalPrice = 0d;
		Double sameGoodsTotal = 0d;

		for(StoreCartItem cartItem : cartItemList){
			Integer activity_id = cartItem.getActivity_id();

			//如果促销活动信息ID不为空
			if(activity_id != null){
				sameGoodsTotal = CurrencyUtil.mul(cartItem.getPrice(), cartItem.getNum());
				actTotalPrice = CurrencyUtil.add(actTotalPrice, sameGoodsTotal);
			}
		}

		return actTotalPrice;
	}

	/**
	 * 获取结算时的用户地址，优先级为：
	 * 1.用户选择过的
	 * 2.用户建立的默认地址
	 * @return 用戶地址
	 */
	private MemberAddress getCheckoutAddress(){
		Member member = UserConext.getCurrentMember();
		if(member!=null){


			/**
			 * 先检查是否选择过地区，有选择过，则使用用户选择的地址。
			 */
			MemberAddress address= StoreCartContainer.getUserSelectedAddress();
			if(address!=null){
				return address;
			}


			/**
			 * 如果用户没有选择过收货地址，则使用用户的默认地址
			 */
			address= memberAddressManager.getMemberDefault(member.getMember_id());
			if(address!=null){

				/**
				 * 同时将默认地址设置为用户的选择过的地址
				 */
				if(StoreCartContainer.getUserSelectedAddress()==null){
					StoreCartContainer.putSelectedAddress(address);
				}

				return address;

			}


		}

		return null;
	}



	@SuppressWarnings({ "rawtypes", "unchecked" })
	private OrderPrice countShipPrice(Map map,int regionid){
		//是否免运费
		boolean free_ship=false;
		int storeid= (Integer)map.get(StoreCartKeyEnum.store_id.toString());
		List<StoreCartItem> list = (List) map.get(StoreCartKeyEnum.goodslist.toString());
		Integer activity_id = (Integer) map.get(StoreCartKeyEnum.activity_id.toString());
		
		//先检测购物商品中是否有免运费的
		for (StoreCartItem storeCartItem : list) {
			//如果有一个商品免运费则此店铺订单免运费
			if (1== storeCartItem.getGoods_transfee_charge()){
				free_ship=true;
				break;
			}
		}

		//取出之前计算好的订单价格
		OrderPrice orderPrice = (OrderPrice)map.get(StoreCartKeyEnum.storeprice.toString());
		
		//得到商品总计和重量，以便计算运费之用
		Double goodsprice = orderPrice.getGoodsPrice();
		Double weight = orderPrice.getWeight();


		//生成配送方式列表，此map中已经含有计算后的运费
		List<Map> shipList  = this.getShipTypeList(storeid, regionid, weight, goodsprice);
		
		//向店铺信息中压入配送方式列表
		map.put(StoreCartKeyEnum.shiptype_list.toString(),shipList);

		//如果免运费，向配送方式列表中加入免运费项
		if(free_ship ||shipList.isEmpty()){
			//修改逻辑问题 edit by jianghongyan
			
				//生成免运费项
				Map freeType = new HashMap();
				freeType.put("type_id", 0);
				freeType.put("name", "免运费");
				freeType.put("shipPrice", 0);
				shipList.clear();
				//将免运费项加入在第一项
				shipList.add(0, freeType);

				//设置运费价格和配送方式id
				orderPrice.setShippingPrice(0d);
				map.put(StoreCartKeyEnum.shiptypeid.toString(), 0);
			
		}else{

			//如果不免运费用第一个配送方式 设置运费价格和配送方式id
			Map firstShipType =shipList.get(0);
			Double shipprice = (Double)firstShipType.get("shipPrice");

			Double actTotalPrice = this.getTotalPrice(list);

			//为判断商品是否参与的免邮活动
			int is_free_ship=0;
			
			//如果促销活动不为空并且参加促销活动商品价格总计大于活动条件基数以及促销活动包含满免邮活动
			//add by DMRain 2016-1-12
			if(activity_id != null){

				ActivityDetail activityDetail = this.activityDetailManager.getDetail(activity_id);

				if(actTotalPrice >= activityDetail.getFull_money()){
					if(activityDetail.getIs_free_ship() == 1){	//判断是否开启免邮活动
						is_free_ship =1;
						orderPrice.setAct_free_ship(shipprice);
						shipprice = 0d;
					}
				}
			}

			orderPrice.setShippingPrice(shipprice);
			//订单总费用为商品价格+运费
			double orderTotal = CurrencyUtil.add(goodsprice, shipprice);
			orderPrice.setOrderPrice(orderTotal);
			
			// xulipeng   修复：当商品中有一个参与了免邮的活动那么此店铺所有的商品都免邮  2016年12月14日 
			if(is_free_ship==1){
				//生成免运费项
				Map freeType = new HashMap();
				freeType.put("type_id", 0);
				freeType.put("name", "免运费");
				freeType.put("shipPrice", 0);
				shipList.clear();
				//将免运费项加入在第一项
				shipList.add(0, freeType);
				map.put(StoreCartKeyEnum.shiptypeid.toString(), 0);
			}else{
				map.put(StoreCartKeyEnum.shiptypeid.toString(), firstShipType.get("type_id"));
			}

		}
		
		orderPrice.setNeedPayMoney(orderPrice.getOrderPrice());
		return orderPrice;
	}



	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<Map> getShipTypeList(int storeid,int regionid,double weight,double goodsprice){
		List<Map> newList  = new ArrayList();
		if(Double.valueOf(weight)!=0d){
			Integer tempid = this.storeTemplateManager.getDefTempid(storeid);
			List<Map> list =   this.storeDlyTypeManager.getDlyTypeList(tempid);

			for(Map maps:list){
				Map newMap = new HashMap();
				String name = (String)maps.get("name");
				Integer typeid = (Integer) maps.get("type_id");
				Double[] priceArray = this.dlyTypeManager.countPrice(typeid, Double.valueOf(weight), goodsprice, regionid+"");
				Double dlyPrice = priceArray[0];//配送费用
				newMap.put("name", name);
				newMap.put("type_id", typeid);
				newMap.put("shipPrice", dlyPrice);
				newList.add(newMap);
			}
		}

		return newList;
	}


	private List<StoreCartItem> checkListGoods(String sessionid) {
		StringBuffer sql = new StringBuffer();

		sql.append("select ");
		/**
		 * 打入插件桩的filterSelectListGoods方法  add by jianghongyan 2016-6-23
		 */
		this.cartPluginBundle.filterSelectListGoods(sql);
		sql.append(" g.cat_id as catid,s.store_id as store_id,s.store_name as store_name,p.weight AS weight,c.cart_id as id,g.goods_id,g.goods_type as goods_type,g.thumbnail as image_default,g.goods_transfee_charge as goods_transfee_charge,c.name ,  p.sn, p.specs  ,g.mktprice,g.unit,g.point,p.product_id,c.price,c.cart_id as cart_id,c.num as num,c.itemtype,c.addon,c.activity_id,c.price  as coupPrice from es_cart c,es_product p,es_goods g ,es_store s ");
		sql.append("where c.itemtype=0 and c.product_id=p.product_id and p.goods_id= g.goods_id  AND c.store_id=s.store_id AND c.is_check=1");
		//System.out.println(sql);
		List list = null;


		/**
		 * 获取当前登陆会员
		 * 如果已经登陆过则通过会员id来读取
		 */
		Member member  =UserConext.getCurrentMember();
		if(member==null){
			sql.append(" and c.session_id=?");
			list = this.daoSupport.queryForList(sql.toString(),StoreCartItem.class, sessionid);
		}else{
			sql.append(" and c.member_id=?");
			list = this.daoSupport.queryForList(sql.toString(), StoreCartItem.class, member.getMember_id());
		}

		//循环判断购物车中的货品是否参加了促销活动，如果参加了促销活动，判断促销活动是否已结束或已失效，如果已结束或已失效，就将此购物项中的促销活动id置为空
		//add_by DMRain 2016-7-12
		for (Object object : list) {
			StoreCartItem cart = (StoreCartItem) object;
			Integer act_id = cart.getActivity_id();
			if (act_id != null && act_id != 0) {
				int result = this.activityManager.checkActivity(act_id);
				if (result == 1) {
					this.daoSupport.execute("update es_cart set activity_id = null where cart_id = ?", cart.getId());
					cart.setActivity_id(0);
				}
			}
		}
		
		cartPluginBundle.filterList(list, sessionid);
		/**
		 * 打入插件桩storeCartPluginBundle 的filterListGoods方法  add by DMRain 2016-07-13
		 */
		this.storeCartPluginBundle.filterGoodsList(list,sessionid);

		return list;
	}

	private List<Map> storeCheckListGoods(String sessionid) {
		List<Map> storeGoodsList= new ArrayList<Map>();
		List<StoreCartItem> goodsList =new ArrayList();

		goodsList= this.checkListGoods(sessionid); //商品列表
		for (StoreCartItem item : goodsList) {
			findStoreMap(storeGoodsList, item);
		}
		return storeGoodsList;
	}
	
	/**
	 * 使用优惠券
	 * 
	 * @author liushuai
	 * @version v1.0,2015年9月22日18:21:33
	 * @param bonuss
	 * @since v1.0
	 */
	private OrderPrice selectBonus(OrderPrice orderprice, MemberBonus bonus,
			Integer storeid) {

		// 计算需要支付的金额
		orderprice.setNeedPayMoney(CurrencyUtil.add(orderprice.getNeedPayMoney(), -bonus.getType_money()));
		//订单优惠金额
		orderprice.setDiscountPrice(bonus.getType_money());
		//订单使用的优惠券
//		orderprice.setBonus_id(bonus.getBonus_type_id());
		return orderprice;

	}

	@Override
	public void changeAll(Integer store_id) {
		this.daoSupport.execute("update es_cart set is_change = 1 where store_id = ? ", store_id);
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.order.service.cart.IStoreCartManager#reReorganization()
	 */
	@Override
	public void reReorganization() {
		String sessionid = ThreadContextHolder.getHttpRequest().getSession().getId();
		Member member  =UserConext.getCurrentMember();
		List<StoreCart> carts;
		StringBuffer sql = new StringBuffer("select * from es_cart where ");
		if(member==null){
			sql.append(" session_id=?");
			carts = this.daoSupport.queryForList(sql.toString(), StoreCart.class, sessionid);
		}else{
			sql.append(" member_id=?");
			carts = this.daoSupport.queryForList(sql.toString(), StoreCart.class, member.getMember_id());
		}
		if(carts.size()==0){
			return;
		}
		for (Cart cart : carts) {
			//判断是否过期
			boolean isExpired = false;
			if(cart.getActivity_end_time()!=null){
				if(cart.getActivity_end_time()<DateUtil.getDateline()){
					isExpired=true;
				}
			}
			
			//如果需要更新
			if(cart.getIs_change()==1||isExpired){
				try {
					//购物车消息
					ThreadContextHolder.getSession().setAttribute(CartManager.CART_MESSAGE, CartManager.CART_MESSAGE_DEFAULT);
					//删除重新添加
					cartManager.delete(sessionid, cart.getCart_id());
					storeCartPublicMethod.addCart(storeProductManager.get(cart.getProduct_id()), cart.getNum(),0);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("重新加入购物车失败",e);
				}
			}
		}
		
	}
	
}
