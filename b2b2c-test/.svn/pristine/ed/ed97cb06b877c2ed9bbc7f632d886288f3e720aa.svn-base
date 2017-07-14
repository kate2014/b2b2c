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
*
* @author LYH 
* @version v1.0
* @since v6.2
* 2017年2月18日 下午3:08:25 
*/
public class MemberAddressTest extends SpringTestSupport{
	
	@Autowired
	private IDaoSupport daoSupport;

	/**
	 * 测试不登陆时进行操作，是没有权限的
	 * @throws Exception
	 */
	@Test
	public void memberAddressNotloginTest() throws Exception{
		
		//未登录无权限进行添加地址
		mockMvc.perform(
				MockMvcRequestBuilders.post("/api/shop/member-address/add.do")
				.param("def_addr", "1")
				.param("name", "kill")
				.param("mobile", "15030658275")
				.param("tel", "06356877789")
				.param("province", "北京")
				.param("province_id", "1")
				.param("city", "海淀区")
				.param("city_id", "2800")
				.param("region", "三环以内")
				.param("region_id", "2848")
				.param("town", "")
				.param("town_id", "")
				.param("addr", "111111")
				.param("shipAddressName", "家里")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				)
				.andDo(MockMvcResultHandlers.print())  
				.andExpect( MockMvcResultMatchers.content().bytes( "ajax 401 没有访问权限".getBytes() ));

		//未登录无权限进行修改
		mockMvc.perform(
				MockMvcRequestBuilders.post("/api/shop/member-address/edit.do")
				.param("addr_id", "33")
				.param("def_addr", "0")
				.param("name", "koko")
				.param("mobile", "15030658275")
				.param("tel", "06356877789")
				.param("province", "北京")
				.param("province_id", "1")
				.param("city", "海淀区")
				.param("city_id", "2800")
				.param("region", "三环以内")
				.param("region_id", "2848")
				.param("town", "")
				.param("town_id", "")
				.param("addr", "111111")
				.param("shipAddressName", "公司")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				)
				.andDo(MockMvcResultHandlers.print())  
				.andExpect( MockMvcResultMatchers.content().bytes( "ajax 401 没有访问权限".getBytes() )); 

		//未登录无权限进行删除
		mockMvc.perform(
				MockMvcRequestBuilders.post("/api/shop/member-address/delete.do")
				.param("addr_id", "22")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				)
				.andDo(MockMvcResultHandlers.print())  
				.andExpect( MockMvcResultMatchers.content().bytes( "ajax 401 没有访问权限".getBytes() )); 
	
	}
	/**
	 * 登录-添加地址-修改地址-删除地址
	 * @throws Exception
	 */
	@Test
	public void memberAddressLoginTest() throws Exception{
		
		//会员进行登录
		this.memberlogin();
		
		//登录-添加地址
		mockMvc.perform(
				MockMvcRequestBuilders.post("/api/shop/member-address/add.do")
				.param("def_addr", "1")
				.param("name", "kill")
				.param("mobile", "15030658275")
				.param("tel", "06356877789")
				.param("province", "北京")
				.param("province_id", "1")
				.param("city", "海淀区")
				.param("city_id", "2800")
				.param("region", "三环以内")
				.param("region_id", "2848")
				.param("town", "")
				.param("town_id", "")
				.param("addr", "111111")
				.param("shipAddressName", "家里")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				)
				.andDo(MockMvcResultHandlers.print())  
				.andExpect(MockMvcResultMatchers.jsonPath("$.result").value(1) );
		
		//添加地址为默认地址,根据默认地址的唯一性def_addr=1,获取地址的addr_id
		int addr_id = this.daoSupport.queryForInt("select addr_id from es_member_address where def_addr=? and member_id=16", 1); 
		
		//登录-添加地址-编辑地址
		mockMvc.perform(
				MockMvcRequestBuilders.post("/api/shop/member-address/edit.do")
				.param("addr_id", ""+addr_id)
				.param("def_addr", "0")
				.param("name", "koko")
				.param("mobile", "15030658275")
				.param("tel", "06356877789")
				.param("province", "北京")
				.param("province_id", "1")
				.param("city", "海淀区")
				.param("city_id", "2800")
				.param("region", "三环以内")
				.param("region_id", "2848")
				.param("town", "")
				.param("town_id", "")
				.param("addr", "111111")
				.param("shipAddressName", "公司")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				)
				.andDo(MockMvcResultHandlers.print())  
				.andExpect(MockMvcResultMatchers.jsonPath("$.result").value(1) );
		
		//登录-添加地址-修改地址-删除地址
		mockMvc.perform(
				MockMvcRequestBuilders.post("/api/shop/member-address/delete.do")
				.param("addr_id", ""+addr_id)
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
				 .contentType(MediaType.APPLICATION_JSON)
				 .accept(MediaType.APPLICATION_JSON)
				 .session(session)
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
