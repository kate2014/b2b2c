package com.enation.app.b2b2c.component.plugin.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.b2b2c.core.store.service.IStoreManager;
import com.enation.app.base.core.model.Member;
import com.enation.app.shop.core.member.plugin.IMemberLoginEvent;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.plugin.AutoRegisterPlugin;
/**
 * b2b2c 会员登录向Session中传入StoreMember对象
 * @author LiFenLong
 * @author Kanon 2016年7月6日，如果店铺会员登录在session 中存入店铺信息
 *
 */
@Component
public class B2b2cMemberLoginPlugin extends AutoRegisterPlugin implements IMemberLoginEvent{
	
	@Autowired
	private IStoreMemberManager storeMemberManager;
	
	@Autowired
	private IStoreManager storeManager;
	
	@Override
	public void onLogin(Member member, Long upLogintime) {
		
		StoreMember storeMember=storeMemberManager.getMember(member.getMember_id());
		ThreadContextHolder.getSession().setAttribute(IStoreMemberManager.CURRENT_STORE_MEMBER_KEY, storeMember);
		
		//如果登录的会员拥有店铺则在session 中存入店铺信息
		if(storeMember.getStore_id()!=null && !storeMember.getIs_store().equals(0) ){
			ThreadContextHolder.getSession().setAttribute(IStoreManager.CURRENT_STORE_KEY, storeManager.getStore(storeMember.getStore_id()));
		}
	}
}
