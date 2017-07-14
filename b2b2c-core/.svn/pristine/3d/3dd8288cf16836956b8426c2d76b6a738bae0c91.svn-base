package com.enation.app.b2b2c.core.statistics.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.b2b2c.core.statistics.service.IB2b2cGoodsStatisticsManager;
import com.enation.app.shop.core.goods.service.IGoodsCatManager;
import com.enation.app.shop.core.statistics.service.IGoodsStatisticsManager;
import com.enation.eop.sdk.context.EopSetting;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.StringUtil;

@Service
public class B2b2cBackendGoodsStatisticsManager implements IB2b2cGoodsStatisticsManager {

	@Autowired
	private IGoodsCatManager goodsCatManager;
	
	@Autowired
	private IDaoSupport daoSupport;
	
	/**
	 * 价格销售统计
	 * @param start_time 开始时间
	 * @param end_time 结束时间
	 * @param cat_id 商品类型ID
	 * @param list 价格区间
	 * @param map 价格销售统计集合
	 * @param store_id 店铺ID
	 * @return 获取到数据集
	 */
	@Override
	public List getPriceSalesList(long startTime,long endTime, Integer catid,
			List<Map> list, Map map,Integer store_id) {
		
		String cat_path = "";
		if(catid==null){
			cat_path = "1";
		}else if(catid.intValue()==0){
			cat_path = "0";
		}else{
			cat_path = this.daoSupport.queryForString("select gc.cat_path from es_goods_cat gc where gc.cat_id="+catid);
		}
		
		String when_sql =  this.getPriceInterval(list);
		String sql = "select count(0) as t_num , case "+when_sql+" as price_interval  from es_order_items oi  left join es_order o on oi.order_id=o.order_id "
				+ " where  oi.cat_id in (select gc.cat_id from es_goods_cat gc where gc.cat_path like '"+cat_path+"%') ";
		sql+=" and create_time >= ? and  create_time <=? ";
		if(store_id != null && store_id != 0){
			sql+=" and  o.store_id = "+store_id;
		}
		sql+=" group by case "+when_sql;
		List<Map> data_list = this.daoSupport.queryForList(sql, startTime , endTime);
		
		List datalist = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			boolean flag = true;
			for (int j = 0; j < data_list.size(); j++) {
				Map m = data_list.get(j);
				if(m.get("price_interval").equals(i+1)){
					datalist.add(m.get("t_num"));
					flag=false;
				}
			}
			if(flag){
				datalist.add(0);
			}
		}

		
		return datalist;
	}
	
	/**
	 * 热点商品金额
	 * @author LSJ
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param page 当前页数
	 * @param pageSize 分页大小
	 * @param catid 商品类型ID
	 * @param map 价格销售统计集合
	 * @param store_id 店铺ID
	 * @return 获取数据集分页
	 * 2016年12月6日上午11:38
	 */
	@Override
	public Page getHotGoodsMoney(long startTime, long endTime, int page, int pageSize, Integer catid, Map map,Integer store_id) {
		
		String cat_path = "";
		if(catid==null){
			cat_path= "1";
		}else if(catid.intValue()==0){
			cat_path = "0";
		}else{
			cat_path = this.daoSupport.queryForString("select gc.cat_path from es_goods_cat gc where gc.cat_id="+catid);
		}
		String storeWhere="";
		if(store_id != null && store_id != 0){
			storeWhere = " AND o.store_id = "+store_id;
		}
		
		String sql = "select oi.goods_id,oi.name as oiname,SUM(oi.num) as t_num,SUM(oi.price*oi.num) as t_money from es_order_items oi left join es_order o on oi.order_id=o.order_id "
				+ "where oi.cat_id in (select gc.cat_id from es_goods_cat gc where gc.cat_path like '"+cat_path+"%') ";
		sql+=" and create_time >= ? and  create_time <=? ";
		sql+=storeWhere;
		sql+=" GROUP BY oi.goods_id, oi.name ORDER BY SUM(oi.price*oi.num) desc";
		
		List list = this.daoSupport.queryForListPage(sql, page, pageSize, startTime,endTime);
		
		List t_list =  this.daoSupport.queryForList(sql, startTime,endTime);
		long totalSize = t_list.size();
		Page webPage = new Page(page, totalSize, pageSize, list);
		
		return webPage;
	}
	
	/**
	 * 热卖商品数量
	 * @author LSJ
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param page 当前页数
	 * @param pageSize 分页大小
	 * @param catid 商品类型ID
	 * @param map 热卖商品数量
	 * @param store_id
	 * @return 获取数据集分页
	 * 2016年12月6日上午11:38
	 */
	@Override
	public Page getHotGoodsNum(long startTime, long endTime, int page, int pageSize, Integer catid, Map map,Integer  store_id) {
		
		String cat_path = "";
		if(catid==null){
			cat_path= "1";
		}else if(catid.intValue()==0){
			cat_path = "0";
		}else{
			cat_path = this.daoSupport.queryForString("select gc.cat_path from es_goods_cat gc where gc.cat_id="+catid);
		}
		String storeWhere="";
		if(store_id != null && store_id != 0){
			storeWhere=" AND o.store_id = "+store_id;
		}
		
		String sql = "select oi.goods_id,oi.name as oiname,SUM(oi.num) as t_num,SUM(oi.price*oi.num) as t_money from es_order_items oi left join es_order o on oi.order_id=o.order_id "
				+ "where oi.cat_id in (select gc.cat_id from es_goods_cat gc where gc.cat_path like '"+cat_path+"%') ";
		sql+=" and create_time >= ? and  create_time <=? ";
		sql+=storeWhere;
		sql+="  GROUP BY oi.goods_id, oi.name ORDER BY t_num desc";
		//System.out.println(sql);
		List list = this.daoSupport.queryForListPage(sql, page, pageSize, startTime,endTime);
		
		List t_list =  this.daoSupport.queryForList(sql, startTime,endTime);
		long totalSize = t_list.size();
		Page webPage = new Page(page, totalSize, pageSize, list);
		
		return webPage;
	}
	
	public String getPriceInterval(List<Map> list){
		String sql ="";
		int i=1;
		for (Map map : list) {
			String minprice = (String) map.get("minprice");
			String maxprice = (String) map.get("maxprice");
			sql+=" when o.order_amount >= "+minprice +" and  o.order_amount <="+ maxprice +" then "+i++;
		}
		sql+=" else 0 end";
		return sql;
	}
	
	/**
	 * 商品销售明细
	 * @author LSJ
	 * @param start_time 开始时间
	 * @param end_time	结束时间
	 * @param page 当前分页页数
	 * @param pageSize 分页大小
	 * @param cat_id 商品分类ID
	 * @param name 商品名称
	 * @param map 所有商品明细
	 * @param store_id 店铺ID
	 * @return	获取到所有商品明细分页
	 * 2016年12月6日上午11:38
	 */
	@Override
	public Page getgoodsSalesDetail(long startTime, long endTime, int page,
			int pageSize, Integer catid, String name, Map map,Integer store_id) {
		
		String cat_path = "";
		if(catid==null || catid.intValue()==0){
			cat_path= "0";
		}else{
			cat_path = this.daoSupport.queryForString("select gc.cat_path from es_goods_cat gc where gc.cat_id="+catid);
		}
		String storeWhere="";
		if(store_id != null && store_id !=0 ){
			storeWhere=" AND o.store_id = "+store_id;
		}
		
		String sql ="select oi.name,oi.product_id,count(oi.order_id) t_order_num,SUM(oi.num) t_goods_num,SUM(oi.num*oi.price) t_money "
				+ "from es_order_items oi left join es_order o on oi.order_id=o.order_id  "
				+ " where oi.cat_id in (select gc.cat_id from es_goods_cat gc where gc.cat_path like '"+cat_path+"%') ";
		sql+=" and o.create_time >= ? and  o.create_time <=? ";
		sql+=storeWhere;
		if(!StringUtil.isEmpty(name)){
			sql+="  and oi.name like '%"+name+"%' ";
		}
		sql+=" GROUP BY oi.goods_id, oi.name, oi.product_id ";
		
		String db_type = EopSetting.DBTYPE;
		if(db_type.equals("3")){
			sql += " order by oi.product_id desc";
		}
		
		List list = this.daoSupport.queryForListPage(sql, page, pageSize, startTime,endTime);
		List t_list =  this.daoSupport.queryForList(sql, startTime,endTime);
		long totalSize = t_list.size();
		Page webPage = new Page(page, totalSize, pageSize, list);
		return webPage;
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.statistics.service.IB2b2cGoodsStatisticsManager#getCollectPage(int, int, java.lang.Integer)
	 */
	@Override
	public Page getCollectPage(int page, int pageSize, Integer storeId) {
		String sql = "select count(f.member_id) y,f.goods_id,g.name,g.store_id,g.price,s.store_name from es_favorite f left join es_goods g on f.goods_id = g.goods_id left join es_store s on s.store_id = g.store_id  ";
		if(storeId!=null && storeId!=0){
			sql+=" where g.store_id=+"+storeId;
		}
		sql+=" GROUP BY f.goods_id,g.name,g.price,s.store_name,g.store_id ORDER BY COUNT(f.member_id) desc";
		List list =  this.daoSupport.queryForListPage(sql, page, pageSize);
		List totleList = this.daoSupport.queryForList(sql);
		Page webPage = new Page(page, totleList.size(), pageSize, list);
		return webPage;
	}


	

}
