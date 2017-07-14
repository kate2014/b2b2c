package com.enation.app.b2b2c.core.goods.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.b2b2c.core.goods.service.IB2b2cGoodsStoreManager;
import com.enation.app.base.core.service.auth.IPermissionManager;
import com.enation.app.base.core.service.auth.impl.PermissionConfig;
import com.enation.app.shop.ShopApp;
import com.enation.eop.resource.model.AdminUser;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.StringUtil;

import net.sf.json.JSONObject;

/**
 * b2b2c商品库存管理
 * @author [kingapex]
 * @version [1.0]
 * @since [5.1]
 * 2015年10月23日下午5:05:39
 * @author Kanon 2016-3-2;6.0版本改造
 */
@Service("b2b2cGoodsStoreManager")
public class B2b2cGoodsStoreManager implements IB2b2cGoodsStoreManager {
	@Autowired
	private IPermissionManager permissionManager;
	
	@Autowired
	private IDaoSupport daoSupport;
	
	/*
	 * 
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.goods.IB2b2cGoodsStoreManager#listSelfStore(java.util.Map, int, int, java.lang.String, java.lang.String)
	 */
	@Override
	public Page listSelfStore(Map map, int page, int pageSize, String sortField, String sortType) {
		
		Integer stype = (Integer) map.get("stype");
		String keyword = (String) map.get("keyword");
		String name = (String) map.get("name");
		String sn = (String) map.get("sn");
		
		int depotid  = (Integer)map.get("depotid") ;
		
		if( StringUtil.isEmpty( sortField)){
			sortField=" g.goods_id";
		}
		
		if( StringUtil.isEmpty( sortType)){
			sortType=" desc";
		}
		
		boolean isSuperAdmin = this.permissionManager.checkHaveAuth(PermissionConfig.getAuthId("super_admin"));// 超级管理员权限
		boolean isDepotAdmin = this.permissionManager.checkHaveAuth(PermissionConfig.getAuthId("depot_admin"));// 库存管理权限
		
		if(!isDepotAdmin&&!isSuperAdmin){
			throw new RuntimeException("没有操作库存的权限");
		}
		StringBuffer sql = new StringBuffer();
		sql.append("select g.goods_id,g.sn,g.name,g.store,c.name cname,g.enable_store from es_goods g,es_goods_cat c where g.cat_id=c.cat_id and market_enable!=2 and g.disabled=0 and g.store_id="+ShopApp.self_storeid);
		
		if(stype!=null && keyword!=null){//基本搜索	
			if(stype==0){
				sql.append(" and ( g.name like '%"+keyword.trim()+"%'");
				sql.append(" or g.sn like '%"+keyword.trim()+"%')");
			}
		}else{ //高级搜索
		
			if(!StringUtil.isEmpty(name)){
				sql.append(" and g.name like '%"+name+"%'");
			}
			
			if(!StringUtil.isEmpty(sn)){
				sql.append(" and g.sn like '%"+sn+"%'");
			}
		}
		
		sql.append(" order by "+sortField+" "+sortType);
		Page webPage = this.daoSupport.queryForPage(sql.toString(), page, pageSize);
		
		List<Map>goodslist = (List<Map>) webPage.getResult();
		
		StringBuffer goodsidstr = new  StringBuffer();
		for (Map goods : goodslist) {
			Integer goodsid  = (Integer)goods.get("goods_id");
			if(goodsidstr.length()!=0){
				goodsidstr.append(",");
			}
			goodsidstr.append(goodsid);
		}
		
		if(goodsidstr.length()!=0){
			
			String ps_sql ="select ps.* from  es_product_store ps where  ps.goodsid in("+goodsidstr+") ";
			if(depotid!=0 ){
				ps_sql=ps_sql+" and depotid="+depotid;
			}else{
				//判断是否为总库存
				if(isDepotAdmin){
					AdminUser adminUser = UserConext.getCurrentAdminUser();
					String depotsql = "select d.* from es_depot d inner join es_depot_user du on du.depotid=d.id where du.userid=?";
					List<Map> depotList=this.daoSupport.queryForList(depotsql,adminUser.getUserid());
					Integer depot_id=0;
					if(depotList.size()!=0){
						for (Map map1:depotList) {
							depot_id=Integer.parseInt(map1.get("id").toString());
						}
						ps_sql=ps_sql+" and depotid="+depot_id;
					}
				}
			}
			ps_sql=ps_sql+" order by goodsid,depotid ";
			List<Map> storeList  = this.daoSupport.queryForList(ps_sql);
			
			for (Map goods : goodslist) {
				Integer goodsid  = (Integer)goods.get("goods_id");
				if(depotid!=0 ||isDepotAdmin){
					goods.put("d_store", 0);
					goods.put("enable_store", 0);
					for (Map store : storeList) {
						Integer store_goodsid  = (Integer)store.get("goodsid");
						if(store_goodsid.compareTo(goodsid)==0){
							Integer d_store = (Integer.valueOf(goods.get("d_store").toString()))+(Integer.valueOf(store.get("store").toString()));
							Integer enable_store = (Integer.valueOf(goods.get("enable_store").toString()))+(Integer.valueOf(store.get("enable_store").toString()));
							goods.put("d_store", d_store);
							goods.put("enable_store", enable_store);
						}
					}
				}else{
					goods.put("d_store", goods.get("store"));
					goods.put("enable_store", goods.get("enable_store"));
					 
				}
			}
			 
		}
		
		return webPage;
		
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.goods.service.IB2b2cGoodsStoreManager#listSelfWarningStore(java.util.Map, int, int, java.lang.String, java.lang.String)
	 */
	@Override
	public Page listSelfWarningStore(Map map, int page, int pageSize, String sortField, String sortType) {

		Integer stype = (Integer) map.get("stype");
		String keyword = (String) map.get("keyword");
		String name = (String) map.get("name");
		String sn = (String) map.get("sn");
		
		int depotid  = (Integer)map.get("depotid") ;
		
		if( StringUtil.isEmpty( sortField)){
			sortField=" g.goods_id";
		}
		
		if( StringUtil.isEmpty( sortType)){
			sortType=" desc";
		}
		
		boolean isSuperAdmin = this.permissionManager.checkHaveAuth(PermissionConfig.getAuthId("super_admin"));// 超级管理员权限
		boolean isDepotAdmin = this.permissionManager.checkHaveAuth(PermissionConfig.getAuthId("depot_admin"));// 库存管理权限
		
		if(!isDepotAdmin&&!isSuperAdmin){
			throw new RuntimeException("没有操作库存的权限");
		}
		//查询商品预警数
		String sql_settings = "select s.cfg_value from es_settings s where s.cfg_group = 'inventory'";
		Map settingmap = daoSupport.queryForMap(sql_settings, null);
		String setting_value = (String) settingmap.get("cfg_value");
		JSONObject jsonObject = JSONObject.fromObject( setting_value );  
		Map itemMap = (Map)jsonObject.toBean(jsonObject, Map.class);
		Integer inventory_warning_count = Integer.parseInt((String) itemMap.get("inventory_warning_count"));
		/*
		 * 拼写sql语句查询数据库预警商品数目，先查询库存表中相同productid且大于预警数的商品数目，如果数目等于仓库数量，则表示
		 * 该货品不需要预警，查询所有不需要预警的货品，与货品表进行比对，在货品表中而不再商品库存表中的货品为需要预警的商品
		 */
		StringBuffer sql = new StringBuffer("select g.goods_id,g.sn,g.name,c.name cname "
				+ " from (select distinct goods_id "
				+ " from es_product p "
				+ " where p.product_id  not in (select productid "
				+ " from (select productid,count(*) count "
				+ " from es_product_store "
				+ " where enable_store> ? "
				+ " group by productid) tem "
				+ " where count=(select count(*) "
				+ " from es_depot))) tem,"
				+ " (select * from es_goods " 
				+ " where store_id=?) g,es_goods_cat c "
				+ " where g.cat_id=c.cat_id " 
				+ "	and g.goods_id=tem.goods_id "
				+ "	and g.market_enable=1 "
				+ " and g.disabled=0 ");
		if(stype!=null && keyword!=null){//基本搜索	
			if(stype==0){
				sql.append(" and ( g.name like '%"+keyword.trim()+"%'");
				sql.append(" or g.sn like '%"+keyword.trim()+"%')");
			}
		}else{ //高级搜索
		
			if(!StringUtil.isEmpty(name)){
				sql.append(" and g.name like '%"+name+"%'");
			}
			
			if(!StringUtil.isEmpty(sn)){
				sql.append(" and g.sn like '%"+sn+"%'");
			}
		}
		
		String countsql = "select count(*) from ("+sql+") temp_table";
		sql.append(" order by g."+sortField+" "+sortType);
		Page webPage = this.daoSupport.queryForPage(sql.toString(), countsql, page, pageSize,inventory_warning_count,ShopApp.self_storeid);
		return webPage;
	}

}
