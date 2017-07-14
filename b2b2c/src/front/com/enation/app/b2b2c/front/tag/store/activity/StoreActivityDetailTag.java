package com.enation.app.b2b2c.front.tag.store.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.shop.core.other.service.IActivityDetailManager;
import com.enation.app.shop.core.other.service.IActivityGiftManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.StringUtil;

import freemarker.template.TemplateModelException;

/**
 * 促销活动优惠详细信息标签
 * @author DMRain
 * @date 2016年6月15日
 * @version v2.0
 * @since v2.0
 */
@Component
public class StoreActivityDetailTag extends BaseFreeMarkerTag{

	@Autowired
	private IActivityDetailManager activityDetailManager;
	@Autowired
	private IActivityGiftManager activityGiftManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String activity_id = request.getParameter("activity_id");
		if (activity_id == null) {
			activity_id = params.get("activity_id").toString();
		}
		List detailList = this.activityDetailManager.listActivityDetail(Integer.parseInt(activity_id));
		Map detailMap=(Map)detailList.get(0);
		if(detailMap.get("is_send_gift").toString().equals("1")){
			detailMap.put("gift_enstore", activityGiftManager.get(StringUtil.toInt(detailMap.get("gift_id"), false)).getEnable_store());
		}
		detailList=new ArrayList();
		detailList.add(detailMap);
		return detailList;
	}

}
