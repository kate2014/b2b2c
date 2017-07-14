package com.enation.app.b2b2c.core.goods.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.b2b2c.core.goods.model.StoreCat;
import com.enation.app.b2b2c.core.goods.service.IStoreGoodsCatManager;
import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.framework.database.IDaoSupport;


/**
 * 多店商品分类管理类
 * @author Kanon 2016-3-2；版本改造
 *
 */
@Service("storeGoodsCatManager")
public class StoreGoodsCatManager implements IStoreGoodsCatManager{
	
	@Autowired
	private IStoreMemberManager storeMemberManager;
	
	@Autowired
	private IDaoSupport daoSupport;
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.goods.service.IStoreGoodsCatManager#storeCatList(java.lang.Integer)
	 */
	@Override
	public List storeCatList(Integer storeId) {
		String sql = "select * from es_store_cat where store_id="+storeId+"  order by sort asc ";
		List list = this.daoSupport.queryForList(sql);
		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.goods.service.IStoreGoodsCatManager#addStoreCat(com.enation.app.b2b2c.core.goods.model.StoreCat)
	 */
	@Override
	public void addStoreCat(StoreCat storeCat) {
		if(storeCat.getStore_cat_pid()==null){
			storeCat.setStore_cat_pid(0);
		}
		this.daoSupport.insert("es_store_cat", storeCat);
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.goods.service.IStoreGoodsCatManager#editStoreCat(com.enation.app.b2b2c.core.goods.model.StoreCat)
	 */
	@Override
	public void editStoreCat(StoreCat storeCat) {
		//如果是父分类并且是关闭状态，则子分类也要是关闭状态
		if(storeCat.getStore_cat_pid()==0 && storeCat.getDisable().equals(0)){
			String sql = "update es_store_cat set disable=0 where store_cat_pid=?";
			this.daoSupport.execute(sql, storeCat.getStore_cat_id());
		}
		//如果是子分类且是开启状态，则父分类也要是开启状态
		if(storeCat.getStore_cat_pid()!=0 && storeCat.getDisable().equals(1)){
			String sql = "update es_store_cat set disable=1 where store_cat_id=?";
			this.daoSupport.execute(sql, storeCat.getStore_cat_pid());
		}
		this.daoSupport.update("es_store_cat", storeCat, " store_cat_id="+storeCat.getStore_cat_id());
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.goods.service.IStoreGoodsCatManager#deleteStoreCat(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public void deleteStoreCat(Integer storeCatId,Integer storeid) {
		Integer num = this.daoSupport.queryForInt("select count(0) from es_store_cat s where s.store_cat_pid=? and store_id=?",storeCatId,storeid);
		if(num>=1){
			throw new RuntimeException("删除失败，分类*有下级分类！");
		}
		
		int goodsnum = this.daoSupport.queryForInt("select count(0) from es_goods where store_cat_id=? and store_id=?", storeCatId,storeid);
		if(goodsnum>=1){
			throw new RuntimeException("删除失败，请删除此分类*下所有商品(包括商品回收站)！");
		}
		
		String sql="delete from es_store_cat where store_cat_id=? and store_id=?";
		this.daoSupport.execute(sql,storeCatId,storeid);
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.goods.service.IStoreGoodsCatManager#getStoreCatList(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public List getStoreCatList(Integer catid,Integer storeId) {
		String sql = "select * from es_store_cat where store_cat_pid=0 and store_id="+storeId;
		if(catid!=null){
			sql+=" and store_cat_id!="+catid;
		}
		sql+="  order by sort asc";
		return this.daoSupport.queryForList(sql);
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.goods.service.IStoreGoodsCatManager#getStoreCat(java.util.Map)
	 */
	@Override
	public StoreCat getStoreCat(Map map) {
		Integer storeid = (Integer) map.get("storeid");
		Integer strore_catid = (Integer) map.get("store_catid");
		String sql = "select * from es_store_cat where store_id=? and store_cat_id=?";
		List<StoreCat> list = this.daoSupport.queryForList(sql,StoreCat.class,storeid,strore_catid);
		StoreCat storeCat =new StoreCat();
		if(!list.isEmpty()){
			storeCat= (StoreCat) list.get(0);
		}
		return storeCat;
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.goods.service.IStoreGoodsCatManager#is_children(java.lang.Integer)
	 */
	@Override
	public Integer is_children(Integer catid) {
		String sql ="select store_cat_pid from es_store_cat where store_cat_id=?";
		Integer pid=this.daoSupport.queryForInt(sql, catid);
		return pid;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.goods.service.IStoreGoodsCatManager#getStoreCatNum(java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Integer getStoreCatNum(Integer store_id,Integer store_cat_pid,Integer sort) {
		String sql = "select count(0) from es_store_cat where store_id =? and store_cat_pid=? and sort=?";
		return this.daoSupport.queryForInt(sql, store_id,store_cat_pid,sort);
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.goods.service.IStoreGoodsCatManager#listAllChildren(java.lang.Integer)
	 */
	@Override
	public List<Map> listAllChildren(Integer catId) {
		StoreMember storemember=storeMemberManager.getStoreMember();
		int storeId=storemember.getStore_id();
		
		String sql = "select * from es_store_cat where store_id ="+storeId;
		List<StoreCat> allStoreCatList=new ArrayList();
		allStoreCatList =this.daoSupport.queryForList(sql,StoreCat.class);
		
		List<Map> endStorelist=new ArrayList<Map>();
		
		for(StoreCat cat :allStoreCatList){
			if(cat.getStore_cat_pid()==0){
				Map map=new HashMap();              						
				map.put("id", cat.getStore_cat_id());			
				map.put("text",cat.getStore_cat_name());
				
				              						
				List<Map> listmap =new ArrayList();
				boolean exis=false;
				
				for (StoreCat cat2 :allStoreCatList){
					if(cat2.getStore_cat_pid().equals(cat.getStore_cat_id())){	
						exis=true;
						Map map2=new HashMap();
						map2.put("id", cat2.getStore_cat_id());
						
						map2.put("text",cat2.getStore_cat_name());
						
						listmap.add(map2);
					}
				}
				
				if(exis){
					map.put("children",listmap);
					map.put("state","closed");
				}
				
				endStorelist.add(map);
			}
			}	
		return endStorelist;
	}


}
