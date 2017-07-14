package com.enation.app.b2b2c.core.statistics.service.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.b2b2c.core.statistics.service.IB2b2cSalesStatisticsManager;
import com.enation.app.shop.core.order.model.Order;
import com.enation.app.shop.core.order.model.OrderLog;
import com.enation.app.shop.core.order.plugin.order.OrderPluginBundle;
import com.enation.app.shop.core.order.service.OrderStatus;
import com.enation.app.shop.core.statistics.model.DayAmount;
import com.enation.app.shop.core.statistics.model.MonthAmount;
import com.enation.eop.resource.model.AdminUser;
import com.enation.eop.sdk.context.EopSetting;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;

@Service
public class B2b2cSalesStatisticsManager implements IB2b2cSalesStatisticsManager{
	@Autowired
	private IDaoSupport daoSupport;
	@Autowired
	private OrderPluginBundle orderPluginBundle;

	public List<MonthAmount> statisticsMonth_Amount() {
		SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy");
		String year = sdfInput.format(new Date());
		String sql = "";
		if(EopSetting.DBTYPE.equals("1")){//是mysql
			sql = "select sum(a.order_amount) as amount, Date_format(FROM_UNIXTIME(a.create_time / 1000),'%Y-%m') as mo from es_order a where Date_format(FROM_UNIXTIME(a.create_time / 1000),'%Y') = ?  group by mo";
		}else if(EopSetting.DBTYPE.equals("3")){//是mssql
			sql = "select sum(a.order_amount) as amount, substring(convert(varchar(10),dateadd(ss,create_time/1000 + 28800,'1970-01-01'),120),1,7) as mo from es_order a where substring(convert(varchar(10),dateadd(ss,create_time/1000 + 28800,'1970-01-01'),120),1,4) = ?  group by substring(convert(varchar(10),dateadd(ss,create_time/1000 + 28800,'1970-01-01'),120),1,7)";
		}else{//是oracle
			//注意：需先在oracle中建立function
			/*代码如下：
			create or replace function FROM_UNIXTIME(mydate IN number) return date is
  				Result date;
			begin
  				Result := TO_DATE('01011970','mmddyyyy')+1/24/60/60*(MYDATE);
  				return(Result);
			end FROM_UNIXTIME;
			 */
			//			String createfunction = "create or replace function FROM_UNIXTIME(mydate IN number) return date is"
			//  				 +" Result date;"
			//  				 +" begin"
			//  				 +" Result := TO_DATE('01011970','mmddyyyy')+1/24/60/60*(MYDATE);"
			//  				 +" return(Result);"
			//  				 +" end FROM_UNIXTIME;";
			//			this.daoSupport.execute(createfunction);
			sql = "select sum(a.order_amount) as amount, to_char(TO_DATE('01011970','mmddyyyy')+1/24/60/60*(a.create_time / 1000),'yyyy-mm') as mo from es_order a where to_char(TO_DATE('01011970','mmddyyyy')+1/24/60/60*(a.create_time / 1000),'yyyy') = ?  group by to_char(TO_DATE('01011970','mmddyyyy')+1/24/60/60*(a.create_time / 1000),'yyyy-mm')";
		}
		List<Map> list = this.daoSupport.queryForList(sql, year);
		List<MonthAmount> target = new ArrayList<MonthAmount>();
		List<String> monthList = getMonthList();
		for(String month:monthList){
			MonthAmount ma = new MonthAmount();
			ma.setMonth(month);
			ma.setAmount(new Double(0));
			for(Map mapdata:list){
				if(mapdata.get("mo").equals(month)){
					ma.setAmount(Double.valueOf(mapdata.get("amount").toString()));
				}
			}
			target.add(ma);
		}
		return target;
	}


