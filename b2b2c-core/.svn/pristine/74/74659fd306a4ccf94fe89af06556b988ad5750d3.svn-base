package com.enation.app.b2b2c.component.bonus.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.component.bonus.model.StoreBonus;
import com.enation.app.b2b2c.component.bonus.service.IB2b2cBonusManager;
import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.b2b2c.core.store.model.Store;
import com.enation.app.b2b2c.core.store.service.IStoreManager;
import com.enation.app.shop.component.bonus.model.Bonus;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.JsonResultUtil;

import freemarker.template.TemplateModelException;

/**
 * 结算页—我的优惠券列表
 * 根据店铺id，店铺商品总价等
 * @author xulipeng
 * @version v1.0
 * @since v6.2.1
 */
@Component
@Scope("prototype")
public class MyStoreBonusByAccountTag extends BaseFreeMarkerTag {

	@Autowired
	private IB2b2cBonusManager b2b2cBonusManager;
	@Autowired
	private IStoreMemberManager storeMemberManager;
	@Autowired
	private IStoreManager storeManager;
	
	/**
	 * @param	is_paging 	是否需要分页	1为需要，默认为0:默认查100条
	 * @param 	store_id	店铺id
	 * @param	storeprice	店铺商品总价
	 * @param	is_usable	1为可用，0为不可用。默认为1
	 * @return	返回单个店铺优惠券列表
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		
		StoreMember member = this.storeMemberManager.getStoreMember();
		if(member ==null){
			return JsonResultUtil.getErrorJson("未登录，不能使用此api");
		}
		
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		
		Integer store_id = (Integer) params.get("store_id");
		Double storeprice = (Double) params.get("storeprice");
		Integer is_usable = (Integer) params.get("is_usable");
		if(is_usable==null){
			is_usable=1;
		}
		
		Integer is_paging = (Integer) params.get("is_paging");
		is_paging = (is_paging==null ? 0:is_paging);
		
		//第几页，默认第1页
		String page = "1";
		//每页多少条，默认100条
		String pageSize = "100";
		
		//如果需要分页
		if(is_paging==1){
			page = request.getParameter("page");
			page = (page == null || page.equals("")) ? "1" : page;
			
			pageSize = request.getParameter("pageSize");
			pageSize = (pageSize == null || pageSize.equals("")) ? "10" : pageSize;
		}
		
		//可用优惠券列表
		Page webpage = this.b2b2cBonusManager.getMyBonusByIsUsable(Integer.parseInt(page),Integer.parseInt(pageSize), member.getMember_id(), is_usable, storeprice, store_id);
		
		Long totalCount = webpage.getTotalCount();
		List<Bonus> bonusList = (List) webpage.getResult();
		bonusList = bonusList == null ? new ArrayList() : bonusList;
		
		//生成店铺优惠券对象
		StoreBonus storeBonus = new StoreBonus();
		Store store = this.storeManager.getStore(store_id);
		storeBonus.setStore_id(store_id);
		storeBonus.setStore_name(store.getStore_name());
		storeBonus.setBonusList(bonusList);
		
		Map result = new HashMap();
		
		result.put("totalCount", totalCount);
		result.put("pageSize", pageSize);
		result.put("page", page);
		result.put("storeBonus", storeBonus);
		
		return result;
	}

}
