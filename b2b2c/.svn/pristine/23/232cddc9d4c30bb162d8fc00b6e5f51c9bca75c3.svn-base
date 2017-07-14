package com.enation.app.b2b2c.front.api.store;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.b2b2c.core.store.model.Store;
import com.enation.app.b2b2c.core.store.service.IStoreManager;
import com.enation.app.b2b2c.core.store.service.IStoreThemesManager;
import com.enation.framework.action.JsonResult;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.JsonResultUtil;
/**
 * 店铺管理API
 * @author LiFenLong
 *
 */
@Controller
@RequestMapping("/api/b2b2c/store-api")
@Validated
public class StoreApiController {

	protected Logger logger = Logger.getLogger(getClass());

	@Autowired
	private IStoreManager storeManager;
	@Autowired
	private IStoreMemberManager storeMemberManager;
	@Autowired
	private IStoreThemesManager storeThemesManager;
	/**
	 * 查看用户名是否重复
	 * @param storeName 店铺名称,String
	 * @return 返回json串
	 * result 	为1表示调用成功0表示失败
	 */
	@ResponseBody
	@RequestMapping(value="/check-store-name",produces= MediaType.APPLICATION_JSON_VALUE)
	public JsonResult checkStoreName(String storeName){
		if(this.storeManager.checkStoreName(storeName)){
			return JsonResultUtil.getErrorJson("店铺名称重复");
		}else{
			return JsonResultUtil.getSuccessJson("店铺名称可以使用");
		}
	}

