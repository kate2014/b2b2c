package com.enation.app.b2b2c.front.api.store.statistics;

import java.util.List;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enation.app.b2b2c.core.statistics.service.IB2b2cGoodsStatisticsManager;
import com.enation.app.b2b2c.core.store.service.statistics.IB2b2cStoreProfileStatisticsManager;
import com.enation.framework.action.GridController;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.action.JsonResult;
import com.enation.framework.database.Page;
import com.enation.framework.util.JsonResultUtil;
import com.enation.framework.util.TestUtil;

import net.sf.json.JSONArray;

/**
 * 
 * 店铺概况统计api
 * 商家中心-统计-店铺概况页所需api
 * @author jianghongyan
 * @version v1.0
 * @since v6.2
 * 2016年10月25日 下午2:22:55
 */
@Controller
@RequestMapping("/api/store-profile")
public class StoreProfileStatisticsApiController  extends GridController {

	@Autowired
	private IB2b2cStoreProfileStatisticsManager b2b2cStoreProfileStatisticsManager;
	
	@Autowired
	private IB2b2cGoodsStatisticsManager b2b2cBackendGoodsStatisticsManager;
	
	protected final Logger logger = Logger.getLogger(getClass());
	/**
	 * 获取30天店铺概况展示数据
	 * @param store_id 店铺id
	 * @return 符合展示要求的JsonResult数据集
	 */
	@ResponseBody
	@RequestMapping("get-last30day-status")
	public JsonResult getLast30dayStatus(Integer store_id){
		try {
			return this.b2b2cStoreProfileStatisticsManager.getLast30dayStatus(store_id);
		} catch (RuntimeException e) {
			this.logger.error("获取30天店铺概况展示数据失败", e);
			TestUtil.print(e);
			return JsonResultUtil.getErrorJson("数据异常");
		}
	}
	
	/**
	 * 获取30天店铺下单金额统计图数据
	 * @param store_id 店铺id
	 * @return 缝合highchart要求的JsonResult数据集
	 */
	@ResponseBody
	@RequestMapping("get-last30day-linechart")
	public JsonResult getLast30dayLineChart(Integer store_id){
		try {
			return this.b2b2cStoreProfileStatisticsManager.getLast30dayLineChart(store_id);
		} catch (RuntimeException e) {
			this.logger.error("获取30天店铺下单金额统计图数据失败", e);
			TestUtil.print(e);
			return JsonResultUtil.getErrorJson("数据异常");
		}
	}
	
	/**
	 * 读取收藏甘特图Json
	 * @param storeId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/get-collect-chart-json")
	public String getCollectChartJson(Integer storeId){
		Page webPage =  this.b2b2cBackendGoodsStatisticsManager.getCollectPage(1, 50, storeId);
		List list = (List) webPage.getResult();
		return JSONArray.fromObject(list).toString();
	}
	
	/**
	 * 读取收藏列表
	 * @param storeId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/get-collect-json")
	public GridJsonResult getCollectJson(Integer storeId){
		Page webPage =  this.b2b2cBackendGoodsStatisticsManager.getCollectPage(this.getPage(), this.getPageSize(), storeId);
		return JsonResultUtil.getGridJson(webPage);
	}
	
	
}
