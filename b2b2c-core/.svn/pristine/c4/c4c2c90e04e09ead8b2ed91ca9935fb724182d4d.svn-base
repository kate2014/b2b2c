package com.enation.app.b2b2c.core.store.service.statistics.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.enation.app.b2b2c.core.store.service.statistics.IB2b2cStoreProfileStatisticsManager;
import com.enation.app.b2b2c.core.store.service.statistics.StatisticsUtil;
import com.enation.app.shop.core.order.service.OrderStatus;
import com.enation.framework.action.JsonResult;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.JsonResultUtil;
import com.enation.framework.util.StringUtil;


/**
 * 
 * 店铺概况管理实现类
 * @author    jianghongyan
 * @version   1.0.0,2016年8月4日
 * @since     v6.2
 */
@Service
public class B2b2cStoreProfileStatisticsManager implements IB2b2cStoreProfileStatisticsManager {

	@Autowired
	private IDaoSupport daoSupport;

	

	@Override
	public JsonResult getLast30dayStatus(Integer store_id) throws RuntimeException {
		try {
			Map<String,Object> map=this.getLast30dayStatusMap(store_id);
			Map<String,Object> map2=this.getStoreInfoMap(store_id);
			map2.putAll(map);
////		 下单高峰期 
			String order_fastigium="暂无";
			map2.put("order_fastigium", order_fastigium);
			return JsonResultUtil.getObjectJson(map2);
			
		} catch (RuntimeException e) {
			throw e;
		}
	}

	private Map<String, Object> getStoreInfoMap(Integer store_id) {
		////		 商品总数 
		//		Integer goods_sum=StringUtil.toInt(m);
		//		 商品收藏量 
		//		Integer goods_collect=StringUtil.toInt(map2.get("goods_collect").toString(), 0);
		////		店铺收藏量 
		//		Integer store_collect;
		
		String sql="SELECT "
				+ " COUNT(distinct g.goods_id) AS goods_sum, COUNT(distinct f.favorite_id) AS goods_collect, COUNT(distinct mc.id) AS store_collect "
				+ " FROM es_goods g "
				+ " LEFT JOIN es_favorite f ON g.goods_id = f.goods_id "
				+ " LEFT JOIN es_member_collect mc ON mc.store_id = g.store_id "
				+ " WHERE g.store_id = ?";
		return this.daoSupport.queryForMap(sql, store_id);
	}

	private Map<String, Object> getLast30dayStatusMap(Integer store_id) {
		////	 近30天下单金额 
		//	Double order_money=StringUtil.toDouble(map.get("order_money").toString(),0.0);
		////	 近30天下单会员数 
		//	Integer order_member=StringUtil.toInt(map.get("order_member").toString(), 0);
		////	 近30天下单量 
		//	Integer order_num=StringUtil.toInt(map.get("order_num").toString(), 0);
		////	近30天下单商品数 
		//	Integer order_goods=StringUtil.toInt(map.get("order_goods").toString(), 0);
		long starttime=DateUtil.startOfSomeDay(30);
		long endtime=DateUtil.endOfyesterday();
		String sql="select SUM(o.paymoney) AS order_money ,COUNT(m.member_id) AS order_member,COUNT(o.order_id) AS order_num,SUM(oi.num) as order_goods from es_order o "
				+ " LEFT JOIN es_member m "
				+ " ON m.member_id=o.member_id "
				+ " LEFT JOIN es_order_items oi "
				+ " ON o.order_id=oi.order_id "
				+ " WHERE o.complete_time>? AND o.complete_time < ? AND o.status>= "+OrderStatus.ORDER_PAY
				+ " AND o.status!= " +OrderStatus.ORDER_CANCELLATION
				+ " AND o.pay_status= "+OrderStatus.PAY_YES
				+ " AND o.store_id=?";
		Map<String,Object> map=this.daoSupport.queryForMap(sql, starttime,endtime,store_id);
		//	 平均客单价 
		Double order_money=StringUtil.toDouble(map.get("order_money")==null?null:map.get("order_money").toString(),0.0);
		map.put("order_money", order_money);
		Integer order_member=StringUtil.toInt(map.get("order_member").toString(), 0);
		Double average_member_money=order_money/order_member;
		if(Double.isNaN(average_member_money)){
			average_member_money=0.0;
		}
		Integer order_goods=StringUtil.toDouble(map.get("order_goods")==null?null:map.get("order_goods").toString(), 0.0).intValue();
		map.put("order_goods", order_goods);
		//	 平均价格 
		Double average_goods_money=order_money/order_goods;
		if(Double.isNaN(average_goods_money)){
			average_goods_money=0.0;
		}
		map.put("average_member_money", average_member_money);
		map.put("average_goods_money", average_goods_money);
		return map;
	}

	@Override
	public JsonResult getLast30dayLineChart(Integer store_id) throws RuntimeException {
		// TODO Auto-generated method stub
		//查询map集合
		try {
			/**
			 * start: 拼接sql语句并查询当前周期数据
			 */
			String condition_sql=StatisticsUtil.getInstance().createSql(30);
			String sql="select count(0) as t_num,SUM(need_pay_money) as t_money, case "+condition_sql+" as day  from es_order o where 1=1 AND status>="+OrderStatus.ORDER_PAY+" AND status!="+OrderStatus.ORDER_CANCELLATION+" AND pay_status="+OrderStatus.PAY_YES;
			if(store_id!=null&&store_id!=0){
				sql+=" and o.store_id="+store_id;
			}
			sql+= " group by case "+condition_sql;
			List list = this.daoSupport.queryForList(sql);
			list=StatisticsUtil.getInstance().fitLast30dayList(list);
			return JsonResultUtil.getObjectJson(list);
		} catch (RuntimeException e) {
			throw e;
		}
	}

}
