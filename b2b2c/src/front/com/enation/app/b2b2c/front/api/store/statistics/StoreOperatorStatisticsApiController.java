package com.enation.app.b2b2c.front.api.store.statistics;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.b2b2c.core.store.service.statistics.IB2b2cOperatorStatisticsManager;
import com.enation.app.b2b2c.core.store.service.statistics.StatisticsUtil;
import com.enation.framework.action.GridController;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.JsonResultUtil;

/**
 * 运营报告 
 * @author jianghongyan 2016年7月1日 版本改造
 * @version v6.1
 * @since v6.1
 * 
 * 本类中所有cycle_type参数  1:按月查询 2:按年查询
 * 
 */
@Controller
@Scope("prototype")
@RequestMapping("/api/operator-statistics")
public class StoreOperatorStatisticsApiController  extends GridController{
	
	
	@Autowired
	private IB2b2cOperatorStatisticsManager b2b2cOperatorStatisticsManager;//运营报告管理类
	/**
	 * load销售统计 下单金额页面
	 * @return
	 */
	@RequestMapping("get-sales-money-html")
	public ModelAndView getSalesMoneyHtml(){
		ModelAndView view=new ModelAndView();
		HttpServletRequest request=ThreadContextHolder.getHttpRequest();
		view.addObject("ctx", request.getContextPath());
		view.setViewName("/themes/b2b2cv2/new_store/pages/statistics/get-sales-money");
		return view;
	}
	/**
	 * load销售统计  下单量页面
	 * @return
	 */
	@RequestMapping("get-sales-num-html")
	public ModelAndView getSalesNumHtml(){
		ModelAndView view=new ModelAndView();
		HttpServletRequest request=ThreadContextHolder.getHttpRequest();
		view.addObject("ctx", request.getContextPath());
		view.setViewName("/themes/b2b2cv2/new_store/pages/statistics/get-sales-num");
		return view;
	}
	/**
	 * 获取销售统计  下单金额highcharts-json格式数据
	 * @param year 查询年份
	 * @param month 查询月份
	 * @param cycle_type 查询周期
	 * @param storeid 店铺id
	 * @return json格式数据集
	 */
	@ResponseBody
	@RequestMapping("get-sales-money")
	public Object getSalesMoney(Integer year,Integer month,Integer cycle_type,Integer storeid){
		return this.b2b2cOperatorStatisticsManager.getSalesMoney(year,month,cycle_type,storeid);
		
	} 
	/**
	 * 获取销售统计 下单量highcharts-json格式数据集
	 * @param year 查询年份
	 * @param month 查询月份
	 * @param cycle_type 查询周期
	 * @param storeid 店铺id
	 * @return json格式数据集
	 */
	@ResponseBody
	@RequestMapping("get-sales-num")
	public Object getSalesNum(Integer year,Integer month,Integer cycle_type,Integer storeid){
		return this.b2b2cOperatorStatisticsManager.getSalesNum(year,month,cycle_type,storeid);
		
	} 
	
	/**
	 * 获取销售统计  下单金额 datagrid-json格式数据
	 * @param year 查询年份
	 * @param month 查询月份
	 * @param cycle_type 查询周期
	 * @param storeid 店铺id
	 * @return json格式数据集
	 */
	@ResponseBody
	@RequestMapping("get-sales-money-dgjson")
	public GridJsonResult getSalesMoneyDgjson(Integer year,Integer month,Integer cycle_type,Integer storeid){
		int page=this.getPage();
		int pageSize=this.getPageSize();
		return this.b2b2cOperatorStatisticsManager.getSalesMoneyDgjson(year,month,cycle_type,storeid,page,pageSize);
	}
	/**
	 * 获取总下单金额和总下单量
	 * @param cycle_type 查询周期
	 * @param year 查询年份
	 * @param month 查询月份
	 * @param storeid 店铺id
	 * @return json格式数据集
	 */
	@ResponseBody
	@RequestMapping("get-total-money-num")
	public Object getTotalMoneyNum(Integer cycle_type,Integer year ,Integer month,Integer storeid){
		return b2b2cOperatorStatisticsManager.getTotalMoneyNum(cycle_type,year,month,storeid);
	}
	/**
	 * load区域分析 下单会员数页面
	 * @return
	 */
	@RequestMapping("get-region-member-html")
	public ModelAndView getRegionMemberHtml(){
		ModelAndView view=new ModelAndView();
		HttpServletRequest request=ThreadContextHolder.getHttpRequest();
		view.addObject("ctx", request.getContextPath());
		view.setViewName("/themes/b2b2cv2/new_store/pages/statistics/get-region-member");
		return view;
	}
	
