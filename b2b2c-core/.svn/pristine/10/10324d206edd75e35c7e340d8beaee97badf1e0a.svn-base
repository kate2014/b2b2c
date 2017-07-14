package com.enation.app.b2b2c.core.statistics.service;

import java.util.List;

public interface IB2b2cRegionStatisticsManager {

	/**
	 * 区域分析统计
	 * @author LSJ
	 * @param type 1.下单会员数、2.下单量、3.下单金额
	 * @param cycle_type 周期模式	1为月，反之则为年
	 * @param year 年
	 * @param month 月
	 * @param store_id 店铺ID
	 * @return 区域分析JSON
	 * 2016年12月6日下午15:16
	 */
	String getRegionStatistics(Integer type, Integer cycle_type, Integer year, Integer month, String store_id);

	/**
	 * 区域分析列表
	 * @author LSJ
	 * @param type 1.下单会员数、2.下单量、3.下单金额
	 * @param string
	 * @param cycle_type 周期模式	1为月，反之则为年
	 * @param year 年
	 * @param month 月
	 * @param store_id 店铺ID
	 * @return 区域分析列表 list
	 * 2016年12月6日下午15:16
	 */
	List regionStatisticsList(Integer type, String string, Integer cycle_type, Integer year, Integer month, String store_id);

}
