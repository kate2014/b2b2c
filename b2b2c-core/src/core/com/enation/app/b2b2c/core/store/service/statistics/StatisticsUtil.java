package com.enation.app.b2b2c.core.store.service.statistics;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.enation.framework.util.DateUtil;

/**
 * 统计专用工具类
 * 本类使用单例模式,调用者使用本类中的方法请先获取类的实例对象
 * @author jianghongyan 2016年7月1日 版本改造
 * @version v6.1
 * @since v6.1
 * 
 */
public class StatisticsUtil {
	
	private static StatisticsUtil statisticsUtil=new StatisticsUtil();
	private StatisticsUtil(){}
	public static StatisticsUtil getInstance(){
		return statisticsUtil;
	}
	
	/**
	 * 获取当前月份的 unix时间戳  最大值
	 * @param month
	 * @return
	 */
	public long getMaxvalType1(int year,int month){ 
		//如果是 12月 那么明年1月的零时作为结束时间
		if(month==12){ 
			return DateUtil.getDateHaveHour((year+1)+"-01-01 00"); 
		}
		//否则 下个月 0时
		return DateUtil.getDateHaveHour(year+"-"+(month+1)+"-01 00"); 
	}
	/**
	 * 获取当前月份的  unix时间戳  最小值
	 * @param month
	 * @return
	 */
	public long getMinvalType1(int year,int month){
		 return DateUtil.getDateHaveHour(year+"-"+month+"-01 00"); 
	}
	/**
	 * 获取当前年份的  unix时间戳 最大值
	 * @param month
	 * @return
	 */
	public long getMaxvalType0(int year){ 
		 return DateUtil.getDateline(year+"-12-31 23:59:59", "yyyy-MM-dd HH:mm:ss");
	}
	/**
	 * 获取当前年份的 unix时间戳  最小值
	 * @param month
	 * @return
	 */
	public long getMinvalType0(int year){
		 return DateUtil.getDateline(year+"-01-01");
	}
	/**
	 * 获取最小和最大时间戳
	 * @param cycle_type 查询周期
	 * @param year 查询年份
	 * @param month 查询月份
	 * @return
	 */
	public long[] getStartTimeAndEndTime(Integer cycle_type,Integer year,Integer month){
		long[] times=new long[2];
		
		switch (cycle_type) {
		case 1:
			times[0]=getMinvalType1(year, month);
			times[1]=getMaxvalType1(year, month);
			break;
		case 2:
			times[0]=getMinvalType0(year);
			times[1]=getMaxvalType0(year);
			break;
		default:
			times[0]=getMinvalType1(year, month);
			times[1]=getMaxvalType1(year, month);
			break;
		}
		return times;
	}
	
