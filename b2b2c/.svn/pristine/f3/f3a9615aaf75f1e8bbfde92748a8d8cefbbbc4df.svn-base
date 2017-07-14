package com.enation.app.b2b2c.front.api.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enation.app.b2b2c.core.order.service.IStoreOrderPrintManager;
import com.enation.framework.action.GridController;

/**
 * 店铺订单发货 API
 * @author fenlongli
 *
 */
@Controller
@Scope("prototype")
@RequestMapping("/api/b2b2c/store-order-print")
public class StoreOrderPrintApiController extends GridController{
	@Autowired
	private IStoreOrderPrintManager storeOrderPrintManager;
	
	/**
	 * 打印发货单
	 * @param script 打印的script,String
	 * @return 发货单的script
	 */
	@ResponseBody
	@RequestMapping(value="ship-script")
	public String shipScript(Integer order_id) {
		String script= storeOrderPrintManager.getShipScript(order_id);
		return script; 
	}

	
}
