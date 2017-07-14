package com.enation.app.b2b2c.core.goods.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.shop.core.goods.action.GoodsController;
import com.enation.framework.context.webcontext.ThreadContextHolder;

/**
 * 自营店商品管理
 * @author [kingapex]
 * @version [2.0],2016年3月7日 sylow  v60版本改造
 * @since [6.0]
 * 2015年10月12日下午4:33:35
 */
@Controller 
@RequestMapping("/b2b2c/admin/self-store-goods")
public class SelfStoreGoodsController extends GoodsController {
	
	/**
	 * 转到商品列表页面
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/list")
	public ModelAndView list(){
		ModelAndView view= getGridModelAndView();
		String market_enable=ThreadContextHolder.getHttpRequest().getParameter("market_enable");
		view.addObject("market_enable", market_enable);
		view.setViewName("/b2b2c/admin/self/goods/goods_list");
		return view;
	}
	
}
