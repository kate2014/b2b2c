package com.enation.app.b2b2c.core.store.service.activity.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.b2b2c.core.order.service.cart.IStoreCartManager;
import com.enation.app.b2b2c.core.store.model.activity.StoreActivity;
import com.enation.app.b2b2c.core.store.service.activity.IStoreActivityManager;
import com.enation.app.shop.core.order.service.ICartManager;
import com.enation.app.shop.core.other.model.Activity;
import com.enation.app.shop.core.other.model.ActivityDetail;
import com.enation.app.shop.core.other.model.ActivityGoods;
import com.enation.app.shop.core.other.service.IActivityDetailManager;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;

/**
 * 促销活动接口实现类
 * @author DMRain
 * @date 2016年1月7日
 * @version v1.0
 * @since v1.0
 */
@Service("storeActivityManager")
public class StoreActivityManager implements IStoreActivityManager{

	protected final Logger logger = Logger.getLogger(getClass());
	@Autowired
	private IDaoSupport daoSupport;
	
	@Autowired
	private IActivityDetailManager activityDetailManager;
	@Autowired
	private IStoreCartManager storeCartManager;
	@Autowired
	private ICartManager cartManager;
	@Autowired
	private IStoreMemberManager storeMemberManager;
	
	/* (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.activity.IActivityManager#getActivityList(java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Page getActivityList(String keyword, Integer store_id, Integer pageNo, Integer pageSize) {
		String sql = "select * from es_activity where store_id = ? and disabled = 0";
		
		//如果关键字不为空
		if(keyword != null && !StringUtil.isEmpty(keyword)){
			sql += " and activity_name like '%" + keyword + "%'";
		}
		
		sql += " order by activity_id desc";
		
		Page page = this.daoSupport.queryForPage(sql, pageNo, pageSize, store_id);
		
		List list = (List) page.getResult();
		Map map = new HashMap();
		
		//遍历获取的当前页的记录
		for(int i = 0; i < list.size(); i++){
			map = (Map) list.get(i);
			String statusStr = this.getCurrentStatus((Long)(map.get("start_time")), (Long)(map.get("end_time")));
            map.put("status", statusStr);
		}
		
		return page;
	}

	/* (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.store.service.activity.IStoreActivityManager#add(com.enation.app.b2b2c.core.store.model.activity.StoreActivity, com.enation.app.shop.core.other.model.ActivityDetail, java.lang.Integer[])
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void add(StoreActivity activity, ActivityDetail detail, Integer[] goods_id) {
		this.daoSupport.insert("es_activity", activity);
		Integer activity_id = this.daoSupport.getLastId("es_activity");
		
		detail.setActivity_id(activity_id);
		this.activityDetailManager.add(detail);
		
		
		
		//如果促销活动没有选择部分商品参与
		if(activity.getRange_type() == 2){
			this.addGoods(goods_id, activity_id);
		}
		
		this.changeCart(activity_id, activity.getStore_id());
	}

	/* (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.store.service.activity.IStoreActivityManager#edit(com.enation.app.b2b2c.core.store.model.activity.StoreActivity, com.enation.app.shop.core.other.model.ActivityDetail, java.lang.Integer[])
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void edit(StoreActivity activity, ActivityDetail detail, Integer[] goods_id) {
		
		//标记之前 被加入购物车的相关商品 要进行更新
		changeCart(activity.getActivity_id(), activity.getStore_id());
		
		this.daoSupport.update("es_activity", activity, "activity_id="+activity.getActivity_id());
		
		detail.setIs_full_minus(detail.getIs_full_minus() == null ? 0 : detail.getIs_full_minus());
		detail.setMinus_value(detail.getMinus_value() == null ? 0 : detail.getMinus_value());
		detail.setIs_send_point(detail.getIs_send_point() == null ? 0 : detail.getIs_send_point());
		detail.setPoint_value(detail.getPoint_value() == null ? 0 : detail.getPoint_value());
		detail.setIs_free_ship(detail.getIs_free_ship() == null ? 0 : detail.getIs_free_ship());
		detail.setIs_send_gift(detail.getIs_send_gift() == null ? 0 : detail.getIs_send_gift());
		detail.setIs_send_bonus(detail.getIs_send_bonus() == null ? 0 : detail.getIs_send_bonus());
		this.activityDetailManager.edit(detail);
		
		//如果参加活动商品形式为1(1：全部参加，2：部分参加)
		if (activity.getRange_type() == 1) {
			this.deleteGoods(activity.getActivity_id());
			
		} else if (activity.getRange_type() == 2) {
			this.deleteGoods(activity.getActivity_id());
			this.addGoods(goods_id, activity.getActivity_id());
		}

		//标记之前 被加入购物车的相关商品 要进行更新
		changeCart(activity.getActivity_id(), activity.getStore_id());
	}
	
	/* (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.activity.IActivityManager#goodsList(java.lang.Integer, java.lang.Integer, java.util.Map)
	 */
	@Override
	public Page goodsList(Integer pageNo, Integer pageSize, Map map) {
		Integer store_id = (Integer) map.get("store_id");
		
		String sql = "select * from es_goods where disabled = 0 and market_enable = 1 and store_id = "+store_id+"";
		
		String name = (String) map.get("keyword");
		
		//如果商品名称不为空
		if (name != null && !StringUtil.isEmpty(name)) {
			sql += " and name like '%" + name + "%'";
		}
		
		sql += " order by goods_id desc";
		
		Page goodsPage = this.daoSupport.queryForPage(sql, pageNo, pageSize);
		return goodsPage;
	}

