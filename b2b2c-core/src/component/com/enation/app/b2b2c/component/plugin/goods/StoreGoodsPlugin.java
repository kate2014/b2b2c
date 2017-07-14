package com.enation.app.b2b2c.component.plugin.goods;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.goods.model.StoreGoods;
import com.enation.app.b2b2c.core.goods.service.IStoreGoodsManager;
import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.b2b2c.core.store.model.StoreSetting;
import com.enation.app.shop.ShopApp;
import com.enation.app.shop.component.gallery.service.IGoodsGalleryManager;
import com.enation.app.shop.core.goods.plugin.IGoodsAfterEditEvent;
import com.enation.app.shop.core.goods.plugin.IGoodsBeforeAddEvent;
import com.enation.app.shop.core.goods.plugin.IGoodsBeforeEditEvent;
import com.enation.app.shop.core.goods.service.IGoodsManager;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.plugin.AutoRegisterPlugin;
/**
 * 店铺商品Plugin
 * @author LiFenLong
 *
 */
@Component
public class StoreGoodsPlugin extends AutoRegisterPlugin implements IGoodsAfterEditEvent,IGoodsBeforeAddEvent,IGoodsBeforeEditEvent {
	
	@Autowired
	private IGoodsGalleryManager goodsGalleryManager;
	
	@Autowired
	private IStoreMemberManager storeMemberManager;

	@Autowired
	private IGoodsManager goodsManager;
	
	@Autowired
	private IDaoSupport daoSupport;
	
	@Autowired
	private IStoreGoodsManager storeGoodsManager;
	
	
	@Override
	/**
	 * 店铺商品修改后修改商品相册内容
	 */
	public void onAfterGoodsEdit(Map goods, HttpServletRequest request) {
		
	}
	
	/**
	 * 如果未上架改为已上架增加店铺商品数量
	 * 如果已上架改为不上架则减少商品数量
	 */
	@Override
	public void onBeforeGoodsEdit(Map goods, HttpServletRequest request) {
		StoreMember member= storeMemberManager.getStoreMember();
		int storeid= ShopApp.self_storeid;
		if(member!=null && member.getStore_id()!=null){
			storeid= member.getStore_id();
		}
		
		Map oldGoods=goodsManager.get(Integer.valueOf(goods.get("goods_id").toString()));
		
		//如果是预览商品
		if(!goods.get("market_enable").equals("2")){
			//审核商品
			editAuth(oldGoods,goods);
			
			if(goods.get("market_enable").equals("1")){
				if(oldGoods.get("market_enable").toString().equals("0")){
				
					String sql="update es_store set goods_num=goods_num+1 where store_id=?";
					this.daoSupport.execute(sql,storeid);
				}
			}else if(goods.get("market_enable").equals("0")){
				if(oldGoods.get("market_enable").toString().equals("1")){
					String sql="update es_store set goods_num=goods_num-1 where store_id=?";
					this.daoSupport.execute(sql, storeid);
				}
			}
		}
		//设置商品列表图片
		String[] status=request.getParameterValues("status");
		String[] imgFs=request.getParameterValues("goods_fs");
		if(status!=null&&imgFs!=null){
			if(status[0].equals("3")||status[0].equals("1")){
				oldGoods.put("thumbnail", imgFs[0]);
			}
		}
		
		
	
	}
	@Override
	/**
	 * 添加商品修改店铺商品数量
	 */
	public void onBeforeGoodsAdd(Map goods, HttpServletRequest request) {
		//如果商品上架则更改店铺商品数量
		if(goods.get("market_enable").equals("1")){
			
			StoreMember member=storeMemberManager.getStoreMember();
			String sql="update es_store set goods_num=goods_num+1 where store_id=?";
			if(member!=null){
				//会员不为null说明是商家中心在保存，用会员的storeid
				this.daoSupport.execute(sql, member.getStore_id());
			}else{
				//会员为null，说明是自营店在保存，则使用自营店的storeid
				this.daoSupport.execute(sql,ShopApp.self_storeid);
			}
			
		}
		
		//添加商品列表图片
		String[] status=request.getParameterValues("status");
		String[] imgFs=request.getParameterValues("goods_fs");
		if(status!=null&&imgFs!=null){
			if(status[0].equals("1")){
				goods.put("thumbnail", imgFs[0]);
			}
		}
		//添加商品购买次数以及评论次数
		goods.put("buy_num", 0) ;	//购买数量
		goods.put("comment_num", 0);	//评论次数
				
		//如果是预览商品
		if(goods.get("market_enable").equals("2")){
			return;
		}
		
		//审核商品
		addAuth(goods);
	}
	
	/**
	 * 添加商品审核
	 * 判读是否开启了审核商品
	 * 如果开启了审核商品非自营店的商品上架则全部需要审核
	 * 如果开启了审核商品并且是自营店商品则需要判断是否自营店商品需审核，如果开启自营店商品审核功能则自营商品上架时也需要审核
	 * @param goods 商品
	 */
	private void addAuth(Map goods){
		//判断商品是否需要审核
		if(StoreSetting.auth==0){
			goods.put("is_auth", 1);
			
		}else{
			
			//如果为自营店商品判断是否需要修改
			if( goods.get("store_id")==null && StoreSetting.self_auth==0){
				goods.put("is_auth", 1);
				
			}else{
				
				//判断是否是否商家如果商家则需要审核
				if(Integer.parseInt(goods.get("market_enable").toString())==1){
					goods.put("market_enable",0);
					goods.put("is_auth", 3);
				}else{
					goods.put("is_auth", 0);
				}
			}
			
		}
	}
	
	/**
	 * 修改商品审核
	 * 判断是否开启的审核商品
	 * 如果开启了审核商品，如果商品第一次上架需要审核。
	 * 如果开启了修改商品审核，则修改商品时需要去审核商品。
	 * 如果开启了自营店审核则修改商品时，自营店商品也需要审核
	 * @param oldGoods 修改前的商品
	 * @param goods 预修改的商品
	 */
	@SuppressWarnings("unchecked")
	private void editAuth(Map oldGoods,Map goods){
		
		
		//修改前的商品审核状态
		Integer is_auth = Integer.parseInt(oldGoods.get("is_auth")==null?"1":oldGoods.get("is_auth").toString());
		
		//修改前的商品上下架状态
		Integer market_enable = Integer.parseInt(oldGoods.get("market_enable")==null?"0":oldGoods.get("market_enable").toString());
		
		//将要修改的商品上下架状态
		Integer will_market_enable = Integer.parseInt(goods.get("market_enable").toString());
		
		//将要修改的商品为下架状态，则不需要审核。
		if(will_market_enable.intValue()==0){
			
		}else{
			//判断修改是否需要审核，1为需要审核
			if(StoreSetting.edit_auth==1){
				goods.put("market_enable", 0);
				goods.put("is_auth", 3);
				
			}else{
				
				//判断商家发布商品是否需要审核，1为需要审核 && 商品是待审核状态
				if(StoreSetting.auth==1 && (is_auth == 3 || is_auth == 2)){	
					goods.put("market_enable", market_enable);
					goods.put("is_auth", 3);
				}
				
				//判断自营店发布商品是否需要审核，1为需要审核 && ((商品是待审核状态||审核未通过)||(修改前为下架 && 将要修改为上架))
				if(StoreSetting.self_auth.intValue()==1 && ((is_auth.intValue() == 3 || is_auth.intValue() == 2)||(market_enable.intValue()==0 && will_market_enable.intValue()==1))){	
					goods.put("market_enable", market_enable);
					goods.put("is_auth", 3);
				}
			}
		}
		
	}
}
