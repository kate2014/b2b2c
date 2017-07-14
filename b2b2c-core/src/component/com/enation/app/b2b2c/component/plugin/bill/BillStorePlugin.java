package com.enation.app.b2b2c.component.plugin.bill;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.plugin.job.IEveryMonthExecuteEvent;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.plugin.AutoRegisterPlugin;
import com.enation.framework.util.DateUtil;
/**
 * 结算每月执行插件
 * @author fenlongli
 *
 */
@Component
public class BillStorePlugin extends AutoRegisterPlugin implements IEveryMonthExecuteEvent {
	
	@Autowired
	private IDaoSupport daoSupport;
	
	/**
	 * 每月触发一次结算
	 * 修改结算单详情
	 */
	@Override
	public void everyMonth() {
		try {
			//所有上月订单修改状态
			Long[] time=DateUtil.getLastMonth();
			Long start_time=time[0]; 
			daoSupport.execute("update es_bill_detail set status = 1 where start_time = ?", start_time); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
}
