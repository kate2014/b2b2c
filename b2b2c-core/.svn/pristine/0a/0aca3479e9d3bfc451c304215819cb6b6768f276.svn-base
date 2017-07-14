package com.enation.app.b2b2c.component.bonus.tag;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.component.bonus.service.IB2b2cBonusManager;
import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;


/**
 * 读取会员优惠券列表(根据可用或者不可用)
 * @author xulipeng
 * @version v1.0
 * @since V6.2.1
 * 2016年12月19日
 */
@Component
public class B2b2cMemberBonusListByUseOrNouserTag extends BaseFreeMarkerTag {

	
	@Autowired
	private IStoreMemberManager storeMemberManager;
	@Autowired
	private IB2b2cBonusManager b2b2cBonusManager;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		
		//isCanUse 1为可用 0为不可用
		Integer is_usable = (Integer) params.get("isCanUse");
		
		//结算页调用时使用，会员中心页调用不需要传递
		Double goodsPrice = (Double) params.get("goodsPrice");
		
		StoreMember member= this.storeMemberManager.getStoreMember();
		
		Integer page = (Integer) params.get("page");
		page = (page == null || page.equals("")) ? 1 : page;
		Integer pageSize = (Integer) params.get("pageSize");
		pageSize = (pageSize ==null || pageSize.equals("")) ? 5:pageSize;
		
		Page webpage = this.b2b2cBonusManager.getBonusListBymemberid(page, pageSize, member.getMember_id(), is_usable);
		
		Map result = new HashMap();
		Long totalCount = webpage.getTotalCount();
		
		List bonusList = (List) webpage.getResult();
		bonusList = bonusList == null ? new ArrayList() : bonusList;

		result.put("totalCount", totalCount);
		result.put("pageSize", pageSize);
		result.put("page", page);
		result.put("bonusList", bonusList);
		
		return result;
	}
	
	
}
