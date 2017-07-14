package com.enation.app.b2b2c.core.order.model;

import com.enation.app.shop.core.order.model.SellBack;

/**
 * 店铺退货单
 * @author fenlongli
 *
 */
public class StoreSellBack extends SellBack{

	private Integer store_id;	//店铺Id
	private String store_name;	//店铺名称
	private int bill_status;		//结算状态
	private String bill_sn;			//结算编号
	
	public Integer getStore_id() {
		return store_id;
	}

	public void setStore_id(Integer store_id) {
		this.store_id = store_id;
	}

	public String getStore_name() {
		return store_name;
	}

	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}

	public int getBill_status() {
		return bill_status;
	}

	public void setBill_status(int bill_status) {
		this.bill_status = bill_status;
	}

	public String getBill_sn() {
		return bill_sn;
	}

	public void setBill_sn(String bill_sn) {
		this.bill_sn = bill_sn;
	}
}
