package com.enation.app.b2b2c.core.store.service.statistics.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.noggit.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.b2b2c.core.store.service.statistics.IB2b2cOperatorStatisticsManager;
import com.enation.app.b2b2c.core.store.service.statistics.StatisticsUtil;
import com.enation.app.shop.core.order.model.Order;
import com.enation.app.shop.core.order.service.OrderStatus;
import com.enation.eop.sdk.context.EopSetting;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.ExcelUtil;
import com.enation.framework.util.FileUtil;
import com.enation.framework.util.JsonResultUtil;
/**
 * 多店商家中心-统计-运营报告管理实现类
 * @author jianghongyan 2016年7月1日 版本改造
 * @version v6.1
 * @since v6.1
 */
@SuppressWarnings({"rawtypes","unchecked"})
@Service("b2b2cOperatorStatisticsManager")
public class B2b2cOperatorStatisticsManager implements IB2b2cOperatorStatisticsManager {

	@Autowired
	private IDaoSupport daoSupport;
	
	/**
	 * 获取销售统计 下单金额highchart-json格式数据集
	 * @param year 查询年份
	 * @param month 查询月份
	 * @param cycle_type 查询周期
	 * @param storeid 店铺id
	 * @return json格式数据集
	 */
	@Override
	public Object getSalesMoney(Integer year, Integer month, Integer cycle_type, Integer storeid) {
		if(cycle_type==null){//非空校验 如果周期为空则设为1,即按月查询
			cycle_type=1;
		}
		/**
		 * start: 拼接sql语句并查询当前周期数据
		 */
		String condition_sql=StatisticsUtil.getInstance().createSql(cycle_type,year,month);
		String sql="select count(0) as t_num,SUM(need_pay_money) as t_money, case "+condition_sql+" as month  from es_order o where 1=1 AND status>="+OrderStatus.ORDER_PAY+" AND status!="+OrderStatus.ORDER_CANCELLATION+" AND pay_status="+OrderStatus.PAY_YES;
		if(storeid!=null&&storeid!=0){
			sql+=" and o.store_id="+storeid;
		}
		sql+= " group by case "+condition_sql;
		List selflist = this.daoSupport.queryForList(sql);
		String selfmessage = "["+getMessage(cycle_type, "t_money", selflist,year,month);
		selfmessage=selfmessage.substring(0, selfmessage.length()-1)+"]";
		/**
		 * end:查询当前周期数据结束
		 */
		
		/**
		 * start:拼接sql并查询上一周期数据
		 */
		if(cycle_type==1){
			month=month-1;
		}else{
			year=year-1;
		}
		condition_sql=StatisticsUtil.getInstance().createSql(cycle_type,year,month);
		sql="select count(0) as t_num,SUM(need_pay_money) as t_money, case "+condition_sql+" as month  from es_order o where 1=1 AND status>="+OrderStatus.ORDER_PAY+" AND status!="+OrderStatus.ORDER_CANCELLATION+"  AND pay_status="+OrderStatus.PAY_YES;
		if(storeid!=null&&storeid!=0){
			sql+=" and o.store_id="+storeid;
		}
		sql+= " group by case "+condition_sql;
		List lastlist = this.daoSupport.queryForList(sql);
		String lastmessage = "["+getMessage(cycle_type, "t_money", lastlist,year,month);
		lastmessage=lastmessage.substring(0, lastmessage.length()-1)+"]";
		/**
		 * end:查询上一周期数据结束
		 */
		return "{\"result\":1,\"message\":{\"lastmessage\":"+lastmessage+",\"selfmessage\":"+selfmessage+"}}";
	}

