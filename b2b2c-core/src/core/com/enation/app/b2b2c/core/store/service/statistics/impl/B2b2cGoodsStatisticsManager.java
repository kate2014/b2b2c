package com.enation.app.b2b2c.core.store.service.statistics.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.b2b2c.core.store.service.statistics.IB2b2cGoodsStatisticsManager;
import com.enation.app.b2b2c.core.store.service.statistics.StatisticsUtil;
import com.enation.app.shop.core.order.service.OrderStatus;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;

/**
 * 商品分析统计 接口实现体
 * @author xin
 */

@Service("b2b2cGoodsStatisticsManager")
public class B2b2cGoodsStatisticsManager  implements IB2b2cGoodsStatisticsManager {
	
	@Autowired
	private IDaoSupport daoSupport;
	
	@Autowired
	IStoreMemberManager storeMemberManager;
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.statistics.IB2b2cGoodsStatisticsManager#getOrderPriceDis(java.util.List, java.lang.String, java.lang.String)
	 */
	@Override
	public List<Map<String, Object>> getGoodsPriceSales(List<Integer> sections,
			String startDate, String endDate,Integer catId,Integer storeid) {
			
		String interval = "";		//INTERVAL 分组
		String dateWhere = " AND o.status>="+OrderStatus.ORDER_PAY+" AND o.status!="+OrderStatus.ORDER_CANCELLATION+" AND o.pay_status="+OrderStatus.PAY_YES;	// 时间条件
		String intervalDesc = "";	//INTERVAL 描述
		List<Integer> list = new ArrayList<Integer>();	//筛选后的区间值
		
		// 如果包含开始时间条件
		if (startDate != null && !"".equals(startDate)) {
			
			dateWhere += " AND o.create_time >= " + startDate;
		}
		// 如果包含结束时间条件
		if (endDate != null && !"".equals(endDate)) {
			
			dateWhere += " AND o.create_time <= " + endDate;
		}
		
		// 遍历去除null值
		for(Integer temp : sections ) {
			if(temp != null) {
				list.add(temp);
			}
		}
		/*
		Collections.sort(list);			//先按照价格从小到大排序
		
		//遍历区间数组 拼凑interval分组和描述
		for (int i = 0; i < list.size(); i++ ) {
			int price = list.get(i);
			int nextPrice = 0;				//下一个区间的价格
			
			interval += price + ",";
			
			//如果是最后一个区间
			if (i == list.size() - 1) {
								
				intervalDesc += "'" + price + "+',";
			} else{
				
				nextPrice = list.get(i + 1);
				intervalDesc += "'" + price + "~" + nextPrice + "',";
			}
			
		}*/
		
		/*//减去最后一个逗号
		interval = interval.substring(0, interval.length() - 1);
		intervalDesc = intervalDesc.substring(0, intervalDesc.length() - 1);
		*/
		
		String cat_path = "";
		if(catId==null){
			cat_path = "0";
		}else if(catId.intValue()==0){
			cat_path = "0";
		}else{
			cat_path = this.daoSupport.queryForString("select gc.cat_path from es_goods_cat gc where gc.cat_id="+catId);
			
		}
		
		
		/*String sql = "SELECT SUM(oi.num) AS num, elt(INTERVAL(oi.price, "
				+ interval
				+ "), "
				+ intervalDesc
				+ ")AS elt_data FROM es_order_items oi left join es_order o on oi.order_id=o.order_id WHERE store_id = ? "
				+ dateWhere
				+ " AND oi.cat_id IN (select gc.cat_id from es_goods_cat gc where gc.cat_path like '"+cat_path+"%') "
				+ " GROUP BY elt_data ORDER BY elt_data DESC";*/
		
		//拼接CASE语句
		String caseStatement = getGoodsPriceSqlCaseStatement(list);
		String sql = "SELECT SUM(oi.num) AS num, " + caseStatement
				+ "AS elt_data FROM es_order_items oi left join es_order o on oi.order_id=o.order_id WHERE store_id = ? "
				+ dateWhere
				+ " AND oi.cat_id IN (select gc.cat_id from es_goods_cat gc where gc.cat_path like '"+cat_path+"%') "
				+ " GROUP BY oi.price";
		
		String mainSql = "SELECT SUM(t1.num) num, t1.elt_data FROM(" + sql + ") t1 GROUP BY t1.elt_data ORDER BY t1.elt_data";
		
		List<Map<String, Object>> data = this.daoSupport.queryForList(mainSql,storeid);
		data=StatisticsUtil.getInstance().fitOrderPriceData(data,list);
		return data;
	}
	
