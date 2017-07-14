package com.enation.app.b2b2c.core.goods.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.b2b2c.core.goods.service.ISelfGoodsCommentsManager;
import com.enation.app.shop.core.member.model.MemberComment;
import com.enation.app.shop.core.member.service.IMemberCommentManager;
import com.enation.eop.sdk.utils.StaticResourcesUtil;
import com.enation.framework.action.GridController;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.action.JsonResult;
import com.enation.framework.util.JsonResultUtil;
import com.enation.framework.util.StringUtil;

/**
 * 自营店商品评论咨询管理
 * @author DMRain
 * @version [1.0]
 * @since [1.0]
 * 2016-4-25
 */
@Controller 
@RequestMapping("/b2b2c/admin/self-goods-comments")
public class SelfGoodsCommentsController extends GridController{

	@Autowired
	private ISelfGoodsCommentsManager selfGoodsCommentsManager;
	
	@Autowired
	private IMemberCommentManager memberCommentManager;
	
	/**
	 * 跳转至评论(咨询)列表
	 * @param type 状态,2为咨询,1为评论,Integer
	 * @return 评论、咨询列表页面
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Integer type) {
		ModelAndView view = this.getGridModelAndView();
		if (type == 2) {
			view.setViewName("/b2b2c/admin/self/goods/ask_list");
		} else {
			view.setViewName("/b2b2c/admin/self/goods/comments_list");
		}
		return view;
	}
	
	/**
	 * 评论(咨询)列表json
	 * @param pageNo 分页页数,Integer
	 * @param pageSize  每页分页的数量,Integer
	 * @param type 状态,2为咨询,1为评论,Integer
	 * @return  评论、咨询列表json
	 */
	@ResponseBody
	@RequestMapping(value="/list-json")
	public GridJsonResult listJson(Integer type) {
		this.webpage = this.selfGoodsCommentsManager.list(this.getPage(), this.getPageSize(), type);
		return JsonResultUtil.getGridJson(webpage);
	}
	
	/**
	 * 跳转至回复用户评论页
	 * @param commentId 会员评论信息ID
	 * @return
	 */
	@RequestMapping(value="/reply-comment")
	public ModelAndView replyComment(Integer commentId, MemberComment memberComment) {
		ModelAndView view = this.getGridModelAndView();
		memberComment = this.memberCommentManager.get(commentId);
		
		if (memberComment != null && !StringUtil.isEmpty(memberComment.getImg())) {
			memberComment.setImgPath(StaticResourcesUtil.convertToUrl(memberComment.getImg()));
		}
		
		List<Map> list = this.memberCommentManager.getCommentGallery(commentId);
		List galleryList = new ArrayList();
		if (list != null) {
			for (Map l : list) {
				l.put("original", StaticResourcesUtil.convertToUrl(l.get("original").toString()));
				galleryList.add(l);
			}
		}
		
		view.addObject("galleryList", galleryList);
		view.addObject("memberComment", memberComment);
		view.setViewName("/b2b2c/admin/self/goods/reply_comments");
		
		return view;
	}
	
	/**
	 * 保存回复评论
	 * @param commentId 会员评论信息ID
	 * @param recomment 商家回复评论内容
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/save-reply")
	public JsonResult saveReply(Integer commentId, String recomment){
		try {
			this.selfGoodsCommentsManager.reply(commentId, recomment);
			return JsonResultUtil.getSuccessJson("回复评论成功");
		} catch (Exception e) {
			this.logger.error("回复失败：", e);
			return JsonResultUtil.getErrorJson("回复评论失败");
		}
	}
	
	/**
	 * 跳转至审核咨询页面
	 * @param commentId 会员咨询信息ID
	 * @return
	 */
	@RequestMapping(value="/review-ask")
	public ModelAndView reviewAsk(Integer commentId){
		ModelAndView view = this.getGridModelAndView();
		view.addObject("memberComment", this.memberCommentManager.get(commentId));
		view.setViewName("/b2b2c/admin/self/goods/review_ask");
		return view;
	}
	
	/**
	 * 保存咨询审核信息
	 * @param commentId 会员咨询信息ID
	 * @param recomment 商家回复咨询内容
	 * @param reciewType 审核类型 1：通过，2：拒绝
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/save-review")
	public JsonResult saveReview(Integer commentId, String recomment, Integer reciewType){
		try {
			this.selfGoodsCommentsManager.review(commentId, recomment, reciewType);
			return JsonResultUtil.getSuccessJson("操作成功");
		} catch (Exception e) {
			this.logger.error("操作失败：", e);
			return JsonResultUtil.getErrorJson("操作失败");
		}
	}
	
	/**
	 * 批量删除评论或者咨询
	 * @param comment_id 评论或者咨询ID组
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/delete")
	public JsonResult delete(Integer[] comment_id){
		try {
			this.selfGoodsCommentsManager.delete(comment_id);
			return JsonResultUtil.getSuccessJson("删除成功");
		} catch (Exception e) {
			this.logger.error("删除失败：", e);
			return JsonResultUtil.getErrorJson("删除失败");
		}
	}
}
