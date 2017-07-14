package com.enation.app.b2b2c.core.order.service;

import java.util.Map;

import com.enation.framework.database.Page;
/**
 * 店铺退货申请
 * @author fenlongli
 *	@author Kanon 修改退货流程 2016-6-24
 */
public interface IStoreSellBackManager {
	/**
	 * 分页显示退货单列表\退款单列表
	 * @param status 状态
	 * @param page 页数
	 * @param pageSize 每页显示数量
	 * @return
	 */
	public Page list(int page,int pageSize,Integer status,Integer store_id,Map map);

	/**
	 * 退款单列表
	 * @param page 分页数量
	 * @param pageSize 分页每页显示数量
	 * @return 退款单列表
	 */
	public Page refundList(Integer page,Integer pageSize);
	
	/**
	 * 根据店铺Id获取退款单列表
	 * @author Kanon
	 * @param page 分页数量
	 * @param pageSize 分页每页显示数量
	 * @param store_id 店铺Id
	 * @return 退款单列表
	 */
	public Page refundByStoreIdList(Integer page,Integer pageSize,Integer store_id);
	
	/**
	 * 获取退款单
	 * @param id
	 */
	public Map getRefund(Integer id);
	
	/**
	 * 获取退货申请
	 * @param id 退货申请Id
	 * @return
	 */
	public Map get(Integer id);
	
	/**
	 * 审核退款单
	 * @param id 退款单Id
	 * @param status 审核状态 0未审核，1审核通过，2审核不通过
	 * @param seller_remark 审核备注
	 */
	public void authRetund(Integer id,Integer status,String seller_remark);
	
	/**
	 * 商家进行退款
	 * @param id 退货单Id
	 * @param alltotal_pay 退款金额
	 */
	public void refund(Integer id,Double alltotal_pay);
	
}
