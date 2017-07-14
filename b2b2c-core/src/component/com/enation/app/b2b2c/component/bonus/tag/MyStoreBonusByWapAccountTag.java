package com.enation.app.b2b2c.component.bonus.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.component.bonus.model.StoreBonus;
import com.enation.app.b2b2c.component.bonus.service.B2b2cBonusSession;
import com.enation.app.b2b2c.component.bonus.service.IB2b2cBonusManager;
import com.enation.app.b2b2c.core.goods.service.StoreCartContainer;
import com.enation.app.b2b2c.core.goods.service.StoreCartKeyEnum;
import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.b2b2c.core.store.model.Store;
import com.enation.app.b2b2c.core.store.service.IStoreManager;
import com.enation.app.shop.component.bonus.model.Bonus;
import com.enation.app.shop.component.bonus.model.MemberBonus;
import com.enation.app.shop.core.order.model.support.OrderPrice;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.JsonResultUtil;

import freemarker.template.TemplateModelException;

/**
 * 结算页—我的优惠券列表(限B2b2c-wap端使用)
 * 描述：根据已选的购物车商品，查询所有店铺可用的优惠券集合
 * 返回格式：Map{totalNum:*,totalStoreBonus:List<StoreBonus>} 
 * <br>其中totalNum：所有店铺(可用或者不可用)优惠券数量,totalStoreBonus:所有店铺的(可用或者不可用)优惠券对象
 * @author xulipeng
 * @version v1.0
 * @since v6.2.1
 */
@Component
@Scope("prototype")
public class MyStoreBonusByWapAccountTag extends BaseFreeMarkerTag {

	@Autowired
	private IB2b2cBonusManager b2b2cBonusManager;
	@Autowired
	private IStoreMemberManager storeMemberManager;
	@Autowired
	private IStoreManager storeManager;
	
	/**
	 * @param	is_usable	1为可用，0为不可用。默认为1
	 * @return	返回所有店铺优惠券列表
	 */
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		
		StoreMember member = this.storeMemberManager.getStoreMember();
		if(member ==null){
			return JsonResultUtil.getErrorJson("未登录，不能使用此api");
		}
		
		//判断 是否可用参数
		Integer is_usable = (Integer) params.get("is_usable");
		if(is_usable==null){
			is_usable=1;
		}
		
		//默认为1页
		int page =1;
		//默认查询的条数
		int pageSize = 100;
		
		//所有店铺优惠券的数量（计数）
		long totalNum=0;
		
		//所有店铺优惠券集合
		List<StoreBonus> totalStoreBonus = new ArrayList();
		
		//由session中获取已选中购物车列表
		List<Map> storeGoodsList = StoreCartContainer.getSelectStoreCartListFromSession();
		
		//购物车没有商品或者进入过购物车界面
		if(storeGoodsList==null){
			return null;
		}
		
		// 以店铺分单位循环购物车列表
		for (Map map : storeGoodsList) {
			
			// 获取此店铺id
			int store_id = (Integer) map.get(StoreCartKeyEnum.store_id.toString());
			
			// 获取店铺名称
			String store_name = (String) map.get(StoreCartKeyEnum.store_name.toString());
			
			// 调用核心api计算总订单的价格，商品价：所有商品，商品重量：
			OrderPrice orderPrice = (OrderPrice) map.get(StoreCartKeyEnum.storeprice.toString());
			
			//查询此店铺的可用或者不可用优惠券数量
			Page webpage = this.b2b2cBonusManager.getMyBonusByIsUsable(page,pageSize, member.getMember_id(), is_usable, orderPrice.getGoodsPrice(), store_id);
			
			Long totalCount = webpage.getTotalCount();
			List<Bonus> bonusList = (List) webpage.getResult();
			bonusList = bonusList == null ? new ArrayList() : bonusList;
			
			//获得此店铺已使用的优惠券
			MemberBonus memberBonus = B2b2cBonusSession.getB2b2cBonus(store_id);
			
			//判断是否为空 并且 查询的是可用优惠券
			if(memberBonus!=null && is_usable.intValue()==1){
				
				for (Bonus bonus : bonusList) {
					//如果相等 设为已使用的状态
					if(bonus.getBonus_id().equals(memberBonus.getBonus_id())){
						bonus.setIs_used(1);
					}else{
						bonus.setIs_used(0);
					}
				}
			}
			
			//生成店铺优惠券对象
			StoreBonus storeBonus = new StoreBonus();
			storeBonus.setStore_id(store_id);
			storeBonus.setStore_name(store_name);
			storeBonus.setBonusList(bonusList);
			
			//所有店铺优惠券数量 计数
			totalNum += totalCount;
			
			totalStoreBonus.add(storeBonus);
		}
		
		Map map = new HashMap();
		map.put("totalNum", totalNum);
		map.put("totalStoreBonus", totalStoreBonus);
		
		return map;
	}

}
