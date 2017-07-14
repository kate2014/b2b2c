package com.enation.app.javashop.b2b2c.shop;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.enation.app.base.core.service.IMemberManager;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.test.SpringTestSupport;
import com.enation.framework.util.StringUtil;



/**
 * 申请开店，审核不通过单元测试类
 * @author yinchao
 * @version v1.0
 * @since v6.2
 * 2017年3月2日 上午10:33:17
 */
@Rollback(true)
public class NewStoreErrorTest extends SpringTestSupport{
	
	@Autowired
	private IDaoSupport daoSupport;
	
	@Autowired
	private IMemberManager memberManager;
	
	/**
	 * 没有登录不能申请开店
	 * @throws Exception
	 */
	@Test
	public void NotLoginApplyStoreErrorTest() throws Exception{
		
		mockMvc.perform(
				MockMvcRequestBuilders.post("/api/b2b2c/store-api/apply.do")
				.param("attr", "测试地址")
				.param("bank_account_name","测试")
				.param("bank_account_number", "12344444232323232323")
				.param("bank_city", "西城区")
				.param("bank_city_id","2801")
				.param("bank_code", "12123333123232323232")
				.param("bank_name","测试")
				.param("bank_province","北京")
				.param("bank_province_id", "1")
				.param("bank_region","内环到二环里")
				.param("bank_region_id","2827")
				.param("bank_town", "")
				.param("bank_town_id", "")
				.param("id_number", "101525252522522525")
				.param("store_city","西城区")
				.param("store_city_id", "2801")
				.param("store_id_img","")
				.param("store_license_img", "")
				.param("store_name", "测试店铺")
				.param("store_name_auth","2")
				.param("store_province", "北京")
				.param("store_province_id", "1")
				.param("store_region", "内环到二环里")
				.param("store_region_id","2827")
				.param("store_store_auth","2")
				.param("store_town", "")
				.param("store_town_id", "")
				.param("tel", "13111111111")
				.param("zip", "100000")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				)
				.andDo(MockMvcResultHandlers.print())
				.andExpect( MockMvcResultMatchers.jsonPath("$.result").value(0)); 
	}
	
	/**
	 * 店铺:新注册会员，会员申请开店，平台管理员登录，审核不同，开店不成功
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
		        .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(1) );
		
		
		//执行验证码请求
		mockMvc.perform(MockMvcRequestBuilders.get("/api/validcode/create.do?vtype=memberreg"));
		
		//注册新的会员
		mockMvc.perform(
				MockMvcRequestBuilders.post("/api/shop/member/register.do")
				.param("email","errorstore@qq.com")
				.param("license", "agree")
				.param("passwd_re", "111111")
				.param("password", "111111")
				.param("username", "errorstore")
				.param("validcode", "1111")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.session(session)
				)
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.result").value(1));
		
		//会员登录
		this.storeLoginTest();
		
		//马上开店
		mockMvc.perform(
				MockMvcRequestBuilders.post("/api/b2b2c/store-api/apply.do")
				.param("attr", "上海市徐汇区测试提示")
				.param("bank_account_name","errorstore")
				.param("bank_account_number", "20222222222222222222")
				.param("bank_city", "徐汇区")
				.param("bank_city_id","2813")
				.param("bank_code", "29988888888888888888")
				.param("bank_name","测试银行")
				.param("bank_province","上海")
				.param("bank_province_id", "2")
				.param("bank_region","城区")
				.param("bank_region_id","51976")
				.param("bank_town", "")
				.param("bank_town_id", "")
				.param("id_number", "202333333333333333")
				.param("store_city","徐汇区")
				.param("store_city_id", "2813")
				.param("store_id_img","")
				.param("store_license_img", "")
				.param("store_name", "errorstore")
				.param("store_auth","0")
				.param("name_auth", "0")
				.param("store_province", "上海")
				.param("store_province_id", "2")
				.param("store_region", "城区")
				.param("store_region_id","51976")
				.param("store_store_auth","2")
				.param("store_town", "")
				.param("store_town_id", "")
				.param("tel", "13555555555")
				.param("zip", "200000")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				)
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.result").value(1));
		
		//管理员登录
		this.storeAdminLogin();
		
		//获取最后一个店铺id，storeId
		int storeId=this.daoSupport.getLastId("storeId");
		
		//获取最后一个会员id，member_id
		int member_id=this.daoSupport.getLastId("member_id");
		
		//店铺：审核店铺（审核不通过）
		mockMvc.perform(MockMvcRequestBuilders.post("/b2b2c/admin/store/audit-pass.do")
				.param("commission","0")
				.param("disabled", "0")
				.param("member_id", StringUtil.toString(member_id))
				.param("pass", "0")
				.param("store_id",StringUtil.toString(storeId))
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
		mockMvc.perform(
				MockMvcRequestBuilders.post("/api/shop/member/login.do")
				.param("password", "111111")
				.param("username", "errorstore")
				.param("validcode","1111")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.session(session)
				)
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.result").value(1));
		
	}
	
	/**
	 * 平台管理员登录
	 * @throws Excepiton
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
				.andExpect(MockMvcResultMatchers.jsonPath("$.result").value(1) ); 
	
	
		
	}

}
