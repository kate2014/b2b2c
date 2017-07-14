package com.enation.app.b2b2c.front.api.store;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.b2b2c.core.store.model.StoreDlyCenter;
import com.enation.app.b2b2c.core.store.service.IStoreDlyCenterManager;
import com.enation.framework.action.JsonResult;
import com.enation.framework.util.JsonResultUtil;

/**
 * 店铺发货地址 API
 * @author XuLiPeng
 * @author Kanon 2016-3-3；6.0版本改造
 */
@Controller
@RequestMapping("/api/b2b2c/store-dly-center")
public class StoreDlyCenterApiController {
	
	@Autowired
	private IStoreDlyCenterManager storeDlyCenterManager;
	@Autowired
	private IStoreMemberManager storeMemberManager;
	/**
	 * 添加发货地址
	 * @param dlyCenter 店铺发货地址,StoreDlyCenter
	 * @return 返回json串
	 * result 	为1表示调用成功0表示失败
	 */
	@ResponseBody
	@RequestMapping(value="/add")
	public JsonResult add(StoreDlyCenter dlyCenter){
		
		try {
			dlyCenter.setChoose("false");
			this.storeDlyCenterManager.addDlyCenter(dlyCenter);
			return JsonResultUtil.getSuccessJson("保存成功！");
		} catch (Exception e) {
			return JsonResultUtil.getErrorJson("保存失败！");
		}
	}
	/**
	 * 修改发货地址
	 * @param dlyCenter 店铺发货地址,StoreDlyCenter
	 * @param dly_center_id 店铺发货地址Id,Integer
	 * @return 返回json串
	 * result 	为1表示调用成功0表示失败
	 */
	@ResponseBody
	@RequestMapping(value="/edit")
	public JsonResult edit(Integer dly_center_id,StoreDlyCenter dlyCenter){
		try {
			dlyCenter.setDly_center_id(dly_center_id);
			this.storeDlyCenterManager.editDlyCenter(dlyCenter);
			return  JsonResultUtil.getSuccessJson("保存成功！");
		} catch (Exception e) {
			return JsonResultUtil.getErrorJson("保存失败！");
		}
	}
	/**
	 * 删除发货地址
	 * @param dly_center_id 发货地址Id
	 * @return 返回json串
	 * result 	为1表示调用成功0表示失败
	 */
	@ResponseBody
	@RequestMapping(value="/delete")
	public JsonResult delete(Integer dly_center_id){
		try {
			this.storeDlyCenterManager.delete(dly_center_id);
			
			return JsonResultUtil.getSuccessJson("删除成功！");
		} catch (Exception e) {
			return JsonResultUtil.getErrorJson("删除失败！");
		}
	}
	
	/**
	 * 设置默认发货地址
	 * @param dly_center_id 发货地址Id,Integer
	 * @param member 店铺会员,StoreMember
	 * @return 返回json串
	 * result 	为1表示调用成功0表示失败
	 */
	@ResponseBody
	@RequestMapping(value="/site-default")
	public JsonResult siteDefault(Integer dly_center_id){
		StoreMember member=storeMemberManager.getStoreMember();
		try {
			this.storeDlyCenterManager.site_default(dly_center_id,member.getStore_id());
			return JsonResultUtil.getSuccessJson("设置成功！");
		} catch (Exception e) {
			return JsonResultUtil.getErrorJson("设置失败！");
		}
	}
	

}
