package com.enation.app.javashop.b2b2c.shop;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.enation.framework.test.SpringTestSupport;

/** 
* 完善会员基本资料单元测试类
* @author LYH 
* @version v1.0
* @since v6.2
* 2017年2月18日 上午3:19:30 
*/
public class MemberInfoUpdateTest extends SpringTestSupport{

	/**
	 * 测试不登陆进行修改，是没有权限的
	 * @throws Exception
	 */
	@Test
	public void memberInfoNologionTest() throws Exception{
		

		//未登录进行访问无权进行修改
		mockMvc.perform(
				 MockMvcRequestBuilders.post("/api/shop/member/save-info.do")
				 .param("file", "")
				 .param("truename", "food11")
				 .param("sex", "0")
				 .param("mybirthday", "2017-02-18")
				 .param("province", "上海")
				 .param("province_id", "2")
				 .param("city", "徐汇区")
				 .param("city_id", "2813")
				 .param("region", "城区")
				 .param("region_id", "51976")
				 .param("town", "")
				 .param("town_id", "")
				 .param("address", "浦东新区")
				 .param("email", "food11@qq.com")
				 .param("zip", "100100")
				 .param("tel", "06356877715")
				 .contentType(MediaType.APPLICATION_JSON)
				 .accept(MediaType.APPLICATION_JSON)
				 )
		         .andDo(MockMvcResultHandlers.print())  
	 	         .andExpect( MockMvcResultMatchers.content().bytes( "ajax 401 没有访问权限".getBytes() ));
	}
	/**
	 * 测试登陆后进行修改
	 * @throws Exception
	 */
	@Test
	public void memberInfoLogionTest() throws Exception{
		
		//会员登录
		this.memberlogin();
		
		//模拟图片上传
        MockMultipartFile file = new MockMultipartFile("file", "face.jpg","multipart/form-data","some pic".getBytes()); 
	    
        //登录后进行修改
		mockMvc.perform(
				 MockMvcRequestBuilders.fileUpload("/api/shop/member/save-info.do").file(file)
				 .param("truename", "food11")
				 .param("sex", "0")
				 .param("mybirthday", "2017-02-18")
				 .param("province", "上海")
				 .param("province_id", "2")
				 .param("city", "徐汇区")
				 .param("city_id", "2813")
				 .param("region", "城区")
				 .param("region_id", "51976")
				 .param("town", "")
				 .param("town_id", "")
				 .param("address", "浦东新区")
				 .param("email", "food11@qq.com")
				 .param("zip", "100100")
				 .param("tel", "06356877715")
				 .session(session)
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
