package com.enation.app.b2b2c.core.statistics.service;

import java.util.List;
import java.util.Map;

public interface IB2b2cReturnedStatisticsManager {

	/**
	 * 退款统计列表    按月统计
	 * @author LSJ
	 * @param year 年
	 * @param month 月
	 * @param store_id 店铺ID
	 * @return 退款统计列表list
	 * 2016年12月6日下午15:16
	 */
	List<Map> statisticsMonth_Amount(long year, long month, String store_id);

	/**
	 * 退款统计列表   按年统计
	 * @author LSJ
	 * @param year 年
	 * @param store_id 店铺ID
	 * @return 退款统计列表list
	 * 2016年12月6日下午15:16
	 */
	List<Map> statisticsYear_Amount(int year, String store_id);

}
