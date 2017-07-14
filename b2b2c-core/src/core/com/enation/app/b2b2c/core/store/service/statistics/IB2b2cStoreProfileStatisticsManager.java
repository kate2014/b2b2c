package com.enation.app.b2b2c.core.store.service.statistics;

import com.enation.framework.action.JsonResult;

/**
 * 
 * 店铺概况管理接口
 * @author    jianghongyan
 * @version   1.0.0,2016年8月4日
 * @since     v6.2
 */
public interface IB2b2cStoreProfileStatisticsManager {

	/**
	 * 获取30天店铺概况展示数据
	 * @param store_id 
	 * @return 符合展示要求的JsonResult数据集
	 */
	JsonResult getLast30dayStatus(Integer store_id) throws RuntimeException ;

	/**
	 * 获取30天店铺下单金额统计图数据
	 * @param store_id 
	 * @return 缝合highchart要求的JsonResult数据集
	 */
	JsonResult getLast30dayLineChart(Integer store_id) throws RuntimeException ;

}
