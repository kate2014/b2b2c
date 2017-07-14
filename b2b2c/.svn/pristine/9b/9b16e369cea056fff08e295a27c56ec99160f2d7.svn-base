package com.enation.app.b2b2c.front.tag.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.component.plugin.store.StoreMenuPluginBundle;
import com.enation.framework.model.LogStore;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 商家中心菜单Tag
 * @author Andste
 * @version v1.0,2016-06-02
 * @since v6.1
 */
@Component
public class StoreMenuListTag  extends BaseFreeMarkerTag {
	@Autowired
	private StoreMenuPluginBundle storeMenuPluginBundle;

	@Override
	protected Object exec(Map params) throws TemplateModelException {
		
		List<Map<String, Object>> mainMenuList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> lowerMenuList = new ArrayList<Map<String, Object>>();
		
		/* 一级菜单
		============================================================================ */
		
		// 概述
		Map<String, Object> mianOutline = new HashMap<String, Object>();
		mianOutline.put("key", "main_outline");
		mianOutline.put("name", "概况");
		mianOutline.put("url", "pages/main_outline.html");
		mianOutline.put("icon", "");
		
		// 商品
		Map<String, Object> mainGoods = new HashMap<String, Object>();
		mainGoods.put("key", "main_goods");
		mainGoods.put("name", "商品管理");
		mainGoods.put("url", "pages/goods/goods_publish.html");
		mainGoods.put("icon", "");
		
		// 交易
		Map<String, Object> mainTransaction = new HashMap<String, Object>();
		mainTransaction.put("key", "main_transaction");
		mainTransaction.put("name", "交易管理");
		mainTransaction.put("url", "pages/transaction/order.html?order_state=all");
		mainTransaction.put("icon", "");
		
		// 促销
		Map<String, Object> mainPromotions = new HashMap<String, Object>();
		mainPromotions.put("key", "main_promotions");
		mainPromotions.put("name", "促销管理");
		mainPromotions.put("url", "pages/promotions/promotions.html");
		mainPromotions.put("icon", "");
		
		// 店铺
		Map<String, Object> mainStore = new HashMap<String, Object>();
		mainStore.put("key", "main_store");
		mainStore.put("name", "店铺管理");
		mainStore.put("url", "pages/store/themes.html");
		mainStore.put("icon", "");
		
		//统计 add by jianghongyan  2016-06-27
		Map<String,Object> mainStatistics=new HashMap<String,Object>();
		mainStatistics.put("key", "main_statistics");
		mainStatistics.put("name", "统计");
		mainStatistics.put("url", "pages/statistics/store-profile.html");
		mainStatistics.put("icon", "");
		
		// 客服
		Map<String, Object> mainService = new HashMap<String, Object>();
		mainService.put("key", "main_service");
		mainService.put("name", "客服管理");
		mainService.put("url", "pages/service/advisory.html");
		mainService.put("icon", "");
		

		// 设置
		Map<String, Object> mainSetting = new HashMap<String, Object>();
		mainSetting.put("key", "main_setting");
		mainSetting.put("name", "设置管理");
		mainSetting.put("url", "pages/setting/setting.html");
		mainSetting.put("icon", "");
		mainSetting.put("unique", "60px"); //  如果属于独特的一级菜单，需要传参，60px是marging-top

		/* add一级菜单
		 ============================================================================ */
		mainMenuList.add(mianOutline);
		mainMenuList.add(mainGoods);
		mainMenuList.add(mainTransaction);
		mainMenuList.add(mainPromotions);
		mainMenuList.add(mainStore);
		//统计 add by jianghongyan 2016-06-27
		mainMenuList.add(mainStatistics);
		mainMenuList.add(mainService);
		mainMenuList.add(mainSetting);
				
		/* 二级菜单
		============================================================================ */
		/*----------  商品中心       ----------*/
		//  商品发布
		Map<String, Object> lowerGoodsPublish = new HashMap<String, Object>();
		lowerGoodsPublish.put("key", "lower_goods_publish");
		lowerGoodsPublish.put("url", "goods/goods_publish.html");
		lowerGoodsPublish.put("name", "商品发布");
		
		//  出售中的商品
		Map<String, Object> lowerGoodsSelling = new HashMap<String, Object>();
		lowerGoodsSelling.put("key", "lower_goods_selling");
		lowerGoodsSelling.put("url", "goods/selling.html");
		lowerGoodsSelling.put("name", "出售中的商品");
		
		//  仓库中的商品
		Map<String, Object> lowerGoodsWarehouse = new HashMap<String, Object>();
		lowerGoodsWarehouse.put("key", "lower_goods_warehouse");
		lowerGoodsWarehouse.put("url", "goods/warehouse.html");
		lowerGoodsWarehouse.put("name", "商品列表");
		
		//  分类管理
		Map<String, Object> lowerGoodsClassify = new HashMap<String, Object>();
		lowerGoodsClassify.put("key", "lower_goods_classify");
		lowerGoodsClassify.put("url", "goods/classify.html");
		lowerGoodsClassify.put("name", "分类管理");
		
		//  标签管理
		Map<String, Object> lowerGoodsTags = new HashMap<String, Object>();
		lowerGoodsTags.put("key", "lower_goods_tags");
		lowerGoodsTags.put("url", "goods/tags.html");
		lowerGoodsTags.put("name", "标签管理");
		
		//  回收站
		Map<String, Object> lowerGoodsTrash = new HashMap<String, Object>();
		lowerGoodsTrash.put("key", "lower_goods_trash");
		lowerGoodsTrash.put("url", "goods/trash.html?disable=1");
		lowerGoodsTrash.put("name", "回收站");
		
		// 预警货品
		Map<String, Object> lowerWarningGoods = new HashMap<String, Object>();
		lowerWarningGoods.put("key", "lower_warning_goods");
		lowerWarningGoods.put("url", "goods/warning_goods.html");
		lowerWarningGoods.put("name", "预警货品");
	
		/*----------  交易管理       ----------*/
		//  订单管理
		Map<String, Object> lowerTransactionOrder = new HashMap<String, Object>();
		lowerTransactionOrder.put("key", "lower_transaction_order");
		lowerTransactionOrder.put("url", "transaction/order.html?order_state=all");
		lowerTransactionOrder.put("name", "所有订单");
		
		//  待发货订单
		Map<String, Object> lowerTransactionNotship = new HashMap<String, Object>();
		lowerTransactionNotship.put("key", "lower_transaction_notship");
		lowerTransactionNotship.put("url", "transaction/notship.html?order_state=wait_ship");
		lowerTransactionNotship.put("name", "待发货订单");
		
		//  订单取消申请
//		Map<String, Object> lowerTransactionCancel = new HashMap<String, Object>();
//		lowerTransactionCancel.put("key", "lower_transaction_cancel");
//		lowerTransactionCancel.put("url", "transaction/cancel.html");
//		lowerTransactionCancel.put("name", "取消订单申请");
		
		//  维权订单
		Map<String, Object> lowerTransactionReturn = new HashMap<String, Object>();
		lowerTransactionReturn.put("key", "lower_transaction_return");
		lowerTransactionReturn.put("url", "transaction/return.html?status=0");
		lowerTransactionReturn.put("name", "维权订单");
		
		//  物流工具
		Map<String, Object> lowerTransactionLogistics = new HashMap<String, Object>();
		lowerTransactionLogistics.put("key", "lower_transaction_logistics");
		lowerTransactionLogistics.put("url", "transaction/logistics.html");
		lowerTransactionLogistics.put("name", "物流管理");
		
		//  评价管理
		Map<String, Object> lowerTransactionEvaluation = new HashMap<String, Object>();
		lowerTransactionEvaluation.put("key", "lower_transaction_evaluation");
		lowerTransactionEvaluation.put("url", "transaction/evaluation.html");
		lowerTransactionEvaluation.put("name", "评价管理");
		
		//  结算管理
		Map<String, Object> lowerTransactionBilling = new HashMap<String, Object>();
		lowerTransactionBilling.put("key", "lower_transaction_billing");
		lowerTransactionBilling.put("url", "transaction/billing.html");
		lowerTransactionBilling.put("name", "结算管理");
		
		/*----------  促销管理       ----------*/
		//  促销活动管理
		Map<String, Object> lowerPromotionsPromotions = new HashMap<String, Object>();
		lowerPromotionsPromotions.put("key", "lower_promotions_promotions");
		lowerPromotionsPromotions.put("url", "promotions/promotions.html");
		lowerPromotionsPromotions.put("name", "促销管理");
		
		//  优惠劵管理
		Map<String, Object> lowerPromotionsCoupon = new HashMap<String, Object>();
		lowerPromotionsCoupon.put("key", "lower_promotions_coupon");
		lowerPromotionsCoupon.put("url", "promotions/coupon.html");
		lowerPromotionsCoupon.put("name", "优惠管理");
		
		//  赠品管理
		Map<String, Object> lowerPromotionsPremiums = new HashMap<String, Object>();
		lowerPromotionsPremiums.put("key", "lower_promotions_gift");
		lowerPromotionsPremiums.put("url", "promotions/gift.html");
		lowerPromotionsPremiums.put("name", "赠品管理");
		
		//  团购管理
//		Map<String, Object> lowerPromotionsGroupbuy = new HashMap<String, Object>();
//		lowerPromotionsGroupbuy.put("key", "lower_promotions_groupbuy");
//		lowerPromotionsGroupbuy.put("url", "promotions/groupbuy_list.html");
//		lowerPromotionsGroupbuy.put("name", "团购管理");
		
		/*----------  店铺管理       ----------*/
		//  店铺模板
		Map<String, Object> lowerStoreThemes = new HashMap<String, Object>();
		lowerStoreThemes.put("key", "lower_store_themes");
		lowerStoreThemes.put("url", "store/themes.html");
		lowerStoreThemes.put("name", "店铺模板");
			
		//  店铺幻灯片
		Map<String, Object> lowerStoreSlideshow = new HashMap<String, Object>();
		lowerStoreSlideshow.put("key", "lower_store_slideshow");
		lowerStoreSlideshow.put("url", "store/slideshow.html");
		lowerStoreSlideshow.put("name", "店铺幻灯片");
	
		//  店铺导航
		Map<String, Object> lowerStoreNavigation = new HashMap<String, Object>();
		lowerStoreNavigation.put("key", "lower_store_navigation");
		lowerStoreNavigation.put("url", "store/navigation.html");
		lowerStoreNavigation.put("name", "店铺导航");
		
		
		//  我的店铺
		Map<String, Object> lowerStoreMystore = new HashMap<String, Object>();
		lowerStoreMystore.put("key", "lower_store_mystore");
		lowerStoreMystore.put("url", "store/mystore.html");
		lowerStoreMystore.put("name", "我的店铺");
		
		/*----------  客服管理       ----------*/
		//  咨询导航
		Map<String, Object> lowerServiceAdvisory = new HashMap<String, Object>();
		lowerServiceAdvisory.put("key", "lower_service_advisory");
		lowerServiceAdvisory.put("url", "service/advisory.html");
		lowerServiceAdvisory.put("name", "咨询管理");
		

		/*----------  店铺统计       ----------*/
		//运营报告
		Map<String, Object> lowerOperationStatistics=new HashMap<String,Object>();
		lowerOperationStatistics.put("key", "lower_statistics_sales");
		lowerOperationStatistics.put("url", "statistics/sales.html?cycle_type=1");
		lowerOperationStatistics.put("name", "运营报告");
		
		//商品分析
		Map<String,Object> lowerGoodsAnalysis=new HashMap<String,Object>();
		lowerGoodsAnalysis.put("key", "lower_goods_analysis");
		lowerGoodsAnalysis.put("url", "statistics/sales-detail.html");
		lowerGoodsAnalysis.put("name", "商品分析");
		
		//店铺概况
		Map<String,Object> lowerStoreProfile=new HashMap<String,Object>();
		lowerStoreProfile.put("key", "lower_store_profile");
		lowerStoreProfile.put("url", "statistics/store-profile.html");
		lowerStoreProfile.put("name", "店铺概况");
		
		//流量统计
		Map<String,Object> lowerFlowStatistics=new HashMap<String,Object>();
		lowerFlowStatistics.put("key", "lower_flow_statistics");
		lowerFlowStatistics.put("url", "statistics/store-flow-statistics.html?cycle_type=1");
		lowerFlowStatistics.put("name", "流量统计");
		
		//收藏统计
		Map<String,Object> lowerCollectStatistics=new HashMap<String,Object>();
		lowerCollectStatistics.put("key", "lower_collect_statistics");
		lowerCollectStatistics.put("url", "statistics/store-collect.html");
		lowerCollectStatistics.put("name", "收藏统计");
		
		//日志管理
		Map<String,Object> storeLogs=new HashMap<String,Object>();
		storeLogs.put("key", "lower_flow_logs");
		storeLogs.put("url", "statistics/storelogs.html");
		storeLogs.put("name", "日志管理");
		

		/*----------  设置管理       ----------*/
		//  店铺设置
		Map<String, Object> lowerSettingSetting = new HashMap<String, Object>();
		lowerSettingSetting.put("key", "lower_setting_setting");
		lowerSettingSetting.put("url", "setting/setting.html");
		lowerSettingSetting.put("name", "店铺设置");
			
		//  店铺认证
		Map<String, Object> lowerSettingAuthenticate = new HashMap<String, Object>();
		lowerSettingAuthenticate.put("key", "lower_setting_authenticate");
		lowerSettingAuthenticate.put("url", "setting/authenticate.html");
		lowerSettingAuthenticate.put("name", "店铺认证");
		
		//  货品预警设置
		Map<String, Object> lowerSettingWarningGoods = new HashMap<String, Object>();
		lowerSettingWarningGoods.put("key", "lower_setting_warning_goods");
		lowerSettingWarningGoods.put("url", "setting/warning-goods-setting.html");
		lowerSettingWarningGoods.put("name", "货品预警设置");
		

		String mainPageName = params.get("main_page_name").toString();
		//String pageName = params.get("page_name").toString();
		
		
		// 菜单项 
		if ("main_goods".equals(mainPageName)) {
			lowerMenuList.add(lowerGoodsPublish);
			lowerMenuList.add(lowerGoodsWarehouse);
			lowerMenuList.add(lowerGoodsClassify);
			lowerMenuList.add(lowerGoodsTags);
			lowerMenuList.add(lowerGoodsTrash);
			lowerMenuList.add(lowerWarningGoods);
		} else if ("main_transaction".equals(mainPageName)) {
			lowerMenuList.add(lowerTransactionOrder);
			lowerMenuList.add(lowerTransactionNotship);
//			lowerMenuList.add(lowerTransactionCancel);
			lowerMenuList.add(lowerTransactionReturn);
			lowerMenuList.add(lowerTransactionLogistics);
			lowerMenuList.add(lowerTransactionEvaluation);
			lowerMenuList.add(lowerTransactionBilling);
		} else if ("main_promotions".equals(mainPageName)) {
			lowerMenuList.add(lowerPromotionsPromotions);
			lowerMenuList.add(lowerPromotionsCoupon);
			lowerMenuList.add(lowerPromotionsPremiums);
			//  添加团购入口  _by: Andste  2016-9-22 14:51:58
			//lowerMenuList.add(lowerPromotionsGroupbuy);
			storeMenuPluginBundle.onStoreMenuShow(lowerMenuList);
		} else if ("main_store".equals(mainPageName)) {
			lowerMenuList.add(lowerStoreThemes);
			lowerMenuList.add(lowerStoreSlideshow);
			lowerMenuList.add(lowerStoreNavigation);
			//lowerMenuList.add(lowerStoreMystore);
		} else if ("main_service".equals(mainPageName)) {
			lowerMenuList.add(lowerServiceAdvisory);
		} else if ("main_statistics".equals(mainPageName)) {//统计
			lowerMenuList.add(lowerStoreProfile);
			lowerMenuList.add(lowerGoodsAnalysis);
			lowerMenuList.add(lowerOperationStatistics);
			lowerMenuList.add(lowerFlowStatistics);
			lowerMenuList.add(lowerCollectStatistics);
			lowerMenuList.add(storeLogs);
		} else if ("main_setting".equals(mainPageName)) {
			lowerMenuList.add(lowerSettingSetting);
			lowerMenuList.add(lowerSettingAuthenticate);
			lowerMenuList.add(lowerSettingWarningGoods);
		} 
		
		Map<String, Object> menuMap = new HashMap<String, Object>();
		menuMap.put("main_menu_list", mainMenuList);
		menuMap.put("lower_menu_list", lowerMenuList);
		
		return menuMap;
	}

}
