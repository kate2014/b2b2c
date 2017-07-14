package com.enation.app.b2b2c.component.plugin.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.store.model.Store;
import com.enation.app.b2b2c.core.store.service.IStoreManager;
import com.enation.app.base.core.model.Member;
import com.enation.app.shop.core.member.plugin.IMemberDeleteEvent;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.plugin.AutoRegisterPlugin;
/**
 * 多店会员注销插件
 * @author HaoBeck
 * @version 1.0
 * @since v61
 * 2016年09月23日 10点55分
 */
@Component
public class B2b2cMemberDeletePlugin extends AutoRegisterPlugin implements IMemberDeleteEvent{
	
	@Autowired
	private IDaoSupport daoSupport;
	@Autowired
	private IStoreManager storeManager;

	@Override
	public void delteMember(Member member) {
		if(member != null){
			//根据member id查询店铺信息
			Store store=storeManager.getStoreByMember(member.getMember_id());
			if(store != null){
				//修改店铺的状态为禁用
				String sql="update es_store set disabled = 2 where store_id = ?";
				this.daoSupport.execute(sql, store.getStore_id());
				//修改此店铺的所有商品为下架状态
				sql="update es_goods set market_enable = 0 where store_id = ?";
				this.daoSupport.execute(sql, store.getStore_id());
			}
			
		}
		
	}
}
