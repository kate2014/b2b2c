package com.enation.app.b2b2c.component.bonus.tag;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.component.bonus.model.StoreBonusType;
import com.enation.app.b2b2c.component.bonus.service.IB2b2cBonusManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 查询某一个优惠劵的详细信息
 * @author xulipeng
 * @version v1.0
 * @since v6.2.1
 */
@Component
public class StoreBonusDetailTag extends BaseFreeMarkerTag {
	@Autowired
	private IB2b2cBonusManager b2b2cBonusManager;
	@Override
	protected Object exec(Map param) throws TemplateModelException {
		Integer bonusid = (Integer) param.get("bonusid");
		StoreBonusType bonus = b2b2cBonusManager.get(bonusid);
		return bonus;
	}
	
	

}