	/**
	 * 申请开店
	 * @param store 店铺信息,Store
	 * @return 返回json串
	 * result 	为1表示调用成功0表示失败
	 */
	@ResponseBody
	@RequestMapping(value="/apply",produces= MediaType.APPLICATION_JSON_VALUE)
	public JsonResult apply(Store store){
		try {
			if(null==storeMemberManager.getStoreMember()){
				return JsonResultUtil.getErrorJson("您没有登录不能申请开店");
			}else if(!storeManager.checkStore()){
				//店铺地址信息
				HttpServletRequest request = ThreadContextHolder.getHttpRequest();    
				store.setStore_provinceid(Integer.parseInt(request.getParameter("store_province_id").toString()));	//店铺省ID
				store.setStore_cityid(Integer.parseInt(request.getParameter("store_city_id").toString()));			//店铺市ID
				store.setStore_regionid(Integer.parseInt(request.getParameter("store_region_id").toString()));     //店铺区ID
				String store_town_id=request.getParameter("store_town_id");
				if(store_town_id!=null && !store_town_id.equals("")){ 
					store.setBank_townid(Integer.parseInt(store_town_id));                                       //店铺所在城镇Id
				}
				store.setBank_provinceid(Integer.parseInt(request.getParameter("bank_province_id").toString())); //开户银行所在省Id
				store.setBank_cityid(Integer.parseInt(request.getParameter("bank_city_id").toString()));		  //开户银行所在市Id
				store.setBank_regionid(Integer.parseInt(request.getParameter("bank_region_id").toString()));    //开户银行所在区Id
				String bank_town_id=request.getParameter("bank_town_id");
				if(bank_town_id!=null && !bank_town_id.equals("")){ 
					store.setBank_townid(Integer.parseInt(bank_town_id));                                       //开户银行所在城镇Id
				}
				//暂时先将店铺等级定为默认等级，以后版本升级更改此处
				store.setStore_level(1);
				store.setCreate_time(DateUtil.getDateline());
				//店铺状态
				store.setDisabled(0);
				
				//店铺佣金
				store.setCommission(0.0);
				//设置库存预警默认值
				store.setGoods_warning_count(5);
				storeManager.apply(store);
				return JsonResultUtil.getSuccessJson("申请成功,请等待审核");
			}else{
				return JsonResultUtil.getErrorJson("您已经申请过了，请不要重复申请");
			}
		} catch (Exception e) {
			this.logger.error("申请失败:"+e);
			e.printStackTrace();
			return JsonResultUtil.getErrorJson("申请失败");
		}
	}
	/**
	 * 重试申请开店
	 * @param store 店铺信息,Store
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/re-apply",produces= MediaType.APPLICATION_JSON_VALUE)
	public JsonResult reApply(Store store){
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		try {
			if(null==storeMemberManager.getStoreMember()){
				return JsonResultUtil.getErrorJson("您没有登录不能申请开店");
			}else {
				//添加店铺地址
				//暂时先将店铺等级定为默认等级，以后版本升级更改此处

				store.setStore_provinceid(Integer.parseInt(request.getParameter("store_province_id").toString()));	//店铺省ID
				store.setStore_cityid(Integer.parseInt(request.getParameter("store_city_id").toString()));			//店铺市ID
				store.setStore_regionid(Integer.parseInt(request.getParameter("store_region_id").toString()));		//店铺区ID
				String store_town_id=request.getParameter("store_town_id");
				if(store_town_id!=null && !store_town_id.equals("")){ 
					store.setBank_townid(Integer.parseInt(store_town_id));                                          //店铺行所在区Id
				}

				store.setBank_provinceid(Integer.parseInt(request.getParameter("bank_province_id").toString())); //开户银行所在省Id
				store.setBank_cityid(Integer.parseInt(request.getParameter("bank_city_id").toString()));		  //开户银行所在市Id
				store.setBank_regionid(Integer.parseInt(request.getParameter("bank_region_id").toString()));    //开户银行所在区Id
				String bank_town_id=request.getParameter("bank_town_id");
				if(bank_town_id!=null && !bank_town_id.equals("")){ 
					store.setBank_townid(Integer.parseInt(bank_town_id));                                       //开户银行所在城镇Id
				}

				store.setStore_level(1);
				storeManager.reApply(store);
				return JsonResultUtil.getSuccessJson("提交申请成功,请等待审核");
			}
		} catch (Exception e) {
			this.logger.error("申请失败:"+e);
			e.printStackTrace();
			return JsonResultUtil.getErrorJson("申请失败");
		}
	}

	/**
	 * 修改店铺设置
	 * @param store 店铺信息,Store
	 * @return 返回json串
	 * result 	为1表示调用成功0表示失败
	 */
	@ResponseBody
	@RequestMapping(value="/edit",produces= MediaType.APPLICATION_JSON_VALUE)
	public JsonResult edit(Store store,Integer province_id,Integer city_id,Integer region_id, Integer town_id){
		try {
			//  如果前台没有传参，则后面的地区id设为-1
			//  by: laiyunchuan 2017-01-10 12:05:49
			city_id   = city_id   == null ? -1 : city_id;
			region_id = region_id == null ? -1 : region_id;
			town_id   = town_id   == null ? -1 : town_id;
			
			store.setStore_provinceid(province_id);	//店铺省ID
			store.setStore_cityid(city_id);			//店铺市ID
			store.setStore_regionid(region_id);		//店铺县ID
			store.setStore_townid(town_id );		//店铺镇ID

			this.storeManager.editStore(store);
			return JsonResultUtil.getSuccessJson("修改店铺信息成功");
		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error("修改店铺信息失败:"+e);
			return JsonResultUtil.getErrorJson("修改店铺信息失败");
		}
	}
	/**
	 * 检测身份证
	 * @param id_number 身份证号
	 * @return 返回json串
	 * result 	为1表示调用成功0表示失败
	 */
	@ResponseBody
	@RequestMapping(value="/check-id-number",produces= MediaType.APPLICATION_JSON_VALUE)
	public JsonResult checkIdNumber(String id_number){
		int result=storeManager.checkIdNumber(id_number);
		if(result==0){
			return JsonResultUtil.getSuccessJson("身份证可以使用！");
		}else{
			return JsonResultUtil.getErrorJson("身份证已经存在！");
		}
	}
	/**
	 * 修改店铺Logo
	 * @param logo Logo,String
	 * @return 返回json串
	 * result 	为1表示调用成功0表示失败
	 */
	@ResponseBody
	@RequestMapping(value="/edit-store-logo",produces= MediaType.APPLICATION_JSON_VALUE)
	public JsonResult editStoreLogo( String logo){
		try {
			storeManager.editStoreOnekey("store_logo",logo);
			return JsonResultUtil.getSuccessJson("店铺Logo修改成功");
		} catch (Exception e) {
			this.logger.error("修改店铺Logo失败:"+e);
			return JsonResultUtil.getErrorJson("店铺Logo修改失败");
		}
	}
	/**
	 * 提交店铺认证信息
	 * @param store_id 店铺Id,Integer
	 * @param fsid_img 身份证图片,String
	 * @param fslicense_img 营业执照图片,String
	 * @param store_auth 店铺认证,Integer
	 * @param name_auth 店主认证,Integer
	 * @return 返回json串
	 * result 	为1表示调用成功0表示失败
	 */
	@ResponseBody
	@RequestMapping(value="/store-auth",produces= MediaType.APPLICATION_JSON_VALUE)
	public JsonResult storeAuth(Integer store_id,Integer store_auth, Integer name_auth,String fsid_img,String fslicense_img){
		try {
			storeManager.saveStoreLicense(store_id, fsid_img, fslicense_img, store_auth, name_auth);
			return JsonResultUtil.getSuccessJson("提交成功，等待审核");
		} catch (Exception e) {
			return JsonResultUtil.getErrorJson("提交失败，请重试");
		}
	}
	/**
	 * 更换店铺模板
	 * @param themes_id 店铺模板ID
	 * @return 更换状态
	 */
	@ResponseBody
	@RequestMapping(value="/change-store-themes",produces= MediaType.APPLICATION_JSON_VALUE)
	public JsonResult changeStoreThemes(Integer themes_id){
		try {
			storeThemesManager.changeStoreThemes(themes_id);
			return JsonResultUtil.getSuccessJson("更换成功");
		} catch (Exception e) {
			this.logger.error(e);
			return JsonResultUtil.getErrorJson("更换失败");
		}
	}

	/**
	 * 修改货品预警数设置
	 * @param store 店铺信息,Store
	 * @return 返回json串
	 * result 	为1表示调用成功0表示失败
	 */
	@ResponseBody
	@RequestMapping(value="/warning_count_edit",produces= MediaType.APPLICATION_JSON_VALUE)
	public JsonResult warning_count_edit(@Valid Store store){
		try {
			this.storeManager.editStore(store);
			return JsonResultUtil.getSuccessJson("修改店铺信息成功");
		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error("修改店铺信息失败:"+e);
			return JsonResultUtil.getErrorJson("修改店铺信息失败");
		}
	}
}
