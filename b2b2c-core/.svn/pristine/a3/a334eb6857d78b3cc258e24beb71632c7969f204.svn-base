package com.enation.app.b2b2c.core.store.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.b2b2c.core.store.model.StoreSetting;
import com.enation.eop.SystemSetting;
import com.enation.eop.processor.facade.IThemePathGeter;
import com.enation.eop.resource.IThemeManager;
import com.enation.eop.resource.model.EopSite;
import com.enation.eop.resource.model.Theme;
import com.enation.framework.context.spring.SpringContextHolder;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.StringUtil;
/***
 * b2b2c模板处理器
 * 处理b2b2c不同的店铺模板 访问
 * @author Kanon 
 * 
 */
@Service("b2b2cThemePathGeter")
public class B2b2cThemePathGeter implements IThemePathGeter{
	
	@Autowired
	private IStoreThemesManager storeThemesManager;
	
	@Override
	public String getThemespath(String url) {
		
		//加入通过wap域名可以访问wap版的逻辑 kingapex add by 2016-09-22
		HttpServletRequest request =ThreadContextHolder.getHttpRequest();
		if(SystemSetting.getWap_open()==1 && request.getServerName().equals(SystemSetting.getWap_domain()) ){
			return  SystemSetting.getWap_folder();
		}
		
		//如果开启wap，并且是手机访问，返回wap  Chopper add by 2015-10-25:17:00
		if(SystemSetting.getWap_open()==1&& this.isMobile()){
			return SystemSetting.getWap_folder();
		}
		
		EopSite site = EopSite.getInstance();
		IThemeManager themeManager = SpringContextHolder.getBean("themeManager");
		Integer themeid  = site.getThemeid();
		if(themeid==null){
			System.out.println("发生 theme id 为空！！");
			System.out.println(" themeid暂时重置为 1");
			themeid=1;
		}
		Theme theme = themeManager.getTheme(themeid);
		return theme.getPath();
	}

	@Override
	public String getTplFileName(String url) {
		if(storeThemesManager.getStoreIdByUrl(url)==0){
			return url;
		}
		
		//获取店铺id
		Integer storeId=storeThemesManager.getStoreIdByUrl(url);
		
		//判读是否访问店铺页面
		if(url.indexOf("/store_themes/"+storeId)<0){
			return url;
		}
		
		//获取店铺id返回店铺模板
		url=url.replaceAll("/store_themes/"+storeId,StoreSetting.store_themes+"/"+storeThemesManager.getStrorePath(storeId));
		return url;
	}
	
	/**
	 * 获取店铺Id
	 * @param url url请求
	 * @return 店铺Id
	 */
	private Integer getStoreId(String url){
		
		String StoreId=paseStoreId(url);
		if(StoreId!=null){
			return Integer.parseInt(StoreId);
		}else{
			return 0;
		}
	}
	/**
	 * 通过正则获取url 中 的店铺Id
	 * @param url url请求
	 * @return 店铺Id
	 */
	private  static String  paseStoreId(String url){
		String pattern = "(/)(\\d+)(/)";
		String value = null;
		Pattern p = Pattern.compile(pattern, 2 | Pattern.DOTALL);
		Matcher m = p.matcher(url);
		if (m.find()) {
			value=m.group(2);
		}
		return value;
	}
	
	/**
	 * 检测是不是手机访问
	 * @return
	 */
	private static boolean isMobile(){
		
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		
		//判断请求是否为空
		if(request==null){
			return false;
		}
		
		String user_agent = request.getHeader("user-agent");
		
		//判断user-agent是否为空
		if(StringUtil.isEmpty(user_agent)){
			 return false;
		}
		 
		String userAgent = user_agent.toLowerCase();

		if(userAgent.contains("android" ) || userAgent.contains("iphone")){
			return true;
		}
		
		return false;
	}

}
