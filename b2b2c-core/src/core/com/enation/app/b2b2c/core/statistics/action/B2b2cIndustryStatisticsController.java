package com.enation.app.b2b2c.core.statistics.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.b2b2c.core.statistics.service.IB2b2cIndustryStatisticsManager;
import com.enation.app.b2b2c.core.store.model.Store;
import com.enation.app.b2b2c.core.store.service.IStoreManager;
import com.enation.app.shop.core.goods.model.Cat;
import com.enation.app.shop.core.goods.service.IGoodsCatManager;
import com.enation.app.shop.core.statistics.model.Collect;
import com.enation.app.shop.core.statistics.service.IIndustryStatisticsManager;
import com.enation.framework.action.GridController;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.JsonMessageUtil;
import com.enation.framework.util.JsonResultUtil;
@Controller
@Scope("prototype")
@RequestMapping("/b2b2c/admin/industryStatistics")
public class B2b2cIndustryStatisticsController extends GridController{
	@Autowired
	private IB2b2cIndustryStatisticsManager b2b2cIndustryStatisticsManager;
	
	@Autowired
	private IGoodsCatManager goodsCatManager;
	
	@Autowired
	private IStoreManager storeManager;
	
	/**
	 * 显示统计主页面
	 * 
	 * @return page
	 */
	@RequestMapping(value="/show-page")
	public ModelAndView showPage() {
		ModelAndView view=new ModelAndView();
		List<Store> storeList=this.storeManager.listAll();
		view.addObject("storeList", storeList);
		view.setViewName("/b2b2c/admin/statistics/industry/index");
		return view;
	}
	
	/**
	 * 显示行业总览界面
	 * 
	 * @return page
	 */
	@RequestMapping(value="/show-collect")
	public ModelAndView showCollect(int cat_id,Integer store_id) {
		ModelAndView view = new ModelAndView();
		List<Cat> cats = this.goodsCatManager.listAllChildren(0);
		view.addObject("cats", cats);
		
		if (cat_id == 0) {
			cat_id = cats.get(0).getCat_id();
		}
		
		List<Store> storeList = this.storeManager.listAll();
		view.addObject("storeList", storeList);
		view.addObject("cat_id", cat_id);
		view.addObject("store_id",store_id==null?0:store_id);
		view.addObject("pageSize", this.getPageSize());
		view.setViewName("/b2b2c/admin/statistics/industry/collect");
		return view;
	}
	
	/**
	 * 总览的表格数据
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/collect-data")
	public GridJsonResult collectData(int cat_id,Integer store_id){
		List<Collect> collects = new ArrayList<Collect>();
		List<Cat> cats = this.goodsCatManager.listAllChildren(0);
		// 如果没有选择。那么显示默认第一个
		if (cat_id == 0) {
			cat_id = cats.get(0).getCat_id();
		}
		try {
			collects = b2b2cIndustryStatisticsManager.listCollect(cat_id,
					goodsCatManager.listAllChildren(cat_id),store_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JsonResultUtil.getGridJson(collects);

	}
	
	/**
	 * 获取价格 统计页面
	 * @param type 查询分类 1：月/2：年
	 * @return page
	 */
	@RequestMapping(value="/industry-price")
	public ModelAndView IndustryPrice(int type, int year, int month,Integer store_id) {
		ModelAndView view = new ModelAndView();
		
		try {
			
			//如果第一次执行。没有时间参数的话，我们赋值现在的时间给查询条件
			if (type == 0) {
				type = 1;
				Date date = new Date();
				year = date.getYear() + 1900;
				month = date.getMonth() + 1;
			}
			
			List<Map> list = this.b2b2cIndustryStatisticsManager.statistics_price(
					type, year, month,store_id);
			view.addObject(JsonMessageUtil.getListJson(list));
			StringBuffer tree = new StringBuffer();
			StringBuffer data = new StringBuffer();
			for (Map map : list) {
				for (Object key : map.keySet()) {
					tree.append("'" + key.toString() + "',");
					data.append("" + map.get(key) + ",");
				}
			}
			ThreadContextHolder.getHttpRequest().setAttribute("tree",
					tree.substring(0, tree.length() - 1));
			ThreadContextHolder.getHttpRequest().setAttribute("data",
					data.substring(0, data.length() - 1));
			if (type == 2) {
				ThreadContextHolder.getHttpRequest().setAttribute("date",
						year + "");
			} else {
				ThreadContextHolder.getHttpRequest().setAttribute("date",
						year + "-" + month);
			}
			
			view.addObject(JsonMessageUtil.getListJson(list));
			view.setViewName("/b2b2c/admin/statistics/industry/industrystatistics_price");
			
		} catch (RuntimeException e) {
			e.printStackTrace();
			this.logger.error("获取数据出错", e);
			view.addObject(JsonResultUtil.getErrorJson("获取数据出错:" + e.getMessage()));
		}
		return view;
	}
	
