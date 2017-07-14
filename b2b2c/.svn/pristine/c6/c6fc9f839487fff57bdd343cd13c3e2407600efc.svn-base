package com.enation.app.b2b2c.front.api.store;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.b2b2c.core.store.model.StoreDlyType;
import com.enation.app.b2b2c.core.store.model.StoreTemlplate;
import com.enation.app.b2b2c.core.store.service.IStoreDlyTypeManager;
import com.enation.app.b2b2c.core.store.service.IStoreTemplateManager;
import com.enation.app.base.core.service.IRegionsManager;
import com.enation.app.shop.core.order.model.support.DlyTypeConfig;
import com.enation.app.shop.core.order.model.support.TypeAreaConfig;
import com.enation.framework.action.JsonResult;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.JsonResultUtil;
import com.enation.framework.util.StringUtil;

/**
 * 店铺物流模板api
 * 
 * @author xulipeng
 * @author xulipeng	 2016年03月03日  改造springMVC
 */

@Scope("prototype")
@Controller
@RequestMapping("/api/b2b2c/dly-type")
public class StoreDlyTypeApiController {
	@Autowired
	private IStoreDlyTypeManager storeDlyTypeManager;
	@Autowired
	private IStoreTemplateManager storeTemplateManager;
	@Autowired
	private IStoreMemberManager storeMemberManager;
	@Autowired
	private IRegionsManager regionsDbManager;
	
	/**
	 * 添加物流工具
	 * 
	 * @param storeMember
	 *            店铺会员 ,StoreMember
	 * @param store_id
	 *            店铺Id,Integer
	 * @param dlyname
	 *            模板名称,String
	 * @param tplType
	 *            类型,String[]
	 * @param templateid
	 *            模板Id,Integer
	 * @return 返回json串 result 为1表示调用成功0表示失败
	 */
	@ResponseBody
	@RequestMapping(value="/add")
	public JsonResult add(String dlyname,Integer pycount,Integer kdcount,Integer yzcount) {

		StoreMember storeMember = storeMemberManager.getStoreMember();
		int i = this.storeTemplateManager.getStoreTemlpateByName(dlyname,storeMember.getStore_id());

		Integer store_id = storeMember.getStore_id();
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String[] tplType = request.getParameterValues("tplType");

		if (tplType == null) {
			return JsonResultUtil.getErrorJson("未选择配送方式");
		}

		StoreDlyType storeDlyType;
		if (i == 0) {

			StoreTemlplate storeTemlplate = new StoreTemlplate();
			storeTemlplate.setName(dlyname);
			storeTemlplate.setStore_id(storeMember.getStore_id());
			storeTemlplate.setDef_temp(0);
			Integer templateid = this.storeTemplateManager.add(storeTemlplate);
			
			for (String tpl : tplType) {
				storeDlyType = new StoreDlyType();
				storeDlyType.setStore_id(store_id);
				storeDlyType.setTemplate_id(templateid);
				storeDlyType.setIs_same(0);

				//在添加运费模板时，要将disabled字段设置为0，证明运费模板可用
				//add by DMRain 2016-1-19
				storeDlyType.setDisabled(0);
				
				// 平邮
				if (Integer.valueOf(tpl) == 1) {
					storeDlyType.setName("平邮");
					addType(request, "py", pycount,storeDlyType);
				}

				// 快递
				if (Integer.valueOf(tpl) == 2) {
					storeDlyType.setName("快递");
					addType(request, "kd", kdcount,storeDlyType);
				}

				// 邮政
				if (Integer.valueOf(tpl) == 3) {
					storeDlyType.setName("邮政");
					addType(request, "yz", yzcount,storeDlyType);
				}
			}

			// 如果只有一个模板、设置此模板为默认模板
			Integer temp_id = this.storeTemplateManager
					.getDefTempid(storeMember.getStore_id());
			if (temp_id == null) {
				this.storeTemplateManager.setDefTemp(templateid,
						storeMember.getStore_id());
			}
			return JsonResultUtil.getSuccessJson("添加成功");
		} else {
			return JsonResultUtil.getErrorJson("添加失败，模板名称已存在");
		}
	}

