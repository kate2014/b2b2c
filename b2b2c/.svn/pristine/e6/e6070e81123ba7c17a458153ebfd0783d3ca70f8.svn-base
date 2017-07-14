package com.enation.app.b2b2c.front.api.order;

 
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.b2b2c.core.order.service.IStoreSellBackManager;
import com.enation.framework.action.GridController;
import com.enation.framework.action.JsonResult;
import com.enation.framework.util.JsonResultUtil;
 
/**
 * 店铺退货申请API
 * @author LiFenlong
 *
 */
@Controller
@Scope("prototype")
@RequestMapping("/api/store/store-sell-back")
public class StoreSellBackApiController extends GridController{
 
	@Autowired 
	private IStoreSellBackManager storeSellBackManager;
	@Autowired 
	private IStoreMemberManager storeMemberManager;
	
	/**
	 * 填写退款金额
	 * @param id 退货单Id
	 * @param alltotal_pay 退款金额
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/refund",produces= MediaType.APPLICATION_JSON_VALUE)
	public JsonResult refund(Integer id,Double alltotal_pay){
		try {
			//增加权限
			StoreMember member = storeMemberManager.getStoreMember();
			Map map = storeSellBackManager.get(id);
			if(map == null || !map.get("store_id").equals(member.getStore_id())){
				return JsonResultUtil.getErrorJson("您没有权限");
			}
			
			storeSellBackManager.refund(id, alltotal_pay);
			return JsonResultUtil.getSuccessJson("操作成功");
		} catch (Exception e) {
			this.logger.error("填写退款金额程序出错",e);
			return JsonResultUtil.getErrorJson("操作失败");
		} 
		
	}
	/**
	 * 审核退款、退货申请
	 * @param id 申请退款单id
	 * @param status 状态 1通过，2拒绝
	 * @param seller_remark 退货审核备注
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/auth-refund",produces= MediaType.APPLICATION_JSON_VALUE)
	public JsonResult authRefund(Integer id,Integer status,String seller_remark){
		try {
			//增加权限
			StoreMember member = storeMemberManager.getStoreMember();
			Map map = storeSellBackManager.get(id);
			if(map == null || !map.get("store_id").equals(member.getStore_id())){
				return JsonResultUtil.getErrorJson("您没有权限");
			}
			
			storeSellBackManager.authRetund(id, status,seller_remark);
			return JsonResultUtil.getSuccessJson("操作成功");
		} catch (Exception e) {
			this.logger.error("审核程序出错",e);
			return JsonResultUtil.getErrorJson("操作失败");
		}
		
	}
}
