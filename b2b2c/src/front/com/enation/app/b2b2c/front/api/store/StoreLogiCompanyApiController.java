package com.enation.app.b2b2c.front.api.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enation.app.b2b2c.core.store.service.IStoreLogiCompanyManager;
import com.enation.framework.action.JsonResult;
import com.enation.framework.util.JsonResultUtil;

/**
 * 店铺物流公司API
 * @author fenlongli
 * @author xulipeng   2016年03月03日		修改springMVC
 */

@Controller
@RequestMapping("/api/b2b2c/store-logi-company")
public class StoreLogiCompanyApiController{
	
	@Autowired
	private IStoreLogiCompanyManager storeLogiCompanyManager;
	
	/**
	 * 保存
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/save-real")
	public JsonResult saveReal(Integer logi_id){
		try {
			storeLogiCompanyManager.addRel(logi_id);
			return JsonResultUtil.getSuccessJson("添加成功");
			
		} catch (Exception e) {
			return JsonResultUtil.getErrorJson("添加失败");
			
		}
	}
	/**
	 * 删除
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="del-real")
	public JsonResult delReal(Integer logi_id){
		try {
			storeLogiCompanyManager.deleteRel(logi_id);
			return JsonResultUtil.getSuccessJson("删除成功");
		} catch (Exception e) {
			return JsonResultUtil.getErrorJson("删除失败");
		}
	}
	
	//get set
	
	
}
