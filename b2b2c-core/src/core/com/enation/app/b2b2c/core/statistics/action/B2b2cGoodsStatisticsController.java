package com.enation.app.b2b2c.core.statistics.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.b2b2c.core.statistics.service.IB2b2cGoodsStatisticsManager;
import com.enation.app.b2b2c.core.store.model.Store;
import com.enation.app.b2b2c.core.store.service.IStoreManager;
import com.enation.app.shop.core.goods.service.IGoodsCatManager;
import com.enation.app.shop.core.statistics.service.IGoodsStatisticsManager;
import com.enation.framework.action.GridController;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.action.JsonResult;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.JsonMessageUtil;
import com.enation.framework.util.JsonResultUtil;
import com.enation.framework.util.StringUtil;

@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
@Controller
@Scope("prototype")
@RequestMapping("/b2b2c/admin/goodsStatis")
public class B2b2cGoodsStatisticsController extends GridController{

	@Autowired
	private IB2b2cGoodsStatisticsManager b2b2cBackendGoodsStatisticsManager;

	@Autowired
	private IStoreManager storeManager;
	
	@Autowired
	private IGoodsCatManager goodsCatManager;

	/**
	 * 跳转价格销量展示页
	 * @return
	 */
	@RequestMapping(value="/price-sales")
	public ModelAndView priceSales(){
		ModelAndView view = new ModelAndView();
		List<Store> storeList = this.storeManager.listAll();
		view.addObject("storeList",storeList);
		view.setViewName("/b2b2c/admin/statistics/goodsanalysis/price_sales_list");
		return view;
	}

	/**
	 * 读取价格销量统计的json
	 * @param cycle_type 周期模式 1为月，反之则为年
	 * @param year 年
	 * @param month	月
	 * @param cat_id 商品分类ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/get-price-sales-json")
	public Object getPriceSalesJson(Integer cycle_type, Integer year, Integer month, Integer cat_id,Integer store_id){

		HttpServletRequest request =  ThreadContextHolder.getHttpRequest();
		String minprices = request.getParameter("minprice");		//最小价格的数据
		String maxprices = request.getParameter("maxprice");		//最大价格的数据

		String [] minp = minprices.split(",");
		String [] maxp = maxprices.split(",");

		//把数据存放在list
		List list = new ArrayList();
		int length=0;
		if(minp.length>maxp.length){
			length=maxp.length;
		}else{
			length=minp.length;
		}
		for(int i =0;i<length;i++){
			Map map = new HashMap();
			if(!StringUtil.isEmpty(minp[i])&&!StringUtil.isEmpty(maxp[i])){
				String minprice="";
				String maxprice="";
				
				if(StringUtil.toInt(minp[i],0)>StringUtil.toInt(maxp[i],0)){
					minprice=maxp[i];
					maxprice=minp[i];
				}else{
					minprice=minp[i];
					maxprice=maxp[i];
				}
				map.put("minprice", minprice);
				map.put("maxprice", maxprice);
				list.add(map);
			}
		}

		if(cycle_type==null){
			cycle_type=1;
		}
		Calendar cal = Calendar.getInstance();
		if(year==null){
			year = cal.get(Calendar.YEAR);
		}
		if(month==null){
			month = cal.get(Calendar.MONTH )+1;
		}

		//当cat_id为空时，为其赋值0，目的是查询全部分类！ 修改人:DMRain 2015-12-07
		if(cat_id == null){
			cat_id = 0;
		}

		List data_list = new ArrayList();
		if(cycle_type.intValue()==1){

			int day = getDaysByYearMonth(year, month);
			long start_time = DateUtil.getDateline(year+"-"+month+"-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
			long end_time = DateUtil.getDateline(year+"-"+month+"-"+day+" 23:59:59", "yyyy-MM-dd HH:mm:ss");
			//System.out.println(start_time+"___"+end_time);
			data_list = this.b2b2cBackendGoodsStatisticsManager.getPriceSalesList(start_time, end_time, cat_id, list, null,store_id);

		}else{

			long start_time = DateUtil.getDateline(year+"-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
			long end_time = DateUtil.getDateline(year+"-12-31 23:59:59", "yyyy-MM-dd HH:mm:ss");  
			data_list = this.b2b2cBackendGoodsStatisticsManager.getPriceSalesList(start_time, end_time, cat_id, list, null,store_id);
		}

		return JsonMessageUtil.getListJson(data_list);
	}

	/**
	 * 跳转热卖商品页
	 * @param cycle_type 周期模式 1为月，反之则为年
	 * @param year 年
	 * @param month	月
	 * @param cat_id 商品分类ID
	 * @param statis_type 统计模式  1为下单金额，反之为下单量
	 * @return
	 */
	@RequestMapping(value="/hot-goods")
	public ModelAndView hotgoods(Integer cycle_type, Integer year, Integer month, Integer cat_id, Integer statis_type){
		ModelAndView view = new ModelAndView();

		if(cycle_type==null){
			cycle_type=1;
		}
		Calendar cal = Calendar.getInstance();
		if(year==null){
			year = cal.get(Calendar.YEAR);
		}
		if(month==null){
			month = cal.get(Calendar.MONTH )+1;
		}
		if(cat_id==null){
			cat_id=0;
		}

		view.addObject("cycle_type", cycle_type);
		view.addObject("year", year);
		view.addObject("month", month);
		List<Store> storeList=this.storeManager.listAll();
		view.addObject("storeList",storeList);

		if(statis_type==null || statis_type==1){	//下单金额

			Page moneyPage = getHotGoodsMoneyList(cycle_type, year, month, cat_id, null,0);
			List<Map> moneyList = (List) moneyPage.getResult();
			List money_list = new ArrayList();
			for (Map map : moneyList) {
				Map money_map = new HashMap();
				money_map.put("name", map.get("oiname"));
				money_map.put("y", map.get("t_money"));
				money_list.add(money_map);
			}

			view.addObject("money_json", JSONArray.fromObject(money_list).toString());
			view.setViewName("/b2b2c/admin/statistics/goodsanalysis/hot_goods_money");
			return view;
		}else{

			Page numPage = getHotGoodsNumList(cycle_type, year, month, cat_id, null,0);
			List<Map> numList = (List) numPage.getResult();
			List num_list = new ArrayList();
			for (Map map : numList) {
				Map money_map = new HashMap();
				money_map.put("name", map.get("oiname"));
				money_map.put("y", map.get("t_num"));
				num_list.add(money_map);
			}

			view.addObject("num_json", JSONArray.fromObject(num_list).toString());
			view.setViewName("/b2b2c/admin/statistics/goodsanalysis/hot_goods_num");
			return view;
		}
	}