	/**
	 * 获取销售统计  下单量highchart-json格式数据集
	 * @param year 查询年份
	 * @param month 查询月份
	 * @param cycle_type 查询周期
	 * @param storeid 店铺id
	 * @return json格式数据集
	 */
	@Override
	public Object getSalesNum(Integer year, Integer month,
			Integer cycle_type, Integer storeid) {
		if(cycle_type==null){
			cycle_type=1;
		}
		/**
		 * start:开始拼接sql并查询当前周期数据
		 */
		String condition_sql=StatisticsUtil.getInstance().createSql(cycle_type,year,month);
		String sql="select count(0) as t_num,SUM(need_pay_money) as t_money, case "+condition_sql+" as month  from es_order o where 1=1 AND status>="+OrderStatus.ORDER_PAY+" AND status!="+OrderStatus.ORDER_CANCELLATION+" AND pay_status="+OrderStatus.PAY_YES;
		if(storeid!=null&&storeid!=0){
			sql+=" and o.store_id="+storeid;
		}
		sql+= " group by case "+condition_sql;
		List selflist = this.daoSupport.queryForList(sql);
		String selfmessage = "["+getMessage(cycle_type, "t_num", selflist,year,month);
		selfmessage=selfmessage.substring(0, selfmessage.length()-1)+"]";
		/**
		 * end:当前周期数据查询结束
		 */
		
		/**
		 * start:开始拼接sql并查询上一周期数据
		 */
		if(cycle_type==1){
			month=month-1;
		}else{
			year=year-1;
		}
		condition_sql=StatisticsUtil.getInstance().createSql(cycle_type,year,month);
		sql="select count(0) as t_num,SUM(need_pay_money) as t_money, case "+condition_sql+" as month  from es_order o where 1=1 AND status>="+OrderStatus.ORDER_PAY+" AND status!="+OrderStatus.ORDER_CANCELLATION+" AND pay_status="+OrderStatus.PAY_YES;
		if(storeid!=null&&storeid!=0){
			sql+=" and o.store_id="+storeid;
		}
		sql+= " group by case "+condition_sql;
		List lastlist = this.daoSupport.queryForList(sql);
		String lastmessage = "["+getMessage(cycle_type, "t_num", lastlist,year,month);
		lastmessage=lastmessage.substring(0, lastmessage.length()-1)+"]";
		/**
		 * end:上一周期数据查询结束
		 */
		return "{\"result\":1,\"message\":{\"lastmessage\":"+lastmessage+",\"selfmessage\":"+selfmessage+"}}";
	}

	
	/**
	 * 判断周期模式（按年或者按月），并返回相应的字串
	 * @author xulipeng
	 * @param cycle_type 	周期模式
	 * @param param		t_num：总订单数，t_money：总金额
	 * @param list	数据集合
	 * @return
	 */
	private String getMessage(int cycle_type,String param,List<Map> list,Integer year,Integer month){
		int num = 0;
		if(cycle_type==1){
			num=StatisticsUtil.getInstance().getDaysByYearMonth(year, month);
		}else{
			num=12;
		}

		String message = "";
		for (int i = 1; i <= num; i++) {
			boolean flag = true;
			for (int j =0;j<list.size();j++) {
				Map map = list.get(j);
				if(!map.get("month").toString().equals("0") && i==Integer.parseInt(map.get("month").toString())){
					message = message+map.get(param).toString()+",";
					flag = false;
				}
			}
			if(flag){
				message = message+"0,";
			}
		}
		return message;
	}

	/**
	 * 获取区域分析 highchart-json格式数据集
	 * @param type 查询类型 1:下单会员数 2.会员下单金额 3.会员下单量
	 * @param cycle_type 查询周期
	 * @param year 查询年份
	 * @param month 查询月份
	 * @param storeid 店铺id
	 * @return json格式数据集
	 */
	@Override
	public String getRegionStatistics(int type, Integer cycle_type,
			Integer year, Integer month, Integer storeid) {
		// 新建地区计划
				Map<String, String> myMap = new HashMap<String, String>(40);

				// 获取所有地区
				myMap = this.getReagionMap(myMap);

				// 返回数据列表
				List list = new ArrayList();

				// 地区统计JSON
				StringBuffer sb = new StringBuffer("[");

				// type 1.下单会员数、2.下单金额、3.下单量
				switch (type) {
					case 1:
						list = getRegionOrderMemberNum(cycle_type,year,month,storeid);
						break;
					case 2:
						list = this.getRegionOrderPrice(cycle_type,year,month,storeid);
						break;
					case 3:
						list = this.getRegionOrderNum(cycle_type,year,month,storeid);
						break;
					default:
						list = getRegionOrderMemberNum(cycle_type,year,month,storeid);
						break;
				}

				String value = "";
				int num=1;
				// 循环地区值
				for (Object o : myMap.keySet()) {

					// 获取地区下单数量
					value = this.getRegionNum(list, o.toString());

					// 拼接JSON
					sb.append("{\"hc-key\":\"").append(myMap.get(o)).append("\",\"value\":").append(value).append("}");
					if(myMap.keySet().size()!=num){
						sb.append(",").append("\n");
					}
					num+=1;

				}
				List list2=new ArrayList();
				for (Object object : list) {
					Map<String,Object> map=(Map<String, Object>) object;
					Double mapvalue=Double.valueOf(map.get("num").toString());
					if(mapvalue!=0){
						list2.add(object);
					}
				}
				return "{\"message\":"+ sb.append("]").toString()+",\"highchartmessage\":"+JSONUtil.toJSON(list2)+"}";
	}
	
	
	
