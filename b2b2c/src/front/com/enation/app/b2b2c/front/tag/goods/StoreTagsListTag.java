package com.enation.app.b2b2c.front.tag.goods;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.goods.service.IStoreGoodsTagManager;
import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
/**
 * 店铺商品标签列表
 * @author LiFenLong
 *
 */
public class StoreTagsListTag extends BaseFreeMarkerTag{
	@Autowired
	private IStoreGoodsTagManager storeGoodsTagManager;
	@Autowired
	private IStoreMemberManager storeMemberManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		List list=new ArrayList();
		if(params.get("type")==null){
			StoreMember member=storeMemberManager.getStoreMember();
			 list=storeGoodsTagManager.list(member.getStore_id());
		}else{
			 list=storeGoodsTagManager.list(Integer.parseInt(params.get("store_id").toString()));
		}
		return list;
	}
}
