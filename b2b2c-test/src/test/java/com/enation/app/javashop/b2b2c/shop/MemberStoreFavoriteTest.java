package com.enation.app.javashop.b2b2c.shop;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.enation.framework.database.IDaoSupport;
import com.enation.framework.test.SpringTestSupport;

/** 
* 我的收藏--收藏店铺   单元测试
* @author LYH 
* @version v1.0
* @since v6.2
* 2017年2月19日 上午12:18:46 
*/
public class MemberStoreFavoriteTest extends SpringTestSupport{
	
	@Autowired
	private IDaoSupport daoSupport;
	
	/**
	 * 测试不登陆进行收藏店铺，没有权限
	 * @throws Exception
	 */
	@Test
	public void storeFavoriteNologionTest() throws Exception{
		
		mockMvc.perform(
				 MockMvcRequestBuilders.post("/api/b2b2c/store-collect/add-collect.do")
				 .param("store_id", "17")
				 .session(session)
				 .contentType(MediaType.APPLICATION_JSON)
				 .accept(MediaType.APPLICATION_JSON)
				 )
		         .andDo(MockMvcResultHandlers.print())  
		         .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(0) ); 
	}
	/**
	 * 测试不登陆进行删除收藏的店铺，没有权限
	 * @throws Exception
	 */
	@Test
	public void storeDeleteFavoriteNologinTest() throws Exception{
		
		mockMvc.perform(
				 MockMvcRequestBuilders.post("/api/b2b2c/store-collect/del.do")
				 .param("celloct_id", "1")
				 .param("store_id", "17")
				 .session(session)
				 .contentType(MediaType.APPLICATION_JSON)
				 .accept(MediaType.APPLICATION_JSON)
				 )
		         .andDo(MockMvcResultHandlers.print())  
		         .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(0) );   
	}
	/**
	 * 收藏测试:登录-添加收藏-删除此店铺的收藏
	 * @throws Exception
	 */
	@Test
	public void storeFavoriteLogionTest() throws Exception{
		
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
		
		//会员登录
		this.memberlogin();
		
		//收藏店铺
		mockMvc.perform(
				MockMvcRequestBuilders.post("/api/b2b2c/store-collect/add-collect.do")
				 .param("store_id", "17")
				 .session(session)
				 .contentType(MediaType.APPLICATION_JSON)
				 .accept(MediaType.APPLICATION_JSON)
				 )
		         .andDo(MockMvcResultHandlers.print())  
		         .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(1) );
		
		//根据goods_id和member_id获取此商铺的 celloct_id
		int celloct_id = this.daoSupport.queryForInt("select id from es_member_collect where member_id=16 and store_id=?", 17);
		
		//删除收藏店铺
		mockMvc.perform(
				 MockMvcRequestBuilders.post("/api/b2b2c/store-collect/del.do")
				 .param("celloct_id", ""+celloct_id)
				 .param("store_id", "17")
				 .session(session)
				 .contentType(MediaType.APPLICATION_JSON)
				 .accept(MediaType.APPLICATION_JSON)
				 )
		         .andDo(MockMvcResultHandlers.print())  
		         .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(1) );
		
	}
	
	/**
	 * 共用的会员登陆方法
	 * @throws Exception
	 */
	private void memberlogin() throws Exception{

		//--------------登录----------------------
		//执行验证码请求		
		mockMvc.perform(MockMvcRequestBuilders.get("/api/validcode/create.do?vtype=memberlogin"));
			
		//登陆api测试
		mockMvc.perform(
				 MockMvcRequestBuilders.post("/api/shop/member/login.do")
				 .param("username", "food")
				 .param("password", "111111")
				 .param("validcode", "1111")
				 .session(session)
				 .contentType(MediaType.APPLICATION_JSON)
				 .accept(MediaType.APPLICATION_JSON)
				 
				 )
			 	.andDo(MockMvcResultHandlers.print())  
			 	.andExpect(MockMvcResultMatchers.jsonPath("$.result").value(1) );  
		//为会员添加一个默认地址
		mockMvc.perform(
				 MockMvcRequestBuilders.post("/api/shop/member-address/add.do")
				 .param("def_addr", "1")
				 .param("name", "虎虎")
				 .param("mobile", "15373167788")
				 .param("tel", "06356877715")
				 .param("province", "上海")
				 .param("province_id", "2")
				 .param("city", "徐汇区")
				 .param("city_id", "2813")
				 .param("region", "城区")
				 .param("region_id", "51976")
				 .param("town", "")
				 .param("town_id", "")
				 .param("addr", "测试地址")
				 .param("shipAddressName", "家里")
				 .session(session)
				 .contentType(MediaType.APPLICATION_JSON)
				 .accept(MediaType.APPLICATION_JSON)
				 )
			 	.andDo(MockMvcResultHandlers.print())  
			 	.andExpect(MockMvcResultMatchers.jsonPath("$.result").value(1) );  
						
	}


}
