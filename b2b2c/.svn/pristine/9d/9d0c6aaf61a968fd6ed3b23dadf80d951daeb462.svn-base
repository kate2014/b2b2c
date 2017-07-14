package com.enation.app.b2b2c.front.tag.store;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberCommentManager;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
/**
 * 店铺商品咨询列表标签
 * @author LiFenLong
 *
 */
public class StoreConsultListTag extends BaseFreeMarkerTag{
	@Autowired
	private IStoreMemberCommentManager storeMemberCommentManager;
	@Autowired
	private IStoreMemberManager storeMemberManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request=ThreadContextHolder.getHttpRequest();
		StoreMember member=storeMemberManager.getStoreMember();
		String page = request.getParameter("page");
		page = (page == null || page.equals("")) ? "1" : page;
		int pageSize=10;
		Map map=new HashMap();
		String stype=request.getParameter("stype");
		String grade=request.getParameter("grade");
		if(request.getParameter("stype")==null){
			stype="0";
		}
		if(request.getParameter("grade")==null){
			grade="-1";
		}
		map.put("type", params.get("type").toString());
		map.put("status",request.getParameter("status"));
		map.put("grade",grade);
		map.put("replyStatus", request.getParameter("replyStatus"));
		map.put("keyword", request.getParameter("keyword"));
		map.put("mname", request.getParameter("mname"));
		map.put("gname", request.getParameter("gname"));
		map.put("content", request.getParameter("searchcontent"));
		map.put("stype", stype);
		Page cmmentList=storeMemberCommentManager.getAllComments(Integer.parseInt(page), pageSize, map, member.getStore_id());
		//获取总记录数
		Long totalCount = cmmentList.getTotalCount();
		
		map.put("page", page);
		map.put("pageSize", pageSize);
		map.put("totalCount", totalCount);
		map.put("cmmentList", cmmentList);
		
		return map;
	}
}
