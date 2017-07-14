package com.enation.app.b2b2c.component.bonus.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.b2b2c.component.bonus.model.StoreBonusType;
import com.enation.app.b2b2c.component.bonus.service.IB2b2cBonusManager;
import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.base.core.model.Member;
import com.enation.app.shop.component.bonus.model.Bonus;
import com.enation.app.shop.component.bonus.model.MemberBonus;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;

/**
 * 多店优惠券manager
 * @author xulipeng
 * @version v1.0
 * @since v6.2.1
 * 2017年01月04日
 */
@Service("b2b2cBonusManager")
public class B2b2cBonusManager implements IB2b2cBonusManager {

	@Autowired
	private IDaoSupport daoSupport;
	@Autowired
	private IStoreMemberManager storeMemberManager;
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.component.bonus.service.IB2b2cBonusManager#add_FullSubtract(com.enation.app.b2b2c.component.bonus.model.StoreBonus)
	 */
	@Override
	public void add_FullSubtract(StoreBonusType bonus) {
		this.daoSupport.insert("es_bonus_type", bonus);
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.component.bonus.service.IB2b2cBonusManager#receive_bonus(java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void receive_bonus(Integer memberid, Integer storeid, Integer type_id) {
		StoreBonusType bonus =	this.getBonus(type_id);
		
		while (memberid != null) {
			String sn = this.createSn(bonus.getType_id()+"");
			int c = this.daoSupport.queryForInt("select count(0) from es_member_bonus where bonus_sn=?", sn);
			
			if (c == 0) {
				this.daoSupport.execute("insert into es_member_bonus(bonus_type_id,bonus_sn,type_name,bonus_type,create_time,member_id)values(?,?,?,?,?,?)", type_id,sn,bonus.getType_name(),bonus.getSend_type(),DateUtil.getDateline(),memberid);
				
				//修改优惠券已被领取的数量 by_DMRain 2016-6-24
				this.daoSupport.execute("update es_bonus_type set received_num = (received_num + 1) where type_id = ?", type_id);
				return;
			} else {
				System.out.println("有相同的sn码,在生成一个sn码");
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.component.bonus.service.IB2b2cBonusManager#getBonus(java.lang.Integer)
	 */
	@Override
	public StoreBonusType getBonus(Integer type_id) {
		String sql ="select * from es_bonus_type  where type_id =?";
		return (StoreBonusType) this.daoSupport.queryForObject(sql, StoreBonusType.class, type_id);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.component.bonus.service.IB2b2cBonusManager#getmemberBonus(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public int getmemberBonus(Integer type_id,Integer memberid) {
		String sql = "select count(0) from es_member_bonus where bonus_type_id=? and member_id=?";
		int num = this.daoSupport.queryForInt(sql, type_id,memberid);
		return num;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.component.bonus.service.IB2b2cBonusManager#edit_FullSubtract(com.enation.app.b2b2c.component.bonus.model.StoreBonus)
	 */
	@Override
	public void edit_FullSubtract(StoreBonusType bonus) {
		this.daoSupport.update("es_bonus_type", bonus, "type_id="+bonus.getType_id());
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.component.bonus.service.IB2b2cBonusManager#deleteBonus(java.lang.Integer)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteBonus(Integer type_id) {
		String sql ="select use_end_date from es_bonus_type where type_id="+type_id;
		long use_end_date = this.daoSupport.queryForLong(sql);
		if(DateUtil.getDateline()<use_end_date){
			throw new RuntimeException("此优惠劵未过期不能删除!");
		}else{
			int result = this.checkForActivity(type_id);
			if (result == 1) {
				throw new RuntimeException("此优惠券已经关联了促销活动，不可删除");
			} else {
				this.daoSupport.execute("delete from es_bonus_type where type_id="+type_id);
				
				//商家删除优惠券后，也将会员领取的此优惠券删除 add_by DMRain 2016-7-28
				this.daoSupport.execute("delete from es_member_bonus where bonus_type_id = ?", type_id);
				
				//将已经结束的促销活动关联的优惠券信息去除 by_DMRain 2016-6-16
				sql = "update es_activity_detail set is_send_bonus = 0,bonus_id = null where bonus_id = ?";
				this.daoSupport.execute(sql, type_id);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.IStorePromotionManager#getList(java.lang.Integer)
	 */
	@Override
	public List<StoreBonusType> getList(Integer store_id) {
		long curTime = DateUtil.getDateline();
		String sql = "select * from es_bonus_type where store_id = ? and create_num > received_num and use_end_date > ?";
		List<StoreBonusType> list = this.daoSupport.queryForList(sql, store_id, curTime);
		return list;
	}

	/* (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.store.service.IStorePromotionManager#getCountBonus(java.lang.Integer)
	 */
	@Override
	public int getCountBonus(Integer type_id) {
		String queryBonusCreate = "select count(0) from es_member_bonus where bonus_type_id = "+type_id;
		int count = this.daoSupport.queryForInt(queryBonusCreate);
		return count;
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.component.bonus.service.IB2b2cBonusManager#get(java.lang.Integer)
	 */
	@Override
	public StoreBonusType get(Integer bonusid) {
		String sql ="select * from es_bonus_type where type_id=?";
		StoreBonusType bonus = (StoreBonusType) this.daoSupport.queryForObject(sql, StoreBonusType.class, bonusid);
		return bonus;
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.store.service.IStoreBonusManager#getBonusListBymemberid(int, int, java.lang.Integer)
	 */
	@Override
	public Page getBonusListBymemberid(int pageNo,int pageSize,Integer memberid,Integer is_usable) { 
		long now = DateUtil.getDateline();
		String sql = "select m.*,b.type_id,b.type_money,b.send_type,b.min_amount,b.max_amount,b.send_start_date,b.send_end_date,b.use_start_date,"
				+"b.use_end_date,b.min_goods_amount,b.use_num,b.create_num,b.recognition"
				+",s.store_name from es_member_bonus m left join es_bonus_type b on b.type_id = m.bonus_type_id"
				+ " left join es_store s on b.store_id=s.store_id where m.member_id="+memberid;
		
		//如果等于2，查全部（包含可用，不可用）。 used：0为未使用，1为已使用
		if(is_usable==null || is_usable==2 ){
			
		}else if(is_usable==1){	//如果等于1，查可用。
			
			//可用优惠券读取条件： 没有使用过的 并且 当前时间大于等于生效时间 并且 当前时间小于等于失效时间
			sql+=" and m.used=0 and b.use_start_date <=" +now+ " and b.use_end_date>="+now;
			
		}else if(is_usable==0){	//如果等于0，查不可用
			
			//不可用优惠券读取条件  当前时间小于生效时间 或者 当前时间大于失效时间 或者 已经使用过的
			sql+=" and (b.use_start_date >"+now+" or b.use_end_date <"+now+" or m.used=1 )";
		}
		
		sql+=" order by m.create_time desc";
		
		Page webPage = this.daoSupport.queryForPage(sql, pageNo, pageSize);
		return webPage;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.component.bonus.service.IB2b2cBonusManager#getConditionBonusList(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.util.Map)
	 */
	@Override
	public Page getConditionBonusList(Integer pageNo, Integer pageSize,Integer store_id,Map map){
		String keyword=String.valueOf(map.get("keyword"));
		String add_time_from=String.valueOf(map.get("add_time_from"));
		String add_time_to=String.valueOf(map.get("add_time_to"));
		StringBuffer sql =new StringBuffer("select * from es_bonus_type where store_id= "+ store_id);
		String sign_time=String.valueOf(map.get("sign_time"));
		
		if(!StringUtil.isEmpty(keyword)&&!keyword.equals("null")){
			sql.append(" AND type_name like '%" + keyword + "%'");
		}
		
		if(!StringUtil.isEmpty(add_time_from)&&!add_time_from.equals("null")){
			sql.append(" AND use_start_date >"+DateUtil.getDateline(add_time_from));
		}
		if(!StringUtil.isEmpty(add_time_to)&&!add_time_to.equals("null")){
			sql.append(" AND use_end_date <"+DateUtil.getDateline(add_time_to));
		}
		if(!StringUtil.isEmpty(sign_time)&&!sign_time.equals("null")){
			sql.append(" AND use_end_date >"+DateUtil.getDateline(sign_time));
		}
		sql.append(" order by type_id ");
		
		Page rpage = this.daoSupport.queryForPage(sql.toString(),pageNo, pageSize, StoreBonusType.class);
		 
		return rpage;
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.store.service.IStoreBonusManager#getUseOrNoUseBonusList(int, int, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Page getMyBonusByIsUsable(int page, int pageSize, Integer memberId, Integer is_usable,Double goodsPrice,Integer storeId) {
		long now = DateUtil.getDateline();
		
		StringBuffer sql = new StringBuffer("select m.*,b.type_money,b.min_goods_amount,b.type_id,b.use_start_date,b.use_end_date,b.store_id,s.store_name from es_member_bonus m left join es_bonus_type b on b.type_id = m.bonus_type_id left join es_store s on s.store_id=b.store_id ");
		sql.append(" where m.used = 0");
		sql.append(" and m.member_id="+memberId);
		sql.append(" and b.store_id="+storeId);
		
		// 判断读取可用或者 不可用优惠券  1为可用
		if(is_usable!=null && is_usable.intValue()==1 ){
			
			//可用优惠券读取条件 当前时间大于等于生效时间 并且 当前时间小于等于失效时间
			sql.append(" and b.use_start_date <=" +now+ " and b.use_end_date>="+now);
			// 并且 大于等于优惠券使用金额条件
			sql.append(" and b.min_goods_amount<="+goodsPrice);
			
		}else{
			
			//不可用优惠券读取条件  当前时间小于生效时间 或者 当前时间大于失效时间 或者 小于最低使用限额
			sql.append(" and (b.use_start_date >"+now+" or b.use_end_date <"+now+" or b.min_goods_amount > "+goodsPrice+" ) ");	
		}
		
		sql.append(" order by b.type_money desc");
		
		Page webPage = this.daoSupport.queryForPage(sql.toString(), page, pageSize,Bonus.class);
		return webPage;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.component.bonus.service.IB2b2cBonusManager#getMyBonusByBonusId(java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public MemberBonus getOneMyBonus(Integer member_id, Integer store_id, Integer bonus_id) {
		StringBuffer sql = new StringBuffer();
		sql.append("select m.*,b.type_money,b.min_goods_amount from es_member_bonus m left join es_bonus_type b on b.type_id = m.bonus_type_id ");
		sql.append(" where m.used!=1 ");
		sql.append(" and m.member_id=? ");
		sql.append(" and m.bonus_id=? ");
		sql.append(" and b.store_id=? ");
		
		List<MemberBonus> list = this.daoSupport.queryForList(sql.toString(),MemberBonus.class,member_id,bonus_id,store_id);
		if(list.isEmpty()){
			return null;
		}
		MemberBonus bonus = (MemberBonus) list.get(0);
		return bonus;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.component.bonus.service.IB2b2cBonusManager#use(int, int, int, java.lang.String, int)
	 */
	@Override
	public void use(int bonusid, int memberid, int orderid, String ordersn, int bonus_type_id) {
		StoreMember member = this.storeMemberManager.getMember(memberid);
		
		String sql="update es_member_bonus set order_id=?,order_sn=?,member_id=?,used_time=?,member_name=?,used = 1 where bonus_id=?";
		this.daoSupport.execute(sql, orderid,ordersn,memberid,DateUtil.getDateline(),member.getUname()+"-"+member.getName(),bonusid);
		this.daoSupport.execute("update es_bonus_type set use_num=use_num+1 where type_id=?",bonus_type_id);
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.component.bonus.service.IBonusManager#returned(int)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)  
	public void returned(int orderid) {
		String sql="update es_member_bonus set order_sn=null,used_time=null,order_id=null,member_name=null  where order_id=?";
		this.daoSupport.execute(sql, orderid);
		
	}
	
	
	
	
/*************************************************************** 内部方法 *********************************************************************/
	
	/**
	 * 检查优惠券是否已经关联了促销活动
	 * add by DMRain 2016-6-16
	 * @param type_id 优惠券ID
	 * @return result 0：否，1：是
	 */
	private int checkForActivity(Integer type_id) {
		String sql = "select count(0) from es_activity a inner join es_activity_detail ad on a.activity_id = ad.activity_id "
				+ "where a.disabled = 0 and a.end_time > ? and ad.bonus_id = ?";
		int result = this.daoSupport.queryForInt(sql, DateUtil.getDateline(), type_id);
		result = result > 0 ? 1 : 0;
		return result;
	}
	
	/**
	 * 生成实体券编码
	 * @param prefix
	 * @return
	 */
	private String createSn(String prefix){
		
		StringBuffer sb = new StringBuffer();
		sb.append(prefix);
		sb.append( DateUtil.toString(new Date(), "yyMM"));
		sb.append( createRandom() );
		
		return sb.toString();
	}
	
	/**
	 * 创建随机数
	 * @return
	 */
	private String createRandom(){
		Random random  = new Random();
		StringBuffer pwd=new StringBuffer();
		for(int i=0;i<6;i++){
			pwd.append(random.nextInt(9));
			 
		}
		return pwd.toString();
	}

	

	
	
}
