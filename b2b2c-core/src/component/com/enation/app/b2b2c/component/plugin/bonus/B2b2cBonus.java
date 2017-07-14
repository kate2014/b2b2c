package com.enation.app.b2b2c.component.plugin.bonus;

/**
 * b2b2c优惠券
 * @author xulipeng
 * 
 */
public class B2b2cBonus {
	
	private Integer bonus_id;			//优惠卷ID
	private Double type_money;			//优惠卷金额
	private Double min_goods_amount;	//满多少可用
	private Integer is_used;			//本次结算中是否已经使用
	private Integer usable;				//是否可用
	private String usable_term;			//可用条件说明
	
	public Integer getBonus_id() {
		return bonus_id;
	}
	public void setBonus_id(Integer bonus_id) {
		this.bonus_id = bonus_id;
	}
	public Double getType_money() {
		return type_money;
	}
	public void setType_money(Double type_money) {
		this.type_money = type_money;
	}
	public Double getMin_goods_amount() {
		return min_goods_amount;
	}
	public void setMin_goods_amount(Double min_goods_amount) {
		this.min_goods_amount = min_goods_amount;
	}
	public Integer getIs_used() {
		return is_used;
	}
	public void setIs_used(Integer is_used) {
		this.is_used = is_used;
	}
	public Integer getUsable() {
		return usable;
	}
	public void setUsable(Integer usable) {
		this.usable = usable;
	}
	public String getUsable_term() {
		return usable_term;
	}
	public void setUsable_term(String usable_term) {
		this.usable_term = usable_term;
	}
	
	
}
