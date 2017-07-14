package com.enation.app.b2b2c.core.store.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.b2b2c.core.store.service.IStoreRegionsManager;
import com.enation.framework.database.IDaoSupport;

/**
 * 地区 manager
 * @author xulipeng
 */
@Service("storeRegionsManager")
public class StoreRegionsManager  implements IStoreRegionsManager {
	@Autowired
	private IDaoSupport daoSupport;
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.IStoreRegionsManager#getRegionsToAreaList()
	 */
	@Override
	public List getRegionsToAreaList() {
		String sql ="select * from es_regions where p_region_id=?";
		List<Map> list = this.daoSupport.queryForList(sql,0);
		for(Map map :list){
			Integer regionid = (Integer) map.get("region_id");
			List arealist = this.daoSupport.queryForList(sql, regionid);
			map.put("arealist", arealist);
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.IStoreRegionsManager#getRegionsbyids(java.lang.String)
	 */
	@Override
	public List getRegionsbyids(String ids) {
		String sql = "select * from es_regions where region_id in ("+ids+")";
		List list = this.daoSupport.queryForList(sql);
		return list;
	}
	
}
