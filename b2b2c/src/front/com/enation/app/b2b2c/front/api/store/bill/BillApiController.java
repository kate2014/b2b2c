package com.enation.app.b2b2c.front.api.store.bill;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enation.app.b2b2c.core.store.model.bill.BillStatusEnum;
import com.enation.app.b2b2c.core.store.service.bill.IBillManager;
import com.enation.framework.action.JsonResult;
import com.enation.framework.util.JsonResultUtil;

/**
 * 结算API
 * @author fenlongli
 * @date 2015-6-7 下午4:05:25
 */
@Controller
@RequestMapping("/api/b2b2c/store-bill")
public class BillApiController {
	protected final Logger logger = Logger.getLogger(getClass());
	
	
	@Autowired
	private IBillManager billManager;
	/**
	 * 确认结算
	 * @return
	 */
	@ResponseBody  
	@RequestMapping(value="/confirm", produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResult confirm(Integer id){
		try {
			billManager.edit_billdetail_status(id, BillStatusEnum.COMPLETE.getIndex());
			return JsonResultUtil.getSuccessJson("提交成功");
		} catch (Exception e) {
			this.logger.error("确定结算失败",e);
			return JsonResultUtil.getErrorJson("提交失败,请重试");
			
		}
	}
	
}
