package com.enation.app.b2b2c.core.goods.service;

import com.enation.framework.database.Page;

/**
 * 自营店商品评论、咨询管理接口
 * @author DMRain 2016-4-25
 * @version [1.0]
 * @since [1.0]
 */
public interface ISelfGoodsCommentsManager {

	/**
	 * 获取自营店评论(咨询)分页列表
	 * @param page
	 * @param pageSize
	 * @param type 类型 1：评论，2：咨询
	 * @return
	 */
	public Page list(Integer pageNo, Integer pageSize, Integer type);
	
	/**
	 * 保存回复评论的内容
	 * @param commentId 评论ID
	 * @param recomment 回复内容
	 */
	public void reply(Integer commentId, String recomment);
	
	/**
	 * 保存审核咨询信息
	 * @param commentId 评论ID
	 * @param recomment 回复内容
	 * @param reviewType 审核类型 1：通过审核，2：拒绝通过
	 */
	public void review(Integer commentId, String recomment, Integer reviewType);
	
	/**
	 * 批量删除评论或咨询
	 * @param comment_id 评论或咨询ID组
	 */
	public void delete(Integer[] comment_id);
	
}
