package com.enation.app.b2b2c.front.api.goods;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enation.app.b2b2c.core.goods.model.StoreCat;
import com.enation.app.b2b2c.core.goods.service.IStoreGoodsCatManager;
import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.base.core.plugin.job.JobExecutePluginsBundle;
import com.enation.app.shop.core.goods.service.IGoodsCatManager;
import com.enation.framework.action.JsonResult;
import com.enation.framework.util.JsonResultUtil;

import net.sf.json.JSONArray;

/**
 * 商品分类API
 * @author LiFenLong
 *2014-9-15
 */
@Controller
@RequestMapping("/api/b2b2c/goods-cat")
public class GoodsCatApiController {
	@Autowired
	private IStoreMemberManager storeMemberManager;
	@Autowired
	private IStoreGoodsCatManager storeGoodsCatManager;
	@Autowired
	private IGoodsCatManager goodsCatManager;

	@Autowired
	private JobExecutePluginsBundle jobExecutePluginsBundle;
	
	
	/**
	 * 获取子类别
	 * @param	cat_id 	父分类Id ,Integer型
	 * @return 	返回json串
	 * result	为1表示调用成功0表示失败
	 */
	@ResponseBody
	@RequestMapping(value="/get-store-goods-child-json", produces = MediaType.APPLICATION_JSON_VALUE)
	public Object getStoreGoodsChildJson(Integer cat_id){
		try {
			JsonResult jsonResult=new JsonResult();
			jsonResult.setResult(1);
			jsonResult.setData(this.goodsCatManager.listChildren(cat_id));
			return jsonResult;
			
		} catch (Exception e) {
			return JsonResultUtil.getErrorJson("加载出错");
		}
	}
	/**
	 * 添加商品分类
	 * @param store_cat_name 店铺分类名称,String型
	 * @param store_cat_pid	店铺分类父Id,Integer型
	 * @param store_sort	店铺分类排序,Integer型
	 * @param disable		店铺分类状态,Integer型
	 * @param storeMember。store_id 店铺Id,Integer型
	 * @return 	返回json串
	 * result	为1表示调用成功0表示失败
	 * 
	 */
	@ResponseBody
	@RequestMapping(value="/add-goods-cat", produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResult addGoodsCat(String store_cat_name,Integer store_cat_pid,Integer store_sort,Integer disable){
		StoreCat storeCat = new StoreCat();
		StoreMember storeMember = storeMemberManager.getStoreMember();
		try {
			storeCat.setStore_cat_name(store_cat_name);
			storeCat.setStore_cat_pid(store_cat_pid);
			storeCat.setSort(store_sort);
			storeCat.setDisable(disable);
			storeCat.setStore_id(storeMember.getStore_id());
			
			int count = this.storeGoodsCatManager.getStoreCatNum(storeMember.getStore_id(),store_cat_pid,store_sort);
			if(count==0){
				this.storeGoodsCatManager.addStoreCat(storeCat);
				return JsonResultUtil.getSuccessJson("保存成功");
			}else{
				return JsonResultUtil.getErrorJson("此分类排序已存在");
			}
			
		} catch (Exception e) {
			return JsonResultUtil.getErrorJson("保存失败");
		}
	}
	/**
	 * 修改店铺商品分类
	 * @param cat_id 	分类Id,Integer型
	 * @param store_cat_pid	店铺分类父Id,Integer型
	 * @param store_cat_name	店铺分类名称,String型
	 * @param store_sort		店铺分类排序,Integer型
	 * @param disable			店铺分类状态,Integer型
	 * @param cat_id			分类Id,Integer型
	 * @param storeCat			店铺分类对象,StoreCat
	 * @return	返回json串
	 * result 	为1表示调用成功0表示失败
	 */
	@ResponseBody
	@RequestMapping(value="/edit-goods-cat", produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResult editGoodsCat(Integer cat_id,String store_cat_name,Integer store_cat_pid,Integer store_sort,Integer disable){
		StoreCat storeCat = new StoreCat();
		try {
			//增加权限验证
			StoreMember member=storeMemberManager.getStoreMember();
			if(member==null){
				return JsonResultUtil.getSuccessJson("请登录！操作失败");
			}
			Map map = new HashMap();
			map.put("storeid", member.getStore_id());
			map.put("store_catid", cat_id);
			StoreCat cat = storeGoodsCatManager.getStoreCat(map);
			if(cat == null || cat.getStore_id() == null){
				return JsonResultUtil.getErrorJson("您没有权限");
			}
			
			int pid = this.storeGoodsCatManager.is_children(cat_id);
			
			if(pid==0 && store_cat_pid!=pid){
				return JsonResultUtil.getErrorJson("顶级分类不可修改上级分类");
			}
			
			storeCat.setStore_cat_name(store_cat_name);
			storeCat.setStore_cat_pid(store_cat_pid);
			storeCat.setSort(store_sort);
			storeCat.setDisable(disable);
			storeCat.setStore_cat_id(cat_id);
			
			this.storeGoodsCatManager.editStoreCat(storeCat);
			return JsonResultUtil.getSuccessJson("保存成功");
		} catch (Exception e) {
			return JsonResultUtil.getErrorJson("保存失败");
		}
		
	}
	/**
	 * 删除店铺商品分类
	 * @param cat_id		分类Id,Integer型
	 * @param cat_name		分类名称,String型	
	 * @return 返回json串
	 * result 	为1表示调用成功0表示失败
	 */
	@ResponseBody
	@RequestMapping(value="/delete", produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResult delete(Integer cat_id,String cat_name){
		
		try {
			StoreMember member = this.storeMemberManager.getStoreMember();
			this.storeGoodsCatManager.deleteStoreCat(cat_id,member.getStore_id());
			return JsonResultUtil.getSuccessJson("删除成功");
		} catch (RuntimeException e) {
			String str_message = e.getMessage().replaceAll("\\*", "【"+cat_name+"】");
			return JsonResultUtil.getErrorJson(str_message);
		}catch (Exception e) {
			return JsonResultUtil.getErrorJson("删除失败");
		}
	}
	/**
	 * 批量删除店铺商品分类
	 * @param catids	店铺分类,String型
	 * @param catnames	店铺分类名称,String型
	 * @return 	返回json串
	 * result 	为1表示调用成功0表示失败
	 */
	@ResponseBody
	@RequestMapping(value="/del-all")
	public JsonResult delAll(String catids,String catnames){
		String catname=null;
		try {
			StoreMember member = this.storeMemberManager.getStoreMember();
			String[] str_catid = catids.split(",");
			String[] str_catname =catnames.split(",");
			for(int i=0;i<str_catid.length;i++){
				String catid = str_catid[i];
				catname = str_catname[i];
				this.storeGoodsCatManager.deleteStoreCat(Integer.parseInt(catid),member.getStore_id());
			}
			return JsonResultUtil.getSuccessJson("删除成功");
		} catch (RuntimeException e) {
			String str_message = e.getMessage().replaceAll("\\*", "【"+catname+"】");
			return JsonResultUtil.getErrorJson(str_message);
		}catch (Exception e) {
			return JsonResultUtil.getErrorJson("删除失败");
		}
	}
	
	@ResponseBody
	@RequestMapping(value="/add-list-json", produces = MediaType.APPLICATION_JSON_VALUE)
	public String addlistJson() {
		List addlist = goodsCatManager.listAllChildren(0);
		String s = JSONArray.fromObject(addlist).toString();
		
		return s.replace("name", "text").replace("cat_id", "id");

	}
	
	@ResponseBody
	@RequestMapping(value="/cat-list-json", produces = MediaType.APPLICATION_JSON_VALUE)
	public Object addStorelistJson() {
		List addlist = goodsCatManager.listAllChildren(0);
		String s = JSONArray.fromObject(addlist).toString();
		return s.replace("name", "text").replace("cat_id", "id");

	}
}