	/**
	 * 获取热卖商品金额json
	 * @param cycle_type 周期模式 1为月，反之则为年
	 * @param year 年
	 * @param month	月
	 * @param cat_id 商品分类ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/get-hot-goods-money-json")
	public JsonResult getHotGoodsMoneyJson(Integer cycle_type, Integer year, Integer month, Integer cat_id,Integer store_id){
		if(cycle_type==null){
			cycle_type=1;
		}
		Calendar cal = Calendar.getInstance();
		if(year==null){
			year = cal.get(Calendar.YEAR);
		}
		if(month==null){
			month = cal.get(Calendar.MONTH )+1;
		}
		Page moneyPage = this.getHotGoodsMoneyList(cycle_type, year, month, cat_id, null,store_id);
		List<Map> moneyList = (List) moneyPage.getResult();
		List money_list = new ArrayList();
		for (Map map : moneyList) {
			Map money_map = new HashMap();
			money_map.put("name", map.get("oiname"));
			money_map.put("y", map.get("t_money"));
			money_list.add(money_map);
		}
		GridJsonResult gridJson= JsonResultUtil.getGridJson(moneyPage);//得到grid格式的json

		Map<String,Object> jsonMap=new HashMap<String,Object>();
		jsonMap.put("gridjson", gridJson);
		jsonMap.put("highchartsjson", money_list);
		return JsonResultUtil.getObjectJson(jsonMap);
	}

	/**
	 * 获取热卖商品数量的grid-json和highcharts-json
	 * @param cycle_type
	 * @param year
	 * @param month
	 * @param cat_id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/get-hot-goods-num-json")
	public JsonResult getHotGoodsNumJson(Integer cycle_type, Integer year, Integer month, Integer cat_id,Integer store_id){
		if(cycle_type==null){
			cycle_type=1;
		}
		Calendar cal = Calendar.getInstance();
		if(year==null){
			year = cal.get(Calendar.YEAR);
		}
		if(month==null){
			month = cal.get(Calendar.MONTH )+1;
		}
		Page numPage = this.getHotGoodsNumList(cycle_type, year, month, cat_id, null,store_id);
		GridJsonResult gridJson= JsonResultUtil.getGridJson(numPage);//得到grid格式的json

		Map<String,Object> jsonMap=new HashMap<String,Object>();
		jsonMap.put("gridjson", gridJson);

		List<Map> numList = (List) numPage.getResult();
		List num_list = new ArrayList();
		for (Map map : numList) {
			Map money_map = new HashMap();
			money_map.put("name", map.get("oiname"));
			money_map.put("y", map.get("t_num"));
			num_list.add(money_map);
		}
		jsonMap.put("highchartsjson", num_list);

		return JsonResultUtil.getObjectJson(jsonMap);
	}
	/**
	 * 获取热卖商品金额的grid-json和highcharts-json
	 * @param cycle_type
	 * @param year
	 * @param month
	 * @param cat_id
	 * @return
	 */

