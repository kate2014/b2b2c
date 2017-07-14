package com.enation.app.b2b2c.core.log;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.eop.resource.model.AdminUser;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.log.ILogCreator;
import com.enation.framework.model.Log;
import com.enation.framework.model.LogStore;
import com.enation.framework.util.DateUtil;

/**
 * 多店日志创建实现
 * @author fk
 * @version v1.0
 * @since v6.2
 * 2016年12月13日 上午10:04:15
 */
@Component
public class B2b2cLogCreator implements ILogCreator{

	@Autowired
	private IStoreMemberManager storeMemberManager;
	
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.framework.log.ILogCreator#createLog()
	 */
	@Override
	public Map createLog() {
		StoreMember storeMember = storeMemberManager.getStoreMember();
		AdminUser adminUser = UserConext.getCurrentAdminUser();
		Map map = new HashMap();
		if(storeMember != null){
			map.put("member_name", storeMember.getUname());
			map.put("member_id", storeMember.getMember_id());
			map.put("store_id", storeMember.getStore_id());
		}
		map.put("admin_user", adminUser);
		return map;
	}

}
