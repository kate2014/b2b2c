package com.enation.app.b2b2c.component.plugin.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.store.model.Store;
import com.enation.app.b2b2c.core.store.service.IStoreManager;
import com.enation.app.base.core.model.Member;
import com.enation.app.shop.core.member.plugin.IMemberRecycleEvent;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.plugin.AutoRegisterPlugin;
/**
 * 多店恢复会员信息
 * @author zh
 * @version1.0
 * @since 6.1.0
 * 2016年09月23号上午 9点10分
 */
@Component
public class B2b2cMemberRecyclePlugin extends AutoRegisterPlugin implements IMemberRecycleEvent{
	
	@Autowired
	private IStoreManager storeManager;
	@Autowired
	private IDaoSupport daoSupport;
	
	@Override
	public void recycleMember(Member member) {
		if(member != null){
			//根据member id查询店铺信息 开启店铺
			Store store=storeManager.getStoreByMember(member.getMember_id());
			if(store != null){
				//修改店铺的状态为开启
				String sql="update es_store set disabled = 1 where store_id = ?";
				this.daoSupport.execute(sql, store.getStore_id());
				//修改此店铺的所有商品为上架状态
				sql="update es_goods set market_enable = 1 where store_id = ?";
				this.daoSupport.execute(sql, store.getStore_id());
			}
			
		}
		
		
		
	}


}
