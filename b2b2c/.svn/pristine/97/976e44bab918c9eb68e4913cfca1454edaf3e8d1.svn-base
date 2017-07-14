package com.enation.app.b2b2c.front.tag.store.bill;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.store.service.bill.IBillManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 结算退货申请订单
 * @author fenlongli
 * @date 2015-6-8 下午3:03:28
 */
@Component
public class StoreBillSellBackListTag extends BaseFreeMarkerTag{
	@Autowired
	private IBillManager billManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request=ThreadContextHolder.getHttpRequest();
		Integer pageSize=10;
		Integer pageNo = request.getParameter("page")==null?1: Integer.parseInt(request.getParameter("page").toString());
		String sn=request.getParameter("sn").toString();
		Map result=new HashMap();
		Page orderList=billManager.bill_sell_back_list(pageNo, pageSize, sn);
		result.put("page", pageNo);
		result.put("pageSize", pageSize);
		result.put("totalCount", orderList.getTotalCount());
		result.put("storeOrder", orderList);
		return result;
	}
}
