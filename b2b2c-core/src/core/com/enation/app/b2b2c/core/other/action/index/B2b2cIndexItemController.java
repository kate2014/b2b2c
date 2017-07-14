package com.enation.app.b2b2c.core.other.action.index;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.b2b2c.core.order.service.IStoreOrderManager;

/**
 * 后台首页显示项
 * @author Kanon
 */
@Scope("prototype")
@Controller
@RequestMapping("/b2b2c/admin/b2b2c-index-item")
public class B2b2cIndexItemController {
	
	@Autowired
	private IStoreOrderManager storeOrderManager;
	
	/**
	 * 统计订单状态
	 * @param orderss订单状态,Map
	 * @return 订单统计页
	 */
	@RequestMapping(value="/order")
	public ModelAndView order(){
		ModelAndView view=new ModelAndView();
		view.setViewName("/b2b2c/admin/index/order");
		view.addObject("orderss", storeOrderManager.censusState());
		return view;
	}

}
