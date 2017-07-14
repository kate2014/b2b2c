package com.enation.app.b2b2c.front.tag.store;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.b2b2c.core.store.service.IStoreDlyTypeManager;
import com.enation.app.b2b2c.core.store.service.IStoreTemplateManager;
import com.enation.app.shop.core.order.model.support.DlyTypeConfig;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 店铺模板标签
 * 
 * @author xulipeng
 *
 */
@Component
public class StoreTransportListTag extends BaseFreeMarkerTag {
	@Autowired
	private IStoreDlyTypeManager storeDlyTypeManager;
	@Autowired
	private IStoreTemplateManager storeTemplateManager;
	@Autowired
	private IStoreMemberManager storeMemberManager;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		StoreMember storeMember = storeMemberManager.getStoreMember();
		
		//取出属于这个店铺的所有模板
		List<Map> templateList = this.storeTemplateManager.getTemplateList(storeMember.getStore_id());
		
		for(Map map:templateList){
			Integer template_id = (Integer) map.get("id");
			List<Map> dlytypelist = this.storeDlyTypeManager.getDlyTypeList(template_id);
			map.put("dlylist", dlytypelist);
			
			//根据配送方式取出所有的指定地区配送
			for(Map dlymap :dlytypelist){
				Integer type_id = (Integer) dlymap.get("type_id");
				List<Map> arealist = this.storeDlyTypeManager.getDlyTypeAreaList(type_id);
				dlymap.put("arealist", arealist);
				dlymap.put("area", "全国");
				DlyTypeConfig dlyConfig = convertTypeJson((String) dlymap.get("config"));
				dlymap.put("dlyConfig", dlyConfig);
				
				//查询并设置指定地区的运送到地区
				for(Map areamap:arealist){
					areamap.put("area", areamap.get("area_name_group"));
					DlyTypeConfig areaConfig = convertTypeJson((String) areamap.get("config"));
					areamap.put("areaConfig", areaConfig);
				}
			}
		}
		
		return templateList;
	}
	
	private DlyTypeConfig convertTypeJson(String config) {
		
		JSONObject typeJsonObject = JSONObject.fromObject(config);
		DlyTypeConfig typeConfig = (DlyTypeConfig) JSONObject.toBean(
				typeJsonObject, DlyTypeConfig.class);
		
		return typeConfig;
		
	}
}
