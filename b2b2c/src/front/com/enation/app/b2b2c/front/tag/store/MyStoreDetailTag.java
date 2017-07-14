package com.enation.app.b2b2c.front.tag.store;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.b2b2c.core.store.model.Store;
import com.enation.app.b2b2c.core.store.service.IStoreManager;
import com.enation.app.b2b2c.core.store.service.IStoreThemesManager;
import com.enation.eop.processor.core.UrlNotFoundException;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.StringUtil;

import freemarker.template.TemplateModelException;
@Component
/**
 * 店铺信息Tag
 * @author LiFenLong
 *
 */
public class MyStoreDetailTag extends BaseFreeMarkerTag{
	@Autowired
	private IStoreManager storeManager;
	@Autowired
	private IStoreMemberManager storeMemberManager;
	@Autowired
	private IStoreThemesManager storeThemesManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		String ctx = this.getRequest().getContextPath();
		HttpServletResponse response= ThreadContextHolder.getHttpResponse();
		Store store=new Store();
		try {
			//判断是浏览商家店铺页面还是访问商家中心,type不为0则为访问商家店铺页面
			if(params.get("type")!=null&&params.get("store_id")==null){
				//通过链接获取店铺Id
				store=storeThemesManager.getStoreByUrl(this.getRequest().getServletPath());
			}else if(params.get("type")!=null&&params.get("store_id")!=null){
				//通过链接获取店铺Id 
				store=storeManager.getStore(StringUtil.toInt(params.get("store_id").toString(),true));
			}else{
				//session中获取会员信息,判断用户是否登陆
				StoreMember member=storeMemberManager.getStoreMember();
				if(member==null){
					response.sendRedirect(ctx+"/store/login.html");
				}
				//重新申请店铺时使用
				else if(member.getStore_id()==null){
					store=storeManager.getStoreByMember(member.getMember_id());
				}else{
					 store=storeManager.getStore(member.getStore_id());
				}
			}
			//if(store.getDisabled()==2){
				//response.sendRedirect(ctx+"/store/dis_store.html");
			//}
		} catch (IOException e) {
			throw new UrlNotFoundException();
		}
		
		return store;
	}
	
}
