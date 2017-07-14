package com.enation.app.b2b2c.front.tag.member;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.b2b2c.core.store.service.IStoreManager;
import com.enation.app.base.core.model.Member;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;


/**
 * 检查b2b2c会员
 * @author LiFenLong
 *
 */
@Component
public class CheckB2b2cMemberTag extends BaseFreeMarkerTag {
	@Autowired
	private IStoreMemberManager storeMemberManager;
	@Autowired
	private IStoreManager storeManager;

	@Override
	/**
	 * 获取当前登录会员
	 */
	protected Object exec(Map params) throws TemplateModelException {
		String ctx = this.getRequest().getContextPath();
		if ("/".equals(ctx)) {
			ctx = "";
		}
		StoreMember member = storeMemberManager.getStoreMember();

		if (member == null) {
			HttpServletResponse response = ThreadContextHolder
					.getHttpResponse();
			try {
				String forwardUrl = getFullURL(ThreadContextHolder.getHttpRequest());
				response.sendRedirect(ctx + "/store/login.html?forward=" + forwardUrl);
				return new Member();
			} catch (IOException e) {
				e.printStackTrace();
				return new Member();
			}
		} else {
			member = storeMemberManager.getMember(member.getMember_id());
			//如果登录的会员拥有店铺则在session 中存入店铺信息
			if(member.getStore_id()!=null&&member.getIs_store()!=0){
				ThreadContextHolder.getSession().setAttribute(IStoreMemberManager.CURRENT_STORE_MEMBER_KEY, member);
				ThreadContextHolder.getSession().setAttribute(IStoreManager.CURRENT_STORE_KEY, storeManager.getStore(member.getStore_id()));
			}
			return member;
		}
	}

	/**
	 * 获取完整的url
	 * @param request
	 * @return
	 */
	public String getFullURL(HttpServletRequest request) {
		StringBuffer url = request.getRequestURL();
		if (request.getQueryString() != null) {
			url.append('?');
			url.append(request.getQueryString());
		}
		return url.toString();
	}
}
