package com.enation.app.b2b2c.core.store.service.activity;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.b2b2c.core.store.model.activity.StoreActivity;
import com.enation.app.shop.core.other.model.ActivityDetail;
import com.enation.framework.database.Page;

/**
 * 促销活动接口
 * @author DMRain
 * @date 2016年1月7日
 * @version v1.0
 * @since v1.0
 */
public interface IStoreActivityManager {

	/**
	 * 获取店铺促销活动分页列表
	 * @param keyword 搜索关键字、
	 * @param store_id 店铺ID
	 * @param pageNo 页数
	 * @param pageSize 每页记录数
	 * @return
	 */
	public Page getActivityList(String keyword, Integer store_id, Integer pageNo, Integer pageSize);
	
	/**
	 * 添加促销活动信息
	 * @param activity 促销活动信息
	 * @param detail 促销活动优惠详细信息
	 * @param goods_id 促销活动商品ID组
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void add(StoreActivity activity, ActivityDetail detail, Integer[] goods_id);
	
	/**
	 * 修改促销活动信息
	 * @param activity 促销活动信息
	 * @param detail 促销活动优惠详细信息
	 * @param goods_id 参与促销活动的商品id组
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void edit(StoreActivity activity, ActivityDetail detail, Integer[] goods_id);
	
	/**
	 * 获取店铺所有上架商品分页列表
	 * @param pageNo 页数
	 * @param pageSize 每页记录数
	 * @param map 参数集合
	 * @return
	 */
	public Page goodsList(Integer pageNo, Integer pageSize, Map map);
	
	/**
	 * 根据促销活动ID获取已经参加促销活动的商品集合
	 * @param activity_id 促销活动ID
	 * @return
	 */
	public List partGoodsList(Integer activity_id);
	
	/**
	 * 根据促销活动ID获取促销活动信息
	 * @param activity_id 促销活动ID
	 * @return activity 促销活动信息
	 */
	public StoreActivity get(Integer activity_id);
	
	/**
	 * 根据促销活动ID获取所有促销活动信息集合
	 * @param activity_id 促销活动集合
	 * @return
	 */
	public List<StoreActivity> getList(Integer activity_id);
	
	/**
	 * 删除促销活动
	 * @param activity_id 促销活动ID
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Integer activity_id);
	
	/**
	 * 根据店铺ID与当前时间获取当前促销活动信息
	 * @param store_id 店铺ID
	 * @return
	 */
	public StoreActivity getCurrentAct(Integer store_id);
	
	/**
	 * 判断商品是否参加了促销活动
	 * @param goods_id 商品ID
	 * @param activity_id 促销活动ID
	 * @return num 0：未参加，1：已参加
	 */
	public int checkGoodsAct(Integer goods_id, Integer activity_id);
	
	/**
	 * 判断促销活动在同一时间段是否重复添加
	 * @param startTime 促销活动开始时间
	 * @param endTime 促销活动结束时间
	 * @param store_id 店铺ID
	 * @return result 0:不重复，1:重复
	 */
	public int checkActByDate(Long startTime, Long endTime, Integer store_id, Integer activity_id);
	
}
