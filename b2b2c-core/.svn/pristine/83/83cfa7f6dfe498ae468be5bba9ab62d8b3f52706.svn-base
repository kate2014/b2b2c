package com.enation.app.b2b2c.core.store.service;

import com.enation.app.b2b2c.core.store.model.Store;
import com.enation.app.b2b2c.core.store.model.StoreThemes;
import com.enation.framework.database.Page;

/**
 * 店铺模板管理类
 * @author Kanon
 *
 */
public interface IStoreThemesManager {

	/**
	 * 店铺模板列表
	 * @return
	 */
	public Page list(Integer pageNo,Integer pageSize);
	
	/**
	 * 店铺模板添加
	 * @param storeThemes 店铺模板
	 * @return
	 */
	public void add(StoreThemes storeThemes);
	
	/**
	 * 店铺模板修改
	 * @param storeThemes 店铺模板
	 * @return
	 */
	public void edit(StoreThemes storeThemes);
	
	/**
	 * 删除店铺模板
	 * @param id 店铺模板Id
	 */
	public void delete(Integer id);
	
	/**
	 * 获取店铺模板信息
	 * @param id 店铺模板Id
	 * @return
	 */
	public StoreThemes getStorethThemes(Integer id);
	
	/**
	 * 根据店铺Id获取模板文件夹
	 * @param storeId 店铺Id
	 * @return 模板文件夹
	 */
	public String getStrorePath(Integer storeId);
	
	/**
	 * 切换当前分销商店铺模板
	 * @param themesId 模板Id
	 */
	public void changeStoreThemes(Integer themes_id);
	
	/**
	 * 获取当前默认模板
	 * @return
	 */
	public StoreThemes getDefaultStoreThemes();
	
	/**
	 * 通过链接获取店铺
	 * @param url 链接 
	 * @return
	 */
	public Store getStoreByUrl(String url);
	/**
	 * 通过链接获取店铺Id
	 * @param url 链接 
	 * @return
	 */
	public Integer getStoreIdByUrl(String url);
	
}
