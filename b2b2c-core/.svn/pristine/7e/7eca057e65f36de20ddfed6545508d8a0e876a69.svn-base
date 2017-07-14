package com.enation.app.b2b2c.core.store.action.stock;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.b2b2c.core.goods.service.IB2b2cGoodsStoreManager;
import com.enation.app.shop.core.goods.action.GoodsStoreController;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.util.JsonResultUtil;
 
/**
 * 自营店库存管理
 * @author [kingapex]
 * @version [2.0],2016年3月7日 sylow  v60版本改造
 * @since [6.0]
 * 2015年10月23日下午1:40:55
 */
@Controller 
@RequestMapping("/b2b2c/admin/self-store-goods-stock")
public class SelfStoreGoodsStockController extends GoodsStoreController{
	
	@Autowired
	private IB2b2cGoodsStoreManager b2b2cGoodsStoreManager;
	

	/**
	 * 跳转至商品库存管理页面
	 * @return 商品库存管理页面
	 */
	@RequestMapping(value="/self-list-goods-store")
	public String listGoodsStore(){
		return "/b2b2c/admin/self/goods/self_store_list";
	}
	
	
	
	/**
	 * 获取商品库存管理列表Json
	 * @param stype 搜索类型,Integer
	 * @param keyword 搜索关键字,String
	 * @param name 商品名称,String
	 * @param sn 商品编号,String
	 * @param depot_id 库房Id,Integer
	 * @return 商品库存管理列表Json
	 */
	@ResponseBody
	@RequestMapping(value="/self-list-goods-store-json")
	public GridJsonResult listGoodsStoreJson(Integer stype,String keyword,String name,String sn,Integer depot_id){
		
		Map storeMap = new HashMap();
		storeMap.put("stype", stype);
		storeMap.put("keyword", keyword);
		storeMap.put("name", name);
		storeMap.put("sn", sn);
		depot_id = depot_id == null ? 0 : depot_id;
		storeMap.put("depotid", depot_id);
		
		return JsonResultUtil.getGridJson(b2b2cGoodsStoreManager.listSelfStore(storeMap, this.getPage(), this.getPageSize(),this.getSort(),this.getOrder()));
	}
	
 
	/**
	 * 跳转至商品预警库存管理页面
	 * @return 商品库存管理页面
	 */
	@RequestMapping(value="/self-list-goods-warning-store")
	public String listGoodsWarningStore(){
		return "/b2b2c/admin/self/goodswarningstore/self_warning_store_list";
	}
	
	/**
	 * 获取商品预警库存管理列表Json
	 * @param stype 搜索类型,Integer
	 * @param keyword 搜索关键字,String
	 * @param name 商品名称,String
	 * @param sn 商品编号,String
	 * @param depot_id 库房Id,Integer
	 * @return 商品库存管理列表Json
	 */
	@ResponseBody
	@RequestMapping(value="/self-list-goods-warning-store-json")
	public GridJsonResult listGoodsWarningStoreJson(Integer stype,String keyword,String name,String sn,Integer depot_id){
		
		Map storeMap = new HashMap();
		storeMap.put("stype", stype);
		storeMap.put("keyword", keyword);
		storeMap.put("name", name);
		storeMap.put("sn", sn);
		depot_id = depot_id == null ? 0 : depot_id;
		storeMap.put("depotid", depot_id);
		
		return JsonResultUtil.getGridJson(b2b2cGoodsStoreManager.listSelfWarningStore(storeMap, this.getPage(), this.getPageSize(),this.getSort(),this.getOrder()));
	}
}