	/**
	 * 获取商品统计页面
	 * @param type 查询分类 1：月/2：年
	 * @return page
	 */
	@RequestMapping(value="/industry-goods")
	public ModelAndView IndustryGoods(int type, int year, int month,Integer store_id) {
		ModelAndView view = new ModelAndView();
		
		try {
			
			//如果第一次执行。没有时间参数的话，我们赋值现在的时间给查询条件
			if (type == 0) {
				type = 1;
				Date date = new Date();
				year = date.getYear() + 1900;
				month = date.getMonth() + 1;
			}
			
			List<Map> list = this.b2b2cIndustryStatisticsManager.statistics_goods(
					type, year, month,store_id);
			view.addObject(JsonMessageUtil.getListJson(list));
			StringBuffer tree = new StringBuffer();
			StringBuffer data = new StringBuffer();
			for (Map map : list) {
				for (Object key : map.keySet()) {
					tree.append("'" + key.toString() + "',");
					data.append("" + map.get(key) + ",");
				}
			}
			ThreadContextHolder.getHttpRequest().setAttribute("tree",
					tree.substring(0, tree.length() - 1));
			ThreadContextHolder.getHttpRequest().setAttribute("data",
					data.substring(0, data.length() - 1));
			if (type == 2) {
				ThreadContextHolder.getHttpRequest().setAttribute("date",
						year + "");
			} else {
				ThreadContextHolder.getHttpRequest().setAttribute("date",
						year + "-" + month);
			}

			view.setViewName("/b2b2c/admin/statistics/industry/industrystatistics_goods");
			
		} catch (RuntimeException e) {
			e.printStackTrace();
			this.logger.error("获取数据出错", e);
			view.addObject(JsonResultUtil.getErrorJson("获取数据出错:" + e.getMessage()));
		}
		return view;
	}
	
	/**
	 * 获取下单量统计
	 * @param type 查询分类 1：月/2：年
	 * @return page
	 */
	@RequestMapping(value="/industry-order")
	public ModelAndView IndustryOrder(int type, int year, int month,Integer store_id) {
		ModelAndView view = new ModelAndView();
		
		try {
			
			//如果第一次执行。没有时间参数的话，我们赋值现在的时间给查询条件
			if (type == 0) {
				type = 1;
				Date date = new Date();
				year = date.getYear() + 1900;
				month = date.getMonth() + 1;
			}
			
			List<Map> list = this.b2b2cIndustryStatisticsManager.statistics_order(
					type, year, month,store_id);
			view.addObject(JsonMessageUtil.getListJson(list));
			StringBuffer tree = new StringBuffer();
			StringBuffer data = new StringBuffer();
			for (Map map : list) {
				for (Object key : map.keySet()) {
					tree.append("'" + key.toString() + "',");
					data.append("" + map.get(key) + ",");
				}
			}
			ThreadContextHolder.getHttpRequest().setAttribute("tree",
					tree.substring(0, tree.length() - 1));
			ThreadContextHolder.getHttpRequest().setAttribute("data",
					data.substring(0, data.length() - 1));
			if (type == 2) {
				ThreadContextHolder.getHttpRequest().setAttribute("date",
						year + "");
			} else {
				ThreadContextHolder.getHttpRequest().setAttribute("date",
						year + "-" + month);
			}

			view.setViewName("/b2b2c/admin/statistics/industry/industrystatistics_order");
			
		} catch (RuntimeException e) {
			e.printStackTrace();
			this.logger.error("获取数据出错", e);
			view.addObject(JsonResultUtil.getErrorJson("获取数据出错:" + e.getMessage()));
		}
		return view;
	}
	
	/**
	 * 如果第一次执行。没有时间参数的话，我们赋值现在的时间给查询条件
	 */
	private void firstExec(int type, int year, int month) {
		// 如果没有选择类型。那么代表是第一次访问所以没有时间，读取系统时间按照第一种方式查询结果集
		if (type == 0) {
			type = 1;
			Date date = new Date();
			year = date.getYear() + 1900;
			month = date.getMonth() + 1;
		}
	}
}
