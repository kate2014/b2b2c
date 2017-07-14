package com.enation.app.b2b2c.component.plugin.goods;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.shop.core.goods.plugin.IGoodsCommentsAddEvent;
import com.enation.app.shop.core.member.model.MemberComment;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.plugin.AutoRegisterPlugin;

/**
 * 
 * 多店铺商品评论插件
 * @author zh
 * @version v1.0
 * @since v6.1
 * 2016年10月29日 上午12:01:41
 */
@Component
public class StoreGoodsComPlugin extends AutoRegisterPlugin implements IGoodsCommentsAddEvent{

	@Autowired
	private IDaoSupport daoSupport;

	@Override
	public void beforeAdd(MemberComment memberComment) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 多店铺商品评论后执行方法
	 * @param memberComment 评论对象
	 */
	@Override
	public void afterAdd(MemberComment memberComment) {
		//根据商品的id查询店铺id
		Integer store_id=this.daoSupport.queryForInt("select store_id from es_goods where goods_id = ?", memberComment.getGoods_id());
		//改变商品评论store_id
		if(store_id != 0){
			this.daoSupport.execute("update es_member_comment set store_id = ? where comment_id = ?", store_id,memberComment.getComment_id());
		}
	}
}
