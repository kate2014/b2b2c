package com.enation.app.b2b2c.core.statistics.action;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.b2b2c.core.statistics.service.IB2b2cMemberStatisticsManager;
import com.enation.app.b2b2c.core.store.model.Store;
import com.enation.app.b2b2c.core.store.service.IStoreManager;
import com.enation.app.shop.core.statistics.service.IMemberStatisticsManager;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.JsonMessageUtil;
import com.enation.framework.util.JsonResultUtil;

@Controller
@Scope("prototype")
@RequestMapping("/b2b2c/admin/memberStatistics")
public class B2b2cMemberStatisticsController {
	protected Logger logger=Logger.getLogger(getClass());
	
	@Autowired
	private IB2b2cMemberStatisticsManager b2b2cMemberStatisticsManager;

	@Autowired
	private IStoreManager storeManager;
	/**
	 * 获得会员分析页
	 * 
	 * @return result name
	 */
	@RequestMapping(value="/member-analysis-html")
	public ModelAndView memberAnalysisHtml() {
		ModelAndView view=new ModelAndView();
		List<Store> storeList=this.storeManager.listAll();
		view.addObject("storeList",storeList);
		view.setViewName("/b2b2c/admin/statistics/member/member_analysis");
		return view;
	}
	
	/**
	 * 获取下单量统计页
	 * @return result name
	 */
	@RequestMapping(value="/order-num-statistics-html")
	public String orderNumStatisticsHtml(){
		return "/b2b2c/admin/statistics/member/order_num_statistics";
	}
	
	/**
	 * 获取下单商品件数统计页
	 * @return result name
	 */
	@RequestMapping(value="/goods-num-statistics-html")
	public String goodsNumStatisticsHtml(){
		
		return "/b2b2c/admin/statistics/member/goods_num_statistics";
	}
	

	/**
	 * 获取下单金额统计页
	 * @return result name
	 */
	@RequestMapping(value="/order-price-statistics-html")
	public String orderPriceStatisticsHtml(){
		
		return "/b2b2c/admin/statistics/member/order_price_statistics";
	}

	
	/**
	 * 获取购买分析html页
	 * @return result name
	 */
	@RequestMapping(value="/buy-analysis-html")
	public ModelAndView buyAnalysisHtml(){
		ModelAndView view = new ModelAndView();
		List<Store> storeList =  this.storeManager.listAll();
		view.addObject("storeList", storeList);
		view.setViewName("/b2b2c/admin/statistics/member/buy_analysis");
		return view;
	}
	
	/**
	 * 获得新增会员
	 * @return result name
	 */
	@RequestMapping(value="/add-member-num-html")
	public ModelAndView addMemberNumHtml(){
		ModelAndView view=new ModelAndView();
		List<Store> storeList=this.storeManager.listAll();
		view.addObject("storeList",storeList);
		view.setViewName("/b2b2c/admin/statistics/member/add_member");
		return view;
	}
	
	/**
	 * 获取会员下单排行
	 * 
	 * @author Sylow
	 * @param top_num
	 *            排名名次 <b>必填</b>
	 * @param start_date 开始时间 可为空
	 * @param start_date 结束时间 可为空
	 * @return Json格式的字符串 result = 1 代表成功 否则失败
	 */
	@ResponseBody
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/get-order-num-top")
	public Object getOrderNumTop(String start_date,String end_date,String store_id) {
		try {
			/**
			 * 排名名次 默认10
			 */
			Integer top_num = 10;
			
			
			String startDateStamp = "";		//开始时间戳
			String endDateStamp = "";		//结束时间戳
			
			// 1.判断并赋值
			if (start_date != null && !"".equals(start_date)) {
				startDateStamp = String.valueOf(DateUtil.getDateline(start_date, "yyyy-MM-dd HH:mm:ss"));
			}
			if (end_date != null && !"".equals(end_date)) {
				endDateStamp = String.valueOf(DateUtil.getDateline(end_date, "yyyy-MM-dd HH:mm:ss"));
			}
			
			Map map = new HashMap();
			map.put("start_date", startDateStamp);
			
			// 2.获取数据
			List<Map<String, Object>> list = this.b2b2cMemberStatisticsManager.getOrderNumTop(top_num, startDateStamp, endDateStamp,store_id);
			
			return JsonMessageUtil.getListJson(list);

		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error("获取用户下单排行出错", e);
			return JsonResultUtil.getErrorJson("获取用户下单排行出错:" + e.getMessage());
		}
	}

