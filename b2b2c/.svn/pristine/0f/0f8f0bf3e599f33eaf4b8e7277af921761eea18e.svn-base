package com.enation.app.b2b2c.front.api.goods;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enation.app.b2b2c.core.goods.model.StoreTag;
import com.enation.app.b2b2c.core.goods.service.IStoreGoodsTagManager;
import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.shop.core.goods.service.ITagManager;
import com.enation.framework.action.GridController;
import com.enation.framework.action.JsonResult;
import com.enation.framework.util.JsonResultUtil;
/**
 * 店铺商品标签
 * @author LiFenLong
 *
 */
@Controller
@RequestMapping("/api/b2b2c/store-goods-tag")
public class StoreGoodsTagsController extends GridController{
	@Autowired
	private IStoreGoodsTagManager storeGoodsTagManager;
	@Autowired
	private IStoreMemberManager storeMemberManager;
	@Autowired
	private ITagManager tagManager;
	
	
	/**
	 * 添加店铺商品标签
	 * @param member 店铺会员,StoreMember
	 * @param tag	店铺商品标签,StoreTag
	 * @return	返回json串
	 * result 	为1表示调用成功0表示失败
	 */
	@ResponseBody
	@RequestMapping(value="add")
	public JsonResult add(StoreTag tag){
		try {
			StoreMember member=storeMemberManager.getStoreMember();
			tag.setRel_count(0);
			tag.setStore_id(member.getStore_id());
			storeGoodsTagManager.add(tag);
			return JsonResultUtil.getSuccessJson("添加成功");
		} catch (Exception e) {
			this.logger.error("商品标签添加失败"+e);
			return JsonResultUtil.getErrorJson("添加失败");
		} 
	}
	/**
	 * 修改店铺商品标签
	 * @param tag 店铺商品标签,StoreTag
	 * @return 返回json串
	 * result 	为1表示调用成功0表示失败
	 */
	@ResponseBody
	@RequestMapping(value="edit")
	public JsonResult edit(StoreTag tag){
		try {
			tagManager.update(tag);
			return JsonResultUtil.getSuccessJson("修改成功");
		} catch (Exception e) {
			this.logger.error("商品标签修改失败:"+e);
			return JsonResultUtil.getErrorJson("修改失败");
		} 
	}
	/**
	 * 删除店铺商品标签
	 * @param tag_id 商品标签Id, Integer[]
	 * @return  返回json串
	 * result 	为1表示调用成功0表示失败
	 */
	public JsonResult delete(Integer[] tag_id){
		try {
			tagManager.delete(tag_id);
			return JsonResultUtil.getSuccessJson("删除成功");
		} catch (Exception e) {
			this.logger.error("标签删除失败:"+e);
			return JsonResultUtil.getErrorJson("删除失败");
		} 
	}
	/**
	 * 删除商品标签引用
	 * @param tagId 商品标签Id,Integer
	 * @param reg_id 商品标签引用Id,Integer[]
	 * @return 返回json串
	 * result 	为1表示调用成功0表示失败
	 */
	@ResponseBody
	@RequestMapping(value="delete-rel")
	public JsonResult deleteRel(Integer tagId,Integer[] reg_id){
		try {
			//增加权限检验
			StoreMember member=storeMemberManager.getStoreMember();
			if(member==null){
				return JsonResultUtil.getSuccessJson("请登录！删除标签引用失败");
			}
			StoreTag storeGoodsTag = storeGoodsTagManager.getStoreGoodsTag(tagId);
			if(storeGoodsTag==null || !member.getStore_id().equals(storeGoodsTag.getStore_id())){
				return JsonResultUtil.getSuccessJson("您没有权限");
			}
			
			storeGoodsTagManager.deleteRel(tagId, reg_id);
			return JsonResultUtil.getSuccessJson("删除成功");
		} catch (Exception e) {
			this.logger.error("标签引用删除失败:"+e);
			return JsonResultUtil.getErrorJson("删除失败");
		} 
	}
	/**
	 * 添加商品标签引用
	 * @param tagId 商品标签Id,Integer
	 * @param reg_id 商品标签引用Id,Integer[]
	 * @return 返回json串
	 * result 	为1表示调用成功0表示失败
	 */
	@ResponseBody
	@RequestMapping(value="save-rel")
	public JsonResult saveRel(Integer tagId,Integer[] reg_id){
		try {
			if(reg_id!=null){
				storeGoodsTagManager.addRels(tagId, reg_id);
				return JsonResultUtil.getSuccessJson("保存成功");
			}else{
				return JsonResultUtil.getErrorJson("未选择商品");
			}
		} catch (Exception e) {
			this.logger.error("标签引用保存失败:"+e);
			return JsonResultUtil.getErrorJson("保存失败");
		} 
	}
	/**
	 * 修改商品标签引用排序
	 * @param tagId 商品标签Id,Integer
	 * @param regId 商品标签引用Id,Integer[]
	 * @param ordernum 排序顺序,Integer[]
	 * @return 返回json串
	 * result 	为1表示调用成功0表示失败
	 */
	@ResponseBody
	@RequestMapping(value="save-sort")
	public JsonResult saveSort(Integer tagId,Integer[] regId,Integer[] ordernum){
		try {
			storeGoodsTagManager.saveSort(tagId, regId, ordernum);
			return JsonResultUtil.getSuccessJson("保存成功");
		} catch (Exception e) {
			this.logger.error("商品标签保存失败:"+e);
			return JsonResultUtil.getErrorJson("保存失败");
		} 
	}
	
}