	public List<MonthAmount> statisticsMonth_Amount(String monthinput) {
		String year = monthinput.substring(0,4);
		String sql = "";
		if("1".equals(EopSetting.DBTYPE)){//是mysql
			sql = "select sum(a.order_amount) as amount, Date_format(FROM_UNIXTIME(a.create_time / 1000),'%Y-%m') as mo from es_order a where a.status = " + OrderStatus.ORDER_COMPLETE + " and Date_format(FROM_UNIXTIME(a.create_time / 1000),'%Y') = ?  group by mo";
		}else if("2".equals(EopSetting.DBTYPE)){//是oracle
			sql = "select sum(a.order_amount) as amount, to_char(TO_DATE('01011970','mmddyyyy')+1/24/60/60*(a.create_time / 1000),'yyyy-mm') as mo from es_order a where a.status = " + OrderStatus.ORDER_COMPLETE + " and to_char(TO_DATE('01011970','mmddyyyy')+1/24/60/60*(a.create_time / 1000),'yyyy') = ?  group by to_char(TO_DATE('01011970','mmddyyyy')+1/24/60/60*(a.create_time / 1000),'yyyy-mm')";
		}else if("3".equals(EopSetting.DBTYPE)){//SQLServer
			sql = "select sum(order_amount) as amount, substring(convert(varchar(10),dateadd(ss,create_time/1000 + 28800,'1970-01-01'),120),1,7) as mo from es_order where status = " + OrderStatus.ORDER_COMPLETE + " and substring(convert(varchar(10),dateadd(ss,create_time/1000 + 28800,'1970-01-01'),120),1,7) = ? group by substring(convert(varchar(10),dateadd(ss,create_time/1000 + 28800,'1970-01-01'),120),1,7)";
		}
		List<Map> list = this.daoSupport.queryForList(sql, year);
		List<MonthAmount> target = new ArrayList<MonthAmount>();
		List<String> monthList = getMonthList(monthinput);
		for(String month:monthList){
			MonthAmount ma = new MonthAmount();
			ma.setMonth(month);
			ma.setAmount(new Double(0));
			for(Map mapdata:list){
				if(mapdata.get("mo").equals(month)){
					ma.setAmount(Double.valueOf(mapdata.get("amount").toString()));
				}
			}
			target.add(ma);
		}
		return target;
	}


	public List<DayAmount> statisticsDay_Amount() {
		SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM");
		String year = sdfInput.format(new Date());
		String sql = "";
		if(EopSetting.DBTYPE.equals("1")){//是mysql
			sql = "select sum(a.order_amount) as amount, Date_format(FROM_UNIXTIME(a.create_time / 1000),'%Y-%m-%d') as mo from es_order a where a.status = " + OrderStatus.ORDER_COMPLETE + " and Date_format(FROM_UNIXTIME(a.create_time / 1000),'%Y-%m') = ?  group by mo";
		}else if(EopSetting.DBTYPE.equals("2")){
			sql = "select sum(a.order_amount) as amount, to_char(TO_DATE('01011970','mmddyyyy')+1/24/60/60*(a.create_time / 1000),'yyyy-mm-dd') as mo from es_order a where a.status = " + OrderStatus.ORDER_COMPLETE + " and to_char(TO_DATE('01011970','mmddyyyy')+1/24/60/60*(a.create_time / 1000),'yyyy-mm') = ?  group by to_char(TO_DATE('01011970','mmddyyyy')+1/24/60/60*(a.create_time / 1000),'yyyy-mm-dd')";
		}else{
			sql = "select sum(a.order_amount) as amount, substring(convert(varchar(10),dateadd(ss,create_time/1000 + 28800,'1970-01-01'),120),1,10) as mo from es_order a where a.status = " + OrderStatus.ORDER_COMPLETE + " and substring(convert(varchar(10),dateadd(ss,create_time/1000 + 28800,'1970-01-01'),120),1,7) = ?  group by substring(convert(varchar(10),dateadd(ss,create_time/1000 + 28800,'1970-01-01'),120),1,10)";
		}
		List<Map> list = this.daoSupport.queryForList(sql, year);
		List<DayAmount> target = new ArrayList<DayAmount>();
		List<String> dayList = getDayList();
		for(String day:dayList){
			DayAmount da = new DayAmount();
			da.setDay(day);
			da.setAmount(new Double(0));
			for(Map mapdata:list){
				if(mapdata.get("mo").equals(day)){
					da.setAmount(Double.valueOf(mapdata.get("amount").toString()));
				}
			}
			target.add(da);
		}
		return target;
	}


