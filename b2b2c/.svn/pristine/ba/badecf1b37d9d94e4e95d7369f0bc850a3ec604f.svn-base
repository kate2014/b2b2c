package com.enation.app.b2b2c.front.api.goods;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enation.app.b2b2c.core.goods.model.StoreGoods;
import com.enation.app.b2b2c.core.goods.service.IStoreGoodsManager;
import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.base.core.model.Member;
import com.enation.app.shop.core.member.service.IFavoriteManager;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.action.GridController;
import com.enation.framework.action.JsonResult;
import com.enation.framework.util.JsonResultUtil;

/**
 *
 *  多店铺收藏商品
 * @author lk
 * @version v1.0
 * @since v6.1
 * 2016年10月17日 下午10:37:15
 */

@Controller
@RequestMapping("/api/b2b2c/goods-collect")
public class StoreGoodsCollectApiController extends GridController{

	@Autowired
	private IFavoriteManager favoriteManager;
	@Autowired
	private IStoreMemberManager storeMemberManager;
	@Autowired
	private IStoreGoodsManager storeGoodsManager;

	/**
	 * 收藏一个商品，必须登录才能调用此api
	 * @param goods_id ：要收藏的商品id,int型
	 * @return 返回json串
	 * result  为1表示调用成功0表示失败 ，int型
	 * message 为提示信息
	 */ 
	@ResponseBody
	@RequestMapping(value="/add-collect",produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResult addCollect(Integer goods_id){
		Member memberLogin =UserConext.getCurrentMember();
		if(memberLogin!=null){
			//获取当前会员是否开店，否的话无需做是否是本店商品的验证
			StoreMember member = this.storeMemberManager.getMember(memberLogin.getMember_id());
			if(member.getStore_id() ==null || member.getStore_id() ==0){
				  //如果用户没有开店，则不存在收藏本店商品的可能，直接判定是否收藏。
				int count = favoriteManager.getCount(goods_id,memberLogin.getMember_id());
				if (count == 0){
					favoriteManager.add(goods_id);
					return JsonResultUtil.getSuccessJson("添加收藏成功");
				}else{
					return JsonResultUtil.getErrorJson("已收藏该商品");				
				}
			   }else if(member.getStore_id() !=0 && member.getIs_store() != 0){
			
				//根据当前商品id查询出次商品的店铺id 拿当前会员的店铺id和商品的id进行比较
				StoreGoods storeGoods=this.storeGoodsManager.getGoods(goods_id);
				if(member.getStore_id() != storeGoods.getStore_id()){
					int count = favoriteManager.getCount(goods_id,memberLogin.getMember_id());
					if (count == 0){
						favoriteManager.add(goods_id);
						return JsonResultUtil.getSuccessJson("添加收藏成功");
					}else{
						return JsonResultUtil.getErrorJson("已收藏该商品");				
					}
				}else{
					return JsonResultUtil.getErrorJson("无法收藏本店商品");	
				}
			}

		}else{
			return JsonResultUtil.getErrorJson("对不起，请您登录后再收藏该商品！");				
		}
		return JsonResultUtil.getErrorJson("出错了！");	
	}



}
