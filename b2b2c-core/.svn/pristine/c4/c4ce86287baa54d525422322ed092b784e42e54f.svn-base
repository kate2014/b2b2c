package com.enation.app.b2b2c.core.statistics.service;

import java.util.List;
import java.util.Map;

import com.enation.framework.database.Page;

public interface IB2b2cGoodsStatisticsManager {

	/**
	 * 商品销售明细
	 * @author LSJ
	 * @param start_time 开始时间
	 * @param end_time	结束时间
	 * @param page 当前分页页数
	 * @param pageSize 分页大小
	 * @param cat_id 商品分类ID
	 * @param name 商品名称
	 * @param map 所有商品明细
	 * @param store_id 店铺ID
	 * @return	获取到所有商品明细分页
	 * 2016年12月6日上午11:38
	 */
	Page getgoodsSalesDetail(long start_time, long end_time, int page, int pageSize, Integer cat_id, String name,
			Map map, Integer store_id);

	/**
	 * 价格销售统计
	 * @author LSJ
	 * @param start_time 开始时间
	 * @param end_time 结束时间
	 * @param cat_id 商品类型ID
	 * @param list 价格区间
	 * @param map 价格销售统计集合
	 * @param store_id 店铺ID
	 * @return 获取到数据集
	 * 2016年12月6日上午11:38
	 */
	List getPriceSalesList(long start_time, long end_time, Integer cat_id, List<Map>  list, Map map, Integer store_id);

	/**
	 * 热点商品金额
	 * @author LSJ
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param page 当前页数
	 * @param pageSize 分页大小
	 * @param catid 商品类型ID
	 * @param map 价格销售统计集合
	 * @param store_id 店铺ID
	 * @return 获取数据集分页
	 * 2016年12月6日上午11:38
	 */
	Page getHotGoodsMoney(long startTime, long endTime, int page, int pageSize, Integer catid, Map map, Integer store_id);

	/**
	 * 热卖商品数量
	 * @author LSJ
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param page 当前页数
	 * @param pageSize 分页大小
	 * @param catid 商品类型ID
	 * @param map 热卖商品数量
	 * @param store_id
	 * @return 获取数据集分页
	 * 2016年12月6日上午11:38
	 */
	Page getHotGoodsNum(long startTime, long endTime, int page, int pageSize, Integer catid, Map map, Integer store_id);


	/**
	 * 根据店铺统计商品收藏
	 * @param page	
	 * @param PageSize
	 * @param store		店铺id，0为所有的店铺，ShopApp.self_storeid 为自营店铺
	 * @return
	 */
	public Page getCollectPage(int page,int pageSize,Integer storeId);

}
