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

/**
 * 商家进行商品删除，还原、彻底删除
 * @author yinchao
 * @version v1.0
 * @since v6.2
 * 2017年3月7日上午10:35:55
 */
@Rollback(true)
public class StoredelGoodsTest extends SpringTestSupport{
	
	@Autowired
	private IDaoSupport daoSupport;
	
	/**
	 * 未登录无法对商品进行任何操作
	 * @throws Exception
	 */
	@Test
	public void notLogin() throws Exception{
		
		//直接访问删除商品，无权删除
		mockMvc.perform(
				MockMvcRequestBuilders.post("/api/b2b2c/store-goods/delete-goods.do")
				.param("goods_id", "426")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				)
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.result").value(1));
		}
	
	/**
	 * 商家登录，对商品进行下架，然后删除到回收站中
	 * @throws Exception
	 */
	@Test
	public void delGoodsTest() throws Exception{
		
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
		
		//商家先登录
		this.login();
		
		//对商品进行下架
		mockMvc.perform(
				MockMvcRequestBuilders.post("/api/b2b2c/store-goods/edit.do")
				.param("brand_id", "0")
				.param("cat_id","45")
				.param("cost","159")
				.param("file","")
				.param("goods_id","425")
				.param("goods_transfee_charge","1")
				.param("groupnames","基本参数")
				.param("haveSpec","0")
				.param("intro", "")
				.param("market_enable", "0")
				.param("mktprice", "159")
				.param("name", "高夫聚能醒肤眼霜20g（男士专用淡化黑眼圈）")
				.param("paramnums","5")
				.param("picnames","http://static.b2b2cv2.javamall.com.cn/attachment//store/17/goods/2016/5/30/12//01049116.jpg")
				.param("price", "159")
				.param("propvalues","")
				.param("sn","201605300025")
				.param("storeGoodsId", "425")
				.param("store_cat_id", "20")
				.param("store_id","17")
				.param("type_id", "25")
				.param("weight", "20")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				)
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.result").value(1));
		
		//对商品进行删除
		mockMvc.perform(MockMvcRequestBuilders.post("/api/b2b2c/store-goods/delete-goods.do")
				.param("goods_id", "425")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				)
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.result").value(1));
				
	}
	
	/**
	 * 回收站还原
	 * @throws Exception
	 */
	@Test
	public void revertGoodsTest() throws Exception{
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
		this.login();
		
		//对商品进行下架
		mockMvc.perform(
				MockMvcRequestBuilders.post("/api/b2b2c/store-goods/edit.do")
				.param("brand_id", "0")
				.param("cat_id","50")
				.param("cost","90")
				.param("file","")
				.param("goods_id","424")
				.param("goods_transfee_charge","1")
				.param("groupnames","基本参数")
				.param("haveSpec","0")
				.param("intro", "")
				.param("market_enable", "0")
				.param("mktprice", "190")
				.param("name", "高夫gf舒爽须后水120ml")
				.param("paramnums","5")
				.param("picnames","http://static.b2b2cv2.javamall.com.cn/attachment//store/17/goods/2016/5/30/11//56103943.jpg")
				.param("price", "90")
				.param("propvalues","")
				.param("sn","201605300024")
				.param("storeGoodsId", "424")
				.param("store_cat_id", "22")
				.param("store_id","17")
				.param("type_id", "26")
				.param("weight", "330")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				)
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.result").value(1));
				
		//对商品进行删除
		mockMvc.perform(
				MockMvcRequestBuilders.post("/api/b2b2c/store-goods/delete-goods.do")
				.param("goods_id", "424")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				)
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.result").value(1));
		
		//对商品进行还原
		mockMvc.perform(
				MockMvcRequestBuilders.post("/api/b2b2c/store-goods/revert-goods.do")
				.param("goods_id", "424")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				)
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath(".result").value(1));
		
	}
	
	/**
	 * 共用的登录方法
	 * @throws Exception
	 */
	private void login() throws Exception{
		//--------------登录----------------------
		//执行验证码请求
		mockMvc.perform(MockMvcRequestBuilders.get("/api/validcode/create.do?vtype=memberlogin"));
		
		//会员登录
		mockMvc.perform(MockMvcRequestBuilders.post("/api/shop/member/login.do")
				.param("password", "111111")
				.param("username", "kans")
				.param("validcode","1111")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				)
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.result").value(1));
	}
}