	/**
	 * 查询区域会员下单数量
	 * @param storeid 
	 * 
	 * @param myMap 地区集合
	 * @return 区域会员下单数量 JSON
	 */
	private List getRegionOrderMemberNum(Integer cycle_type,Integer year,Integer month, Integer storeid) {
		// 拼装 区域会员下单数量SQL
		StringBuffer sql = new StringBuffer("select s.local_name,(SELECT COUNT(member_id) from es_member ");
		sql.append("WHERE disabled!=1 and member_id IN(SELECT o.member_id FROM es_order o WHERE o.ship_provinceid=s.region_id  AND status>="+OrderStatus.ORDER_PAY+" AND status!="+OrderStatus.ORDER_CANCELLATION+" AND pay_status="+OrderStatus.PAY_YES);
		
		//判断周期模式 1为月
		if(cycle_type==1){
			sql.append(" AND o.complete_time<"+StatisticsUtil.getInstance().getMaxvalType1(year,month)+" AND o.complete_time > "+StatisticsUtil.getInstance().getMinvalType1(year, month));
		}else{
			//如果周日为年
			sql.append(" AND o.complete_time<"+StatisticsUtil.getInstance().getMaxvalType0(year)+" AND o.complete_time > "+StatisticsUtil.getInstance().getMinvalType0(year));
		}
		sql.append(" AND o.store_id="+storeid );
		sql.append(" ) ) num ");
		sql.append("FROM es_regions s WHERE s.p_region_id=0 ");
		// 获取所有地区的下单量的统计值
		return daoSupport.queryForList(sql.toString());

	}

	/**
	 * 查看区域下单数量
	 * @param storeid 
	 * 
	 * @param myMap
	 *            地区集合
	 * @return 区域下单数量 JSON
	 */
	private List getRegionOrderNum(Integer cycle_type,Integer year,Integer month, Integer storeid) {
		StringBuffer sql = new StringBuffer("select s.local_name,(SELECT COUNT(o.order_id) FROM es_order o WHERE o.ship_provinceid=s.region_id  AND status>="+OrderStatus.ORDER_PAY+" AND status!="+OrderStatus.ORDER_CANCELLATION+" AND pay_status="+OrderStatus.PAY_YES);
		
		//判断周期模式 1为月
		if(cycle_type==1){
			sql.append(" AND o.complete_time<"+StatisticsUtil.getInstance().getMaxvalType1(year,month)+" AND o.complete_time > "+StatisticsUtil.getInstance().getMinvalType1(year, month));
		}else{
			//如果周期为年
			sql.append(" AND o.complete_time<"+StatisticsUtil.getInstance().getMaxvalType0(year)+" AND o.complete_time > "+StatisticsUtil.getInstance().getMinvalType0(year));
		}
		sql.append(" AND o.store_id="+storeid );
		sql.append(" ) num FROM es_regions s WHERE s.p_region_id=0");
		
		// 获取所有地区的下单量的统计值
		return daoSupport.queryForList(sql.toString());
	}

