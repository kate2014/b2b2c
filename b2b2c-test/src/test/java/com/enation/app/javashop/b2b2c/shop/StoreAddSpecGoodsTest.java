package com.enation.app.javashop.b2b2c.shop;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.enation.framework.database.IDaoSupport;
import com.enation.framework.test.SpringTestSupport;
import com.enation.framework.util.StringUtil;


/**
 * 商家发布规格商品
 * @author yinchao
 * @version v1.0
 * @since v6.2
 * 2017年3月8日上午10:36:59
 */
@Rollback(true)
public class StoreAddSpecGoodsTest extends SpringTestSupport{
	
	@Autowired
	private IDaoSupport daoSupport;
	
	/**
	 * 平台关联规格
	 * @throws Exception
	 */
	@Test
	public void linkSpecTest() throws Exception{
		
		//平台管理员登录
		this.adminLogin();
		
		//开启规格（坚果）
		mockMvc.perform(
				MockMvcRequestBuilders.post("/shop/admin/type/checkname.do")
				.param("have_parm", "1")
				.param("have_prop", "1")
				.param("join_brand", "1")
				.param("join_spec","1")
				.param("name","坚果")
				.param("type_id", "1")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				)
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.result").value(1));
		
		//关联规格（坚果）
		mockMvc.perform(
				MockMvcRequestBuilders.post("/shop/admin/type/save-spec.do")
				.param("choose_specs", "3")
				.param("typeId", "1")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				)
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.result").value(1));
		
				
	}
	
	/**
	 * 商家发布规格商品
	 * @throws Exception
	 */
	@Test
	public void specAddGoodsTest() throws Exception{
		
		//启用b2b2c组件
		mockMvc.perform(
				MockMvcRequestBuilders.post("/core/admin/component/start.do")
				.param("componentid", "b2b2cComponent")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				)
				.andDo(MockMvcResultHandlers.print())  
				.andExpect(MockMvcResultMatchers.jsonPath("$.result").value(1));
		
		//登录
		this.memberLogin();
			
		//商家中心发布规格商品
		mockMvc.perform(
				MockMvcRequestBuilders.post("/api/b2b2c/store-goods/add.do")
				.param("brand_id", "0")
				.param("cat_id", "2")
				.param("cost", "14")
				.param("costs", "14")
				.param("goods_transfee_charge", "1")
				.param("groupnames", "规格参数")
				.param("haveSpec","1")
				.param("intro", "")
				.param("market_enable", "1")
				.param("mktprice", "12")
				.param("paramnames", "规格")
				.param("paramnames", "重量")
				.param("paramnames", "产品标准号")
				.param("paramnames", "生产日期")
				.param("paramnames", "保质期")
				.param("paramnames", "储存方法")
				.param("paramnames", "配料")
				.param("paramnames", "产地")
				.param("paramnames", "厂家")
				.param("name", "测试规格商品")
				.param("paramnums", "9")
				.param("picnames", "http://localhost:8080/b2b2c/statics/attachment//store/15/goods/2017/3/9/20//09482199.jpeg")
				.param("price","13")
				.param("prices", "12")
				.param("propnames", "产地")
				.param("propnames", "包装")
				.param("sn", "1009988277")
				.param("sns", "1009988277-0")
				.param("specids","3")
				.param("specvalue_0", "1GB")
				.param("specvids", "58")
				.param("storeGoodsId","468")
				.param("store_cat_id", "2")
				.param("type_id", "1")
				.param("weight", "100")
				.param("weights", "100")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				)
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.result").value(1));
	
				
	}
	
	/**
	 * 修改库存
	 * @throws Exception
	 */
	@Test
	public void saveGoodsStoreTest() throws Exception{
		
		//启用b2b2c组件
		mockMvc.perform(
				MockMvcRequestBuilders.post("/core/admin/component/start.do")
				.param("componentid", "b2b2cComponent")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				)
				.andDo(MockMvcResultHandlers.print())  
				.andExpect(MockMvcResultMatchers.jsonPath("$.result").value(1));
				
		//登录
		this.memberLogin();
		
		//修改库存
		mockMvc.perform(
				MockMvcRequestBuilders.post("/api/b2b2c/store-goods/save-goods-store.do")
				.param("haveSpec","1")
				.param("goods_id", "446")
				.param("storeIds", "0")
				.param("storeNum", "10")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				)
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.result").value(1));
				
				
	}
	
	/**
	 * 平台共用登录
	 * @throws Exception
	 */
	private void adminLogin() throws Exception{
		
		//执行验证码请求
		mockMvc.perform(MockMvcRequestBuilders.get("/api/validcode/create.do?vtype=admin"));
		
		//管理员登录api测试
		mockMvc.perform(
				MockMvcRequestBuilders.post("/core/admin/admin-user/login.do")
				.param("username", "admin")
				.param("password", "admin")
				.param("valid_code", "1111")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				)
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.result").value(1));
	}	
	
	/**
	 * 会员共用登录
	 * @throws Exception
	 */
	private void memberLogin() throws Exception{
		
		//执行验证码请求
		mockMvc.perform(MockMvcRequestBuilders.get("/api/validcode/create.do?vtype=memberlogin"));
		
		//会员登录
		mockMvc.perform(
				MockMvcRequestBuilders.post("/api/shop/member/login.do")
				.param("password", "111111")
				.param("username", "food")
				.param("validcode", "1111")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				)
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.result").value(1));
		}
}
