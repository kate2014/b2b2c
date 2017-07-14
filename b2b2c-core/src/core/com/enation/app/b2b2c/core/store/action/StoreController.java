package com.enation.app.b2b2c.core.store.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.b2b2c.component.plugin.store.StorePluginBundle;
import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.b2b2c.core.store.model.Store;
import com.enation.app.b2b2c.core.store.model.StoreThemes;
import com.enation.app.b2b2c.core.store.service.IStoreManager;
import com.enation.app.b2b2c.core.store.service.IStoreThemesManager;
import com.enation.app.base.core.service.IMemberManager;
import com.enation.eop.SystemSetting;
import com.enation.eop.sdk.context.EopSetting;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.action.GridController;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.action.JsonResult;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.JsonResultUtil;
import com.enation.framework.util.StringUtil;
/**
 * 店铺管理
 * @author LiFenLong
 *
 */
@Controller
@RequestMapping("/b2b2c/admin/store")
public class StoreController extends GridController{


	@Autowired
	private IStoreManager storeManager;

	@Autowired
	private IStoreMemberManager storeMemberManager;

	@Autowired
	private IMemberManager memberManager;

	@Autowired
	private StorePluginBundle storePluginBundle;
	
	@Autowired
	private IStoreThemesManager storeThemesManager;
	
	/**
	 * 店铺列表
	 * @return
	 */
	@RequestMapping(value="/store-list")
	public ModelAndView storeList(){
		ModelAndView view=getGridModelAndView();
		view.setViewName("/b2b2c/admin/store/store_list");
		return view;
	}
	/**
	 * 开店申请
	 * @return
	 */
	@RequestMapping(value="/audit-list")
	public ModelAndView auditList(){
		ModelAndView view=getGridModelAndView();
		view.setViewName("/b2b2c/admin/store/audit_list");
		return view;
	}
	/**
	 * 店铺认证审核列表
	 * @return
	 */
	@RequestMapping(value="/license-list")
	public ModelAndView licenseList(){
		ModelAndView view=getGridModelAndView();
		view.setViewName("/b2b2c/admin/store/license_list");
		return view;
	}
	/**
	 * 禁用店铺列表
	 * @return
	 */
	@RequestMapping(value="/disStore-list")
	public ModelAndView disStoreList(){
		ModelAndView view=getGridModelAndView();
		view.setViewName("/b2b2c/admin/store/disStore_list");
		return view;
	}

	/**
	 * 新增店铺验证用户
	 * @return
	 */
	public String opt(){
		return "/b2b2c/admin/store/opt_member";
	}
	/**
	 * 审核店铺
	 * @return
	 */
	@RequestMapping("/pass")
	public ModelAndView pass(Integer storeId,Store store){


		ModelAndView view=new ModelAndView();
		view.addObject("pluginTabs", storePluginBundle.getEditTabList(storeManager.getStore(storeId)));

		view.addObject("store", store);
		view.setViewName("/b2b2c/admin/store/pass");
		return view;
	}

