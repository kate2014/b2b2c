package com.enation.app.javashop.b2b2c.shop;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.enation.framework.test.SpringTestSupport;
/** 
*
* @author LYH 
* @version v1.0
* @since v6.2
* 2017年1月14日 下午3:08:25 
*/
public class StoreProfileStatisticsCollectChartJsonTest extends SpringTestSupport {

	/**
	 * 登录测试收藏商品，在商家中心收藏统计统计【这单元测试只测试单个收藏商品】
	 * @throws Exception
	 */
	@Test
	public void addCollectAfterCollectTest() throws Exception {

		// 执行验证码请求
		mockMvc.perform(MockMvcRequestBuilders.get("/api/validcode/create.do?vtype=memberlogin"));
		//登录API
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
		//收藏商品
		mockMvc.perform(
				 MockMvcRequestBuilders.get("/api/b2b2c/goods-collect/add-collect.do")
				 .param("goods_id","401")
				 .contentType(MediaType.APPLICATION_JSON)
				 .accept(MediaType.APPLICATION_JSON)
				 )
		         .andDo(MockMvcResultHandlers.print())  
		         .andExpect(MockMvcResultMatchers.status().isOk())
		         .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(1) );  
		//注销
		mockMvc.perform(
				 MockMvcRequestBuilders.post("/api/shop/member/logout.do")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				)
		 		.andDo(MockMvcResultHandlers.print())  
		 		.andExpect(MockMvcResultMatchers.jsonPath("$.result").value(1) ); 
		
		// 执行验证码请求
				mockMvc.perform(MockMvcRequestBuilders.get("/api/validcode/create.do?vtype=memberlogin"));
				//登录API
				mockMvc.perform(
						MockMvcRequestBuilders.post("/api/shop/member/login.do")
						.param("username", "kans")
						.param("password", "111111")
						.param("validcode", "1111")
						.session(session)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						)
						.andDo(MockMvcResultHandlers.print())  
						.andExpect(MockMvcResultMatchers.jsonPath("$.result").value(1) );  
						
						// 读取收藏甘特图Json
				mockMvc.perform(
						MockMvcRequestBuilders.post("/api/store-profile/get-collect-chart-json.do")
						.param("storeId", "17")
						.session(session)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						)
				  		.andDo(MockMvcResultHandlers.print())  
				  		.andExpect(MockMvcResultMatchers.jsonPath("$[0].y").value(1) );  
						
					 
				
						//读取收藏列表
				mockMvc.perform(
						MockMvcRequestBuilders.post("/api/store-profile/get-collect-json.do")
						.param("storeId", "17")
						.session(session)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						)
				  		.andDo(MockMvcResultHandlers.print())  
			         	.andExpect(MockMvcResultMatchers.jsonPath("$.total").value(1) ); 
	}
	
	/**
	 * 未登录收藏商品
	 * @throws Exception
	 */
	@Test
	public void addCollectNotLoginTest() throws Exception{
		
		//未登录  收藏商品
		mockMvc.perform(
				 MockMvcRequestBuilders.post("/api/b2b2c/goods-collect/add-collect.do")
				 .param("goods_id", "401")
				 .contentType(MediaType.APPLICATION_JSON)
				 .accept(MediaType.APPLICATION_JSON)
				 )
		         .andDo(MockMvcResultHandlers.print())  
	 	         .andExpect( MockMvcResultMatchers.jsonPath("$.result").value(0));
	}
	
}
