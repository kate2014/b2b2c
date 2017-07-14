package com.enation.app.b2b2c.front.tag.store.activity;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.b2b2c.core.store.model.activity.StoreActivityGift;
import com.enation.app.b2b2c.core.store.service.activity.IStoreActivityGiftManager;
import com.enation.eop.processor.core.UrlNotFoundException;
import com.enation.eop.sdk.utils.StaticResourcesUtil;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.StringUtil;

import freemarker.template.TemplateModelException;

/**
 * 获取一条店铺促销活动赠品信息Tag
 * @author DMRain
 * @date 2016年1月14日
 * @version v1.0
 * @since v1.0
 */
@Component
public class ActivityGiftTag extends BaseFreeMarkerTag{
	@Autowired
	private IStoreActivityGiftManager storeActivityGiftManager;
	@Autowired
	private IStoreMemberManager storeMemberManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String gift_id = request.getParameter("gift_id");
		
		//如果赠品id为空则是从标签中传递过来的
		if(gift_id == null && StringUtil.isEmpty(gift_id)){
			if(params.get("gift_id")!=null){
				gift_id = (String) params.get("gift_id");
			}else{
				return new StoreActivityGift();
			}
		}
		
		StoreActivityGift storeActivityGift = this.storeActivityGiftManager.get(Integer.parseInt(gift_id));
//		//增加权限
//		StoreMember member = storeMemberManager.getStoreMember();
//		if(storeActivityGift==null || (!storeActivityGift.getStore_id().equals(member.getStore_id()))){
//			throw new UrlNotFoundException();
//		}
		
		storeActivityGift.setGift_img(StaticResourcesUtil.convertToUrl(storeActivityGift.getGift_img()));
		
		return storeActivityGift;
	}
	
}
