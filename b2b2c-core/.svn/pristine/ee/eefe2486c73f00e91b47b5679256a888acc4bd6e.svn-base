package com.enation.app.b2b2c.core.store.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.b2b2c.core.store.model.StoreDlyType;
import com.enation.app.b2b2c.core.store.service.IStoreDlyTypeManager;
import com.enation.app.shop.core.order.model.support.DlyTypeConfig;
import com.enation.app.shop.core.order.model.support.TypeArea;
import com.enation.app.shop.core.order.model.support.TypeAreaConfig;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;

import net.sf.json.JSONObject;

/**
 * 配送方式
 * @author xulipeng
 * @author Kanon 2016-3-2;6.0版本改造
 */

@Service("storeDlyTypeManager")
public class StoreDlyTypeManager implements IStoreDlyTypeManager {
	@Autowired
	private IDaoSupport daoSupport;
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.store.service.IStoreDlyTypeManager#getDlyTypeById(java.lang.String)
	 */
	@Override
	public List getDlyTypeById(String typeid) {
		
		String sql ="select * from es_dly_type where type_id in ("+typeid+")";
		List list =this.daoSupport.queryForList(sql);
		
		return list;
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.IStoreDlyTypeManager#listByStoreId(int)
	 */
	@Override
	public Page pageByStoreId(int storeid,int pageNo,int pageSize) {
		
		/**
		 * 联合es_dly_type表和es_store_template表查询 某店铺的所有配送方式，两表通过tempate_id字段关联。
		 */
		String sql = "select dt.* from  es_dly_type dt,es_store_template st  where st.template_id=dt.template_id and st.store_id=? and def_temp=1 order by type_id";
		Page page  = this.daoSupport.queryForPage(sql, storeid,pageNo,pageSize);
		
		return page;
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.store.service.IStoreDlyTypeManager#getDlyTypeList(java.lang.Integer)
	 */
	@Override
	public List getDlyTypeList(Integer template_id) {
		//加上where条件 disabled=0 add by DMRain 2016-1-19
		String sql = "select * from es_dly_type where template_id=? and disabled=0 order by type_id";
		List list = this.daoSupport.queryForList(sql, template_id);
		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.store.service.IStoreDlyTypeManager#add(com.enation.app.b2b2c.core.store.model.StoreDlyType, com.enation.app.shop.core.order.model.support.DlyTypeConfig, com.enation.app.shop.core.order.model.support.TypeAreaConfig[])
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void add(StoreDlyType storeDlyType, DlyTypeConfig config,
			TypeAreaConfig[] configArray) {
		
		storeDlyType = this.fillType(storeDlyType, config);
		// }
		this.daoSupport.insert("es_dly_type", storeDlyType);
		Integer typeId = this.daoSupport.getLastId("es_dly_type");
		this.addTypeArea(typeId, configArray);
		
	}

	private StoreDlyType fillType(StoreDlyType storeDlyType, DlyTypeConfig config) {
		Double firstprice = config.getFirstprice(); // 首重费用
		Double continueprice = config.getContinueprice();// 续重费用
		Integer firstunit = config.getFirstunit(); // 首重重量
		Integer continueunit = config.getContinueunit(); // 续重重量

		// 组合公式
		String expressions = null;

		if (config.getUseexp() == 0) {
			expressions = this.createExpression(firstprice, continueprice,
					firstunit, continueunit);
		} else {
			expressions = config.getExpression();
		}

		storeDlyType.setExpressions(expressions);
		storeDlyType.setConfig(JSONObject.fromObject(config).toString());
		return storeDlyType;
	}
	
	private void addTypeArea(Integer typeId, TypeAreaConfig[] configArray) {
		for (TypeAreaConfig areaConfig : configArray) {
			if (areaConfig != null) {

				TypeArea typeArea = new TypeArea();
				typeArea.setArea_id_group(areaConfig.getAreaId());
				typeArea.setArea_name_group(areaConfig.getAreaName());
				typeArea.setType_id(typeId);
				typeArea.setHas_cod(areaConfig.getHave_cod());

				typeArea.setConfig(JSONObject.fromObject(areaConfig).toString());
				String expressions = "";
				if (areaConfig.getUseexp().intValue() == 1) { // 启用公式
					expressions = areaConfig.getExpression();
				} else {
					// 组合公式
					expressions = createExpression(areaConfig);
				}
				typeArea.setExpressions(expressions);
				this.daoSupport.insert("es_dly_type_area", typeArea);
			}
		}
	}
	
	/**
	 * 生成公式字串
	 * 
	 * @param areaConfig
	 * @return
	 */
	private String createExpression(TypeAreaConfig areaConfig) {

		return this.createExpression(areaConfig.getFirstprice(),
				areaConfig.getContinueprice(), areaConfig.getFirstunit(),
				areaConfig.getContinueunit());
	}
	
	/**
	 * 生成公式字串
	 * 
	 * @param firstprice
	 * @param continueprice
	 * @param firstunit
	 * @param continueunit
	 * @return
	 */
	private String createExpression(Double firstprice, Double continueprice,
			Integer firstunit, Integer continueunit) {
		return firstprice + "+tint(w-" + firstunit + ")/" + continueunit + "*"
				+ continueprice;
	}

	@Override
	public Integer getLastId() {
		Integer type_id = this.daoSupport.getLastId("es_dly_type");
		return type_id;
	}

	@Override
	public List getDlyTypeAreaList(Integer type_id) {
		String sql ="select * from es_dly_type_area where type_id=?";
		List list = this.daoSupport.queryForList(sql, type_id);
		return list;
	}


	@Override
	public Integer getDefaultDlyId(Integer store_id) {
		String sql = "select type_id from es_dly_type where store_id=? and defaulte=1";
		Integer id = this.daoSupport.queryForInt(sql, store_id);
		return id;
	}

	@Override
	public void del_dlyType(Integer tempid) {
		String sql  ="select * from es_dly_type where template_id=?";
		List<Map> list = this.daoSupport.queryForList(sql, tempid);
		if(!list.isEmpty()){
			StringBuffer dlyids =new StringBuffer();
			for(Map  map : list) {
				Integer type_id = (Integer) map.get("type_id");
				dlyids.append(type_id+",");
			}
			String ids = dlyids.toString().substring(0, dlyids.toString().length()-1);
			
			String areadelsql = "delete from es_dly_type_area where type_id in ("+ids+")";
			String dlydelsql = "delete from es_dly_type where template_id=?";
			this.daoSupport.execute(areadelsql);
			this.daoSupport.execute(dlydelsql,tempid);
		}
	}

	

}
