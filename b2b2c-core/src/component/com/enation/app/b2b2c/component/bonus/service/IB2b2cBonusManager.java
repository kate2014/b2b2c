package com.enation.app.b2b2c.component.bonus.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.b2b2c.component.bonus.model.StoreBonusType;
import com.enation.app.shop.component.bonus.model.MemberBonus;
import com.enation.framework.database.Page;

/**
 * 优惠券接口
 * @author xulipeng
 * @version v1.0
 * @since v6.2.1
 */
public interface IB2b2cBonusManager {
	
	/**
	 * 添加满减优惠
	 * @return
	 */
	public void add_FullSubtract(StoreBonusType bonus);
	
	/**
	 * 会员领取优惠卷
	 * @param memberid	会员id
	 * @param storeid	店铺id
	 * @param type_id	优惠卷id
	 */
	public void receive_bonus(Integer memberid,Integer storeid,Integer type_id );
	
	/**
	 * 获取优惠劵
	 * @param type_id
	 * @return
	 */
	public StoreBonusType getBonus(Integer type_id);
	
	/**
	 * 获取会员领取的优惠劵的数量
	 * @param type_id
	 * @return
	 */
	public int getmemberBonus(Integer type_id,Integer memberid);
	
	/**
	 * 修改优惠劵
	 * @param bonus
	 */
	public void edit_FullSubtract(StoreBonusType bonus);
	
	/**
	 * 删除优惠劵
	 * @param bonus
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteBonus(Integer type_id);
	
	/**
	 * 获取店铺所有有效的优惠券List
	 * @author DMRain
	 * @date 2016-1-19
	 * @param store_id 店铺ID
	 * @return
	 */
	public List<StoreBonusType> getList(Integer store_id);
	
	/**
	 * 获取优惠券已被领取的数量
	 * @param type_id 优惠券ID
	 * @return
	 */
	public int getCountBonus(Integer type_id);
	
	
	/**
	 * 根据id查询
	 * @param bonusid
	 * @return
	 */
	public StoreBonusType get(Integer bonusid);
	
	/**
	 * 根据条件搜索优惠券    whj 2015-06-01
	 */
	
	public Page getConditionBonusList(Integer pageNo,Integer pageSize,Integer store_id,Map map);
	
	/**
	 * 会员中心——读取会员优惠券
	 * @param 	memberid
	 * @param 	is_usable	1为可用，0为不可用，2为全部。 默认为2
	 * @return
	 */
	public Page getBonusListBymemberid(int pageNo,int pageSize,Integer memberid,Integer is_usable);
	
	/**
	 * 订单结算—读取会员的优惠券（根据可用或者不可用）
	 * @param page		
	 * @param pageSize
	 * @param memberId	会员id
	 * @param is_usable	可用或者不可用 。可用：1，不可用：2
	 * @param goodsPrice 	商品金额
	 * @return
	 */
	public Page getMyBonusByIsUsable(int page,int pageSize,Integer memberId,Integer is_usable,Double goodsPrice,Integer storeId);
	
	
	/**
	 * 获取我的优惠券(一个)<br>
	 * 根据MemberBonus的bonus_id，member_id 和StoreBonusType的store_id<br>
	 * @param member_id	当前会员id
	 * @param store_id	店铺id
	 * @param bonus_id	优惠券id
	 * @return
	 */
	public MemberBonus getOneMyBonus(Integer member_id,Integer store_id,Integer bonus_id);
	
	/**
	 * 使用一个优惠券
	 * @param bonusid
	 * @param memberid
	 * @param orderid
	 * @param ordersn
	 * @param bonus_type_id
	 */
	@Transactional(propagation = Propagation.REQUIRED)  
	public void use(int bonusid,int memberid,int orderid,String ordersn,int bonus_type_id);
	
	/**
	 * 退还红包
	 * @param orderid
	 */
	@Transactional(propagation = Propagation.REQUIRED)  
	public void returned(int orderid);
	
}
