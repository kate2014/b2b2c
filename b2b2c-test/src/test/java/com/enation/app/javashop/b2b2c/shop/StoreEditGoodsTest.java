package com.enation.app.javashop.b2b2c.shop;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.enation.app.b2b2c.core.goods.service.IStoreGoodsManager;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.b2b2c.core.store.service.IStoreManager;
import com.enation.app.shop.core.goods.service.IGoodsManager;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.test.SpringTestSupport;

/**
 * 修改商家商品
 * @author yinchao
 * @version v1.0
 * @since v6.2
 * 2017年3月6日 下午3:33:14
 */
@Rollback(true)
public class StoreEditGoodsTest extends SpringTestSupport{

	@Autowired
	private IDaoSupport daoSupport;
	
	/**
	 * 会员商家登录后修改商品信息
	 * @throws Exception
	 */
	@Test
	public void EditGoodsTest() throws Exception{
	
		//商家登录
		this.storeLoginTest();
		
		//商家修改商品
		mockMvc.perform(
				MockMvcRequestBuilders.post("/api/b2b2c/store-goods/edit.do")
				.param("brand_id", "0")
				.param("cat_id", "396")
				.param("cost", "100")
				.param("file", "")
				.param("goods_id", "426")
				.param("goods_transfee_charge","1")
				.param("groupnames", "修改基本参数")
				.param("haveSpec", "0")
				.param("intro", "")
				.param("market_enable","1")
				.param("mktprice" ,"100")
				.param("name","修改欧莱雅（LOREAL）男士激能套装")
				.param("picnames","http://static.b2b2cv2.javamall.com.cn/attachment//store/17/goods/2016/5/30/12//06374159.jpg")
				.param("price","100")
				.param("propnames","修改功能")
				.param("sn","201605300026")
				.param("storeGoodsId","426")
				.param("store_cat_id","21")
				.param("store_id","17")
				.param("type_id","26")
				.param("weight", "360")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				)
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.result").value(1));
		
	}
	
	/**
	 * 共用的登录方式
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
				.param("username", "kans")
				.param("validcode","1111")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.session(session)
				)
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.result").value(1));
	}
	
}