	public List<DayAmount> statisticsDay_Amount(String monthinput) {
		String sql = "";
		if("1".equals(EopSetting.DBTYPE)){//是mysql
			sql = "select sum(a.order_amount) as amount, Date_format(FROM_UNIXTIME(a.create_time / 1000),'%Y-%m-%d') as mo from es_order a where a.status = " + OrderStatus.ORDER_COMPLETE + " and Date_format(FROM_UNIXTIME(a.create_time / 1000),'%Y-%m') = ?   group by mo";
		}else if("2".equals(EopSetting.DBTYPE)){//Oracle
			sql = "select sum(a.order_amount) as amount, to_char(TO_DATE('01011970','mmddyyyy')+1/24/60/60*(a.create_time / 1000),'yyyy-mm-dd') as mo from es_order  a where a.status = " + OrderStatus.ORDER_COMPLETE + " and to_char(TO_DATE('01011970','mmddyyyy')+1/24/60/60*(a.create_time / 1000),'yyyy-mm') = ?   group by to_char(TO_DATE('01011970','mmddyyyy')+1/24/60/60*(a.create_time / 1000),'yyyy-mm-dd')";
		}else if("3".equals(EopSetting.DBTYPE)){//SQLServer
			sql = "select sum(order_amount) as amount, substring(convert(varchar(10),dateadd(ss,create_time/1000 + 28800,'1970-01-01'),120),1,10) as mo from es_order a where status = " + OrderStatus.ORDER_COMPLETE + " and substring(convert(varchar(10),dateadd(ss,create_time/1000 + 28800,'1970-01-01'),120),1,7) = ? group by substring(convert(varchar(10),dateadd(ss,create_time/1000 + 28800,'1970-01-01'),120),1,10)";
		}
		List<Map> list = this.daoSupport.queryForList(sql, monthinput);
		List<DayAmount> target = new ArrayList<DayAmount>();
		List<String> dayList = getDayList(monthinput);
		for(String day:dayList){
			DayAmount da = new DayAmount();
			da.setDay(day);
			da.setAmount(new Double(0));
			for(Map mapdata:list){
				if(mapdata.get("mo").equals(day)){
					da.setAmount(Double.valueOf(mapdata.get("amount").toString()));
				}
			}
			target.add(da);
		}
		return target;
	}


	private static List<String> getMonthList(){
		List<String> monthList = new ArrayList<String>();
		SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy");
		String year = sdfInput.format(new Date());
		DecimalFormat df = new DecimalFormat("00");
		for(int i=1;i<=12;i++){
			monthList.add(year + "-" + df.format(i));
		}
		return monthList;
	}

	private static List<String> getMonthList(String monthinput){
		List<String> monthList = new ArrayList<String>();
		String year = monthinput.substring(0,4);
		DecimalFormat df = new DecimalFormat("00");
		for(int i=1;i<=12;i++){
			monthList.add(year + "-" + df.format(i));
		}
		return monthList;
	}

	private static List<String> getDayList(){
		List<String> dayList = new ArrayList<String>();
		Date date = new Date();
		Calendar cal =Calendar.getInstance();
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM");
		String str_month = sdfInput.format(date);
		DecimalFormat df = new DecimalFormat("00");
		int count = days(year, month);
		for(int i=1;i<=count;i++){
			dayList.add(str_month + "-" + df.format(i));
		}
		return dayList;
	}

	private static List<String> getDayList(String monthinput){
		List<String> dayList = new ArrayList<String>();

		Date date =DateUtil.toDate(monthinput+"-01", "yyyy-MM-dd");// new Date(monthinput + "-01");

		Calendar cal =Calendar.getInstance();
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		String str_month = monthinput;
		DecimalFormat df = new DecimalFormat("00");
		int count = days(year, month);
		for(int i=1;i<=count;i++){
			dayList.add(str_month + "-" + df.format(i));
		}
		return dayList;
	}

