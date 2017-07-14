package com.enation.app.b2b2c.core.statistics.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.b2b2c.core.statistics.service.IB2b2cRegionStatisticsManager;
import com.enation.app.b2b2c.core.store.model.Store;
import com.enation.app.b2b2c.core.store.service.IStoreManager;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.util.JsonResultUtil;

@SuppressWarnings({ "rawtypes" })
@Controller
@Scope("prototype")
@RequestMapping("/b2b2c/admin/regionOrderStatistics")
public class B2b2cRegionOrderStatisticsController {
	@Autowired
	private IB2b2cRegionStatisticsManager b2b2cRegionStatisticsManager;
	@Autowired
	private IStoreManager storeManager;
	/**
	 * 区域分析页面
	 * @return 区域分析页面
	 */
	@RequestMapping(value="/region-list")
	public ModelAndView regionList(){
		ModelAndView view=new ModelAndView();
		List<Store> storeList=this.storeManager.listAll();
		view.addObject("storeList", storeList);
		view.setViewName("/b2b2c/admin/statistics/sales/quyu");
		return view;  
	}
	
	/**
	 * 区域分析JSON
	 * @param type 1.下单会员数、2.下单量、3.下单金额
	 * @param cycle_type 周期模式	1为月，反之则为年
	 * @param year 年
	 * @param month 月
	 * @param data 区域分析JSON
	 * @return 区域分析JSON
	 */
	@ResponseBody
	@RequestMapping(value="/region-type-list-json")
	public Object regionTypeListJson(String data, Integer type, Integer cycle_type, Integer year, Integer month,String store_id){
		try {
			data = b2b2cRegionStatisticsManager.getRegionStatistics(type,cycle_type,year,month,store_id);
			return "{\"message\":"+data+"}";
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResultUtil.getErrorJson("获取json失败！");
		}
	}
	
	/**
	 * 获取区域分析列表JSON
	 * @param type 1.下单会员数、2.下单量、3.下单金额
	 * @param sort 排序方式,正序、倒序
	 * @return 区域分析列表JSON
	 */
	@ResponseBody
	@RequestMapping(value="/region-list-json")
	public GridJsonResult regionListJson(Integer type, Integer cycle_type, Integer year, Integer month,String store_id){
		
		List regionList = b2b2cRegionStatisticsManager.regionStatisticsList(type, " desc ",cycle_type,year,month,store_id);
		return JsonResultUtil.getGridJson(regionList);
		
	}
}