	@RequestMapping(value="/get-hot-goods-money")
	public String getHotGoodsMoney(Integer cycle_type, Integer year, Integer month, Integer cat_id){


		return "/b2b2c/admin/statistics/goodsanalysis/get-hot-goods-money";

	}





	
	@RequestMapping(value="/get-hot-goods-num")
	public String getHotGoodsNum(Integer cycle_type, Integer year, Integer month, Integer cat_id){
		
		return "/b2b2c/admin/statistics/goodsanalysis/get-hot-goods-num";
	}
	
	/**
	 * 商品销售明细
	 * @param cycle_type 周期模式 1为月，反之则为年
	 * @param year 年
	 * @param month	月
	 * @return
	 */
	@RequestMapping(value="/goods-sales-detail")
	public ModelAndView goodsSalesDetail(Integer cycle_type, Integer year, Integer month){
		ModelAndView view = new ModelAndView();

		if(cycle_type==null){
			cycle_type=1;
		}
		view.addObject("cycle_type", cycle_type);

		Calendar cal = Calendar.getInstance();

		if(year==null){
			year = cal.get(Calendar.YEAR);
		}
		view.addObject("year", year);

		if(month==null){
			month = cal.get(Calendar.MONTH )+1;
		}
		view.addObject("month", month);

		List<Store> storeList=this.storeManager.listAll();
		view.addObject("storeList", storeList);
		
		view.setViewName("/b2b2c/admin/statistics/goodsanalysis/goods_sales_detail");
		return view;
	}

	/**
	 * 商品销售明细的json
	 * @param cycle_type 周期模式 1为月，反之则为年
	 * @param year 年
	 * @param month	月
	 * @param cat_id 商品分类ID
	 * @param name 商品名称
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/goods-sales-detail-json")
	public GridJsonResult goodsSalesDetailJson(Integer cycle_type, Integer year, Integer month, Integer cat_id, String name,Integer store_id){
		if(cycle_type==null){
			cycle_type=1;
		}
		Calendar cal = Calendar.getInstance();
		if(year==null){
			year = cal.get(Calendar.YEAR);
		}
		if(month==null){
			month = cal.get(Calendar.MONTH )+1;
		}

		Page webpage = new Page();

		if(cycle_type.intValue()==1){

			int day = getDaysByYearMonth(year, month);
			long start_time = DateUtil.getDateline(year+"-"+month+"-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
			long end_time = DateUtil.getDateline(year+"-"+month+"-"+day+" 23:59:59", "yyyy-MM-dd HH:mm:ss");
			webpage = this.b2b2cBackendGoodsStatisticsManager.getgoodsSalesDetail(start_time, end_time, this.getPage(), this.getPageSize(), cat_id, name, null,store_id);

		}else{

			long start_time = DateUtil.getDateline(year+"-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
			long end_time = DateUtil.getDateline(year+"-12-31 23:59:59", "yyyy-MM-dd HH:mm:ss");  
			webpage = this.b2b2cBackendGoodsStatisticsManager.getgoodsSalesDetail(start_time, end_time, this.getPage(), this.getPageSize(), cat_id, name, null,store_id);
		}
		return JsonResultUtil.getGridJson(webpage);
	}
	
	/**
	 * 读取收藏统计列表
	 * @return
	 */
	@RequestMapping(value="/get-collect-list")
	public ModelAndView getCollectList(Integer storeId){
		Page webPage =  this.b2b2cBackendGoodsStatisticsManager.getCollectPage(this.getPage(), 50, storeId);
		ModelAndView view = new ModelAndView();
		List list = (List) webPage.getResult();
		
		List<Store> storeList = this.storeManager.listAll();
		view.addObject("storeList",storeList);
		view.addObject("collectJosn", JSONArray.fromObject(list).toString());
		view.setViewName("/b2b2c/admin/statistics/goodsanalysis/goods-collect");
		return view;
	}
	
