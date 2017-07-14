package com.enation.app.b2b2c.front.api.goods;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.b2b2c.core.goods.model.StoreGoods;
import com.enation.app.b2b2c.core.goods.service.IStoreGoodsManager;
import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.b2b2c.core.store.model.Store;
import com.enation.app.b2b2c.core.store.service.IStoreManager;
import com.enation.app.b2b2c.core.store.service.IStoreTemplateManager;
import com.enation.app.shop.core.goods.service.IGoodsManager;
import com.enation.app.shop.core.goods.service.IProductManager;
import com.enation.app.shop.core.order.service.IOrderManager;
import com.enation.eop.processor.HttpCopyWrapper;
import com.enation.eop.processor.facade.GoodsPreviewParser;
import com.enation.framework.action.JsonResult;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.JsonResultUtil;
import com.enation.framework.util.StringUtil;

/**
 * 店铺商品管理API
 * @author LiFenLong
 *2014-9-15
 */
@Controller
@RequestMapping("/api/b2b2c/store-goods")
public class StoreGoodsApiController{
	@Autowired
	private IGoodsManager goodsManager;
	@Autowired
	private IOrderManager orderManager;
	@Autowired
	private IStoreGoodsManager storeGoodsManager;
	@Autowired
	private IStoreTemplateManager storeTemplateManager;
	@Autowired
	private IStoreMemberManager storeMemberManager;
	@Autowired
	private IStoreManager storeManager;
	@Autowired
	private IProductManager productManager;
	
