package com.enation.app.b2b2c.component.bonus.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.enation.app.shop.component.bonus.model.Bonus;
import com.enation.app.shop.component.bonus.model.MemberBonus;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.CurrencyUtil;

/**
 * 多店的优惠券session管理
 * @author xulipeng
 * @version v1.0
 * @since v6.2.1
 * 2017年01月04日15:39:29
 */
@SuppressWarnings({ "unused","unchecked","rawtypes" })
public class B2b2cBonusSession {
	
	//所有的已使用的优惠券集合
	private static final String B2B2C_LIST_SESSION_KEY="b2b2c_bonus_list_session_key"; 
	//使用优惠券存放的session key
	private static final String B2B2C_BONUS_SESSION_KEY ="b2b2c_session_key";
	
	
	/**
	 * 使用一个优惠券
	 * @param sotreid	店铺id
	 * @param bonus		会员红包对象
	 */
	public static void useBonus(int sotreid,MemberBonus bonus){
		//添加到session
		Map<Integer,MemberBonus> bonusMap = (Map)ThreadContextHolder.getSession().getAttribute(B2B2C_BONUS_SESSION_KEY);
		if(bonusMap==null){
			bonusMap= new HashMap<Integer, MemberBonus>();
		}
		bonusMap.put(sotreid,bonus); 
		ThreadContextHolder.getSession().setAttribute(B2B2C_BONUS_SESSION_KEY, bonusMap);
	}
	
	
	/**
	 * 取消某个店铺的优惠券
	 * @param store_id
	 */
	public static void cancelB2b2cBonus(int store_id){
		Map<Integer,MemberBonus> bonusMap = (Map)ThreadContextHolder.getSession().getAttribute(B2B2C_BONUS_SESSION_KEY);
		
		//如果不为空
		if (bonusMap!=null&&!bonusMap.isEmpty()) {
			bonusMap.remove(store_id);
			ThreadContextHolder.getSession().setAttribute(B2B2C_BONUS_SESSION_KEY, bonusMap); 
		}
	}
	
	/**
	 * 读取某个店铺的优惠券
	 * @param store_id
	 */
	public static MemberBonus getB2b2cBonus(int store_id){
		Map<Integer,MemberBonus> bonusMap = (Map)ThreadContextHolder.getSession().getAttribute(B2B2C_BONUS_SESSION_KEY);
		
		//如果不为空，则返回对应店铺的优惠券
		if (bonusMap != null && !bonusMap.isEmpty()) {
			if(bonusMap.containsKey(store_id)) {
				return bonusMap.get(store_id); 
			}
		}
		return null;
	}
	
	
	/**
	 * 获取已使用的优惠券集合，已同步所有已使用的优惠券。
	 * @return
	 */
	public static List<MemberBonus> get(){
		//同步所有已使用的优惠券
		syncAllBonus();
		return  (List)ThreadContextHolder.getSession().getAttribute(B2B2C_LIST_SESSION_KEY);
	}
	

	/**
	 * 同步所有已使用的优惠券
	 */
	public static void syncAllBonus(){
		Map<Integer,MemberBonus> bonusMap = (Map)ThreadContextHolder.getSession().getAttribute(B2B2C_BONUS_SESSION_KEY);
		if(bonusMap==null){
			return ;
		}
		List<MemberBonus> bounsList = new ArrayList<MemberBonus>();
		for (MemberBonus memberBonus : bonusMap.values()) { 
			bounsList.add(memberBonus);
		}
		ThreadContextHolder.getSession().setAttribute(B2B2C_LIST_SESSION_KEY, bounsList);
	}
	
	
	/**
	 * 获取已使用的所有（优惠券）金额
	 * @return
	 */
	public static double getUseMoney(){
		List<MemberBonus> bonusList =get();
		double moneyCount = 0;
		if(bonusList!=null){
			for (MemberBonus memberBonus : bonusList) {
				double bonusMoney = memberBonus.getType_money(); //红包金额
				moneyCount= CurrencyUtil.add(moneyCount,bonusMoney);//累加所有的红包金额
			}
		}
		return  moneyCount;
	}
	
	/**
	 * 清空所有已使用的优惠券集合(List)
	 */
	public static void cleanAll(){
		ThreadContextHolder.getSession().removeAttribute(B2B2C_LIST_SESSION_KEY);
		ThreadContextHolder.getSession().removeAttribute(B2B2C_BONUS_SESSION_KEY);
	}
	
}
