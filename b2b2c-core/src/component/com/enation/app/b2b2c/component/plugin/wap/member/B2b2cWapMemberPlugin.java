package com.enation.app.b2b2c.component.plugin.wap.member;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.service.IMemberManager;
import com.enation.app.shop.core.member.plugin.IMemberEditEvent;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.plugin.AutoRegisterPlugin;

/**
 * b2b2c-wap会员插件
 * @author xulipeng
 * @version v1.0
 * @since v6.2.1
 * 2016年12月26日
 */
@Component
public class B2b2cWapMemberPlugin extends AutoRegisterPlugin implements IMemberEditEvent {

	@Autowired
	private IMemberManager memberManager;
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.core.member.plugin.IMemberEditEvent#onEditMember(com.enation.app.base.core.model.Member)
	 */
	@Override
	public void onEditMember(Member member) {
		try {
			HttpServletRequest request = ThreadContextHolder.getHttpRequest();
			if(request.getParameter("file")==null){
				if(request.getParameter("head_fs")!=null){
					this.memberManager.editMemberImg(member.getMember_id(), request.getParameter("head_fs"));
				}
			};
		} catch (Exception e) {
			this.logger.error("B2b2cWapMemberPlugin  修改会员头像出现错误");
		}
	}

}