	/**
	 * 获取会员下单商品排行
	 * 
	 * @author Sylow
	 * @param top_num
	 *            排名名次 <b>必填</b>
	 * @param start_date 开始时间 可为空
	 * @param start_date 结束时间 可为空
	 * @return Json格式的字符串 result = 1 代表成功 否者失败
	 */
	@ResponseBody
	@RequestMapping("/get-goods-num-top")
	public Object getGoodsNumTop(String start_date,String end_date,String store_id) {
		try {

			/**
			 * 排名名次 默认10
			 */
			Integer top_num = 10;
			
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
			List<Map<String, Object>> list = this.b2b2cMemberStatisticsManager.getGoodsNumTop(top_num, startDateStamp, endDateStamp,store_id);

			return JsonMessageUtil.getListJson(list);
		} catch (RuntimeException e) {
			e.printStackTrace();
			this.logger.error("获取用户下单商品数量排行出错", e);
			return JsonResultUtil.getErrorJson("获取用户下单商品数量排行出错:" + e.getMessage());
		}
	}
	
	/**
	 * 获取会员下单总金额排行
	 * 
	 * @author Sylow
	 * @param top_num
	 *            排名名次 <b>必填</b>
	 * @param start_date 开始时间 可为空
	 * @param start_date 结束时间 可为空
	 * @return Json格式的字符串 result = 1 代表成功 否者失败
	 */
	@ResponseBody
	@RequestMapping("/get-order-price-top")
	public Object getOrderPriceTop(String start_date,String end_date,String store_id) {
		try {
			
			/**
			 * 排名名次 默认10
			 */
			Integer top_num = 10;
			
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
			List<Map<String, Object>> list = this.b2b2cMemberStatisticsManager.getOrderPriceTop(top_num, startDateStamp, endDateStamp,store_id);
			return JsonMessageUtil.getListJson(list);
		} catch (RuntimeException e) {
			e.printStackTrace();
			this.logger.error("获取用户下单总额排行出错", e);
			
			return JsonResultUtil.getErrorJson("获取用户下单总额排行出错:" + e.getMessage());
		}
	}
	
	/**
	 * 获取客单价分布数据
	 * 
	 * @author Sylow
	 * @param start_date 开始时间 可为空
	 * @param start_date 结束时间 可为空
	 * @return Json格式的字符串 result = 1 代表成功 否者失败
	 */
	@ResponseBody
	@RequestMapping("/get-order-price-dis")
	public Object getOrderPriceDis(String start_date,String end_date, Integer[] sections,String store_id) {
		try {
			
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
			List<Map<String, Object>> list = this.b2b2cMemberStatisticsManager.getOrderPriceDis(Arrays.asList(sections), startDateStamp, endDateStamp,store_id);
			return JsonMessageUtil.getListJson(list);
		} catch (RuntimeException e) {
			this.logger.error("获取用户客单价分布数据出错", e);
			return JsonResultUtil.getErrorJson("获取用户客单价分布数据出错:" + e.getMessage());
		}
	}
	
	/**
	 * 获取用户购买频次数据
	 * @author Sylow
	 * @param start_date 开始时间 可为空
	 * @param start_date 结束时间 可为空
	 * @return Json格式的字符串 result = 1 代表成功 否者失败
	 */
	@ResponseBody
	@RequestMapping("/get-buy-fre")
	public Object getBuyFre(String start_date,String end_date,String store_id) {
		try {
			
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
			List<Map<String, Object>> list = this.b2b2cMemberStatisticsManager.getBuyFre(startDateStamp, endDateStamp,store_id);
			
			return JsonMessageUtil.getListJson(list);
		} catch (RuntimeException e) {
			e.printStackTrace();
			this.logger.error("获取用户购买频次出错", e);
			return JsonResultUtil.getErrorJson("获取用户购买频次出错:" + e.getMessage());
		}
	}
	
