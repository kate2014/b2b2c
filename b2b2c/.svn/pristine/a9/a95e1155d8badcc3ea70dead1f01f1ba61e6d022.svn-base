package com.enation.app.b2b2c.front.tag.order.orderReport;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.b2b2c.core.order.service.IStoreSellBackManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 退款申请列表
 * 查询会员的退款申请
 * @author Kanon
 * @version 1.0 2016年7月6日
 * @since v3.1
 */
public class RefundListTag extends BaseFreeMarkerTag{
	
	@Autowired
	private IStoreSellBackManager storeSellBackManager;
	
	@Autowired
	private IStoreMemberManager StoreMemberManager;
	
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		
		
		return storeSellBackManager.refundByStoreIdList(this.getPage(), this.getPageSize(), StoreMemberManager.getStoreMember().getStore_id());
	}

}