	/**
	 * load区域分析  会员下单金额页面
	 * @return
	 */
	@RequestMapping("get-region-money-html")
	public ModelAndView getRegionMoneyHtml(){
		ModelAndView view=new ModelAndView();
		HttpServletRequest request=ThreadContextHolder.getHttpRequest();
		view.addObject("ctx", request.getContextPath());
		view.setViewName("/themes/b2b2cv2/new_store/pages/statistics/get-region-money");
		return view;
	}
	/**
	 * load区域分析  下单会员数页面
	 * @return
	 */
	@RequestMapping("get-region-num-html")
	public ModelAndView getRegionNumHtml(){
		ModelAndView view=new ModelAndView();
		HttpServletRequest request=ThreadContextHolder.getHttpRequest();
		view.addObject("ctx", request.getContextPath());
		view.setViewName("/themes/b2b2cv2/new_store/pages/statistics/get-region-num");
		return view;
	}
	
	/**
	 * 获取区域分析 下单会员数  highchart-json格式数据
	 * @param cycle_type 查询周期
	 * @param year 查询年份
	 * @param month 查询月份
	 * @param storeid 店铺id
	 * @return json格式数据集
	 */
	@ResponseBody
	@RequestMapping("region-member-list-json")
	public Object regionMemberListJson(Integer cycle_type,Integer year ,Integer month,Integer storeid){
		try {
			return  b2b2cOperatorStatisticsManager.getRegionStatistics(1,cycle_type,year,month,storeid);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResultUtil.getErrorJson("获取json失败！");
		}
	}
	
	/**
	 * 获取区域分析 会员下单金额 highchart-json格式数据
	 * @param cycle_type 查询周期
	 * @param year 查询年份
	 * @param month 查询月份
	 * @param storeid 店铺id
	 * @return json格式数据集
	 */
	@ResponseBody
	@RequestMapping("region-money-list-json")
	public Object regionMoneyListJson(Integer cycle_type,Integer year ,Integer month,Integer storeid){
		try {
			return  b2b2cOperatorStatisticsManager.getRegionStatistics(2,cycle_type,year,month,storeid);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResultUtil.getErrorJson("获取json失败！");
		}
	}
	/**
	 * 获取区域分析 会员下单数量highchart-json格式数据
	 * @param cycle_type 查询周期
	 * @param year 查询年份
	 * @param month 查询月份
	 * @param storeid 店铺id
	 * @return json格式数据集
	 */
	@ResponseBody
	@RequestMapping("region-num-list-json")
	public Object regionNumListJson(Integer cycle_type,Integer year ,Integer month,Integer storeid){
		try {
			return b2b2cOperatorStatisticsManager.getRegionStatistics(3,cycle_type,year,month,storeid);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResultUtil.getErrorJson("获取json失败！");
		}
	}
	
	/**
	 * 获取购买分析  客单价分布和购买时段分布highchart-json格式数据
	 * @param cycle_type 查询周期
	 * @param year 查询年份
	 * @param month 查询月份
	 * @param storeid 店铺id
	 * @return json格式数据集
	 */
	@ResponseBody
	@RequestMapping("buy-list-json")
	public Object buyListJson(Integer cycle_type,Integer year ,Integer month,Integer storeid){
		return b2b2cOperatorStatisticsManager.getBuyStatistics(cycle_type,year,month,storeid);
	}
	
	/**
	 * 获取购买分析 客单价分布highchart-json格式数据
	 * @param cycle_type 查询周期
	 * @param year 查询年份
	 * @param month 查询月份
	 * @param storeid 店铺id
	 * @param sections 价格区间
	 * @return json格式数据集
	 */
	@ResponseBody
	@RequestMapping("buy-price-json")
	public Object buyPriceListJson(Integer cycle_type,Integer year ,Integer month,Integer storeid,@RequestParam("sections[]")Integer[] sections){
		
		long times[]=StatisticsUtil.getInstance().getStartTimeAndEndTime(cycle_type, year, month);
		
		@SuppressWarnings("rawtypes")
		List list= b2b2cOperatorStatisticsManager.getOrderPriceDis(Arrays.asList(sections), String.valueOf(times[0]), String.valueOf(times[1]), storeid);
	
		return JsonResultUtil.getObjectJson(list);
	}
	
	/**
	 * 导出excel
	 * @param year 查询年份
	 * @param month 查询月份
	 * @param cycle_type 查询周期
	 * @param storeid 店铺id
	 */
	@RequestMapping("export-excel")
	public ModelAndView exportExcel(Integer year,Integer month,Integer cycle_type,Integer storeid){
		this.b2b2cOperatorStatisticsManager.exportExcel(year,month,cycle_type,storeid);
		//return new ModelAndView(, modelName, modelObject)
		return null;
	}
}
