package com.enation.app.b2b2c.core.store.action.bill;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.b2b2c.core.store.model.bill.BillDetail;
import com.enation.app.b2b2c.core.store.model.bill.BillStatusEnum;
import com.enation.app.b2b2c.core.store.service.IStoreManager;
import com.enation.app.b2b2c.core.store.service.bill.IBillManager;
import com.enation.framework.action.GridController;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.action.JsonResult;
import com.enation.framework.util.JsonResultUtil;

/**
 * 结算管理
 * @author fenlongli
 *
 */

@Controller
@RequestMapping("/b2b2c/admin/store-bill")
public class StoreBillController extends GridController{
	
	@Autowired
	private IBillManager billManager;
	
	@Autowired
	private IStoreManager storeManager;
	
	/**
	 * 结算单列表
	 * @return
	 */
	@RequestMapping(value="/list")
	public ModelAndView  list(){
		ModelAndView view=getGridModelAndView();
		view.setViewName("/b2b2c/admin/bill/list");
		return view;
	}
	
	/**
	 * 获取结算列表JSON
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/list-json")
	public GridJsonResult listJson(){
		return  JsonResultUtil.getGridJson(billManager.bill_list(this.getPage(), this.getPageSize()));
	}
	
	/**
	 * 结算详细列表
	 * @return 结算详细列表页
	 */
	@RequestMapping(value="/detail-list")
	public ModelAndView detailList(Integer bill_id){
		ModelAndView view=this.getGridModelAndView();
		view.addObject("bill_id", bill_id);
		view.setViewName("/b2b2c/admin/bill/detail_list");
		return view;
	}
	
	/**
	 * 获取结算详细列表JSON
	 * @param bill_id 结算单Id
	 * @param keyword 店铺名称关键字
	 * @param webpage 结算详细列表
	 * @return 结算详细列表页JSON
	 */
	@ResponseBody
	@RequestMapping(value="/detail-list-json")
	public GridJsonResult detailListJson(Integer bill_id,String keyword){
		return JsonResultUtil.getGridJson(billManager.bill_detail_list(this.getPage(), this.getPageSize(), bill_id, keyword));
	}
	/**
	 * 获取结算单详细
	 * @param id 结算详细单Id
	 * @param billDetail 结算详细单
	 * @param store 店铺信息
	 * @return
	 */
	@RequestMapping(value="/detail")
	public ModelAndView detail(Integer bill_id){
		
		ModelAndView view=this.getGridModelAndView();
		BillDetail billDetail=this.billManager.get_bill_detail(bill_id);
		view.addObject("billDetail", billDetail);
		view.addObject("store", storeManager.getStore(billDetail.getStore_id()));
		view.addObject("bill_id", bill_id);
		view.setViewName("/b2b2c/admin/bill/detail");
		return view;
		
	}
	/**
	 * 修改结算详细状态
	 * @param status 结算单详细状态
	 * @param bill_id 结算详细单状态
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/edit-bill-detail")
	public JsonResult editBillDetail(Integer bill_id, Integer status){
		try {
			if(status==BillStatusEnum.COMPLETE.getIndex()){
				status=BillStatusEnum.PASS.getIndex();
			}else if(status==BillStatusEnum.PASS.getIndex()){
				status=BillStatusEnum.PAY.getIndex();
			}
			billManager.edit_billdetail_status(bill_id, status);
			return JsonResultUtil.getSuccessJson("更改状态成功");
		} catch (Exception e) {
			this.logger.error("更改结算单状态失败",e);
			return JsonResultUtil.getErrorJson("更改状态失败，请重试");
		}
		
	}
	/**
	 * 订单结算列表JSON
	 * @param page 分页页码
	 * @param pageSize 分页
	 * @param sn 结算单编号
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/bill-order-list-json")
	public GridJsonResult billOrderListJson(String sn){
		
		return JsonResultUtil.getGridJson(billManager.bill_order_list(this.getPage(), this.getPageSize(), sn));
	}
	/**
	 * 结算退货结算列表JSON
	 * @param page 分页页码
	 * @param pageSize 分页
	 * @param sn 结算单编号
	 */
	@ResponseBody
	@RequestMapping(value="/bill-sellback-list-json")
	public GridJsonResult billSellbackListJson(String sn){
		return JsonResultUtil.getGridJson(billManager.bill_sell_back_list(this.getPage(), this.getPageSize(), sn));
	}
	
	
}