	public static int days(int year,int month){
		int days = 0;
		if(month!=2){
			switch(month){
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:days = 31 ;break;
			case 4:
			case 6:
			case 9:
			case 11:days = 30;

			}
		}else{
			if(year%4==0 && year%100!=0 || year%400==0)
				days = 29;
			else  
				days = 28;
		}
		return days;
	}


	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.core.service.ISalesStatisticsManager#orderStatByPayment()
	 */
	public List<Map> orderStatByPayment(){
		String sql ="select count(0) num,sum(order_amount) amount,max(payment_name) payment_name from es_order where disabled=0 group by shipping_id";
		return this.daoSupport.queryForList(sql);
	}



	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.core.service.ISalesStatisticsManager#orderStatByShip()
	 */
	public List<Map> orderStatByShip(){
		String sql ="select count(0) num,sum(order_amount) amount,max(shipping_type) shipping_type from es_order where disabled=0 group by shipping_id";
		return this.daoSupport.queryForList(sql);
	}

	/**
	 * 订单统计   下单量 按年的统计
	 * @author LSJ
	 * @param order_status 订单状态
	 * @param year 年
	 * @param store_id 店铺ID
	 * @return 下单量 list
	 * 2016年12月6日下午15:16
	 */
	@Override
	public List<Map> statisticsYear_Amount(Integer status, int year,String store_id) {
		String condition_sql = createSqlByYear(1, year+"");
		String sql =  "select count(0) as t_num,SUM(need_pay_money) as t_money, case "+ condition_sql +" as month  from es_order o where 1=1 ";
		String storeWhere="";
		if(store_id != null && !"0".equals(store_id)){
			storeWhere = " AND o.store_id = "+store_id;
		}
		if( status!=null && status.intValue()!=0 && status.intValue()!=99 ){
			sql += " and o.status="+status;
		}
		sql+=" and o.parent_id is not null";
		sql += storeWhere;

		sql +=  " group by case "+condition_sql;
		List list = this.daoSupport.queryForList(sql);
		return list;
	}

	/**
	 * 订单统计   下单量 按月的统计
	 * @author LSJ
	 * @param order_status 订单状态
	 * @param year 年
	 * @param month 月
	 * @param store_id 店铺ID
	 * @return 下单量list
	 * 2016年12月6日下午15:16
	 */
	@Override
	public List<Map> statisticsMonth_Amount(Integer status, int year,int month,String store_id) {
		String condition_sql = createSql(1, year,month);
		String sql =  "select count(0) as t_num,SUM(need_pay_money) as t_money, case "+ condition_sql +" as month  from es_order o where 1=1";
		String storeWhere="";
		if(store_id != null && !"0".equals(store_id)){
			storeWhere = " AND o.store_id = "+store_id;
		}
		if( status!=null && status.intValue()!=0 && status.intValue()!=99 ){
			sql += " and o.status="+status;
		}
		sql+= " and o.parent_id is not null ";
		sql += storeWhere;
		sql+= " group by case "+condition_sql;
		List list = this.daoSupport.queryForList(sql);
		return list;
	}