	/**
	 * 店铺列表JSON
	 * @param disabled
	 * @param storeName
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/store-list-json")
	public GridJsonResult storeListJson( Integer disabled,String storeName){
		Map other=new HashMap();
		other.put("disabled", disabled);
		other.put("name", storeName);

		return JsonResultUtil.getGridJson(storeManager.store_list(other,disabled,this.getPage(),this.getPageSize()));
	}
	/**
	 * 审核通过
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/audit-pass")
	public JsonResult auditPass( Integer member_id,Integer pass,Integer store_id,Integer name_auth,Integer store_auth,Double commission, String id_img, String license_img){
		try {
			storeManager.audit_pass(member_id, store_id, pass, name_auth, store_auth,commission, id_img, license_img);

			return JsonResultUtil.getSuccessJson("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error("操作失败:"+e);
			return JsonResultUtil.getSuccessJson("审核失败");
		}
	}

	/**
	 * 禁用店铺
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="dis-store")
	public JsonResult disStore(Integer storeId){
		if(EopSetting.IS_DEMO_SITE){
			return JsonResultUtil.getErrorJson(EopSetting.DEMO_SITE_TIP);
		}

		try {
			storeManager.disStore(storeId);
			return JsonResultUtil.getSuccessJson("店铺关闭成功");
		} catch (Exception e) {
			this.logger.error("店铺关闭失败:"+e);
			return JsonResultUtil.getErrorJson("店铺关闭失败");
		}
	}
	/**
	 * 店铺恢复使用
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/use-store")
	public JsonResult useStore(Integer storeId){
		try {
			storeManager.useStore(storeId);
			return JsonResultUtil.getSuccessJson("店铺恢复使用成功");
		} catch (Exception e) {
			this.logger.error("店铺恢复使用失败"+e);
			return JsonResultUtil.getErrorJson("店铺恢复使用失败");
		}
	}
	/**
	 * 添加店铺
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/save")
	public JsonResult save(Store store){
		try {
			store=assign(store);
			StoreThemes st = storeThemesManager.getDefaultStoreThemes();
			//如果店铺没有磨人模版
			if(st==null){ 
				return JsonResultUtil.getErrorJson("店铺模版异常，请处理店铺模版");
			}
			store.setThemes_id(st.getId());
			store.setThemes_path(st.getPath());
			this.storeManager.registStore(store);
			return JsonResultUtil.getSuccessJson("保存成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResultUtil.getErrorJson("保存失败");
		}
	}

	/**
	 * 注册店铺
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/regist-store")
	public JsonResult registStore(Store store) {
		try {
			store=assign(store);
			// 注册商店,同时注册会员
			this.storeManager.registStore(store);
			return JsonResultUtil.getSuccessJson("保存成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResultUtil.getErrorJson("保存失败");
		}
	}

	/**
	 * 修改店铺
	 * @param storeId
	 * @return
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit(Integer storeId){
		ModelAndView view= new ModelAndView();
		//view.addObject("store", this.storeManager.getStore(storeId));
		//view.addObject("level_list", storeLevelManager.storeLevelList());
		view.addObject("pluginTabs", storePluginBundle.getEditTabList(storeManager.getStore(storeId)));
		view.setViewName("/b2b2c/admin/store/store_edit");

		return view;
	}
	/**
	 * 修改店铺信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/save-edit")
	public JsonResult saveEdit(Store store,Integer pass){
		try {
			/**
			 * 判断审核还是修改状态
			 */
			if(pass!=null){
				storeManager.audit_pass(store.getMember_id(), store.getStore_id(), pass, store.getName_auth(), store.getStore_auth(),store.getCommission(), store.getId_img(), store.getLicense_img());
				return JsonResultUtil.getSuccessJson("修改成功");
			}else{
				store=assign(store);
				Integer disable= store.getDisabled();
				//判断店铺状态 更改店铺状态
				if(!disable.equals(store.getDisabled())){
					if(store.getDisabled()==1){
						storeManager.useStore(store.getStore_id());
					}else{
						storeManager.disStore(store.getStore_id());
					}
				}
				this.storeManager.editStoreInfo(store);
				return JsonResultUtil.getSuccessJson("修改成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return  JsonResultUtil.getErrorJson("修改失败，请稍后重试！");
		}
	}


	/**
	 * 验证用户 
	 * @param uname 会员名称
	 * @param password 密码
	 * @param assign_password 是否验证密码 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/opt-member")
	public JsonResult optMember(String uname,String password,Integer assign_password){
		try {
			StoreMember storeMember= storeMemberManager.getMember(uname);
			//检测是否为新添加的会员
			if(storeMember.getIs_store()==null){
				return JsonResultUtil.getSuccessJson(uname);
			}
			//判断用户是否已经拥有店铺
			if(storeMember.getIs_store()==1){
				return JsonResultUtil.getErrorJson("会员已拥有店铺");
			}
			//验证会员密码
			if(assign_password!=null&&assign_password==1){
				if(!storeMember.getPassword().equals(StringUtil.md5(password))){
					return JsonResultUtil.getErrorJson("密码不正确");
				}
			}
			if(storeMember.getIs_store()==-1){
				return JsonResultUtil.getSuccessJson(uname);
			}else{
				return JsonResultUtil.getSuccessJson("2");
			}
		} catch (Exception e) {
			return JsonResultUtil.getErrorJson("没有此用户");
		}

	}

	/**
	 * 跳转到店铺添加页面
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/add")
	public ModelAndView add(String uname){
		//		level_list=storeLevelManager.storeLevelList(); 
		ModelAndView view=new ModelAndView();
		view.addObject("uname", uname);
		view.addObject("pluginTabs", storePluginBundle.getAddTabList());
		view.setViewName("/b2b2c/admin/store/store_add");
		return view;
	}
	/**
	 * 跳转到申请信息页面
	 * @return
	 */
	@RequestMapping(value="/auth-list")
	public ModelAndView authList(){
		ModelAndView view=getGridModelAndView();
		view.setViewName("/b2b2c/admin/store/auth_list");
		return view;
	}

