package com.enation.app.b2b2c.core.store.service.statistics;

import java.util.List;
import java.util.Map;

import com.enation.framework.action.GridJsonResult;

/**
 * 多店商家中心-统计-运营报告管理接口
 * @author jianghongyan 2016年7月1日 版本改造
 * @version v6.1
 * @since v6.1
 */
public interface IB2b2cOperatorStatisticsManager {

	/**
	 * 获取销售统计 下单金额highchart-json格式数据集
	 * @param year 查询年份
	 * @param month 查询月份
	 * @param cycle_type 查询周期
	 * @param storeid 店铺id
	 * @return json格式数据集
	 */
	Object getSalesMoney(Integer year, Integer month, Integer cycle_type, Integer storeid);

	/**
	 * 获取销售统计  下单量highchart-json格式数据集
	 * @param year 查询年份
	 * @param month 查询月份
	 * @param cycle_type 查询周期
	 * @param storeid 店铺id
	 * @return json格式数据集
	 */
	Object getSalesNum(Integer year, Integer month, Integer cycle_type, Integer storeid);

	/**
	 * 获取区域分析 highchart-json格式数据集
	 * @param type 查询类型 1:下单会员数 2.会员下单金额 3.会员下单量
	 * @param cycle_type 查询周期
	 * @param year 查询年份
	 * @param month 查询月份
	 * @param storeid 店铺id
	 * @return json格式数据集
	 */
	String getRegionStatistics(int type, Integer cycle_type, Integer year,
			Integer month, Integer storeid);
	
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
	GridJsonResult getSalesMoneyDgjson(Integer year, Integer month, Integer cycle_type,
			Integer storeid,int pageNo,int pageSize);

	/**
	 * 获取购买分析 客单价分析  购买时段分析  highchart-json格式数据集
	 * @param cycle_type 查询周期
	 * @param year 查询年份
	 * @param month 查询月份
	 * @param storeid 店铺id
	 * @return json格式数据集
	 */
	Object getBuyStatistics(Integer cycle_type, Integer year, Integer month,
			Integer storeid);

	/**
	 * 获取购买分析  客单价分布 highchart-json格式数据集
	 * @param sections 价格区间
	 * @param startDate 起始时间
	 * @param endDate 结束时间
	 * @param storeid 店铺id
	 * @return json格式数据集
	 */
	List<Map<String, Object>> getOrderPriceDis(List<Integer> sections,
			String startDate, String endDate, Integer storeid);

	/**
	 * 获取购买分析  购买时段分析 highchart-json格式数据集
	 * @param startDate 起始时间
	 * @param endDate 结束时间
	 * @param storeid 店铺id
	 * @return json格式数据集
	 */
	List<Map<String, Object>> getBuyTimeDis(String startDate, String endDate, Integer storeid);

	/**
	 * 获取 销售统计  下单总金额 下单总量json格式数据集
	 * @param cycle_type
	 * @param year
	 * @param month
	 * @param storeid
	 * @return json格式数据集
	 */
	Object getTotalMoneyNum(Integer cycle_type, Integer year, Integer month,
			Integer storeid);

	/**
	 * 导出excel表格
	 * @param year
	 * @param month
	 * @param cycle_type
	 * @param storeid
	 */
	void exportExcel(Integer year, Integer month, Integer cycle_type,
			Integer storeid);

}
