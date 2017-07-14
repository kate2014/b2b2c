package com.enation.app.b2b2c.core.goods.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.b2b2c.core.goods.service.IB2b2cGoodsTagManager;
import com.enation.framework.action.GridController;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.util.JsonResultUtil;
/**
 * 商品标签action
 * @author LiFenLong
 *
 */
@Controller
@RequestMapping("/b2b2c/admin/b2b2c-tag")
public class B2b2cTagController extends GridController{
	@Autowired
	private IB2b2cGoodsTagManager b2b2cGoodsTagManager;
	/**
	 * 商品标签列表
	 * @return
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(){
		
		ModelAndView view=getGridModelAndView();
		view.setViewName("/b2b2c/admin/tags/tag_list");
		return view;
	}
	
	@ResponseBody
	@RequestMapping(value="/list-json")
	public GridJsonResult listJson(){
		return JsonResultUtil.getGridJson(b2b2cGoodsTagManager.list(this.getPage(), this.getPageSize()));
		
	}
	
}
