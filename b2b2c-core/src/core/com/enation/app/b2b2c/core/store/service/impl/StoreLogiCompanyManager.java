package com.enation.app.b2b2c.core.store.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.b2b2c.core.store.service.IStoreLogiCompanyManager;
import com.enation.framework.database.IDaoSupport;

/**
 * 店铺物流公司管理类
 * @author Kanon
 *
 */
@Service("storeLogiCompanyManager")
public class StoreLogiCompanyManager implements IStoreLogiCompanyManager {
	
	@Autowired
	private IDaoSupport daoSupport;
	@Autowired
	private IStoreMemberManager storeMemberManager;
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.IStoreLogiCompanyManager#list()
	 */
	@Override
	public List list() {
		StoreMember member =storeMemberManager.getStoreMember();
		String sql="SELECT * from es_logi_company l  LEFT JOIN (select * from es_store_logi_rel s where store_id=? ) s ON l.id=s.logi_id";
		return this.daoSupport.queryForList(sql,member.getStore_id());
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.IStoreLogiCompanyManager#listByStore()
	 */
	@Override
	public List listByStore() {
		StoreMember member =storeMemberManager.getStoreMember();
		String sql="SELECT * from es_logi_company l  LEFT JOIN (select logi_id from es_store_logi_rel s WHERE s.store_id=? ) s ON l.id=s.logi_id  ";
		return this.daoSupport.queryForList(sql,member.getStore_id());
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.store.service.IStoreLogiCompanyManager#addRel(java.lang.Integer)
	 */
	@Override
	public void addRel(Integer logi_id) {
		StoreMember member =storeMemberManager.getStoreMember();
		Map map=new HashMap();
		map.put("logi_id", logi_id);
		map.put("store_id", member.getStore_id());
		this.daoSupport.insert("es_store_logi_rel", map);
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.store.service.IStoreLogiCompanyManager#deleteRel(java.lang.Integer)
	 */
	@Override
	public void deleteRel(Integer logi_id) {
		StoreMember member =storeMemberManager.getStoreMember();
		String sql="delete from es_store_logi_rel where store_id=? and logi_id=?";
		this.daoSupport.execute(sql, member.getStore_id(),logi_id);
	}
	
}
