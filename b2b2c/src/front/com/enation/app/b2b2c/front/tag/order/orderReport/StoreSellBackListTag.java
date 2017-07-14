package com.enation.app.b2b2c.front.tag.order.orderReport;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.b2b2c.core.order.service.IStoreSellBackManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.DateUtil;

import freemarker.template.TemplateModelException;
/**
 * 退货列表、或退款单列表
 * @author fenlongli
 * @version v1.1 Kanon 2016年7月6日；修改退货单列表增加类型可以显示退款单
 */
@Component
public class StoreSellBackListTag extends BaseFreeMarkerTag{
	@Autowired
	private IStoreSellBackManager storeSellBackManager;
	@Autowired
	private IStoreMemberManager storeMemberManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request=ThreadContextHolder.getHttpRequest();
		Integer page=this.getPage();
		Integer pageSize=this.getPageSize();
		Map map=new HashMap();
		StoreMember storeMember=storeMemberManager.getStoreMember();

		Integer status=params.get("status")!=null?Integer.parseInt(params.get("status").toString()):null;
		//获取类型
		Integer type=params.get("type")!=null?Integer.parseInt(params.get("type").toString()):null;
		if(type==null){
			type=request.getParameter("type")!=null?Integer.parseInt(request.getParameter("type").toString()):null;
		}
		if(type!=null){
			map.put("type",type);
		}
		//申请时间开始
		String start_time=request.getParameter("start_time")!=null?request.getParameter("start_time").toString():null;
		if(start_time!=null){
			map.put("start_time",DateUtil.getDateline(start_time+" 00:00:00", "yyyy-MM-dd HH:mm:ss"));
		}
		//申请时间结束
		String end_time=request.getParameter("end_time")!=null?request.getParameter("end_time").toString():null;
		if(end_time!=null){
			map.put("end_time",DateUtil.getDateline(end_time+" 23:59:59", "yyyy-MM-dd HH:mm:ss"));
		}
		//订单号
		String ordersn=request.getParameter("order_sn")!=null?request.getParameter("order_sn").toString():null;
		if(ordersn!=null){
			map.put("order_sn",ordersn);
		}
		//退款单好
		String tradeno=request.getParameter("tradeno")!=null?request.getParameter("tradeno").toString():null;
		if(tradeno!=null){
			map.put("tradeno",tradeno);
		}
		//状态
		String tradestatus=request.getParameter("tradestatus")!=null?request.getParameter("tradestatus").toString():null;
		if(tradestatus!=null){
			map.put("tradestatus",tradestatus);
		}
		
		
		
		//获取 列表
		Page sellBackList= storeSellBackManager.list(page, pageSize,storeMember.getStore_id(),status,map);
		Long totalCount = sellBackList.getTotalCount();
		
		map.put("page",page);
		map.put("pageSize",pageSize);
		map.put("totalCount", totalCount);
		map.put("sellBackList", sellBackList);
		return map;
	}
}
