package com.enation.app.b2b2c.core.store.service.statistics;

import java.util.List;
import java.util.Map;

import com.enation.framework.database.Page;

/**
 * 店铺日志接口
 * @author fk
 * @version v1.0
 * @since v6.2
 * 2016年12月13日 下午2:19:47
 */
public interface IStoreLogsManager {

	/**
	 * 查询日志列表
	 * @param page
	 * @param pageSize
	 * @param map 条件
	 * @return
	 */
	public Page getStoreLogsList(int page , int pageSize , Map map);
}