	/**
	 * 读取收藏甘特图Json
	 * @param storeId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/get-collect-chart-json")
	public String getCollectChartJson(Integer storeId){
		Page webPage =  this.b2b2cBackendGoodsStatisticsManager.getCollectPage(this.getPage(), 50, storeId);
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

	/**
	 * 热卖商品金额top list
	 * @param cycle_type
	 * @param year
	 * @param month
	 * @param cat_id
	 * @param map
	 * @param store_id 
	 * @return
	 */
	public Page getHotGoodsMoneyList(Integer cycle_type, int year,int month,Integer cat_id,Map map, Integer store_id){
		List moneyList = new ArrayList();
		Page moneyPage = new Page();

		int rows = 50;

		if(cycle_type.intValue()==1){

			int day = getDaysByYearMonth(year, month);
			long start_time = DateUtil.getDateline(year+"-"+month+"-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
			long end_time = DateUtil.getDateline(year+"-"+month+"-"+day+" 23:59:59", "yyyy-MM-dd HH:mm:ss");
			moneyPage = this.b2b2cBackendGoodsStatisticsManager.getHotGoodsMoney(start_time, end_time, this.getPage(), this.getPageSize(), cat_id, map,store_id);

		}else{

			long start_time = DateUtil.getDateline(year+"-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
			long end_time = DateUtil.getDateline(year+"-12-31 23:59:59", "yyyy-MM-dd HH:mm:ss");  

			moneyPage = this.b2b2cBackendGoodsStatisticsManager.getHotGoodsMoney(start_time, end_time, this.getPage(), this.getPageSize(), cat_id, map,store_id);
		}
		return moneyPage;
	}
	
	/**
	 * 热卖商品数量top list
	 * @param cycle_type
	 * @param year
	 * @param month
	 * @param cat_id
	 * @param map
	 * @param store_id 
	 * @return
	 */
	public Page getHotGoodsNumList(Integer cycle_type, int year,int month,Integer cat_id,Map map, Integer store_id){
		List numList = new ArrayList();
		Page numPage = new Page();

		int rows = 50;
		if(cycle_type.intValue()==1){

			int day = getDaysByYearMonth(year, month);
			long start_time = DateUtil.getDateline(year+"-"+month+"-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
			long end_time = DateUtil.getDateline(year+"-"+month+"-"+day+" 23:59:59", "yyyy-MM-dd HH:mm:ss");
			numPage = this.b2b2cBackendGoodsStatisticsManager.getHotGoodsNum(start_time, end_time, this.getPage(), this.getPageSize(), cat_id, map,store_id);

		}else{

			long start_time = DateUtil.getDateline(year+"-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
			long end_time = DateUtil.getDateline(year+"-12-31 23:59:59", "yyyy-MM-dd HH:mm:ss");  

			numPage = this.b2b2cBackendGoodsStatisticsManager.getHotGoodsNum(start_time, end_time, this.getPage(), this.getPageSize(), cat_id, map,store_id);
		}
		return numPage;
	}

	//获取当前年月的最大的天数
	public int getDaysByYearMonth(int year, int month) {  
		Calendar a = Calendar.getInstance();  
		a.set(Calendar.YEAR, year);  
		a.set(Calendar.MONTH, month - 1);  
		a.set(Calendar.DATE, 1);  
		a.roll(Calendar.DATE, -1);  
		int maxDate = a.get(Calendar.DATE);  
		return maxDate;  
	}
}