	/**
	 * 获取区域下单价格
	 * @param storeid 
	 * 
	 * @param 地区集合
	 * @return 区域下单金额 JSON
	 */
	private List getRegionOrderPrice(Integer cycle_type,Integer year,Integer month, Integer storeid) {
		StringBuffer sql = new StringBuffer("select s.local_name,(SELECT SUM(o.paymoney) from es_order o WHERE o.ship_provinceid=s.region_id  AND status>="+OrderStatus.ORDER_PAY+" AND status!="+OrderStatus.ORDER_CANCELLATION+" AND pay_status="+OrderStatus.PAY_YES);
		//判断周期模式 1为月
		if(cycle_type==1){
			sql.append(" AND o.complete_time<"+StatisticsUtil.getInstance().getMaxvalType1(year,month)+" AND o.complete_time > "+StatisticsUtil.getInstance().getMinvalType1(year, month));
		}else{
			//如果周日为年
			sql.append(" AND o.complete_time<"+StatisticsUtil.getInstance().getMaxvalType0(year)+" AND o.complete_time > "+StatisticsUtil.getInstance().getMinvalType0(year));
		}
		sql.append(" AND o.store_id="+storeid );
		sql.append(") num FROM es_regions s WHERE s.p_region_id=0 ");
		// 获取所有地区的下单量的统计值
		return daoSupport.queryForList(sql.toString());
	}

	/**
	 * 获取下单地区的订单数量
	 * @param list 地区列表，包含数量
	 * @param local_name 地区名称
	 * @return 下单地区的订单数量
	 */
	private String getRegionNum(List<Map> list, String local_name) {
		String num = "0";
		for (Map map : list) {
			if (map.get("num") != null) {
				if (map.get("local_name").toString().equals(local_name)) {
					num = map.get("num").toString();
					break;
				}
			}
		}
		return num;
	}
	/**
	 * 获取所有地区集合
	 * @param myMap 地区集合
	 * @return 地区集合
	 */
	
	private Map<String,String> getReagionMap(Map<String,String> myMap) {
		myMap.put("吉林", "cn-jl");
		myMap.put("天津", "cn-tj");
		myMap.put("安徽", "cn-ah");
		myMap.put("山东", "cn-sd");
		myMap.put("山西", "cn-sx");
		myMap.put("新疆", "cn-xj");
		myMap.put("河北", "cn-hb");
		myMap.put("河南", "cn-he");
		myMap.put("湖南", "cn-hn");
		myMap.put("甘肃", "cn-gs");
		myMap.put("福建", "cn-fj");
		myMap.put("贵州", "cn-gz");
		myMap.put("重庆", "cn-cq");
		myMap.put("江苏", "cn-js");
		myMap.put("湖北", "cn-hu");
		myMap.put("内蒙古", "cn-nm");
		myMap.put("广西", "cn-gx");
		myMap.put("黑龙江", "cn-hl");
		myMap.put("云南", "cn-yn");
		myMap.put("辽宁", "cn-ln");
		myMap.put("香港", "cn-6668");
		myMap.put("浙江", "cn-zj");
		myMap.put("上海", "cn-sh");
		myMap.put("北京", "cn-bj");
		myMap.put("广东", "cn-gd");
		myMap.put("澳门", "cn-3681");
		myMap.put("西藏", "cn-xz");
		myMap.put("陕西", "cn-sa");
		myMap.put("四川", "cn-sc");
		myMap.put("海南", "cn-ha");
		myMap.put("宁夏", "cn-nx");
		myMap.put("青海", "cn-qh");
		myMap.put("江西", "cn-jx");
		myMap.put("台湾", "tw-tw");
		myMap.put("南沙群岛", "cn-3664");
		return myMap;
	}

