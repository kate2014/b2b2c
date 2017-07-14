package com.enation.app.b2b2c.core.store.service.statistics.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.b2b2c.core.store.service.statistics.IB2b2cFlowStatisticsManager;
import com.enation.app.b2b2c.core.store.service.statistics.StatisticsUtil;
import com.enation.eop.sdk.context.EopSetting;
import com.enation.framework.action.JsonResult;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.JsonResultUtil;

/**
 * 
 * 流量统计管理接口
 * @author    jianghongyan
 * @version   1.0.0,2016年8月4日
 * @since     v6.2
 */
@Service
public class B2b2cFlowStatisticsManager implements IB2b2cFlowStatisticsManager{

	@Autowired
	private IDaoSupport daoSupport;
	
	
	@Override
	public JsonResult getStoreFlowStatistics(Integer year, Integer month,
			Integer cycle_type, Integer storeid) {
		//TODO 未完成店铺总量统计
		String dateWhere = ""; // 时间条件
		String dateType = "%d";	//时间类型 按年查询就显示 12个月 按月查询就显示天数
		String startDate="";
		String endDate="";
		if(cycle_type==1){
			startDate=String.valueOf(DateUtil.getDateline(year+"-"+month+"-"+"01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
			endDate=String.valueOf(DateUtil.getDateline(year+"-"+month+"-"+StatisticsUtil.getInstance().getDaysByYearMonth(year, month)+" 23:59:59", "yyyy-MM-dd HH:mm:ss"));
		}else{
			startDate=String.valueOf(DateUtil.getDateline(year+"-"+1+"-"+"01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
			endDate=String.valueOf(DateUtil.getDateline(year+"-"+12+"-"+31+" 23:59:59", "yyyy-MM-dd HH:mm:ss"));
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
			if (2==cycle_type) {
				dateType = "%m";
			}
			dateFunction = "CONVERT(FROM_UNIXTIME(visit_time, '" + dateType + "'),SIGNED)";
		} else if (db_type.equals("2")) {		//oracle
			dateType = "dd";
			// 如果是按照年份查询
			if (2==cycle_type) {
				dateType = "mm";
			}
			dateFunction = "TO_NUMBER(" + this.getOracleTimeFormatFunc("visit_time", dateType) + ")";
		} else if (db_type.equals("3")) {		//sqlserver
			//TODO 数值型（单位：秒）表示的日期格式转换 for SQLServer
			dateType="dd";
			if (2==cycle_type) {
				dateType = "mm";
			}
			dateFunction ="DATEPART("+dateType+",DATEADD(SECOND, visit_time, '1970-01-01 08:00:00'))";
		}
	
		String sql = "SELECT count(flow_id) AS num, " + dateFunction + " AS day_num "
				+ "FROM es_flow_log "
				+ "WHERE 1=1 AND store_id=? "
				+ dateWhere
				+ " GROUP BY " + dateFunction + " ORDER BY day_num";
		
		List<Map<String, Object>> data = this.daoSupport.queryForList(sql,storeid);
		data=StatisticsUtil.getInstance().fitFlowList(data,year,month,cycle_type);
		return JsonResultUtil.getObjectJson(data);
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
	@Override
	public JsonResult getTopGoodsFlowStatistics(Integer year, Integer month,
			Integer cycle_type, Integer storeid) {
				int topNum=30;
				String dateWhere = ""; // 时间条件
				String startDate="";
				String endDate="";
				if(cycle_type==1){
					startDate=String.valueOf(DateUtil.getDateline(year+"-"+month+"-"+"01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
					endDate=String.valueOf(DateUtil.getDateline(year+"-"+month+"-"+StatisticsUtil.getInstance().getDaysByYearMonth(year, month)+" 23:59:59", "yyyy-MM-dd HH:mm:ss"));
				}else{
					startDate=String.valueOf(DateUtil.getDateline(year+"-"+1+"-"+"01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
					endDate=String.valueOf(DateUtil.getDateline(year+"-"+12+"-"+31+" 23:59:59", "yyyy-MM-dd HH:mm:ss"));
				}
				// 如果包含开始时间条件
				if (startDate != null && !"".equals(startDate)) {

					dateWhere += "AND f.visit_time >= " + startDate;
				}
				// 如果包含结束时间条件
				if (endDate != null && !"".equals(endDate)) {

					dateWhere += " AND f.visit_time <= " + endDate;
				}

				String sql = "SELECT count(f.flow_id)AS num, g.NAME FROM es_flow_log f "
						+ "LEFT JOIN es_goods g ON f.goods_id = g.goods_id "
						+ "WHERE 1 = 1 AND f.store_id=? "
						+ dateWhere
						+ " GROUP BY f.goods_id, g.NAME ";
				String countSql = "SELECT COUNT(*) FROM (" + sql + ") tmp0";
				String mainSql = sql + " ORDER BY count(f.flow_id) DESC ";
				
				Page page = this.daoSupport.queryForPage(mainSql, countSql, 1, topNum,storeid);
				List<Map<String, Object>> list=(List<Map<String, Object>>) page.getResult();
				list=StatisticsUtil.fitTopGoodsFlowList(list,topNum);
				return JsonResultUtil.getObjectJson(list);
	}
	
}
