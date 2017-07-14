package com.enation.app.b2b2c.core.store.action.stock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.b2b2c.core.goods.service.IStoreGoodsSpecManager;

/**
 * 店铺商品库存信息
 * @author fenlongli
 *
 */
@Controller
@RequestMapping("/b2b2c/admin/store-goods-stock")
public class StoreGoodsStockController {
	
	@Autowired
	private IStoreGoodsSpecManager storeGoodsSpecManager;
	
	/**
	 * 跳转至商品库存管理页面
	 * @return 商品库存管理页面
	 */
	@RequestMapping("/list-goods-store")
	public String listGoodsStore(){
		return "/b2b2c/admin/goodsstore/goodsstore_list";
	}
	
	/**
	 * 获取库存维护对话框页面html;
	 * @param goodsid 商品Id,Integer
	 * @return
	 */
	@RequestMapping("/get-stock-dialog-html")
	public ModelAndView getStockDialogHtml(Integer goodsid) {
		
		ModelAndView view= new ModelAndView();
		view.addObject("goodsid", goodsid);
		view.addObject("html", storeGoodsSpecManager.getStoreHtml(goodsid));
		view.setViewName("/b2b2c/admin/goodsstore/dialog");
		return view;
	}


}