	/**
	 * 获取用户购买时段分布数据
	 * @author Sylow
	 * @param start_date 开始时间 可为空
	 * @param start_date 结束时间 可为空
	 * @return Json格式的字符串 result = 1 代表成功 否者失败
	 */
	@ResponseBody
	@RequestMapping("/get-buy-time-dis")
	public Object getBuyTimeDis(String start_date,String end_date,String store_id) {
		try {
			
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
			List<Map<String, Object>> list = this.b2b2cMemberStatisticsManager.getBuyTimeDis(startDateStamp, endDateStamp,store_id);
			return JsonMessageUtil.getListJson(list);
		} catch (RuntimeException e) {
			e.printStackTrace();
			this.logger.error("获取用户购买时段分布出错", e);
			return JsonResultUtil.getErrorJson("获取用户购买时段分布出错:" + e.getMessage());
		}
	}


	/**
	 * 获得新增会员
	 * 
	 * @author whj
	 * @param start_date 开始时间
	 * @param lastStart_date 上月开始时间
	 * @param type 搜索类型  如果是2，按年搜索，
	 * @return Json格式的字符串 result = 1 代表成功 否则失败
	 */
	@ResponseBody
	@RequestMapping("/get-add-member-num")
	public Object getAddMemberNum(String start_date,String end_date , String lastStart_date,String lastEnd_date,Integer type) {
		try {
			
			String startDateStamp = "";		//本月开始时间戳
			String endDateStamp = "";		//本月结束时间戳
			String lastStartDateStamp = "";		//本月开始时间戳
			String lastEndDateStamp = "";		//本月结束时间戳
			
			// 1.判断并赋值
			if (start_date != null && !"".equals(start_date)) {
				startDateStamp = String.valueOf(DateUtil.getDateline(start_date,"yyyy-MM-dd HH:mm:ss"));
			}
			if (end_date != null && !"".equals(end_date)) {
				endDateStamp = String.valueOf(DateUtil.getDateline(end_date,"yyyy-MM-dd HH:mm:ss"));
			}
			
			if (lastStart_date != null && !"".equals(lastStart_date)) {
				lastStartDateStamp = String.valueOf(DateUtil.getDateline(lastStart_date,"yyyy-MM-dd HH:mm:ss"));
			}
			if (lastEnd_date != null && !"".equals(lastEnd_date)) {
				lastEndDateStamp = String.valueOf(DateUtil.getDateline(lastEnd_date,"yyyy-MM-dd HH:mm:ss"));
			}
			
			if(type==2){
				// 2.获取本月数据
				List<Map<String, Object>> list = this.b2b2cMemberStatisticsManager.getAddYearMemberNum(startDateStamp, endDateStamp);

				// 3.获取上月数据
				List<Map<String, Object>> lastList = this.b2b2cMemberStatisticsManager.getLastAddYearMemberNum(lastStartDateStamp, lastEndDateStamp);
				//4.放到map中
				Map result = new HashMap();
				result.put("list", list);
				result.put("lastList", lastList);
				return JsonMessageUtil.getObjectJson(result);
			}else{
				// 2.获取本月数据
				List<Map<String, Object>> list = this.b2b2cMemberStatisticsManager.getAddMemberNum(startDateStamp, endDateStamp);

				// 3.获取上月数据
				List<Map<String, Object>> lastList = this.b2b2cMemberStatisticsManager.getLastAddMemberNum(lastStartDateStamp, lastEndDateStamp);
				//4.放到map中
				Map result = new HashMap();
				result.put("list", list);
				result.put("lastList", lastList);
				return JsonMessageUtil.getObjectJson(result);
			}
			
			

		} catch (RuntimeException e) {
			e.printStackTrace();
			this.logger.error("获取用户数据出错", e);
			return JsonResultUtil.getErrorJson("获取用户数据出错:" + e.getMessage());
		}
	}
}