	/**
	 * 修改物流工具
	 * 
	 * @param storeMember
	 *            店铺会员 ,StoreMember
	 * @param store_id
	 *            店铺Id,Integer
	 * @param tplType
	 *            类型,String[]
	 * @param storeTemlplate
	 *            店铺物流工具,StoreTemlplate
	 * @param dlyname
	 *            模板名称,String
	 * @param pycount
	 * @param kdcount
	 * @param yzcount
	 * @return 返回json串 result 为1表示调用成功0表示失败
	 */
	@ResponseBody
	@RequestMapping(value="/update")
	public JsonResult update(String dlyname,Integer tempid,Integer pycount,Integer kdcount,Integer yzcount) {
		StoreMember storeMember = storeMemberManager.getStoreMember();
		Integer store_id = storeMember.getStore_id();
		//增加权限
		Map map = storeTemplateManager.getTemplae(store_id, tempid);
		if(map==null){
			return JsonResultUtil.getErrorJson("您没有权限");
		}
		
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String[] tplType = request.getParameterValues("tplType");

		StoreTemlplate storeTemlplate = new StoreTemlplate();
		storeTemlplate.setStore_id(storeMember.getStore_id());
		storeTemlplate.setName(dlyname);
		storeTemlplate.setId(tempid);
		
		//检查当前模板是否为默认模板
		int result = this.storeTemplateManager.checkIsDef(tempid);
		
		//如果当前模板是默认模板 result==1
		if(result == 1){
		    storeTemlplate.setDef_temp(1);
		}else{
		    storeTemlplate.setDef_temp(0);
		}
		
		this.storeTemplateManager.edit(storeTemlplate);
		Integer templateid = tempid;
		this.storeDlyTypeManager.del_dlyType(templateid);
		
		StoreDlyType storeDlyType;
		for (String tpl : tplType) {
			storeDlyType = new StoreDlyType();
			storeDlyType.setStore_id(store_id);
			storeDlyType.setTemplate_id(templateid);
			storeDlyType.setIs_same(0);

			//在修改运费模板时，要将disabled字段设置为0，证明运费模板可用
			//add by DMRain 2016-1-19
			storeDlyType.setDisabled(0);
			
			// 平邮
			if (Integer.valueOf(tpl) == 1) {
				storeDlyType.setName("平邮");
				addType(request, "py", pycount,storeDlyType);
			}

			// 快递
			if (Integer.valueOf(tpl) == 2) {
				storeDlyType.setName("快递");
				addType(request, "kd", kdcount,storeDlyType);
			}

			// 邮政
			if (Integer.valueOf(tpl) == 3) {
				storeDlyType.setName("邮政");
				addType(request, "yz", yzcount,storeDlyType);
			}
		}
		return JsonResultUtil.getSuccessJson("修改成功");
	}

	/**
	 * 添加配送方式
	 * 
	 * @param tpl
	 *            类型,
	 * @param count
	 * @param firstunit
	 * @param firstmoney
	 * @param continueunit
	 * @param continuemoney
	 * @param config
	 *            DlyTypeConfig
	 * @param areaConfig
	 *            TypeAreaConfig
	 */
	private void addType(HttpServletRequest request, String tpl, Integer count,StoreDlyType storeDlyType) {
		

		String firstunit = request.getParameter("default_firstunit_" + tpl);
		String continueunit = request.getParameter("default_continueunit_"
				+ tpl);
		String firstmoney = request.getParameter("default_firstmoney_" + tpl);
		String continuemoney = request.getParameter("default_continueprice_"
				+ tpl);

		DlyTypeConfig config = new DlyTypeConfig();
		config.setFirstunit(StringUtil.toInt(firstunit)); // 首重
		config.setContinueunit(StringUtil.toInt(continueunit)); // 续重

		config.setFirstprice(StringUtil.toDouble(firstmoney)); // 首重费用
		config.setContinueprice(StringUtil.toDouble(continuemoney)); // 续重费用

		config.setIs_same(0); // 店铺都是指定地区的配送方式
		config.setDefAreaFee(1); // 店铺都有默认费用设置
		config.setUseexp(0); // 店铺都不启动公式

		TypeAreaConfig[] configArray = new TypeAreaConfig[count];
		// 指定地区

		for (int i = 1; i <= count; i++) {
			TypeAreaConfig areaConfig = new TypeAreaConfig();
			String firstprice = request.getParameter("express_" + tpl
					+ "_firstmoney_" + i);
			String continueprice = request.getParameter("express_" + tpl
					+ "_continuemoney_" + i);
			String areaids = request.getParameter("express_" + tpl
					+ "_areaids_" + i);
			String areanames = request.getParameter("express_" + tpl
					+ "_areanames_" + i);

			if (firstprice != null && continueprice != null && areaids != null
					&& areanames != null) {

				areaConfig.setFirstprice(StringUtil.toDouble(firstprice,null)); // 首重费用
				areaConfig.setFirstunit(StringUtil.toInt(firstunit,0)); // 首重重量

				areaConfig.setContinueprice(StringUtil.toDouble(continueprice,0.0)); // 续重费用
				areaConfig.setContinueunit(StringUtil.toInt(continueunit,0)); // 续重重量
				areaConfig.setUseexp(0);

				if (areaids != null) {
					
					if(areaids.endsWith(",")){
						areaids = areaids.substring(0,areaids.length() - 1).toString();
					}
					String[] areaid = areaids.split(",");
					//先增加2级地区
					StringBuffer areas = new StringBuffer();
					areas.append(areaids); 
					List tRegions ;
					for (String aid : areaid) {
						tRegions = new ArrayList();
						tRegions = regionsDbManager.listChildren(aid);
						for (int j = 0; j < tRegions.size(); j++) {
							areas.append(","+tRegions.get(j));
						}
					}
					areaConfig.setAreaId(areas.toString()); 
				}

				if (areanames != null ) {
					
					if(areanames.endsWith(",")){
						areaConfig.setAreaName(areanames.substring(0,areanames.length() - 1));
					}else{
						areaConfig.setAreaName(areanames);
					}
				}

				configArray[i - 1] = areaConfig;
			}
		}
		this.storeDlyTypeManager.add(storeDlyType, config, configArray);
	}
}