	/**
	 * 审核json 列表
	 * @param other
	 * @param disabled
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/auth-list-json")
	public GridJsonResult authListJson( Map other, Integer disabled){
		return  JsonResultUtil.getGridJson(storeManager.auth_list(other, disabled, this.getPage(), this.getPageSize()));
	}
	/**
	 * 审核店铺认证
	 * @param storeId 店铺Id
	 * @param name_auth 店主认证
	 * @param store_auth 店铺认证
	 */
	@ResponseBody
	@RequestMapping(value="/auth-pass")
	public JsonResult authPass(Integer storeId,Integer name_auth,Integer store_auth){
		try{
			storeManager.auth_pass(storeId, name_auth, store_auth);
			return JsonResultUtil.getSuccessJson("操作成功");
		}catch(Exception e){
			this.logger.error("审核店铺认证失败:"+e);
			return JsonResultUtil.getErrorJson("操作失败");
		}
	}
 

	/**
	 * 获取店铺信息
	 * @return
	 */
	private Store assign(Store store){
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		//由于该方法为很多方法使用，店铺添加时，参数可能不一致，所以此处废话多，by fengkun
		String province_id = request.getParameter("province_id")==null?request.getParameter("store_province_id"):request.getParameter("province_id");
		String city_id = request.getParameter("city_id")==null?request.getParameter("store_city_id"):request.getParameter("city_id");
		String region_id = request.getParameter("region_id")==null?request.getParameter("store_region_id"):request.getParameter("region_id");
		String town_id = request.getParameter("town_id")==null?request.getParameter("store_town_id"):request.getParameter("town_id");
		
		
		//店铺地址信息
		store.setStore_provinceid(Integer.parseInt(province_id));	//店铺省ID
		store.setStore_cityid(Integer.parseInt(city_id));			//店铺市ID
		store.setStore_regionid(Integer.parseInt(region_id));		//店铺区ID
//		String town_id=request.getParameter("town_id");
		if(town_id!=null && !town_id.equals("")){ 
			store.setBank_townid(Integer.parseInt(town_id));                                       //店铺所在城镇Id
		}
		String province = request.getParameter("province")==null?request.getParameter("store_province"):request.getParameter("province");
		String city = request.getParameter("city")==null?request.getParameter("store_city"):request.getParameter("city");
		String region = request.getParameter("region")==null?request.getParameter("store_region"):request.getParameter("region");
		String town = request.getParameter("town")==null?request.getParameter("store_town"):request.getParameter("town");
		store.setStore_province(province);	//店铺省
		store.setStore_city(city);			//店铺市
		store.setStore_region(region);		//店铺区
//		String town=request.getParameter("town");
		if(town!=null && !town.equals("")){ 
			store.setBank_town(town);                          //店铺所在城镇
		}
		store.setAttr(request.getParameter("attr"));						//店铺详细地址
		//店铺银行信息
		store.setBank_account_name(request.getParameter("bank_account_name")); 		//银行开户名   
		store.setBank_account_number(request.getParameter("bank_account_number")); 	//公司银行账号 
		store.setBank_name(request.getParameter("bank_name")); 						//开户银行支行名称
		store.setBank_code(request.getParameter("bank_code")); 						//支行联行号

		String bank_province_id = request.getParameter("province_id")==null?request.getParameter("bank_province_id"):request.getParameter("province_id");
		String bank_city_id = request.getParameter("city_id")==null?request.getParameter("bank_city_id"):request.getParameter("city_id");
		String bank_region_id = request.getParameter("region_id")==null?request.getParameter("bank_region_id"):request.getParameter("region_id");
		String bank_town_id = request.getParameter("town_id")==null?request.getParameter("bank_town_id"):request.getParameter("town_id");
		
		
		store.setBank_provinceid(Integer.parseInt(bank_province_id)); //开户银行所在省Id
		store.setBank_cityid(Integer.parseInt(bank_city_id));		  //开户银行所在市Id
		store.setBank_regionid(Integer.parseInt(bank_region_id));    //开户银行所在区Id
//		String bank_town_id=request.getParameter("town_id");
		if(bank_town_id!=null && !bank_town_id.equals("")){ 
			store.setBank_townid(Integer.parseInt(bank_town_id));                                       //开户银行所在城镇Id
		}
		String bank_province = request.getParameter("province")==null?request.getParameter("bank_province"):request.getParameter("province");
		String bank_city = request.getParameter("city")==null?request.getParameter("bank_city"):request.getParameter("city");
		String bank_region = request.getParameter("region")==null?request.getParameter("bank_region"):request.getParameter("region");
		String bank_town = request.getParameter("town")==null?request.getParameter("bank_town"):request.getParameter("town");
		store.setBank_province(bank_province);	//开户银行所在省
		store.setBank_city(bank_city);			//开户银行所在市
		store.setBank_region(bank_region);		//开户银行所在区
//		String bank_town=request.getParameter("town");
		if(bank_town_id!=null && !bank_town_id.equals("")){ 
			store.setBank_town(bank_town);                          //开户银行所在城镇
		}
		return store;
	}

}
