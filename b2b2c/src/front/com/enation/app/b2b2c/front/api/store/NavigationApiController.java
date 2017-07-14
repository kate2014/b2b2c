package com.enation.app.b2b2c.front.api.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.b2b2c.core.store.model.Navigation;
import com.enation.app.b2b2c.core.store.service.INavigationManager;
import com.enation.framework.action.JsonResult;
import com.enation.framework.util.JsonResultUtil;



/**
 * 导航栏API
 * @author Kanon 2016-3-3；6.0版本改造
 *
 */
@Controller
@RequestMapping("/api/b2b2c/navigation")
public class NavigationApiController {
	@Autowired
	private INavigationManager navigationManager;
	@Autowired
	private IStoreMemberManager storeMemberManager;

	@ResponseBody
	@RequestMapping(value="/add",produces= MediaType.APPLICATION_JSON_VALUE)
	public JsonResult add(Navigation navigation){
		StoreMember storeMember = storeMemberManager.getStoreMember();
		navigation.setStore_id(storeMember.getStore_id());
		
		try {
			this.navigationManager.save(navigation);
			return JsonResultUtil.getSuccessJson("添加成功");
		} catch (Exception e) {
			return JsonResultUtil.getErrorJson("添加失败");
		}
	}
	
	@ResponseBody
	@RequestMapping(value="/edit",produces= MediaType.APPLICATION_JSON_VALUE)
	public JsonResult edit(Navigation navigation){
		StoreMember storeMember =storeMemberManager.getStoreMember();

		navigation.setStore_id(storeMember.getStore_id());
		
		try {
			//增加权限
			Navigation storeNavigation = navigationManager.getNavication(navigation.getId());
			if(storeNavigation==null ||!storeNavigation.getStore_id().equals(storeMember.getStore_id())){
				return JsonResultUtil.getErrorJson("您没有权限");
			}
			
			this.navigationManager.edit(navigation);
			return JsonResultUtil.getSuccessJson("修改成功");
		} catch (Exception e) {
			return JsonResultUtil.getErrorJson("修改失败");
		}
	}
	
	@ResponseBody
	@RequestMapping(value="/delete",produces= MediaType.APPLICATION_JSON_VALUE)
	public JsonResult delete(Integer nav_id){
		try {
			//增加权限
			StoreMember storeMember = storeMemberManager.getStoreMember();
			Navigation storeNavigation = navigationManager.getNavication(nav_id);
			if(storeNavigation==null || !storeNavigation.getStore_id().equals(storeMember.getStore_id())){
				return JsonResultUtil.getErrorJson("您没有权限");
			}
			this.navigationManager.delete(nav_id);
			return JsonResultUtil.getSuccessJson("删除成功");
		} catch (Exception e) {
			return JsonResultUtil.getErrorJson("删除失败");
		}
	}
	
	
}
