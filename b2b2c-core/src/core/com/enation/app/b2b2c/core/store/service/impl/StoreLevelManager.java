package com.enation.app.b2b2c.core.store.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.b2b2c.core.store.model.StoreLevel;
import com.enation.app.b2b2c.core.store.service.IStoreLevelManager;
import com.enation.framework.database.IDaoSupport;
@Service("storeLevelManager")
public class StoreLevelManager  implements IStoreLevelManager{
	@Autowired
	private IDaoSupport daoSupport;
	@Override
	public List storeLevelList() {
		String sql="select * from es_store_level";
		return this.daoSupport.queryForList(sql);
	}

	@Override
	public void addStoreLevel(String levelName) {
		StoreLevel storeLevel=new StoreLevel();
		storeLevel.setLevel_name(levelName);
		this.daoSupport.insert("es_store_level", storeLevel);
		
	}

	@Override
	public void editStoreLevel(String levelName, Integer levelId) {
		
		String sql="update es_store_level set level_name=? where level_id=?";
		this.daoSupport.execute(sql, levelName,levelId);
	}

	@Override
	public void delStoreLevel(Integer levelId) {
		String sql="DELETE from es_store_level WHERE level_id=?";
		this.daoSupport.execute(sql, levelId);
	}

	@Override
	public StoreLevel getStoreLevel(Integer levelId) {
		String sql="select * from es_store_level where level_id=?";
		return (StoreLevel) this.daoSupport.queryForObject(sql,StoreLevel.class, levelId);
	}


}
