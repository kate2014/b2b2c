package com.enation.app.b2b2c.core.store.service.activity.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.b2b2c.core.store.model.activity.StoreActivityGift;
import com.enation.app.b2b2c.core.store.service.activity.IStoreActivityGiftManager;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;

/**
 * 店铺促销活动赠品管理接口实现类
 * @author DMRain
 * @date 2016年1月14日
 * @version v1.0
 * @since v1.0
 */
@Service("storeActivityGiftManager")
public class StoreActivityGiftManager  implements IStoreActivityGiftManager{

	@Autowired
	private IDaoSupport  daoSupport;
	
	/* (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.activity.IStoreActivityGiftManager#list(java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Page list(String keyword, Integer store_id, Integer pageNo, Integer pageSize) {
		String sql = "select * from es_activity_gift where store_id = ? and disabled = 0";
		
		//如果关键字不为空
		if(keyword != null && !StringUtil.isEmpty(keyword)){
			sql += " and gift_name like '%" + keyword + "%'";
		}
		
		sql += " order by create_time desc";
		
		Page page = this.daoSupport.queryForPage(sql, pageNo, pageSize, store_id);
		return page;
	}

	/* (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.activity.IStoreActivityGiftManager#add(com.enation.app.b2b2c.core.model.activity.StoreActivityGift)
	 */
	@Override
	public void add(StoreActivityGift StoreActivityGift) {
		this.daoSupport.insert("es_activity_gift", StoreActivityGift);
		
	}

	/* (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.activity.IStoreActivityGiftManager#edit(com.enation.app.b2b2c.core.model.activity.StoreActivityGift)
	 */
	@Override
	public void edit(StoreActivityGift StoreActivityGift) {
		this.daoSupport.update("es_activity_gift", StoreActivityGift, "gift_id="+StoreActivityGift.getGift_id());
		
	}
	
	/* (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.activity.IStoreActivityGiftManager#get(java.lang.Integer)
	 */
	@Override
	public StoreActivityGift get(Integer gift_id) {
		String sql = "select * from es_activity_gift where gift_id = ? and disabled = 0";
		return this.daoSupport.queryForObject(sql, StoreActivityGift.class, gift_id);
	}

	/* (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.activity.IStoreActivityGiftManager#listAll(java.lang.Integer)
	 */
	@Override
	public List<StoreActivityGift> listAll(Integer store_id) {
		String sql = "select * from es_activity_gift where store_id = ? and disabled = 0";
		List<StoreActivityGift> list = this.daoSupport.queryForList(sql, StoreActivityGift.class, store_id);
		return list;
	}

}
