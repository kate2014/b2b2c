package com.enation.app.b2b2c.front.api.store.statistics;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enation.app.b2b2c.core.store.service.statistics.IB2b2cFlowStatisticsManager;
import com.enation.framework.action.JsonResult;
import com.enation.framework.util.JsonResultUtil;
import com.enation.framework.util.TestUtil;
/**
 * 
 * 流量统计api
 * @author    jianghongyan
 * @version   1.0.0,2016年8月4日
 * @since     v6.2
 */
@Controller
@RequestMapping("/api/flow-statistics")
public class StoreFlowStatisticsApiController {
	@Autowired
	private IB2b2cFlowStatisticsManager b2b2cFlowStatisticsManager;
	
	protected final Logger logger = Logger.getLogger(getClass());
	
	/**
	 * 获取店铺总流量linechart数据
	 * @param year 年份
	 * @param month 月份
	 * @param cycle_type 周期
	 * @param storeid 店铺id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("get-store-flow-statistics")
	public JsonResult getStoreFlowStatistics(Integer year,Integer month,Integer cycle_type,Integer storeid){
		try {
			return this.b2b2cFlowStatisticsManager.getStoreFlowStatistics(year,month,cycle_type,storeid);
		} catch (RuntimeException e) {
			// TODO: handle exception
			this.logger.error("获取店铺总流量linechart数据失败",e);
			TestUtil.print(e);
			return JsonResultUtil.getErrorJson("");
		}
	}
	@ResponseBody
	@RequestMapping("get-topgoods-flow-statistics")
	public JsonResult getTopGoodsFlowStatistics(Integer year,Integer month,Integer cycle_type,Integer storeid){
		try {
			return this.b2b2cFlowStatisticsManager.getTopGoodsFlowStatistics(year,month,cycle_type,storeid);
		} catch (RuntimeException e) {
			// TODO: handle exception
			this.logger.error("获取店铺总流量linechart数据失败",e);
			TestUtil.print(e);
			return JsonResultUtil.getErrorJson("");
		}
	}
}
