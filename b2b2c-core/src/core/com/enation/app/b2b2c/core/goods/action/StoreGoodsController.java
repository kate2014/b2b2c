package com.enation.app.b2b2c.core.goods.action;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.b2b2c.core.goods.service.IStoreGoodsManager;
import com.enation.app.shop.core.goods.model.support.GoodsEditDTO;
import com.enation.app.shop.core.goods.plugin.GoodsPluginBundle;
import com.enation.app.shop.core.goods.service.IGoodsCatManager;
import com.enation.app.shop.core.goods.service.IGoodsManager;
import com.enation.framework.action.GridController;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.action.JsonResult;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.JsonResultUtil;

@Controller
@RequestMapping("/b2b2c/admin/store-goods")
public class StoreGoodsController extends GridController {
	
	@Autowired
	private IGoodsCatManager goodsCatManager;
	
	@Autowired
	private IGoodsManager goodsManager;
	
	@Autowired
	private GoodsPluginBundle goodsPluginBundle;
	
	@Autowired
	private IStoreGoodsManager storeGoodsManager;
	/**
	 * 商品列表
	 * @param brand_id 品牌Id,Integer
	 * @param catid 商品分类Id,Integer
	 * @param name 商品名称,String
	 * @param sn 商品编号,String 
	 * @param tagids 商品标签Id,Integer[]
	 * @return 商品列表页
	 */
	@RequestMapping(value="/list")
	public ModelAndView list() {
		String market_enable=ThreadContextHolder.getHttpRequest().getParameter("market_enable");
		ModelAndView view= getGridModelAndView();
		view.addObject("optype","no");
		view.addObject("market_enable",market_enable);
		view.setViewName("/b2b2c/admin/goods/goods_list");
		return view;
	}


	/**
	 * 跳转到商品详细页
	 * @param catList 商品分类列表,List
	 * @param actionName 修改商品方法,String
	 * @param is_edit 是否为修改商品,boolean
	 * @param goodsView 商品信息,Map
	 * @param pluginTabs 商品tab标题List,List
	 * @param pluginHtmls 商品添加内容List,List
	 * @return 商品详细页
	 */
	
	@RequestMapping(value="/edit")
	public ModelAndView edit(Integer goodsId,String actionName) {
		
		ModelAndView view =new ModelAndView();
		GoodsEditDTO editDTO = this.goodsManager.getGoodsEditData(goodsId);

		view.addObject("pluginTabs",editDTO.getTabs());
		
		
		view.addObject("actionName", "/shop/admin/goods/save-edit.do");
		view.addObject("is_edit", true);
		view.addObject("catList", goodsCatManager.listAllChildren(0));
		view.addObject("editDTO", editDTO);
		view.addObject("goodsView", editDTO.getGoods());
		view.setViewName("/b2b2c/admin/goods/goods_input.html");
		return view;
	}
	
	/**
	 * 跳转至审核商品列表
	 * @return
	 */
	@RequestMapping(value="/auth-list")
	public ModelAndView authList(){
		ModelAndView view =this.getGridModelAndView();
		view.setViewName("/b2b2c/admin/goods/auth/auth_list");
		return view;
	}
	
	/***
	 * 跳转至审核商品页
	 * @param goodsId
	 * @return
	 */
	@RequestMapping(value="/auth-input")
	public ModelAndView authInput(Integer goodsId){
		ModelAndView view=new ModelAndView();
		view.addObject("storeGoods", storeGoodsManager.getGoods(goodsId));
		view.setViewName("/b2b2c/admin/goods/auth/auth_input");
		return view;
	}
	/**
	 * 审核商品JSON列表
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/auth-list-json")
	public GridJsonResult authListJson(){
		return JsonResultUtil.getGridJson(storeGoodsManager.authStoreGoodsList(this.getPage(), this.getPageSize()));
	}
	
	/**
	 * 审核商品
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/pass")
	public JsonResult pass(Integer goodsId,Integer pass,String message){
		try {
			storeGoodsManager.authStoreGoods(goodsId, pass, message);
			return JsonResultUtil.getSuccessJson("审核成功");
		} catch (Exception e) {
			return JsonResultUtil.getErrorJson("审核失败");
		}
	}
}
