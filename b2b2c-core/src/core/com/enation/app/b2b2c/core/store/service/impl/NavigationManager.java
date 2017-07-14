package com.enation.app.b2b2c.core.store.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.b2b2c.core.store.model.Navigation;
import com.enation.app.b2b2c.core.store.service.INavigationManager;
import com.enation.framework.database.IDaoSupport;

/**
 * 店铺导航管理类
 * @author Kanon 2016-3-2;版本改造
 *
 */
@Service("navigationManager")
public class NavigationManager implements INavigationManager {
	
	@Autowired
	private IDaoSupport daoSupport;
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.store.service.INavigationManager#getNavicationList(java.lang.Integer)
	 */
	@Override
	public List getNavicationList(Integer storeid) {
		String sql ="select * from es_navigation where store_id=? order by sort asc";
		List list = this.daoSupport.queryForList(sql,storeid);
		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.store.service.INavigationManager#save(com.enation.app.b2b2c.core.store.model.Navigation)
	 */
	@Override
	public void save(Navigation navigation) {
		this.daoSupport.insert("es_navigation", navigation);
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.store.service.INavigationManager#edit(com.enation.app.b2b2c.core.store.model.Navigation)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void edit(Navigation navigation) {
		this.daoSupport.update("es_navigation", navigation, " id="+navigation.getId()+" and store_id="+navigation.getStore_id());
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.store.service.INavigationManager#delete(java.lang.Integer)
	 */
	@Override
	public void delete(Integer id) {
		this.daoSupport.execute("delete from es_navigation where id="+id);
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.store.service.INavigationManager#getNavication(java.lang.Integer)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public Navigation getNavication(Integer id) {
		String sql = "select * from es_navigation where id="+id;
		List<Navigation> list = this.daoSupport.queryForList(sql,Navigation.class);
		Navigation navigation = null;
		if(list != null && list.size()>0){
			navigation = (Navigation) list.get(0);
		}
		return navigation;
	}

}
