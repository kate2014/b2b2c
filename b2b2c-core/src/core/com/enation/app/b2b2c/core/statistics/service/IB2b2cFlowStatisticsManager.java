package com.enation.app.b2b2c.core.statistics.service;

import java.util.List;
import java.util.Map;

public interface IB2b2cFlowStatisticsManager {
	/**
	 * 流量统计
	 * @author LSJ
	 * @param statisticsType  统计类型
	 * @param startDateStamp  开始时间
	 * @param endDateStamp	结束时间
	 * @param store_id 店铺ID
	 * @return 返回数据集
	 * 2016年12月6日上午11:38
	 */
	List<Map<String, Object>> getFlowStatistics(String statisticsType, String startDateStamp, String endDateStamp, String store_id);

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
	List<Map<String, Object>> getGoodsFlowStatistics(int top_num, String startDateStamp, String endDateStamp, String store_id);

	/**
	 * 增加访问日志
	 * @author LSJ
	 * @param goodsId  商品ID
	 * @param store_id 店铺ID
	 * 2016年12月6日上午11:38
	 */
	void addFlowLog(int goodsId, int store_id);

}