	/**
	 * 填充客单价分布数据集合
	 * @param data
	 * @param ranges
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> fitOrderPriceData(List<Map<String, Object>> data,
			List<Integer> ranges) {
		if (ranges == null || ranges.size() == 0) {
			return data;
		}
		List<Map<String,Object>> data2=new ArrayList<Map<String,Object>>();
		//由小道大排序
		Collections.sort(ranges, new Comparator<Integer>(){
			@Override
			public int compare(Integer o1, Integer o2) {
				if (o1 < o2) {
					return -1;
				} else  if (o1 > o2) {
					return 1;
				}
				return 0;
			}
		});
		Map<String,Object> exrangesMap=new HashMap<String,Object>();
		for (Map<String,Object> map : data) {
			String elt_data=map.get("elt_data").toString();
			exrangesMap.put(elt_data,map);
		}
		for (int i = 0; i < ranges.size()-1; i++) {
			String r=ranges.get(i).toString()+"~"+ranges.get(i+1).toString();
			if(!exrangesMap.containsKey(r)){
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("num", 0.0);
				map.put("elt_data", r);
				map.put("need_pay_money", 0.0);
				data2.add(map);
			}else{
				data2.add((Map<String, Object>) exrangesMap.get(r));
			}
		}
		return data2;
	}
	/**
	 * 填充购买时间数据集合
	 * @param data
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> fitTimeData(List<Map<String, Object>> data) {
		Map<Integer,Object> exnumhourMap=new HashMap<Integer,Object>();
		List<Map<String,Object>> data2=new ArrayList<Map<String,Object>>();
		for (Map<String, Object> map : data) {
			Integer num_hour=Integer.valueOf(map.get("hour_num").toString());
			exnumhourMap.put(num_hour,map);
		}
		for(int i=0;i<24;i++){
			if(!exnumhourMap.containsKey(i)){
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("num", 0);
				map.put("hour_num", i);
				data2.add(map);
			}else{
				data2.add((Map<String, Object>) exnumhourMap.get(i));
			}
		}
		
		return data2;
	}
	
	/**
	 * 创建SQL语句
	 * @param type 1.按照月查询(查询出此月每天的下单金额)
	 * @param date
	 */
	public  String createSql(int type,int year,int month){
		
		if(type==2){
			return createSqlByYear(type,year);
		}
		StringBuffer sql =new StringBuffer();
		String date = year+"-"+month;
		int day = getDaysByYearMonth(year, month);
		for(int i=1;i<=day;i++){
			String day_date= date+"-"+i;
			long start = DateUtil.getDateline(  day_date+" 00:00:00", "yyyy-MM-dd HH:mm:ss");
			long end = DateUtil.getDateline(  day_date+" 23:59:59", "yyyy-MM-dd HH:mm:ss");
			sql.append(" when create_time >= "+start +" and   create_time <="+ end +" then "+i );
		 }
		
		 sql.append(" else 0 end");
		 return sql.toString();
	}
	/**
	 * 创建sql语句
	 * @param dayUntilNow 距今多少天
	 * @return
	 */
	public String createSql(int dayUntilNow){
		StringBuffer sql=new StringBuffer();
		for(int i=dayUntilNow;i>=1;i--){
			Map<String,Object> map=DateUtil.getYearMonthAndDay(i);
			String year=map.get("year").toString();
			String month=map.get("month").toString();
			String day=map.get("day").toString();
			String day_date=year+"-"+month+"-"+day;
			long start = DateUtil.getDateline(  day_date+" 00:00:00", "yyyy-MM-dd HH:mm:ss");
			long end = DateUtil.getDateline(  day_date+" 23:59:59", "yyyy-MM-dd HH:mm:ss");
			sql.append(" when complete_time >= "+start +" and   complete_time <="+ end +" then '"+month+"-"+day+"'" );
		}
		sql.append(" else '0' end");
		return sql.toString();
	}

	
	/**
	 * 创建SQL语句
	 * @param type 1.按照年查询(查询出此年每月的下单金额)
	 * @param date
	 */
	public  String createSqlByYear(int type,int year){
		StringBuffer sql =new StringBuffer();
		for(Integer i=1;i<=12;i++){
			Integer month = i;
			String day_date = year+"-"+month;
			long start = DateUtil.getDateline(day_date+"-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
			int day=getDaysByYearMonth(year, month);
			long end = DateUtil.getDateline(day_date+"-"+day+" 23:59:59", "yyyy-MM-dd HH:mm:ss");
			sql.append(" when create_time >= "+start +" and  create_time <="+ end +" then "+i );
		 }
		 sql.append(" else 0 end");
		 return sql.toString();
	}
	/**
	 * 获取当前年月最大日期
	 * @param year 年份
	 * @param month 月份
	 * @return 最大日期
	 */
	public  int getDaysByYearMonth(int year, int month) {  
        Calendar a = Calendar.getInstance();  
        a.set(Calendar.YEAR, year);  
        a.set(Calendar.MONTH, month - 1);  
        a.set(Calendar.DATE, 1);  
        a.roll(Calendar.DATE, -1);  
        int maxDate = a.get(Calendar.DATE);  
        return maxDate;  
    }
	public List fitLast30dayList(List list) {
		Map<String,Object> compareMap=new HashMap<String,Object>();
		for (Object object : list) {
			Map<String,Object> map=(Map<String, Object>) object;
			String key=map.get("day").toString();
			compareMap.put(key, map);
		}
		
		List<Map<String,Object>> resultList=new ArrayList<Map<String,Object>>();
		for(int i=30;i>=1;i--){
			Map<String,Object> map=DateUtil.getYearMonthAndDay(i);
			String month=map.get("month").toString();
			String day=map.get("day").toString();
			String key=month+"-"+day;
			if(!compareMap.containsKey(key)){
				Map<String,Object> beaddMap=new HashMap<String,Object>();
				beaddMap.put("t_num", 0);
				beaddMap.put("t_money", 0.0);
				beaddMap.put("day", key);
				resultList.add(beaddMap);
			}else{
				resultList.add((Map<String,Object>)compareMap.get(key));
			}
		}
		return resultList;
	}
	public List<Map<String, Object>> fitFlowList(List<Map<String, Object>> data,int year,int month,int cycle_type) {
		Map<String,Object> compareMap=new HashMap<String,Object>();
		List<Map<String,Object>> resultList=new ArrayList<Map<String,Object>>();
		for (Map<String, Object> map : data) {
			String key=map.get("day_num").toString();
			compareMap.put(key, map);
		}
		int count=0;
		if(cycle_type==1){//按月
			count=this.getDaysByYearMonth(year, month);
		}else{//按年
			count=12;
		}
		for(int i=1;i<=count;i++){
			if(!compareMap.containsKey(String.valueOf(i))){
				Map<String,Object> newMap=new HashMap<String,Object>();
				newMap.put("num", 0);
				newMap.put("day_num", i);
				resultList.add(newMap);
			}else{
				resultList.add((Map<String, Object>) compareMap.get(String.valueOf(i)));
			}
		}
		return resultList;
	}
	public static List<Map<String, Object>> fitTopGoodsFlowList(
			List<Map<String, Object>> list, int topNum) {
		// TODO Auto-generated method stub
		if(list.size()<topNum){
			int count=topNum-list.size();
			for (int i = 0; i < count; i++) {
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("num", 0);
				map.put("name", "");
				list.add(map);
			}
		}
		return list;
	}
}
