package com.enation.app.b2b2c.front.api.store;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.b2b2c.core.store.service.IStoreTemplateManager;
import com.enation.framework.action.JsonResult;
import com.enation.framework.util.JsonResultUtil;

/**
 * 物流模板管理类
 * @author xulipeng
 * 2016年03月03日 改造springMVC
 */

@Controller
@RequestMapping("/api/b2b2c/transport")
public class TransportApiController {
	@Autowired
	private IStoreTemplateManager storeTemplateManager;
	@Autowired
	private IStoreMemberManager storeMemberManager;
	
	/**
	 * 删除
	 * @param tempid 模板名称
	 * @return 返回json串
	 * result 	为1表示调用成功0表示失败
	 */
	@ResponseBody
	@RequestMapping(value="del",produces= MediaType.APPLICATION_JSON_VALUE)
	public JsonResult del(Integer tempid){
		try {	
			//增加权限
			StoreMember member=storeMemberManager.getStoreMember();
			Map map = storeTemplateManager.getTemplae(member.getStore_id(), tempid);
			if(map==null){
				return JsonResultUtil.getErrorJson("您没有权限");
			}
			
			this.storeTemplateManager.delete(tempid);
			return JsonResultUtil.getSuccessJson("删除成功");
			
		} catch (RuntimeException e) {
			return JsonResultUtil.getErrorJson(e.getMessage());
			
		} catch (Exception e) {
			return JsonResultUtil.getErrorJson("删除失败");
			
		}
	}
	
	
	/**
	 * 设置默认模板
	 * @param tempid 模板名称
	 * @param member 店铺会员,StoreMember
	 * @return 返回json串
	 * result 	为1表示调用成功0表示失败
	 */
	@ResponseBody
	@RequestMapping(value="set-def-temp",produces= MediaType.APPLICATION_JSON_VALUE)
	public JsonResult setDefTemp(Integer tempid){
		try {
			//增加权限
			StoreMember member=storeMemberManager.getStoreMember();
			Map map = storeTemplateManager.getTemplae(member.getStore_id(), tempid);
			if(map==null){
				return JsonResultUtil.getErrorJson("您没有权限");
			}
			
			this.storeTemplateManager.setDefTemp(tempid,member.getStore_id());
			return JsonResultUtil.getSuccessJson("设置成功!");
			
		} catch (Exception e) {
			return JsonResultUtil.getErrorJson("设置失败，请稍后重试！");
			
		}
	}

	
	
}
