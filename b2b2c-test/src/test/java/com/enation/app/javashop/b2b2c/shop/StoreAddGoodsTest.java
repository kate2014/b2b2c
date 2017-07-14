package com.enation.app.javashop.b2b2c.shop;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.enation.app.shop.core.goods.service.IGoodsCatManager;
import com.enation.app.shop.core.goods.service.IGoodsManager;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.test.SpringTestSupport;
import com.enation.framework.util.StringUtil;
/**
 * 商家发布商品
 * @author yinchao
 * @version v1.0
 * @since v6.2
 * 2017年3月2日 下午4:08:35
 */
@Rollback(true)
public class StoreAddGoodsTest extends SpringTestSupport{
	
	@Autowired
	private IDaoSupport daoSupport;
	
	@Autowired
	private IGoodsManager goodsManager;
	
	/**
	 * 未登录无法发布商品
	 * @throws Exception
	 */
	@Test
	public void NotLoginAddGoodsTest() throws Exception{
		
		//新增分类
		mockMvc.perform(MockMvcRequestBuilders.post("/api/b2b2c/goods-cat/add-goods-cat.do")
				.param("disable", "1")
				.param("store_cat_name", "addgoods分类")
				.param("store_cat_pid", "0")
				.param("store_sort", "1")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				)
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.result").value(0) );
		
		//发布商品
		mockMvc.perform(
				MockMvcRequestBuilders.post("/api/b2b2c/store-goods/add.do")
				.param("brand_id", "0")
				.param("cat_id", "2")
				.param("cost", "100")
				.param("file", "")
				.param("goods_transfee_charge", "1")
				.param("groupnames", "规格参数")
				.param("intro", "")
				.param("market_enable", "1")
				.param("mktprice", "100")
				.param("name","添加测试商品1")
				.param("paramnames", "测试")
				.param("paramnums", "9")
				.param("paramvalues","规格1")
				.param("picnames", "http://localhost:8080/b2b2c/statics/attachment//store/34/goods/2017/3/2/21//19043976.jpeg")
				.param("price", "100")
				.param("propnames", "产地")
				.param("propvalues", "0")
				.param("sn", "201703030001")
				.param("storeGoodsId", "451")
				.param("store_cat_id", "27")
				.param("type_id", "1")
				.param("unit", "g")
				.param("weight","100")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				)
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.result").value(0));
	}
	/**
	 * 新会员注册：申请开店、发布商品
	 * @throws Exception
	 */
	@Test
	public void applyStoreNowTest() throws Exception{
		
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
		
		//执行代码请求
		mockMvc.perform(MockMvcRequestBuilders.get("/api/validcode/create.do?vtype=memberreg"));
		
		//注册新会员
		mockMvc.perform(
				MockMvcRequestBuilders.post("/api/shop/member/register.do")
				.param("email","addgoods@qq.com")
				.param("license", "agree")
				.param("passwd_re", "111111")
				.param("password", "111111")
				.param("username", "addgoods")
				.param("validcode", "1111")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				)
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.result").value(1));
		
		//会员登录
		this.storeLoginTest();
		
		//马上开店
		mockMvc.perform(
				MockMvcRequestBuilders.post("/api/b2b2c/store-api/apply.do")
				.param("attr", "北京西城区内环测试地址")
				.param("bank_account_name","addgoods")
				.param("bank_account_number", "10999999999999999999")
				.param("bank_city", "海淀区")
				.param("bank_city_id","2800")
				.param("bank_code", "10999999999999999999")
				.param("bank_name","测试银行")
				.param("bank_province","北京")
				.param("bank_province_id", "1")
				.param("bank_region","三环以内")
				.param("bank_region_id","2848")
				.param("bank_town", "")
				.param("bank_town_id", "")
				.param("id_number", "120000000000000000")
				.param("store_city","西城区")
				.param("store_city_id", "2801")
				.param("store_id_img","")
				.param("store_license_img", "")
				.param("store_name", "addgoods")
				.param("store_name_auth","2")
				.param("store_province", "北京")
				.param("store_province_id", "1")
				.param("store_region", "内环到二环里")
				.param("store_region_id","2827")
				.param("store_store_auth","2")
				.param("store_town", "")
				.param("store_town_id", "")
				.param("tel", "13666666666")
				.param("zip", "100000")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				)
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.result").value(1));
		
		
		
				//管理员登录
				this.storeAdminLogin();
				
				//获取最后一个storeId
				int storeId=this.daoSupport.getLastId("es_store");
				
				//获取最后一个会员id，member_id
				int member_id=this.daoSupport.getLastId("es_member");
				
				//店铺：审核店铺（审核通过，开店成功）
				mockMvc.perform(MockMvcRequestBuilders.post("/b2b2c/admin/store/audit-pass.do")
						.param("commission","0")
						.param("disabled", "0")
						.param("member_id", StringUtil.toString(member_id))
						.param("pass", "1")
						.param("store_id",StringUtil.toString(storeId))
						.session(session)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						)
						.andDo(MockMvcResultHandlers.print())
						.andExpect(MockMvcResultMatchers.jsonPath("$.result").value(1));
				
				//商家会员登录
				this.storeLoginTest();
				
				//进入商家中心、添加商品分类，发布商品
				mockMvc.perform(
						MockMvcRequestBuilders.post("/api/b2b2c/goods-cat/add-goods-cat.do")
						.param("disable", "1")
						.param("store_cat_name", "addgoods分类")
						.param("store_cat_pid", "0")
						.param("store_sort", "1")
						.session(session)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						)
						.andDo(MockMvcResultHandlers.print())
						.andExpect(MockMvcResultMatchers.jsonPath("$.result").value(1) );
				
				//发布商品
				mockMvc.perform(
						MockMvcRequestBuilders.post("/api/b2b2c/store-goods/add.do")
						.param("brand_id", "0")
						.param("cat_id", "2")
						.param("cost", "100")
						.param("file", "")
						.param("goods_transfee_charge", "1")
						.param("groupnames", "规格参数")
						.param("intro", "")
						.param("market_enable", "1")
						.param("mktprice", "100")
						.param("name","添加测试商品1")
						.param("paramnames", "测试")
						.param("paramnums", "9")
						.param("paramvalues","规格1")
						.param("picnames", "http://localhost:8080/b2b2c/statics/attachment//store/34/goods/2017/3/2/21//19043976.jpeg")
						.param("price", "100")
						.param("propnames", "产地")
						.param("propvalues", "0")
						.param("sn", "201703030001")
						.param("storeGoodsId", "451")
						.param("store_cat_id", "27")
						.param("type_id", "1")
						.param("unit", "g")
						.param("weight","100")
						.session(session)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						)
						.andDo(MockMvcResultHandlers.print())
						.andExpect(MockMvcResultMatchers.jsonPath("$.result").value(1));
						
	}

	
	/**
	 * 会员登录
	 * @throws Exception
	 */
	private void storeLoginTest() throws Exception{
		
		//--------------登录----------------------
		//执行验证码请求
		mockMvc.perform(MockMvcRequestBuilders.get("/api/validcode/create.do?vtype=memberlogin"));
		
		//会员登录
		mockMvc.perform(MockMvcRequestBuilders.post("/api/shop/member/login.do")
				.param("password", "111111")
				.param("username", "addgoods")
				.param("validcode","1111")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				)
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.result").value(1));
		
	}
	
	/**
	 * admin管理员登录
	 * @throws Exception
	 */
	private void storeAdminLogin() throws Exception{
		
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
}
