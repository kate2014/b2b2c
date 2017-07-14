package com.enation.app.b2b2c.component.bonus.tag;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.component.bonus.model.StoreBonusType;
import com.enation.app.b2b2c.component.bonus.service.IB2b2cBonusManager;
import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 获取店铺所有有效的优惠券集合Tag
 * @author DMRain
 * @date 2016年1月19日
 * @version v1.0
 * @since v1.0
 */
@Component
public class BonusListTag extends BaseFreeMarkerTag{
	@Autowired
	private IB2b2cBonusManager b2b2cBonusManager;
	@Autowired
	private IStoreMemberManager storeMemberManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		StoreMember member = this.storeMemberManager.getStoreMember();
		List<StoreBonusType> bonusList = this.b2b2cBonusManager.getList(member.getStore_id());
		return bonusList;
	}
}
