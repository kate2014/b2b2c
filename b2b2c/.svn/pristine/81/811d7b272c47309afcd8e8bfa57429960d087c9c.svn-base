package com.enation.app.b2b2c.front.tag.store.activity;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.b2b2c.core.store.service.activity.IStoreActivityManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 获取店铺全部的有效并上架的商品分页列表Tag
 * @author DMRain
 * @date 2015年12月30日
 * @version v1.0
 * @since v1.0
 */
@Component
public class StoreGoodsAllTag extends BaseFreeMarkerTag{
	@Autowired
	private IStoreActivityManager storeActivityManager;
	@Autowired
	private IStoreMemberManager storeMemberManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		Integer store_id = this.storeMemberManager.getStoreMember().getStore_id();//获取当前登陆的店铺会员
		
		int pageNo = this.getPage();
		int pageSize = this.getPageSize();
		
		Map result = new HashMap();
		
		String keyword = request.getParameter("keyword") == null ? "" : request.getParameter("keyword");
		
		result.put("store_id", store_id);
		result.put("keyword", keyword);
		
		Page page = this.storeActivityManager.goodsList(pageNo, pageSize, result);
		
		return page;
	}
	
}
