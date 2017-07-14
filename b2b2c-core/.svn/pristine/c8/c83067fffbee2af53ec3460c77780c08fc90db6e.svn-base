package com.enation.app.b2b2c.core.store.service.statistics;

import java.util.List;
import java.util.Map;

import com.enation.framework.database.Page;

/**
 * 商品分析manager接口
 * @author xin
 * @version v1.0,2015-12-29
 * @since v1.0
 */
public interface IB2b2cGoodsStatisticsManager {
	
	/**
	 * 获取商品详情
	 * @param 商品分类
	 * @param 商品名称 
	 * @return List<Map> 商品详情list
	 */
	public List<Map<String, Object>> getGoodsDetal(Integer catId,String name);
	
	/**
	 * 获取商品下单金额排行
	 * @param topNum 名次 默认为30
	 * @param startDate 开始时间戳
	 * @param endDate 结束时间戳
	 * @return List<Map> 商品排名
	 */
	public List<Map<String, Object>> getGoodsOrderPriceTop(int topNum, String startDate, String endDate);
	
	/**
	 * 获取下单商品数量排行
	 * @param topNum 名次 默认为30
	 * @param startDate 开始时间戳
	 * @param endDate 结束时间戳
	 * @return List<Map> 商品排名
	 */
	public List<Map<String, Object>> getGoodsNumTop(int topNum, String startDate, String endDate);
	
	/**
	 * 获取商品价格数据
	 * @param sections 区间List  格式：0 100 200 
	 * @param startDate 开始时间戳
	 * @param endDate 结束时间戳
	 * @param catId 商品种类
	 * @param storeid 
	 * @return 商品价格分布List
	 */
	public List<Map<String, Object>> getGoodsPriceSales(List<Integer> sections, String startDate, String endDate,Integer catId, Integer storeid);
	
	
	
}