	/* (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.activity.IActivityManager#partGoodsList(java.lang.Integer)
	 */
	@Override
	public List partGoodsList(Integer activity_id) {
		String sql = "select g.* from es_activity_goods a left join es_goods g on a.goods_id = g.goods_id "
				+ "where a.activity_id = ? order by g.goods_id desc";
		return this.daoSupport.queryForList(sql, activity_id);
	}
	
	/* (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.activity.IActivityManager#get(java.lang.Integer)
	 */
	@Override
	public StoreActivity get(Integer activity_id) {
		StoreActivity activity = this.daoSupport.queryForObject("select * from es_activity where activity_id = ?", StoreActivity.class, activity_id);
		return activity;
	}

	/* (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.activity.IActivityManager#getList(java.lang.Integer)
	 */
	@Override
	public List<StoreActivity> getList(Integer activity_id) {
		List<StoreActivity> list = this.daoSupport.queryForList("select * from es_activity where activity_id = ?", StoreActivity.class, activity_id);
		return list;
	}
	
	/* (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.activity.IActivityManager#delete(java.lang.Integer)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Integer activity_id) {

		StoreMember member = storeMemberManager.getStoreMember();
		//更新活动
		if (member != null) {
			changeCart(activity_id, member.getStore_id());
		}
		
		String sql = "update es_activity set disabled = 1 where activity_id = ?";
		this.daoSupport.execute(sql, activity_id);
		
		this.deleteGoods(activity_id);

	}
	
	/* (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.activity.IActivityManager#getCurrentAct(java.lang.Integer)
	 */
	@Override
	public StoreActivity getCurrentAct(Integer store_id){
		long currentTime = DateUtil.getDateline();
		String sql = "select * from es_activity where disabled = 0 and start_time < "+currentTime+" and end_time > "+currentTime+" and store_id = "+store_id+"";
		StoreActivity activity = this.daoSupport.queryForObject(sql, StoreActivity.class);
		return activity;
	}
	
