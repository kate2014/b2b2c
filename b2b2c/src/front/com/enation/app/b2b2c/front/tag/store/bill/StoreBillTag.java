package com.enation.app.b2b2c.front.tag.store.bill;

import java.util.Calendar;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.store.model.bill.BillDetail;
import com.enation.app.b2b2c.core.store.service.bill.IBillManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
/**
 * 结算单详细
 * @author fenlongli
 * @date 2015-5-21 下午4:04:10
 */
@Component
public class StoreBillTag extends BaseFreeMarkerTag{
	@Autowired
	private IBillManager billManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Integer id=Integer.parseInt(params.get("id").toString());
		
		BillDetail bd =  billManager.get_bill_detail(id);
		Integer day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		//如果日期大于 5号。那么写入状态为0 ，意义为 非0-5号不能申请结算
		 if(day<=5){
			 billManager.editBillStatus(bd.getStatus());
			 
		 }else{
			 bd.setStatus(0);
		 }
		return bd;
	}
}
