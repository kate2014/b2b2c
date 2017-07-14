package com.enation.app.b2b2c.core.store.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.b2b2c.core.store.model.StoreDlyCenter;
import com.enation.app.b2b2c.core.store.service.IStoreDlyCenterManager;
import com.enation.framework.database.IDaoSupport;

/**
 * 店铺发货信息管理类
 * @author Kanon 2016-3-2；6.0版本改造
 *
 */
@Service("storeDlyCenterManager")
public class StoreDlyCenterManager   implements IStoreDlyCenterManager {
	@Autowired
	private IDaoSupport daoSupport;
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.store.service.IStoreDlyCenterManager#getDlyCenterList(java.lang.Integer)
	 */
	@Override
	public List getDlyCenterList(Integer store_id) {
		String sql = "select * from es_dly_center where store_id=?";
		List list = this.daoSupport.queryForList(sql, store_id);
		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.store.service.IStoreDlyCenterManager#addDlyCenter(com.enation.app.b2b2c.core.store.model.StoreDlyCenter)
	 */
	@Override
	public void addDlyCenter(StoreDlyCenter dlyCenter) {
		this.daoSupport.insert("es_dly_center", dlyCenter);
		
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.store.service.IStoreDlyCenterManager#editDlyCenter(com.enation.app.b2b2c.core.store.model.StoreDlyCenter)
	 */
	@Override
	public void editDlyCenter(StoreDlyCenter dlyCenter) {
		this.daoSupport.update("es_dly_center", dlyCenter, " dly_center_id="+dlyCenter.getDly_center_id());
		
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.store.service.IStoreDlyCenterManager#getDlyCenter(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public StoreDlyCenter getDlyCenter(Integer store_id, Integer dlyid) {
		String sql  = "select * from es_dly_center where dly_center_id=? and store_id=?";
		List list = this.daoSupport.queryForList(sql, StoreDlyCenter.class, dlyid,store_id);
		StoreDlyCenter dlyCenter = (StoreDlyCenter) list.get(0);
		return dlyCenter;
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.store.service.IStoreDlyCenterManager#delete(java.lang.Integer)
	 */
	@Override
	public void delete(Integer dly_id) {
		String sql = "delete from es_dly_center where dly_center_id="+dly_id;
		this.daoSupport.execute(sql);
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.store.service.IStoreDlyCenterManager#site_default(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public void site_default(Integer dly_id,Integer store_id) {
		
		String sql ="update es_dly_center set disabled='false' where store_id="+store_id;
		String sitesql = "update es_dly_center set disabled='true' where dly_center_id="+dly_id+" and store_id="+store_id;
		
		this.daoSupport.execute(sql);
		this.daoSupport.execute(sitesql);
	}



}
