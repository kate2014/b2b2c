package com.enation.app.b2b2c.core.statistics.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.enation.app.b2b2c.core.statistics.service.IB2b2cFlowStatisticsManager;
import com.enation.eop.sdk.context.EopSetting;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;

@Service
public class B2b2cBackendFlowStatisticsManager implements IB2b2cFlowStatisticsManager {

	@Autowired
	private IDaoSupport daoSupport;
	
	/**
	 * 增加访问日志
	 * @author LSJ
	 * @param goodsId  商品ID
	 * @param store_id 店铺ID
	 * 2016年12月6日上午11:38
	 */
	@Override
	public void addFlowLog(int goodsId,int store_id) {
		
		// 获取当前时间戳
		long timeStamp = DateUtil.getDateline();
		if("b2c".equals(EopSetting.PRODUCT)){
			String sql = "INSERT INTO es_flow_log(goods_id, visit_time) VALUES(?, ?)";
			this.daoSupport.execute(sql, goodsId, timeStamp);
		}else if("b2b2c".equals(EopSetting.PRODUCT)){
			String sql = "INSERT INTO es_flow_log (goods_id, visit_time,store_id) VALUES (?,?,?)";
			this.daoSupport.execute(sql, goodsId,timeStamp,store_id);
		}
	}
	
	/**
	 * 流量统计
	 * @param statisticsType  统计类型
	 * @param startDateStamp  开始时间
	 * @param endDateStamp	结束时间
	 * @param store_id 店铺ID
	 * @return 返回数据集
	 */
	@Override
	public List<Map<String, Object>> getFlowStatistics(String statisticsType, String startDate,
			String endDate,String store_id) {
		String dateWhere = ""; // 时间条件
		String dateType = "%d";	//时间类型 按年查询就显示 12个月 按月查询就显示天数

		String storeWhere="";
		if(store_id != null && !"0".equals(store_id)){
			storeWhere=" AND store_id = " +store_id;
		}
		
		// 如果包含开始时间条件
		if (startDate != null && !"".equals(startDate)) {

			dateWhere += "AND visit_time >= " + startDate;
		}
		
		// 如果包含结束时间条件
		if (endDate != null && !"".equals(endDate)) {

			dateWhere += " AND visit_time <= " + endDate;
		}
		
		String db_type = EopSetting.DBTYPE;
		String dateFunction = "";
		if (db_type.equals("1")) {		//mysql
			// 如果是按照年份查询
			if ("1".equals(statisticsType)) {
				dateType = "%m";
			}
			dateFunction = "CONVERT(FROM_UNIXTIME(visit_time, '" + dateType + "'),SIGNED)";
		} else if (db_type.equals("2")) {		//oracle
			dateType = "dd";
			// 如果是按照年份查询
			if ("1".equals(statisticsType)) {
				dateType = "mm";
			}
			dateFunction = "TO_NUMBER(" + this.getOracleTimeFormatFunc("visit_time", dateType) + ")";
		} else if (db_type.equals("3")) {		//sqlserver
			//TODO 数值型（单位：秒）表示的日期格式转换 for SQLServer
			dateFunction = "DATEPART(dd,DATEADD(SECOND, visit_time, '1970-01-01 08:00:00'))";
		}
	
		String sql = "SELECT count(flow_id) AS num, " + dateFunction + " AS day_num "
				+ "FROM es_flow_log "
				+ "WHERE 1=1 "
				+ dateWhere
				+ storeWhere
				+ " GROUP BY " + dateFunction + " ORDER BY day_num";
		
		List<Map<String, Object>> data = this.daoSupport.queryForList(sql);
		
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
	 * 商品流量分析
	 * @author LSJ
	 * @param top_num  商品访问量top
	 * @param startDateStamp  开始时间
	 * @param endDateStamp	结束时间
	 * @param store_id	店铺ID
	 * @return 获取到的结果集
	 * 2016年12月6日上午11:38
	 */
	@Override
	public List<Map<String, Object>> getGoodsFlowStatistics(int topNum, String startDate,
			String endDate,String store_id) {
		String dateWhere = ""; // 时间条件

		String storeWhere="";
		if(store_id != null && !"0".equals(store_id)){
			storeWhere = " AND f.store_id = " +store_id;
		}
		
		// 如果包含开始时间条件
		if (startDate != null && !"".equals(startDate)) {

			dateWhere += "AND f.visit_time >= " + startDate;
		}
		// 如果包含结束时间条件
		if (endDate != null && !"".equals(endDate)) {

			dateWhere += " AND f.visit_time <= " + endDate;
		}

		String sql = "SELECT count(f.flow_id) AS num, g.NAME FROM es_flow_log f "
				+ "LEFT JOIN es_goods g ON f.goods_id = g.goods_id "
				+ "WHERE 1 = 1 "
				+ dateWhere
				+storeWhere
				+ " GROUP BY f.goods_id, g.NAME ";
		String mainSql = sql + " ORDER BY count(f.flow_id) DESC ";
		String countSql = "SELECT COUNT(*) FROM (" + sql + ") tmp0";
		
		Page page = this.daoSupport.queryForPage(mainSql, countSql, 1, topNum);
		
		return (List<Map<String, Object>>) page.getResult();
	}
	
	

}
