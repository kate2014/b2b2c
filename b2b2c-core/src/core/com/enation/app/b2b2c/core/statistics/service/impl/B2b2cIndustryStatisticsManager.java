package com.enation.app.b2b2c.core.statistics.service.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.b2b2c.core.statistics.service.IB2b2cIndustryStatisticsManager;
import com.enation.app.shop.core.goods.model.Cat;
import com.enation.app.shop.core.order.service.OrderStatus;
import com.enation.app.shop.core.statistics.model.Collect;
import com.enation.app.shop.core.statistics.service.IIndustryStatisticsManager;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;

@Service
public class B2b2cIndustryStatisticsManager  implements
IB2b2cIndustryStatisticsManager {

	@Autowired
	private IDaoSupport daoSupport;

	// 获取所有的 根节点菜单 sql
	private	String treeRootSql = "select gc.cat_id,gc.name from es_goods_cat gc where gc.parent_id = 0";

	/**
	 * 行业下单金额统计
	 * @author LSJ
	 * @param type 查询  按[1 表示：年] 或 [2表示：月]
	 * @param year 年
	 * @param month 月
	 * @param store_id 店铺ID
	 * @return 下单金额统计 list
	 * 2016年12月6日上午11:38
	 */
	@Override
	public List<Map> statistics_price(int type, int year, int month,Integer store_id) {
		// cat_id 类型 name 名称
		List<Map> treeRoot = (List) this.daoSupport.queryForList(treeRootSql);

		String storeWhere="";
		if(store_id != null && store_id != 0){
			storeWhere = " AND od.store_id = "+store_id;
		}
		
		List<Map> result = new ArrayList<Map>();
		Map tMap = new HashMap();
		for (int i = 0; i < treeRoot.size(); i++) {
			tMap = new HashMap();
			if (type == 1) {
				// 月份统计
				String temSql = "select SUM(oi.price) sump from es_order_items oi left join es_order od on oi.order_id = od.order_id where  oi.cat_id in (select cat_id from es_goods_cat gc where gc.cat_path like '0|"
						+ treeRoot.get(i).get("cat_id") + "%') "
						+ storeWhere
						+ " and od.create_time BETWEEN "+this.getMinvalType1(year, month)+" and "+this.getMaxvalType1(year, month) +" and od.disabled = 0 AND ((od.payment_id = 3  AND od.status = "
						+ OrderStatus.ORDER_COMPLETE
						+ ") OR ( od.pay_status = "
						+ OrderStatus.PAY_YES
						+ "))";
				tMap.put(treeRoot.get(i).get("name"),
						this.daoSupport.queryForMap(temSql).get("sump"));

			} else {
				// 年份统计
				String temSql = "select SUM(oi.price) sump from es_order_items oi left join es_order od on oi.order_id = od.order_id where  oi.cat_id in (select cat_id from es_goods_cat gc where gc.cat_path like '0|"
						+ treeRoot.get(i).get("cat_id") + "%') and od.create_time BETWEEN "+this.getMinvalType0(year)+" and "+this.getMaxvalType0(year)+" and od.disabled = 0 AND ((od.payment_id = 3  AND od.status = "
						+ OrderStatus.ORDER_COMPLETE
						+ ") OR ( od.pay_status = "
						+ OrderStatus.PAY_YES
						+ "))"; 
				tMap.put(treeRoot.get(i).get("name"),
						this.daoSupport.queryForMap(temSql).get("sump"));
			}
			result.add(tMap);
		}
		return result;
	}

	/**
	 * 下单量统计
	 * @author LSJ
	 * @param type 查询  按[1 表示：年] 或 [2表示：月]
	 * @param year 年
	 * @param month 月
	 * @param store_id 店铺ID
	 * @return 下单数 List
	 * 2016年12月6日上午11:38
	 */
	@Override
	public List<Map> statistics_order(int type, int year, int month,Integer store_id) {
		// cat_id 类型 name 名称
		List<Map> treeRoot = (List) this.daoSupport.queryForList(treeRootSql);
		
		String storeWhere="";
		if(store_id != null && store_id != 0){
			storeWhere =" AND od.store_id = "+store_id;
		}
		
		List<Map> result = new ArrayList<Map>();
		Map tMap = new HashMap();
		for (int i = 0; i < treeRoot.size(); i++) {
			tMap = new HashMap();
			if (type == 1) {
				// 月份统计
				String temSql = "select count(od.order_id) sump from  es_order od left join es_order_items oi on oi.order_id = od.order_id where  oi.cat_id in (select cat_id from es_goods_cat gc where gc.cat_path like '0|"
						+ treeRoot.get(i).get("cat_id") + "%') "
						+ storeWhere
						+ " and  od.create_time BETWEEN "+this.getMinvalType1(year, month)+" and "+this.getMaxvalType1(year, month) +" and od.disabled = 0 AND ((od.payment_id = 3  AND od.status = "
						+ OrderStatus.ORDER_COMPLETE
						+ ")	OR ( od.pay_status = "
						+ OrderStatus.PAY_YES
						+ "))"; 
				tMap.put(treeRoot.get(i).get("name"),
						this.daoSupport.queryForInt(temSql));

			} else {
				// 年份统计
				String temSql = "select count(od.order_id) sump from es_order od left join es_order_items oi on oi.order_id = od.order_id where  oi.cat_id in (select cat_id from es_goods_cat gc where gc.cat_path like '0|"
						+ treeRoot.get(i).get("cat_id") + "%') "
						+ storeWhere
						+ " and  od.create_time BETWEEN "+this.getMinvalType0(year)+" and "+this.getMaxvalType0(year)+" and od.disabled = 0 AND ((od.payment_id = 3  AND od.status = "
						+ OrderStatus.ORDER_COMPLETE
						+ ")	OR ( od.pay_status = "
						+ OrderStatus.PAY_YES
						+ "))"; 
				tMap.put(treeRoot.get(i).get("name"),
						this.daoSupport.queryForInt(temSql));

			}
			result.add(tMap);
		}
		return result;
	}

	/**
	 * 下单商品数量统计
	 * @author LSJ
	 * @param type 查询  按[1 表示：年] 或 [2表示：月]
	 * @param year 年
	 * @param month 月
	 * @param store_id 店铺ID
	 * @return 下单商品数量List
	 * 2016年12月6日上午11:38
	 */
	@Override
	public List<Map> statistics_goods(int type, int year, int month,Integer store_id) {
		// cat_id 类型 name 名称
		List<Map> treeRoot = (List) this.daoSupport.queryForList(treeRootSql);
		String storeWhere="";
		if(store_id != null && store_id != 0){
			storeWhere=" AND od.store_id = "+store_id;
		}
		
		List<Map> result = new ArrayList<Map>();
		Map tMap = new HashMap();
		for (int i = 0; i < treeRoot.size(); i++) {
			tMap = new HashMap();
			if (type == 1) {
				// 月份统计
				String temSql = "select count(oi.price) sump from es_order_items oi left join es_order od on oi.order_id = od.order_id where  oi.cat_id in (select cat_id from es_goods_cat gc where gc.cat_path like '0|"
						+ treeRoot.get(i).get("cat_id") + "%') "
						+ storeWhere
						+ " and od.create_time BETWEEN "+this.getMinvalType1(year, month)+" and "+this.getMaxvalType1(year, month)+" and od.disabled = 0 AND ((od.payment_id = 3  AND od.status = "
						+ OrderStatus.ORDER_COMPLETE
						+ ")	OR ( od.pay_status = "
						+ OrderStatus.PAY_YES
						+ "))"; 
				tMap.put(treeRoot.get(i).get("name"),
						this.daoSupport.queryForInt(temSql));

			} else {
				// 年份统计
				String temSql = "select count(oi.price) sump from es_order_items oi left join es_order od on oi.order_id = od.order_id where  oi.cat_id in (select cat_id from es_goods_cat gc where gc.cat_path like '0|"
						+ treeRoot.get(i).get("cat_id") + "%') and od.create_time BETWEEN "+this.getMinvalType0(year)+" and "+this.getMaxvalType0(year)+" and od.disabled = 0 AND ((od.payment_id = 3  AND od.status = "
						+ OrderStatus.ORDER_COMPLETE
						+ ")	OR ( od.pay_status = "
						+ OrderStatus.PAY_YES
						+ "))"; 
				tMap.put(treeRoot.get(i).get("name"),
						this.daoSupport.queryForInt(temSql));

			}
			result.add(tMap);
		}
		return result;
	} 

	/**
	 * 获取所有的一级菜单
	 * @author LSJ
	 * @return 菜单list
	 * 2016年12月6日上午11:38
	 */
	@Override
	public List<Map> getAllRootMenu() { 
		return this.daoSupport.queryForList(treeRootSql);
	} 

	/**
	 * 概括总览表
	 * @param cat_id 商品类型ID
	 * @param listAllChildren  获取某个类别的所有子类，所有的子孙
	 * @param store_id 店铺ID
	 * @return 总览表 List
	 */
	@Override
	public List<Collect> listCollect(int cat_id,List<Cat> cats,Integer store_id) { 
		List<Collect> collects = new ArrayList<Collect>(); 
		//展示的是近三十天的属性。所以先获取两个时间戳
		Date now = new Date();  
		Long endDate = DateUtil.getDateline();
		Long startDate = DateUtil.getDateline(this.getLastMonth(now), null);
		Collect collect = null;
		DecimalFormat df = new DecimalFormat("######0.00");
		String avgWhere = "";
		String saleGoodsWhere="";
		String residueWhere="";
		String countWhere="";
		if(store_id != null && store_id != 0){
			avgWhere=" AND store_id = " +store_id;
			saleGoodsWhere=" AND od.store_id = "+store_id;
			residueWhere=" AND g.store_id = " +store_id;
			countWhere = " AND od.store_id = "+store_id;
		}
		
		for (Cat cat : cats) { 
			//这里查询到的有一个是父类所以要判定
			if(cat.getCat_id()==cat_id){
				continue;
			}

			collect = new Collect();
			//名称
			collect.setName(cat.getName());
			//平均价格
			String avgSql = "select AVG(gs.price) as avg from es_goods gs where cat_id in (select cat_id from es_goods_cat gc where gc.cat_path like '%|"	+ cat.getCat_id() + "|%') "+avgWhere;
			try {
				//获取的数据AvgPrice为null  返回 0 兼容sqlserver
				collect.setAvgPrice(StringUtil.toDouble(this.daoSupport.queryForMap(avgSql).get("avg"), false));
			} catch (NumberFormatException e1) { 
			}
			//有销量商品数
			String salesGoodsSql = "select count(0)from (select oi.goods_id from es_order_items oi " +
					"left join es_order od on oi.order_id = od.order_id where oi.cat_id in " +
					"(select cat_id from es_goods_cat gc where gc.cat_path like '%|"
					+ cat.getCat_id() + "|%') and od.create_time BETWEEN "+startDate+" and "+endDate+""
					+" and od.disabled = 0 "
					+saleGoodsWhere
					+ " AND ((od.payment_id = 3  AND od.status = "
					+ OrderStatus.ORDER_COMPLETE
					+ ")	OR ( od.pay_status = "
					+ OrderStatus.PAY_YES
					+ "))"+" GROUP BY oi.goods_id) tt";
			try {
				collect.setSalesGoodsNum(this.daoSupport.queryForInt(salesGoodsSql));
			} catch (Exception e) { 
			}
			//无销量商品数 / 总商品数
			String residueSql = "select count(0) from es_goods g where g.cat_id in (select cat_id from es_goods_cat gc where gc.cat_path like '%|"	+ cat.getCat_id() + "|%') " +residueWhere;
			try {
				collect.setCountGoods(this.daoSupport.queryForInt(residueSql));
			} catch (Exception e) { 
			}
			try {
				collect.setResidue(collect.getCountGoods()-collect.getSalesGoodsNum());
			} catch (Exception e) { 
			}
			//销售总额
			String countSql = "select sum(oi.price) as zonge,count(0) as zongs from es_order_items oi left join es_order od on oi.order_id = od.order_id where  oi.cat_id in (select cat_id from es_goods_cat gc where gc.cat_path like '%|"
					+ cat.getCat_id() + "|%') "
					+countWhere
					+ " and od.create_time BETWEEN "+startDate+" and "+endDate +" and od.disabled = 0 AND ((od.payment_id = 3  AND od.status = "
					+ OrderStatus.ORDER_COMPLETE
					+ ")	OR ( od.pay_status = "
					+ OrderStatus.PAY_YES
					+ "))";
			Map map = this.daoSupport.queryForMap(countSql);
			try {
				//获取的数据为Saleroom， Sales   返回 0    兼容sqlserver 
				collect.setSaleroom(StringUtil.toDouble(map.get("zonge"), false));
				collect.setSales(StringUtil.toInt(map.get("zongs"), false));
			} catch (NumberFormatException e) { 
			}

			collects.add(collect);
		}
		return collects;
	}


	/**
	 * 获取上个月的同一时间
	 * @param 当前时间
	 * @return
	 */
	private String getLastMonth(Date now) { 
		// date getYear 获取到的年份 是从1990年算起所以需要加 1990 
		GregorianCalendar calendar = new GregorianCalendar(now.getYear()+1900,
				now.getMonth(), now.getDate()); 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");// 定义日期显示格式
		calendar.add(Calendar.MONTH, -1);// 获取上个月月份
		return (sdf.format(calendar.getTime()));
	}

	/**
	 * 获取当前月份的 unix时间戳  最大值
	 * @param 年份
	 * @param 月份
	 * @return
	 */
	private long getMaxvalType1(int year,int month){ 
		//如果是 12月 那么明年1月的零时作为结束时间
		if(month==12){ 
			return DateUtil.getDateHaveHour((year+1)+"-01-01 00"); 
		}
		//否则 下个月 0时
		return DateUtil.getDateHaveHour(year+"-"+(month+1)+"-01 00"); 
	}
	/**
	 * 获取当前月份的  unix时间戳  最小值
	 * @param 年份
	 * @param 月份
	 * @return
	 */
	private long getMinvalType1(int year,int month){
		return DateUtil.getDateHaveHour(year+"-"+month+"-01 00"); 
	}


	/**
	 * 获取当前年份的  unix时间戳 最大值
	 * @param 年份
	 * @return
	 */
	private long getMaxvalType0(int year){ 
		return DateUtil.getDateline(year+"-12-31 23:59:59" , "yyyy-MM-dd HH:mm:ss");
	}
	/**
	 * 获取当前年份的 unix时间戳  最小值
	 * @param 年份
	 * @return
	 */
	private long getMinvalType0(int year){
		return DateUtil.getDateline(year+"-01-01");
	}



}