	/* (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.activity.IActivityManager#checkGoodsAct(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public int checkGoodsAct(Integer goods_id, Integer activity_id) {
		String sql = "select count(0) from es_activity_goods where goods_id = ? and activity_id = ?";
		int num = this.daoSupport.queryForInt(sql, goods_id, activity_id);
		//如果没有指定活动，则查看是否有全局活动
		if (num == 0) {
			try {
				//判定是否有全局活动
				if (this.daoSupport.queryForInt("select range_type from es_activity where activity_id = ?",activity_id) == 1) {
					return 1;
				}
			} catch (Exception e) {
				logger.error("错误的活动", e);
				return 0;
			}
		}
		num = num > 0 ? 1 : 0;
		return num;
	}
	
	/* (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.activity.IActivityManager#checkActByDate(java.lang.Long, java.lang.Long, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public int checkActByDate(Long startTime, Long endTime, Integer store_id, Integer activity_id) {
		Long currentTime = DateUtil.getDateline();	//获取当前时间
		String sql = "select start_time,end_time from es_activity where disabled = 0 and store_id = ? and end_time > ?";
		
		if(activity_id != null && activity_id != 0){
			sql += " and activity_id != "+activity_id+"";
		}
		List<Map> list = this.daoSupport.queryForList(sql, store_id, currentTime);
		
		int result = 0;
		if(list.size() != 0){
			for(Map map : list){
				if(startTime >= (Long)map.get("start_time") && startTime <= (Long)map.get("end_time")){
					result = 1;
					break;
				}
				if(endTime >= (Long)map.get("start_time") && endTime <= (Long)map.get("end_time")){
					result = 1;
					break;
				}
			}
		}
		
		return result;
	}
	
	
	
	

	/**
	 * 
	 * 修改活动相关购物车
	 * @param activity_id 活动
	 * @param store_id 店铺
	 */
	private void changeCart(Integer activity_id,Integer store_id) {
		Activity act = this.get(activity_id);
		// 如果当前进行的活动是编辑的活动，才进行处理
		Activity curAct = this.getCurrentAct(store_id);
		//当前活动为空，则赋值 编辑的活动给它，让其符合下方的if条件
		if(curAct==null){
			curAct=act;
		}
		if (curAct.getActivity_id().equals(activity_id)) {
			// 全局活动 则修改所有购物车
			if (act.getRange_type() == 1) {
				//修改所有该店铺购物车标记需要修复
				storeCartManager.changeAll(store_id);
			} else {
				List<ActivityGoods> acgs = this.daoSupport.queryForList("select * from es_activity_goods where activity_id = ?",
								ActivityGoods.class, activity_id);
				if (acgs.size() > 0) {
					for (ActivityGoods ag : acgs) {
						cartManager.changeProduct(ag.getGoods_id());
					}
				}
			}
		}

	}
	
	
    /**
     * 获取当前状态
     * @param startTime 活动开始时间
     * @param endTime 活动结束时间
     * @return
     */
    private String getCurrentStatus(Long startTime, Long endTime) {
        String status = "";
        long currentTime = System.currentTimeMillis() / 1000; //获取当前时间秒数
        if (startTime > currentTime) {
            status = "未开始";
        } else if ((startTime <= currentTime) && (endTime >= currentTime)) {
            status = "进行中";
        } else if (endTime < currentTime) {
            status = "已结束";
        }
        return status;
    }

    /**
	 * 添加参与促销活动的商品
	 * @param goods_id 商品id
	 * @param activity_id 促销活动id
	 */
	private void addGoods(Integer[] goods_id, Integer activity_id){
		if(goods_id != null){
			ActivityGoods activityGoods = new ActivityGoods();
			for(int i = 0; i < goods_id.length; i++){
				activityGoods.setActivity_id(activity_id);
				activityGoods.setGoods_id(goods_id[i]);
				this.daoSupport.insert("es_activity_goods", activityGoods);
			}
		}
	}
	
	/**
	 * 根据促销活动ID删除参与促销活动的商品
	 * @param activity_id 促销活动ID
	 */
	private void deleteGoods(Integer activity_id){
		String sql = "delete from es_activity_goods where activity_id = ?";
		this.daoSupport.execute(sql, activity_id);
	}
}
