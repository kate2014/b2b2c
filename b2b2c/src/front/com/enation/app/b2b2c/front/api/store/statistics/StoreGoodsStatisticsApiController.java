package com.enation.app.b2b2c.front.api.store.statistics;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.b2b2c.core.store.service.statistics.IB2b2cGoodsStatisticsManager;
import com.enation.app.b2b2c.core.store.service.statistics.StatisticsUtil;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.JsonMessageUtil;
import com.enation.framework.util.JsonResultUtil;

/**
 * 销售统计 
 * @author DMRain 2016年3月5日 版本改造
 * @version v2.0 改为spring mvc
 * @since v6.0
 */
@Controller
@Scope("prototype")
@RequestMapping("/api/goods-statistics")
public class StoreGoodsStatisticsApiController {

	protected final Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private IB2b2cGoodsStatisticsManager b2b2cGoodsStatisticsManager;
	
	@RequestMapping(value="get-hotsales-money-html")
	public ModelAndView getHotsalesMoneyHtml(){
		ModelAndView view=new ModelAndView();
		HttpServletRequest request=ThreadContextHolder.getHttpRequest();
		view.addObject("ctx", request.getContextPath());
		view.setViewName("/themes/b2b2cv2/new_store/pages/statistics/get-hotsales-money");
		return view;
	}
	
	
	@RequestMapping(value="get-hotsales-num-html")
	public ModelAndView getHotsalesNumHtml(){
		ModelAndView view=new ModelAndView();
		HttpServletRequest request=ThreadContextHolder.getHttpRequest();
		view.addObject("ctx", request.getContextPath());
		view.setViewName("/themes/b2b2cv2/new_store/pages/statistics/get-hotsales-num");
		return view;
	}
	
	/**
	 * 获取商品销售明细
	 * @param cat_id 商品类
	 * @param name   商品名称
	 * @return Json格式的字符串 result = 1 代表成功 否者失败
	 */
	@ResponseBody
	@RequestMapping(value="/get-goods-sales-detail")
	public Object getGoodsSalesDetail(Integer cat_id, String name) {
		try {			
			// 获取数据
			List<Map<String, Object>> list = this.b2b2cGoodsStatisticsManager.getGoodsDetal(cat_id, name);
			return JsonMessageUtil.getListJson(list);
		} catch (RuntimeException e) {
			e.printStackTrace();
			this.logger.error("获取商品下单总额排行出错", e);
			return JsonResultUtil.getErrorJson("获取商品下单总额排行出错:" + e.getMessage());
		}
	}
	
	/**
	 * 获取商品下单总金额排行
	 * @param top_num
	 * @param start_date 开始时间 可为空
	 * @param end_date 结束时间 可为空
	 * @return Json格式的字符串 result = 1 代表成功 否者失败
	 */
	@ResponseBody
	@RequestMapping(value="/get-goods-order-price-top")
	public Object getGoodsOrderPriceTop(String start_date, String end_date) {
		try {
			
			Integer top_num = 30;			//排名名次 默认30
			String startDateStamp = "";		//开始时间戳
			String endDateStamp = "";		//结束时间戳
			
			// 1.判断并赋值
			if (start_date != null && !"".equals(start_date)) {
				startDateStamp = String.valueOf(DateUtil.getDateline(start_date, "yyyy-MM-dd HH:mm:ss"));
			}
			if (end_date != null && !"".equals(end_date)) {
				endDateStamp = String.valueOf(DateUtil.getDateline(end_date, "yyyy-MM-dd HH:mm:ss"));
			}
			
			// 2.获取数据
			List<Map<String, Object>> list = this.b2b2cGoodsStatisticsManager.getGoodsOrderPriceTop(top_num, startDateStamp, endDateStamp);
			return JsonMessageUtil.getListJson(list);
		} catch (RuntimeException e) {
			e.printStackTrace();
			this.logger.error("获取商品下单总额排行出错", e);
			return JsonResultUtil.getErrorJson("获取商品下单总额排行出错:" + e.getMessage());
		}
	}
	
	/**
	 * 获取下单商品排行
	 * @param top_num
	 *            排名名次 <b>必填</b>
	 * @param start_date 开始时间 可为空
	 * @param start_date 结束时间 可为空
	 * @return Json格式的字符串 result = 1 代表成功 否者失败
	 */
	@ResponseBody
	@RequestMapping(value="/get-goods-num-top")
	public Object getGoodsNumTop(String start_date, String end_date) {
		try {

			Integer top_num = 30;			//排名名次 默认30
			String startDateStamp = "";		//开始时间戳
			String endDateStamp = "";		//结束时间戳
			
			// 1.判断并赋值
			if (start_date != null && !"".equals(start_date)) {
				startDateStamp = String.valueOf(DateUtil.getDateline(start_date, "yyyy-MM-dd HH:mm:ss"));
			}
			if (end_date != null && !"".equals(end_date)) {
				endDateStamp = String.valueOf(DateUtil.getDateline(end_date, "yyyy-MM-dd HH:mm:ss"));
			}
			
			// 2.获取数据
			List<Map<String, Object>> list = this.b2b2cGoodsStatisticsManager.getGoodsNumTop(top_num, startDateStamp, endDateStamp);

			return JsonMessageUtil.getListJson(list);
		} catch (RuntimeException e) {
			e.printStackTrace();
			this.logger.error("获取下单商品数量排行出错", e);
			return JsonResultUtil.getErrorJson("获取下单商品数量排行出错:" + e.getMessage());
		}
	}
	
	/**
	 * 获取价格销量分析json数据
	 * @param cycle_type
	 * @param year
	 * @param month
	 * @param storeid
	 * @param sections
	 * @return
	 */
	@ResponseBody
	@RequestMapping("get-goods-price-sales")
	public Object getGoodsPriceSales(Integer cycle_type,Integer year ,Integer month,Integer storeid,Integer[] sections,Integer catid){
		
		long times[]=StatisticsUtil.getInstance().getStartTimeAndEndTime(cycle_type, year, month);
		
		@SuppressWarnings("rawtypes")
		List list= b2b2cGoodsStatisticsManager.getGoodsPriceSales(Arrays.asList(sections), String.valueOf(times[0]), String.valueOf(times[1]), catid,storeid);
	
		return JsonResultUtil.getObjectJson(list);
	}
	
	/**
	 * 获得热卖商品分析页
	 * @return result name
	 */
	@RequestMapping(value="/hot-sales-goods-html")
	public String hotSalesGoodsHtml() {
		return "/themes/b2b2cv2/store/statistics/goodsanalysis/hot_sales_goods";
	}
	
	/**
	 * 获取下单商品件数统计页
	 * @return result name
	 */
	@RequestMapping(value="/goods-num-statistics-html")
	public String goodsNumStatisticsHtml(){
		return "/themes/b2b2cv2/store/statistics/goodsanalysis/goods_num_statistics";
	}
	
	/**
	 * 获取下单金额统计页
	 * @return result name
	 */
	@RequestMapping(value="/goods-order-price-statistics-html")
	public String goodsOrderPriceStatisticsHtml(){
		return "/themes/b2b2cv2/store/statistics/goodsanalysis/hot_goods_money";
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
