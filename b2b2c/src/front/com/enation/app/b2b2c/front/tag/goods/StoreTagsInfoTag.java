package com.enation.app.b2b2c.front.tag.goods;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.shop.core.goods.service.ITagManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
@Component
/**
 * 获取标签
 * @author LiFenLong
 *
 */
public class StoreTagsInfoTag extends BaseFreeMarkerTag{
	@Autowired
	private ITagManager tagManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		return tagManager.getById(Integer.valueOf(ThreadContextHolder.getHttpRequest().getParameter("tag_id").toString()));
	}
}
