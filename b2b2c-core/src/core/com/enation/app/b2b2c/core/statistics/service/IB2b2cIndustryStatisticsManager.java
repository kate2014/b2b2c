package com.enation.app.b2b2c.core.statistics.service;

import java.util.List;
import java.util.Map;

import com.enation.app.shop.core.goods.model.Cat;
import com.enation.app.shop.core.statistics.model.Collect;

public interface IB2b2cIndustryStatisticsManager {

	/**
	 * 概括总览表
	 * @author LSJ
	 * @param cat_id 商品类型ID
	 * @param listAllChildren  获取某个类别的所有子类，所有的子孙
	 * @param store_id 店铺ID
	 * @return 总览表 List
	 * 2016年12月6日上午11:38
	 */
	List<Collect> listCollect(int cat_id, List<Cat> listAllChildren, Integer store_id);

	/**
	 * 行业下单金额统计
	 * @author LSJ
	 * @param type 查询  按[1 表示：年] 或 [2表示：月]
	 * @param year 年
	 * @param month 月
	 * @param store_id 店铺ID
	 * @return 下单金额统计 list
	 * 2016年12月6日上午11:38
	 */
	List<Map> statistics_price(int type, int year, int month, Integer store_id);

	/**
	 * 下单商品数量统计
	 * @author LSJ
	 * @param type 查询  按[1 表示：年] 或 [2表示：月]
	 * @param year 年
	 * @param month 月
	 * @param store_id 店铺ID
	 * @return 下单商品数量List
	 * 2016年12月6日上午11:38
	 */
	List<Map> statistics_goods(int type, int year, int month, Integer store_id);

	/**
	 * 下单量统计
	 * @author LSJ
	 * @param type 查询  按[1 表示：年] 或 [2表示：月]
	 * @param year 年
	 * @param month 月
	 * @param store_id 店铺ID
	 * @return 下单数 List
	 * 2016年12月6日上午11:38
	 */
	List<Map> statistics_order(int type, int year, int month, Integer store_id);

	/**
	 * 获取所有的一级菜单
	 * @author LSJ
	 * @return 菜单list
	 * 2016年12月6日上午11:38
	 */
	List<Map> getAllRootMenu();

}
