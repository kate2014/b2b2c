package com.enation.app.b2b2c.core.goods.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.b2b2c.core.goods.service.ISelfGoodsCommentsManager;
import com.enation.framework.annotation.Log;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.log.LogType;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;

/**
 * 自营店商品评论、咨询管理接口实现类
 * @author DMRain 2016-4-25
 * @version [1.0]
 * @since [1.0]
 */
@Service("selfGoodsCommentsManager")
public class SelfGoodsCommentsManager implements ISelfGoodsCommentsManager{

	@Autowired
	private IDaoSupport daoSupport;
	
	/* (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.goods.ISelfGoodsCommentsManager#list(java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Page list(Integer pageNo, Integer pageSize, Integer type) {
		String sql = "select c.*,m.uname muname, g.name gname from es_member_comment c "
				+ "left join es_member m on c.member_id = m.member_id "
				+ "left join es_goods g on c.goods_id = g.goods_id where c.type = ? and c.store_id = 1 order by c.dateline desc";
		Page page = this.daoSupport.queryForPage(sql, pageNo, pageSize, type);
		return page;
	}

	/* (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.goods.ISelfGoodsCommentsManager#reply(java.lang.Integer, java.lang.String)
	 */
	@Override
	@Log(type=LogType.GOODS,detail="${commentId}评论审核通过")
	public void reply(Integer commentId, String recomment) {
		String sql = "update es_member_comment set reply = ?,replytime = ?,replystatus = 1 where comment_id = ?";
		this.daoSupport.execute(sql, recomment, DateUtil.getDateline(), commentId);
	}

	/* (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.goods.ISelfGoodsCommentsManager#reviewPass(java.lang.Integer, java.lang.String, java.lang.Integer)
	 */
	@Override
	@Log(type=LogType.GOODS,detail="${commentId}商品咨询审核通过")
	public void review(Integer commentId, String recomment, Integer reviewType) {
		String sql = "";
		
		//判断审核类型 1：通过，2：拒绝
		if (reviewType == 1) {
			sql = "update es_member_comment set status = 1,reply = ?,replytime = ?,replystatus = 1 where comment_id = ?";
			this.daoSupport.execute(sql, recomment, DateUtil.getDateline(), commentId);
		} else if (reviewType == 2) {
			sql = "update es_member_comment set status = 2,replytime = ?,replystatus = 1";
			
			//如果回复不为空
			if (recomment != null && !StringUtil.isEmpty(recomment)) {
				sql += ",reply = '"+recomment+"'";
			}
			sql += " where comment_id = ?";
			this.daoSupport.execute(sql, DateUtil.getDateline(), commentId);
		}
	}

	/* (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.goods.ISelfGoodsCommentsManager#delete(java.lang.Integer[])
	 */
	@Override
	@Log(type=LogType.GOODS,detail="删除评论")
	public void delete(Integer[] comment_id) {
		if (comment_id== null) {
			return;
		}
		String id_str = StringUtil.arrayToString(comment_id, ",");
		String sql = "delete from es_member_comment where comment_id in (" + id_str + ")";
		this.daoSupport.execute(sql);
		
	}
	
}
