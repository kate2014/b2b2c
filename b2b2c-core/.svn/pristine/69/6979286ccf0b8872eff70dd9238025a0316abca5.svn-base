package com.enation.app.b2b2c.core.store.service;

import java.util.List;
import java.util.Map;

import com.enation.app.b2b2c.core.store.model.Store;
import com.enation.framework.database.Page;


/**
 * 店铺管理类
 * @author LiFenLong
 *2014-9-11
 */
public interface IStoreManager {
	
	public static final String CURRENT_STORE_KEY="curr_store";
	/**
	 * 申请店铺
	 * @param store
	 */
	public void apply(Store store);
	/**
	 * 店铺审核
	 * @param member_id 会员ID
	 * @param storeId 店铺ID
	 * @param pass	是否通过：1，通过 0未通过
	 * @param commission 平台佣金
	 * @param name_auth 店主认证
	 * @param store_auth 店铺认证
	 */
	public void audit_pass(Integer member_id,Integer storeId,Integer pass,Integer name_auth,Integer store_auth,Double commission, String id_img, String license_img);
	
	/**
	 * 店铺列表
	 * @param other
	 * @param disabled
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Page store_list(Map other,Integer disabled,int pageNo,int pageSize);
	/**
	 * 禁用店铺
	 * @param storeId
	 */
	public void disStore(Integer storeId);
	/**
	 * 恢复店铺使用
	 * @param storeId
	 */
	public void useStore(Integer storeId);
	/**
	 * 获取一个店铺
	 * @param storeId
	 * @return Store
	 */
	public Store getStore(Integer storeId);
	
	/**
	 * 获取当前登录店铺信息
	 * @author Kanon
	 * @return store
	 */ 
	public Store getStore();
	/**
	 * 检查店铺名称是否重复
	 * @param storeName
	 * @return boolean
	 */
	public boolean checkStoreName(String storeName);
	/**
	 * 修改店铺信息
	 * @param store
	 */
	public void editStore(Store store);
	/**
	 * 修改店铺信息-后台使用
	 * @param store
	 */
	public void editStoreInfo(Store store);
	/**
	 * 修改店铺信息
	 * @param store
	 */
	public void editStore(Map store);
	/**
	 *	检查会员是否已经申请了店铺
	 * @return
	 */
	public boolean checkStore();
	
	/**
	 * 新增店铺
	 * @param store
	 */
	public void save(Store store);
	
	/***
	 * 检查身份证信息
	 * @author LiFenLong
	 * @param idNumber
	 */
	public Integer checkIdNumber(String idNumber);
	/**
	 * 修改店铺中的某一个值
	 * @param key
	 * @param value
	 */
	public void editStoreOnekey(String key,String value);
	
	/**
	 * 增加收藏数量
	 * @param storeid
	 */
	public void addcollectNum(Integer storeid);
	
	/**
	 * 减少收藏数量
	 * @param storeid
	 */
	public void reduceCollectNum(Integer storeid);
	
	/**
	 * 审核执照
	 * @author LiFenLong
	 * @param storeid
	 * @param id_img
	 * @param license_img
	 * @param store_auth
	 * @param name_auth
	 */
	public void saveStoreLicense(Integer storeid,String id_img,String license_img,Integer store_auth,Integer name_auth);
	/**
	 * 审核信息列表
	 * @author LiFenLong
	 * @param other
	 * @param disabled
	 * @param pageNo
	 * @param pageSize
	 * @return 店铺待审核信息列表
	 */
	public Page auth_list(Map other,Integer disabled,int pageNo,int pageSize);
	/**
	 * 审核认证新
	 * @param store_id
	 * @param name_auth
	 * @param store_auth
	 */
	public void auth_pass(Integer store_id,Integer name_auth,Integer store_auth);
	/**
	 * 根据会员Id获取店铺
	 * @param memberId
	 * @return
	 */
	public Store getStoreByMember(Integer memberId);
	/**
	 * 重新申请店铺
	 * @param store
	 */
	public void reApply(Store store);
	
	/**
	 * 后台注册店铺
	 * @param store 店铺对象
	 */
	public void registStore(Store store);
	
	public List<Store> listAll();
}
