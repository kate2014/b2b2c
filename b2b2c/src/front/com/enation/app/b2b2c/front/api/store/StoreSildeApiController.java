package com.enation.app.b2b2c.front.api.store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enation.app.b2b2c.core.store.service.IStoreSildeManager;
import com.enation.framework.action.JsonResult;
import com.enation.framework.util.JsonResultUtil;
/**
 * 店铺幻灯片API
 * @author LiFenLong
 * @author Kanon 2016-3-3；6.0版本改造
 *
 */
@Controller
@RequestMapping("/api/b2b2c/store-silde")
public class StoreSildeApiController {
	@Autowired
	private IStoreSildeManager storeSildeManager;
	/**
	 * 修改店铺幻灯片设置
	 * @param silde_id 幻灯片Id,Integer[]
	 * @param store_fs 图片地址,String[]
	 * @param silde_url 映射地址,String[]
	 * @return 返回json串
	 * result 	为1表示调用成功0表示失败
	 */
	@ResponseBody
	@RequestMapping(value="/edit-store-silde",produces= MediaType.APPLICATION_JSON_VALUE)
	public JsonResult editStoreSilde(String[] silde_url,String[] store_fs,Integer[] silde_id){
		try {
			this.storeSildeManager.edit(silde_id, store_fs, silde_url);
			return JsonResultUtil.getSuccessJson("修改成功");
		} catch (Exception e) {
			return JsonResultUtil.getErrorJson("修改失败");
		}
	}
}
