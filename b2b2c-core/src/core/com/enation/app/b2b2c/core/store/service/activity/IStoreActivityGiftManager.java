package com.enation.app.b2b2c.core.store.service.activity;


import java.util.List;

import com.enation.app.b2b2c.core.store.model.activity.StoreActivityGift;
import com.enation.framework.database.Page;

/**
 * 店铺促销活动赠品管理接口
 * @author DMRain
 * @date 2016年1月14日
 * @version v1.0
 * @since v1.0
 */
public interface IStoreActivityGiftManager {

	/**
	 * 获取店铺促销活动赠品分页列表
	 * @param keyword	搜索关键字
	 * @param store_id	店铺ID
	 * @param pageNo	页数
	 * @param pageSize	每页记录数
	 * @return
	 */
	public Page list(String keyword, Integer store_id, Integer pageNo, Integer pageSize);
	
	/**
	 * 添加店铺促销活动赠品
	 * @param StoreActivityGift
	 */
	public void add(StoreActivityGift StoreActivityGift);
	
	/**
	 * 修改店铺促销活动赠品信息
	 * @param StoreActivityGift
	 */
	public void edit(StoreActivityGift StoreActivityGift);
	
	/**
	 * 根据促销活动赠品ID获取店铺促销活动赠品信息
	 * @param gift_id 赠品ID
	 * @return
	 */
	public StoreActivityGift get(Integer gift_id);
	
	/**
	 * 获取店铺所有促销活动赠品集合
	 * @param store_id 店铺id
	 * @return
	 */
	public List<StoreActivityGift> listAll(Integer store_id);
	
}
