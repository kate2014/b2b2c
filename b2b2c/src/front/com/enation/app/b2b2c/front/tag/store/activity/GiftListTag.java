package com.enation.app.b2b2c.front.tag.store.activity;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.b2b2c.core.store.model.activity.StoreActivityGift;
import com.enation.app.b2b2c.core.store.service.activity.IStoreActivityGiftManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 获取店铺所有促销活动赠品集合Tag
 * @author DMRain
 * @date 2016年1月28日
 * @version v1.0
 * @since v1.0
 */
@Component
public class GiftListTag extends BaseFreeMarkerTag{
	@Autowired
	private IStoreActivityGiftManager storeActivityGiftManager;
	@Autowired
	private IStoreMemberManager storeMemberManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		StoreMember member = this.storeMemberManager.getStoreMember();
		List<StoreActivityGift> giftList = this.storeActivityGiftManager.listAll(member.getStore_id());
		return giftList;
	}
}
