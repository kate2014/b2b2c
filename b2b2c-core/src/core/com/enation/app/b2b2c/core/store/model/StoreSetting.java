package com.enation.app.b2b2c.core.store.model;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.enation.app.base.core.service.ISettingService;
import com.enation.framework.context.spring.SpringContextHolder;

/**
 * 店铺设置
 * @author Kanon 2016-2-7
 * @version 1.0
 * @since 3.0
 */
@Component
public class StoreSetting {

	public static final String setting_key="store"; //系统设置中的分组
	
	public static Integer auth=0;				//商家商品是否审核、 0：不需要审核，1：需要审核
	
	public static Integer edit_auth=0;			//商品修改是否审核、 0：不需要审核，1：需要审核

	public static Integer self_auth=0;			//自营商品是否审核、 0：不需要审核，1：需要审核
	
	//public static Integer can_buy=0;			//商家是否可以购买自己的商品，0能购买；1不能购买
	
	//public static Integer can_cod;			//商品是否可以使用货到付款
	
	//public static Integer can_delete_comment;	//商品是否可以删除评论
	
	public static String store_themes="/store/themes/";			//店铺模板路径
		/**
		 * 加载店铺设置
		 * 由数据库中加载
		 */
		public static void load(){
			
			ISettingService settingService= SpringContextHolder.getBean("settingService");
			Map<String,String> settings = settingService.getSetting(setting_key);
			auth = Integer.parseInt(settings.get("auth"));
			edit_auth = Integer.parseInt(settings.get("edit_auth"));
			self_auth = Integer.parseInt(settings.get("self_auth"));
			//can_buy = Integer.parseInt(settings.get("can_buy"));
			store_themes=settings.get("store_themes");
		}
}
