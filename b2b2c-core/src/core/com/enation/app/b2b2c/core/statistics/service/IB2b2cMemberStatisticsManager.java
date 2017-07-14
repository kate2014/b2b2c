package com.enation.app.b2b2c.core.statistics.service;

import java.util.List;
import java.util.Map;

public interface IB2b2cMemberStatisticsManager {

	/**
	 * 会员下单量统计
	 * @author LSJ
	 * @param top_num 排名名次
	 * @param startDateStamp 开始时间
	 * @param endDateStamp 结束时间
	 * @param store_id 店铺ID
	 * @return 会员下单量 list
	 * 2016年12月6日上午11:38
	 */
	List<Map<String, Object>> getOrderNumTop(int top_num, String startDateStamp, String endDateStamp,
			String store_id);

	/**
	 * 会员下单商品数量
	 * @author LSJ
	 * @param top_num 排行名次
	 * @param startDateStamp 开始时间
	 * @param endDateStamp 结束时间
	 * @param store_id 店铺ID
	 * @return 会员下单商品数量
	 * 2016年12月6日上午11:38
	 */
	List<Map<String, Object>> getGoodsNumTop(int top_num, String startDateStamp, String endDateStamp,
			String store_id);

	/**
	 * 会员下单总金额统计
	 * @author LSJ
	 * @param top_num 排行名次
	 * @param startDateStamp 开始时间
	 * @param endDateStamp 结束时间
	 * @param store_id 店铺ID
	 * @return 下单金额 list
	 * 2016年12月6日上午11:38
	 */
	List<Map<String, Object>> getOrderPriceTop(int top_num, String startDateStamp, String endDateStamp,
			String store_id);

	/**
	 * 客单价分布数据
	 * @author LSJ
	 * @param asList 区间
	 * @param startDateStamp 开始时间
	 * @param endDateStamp 结束时间
	 * @param store_id 店铺ID
	 * @return 客单价分布数据list
	 * 2016年12月6日上午11:38
	 */
	List<Map<String, Object>> getOrderPriceDis(List<Integer> asList, String startDateStamp, String endDateStamp, String store_id);

	/**
	 * 用户购买频次数据
	 * @author LSJ
	 * @param startDateStamp 开始时间
	 * @param endDateStamp 结束时间
	 * @param store_id 店铺ID
	 * @return 用户购买频次数据 list
	 * 2016年12月6日上午11:38
	 */
	List<Map<String, Object>> getBuyFre(String startDateStamp, String endDateStamp, String store_id);

	/**
	 * 用户购买时段分布数据
	 * @author LSJ
	 * @param startDateStamp 开始时间
	 * @param endDateStamp 结束时间
	 * @param store_id 店铺ID
	 * @return 用户购买时段分布数据 list
	 * 2016年12月6日上午11:38
	 */
	List<Map<String, Object>> getBuyTimeDis(String startDateStamp, String endDateStamp, String store_id);

	/**
	 * 会员新增  按年统计 【当前年份】
	 * @author LSJ
	 * @param startDateStamp 开始时间 
	 * @param endDateStamp 结束时间
	 * @return 按年统计 list
	 * 2016年12月6日上午11:38
	 */
	List<Map<String, Object>> getAddYearMemberNum(String startDateStamp, String endDateStamp);

	/**
	 * 会员新增 按年统计 【上一年】
	 * @author LSJ
	 * @param lastStartDateStamp
	 * @param lastEndDateStamp
	 * @return 按年统计list
	 * 2016年12月6日上午11:38
	 */
	List<Map<String, Object>> getLastAddYearMemberNum(String lastStartDateStamp, String lastEndDateStamp);

	/**
	 * 会员新增  按月统计 【当前月份】
	 * @author LSJ
	 * @param startDateStamp
	 * @param endDateStamp
	 * @return 按月统计list
	 * 2016年12月6日上午11:38
	 */
	List<Map<String, Object>> getAddMemberNum(String startDateStamp, String endDateStamp);

	/**
	 * 会员新增 按月统计 【上一个月】
	 * @author LSJ
	 * @param lastStartDate 开始时间
	 * @param lastEndDate 结束时间
	 * @return 按月统计list
	 * 2016年12月6日上午11:38
	 */
	List<Map<String, Object>> getLastAddMemberNum(String lastStartDate, String lastEndDate);

}
