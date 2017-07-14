package com.enation.app.b2b2c.front.tag.store;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.b2b2c.core.store.service.statistics.IStoreLogsManager;
import com.enation.eop.processor.core.UrlNotFoundException;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.StringUtil;

import freemarker.template.TemplateModelException;

/**
 * 查询店铺的日志列表
 * @author fk
 * @version v1.0
 * @since v6.2
 * 2016年12月13日 下午2:12:35
 */
@Component
public class StoreLogsTag  extends BaseFreeMarkerTag {

	@Autowired
	private IStoreMemberManager storeMemberManager;
	
	@Autowired
	private IStoreLogsManager storeLogsManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request=ThreadContextHolder.getHttpRequest();
		//session中获取会员信息,判断用户是否登陆
		StoreMember member=storeMemberManager.getStoreMember();
		if(member==null){
			HttpServletResponse response= ThreadContextHolder.getHttpResponse();
			try {
				response.sendRedirect("login.html");
			} catch (IOException e) {
				throw new UrlNotFoundException();
			}
		}
		String page = request.getParameter("page") == null? "1" : request.getParameter("page");
		String userid = request.getParameter("userid");
		String type = request.getParameter("type");
		String start_time = request.getParameter("start_time");
		String end_time = request.getParameter("end_time");
		Map<String,Object> result = new HashMap<String,Object>();
		int pageSize=10;
		result.put("storeid", member.getStore_id());
		result.put("userid", StringUtil.toInt(userid));
		result.put("type", type);
		result.put("start_time", start_time);
		result.put("end_time", end_time);
		Page storeLogs= storeLogsManager.getStoreLogsList(Integer.parseInt(page) , pageSize,result);
		
		Long totalCount = storeLogs.getTotalCount();
		
		result.put("page", page);
		result.put("pageSize", pageSize);
		result.put("totalCount", totalCount);
		result.put("storeLogs", storeLogs);
		
		return result;
	}

}