	/**
	 * 获取销售分析  下单金额 datagrid-json格式数据
	 * @param year 查询年份
	 * @param month 查询月份
	 * @param cycle_type 查询周期
	 * @param storeid 店铺id
	 * @param pageNo 查询页码
	 * @param pageSize 分页数据长度
	 * @return json格式数据集
	 */
	@Override
	public GridJsonResult getSalesMoneyDgjson(Integer year, Integer month,
			Integer cycle_type, Integer storeid,int pageNo,int pageSize) {
		StringBuffer sbsql=new StringBuffer();
		sbsql.append("select o.sn as ordersn,m.name as ordermember,o.create_time as createtime,o.need_pay_money as needpaymoney,o.status as orderstatus  from es_order o left join es_member m on o.member_id=m.member_id where 1=1  AND status>="+OrderStatus.ORDER_PAY+" AND status!="+OrderStatus.ORDER_CANCELLATION+" AND pay_status="+OrderStatus.PAY_YES);
		
		if(cycle_type==null){
			cycle_type=1;
		}
		Calendar calendar = Calendar.getInstance();
		if(year==null){
			year=calendar.get(Calendar.YEAR);
		}
		if(month==null){
			month = calendar.get(Calendar.MONTH) + 1;
		}
		long times[]=StatisticsUtil.getInstance().getStartTimeAndEndTime(cycle_type, year, month);
		long starttime=times[0];
		long endtime=times[1];
		sbsql.append(" and  o.create_time > ? ");
		sbsql.append(" and  o.create_time < ? ");
		sbsql.append(" and o.store_id=?");
		sbsql.append(" order by order_id desc ");
		String sql=sbsql.toString();
		List list= this.daoSupport.queryForListPage(sql, pageNo, pageSize, starttime,endtime,storeid);
		List list2=this.daoSupport.queryForList(sql,  starttime,endtime,storeid);
		
		
		int totalSize=list2.size();
		Page webPage = new Page(pageNo, totalSize, pageSize, list);
		
		//进行json拼接
		return JsonResultUtil.getGridJson(webPage);
	}

	/**
	 * 获取购买分析 客单价分析  购买时段分析  highchart-json格式数据集
	 * @param cycle_type 查询周期
	 * @param year 查询年份
	 * @param month 查询月份
	 * @param storeid 店铺id
	 * @return json格式数据集
	 */
	@Override
	public Object getBuyStatistics(Integer cycle_type, Integer year,
			Integer month, Integer storeid) {
		//获取购买分析的数据
		
		long times[]=StatisticsUtil.getInstance().getStartTimeAndEndTime(cycle_type, year, month);
		long starttime=times[0];
		long endtime=times[1];
		
		HttpServletRequest request=ThreadContextHolder.getHttpRequest();
		String[] sectionsstr= request.getParameterValues("sections");
		List<Integer> sectionsList=new ArrayList<Integer>();
		for (String string : sectionsstr) {
			sectionsList.add(Integer.valueOf(string));
		}
		//1.获取客单分析数据
		List pricelist=this.getOrderPriceDis(sectionsList, String.valueOf(starttime), String.valueOf(endtime),storeid);
		//2.获取购买时段分析数据
		List timelist=this.getBuyTimeDis(String.valueOf(starttime), String.valueOf(endtime),storeid);
		
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("price",pricelist);
		map.put("time", timelist);
		return JsonResultUtil.getObjectJson(map);
	}