	/**
	 * 热卖商品排行—下单金额
	 * @author LSJ
	 * @return 热卖商品排行—下单金额list
	 * 2016年12月6日下午15:16
	 */
	@Override
	public List<Map> hotGoodsTop_Money() {
		StringBuffer sql = new StringBuffer();
		sql.append("select sum(i.price*i.num) as t_price,i.`name`,c.`name` from es_order_items i left join es_order o on i.order_id=o.order_id left join es_goods_cat c on c.cat_id = i.cat_id ");
		sql.append("");
		sql.append(" GROUP BY i.goods_id  ORDER BY t_price DESC LIMIT 0,50");
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.core.service.IStatisticsManager#hotGoodsTop_Num()
	 */
	@Override
	public List<Map> hotGoodsTop_Num() {
		return null;
	}



	/**
	 * 创建SQL语句
	 * @param type 1.按照月查询(查询出此月每天的下单金额)
	 * @param date
	 */
	public static String createSql(int type,int year,int month){
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
	 * 创建SQL语句
	 * @param type 1.按照年查询(查询出此年每月的下单金额)
	 * @param date
	 */
	public static String createSqlByYear(int type,String date){
		StringBuffer sql =new StringBuffer();
		for(int i=1;i<=12;i++){
			String day = "0"+i;
			day = day.substring( day.length()-2,day.length());
			String day_date = date+"-"+day;
			long start = DateUtil.getDateline(day_date+"-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
			long end = DateUtil.getDateline(day_date+"-31 23:59:59", "yyyy-MM-dd HH:mm:ss");
			sql.append(" when create_time >= "+start +" and  create_time <="+ end +" then "+i );
		}
		sql.append(" else 0 end");
		return sql.toString();
	}

	/**
	 * 销售收入统计json数据
	 * @author LSJ
	 * @param year 年
	 * @param month 月
	 * @param page 当前页数
	 * @param pageSize 分页大小
	 * @param map
	 * @param store_id 店铺ID
	 * @return 销售收入统计json数据分页
	 * 2016年12月6日下午15:16
	 */
	@Override
	public Page getSalesIncome(int year, int month, int page, int pageSize,
			Map map,String store_id) {

		String  date = year+"-"+month;
		long start = DateUtil.getDateline(date+"-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
		long end = DateUtil.getDateline(date+"-31 23:59:59", "yyyy-MM-dd HH:mm:ss");

		String storeWhere = "";
		if(store_id != null && !"0".equals(store_id)){
			storeWhere =" AND o.store_id ="+store_id;
		}

		String sql = "select oi.goods_id,oi.name,oi.price,SUM(oi.num) t_num,SUM(oi.num*oi.price) t_price from es_order_items oi "
				+ " left join es_order o on oi.order_id=o.order_id "
				+ " where o.create_time >=? and  o.create_time <=? and ship_num >0 "
				+storeWhere
				+ " group by oi.goods_id,oi.name,oi.price ";

		String db_type = EopSetting.DBTYPE;
		if(db_type.equals("3")){
			sql += " order by oi.goods_id desc";
		}
		List list = this.daoSupport.queryForListPage(sql, page, pageSize, start, end );

		Page salesPage= new Page(0, daoSupport.queryForList(sql, start, end).size(), pageSize, list);

		return salesPage;
	}


	/**
	 * 销售收入统计总览表
	 * @author LSJ
	 * @param year 年
	 * @param month 月
	 * @param parames
	 * @param store_id 店铺ID
	 * @return 收款金额
	 * 2016年12月6日下午15:16
	 */
	@Override
	public Double getReceivables(int year, int month, Map parames,String store_id) {

		String  date = year+"-"+month;
		long start = DateUtil.getDateline(date+"-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
		long end = DateUtil.getDateline(date+"-31 23:59:59", "yyyy-MM-dd HH:mm:ss");

		String storeWhere="";
		if(store_id != null && !"0".equals(store_id)){
			storeWhere = " AND o.store_id = " + store_id;
		}

		String sql = "select SUM(o.need_pay_money) as receivables from es_order o where create_time >=? and  create_time <=?";
		sql+=storeWhere;
		//String sql = "select SUM(pl.money) as receivables from es_payment_logs pl where pl.pay_date >=? and pl.pay_date <=?";
		Map map = this.daoSupport.queryForMap(sql, start,end);
		Double receivables = 0.0;
		if(map!=null){
//			receivables = Double.parseDouble(map.get("receivables").toString());
			receivables = StringUtil.toDouble(map.get("receivables"), false);
		}
		return receivables;
	}

	/**
	 * 销售收入统计总览表
	 * @author LSJ
	 * @param year 年
	 * @param month 月
	 * @param parames
	 * @param store_id 店铺ID
	 * @return 退款金额
	 * 2016年12月6日下午15:16
	 */
	@Override
	public Double getRefund(int year, int month, Map parames,String store_id) {

		String  date = year+"-"+month;
		long start = DateUtil.getDateline(date+"-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
		long end = DateUtil.getDateline(date+"-31 23:59:59", "yyyy-MM-dd HH:mm:ss");
		String storeWhere="";
		if(store_id != null && !"0".equals(store_id)){
			storeWhere= " AND sl.store_id = "+store_id;
		}

		String sql = "select SUM(sl.alltotal_pay) as refund from es_sellback_list sl  where sl.regtime >=? and sl.regtime <=?";
		sql+=storeWhere;
		Map map = this.daoSupport.queryForMap(sql, start,end);
		Double refund = 0.0;
		if(map!=null){
//			refund = Double.parseDouble(map.get("refund").toString());
			refund = StringUtil.toDouble(map.get("refund"), false);
		}
		return refund;
	}


	//获取当前年月的最大的天数
	public static int getDaysByYearMonth(int year, int month) {  
		Calendar a = Calendar.getInstance();  
		a.set(Calendar.YEAR, year);  
		a.set(Calendar.MONTH, month - 1);  
		a.set(Calendar.DATE, 1);  
		a.roll(Calendar.DATE, -1);  
		int maxDate = a.get(Calendar.DATE);  
		return maxDate;  
	}

	/**
	 * @author LSJ
	 * @param orderMap
	 * @param page 当前页数
	 * @param pageSize 分页大小
	 * @param sort 排序
	 * @param order
	 * @return
	 * 2016年12月6日下午15:16
	 */
	@Transactional(propagation = Propagation.REQUIRED) 
	@Override
	public Page listOrder(Map map, int page, int pageSize, String other, String order) {
		this.cancelOrder();
		String sql = createTempSql(map, other,order);
		Page webPage = this.daoSupport.queryForPage(sql, page, pageSize);
		orderPluginBundle.filterOrderPage(webPage);
		return webPage;
	}


	/**
	 * 检查订单是否过期，若已过期，将其状态置为取消  
	 * 添加人：DMRain 2015-12-08
	 */
	private void cancelOrder(){
		String sql = "select * from es_order where status = 0 and pay_status = 0 and ship_status = 2 and payment_type != 'cod'";
		List<Order> list = this.daoSupport.queryForList(sql, Order.class);

		if(list != null){
			for(Order order : list){
				long createTime = order.getCreate_time();
				long nowTime = DateUtil.getDateline();
				if((nowTime - createTime) > 259200){
					Integer order_id = order.getOrder_id();
					this.daoSupport.execute("update es_order set status = 8,cancel_reason = '订单过期，系统自动将其取消' where order_id = ?", order_id);

					//响应订单取消事件 add by DMRain 2016-4-22
					this.orderPluginBundle.onCanel(this.get(order_id));

					//添加订单日志
					this.addLog(order_id, "订单过期，系统自动将其取消");
				}
			}
		}
	}
	public void addLog(Integer order_id,String message){

		//获取当前后台管理员
		AdminUser adminUser = UserConext.getCurrentAdminUser();
		OrderLog orderLog = new OrderLog();
		orderLog.setMessage(message);
		if(adminUser==null){
			orderLog.setOp_id(0);
			orderLog.setOp_name("系统检测");
		}else{
			orderLog.setOp_id(0);
			orderLog.setOp_name(adminUser.getUsername());
		}
		orderLog.setOp_time(DateUtil.getDateline());
		orderLog.setOrder_id(order_id);
		this.daoSupport.insert("es_order_log", orderLog);
	}
	public Order get(Integer orderId) {
		String sql = "select * from es_order where order_id=?";
		Order order = (Order) this.daoSupport.queryForObject(sql,
				Order.class, orderId);
		return order;
	}
	
	/**
	 * 
	 * @param map
	 * @param other
	 * @param order
	 * @return
	 */
	private String  createTempSql(Map map,String other,String order){
		
		Integer stype = (Integer) map.get("stype");
		String keyword = (String) map.get("keyword");
		String orderstate =  (String) map.get("order_state");//订单状态
		String start_time = (String) map.get("start_time");
		String end_time = (String) map.get("end_time");
		Integer status = (Integer) map.get("status");
		String sn = (String) map.get("sn");
		String ship_name = (String) map.get("ship_name");
		Integer paystatus = (Integer) map.get("paystatus");
		Integer shipstatus = (Integer) map.get("shipstatus");
		Integer shipping_type = (Integer) map.get("shipping_type");
		Integer payment_id = (Integer) map.get("payment_id");
		Integer depotid = (Integer) map.get("depotid");
		String complete = (String) map.get("complete");
		String storeWhere=(String)map.get("storeWhere");
		
		StringBuffer sql =new StringBuffer();
		sql.append("select o.*, m.uname from es_order o left join es_member m on o.member_id = m.member_id where o.disabled=0");
		
		if(stype!=null && keyword!=null){			
			if(stype==0){
				sql.append(" and (o.sn like '%"+keyword+"%'");
				sql.append(" or o.ship_name like '%"+keyword+"%')");
			}
		}
		
		if(status!=null && status!=99){
			sql.append(" and o.status="+status);
		}/*else{
			sql.append(" and o.status!=8");
		}*/
		
		if(!StringUtil.isEmpty(storeWhere)){
			sql.append(storeWhere);
		}
		
		if(sn!=null && !StringUtil.isEmpty(sn)){
			sql.append(" and o.sn like '%"+sn+"%'");
		}
		
		if(ship_name!=null && !StringUtil.isEmpty(ship_name)){
			sql.append(" and o.ship_name like '"+ship_name+"'");
		}
		
		if(paystatus!=null){
			sql.append(" and o.pay_status="+paystatus);
		}
		
		if(shipstatus!=null){
			sql.append(" and o.ship_status="+shipstatus);
		}
		
		if(shipping_type!=null){
			sql.append(" and o.shipping_id="+shipping_type);
		}
		
		if(payment_id!=null){
			sql.append(" and o.payment_id="+payment_id);
		}
		
		if (depotid!=null && depotid > 0) {
			sql.append(" and o.depotid=" + depotid);
		}
		
		if(start_time!=null&&!StringUtil.isEmpty(start_time)){			
			long stime = com.enation.framework.util.DateUtil.getDateline(start_time+" 00:00:00","yyyy-MM-dd HH:mm:ss");
			sql.append(" and o.create_time>"+stime);
		}
		if(end_time!=null&&!StringUtil.isEmpty(end_time)){			
			long etime = com.enation.framework.util.DateUtil.getDateline(end_time +" 23:59:59", "yyyy-MM-dd HH:mm:ss");
			sql.append(" and o.create_time<"+etime);
		}
		if( !StringUtil.isEmpty(orderstate)){
			if(orderstate.equals("wait_ship") ){ //对待发货的处理
				sql.append(" and ( ( payment_type!='cod'  and  status="+OrderStatus.ORDER_PAY +") ");//非货到付款的，要已结算才能发货
				sql.append(" or ( payment_type='cod' and  status="+OrderStatus.ORDER_CONFIRM +")) ");//货到付款的，已确认就可以发货
			}else if(orderstate.equals("wait_pay") ){
				sql.append(" and ( ( payment_type!='cod' and  status="+OrderStatus.ORDER_CONFIRM +") ");//非货到付款的，未付款状态的可以结算
				sql.append(" or ( payment_type='cod' and   status="+OrderStatus.ORDER_ROG+"  ) )");//货到付款的要发货或收货后才能结算
			}else if(orderstate.equals("wait_rog") ){ 
				sql.append(" and o.status="+OrderStatus.ORDER_SHIP  ); 
			}else{
				sql.append(" and o.status="+orderstate);
			}
		
		
		}
		
		if(!StringUtil.isEmpty(complete)){
			sql.append(" and o.status="+OrderStatus.ORDER_COMPLETE);
		}
		sql.append(" and o.parent_id is not null ");
		sql.append(" ORDER BY o."+other+" "+order);
		
//		System.out.println(sql.toString());
		return sql.toString();
	}
}