	/**
	 * 生成价格销量统计的SQL CASE语句
	 * @param ranges 整数数组
	 * @return SQL CASE Statement
	 */
	private static String getGoodsPriceSqlCaseStatement(List<Integer> ranges) {
		if (ranges == null || ranges.size() == 0) {
			return "0";
		}
		//由大到小排序
		Collections.sort(ranges, new Comparator<Integer>(){
			@Override
			public int compare(Integer o1, Integer o2) {
				if (o1 > o2) {
					return -1;
				} else  if (o1 < o2) {
					return 1;
				}
				return 0;
			}
		});
		
		StringBuilder sb = new StringBuilder("(case ");
		for (int i = 0; i < ranges.size(); i++) {
			Integer num = ranges.get(i);
			Integer nextNum = 0;
			if (i == 0) {
				sb.append("when oi.price > ").append(num).append(" then '").append(num).append("+' ");
			} 
			if (i < ranges.size() - 1) {
				nextNum = ranges.get(i + 1);
				sb.append("when oi.price >= ").append(nextNum).append(" and oi.price <= ").append(num)
				  .append(" then '").append(nextNum).append("~").append(num).append("' ");
			}
		}
		sb.append("else '0' end ) ");
		return sb.toString();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.statistics.IB2b2cGoodsStatisticsManager#getGoodsNumTop(int, java.lang.String, java.lang.String)
	 */
	@Override
	public List<Map<String, Object>> getGoodsNumTop(int topNum, String startDate, String endDate) {
		
		//获取当前登陆的会员店铺id
		StoreMember storemember=storeMemberManager.getStoreMember();
		int storeId=storemember.getStore_id();
		
		// 如果排名没有值
		if (topNum == 0) {
			topNum = 30;
		}
		
		String dateWhere = "";		// 时间条件
		
		// 如果包含开始时间条件
		if (startDate != null && !"".equals(startDate)) {
			
			dateWhere += "AND o.create_time >= " + startDate;
		}
		// 如果包含结束时间条件
		if (endDate != null && !"".equals(endDate)) {
			
			dateWhere += " AND o.create_time <= " + endDate;
		}

		// 拼接Sql 条件是 网上支付且已经付款的 或者 货到付款且订单完成的 订单
		String sql = "FROM es_order_items WHERE order_id IN (SELECT order_id FROM es_order o WHERE "
				   +" store_id = ? AND status=? "
				   + dateWhere
				   +") GROUP BY name, goods_id";
		String selectPage = "SELECT name,goods_id,SUM(ship_num) as sum " + sql ;
		String selectCount = "select count(*) from (" + selectPage + ") t0";
		selectPage += " ORDER BY SUM(ship_num) DESC";
		//List<Map<String, Object>> list = this.daoSupport.queryForList(sql,storeId,OrderStatus.ORDER_COMPLETE);
		Page page = this.daoSupport.queryForPage(selectPage, selectCount, 1, topNum, storeId, OrderStatus.ORDER_COMPLETE);
		return (List<Map<String, Object>>) page.getResult();
	}

	

	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.statistics.IB2b2cGoodsStatisticsManager#getGoodsOrderPriceTop(int, java.lang.String, java.lang.String)
	 */
	@Override
	public List<Map<String, Object>> getGoodsOrderPriceTop(int topNum,
			String startDate, String endDate) {
		//获取当前登陆的会员店铺id
		StoreMember storemember=storeMemberManager.getStoreMember();
		int storeId=storemember.getStore_id();
		
		// 如果排名没有值
		if (topNum == 0) {
			topNum = 30;
		}
		
		String dateWhere = "";		// 时间条件
		
		// 如果包含开始时间条件
		if (startDate != null && !"".equals(startDate)) {
			
			dateWhere += "AND o.create_time >= " + startDate;
		}
		// 如果包含结束时间条件
		if (endDate != null && !"".equals(endDate)) {
			
			dateWhere += " AND o.create_time <= " + endDate;
		}

		// 拼接Sql 条件是 网上支付且已经付款的 或者 货到付款且订单完成的 订单
		String sql = "FROM es_order_items WHERE order_id IN (SELECT order_id FROM es_order o WHERE "
				   +" store_id = ? AND status=? "
				   + dateWhere
				   +""
				   +") GROUP BY goods_id, name, price ";
		
		String selectPage = "SELECT name,goods_id,price,SUM(ship_num) as allnum,SUM(ship_num) * price AS sum " + sql;
		
		String selectCount = "SELECT count(*) from (" + selectPage + ") t0";
		
		selectPage += " ORDER BY SUM(ship_num) * price DESC";

		Page page = this.daoSupport.queryForPage(selectPage, selectCount, 1, topNum, storeId, OrderStatus.ORDER_COMPLETE);

		return (List<Map<String, Object>>) page.getResult();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.statistics.IB2b2cGoodsStatisticsManager#getGoodsDetal(int, java.lang.String)
	 */
	@Override
	public List<Map<String, Object>> getGoodsDetal(Integer catId, String name) {
		//获取当前登陆的会员店铺id
		StoreMember storemember=storeMemberManager.getStoreMember();
		int storeId=storemember.getStore_id();
		
		String cat_path = "";
		if(catId==null){
			cat_path = "1";
		}else if(catId.intValue()==0){
			cat_path = "0";
		}else{
			cat_path = this.daoSupport.queryForString("select gc.cat_path from es_goods_cat gc where gc.cat_id="+catId);
			
		}
		
		//获得今天的最后时间
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH )+1;
        int day = cal.get(Calendar.DATE);
		long endtime =(long)DateUtil.toDate(year+"-"+month+"-"+day+" 23:59:59", "yyyy-MM-dd HH:mm:ss").getTime()/1000;
        
		
		long startime=  endtime-2592000;
		
        String startDate= String.valueOf(startime);
        String endDate= String.valueOf(endtime);
        
        String nameWhere="";
        if(name!=null && !name.equals("")){
        	nameWhere= " AND oi.name LIKE '%"+name+"%' ";
        }
        String dateWhere=" AND o.create_time >= "+startDate+" AND o.create_time <= "+endDate;

		// 拼接Sql 条件是 网上支付且已经付款的 或者 货到付款且订单完成的 订单
		String sql ="SELECT name , SUM(num) AS nums,ROUND(price,2) AS price,ROUND(price * SUM(num),2) AS prices FROM es_order_items oi WHERE oi.order_id IN (SELECT order_id FROM es_order o WHERE store_id = ? and status=?"
				   +""+nameWhere
				   +""+dateWhere
				   + " AND oi.cat_id IN (select gc.cat_id from es_goods_cat gc where gc.cat_path like '"+cat_path+"%')"
			       +") GROUP BY goods_id, name, price";
		
		List<Map<String, Object>> list = this.daoSupport.queryForList(sql,storeId,OrderStatus.ORDER_COMPLETE);
		
		return list;
	}
	
	


}
