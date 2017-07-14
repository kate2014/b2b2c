package com.enation.app.b2b2c.front.tag.goods;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.member.service.IStoreMemberCommentManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
/**
 * 店铺商品评论信息标签
 * @author fenlongli
 *
 */
@Component
public class StoreGoodsCommentInfoTag extends BaseFreeMarkerTag{
	@Autowired
	private IStoreMemberCommentManager storeMemberCommentManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Integer goods_id=Integer.parseInt(params.get("goods_id").toString());
		return storeMemberCommentManager.getGoodsStore_desccredit(goods_id);
	}
}