	protected Logger logger=Logger.getLogger(getClass());
	
	
	/**
	 * 发布商品
	 * @param storeMember	店铺会员,StoreMember
	 * @param storeGoods	店铺商品,StoreGoods
	 * @return 返回json串
	 * result 	为1表示调用成功0表示失败
	 */
	@ResponseBody
	@RequestMapping(value="/add", produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResult add(StoreGoods storeGoods){
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String haveSpec = request.getParameter("haveSpec");
		//如果有规格，验证价格、重量、成本价格式
		if ("1".equals(haveSpec)) {
			String[] prices = request.getParameterValues("prices");
			String[] costs = request.getParameterValues("costs");
			String[] weights = request.getParameterValues("weights");
			if(prices!=null && prices.length>0){
				String errorMessage = "";
				boolean flag = true;
				for(int i = 0;i<prices.length;i++){
					if(!isNumber(prices[i])){
						flag = false;
						errorMessage = "规格价格必须为数字";
						break;
					}
					if(!isNumber(weights[i])){
						flag = false;
						errorMessage = "规格重量必须为数字";
						break;
					}
					if(!isNumber(costs[i])){
						flag = false;
						errorMessage = "规格成本价必须为数字";
						break;
					}
				}
				if(!flag){
					return JsonResultUtil.getErrorJson("商品修改失败:"+errorMessage);
				}
			}
		}
		try {
			StoreMember storeMember = storeMemberManager.getStoreMember();
			int storeid =storeMember.getStore_id();
			
			storeGoods.setStore_id(storeid);
			
			String storeGoodsId=  ThreadContextHolder.getHttpRequest().getParameter("storeGoodsId");
			
			//判断是否已经自动保存草稿了，如果以保存则进入修改商品操作
			if(!StringUtil.isEmpty(storeGoodsId) && Integer.parseInt(storeGoodsId)!=0){
				storeGoods.setGoods_id(Integer.parseInt(storeGoodsId));
				this.edit(storeGoods);
				
			}else{
				if(storeGoods.getGoods_transfee_charge()==0){
					Integer tempid = this.storeTemplateManager.getDefTempid(storeMember.getStore_id());
					if(tempid==null){
						return JsonResultUtil.getErrorJson("店铺未设置默认配送模板，不能设置【买家承担运费】。");
					}
				}
				//2015-05-20新增店铺名称字段-by kingaepx
				Store store  = this.storeManager.getStore(storeid);
				storeGoods.setStore_name(store.getStore_name());
				
				goodsManager.add(storeGoods);
			}
			return JsonResultUtil.getSuccessJson("商品添加成功");	
		} catch (Exception e) {
			logger.error(e);
			return JsonResultUtil.getErrorJson("商品添加失败:"+e.getMessage());
		}
	}
	
	/**
	 * 编辑商品信息
	 * @param  storeGoods	店铺商品,StoreGoods
	 * @return 返回json串
	 * result 	为1表示调用成功0表示失败
	 */
	@ResponseBody
	@RequestMapping(value="/edit", produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResult edit(StoreGoods storeGoods){
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String haveSpec = request.getParameter("haveSpec");
		//如果有规格，验证价格、重量、成本价格式
		if ("1".equals(haveSpec)) {
			String[] prices = request.getParameterValues("prices");
			String[] costs = request.getParameterValues("costs");
			String[] weights = request.getParameterValues("weights");
			if(prices!=null && prices.length>0){
				String errorMessage = "";
				boolean flag = true;
				for(int i = 0;i<prices.length;i++){
					if(!isNumber(prices[i])){
						flag = false;
						errorMessage = "规格价格必须为数字";
						break;
					}
					if(!isNumber(weights[i])){
						flag = false;
						errorMessage = "规格重量必须为数字";
						break;
					}
					if(!isNumber(costs[i])){
						flag = false;
						errorMessage = "规格成本价必须为数字";
						break;
					}
				}
				if(!flag){
					return JsonResultUtil.getErrorJson("商品修改失败:"+errorMessage);
				}
			}
		}
		try {
			StoreMember storeMember = storeMemberManager.getStoreMember();
			if(storeGoods.getGoods_transfee_charge()==0){
				Integer tempid = this.storeTemplateManager.getDefTempid(storeMember.getStore_id());
				if(tempid==null){
					return JsonResultUtil.getErrorJson("店铺未设置默认配送模板，不能设置【买家承担运费】。");
				}
			}
			
			goodsManager.edit(storeGoods);
			
			return JsonResultUtil.getSuccessJson("商品修改成功");
		} catch (Exception e) {
			this.logger.error(e);
			return JsonResultUtil.getErrorJson("商品修改失败:"+e.getMessage());
		}
	}

	/**
	 * 删除商品(将商品添加至回收站)
	 * @author Kanon 2016-7-14 添加判断商品是否已经下单购买如果已经下单购买则无法删除商品
	 * @param goods_id 商品Id,Integer[]型
	 * @return 返回json串
	 * result 	为1表示调用成功0表示失败
	 */
	@ResponseBody
	@RequestMapping(value="/delete-goods", produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResult deleteGoods( Integer[] goods_id){
		try {
			StoreMember member=storeMemberManager.getStoreMember();
			if(member==null){
				return JsonResultUtil.getSuccessJson("请登录！删除商品失败");
			}
			if(goods_id!=null){
				
				//增加权限
				Integer count = goodsManager.getCountByGoodsIds(goods_id,member.getStore_id());
				if(count==null || !count.equals(goods_id.length)){
					return JsonResultUtil.getErrorJson("您没有权限");
				}
				
				//循环判断删除的商品是否已经下单购买，如果已经下单则无法删除商品。
				for (Integer goodsid : goods_id) {
					if (orderManager.checkGoodsInOrder(goodsid)) {
						return JsonResultUtil.getErrorJson("删除失败，此商品已经下单！");
					}
				}
				goodsManager.delete(goods_id);
				return JsonResultUtil.getSuccessJson("商品添加至回收站成功");
			}else{
				return JsonResultUtil.getErrorJson("请选择商品");
			}
		} catch (Exception e) {
			this.logger.error(e);
			return JsonResultUtil.getErrorJson("商品添加至回收站失败");
		}
	}
	
	/**
	 * 下架商品
	 * @param goods_id 商品Id,Integer[]型
	 * @return 返回json串
	 * result 	为1表示调用成功 0表示失败
	 */
	@ResponseBody
	@RequestMapping(value="/under-goods", produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResult underGoods( Integer[] goods_id){
		try {
			StoreMember member=storeMemberManager.getStoreMember();
			if(member==null){
				return JsonResultUtil.getSuccessJson("请登录！删除商品失败");
			}
			if(goods_id!=null){
				//增加权限
				Integer count = goodsManager.getCountByGoodsIds(goods_id,member.getStore_id());
				if(count==null || !count.equals(goods_id.length)){
					return JsonResultUtil.getErrorJson("您没有权限");
				}
				
				goodsManager.under(goods_id);
				return JsonResultUtil.getSuccessJson("商品下架成功，请到仓库中查看下架商品");
			}else{
				return JsonResultUtil.getErrorJson("请选择商品");
			}
		} catch (Exception e) {
			this.logger.error(e);
			return JsonResultUtil.getErrorJson("商品下架失败");
		}
	}
	
	/**
	 * 清除商品
	 * @param goods_id 商品Id,Integer[]型
	 * @return 返回json串
	 * result 	为1表示调用成功0表示失败
	 */
	@ResponseBody
	@RequestMapping(value="/clean-goods", produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResult cleanGoods( Integer[] goods_id){
		try {
			StoreMember member=storeMemberManager.getStoreMember();
			if(member==null){
				return JsonResultUtil.getSuccessJson("请登录！删除商品失败");
			}
			if(goods_id!=null){
				//增加权限
				Integer count = goodsManager.getCountByGoodsIds(goods_id,member.getStore_id());
				if(count==null || !count.equals(goods_id.length)){
					return JsonResultUtil.getErrorJson("您没有权限");
				}
				
				goodsManager.clean(goods_id);
				return JsonResultUtil.getSuccessJson("清除商品成功");
			}else{
			}
		} catch (Exception e) {
			this.logger.error(e);
			return JsonResultUtil.getErrorJson("清除商品失败");
		}
		return null;
	}
	/**
	 * 检验是否有订单购买过此商品
	 * @param productid	货品Id,Integer型
	 * @return 返回json串
	 * result 	为1表示调用成功0表示失败
	 */
	@ResponseBody
	@RequestMapping(value="/check-pro-in-order", produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResult checkProInOrder(Integer productid){
		boolean isinorder = this.orderManager.checkProInOrder(productid);
		if (isinorder) {
			return JsonResultUtil.getErrorJson("此货品已经有顾客购买，如果删除此订单将不能配货发货，请谨慎操作!\n点击确定删除此货品，点击取消保留此货品。");
		}else{
			return JsonResultUtil.getSuccessJson("删除吧");
		}
	}
	/**
	 * 还原商品
	 * @param goods_id 商品Id,Integer[]型
	 * @return 返回json串
	 * result 	为1表示调用成功0表示失败
	 * 
	 */
	@ResponseBody
	@RequestMapping(value="/revert-goods", produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResult revertGoods( Integer[] goods_id) {
		try {
			StoreMember member=storeMemberManager.getStoreMember();
			if(member==null){
				return JsonResultUtil.getSuccessJson("请登录！删除商品失败");
			}
			if(goods_id!=null){
				//增加权限
				Integer count = goodsManager.getCountByGoodsIds(goods_id,member.getStore_id());
				if(count==null || !count.equals(goods_id.length)){
					return JsonResultUtil.getErrorJson("您没有权限");
				}
				
				this.goodsManager.revert(goods_id);
				return JsonResultUtil.getSuccessJson("还原成功");
			}else{
				return JsonResultUtil.getErrorJson("请选择商品");
			}
		} catch (RuntimeException e) {
			logger.error("商品还原失败", e);
			return JsonResultUtil.getErrorJson("还原失败");
		}
	}
	/**
	 * 修改商品库存
	 * @param goods_id 商品Id,Integer[]型
	 * @param store 商品库存,Integer型
	 * @param storeid	店铺Id,Integer型
	 * @return	返回json串
	 * result 	为1表示调用成功0表示失败
	 * 
	 */
	@ResponseBody
	@RequestMapping(value="/save-goods-store", produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResult saveGoodsStore( Integer[] goods_id,Integer store,Integer storeid,Integer[] productIds, Integer[] storeIds,Integer[] storeNum){
		try {
			//增加权限
			StoreMember member=storeMemberManager.getStoreMember();
			StoreGoods goods = storeGoodsManager.getGoods(goods_id[0]);
			if(goods==null || !goods.getStore_id().equals(member.getStore_id())){
				return JsonResultUtil.getErrorJson("您没有权限");
			}
			
			if(productIds!=null && productIds.length>0){
				this.storeGoodsManager.saveGoodsSpecStore(storeIds, goods_id[0], storeNum, productIds);
			}else{
				if(store==null){
					store=0;
				}
				this.storeGoodsManager.saveGoodsStore(storeid,goods_id[0],store);
			}
			return JsonResultUtil.getSuccessJson("保存库存成功");
		} catch (Exception e) {
			this.logger.error("保存库存失败:"+e);
			return JsonResultUtil.getErrorJson("保存库存失败");
		}
	}

	
	
	/**
	 * 店铺内搜索商品（商家）
	 * @param keyword:搜索关键字,String，可为空
	 * @param store_catid 店铺分类id,int ，如果为0，则搜索全部
	 * @param is_groupbuy 是否已经为团购商品.
	 * @return 商品列表， List<Map> 型的json，Map中存的是goods
	 */
	@ResponseBody
	@RequestMapping(value="/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public Object search(){
		try {
			HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();    
			String keyword = request.getParameter("keyword");
			String store_catid = request.getParameter("store_catid");
			StoreMember storeMember = storeMemberManager.getStoreMember();
			if(storeMember==null ) {
				return JsonResultUtil.getErrorJson("尚未登陆，不能使用此API");
			}
			
			
			Map params = new HashMap();
			params.put("keyword", keyword);
			params.put("store_catid", store_catid);
			List<Map> goodsList  = this.storeGoodsManager.storeGoodsList(storeMember.getStore_id(),params);
			return goodsList;
			
		} catch (Exception e) {
			this.logger.error("商品搜索出错",e);
			return JsonResultUtil.getErrorJson("api调用失败"+e.getMessage());
		}
	}
	
	/**
	 * 
	 * 判断商品货号是否存在
	 * @author xulipeng
	 * @param sn：货号
	 * 2015年08月31日22:59:49
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/sn-is-exist", produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResult snIsExist(String sn,Integer goodsid){
		StoreMember member = this.storeMemberManager.getStoreMember();
		int count =  this.productManager.getSnIsExist(sn,goodsid,member.getStore_id());
		if(count==1){
			return JsonResultUtil.getErrorJson("货号已存在！");
		}else{
			return JsonResultUtil.getSuccessJson("ok");
		}
	}
	
	/**
	 * 添加草稿商品
	 * @author xulipeng
	 * @param storeMember	店铺会员,StoreMember
	 * @param storeGoods	店铺商品,StoreGoods
	 * @return 返回json串
	 * result 	为1表示调用成功0表示失败
	 */
	@ResponseBody
	@RequestMapping(value="/add-draft-goods", produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResult addDraftGoods(StoreGoods storeGoods){
		try {
			StoreMember storeMember = storeMemberManager.getStoreMember();
			int storeid =storeMember.getStore_id();
			
			if(storeGoods.getName()==null && storeGoods.getName().equals("") ){
				storeGoods.setName("无标题商品");
			}
			
			storeGoods.setStore_id(storeid);
			
			//2015-05-20新增店铺名称字段-by kingaepx
			Store store  = this.storeManager.getStore(storeid);
			storeGoods.setStore_name(store.getStore_name());
			storeGoods.setMarket_enable(2);
			goodsManager.addPreviewGoods(storeGoods);
			
			return JsonResultUtil.getNumberJson("goods_id", storeGoods.getGoods_id());
		} catch (Exception e) {
			logger.error(e);
			return JsonResultUtil.getErrorJson("商品添加失败:"+e.getMessage());
		}
	}
	
	/**
	 * 修改草稿商品
	 * @author xulipeng
	 * @param  storeGoods	店铺商品,StoreGoods
	 * @return 返回json串
	 * result 	为1表示调用成功0表示失败
	 */
	@ResponseBody
	@RequestMapping(value="/edit-draft-goods", produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResult editaddDraftGoods(StoreGoods storeGoods){
		try {
			
			HttpServletRequest request = ThreadContextHolder.getHttpRequest();
			String storeGoodsId = request.getParameter("storeGoodsId");
			
			if(!StringUtil.isEmpty(storeGoodsId)){
				storeGoods.setGoods_id(Integer.parseInt(storeGoodsId));
			}
			storeGoods.setMarket_enable(2);
			StoreMember storeMember = storeMemberManager.getStoreMember();
			goodsManager.edit(storeGoods);
			
			return JsonResultUtil.getNumberJson("goods_id", storeGoods.getGoods_id());
		} catch (Exception e) {
			this.logger.error(e);
			return JsonResultUtil.getErrorJson(e.getMessage());
		}
	}
	
	/**
	 * 商品预览
	 * @param sn
	 * @param goodsid
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/preview", produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView preview(Integer goodsId){
		
		
		HttpServletRequest request  = ThreadContextHolder.getHttpRequest();
		goodsId = Integer.parseInt(request.getParameter("goodsId"));
		
		HttpCopyWrapper newrequest = new HttpCopyWrapper(request); 
		
		
		String goodsUrl = "/goods-"+goodsId+".html";
		
		newrequest.setServletPath(goodsUrl);
		ThreadContextHolder.setHttpRequest(newrequest);
		
		GoodsPreviewParser parser = new GoodsPreviewParser();
		parser.parse(goodsUrl);
		 
		return null;
	}
	
	
    /* 
     * 判断是否为数字
     * @param str 传入的字符串  
     * @return 是整数返回true,否则返回false  
   */  
     public static boolean isNumber(String str) { 
       Pattern patternInteger = Pattern.compile("^[-\\+]?[\\d]*$");		//是否是整数
       Pattern patternDouble = Pattern.compile("^[-\\+]?[.\\d]*$");  	//是否是小数
       return patternInteger.matcher(str).matches() || patternDouble.matcher(str).matches();    
     }  
	
}
