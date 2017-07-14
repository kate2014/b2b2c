package com.enation.app.b2b2c.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.service.dbsolution.DBSolutionFactory;
import com.enation.app.base.core.service.solution.IInstaller;
import com.enation.eop.resource.IMenuManager;
import com.enation.eop.sdk.context.EopSetting;
import com.enation.framework.component.IComponent;
import com.enation.framework.context.spring.SpringContextHolder;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.data.IDataOperation;
@Component
/**
 * b2b2c组件
 */
public class B2b2cComponent implements IComponent{
	
	@Autowired
	private IDaoSupport daoSupport;
	
	@Autowired
	private IDataOperation dataOperation;
	
	@Autowired
	private IMenuManager menuManager;
	
	@Override
	public void install() {
		dataOperation.imported("file:com/enation/app/b2b2c/component/b2b2c_install.xml");
		
		/**
		 * 安装b2b2c数据库索引
		 */
		dataOperation.imported("file:com/enation/app/b2b2c/core/b2b2c_index.xml");
		
		//修改后台显示菜单
		daoSupport.execute("update es_index_item set url='/b2b2c/admin/b2b2c-index-item/order.do' where id=2 ");
		if("2".equals(EopSetting.DBTYPE)){
			daoSupport.execute("update es_index_item set url='/b2b2c/admin/b2b2c-index-item/order.do' where id=1001 ");
			
		}
		
		IInstaller installer  = SpringContextHolder.getBean("storeSettingInstaller");
		
		installer.install("b2b2c", null);
		//安装b2b2c时  删除掉(会员等级) 菜单列表
		this.menuManager.delete("会员等级");
		
	}

	@Override
	public void unInstall() {
		dataOperation.imported("file:com/enation/app/b2b2c/component/b2b2c_uninstall.xml" );
	}

}
