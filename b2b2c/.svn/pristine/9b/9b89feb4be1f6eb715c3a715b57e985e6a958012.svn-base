package com.enation.app.b2b2c.front.tag.store;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.member.service.IStoreMemberCommentManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
/**
 * 店铺咨询评论标签
 * @author LiFenLong
 *
 */
@Component
public class StoreCommentTag extends BaseFreeMarkerTag{
	@Autowired
	private IStoreMemberCommentManager storeMemberCommentManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request=ThreadContextHolder.getHttpRequest();
		return storeMemberCommentManager.get(Integer.parseInt(request.getParameter("comment_id")));
	}
}
