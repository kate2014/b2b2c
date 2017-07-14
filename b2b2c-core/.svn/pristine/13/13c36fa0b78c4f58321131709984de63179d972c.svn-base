package com.enation.app.b2b2c.core.store.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.b2b2c.core.store.model.StoreTemlplate;
import com.enation.app.b2b2c.core.store.service.IStoreTemplateManager;
import com.enation.framework.database.IDaoSupport;

@SuppressWarnings({ "unchecked", "rawtypes" })
@Service("storeTemplateManager")
public class StoreTemplateManager  implements IStoreTemplateManager {
	@Autowired
	private IDaoSupport daoSupport;
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.store.service.IStoreTemplateManager#add(com.enation.app.b2b2c.core.store.model.StoreTemlplate)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Integer add(StoreTemlplate storeTemlplate) {
		this.daoSupport.insert("es_store_template", storeTemlplate);
		return this.daoSupport.getLastId("es_store_template");
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.store.service.IStoreTemplateManager#getTemplateList(java.lang.Integer)
	 */
	@Override
	public List getTemplateList(Integer store_id) {
		String sql = "select * from es_store_template where store_id=?";
		List list = this.daoSupport.queryForList(sql, store_id);
		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.store.service.IStoreTemplateManager#getLastId()
	 */
	@Override
	public Integer getLastId() {
		Integer id= this.daoSupport.getLastId("es_store_template");
		return id;
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.store.service.IStoreTemplateManager#getTemplae(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Map getTemplae(Integer store_id, Integer tempid) {
		String sql = "select * from es_store_template where store_id=? and id=?";
		List list = this.daoSupport.queryForList(sql, store_id,tempid);
		Map map = null;
		if(list != null && list.size() > 0){
			map = (Map) list.get(0);
		}
		return map;
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.store.service.IStoreTemplateManager#edit(com.enation.app.b2b2c.core.store.model.StoreTemlplate)
	 */
	@Override
	public void edit(StoreTemlplate storeTemlplate) {
		this.daoSupport.update("es_store_template", storeTemlplate, " id="+storeTemlplate.getId());
		
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.store.service.IStoreTemplateManager#delete(java.lang.Integer)
	 */
	@Override
	public void delete(Integer tempid) {
		Integer def_temp = this.daoSupport.queryForInt("select def_temp from es_store_template where id=?", tempid);
		if(def_temp==1){
			throw new RuntimeException("不能删除默认物流模板");
		}
		
		String sql  ="select * from es_dly_type where template_id=?";
		List<Map> list = this.daoSupport.queryForList(sql, tempid);
		if(!list.isEmpty()){
			StringBuffer dlyids =new StringBuffer();
			for(Map  map : list) {
				Integer type_id = (Integer) map.get("type_id");
				dlyids.append(type_id+",");
			}
			String ids = dlyids.toString().substring(0, dlyids.toString().length()-1);
			
			String areadelsql = "delete from es_dly_type_area where type_id in ("+ids+")";
			String dlydelsql = "delete from es_dly_type where template_id=?";
			this.daoSupport.execute(areadelsql);
			this.daoSupport.execute(dlydelsql,tempid);
		}
		this.daoSupport.execute("delete from es_store_template where id=?", tempid);
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.store.service.IStoreTemplateManager#getDefTempid(java.lang.Integer)
	 */
	@Override
	public Integer getDefTempid(Integer storeid) {
		String sql = "select * from es_store_template where store_id=? and def_temp=1";
		List list = this.daoSupport.queryForList(sql, storeid);
		if(list.isEmpty()){return null;}
		Map map = (Map) list.get(0);
		return (Integer) map.get("id");
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.store.service.IStoreTemplateManager#setDefTemp(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void setDefTemp(Integer tempid,Integer storeid) {
		this.daoSupport.execute("update es_store_template set def_temp=0 where store_id=?", storeid);
		this.daoSupport.execute("update es_store_template set def_temp=1 where id=?", tempid);
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.store.service.IStoreTemplateManager#getStoreTemlpateByName(java.lang.String, java.lang.Integer)
	 */
	@Override
	public Integer getStoreTemlpateByName(String name,Integer storeid) {
		int i= this.daoSupport.queryForInt("select count(0) from es_store_template where name=? and store_id=?", name,storeid);
		return i;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.store.service.IStoreTemplateManager#checkIsDef(java.lang.Integer)
	 */
	@Override
    public int checkIsDef(Integer tempid) {
        int result = this.daoSupport.queryForInt("select def_temp from es_store_template where id = ?", tempid);
        return result;
    }


}
