package com.enation.app.b2b2c.component.bonus.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.component.bonus.service.IB2b2cBonusManager;
import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 个人中心——我所有的的优惠券
 * @author xulipeng
 * @version v1.0
 * @since v6.2.1
 */
@Component
@Scope("prototype")
public class MyStoreBonusTag extends BaseFreeMarkerTag {

	@Autowired
	private IStoreMemberManager storeMemberManager;
	@Autowired
	private IB2b2cBonusManager b2b2cBonusManager;
	
	/**
	 * @param   is_usable	1为可用，0为不可用，2为全部。默认值为2
	 * @param	page 		第几页
	 * @param	pageSize 	默认每页条数为10
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected Object exec(Map params) throws TemplateModelException {

		StoreMember member= this.storeMemberManager.getStoreMember();
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		Map result = new HashMap();
		
		//第几页 默认为1
		String page = request.getParameter("page");
		page = (page == null || page.equals("")) ? "1" : page;
		
		//每页条数 默认为10
		String pageSize = request.getParameter("pageSize");
		pageSize = (pageSize == null || pageSize.equals("")) ? "10" : page;
		
		//查询优惠券的状态	1为可用，0为不可用，2为全部  默认为2
		Integer is_usable = (Integer) params.get("is_usable");
		if(is_usable==null){
			is_usable = 2;
		}
		
		Page webpage = this.b2b2cBonusManager.getBonusListBymemberid(Integer.parseInt(page), Integer.parseInt(pageSize), member.getMember_id(),is_usable);
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
