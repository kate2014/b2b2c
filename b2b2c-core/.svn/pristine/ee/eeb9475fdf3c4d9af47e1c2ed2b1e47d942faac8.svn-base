package com.enation.app.b2b2c.core.store.service.statistics.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.b2b2c.core.store.service.statistics.IStoreLogsManager;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;

/**
 * 店铺操作日志
 * @author fk
 * @version v1.0
 * @since v6.2
 * 2016年12月13日 下午2:25:20
 */
@Service
public class StoreLogsManager implements IStoreLogsManager{

	@Autowired
	private IDaoSupport daoSupport;
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.store.service.statistics.IStoreLogsManager#getStoreLogsList(int, int, java.util.Map)
	 */
	@Override
	public Page getStoreLogsList(int page, int pageSize, Map map) {
		String sql = "select log_type type,operator_name name,log_detail detail,log_time time from es_store_logs where 1=1 ";
		
		Integer storeid = (Integer) map.get("storeid");
		Integer userid = (Integer) map.get("userid");
		String type = (String) map.get("type");
		String start_time = (String) map.get("start_time");
		String end_time = (String) map.get("end_time");
		
		if(storeid!=null){
			sql+= " and store_id = "+storeid;
		}
		
		if(userid!=null && userid!=0){
			sql+= " and operator_id = "+userid;
		}
		
		if(!StringUtil.isEmpty(type) && !type.equals("0")){
			sql+= " and log_type like '%"+ type +"%' ";
		}
		
		if(!StringUtil.isEmpty(start_time)){			
			long stime = DateUtil.getDateline(start_time+" 00:00:00", "yyyy-MM-dd HH:mm:ss");
			sql+=" and log_time>"+stime;
		}
		
		if(!StringUtil.isEmpty(end_time)){			
			long etime = DateUtil.getDateline(end_time +" 23:59:59", "yyyy-MM-dd HH:mm:ss");
			sql+=" and log_time<"+etime;
		}
		
		sql += " order by log_time desc ";
		
		return daoSupport.queryForPage(sql, page, pageSize);
	}

}
