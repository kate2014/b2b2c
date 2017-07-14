package com.enation.app.b2b2c.core.store.service.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.b2b2c.core.store.model.Store;
import com.enation.app.b2b2c.core.store.model.StoreThemes;
import com.enation.app.b2b2c.core.store.service.IStoreManager;
import com.enation.app.b2b2c.core.store.service.IStoreThemesManager;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.StringUtil;
/**
 * 店铺模板管理类
 * @author Kanon
 *
 */
@Service("storeThemesManager")
public class StoreThemesManager implements IStoreThemesManager {
	
	@Autowired
	private IDaoSupport daoSupport;
	
	@Autowired
	private IStoreMemberManager storeMemberManager;
	
	@Autowired
	private IStoreManager storeManager;
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.distribution.core.service.store.IStoreThemesManager#list(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Page list(Integer pageNo,Integer pageSize) {
		String sql="select * from es_store_themes";
		return daoSupport.queryForPage(sql, pageNo, pageSize);
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.distribution.core.service.store.IStoreThemesManager#add(java.lang.String, java.lang.String)
	 */
	@Override
	public void add(StoreThemes storeThemes) {
		String sql="select count(id) from es_store_themes";
		if(daoSupport.queryForInt(sql)==0){
			storeThemes.setIs_default(1);
		}else{
			storeThemes.setIs_default(0);
		}
		this.daoSupport.insert("es_store_themes", storeThemes);
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.distribution.core.service.store.IStoreThemesManager#edit(java.lang.String, java.lang.String)
	 */
	@Override
	public void edit(StoreThemes storeThemes) {
		//只能有一个默认模板
		if(storeThemes.getIs_default().equals(1)){
			this.daoSupport.execute("UPDATE es_store_themes SET is_default=0");
		}
		this.daoSupport.update("es_store_themes", storeThemes, "id="+storeThemes.getId());
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.distribution.core.service.store.IStoreThemesManager#delete(java.lang.String)
	 */
	@Override
	public void delete(Integer id) {
		//查询到当前默认模版
		String sql="select * from es_store_themes where is_default = 1";
		StoreThemes themes=this.daoSupport.queryForObject(sql, StoreThemes.class);
		//将用到此模版的店铺修改成默认模版
		this.daoSupport.execute("update es_store set themes_id = ?,themes_path = ? where themes_id = ?",themes.getId(),themes.getPath(),id);
		//删除模版
		this.daoSupport.execute("delete from es_store_themes where id=?", id);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.distribution.core.service.store.IStoreThemesManager#getStorethThemes(java.lang.Integer)
	 */
	@Override
	public StoreThemes getStorethThemes(Integer id) {
		return (StoreThemes) this.daoSupport.queryForObject("select * from es_store_themes where id=?", StoreThemes.class, id);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.distribution.core.service.store.IStoreThemesManager#getStrorePath(java.lang.Integer)
	 */
	@Override
	public String getStrorePath(Integer store_id) {
		String storePath=this.daoSupport.queryForString("select themes_path from es_store where store_id="+store_id);
		if(storePath!=null){
			return storePath;
		}else{
			return this.getDefaultStoreThemes().getPath();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.distribution.core.service.store.IStoreThemesManager#changeStoreThemes(java.lang.Integer)
	 */
	@Override
	public void changeStoreThemes(Integer themes_id) {
		StoreMember member=storeMemberManager.getStoreMember();
		StoreThemes storeThemes=this.getStorethThemes(themes_id);
		this.daoSupport.execute("update es_store set themes_id=?,themes_path=? where member_id=?", themes_id,storeThemes.getPath(),member.getMember_id());
	}

	@Override
	public StoreThemes getDefaultStoreThemes() {
		return (StoreThemes) this.daoSupport.queryForObject("select * from es_store_themes where is_default=1", StoreThemes.class);
	}

	
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.store.service.IStoreThemesManager#getStoreByUrl(java.lang.String)
	 */
	@Override
	public Store getStoreByUrl(String url) {
		String storeId=paseStoreId(url);
		if(storeId == null){
			return null;
		}
		return storeManager.getStore(StringUtil.toInt(storeId, true));
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.store.service.IStoreThemesManager#getStoreIdByUrl(java.lang.String)
	 */
	@Override
	public Integer getStoreIdByUrl(String url) {
		String storeId=paseStoreId(url);
		
		if(StringUtil.toInt(storeId, 0)==0){
			return 0;
		}
		return StringUtil.toInt(storeId,true);
	}
	
	/**
	 * 通过正则获取url 中 的店铺Id
	 * @param url url请求
	 * @return 店铺Id
	 */
	private  static String  paseStoreId(String url){
		String pattern = "(/)(\\d+)(/)";
		String value = null;
		Pattern p = Pattern.compile(pattern, 2 | Pattern.DOTALL);
		Matcher m = p.matcher(url);
		if (m.find()) {
			value=m.group(2);
		}
		return value;
	}
}
