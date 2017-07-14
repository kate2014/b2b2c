package com.enation.app.b2b2c.component.bonus.tag;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.component.bonus.service.IB2b2cBonusManager;
import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.DateUtil;

import freemarker.template.TemplateModelException;

/**	
 * 店铺优惠券列表标签<br>
 * 根据店铺id查集合
 * @author xulipeng
 * 2015年1月13日15:45:55
 */
@Component
@Scope("prototype")
public class StoreBonusListTag extends BaseFreeMarkerTag {

	@Autowired
	private IB2b2cBonusManager b2b2cBonusManager;

	@Autowired
	private IStoreMemberManager storeMemberManager;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request=ThreadContextHolder.getHttpRequest();
		Integer store_id = (Integer) params.get("store_id");
		if(store_id==null){
			StoreMember storeMember = storeMemberManager.getStoreMember();
			store_id = storeMember.getStore_id();
		}

		//获得优惠券参数
		int pageSize=10;

		String page = request.getParameter("page")==null?"1": request.getParameter("page");
		String add_time_from=request.getParameter("add_time_from");
		String add_time_to=request.getParameter("add_time_to");

		Map result=new HashMap();
		result.put("add_time_from", add_time_from);
		result.put("add_time_to", add_time_to);
		
		//标识是店铺显示 只显示没有过期的优惠券, 当前日期>优惠券最后时间,则过期
		Object obj=params.get("sign");
		String sign_time="";
		if(obj != null){
			sign_time=DateUtil.toString(new Date(), "yyyy-MM-dd");
			result.put("sign_time", sign_time);
		}
		Page bonusList=b2b2cBonusManager.getConditionBonusList(Integer.parseInt(page), pageSize,store_id, result);
		//获取总记录数
		Long totalCount = bonusList.getTotalCount();

		result.put("page", page);
		result.put("pageSize", pageSize);
		result.put("totalCount", totalCount);
		result.put("bonusList", bonusList);
		return result;
	}

}
