package com.enation.app.b2b2c.core.statistics.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.b2b2c.core.statistics.service.IB2b2cReturnedStatisticsManager;
import com.enation.app.shop.core.order.model.SellBackStatus;
import com.enation.app.shop.core.statistics.service.IReturnedStatisticsManager;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.util.DateUtil;
@Service
public class B2b2cReturnedStatisticsManager  implements IB2b2cReturnedStatisticsManager {
	
	@Autowired
	private IDaoSupport daoSupport;
	
	/**
	 * 退款统计列表    按月统计
	 * @author LSJ
	 * @param year 年
	 * @param month 月
	 * @param store_id 店铺ID
	 * @return 退款统计列表list
	 * 2016年12月6日下午15:16
	 */
	@Override
	public List<Map> statisticsMonth_Amount(long year,long month,String store_id){
		String condition_sql = createSql(1, year+"-"+month);
		String storeWhere="";
		if(store_id != null && !"0".equals(store_id)){
			storeWhere=" AND store_id = "+ store_id;
		}
		String sql =   "select count(0) as t_num,SUM(alltotal_pay) as t_money, case "+ condition_sql +" as month  from es_sellback_list where tradestatus="
				+SellBackStatus.refund.getValue()
				+storeWhere
				+ " group by case "+condition_sql;
		List list = this.daoSupport.queryForList(sql);
		return list;
	}
	
	/**
	 * 退款统计列表   按年统计
	 * @author LSJ
	 * @param year 年
	 * @param store_id 店铺ID
	 * @return 退款统计列表list
	 * 2016年12月6日下午15:16
	 */
	@Override
	public List<Map> statisticsYear_Amount(int year,String store_id) {
		String condition_sql = createSqlByYear(1, year+"");
		String storeWhere="";
		if(store_id !=null && !"0".equals(store_id)){
			storeWhere= " AND store_id = "+ store_id;
		}
		String sql =  "select count(0) as t_num,SUM(alltotal_pay) as t_money, case "+ condition_sql +" as month  from es_sellback_list where tradestatus=3"
				+storeWhere
				+ " group by case "+condition_sql;
		
		List list = this.daoSupport.queryForList(sql);
		return list;
	}
	
	/**
	 * 创建SQL语句
	 * @param type 1.按照月查询(查询出此月每天的下单金额)
	 * @param date 时间，选中的某一月
	 */
	private  String createSql(int type,String date){
		StringBuffer sql =new StringBuffer();
	  
		for(int i=1;i<=29;i++){
			  String day = "0"+i;
			  day= day.substring( day.length()-2,day.length());
			  String day_date= date+"-"+day;
			  long start = DateUtil.getDateline(  day_date+" 00:00:00", "yyyy-MM-dd HH:mm:ss");
			  long end = DateUtil.getDateline(  day_date+" 23:59:59", "yyyy-MM-dd HH:mm:ss");
			  sql.append(" when regtime >= "+start +" and   regtime <="+ end +" then "+i );
		 }
		
		 sql.append(" else 0 end");
		 return sql.toString();
	}
	
	/**
	 * 创建SQL语句
	 * @param type 1.按照年查询(查询出此年每月的下单金额)
	 * @param date 时间，选中的某一年
	 */
	private  String createSqlByYear(int type,String date){
		StringBuffer sql =new StringBuffer();
		for(int i=1;i<=12;i++){
			String day = "0"+i;
			day = day.substring( day.length()-2,day.length());
			String day_date = date+"-"+day;
			long start = DateUtil.getDateline(day_date+"-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
			long end = DateUtil.getDateline(day_date+"-31 23:59:59", "yyyy-MM-dd HH:mm:ss");
			sql.append(" when regtime >= "+start +" and  regtime <="+ end +" then "+i );
		 }
		 sql.append(" else 0 end");
		 return sql.toString();
	}
	
}