	/**
	 * 获取购买分析  客单价分布 highchart-json格式数据集
	 * @param sections 价格区间
	 * @param startDate 起始时间
	 * @param endDate 结束时间
	 * @param storeid 店铺id
	 * @return json格式数据集
	 */
	@Override
	public List<Map<String, Object>> getOrderPriceDis(List<Integer> sections,
			String startDate, String endDate, Integer storeid) {
		
		// 如果没有区间数据
		if (sections == null || sections.size() == 0) {
			
			throw new RuntimeException("区间不能为空");
		}
		
		
		String dateWhere = "";		// 时间条件
		List<Integer> list = new ArrayList<Integer>();	//筛选后的区间值
		
		// 如果包含开始时间条件
		if (startDate != null && !"".equals(startDate)) {
			
			dateWhere += "AND o.create_time >= " + startDate;
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
		String caseStatement = getOrderPriceDisSqlCaseStatement(list);
		String sql = "SELECT count(o.order_id) AS num, " + caseStatement
				+ " AS elt_data,o.need_pay_money FROM es_order o "
				+ "WHERE 1=1  And o.store_id="+storeid+"  AND status>="+OrderStatus.ORDER_PAY+" AND status!="+OrderStatus.ORDER_CANCELLATION+" AND pay_status="+OrderStatus.PAY_YES+" "
				+ dateWhere
				+ " GROUP BY o.need_pay_money ";
		
		String mainSql = "SELECT SUM(t1.num) num, t1.elt_data, SUM(t1.need_pay_money) need_pay_money FROM(" + sql + ") t1 "
				+ "GROUP BY t1.elt_data ORDER BY t1.elt_data";

		List<Map<String, Object>> data = this.daoSupport.queryForList(mainSql);
		data=StatisticsUtil.getInstance().fitOrderPriceData(data,list);
		return data;
	}
	
	
	
	/**
	 * 生成价格销量统计的SQL CASE语句
	 * @param ranges 整数数组
	 * @return SQL CASE Statement
	 */
	private static String getOrderPriceDisSqlCaseStatement(List<Integer> ranges) {
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
				sb.append("when o.need_pay_money > ").append(num).append(" then '").append(num).append("+' ");
			} 
			if (i < ranges.size() - 1) {
				nextNum = ranges.get(i + 1);
				sb.append("when o.need_pay_money >= ").append(nextNum).append(" and o.need_pay_money <= ").append(num)
				  .append(" then '").append(nextNum).append("~").append(num).append("' ");
			}
		}
		sb.append("else '0' end ) ");
		return sb.toString();
	}
	
	/**
	 * 获取购买分析  购买时段分析 highchart-json格式数据集
	 * @param startDate 起始时间
	 * @param endDate 结束时间
	 * @param storeid 店铺id
	 * @return json格式数据集
	 */
	@Override
	public List<Map<String, Object>> getBuyTimeDis(String startDate,
			String endDate, Integer storeid) {
		String dateWhere = ""; // 时间条件

		// 如果包含开始时间条件
		if (startDate != null && !"".equals(startDate)) {

			dateWhere += "AND o.create_time >= " + startDate;
		}
		// 如果包含结束时间条件
		if (endDate != null && !"".equals(endDate)) {

			dateWhere += " AND o.create_time <= " + endDate;
		}
		
		String db_type = EopSetting.DBTYPE;
		String dateFunction = "";
		if (db_type.equals("1")) {		//mysql
			dateFunction = "CONVERT(FROM_UNIXTIME(o.create_time, '%k'),SIGNED)";
		} else if (db_type.equals("2")) {		//oracle
			dateFunction = "TO_NUMBER(" + this.getOracleTimeFormatFunc("o.create_time", "hh24") + ")";
		} else if (db_type.equals("3")) {		//sqlserver
			//TODO 数值型（单位：秒）表示的日期格式转换 for SQLServer
			dateFunction ="DATEPART(hh,DATEADD(SECOND, o.create_time, '1970-01-01 08:00:00'))";
		}

		String sql = "SELECT count(o.order_id) AS num, " + dateFunction + " AS hour_num "
				+ "FROM es_order o "
				+ "WHERE 1=1  And o.store_id="+storeid+"  AND status>="+OrderStatus.ORDER_PAY+" AND status!="+OrderStatus.ORDER_CANCELLATION+" AND pay_status="+OrderStatus.PAY_YES+" "
				+ dateWhere
				+ " GROUP BY " + dateFunction + " ORDER BY hour_num";
		
		List<Map<String, Object>> data = this.daoSupport.queryForList(sql);
		data=StatisticsUtil.getInstance().fitTimeData(data);
		return data;
	}
	

	/**
	 * 数值型（单位：秒）表示的日期格式转换 for Oracle
	 * @param col 列名，如：r.name
	 * @param pattern 转换格式如：yyyy-mm-dd hh24:mi:ss
	 * @return SQL语句片段
	 */
	private String getOracleTimeFormatFunc(String col, String pattern) {
		String func = "to_char("
				+ "TO_DATE('19700101','yyyymmdd') + " + col + "/86400 + TO_NUMBER(SUBSTR(TZ_OFFSET(sessiontimezone),1,3))/24, '" + pattern + "')";
		return func;
	}

	/**
	 * 获取 销售统计  下单总金额 下单总量json格式数据集
	 * @param cycle_type
	 * @param year
	 * @param month
	 * @param storeid
	 * @return json格式数据集
	 */
	@Override
	public Object getTotalMoneyNum(Integer cycle_type, Integer year,
			Integer month, Integer storeid) {
		if(cycle_type==null){
			cycle_type=1;
		}
		long times[]=StatisticsUtil.getInstance().getStartTimeAndEndTime(cycle_type, year, month);
		long starttime=times[0];
		long endtime=times[1];
		String sql="select count(0) as t_num,SUM(need_pay_money) as t_money from es_order o where 1=1 AND status>="+OrderStatus.ORDER_PAY+" AND status!="+OrderStatus.ORDER_CANCELLATION+" AND pay_status="+OrderStatus.PAY_YES;
		if(storeid!=null&&storeid!=0){
			sql+=" and o.store_id="+storeid;
		}
		sql+=" and o.create_time > "+starttime;
		sql+=" and o.create_time < "+endtime;
		List list = this.daoSupport.queryForList(sql);
		
		return JsonResultUtil.getObjectJson(list.get(0));
	}

	@Override
	public void exportExcel(Integer year, Integer month, Integer cycle_type,
			Integer storeid) {
		
		//拼接sql
		StringBuffer sbsql=new StringBuffer();
		sbsql.append("select o.sn as ordersn,m.name as ordermember,o.create_time as createtime,o.need_pay_money as needpaymoney,o.status as orderstatus  from es_order o left join es_member m on o.member_id=m.member_id where 1=1  AND status>="+OrderStatus.ORDER_PAY+" AND status!="+OrderStatus.ORDER_CANCELLATION+" AND pay_status="+OrderStatus.PAY_YES+" ");
		
		if(cycle_type==null){
			cycle_type=1;
		}
		Calendar calendar = Calendar.getInstance();
		if(year==null){
			year=calendar.get(Calendar.YEAR);
		}
		if(month==null){
			month = calendar.get(Calendar.MONTH) + 1;
		}
		long times[]=StatisticsUtil.getInstance().getStartTimeAndEndTime(cycle_type, year, month);
		long starttime=times[0];
		long endtime=times[1];
		sbsql.append("and  o.create_time > ? ");
		sbsql.append("and  o.create_time < ? ");
		sbsql.append("and o.store_id=?");
		String sql=sbsql.toString();
		
		List<Map<String,Object>> list=this.daoSupport.queryForList(sql,  starttime,endtime,storeid);
		
		//使用excel导出流量报表
		ExcelUtil excelUtil = new ExcelUtil(); 

		//流量报表excel模板在类包中，转为流给excelutil
		InputStream in =FileUtil.getResourceAsStream("com/enation/app/b2b2c/core/store/service/statistics/impl/sales_detail.xls");

		excelUtil.openModal( in );
		int i=1;
		for(Map map :list){
			

			excelUtil.writeStringToCell(i, 0,map.get("ordersn").toString()); //订单编号
			excelUtil.writeStringToCell(i, 1, map.get("ordermember").toString() ); //买家
			excelUtil.writeStringToCell(i, 2, DateUtil.toString(new Date(Long.valueOf(map.get("createtime").toString())*1000), "yyyy-MM-dd HH:mm:ss")); //下单时间
			excelUtil.writeStringToCell(i, 3,""+map.get("needpaymoney").toString() ); //订单总额
			excelUtil.writeStringToCell(i, 4,OrderStatus.getOrderStatusText(Integer.valueOf(map.get("orderstatus").toString())) ); //订单状态
			i++;
		}
		HttpServletResponse response=ThreadContextHolder.getHttpResponse();
		String filename=processFileName(ThreadContextHolder.getHttpRequest(), "销售统计");
		excelUtil.writeToResponse(response,filename);
	}
	
	/**
	 * 
	 * @param request http请求
	 * @param fileNames	文件名
	 * @return
	 */
    private static String processFileName(HttpServletRequest request, String fileNames) {  
        String codedfilename = null;  
        try {  
            String agent = request.getHeader("USER-AGENT");  
            if (null != agent && -1 != agent.indexOf("MSIE") || null != agent  
                    && -1 != agent.indexOf("Trident")) {// ie  
  
                String name = java.net.URLEncoder.encode(fileNames, "UTF8");  
  
                codedfilename = name;  
            } else if (null != agent && -1 != agent.indexOf("Mozilla")) {// 火狐,chrome等  
  
  
                codedfilename = new String(fileNames.getBytes("UTF-8"), "UTF-8");  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return codedfilename;  
    }  
}
