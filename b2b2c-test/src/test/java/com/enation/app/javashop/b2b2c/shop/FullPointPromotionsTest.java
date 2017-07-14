package com.enation.app.javashop.b2b2c.shop;

import java.util.Map;

import org.junit.Assert;

import net.sf.json.JSONObject;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.enation.app.shop.core.member.model.MemberAddress;
import com.enation.app.shop.core.member.service.IMemberAddressManager;
import com.enation.app.shop.core.order.model.Order;
import com.enation.app.shop.core.order.service.IOrderManager;
import com.enation.app.shop.core.order.service.OrderStatus;
import com.enation.app.shop.core.other.service.IActivityManager;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.test.SpringTestSupport;
import com.enation.framework.util.StringUtil;

/**
 * 满X金额赠送积分
 * @author yinchao
 * @version v1.0
 * @since v6.2.1
 * 2017年3月14日上午11:27:06
 */
@Rollback(true)
public class FullPointPromotionsTest extends SpringTestSupport{
	
	@Autowired
	private IDaoSupport daoSupport;
	
	@Autowired
	private IMemberAddressManager memberAddressManager;
	
	@Autowired
	private IOrderManager orderManager;
	
	/**
	 * 未登录购买满X金额，赠送积分，没有权限
	 * @throws Exception
	 */
	@Test
	public void notLoginPromotionsTest() throws Exception{
		
		//调用添加促销赠送积分活动
		this.addPointPromotionsTest();
		
		//会员退出
		mockMvc.perform(
				MockMvcRequestBuilders.post("/api/shop/member/logout.do")
				.param("store_id", "17")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				)
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.result").value(1));
		
		//商品加入购物车
		mockMvc.perform(
				MockMvcRequestBuilders.post("/api/store/store-cart/add-product.do")
				.param("activity_id","1")
				.param("action", "add-product")
				.param("ajax","yes")
				.param("exchange","")
				.param("goodsid", "348")
				.param("havespec", "1")
				.param("num", "1")
				.param("productid", "314")
				.param("showCartData", "0")
				.param("store_id", "17")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				)
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.result").value(1));
		
		//创建订单
		mockMvc.perform(
				MockMvcRequestBuilders.post("/api/store/store-order/create.do")
				.param("activity_id", "1")
				 .param("addressId", "1")
				 .param("paymentId", "0")
				 .param("typeId", "0")
				 .param("bonusid", "0-0")
				 .param("storeid", "1")
				 .param("remark", "")
				 .param("shipDay", "任意时间")
				 .param("receipt", "2")
				 .param("receiptType", "1")
				 .param("receiptContent", "办公用品")
				 .param("receiptTitle", "")
				 .session(session)
				 .contentType(MediaType.APPLICATION_JSON)
				 .accept(MediaType.APPLICATION_JSON)
				 )
				 .andDo(MockMvcResultHandlers.print())
				 .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(0));
	}
	
	/**
	 * 商家满X金额赠送积分促销活动
	 * @throws Exception
	 */
	@Test
	public void addPointPromotionsTest() throws Exception{
		
		//清空促销活动列表
		this.daoSupport.execute("TRUNCATE table es_activity");
		
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
		
		//--------------商家登录----------------------
		//执行验证码请求
		mockMvc.perform(MockMvcRequestBuilders.get("/api/validcode/create.do?vtype=memberlogin"));
		
		//kans商家登录
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
				.andExpect(MockMvcResultMatchers.jsonPath("$.result").value(1));
		
		//添加促销活动
		mockMvc.perform(
				MockMvcRequestBuilders.post("/api/b2b2c/store-activity/save-add.do")
				.param("activity_name","满50元赠送100积分")
				.param("activity_type", "1")
				.param("bonus_id", "0")
				.param("description", "满50元赠送100积分")
				.param("endTime", "2019-03-14 14:15:26")
				.param("full_money", "50")
				.param("gift_id", "0")
				.param("goods_id", "426")
				.param("is_send_point", "1")
				.param("keyword","")
				.param("minus_value", "")
				.param("point_value", "100")
				.param("range_type", "2")
				.param("startTime", "2017-03-14 14:15:23")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				)
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.result").value(1));
	}
	
	
	
	/**
	 * 会员下单：登录、添加地址、加入购物车、下单、确认收款、发货、确认收货
	 * @throws Exception
	 */
	@Test
	public void funllPointOrderTest() throws Exception{
		
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
		
		//调用添加促销活动
		this.addPointPromotionsTest();
		
		//登录
		this.login();
		
		//--------------创建订单----------------------
		
		//商品加入购物车
		mockMvc.perform(
				MockMvcRequestBuilders.post("/api/store/store-cart/add-product.do")
				.param("action", "add-product")
				.param("ajax", "yes")
				.param("exchange", "")
				.param("goodsid", "426")
				.param("havespec", "1")
				.param("num", "1")
				.param("productid","432")
				.param("showCartData","0")
				.param("store_id","17")
				.param("activity_id","1")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				)
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.result").value(1));
		
		//获取会员默认地址
		MemberAddress addr = this.memberAddressManager.getMemberDefault(16);
		
		//将地址压入session
		mockMvc.perform(
				MockMvcRequestBuilders.post("/api/store/store-order/change-address.do")
				.param("address_id","" + addr.getAddr_id())
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				)
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.result").value(1));
		
		//创建订单
		MvcResult result = mockMvc.perform(
				MockMvcRequestBuilders.post("/api/store/store-order/create.do")
				.param("addressId", "" + addr.getAddr_id())
				.param("activity_id","1")
				.param("bonusid","0-0")	
				.param("paymentId","0")
				.param("receipt", "1")
				.param("receiptContent","办公用品")
				.param("receiptTitle", "易族智汇（北京）科技有限公司")
				.param("receiptType", "2")
				.param("remark", "商品一定要条仔细！！")
				.param("shipDay", "任意时间")
				.param("storeid", "17")
				.param("typeId", "0")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				)
				.andDo(MockMvcResultHandlers.print())
				
				//返回正确结果
				.andExpect(MockMvcResultMatchers.jsonPath("$.result").value(1))
				
				//商品价格为199
				.andExpect(MockMvcResultMatchers.jsonPath("$.data.goods_amount").value(199.0))
				
				//商品总金额199
				.andExpect(MockMvcResultMatchers.jsonPath("$.data.order_amount").value(199.0))
				
				//运费总计
				.andExpect(MockMvcResultMatchers.jsonPath("$.data.shipping_amount").value(0.0))
				
				.andReturn();
		
				//由返回结果中查出orderid
				String resultStr = result.getResponse().getContentAsString();
				JSONObject orderResult = JSONObject.fromObject(resultStr);
				Map orderData = (Map) orderResult.get("data");
				Integer orderid =(Integer) orderData.get("order_id");
				
				//根据orderid获取paymentid
				int paymentid = this.daoSupport.queryForInt("select payment_id from es_payment_logs where order_id=?", orderid);
				
				//--------------确认收款----------------------
				
				//平台管理员登录
				this.adminLogin();
				
				//平台确认收款
				mockMvc.perform(
						MockMvcRequestBuilders.post("/shop/admin/payment/pay.do")
						.param("orderId","" +orderid)
						.param("payment_id", "" + paymentid)
						.param("paymoney","199")
						.session(session)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						)
						.andDo(MockMvcResultHandlers.print())
						.andExpect(MockMvcResultMatchers.jsonPath("$.result").value(1));
				
				//断言订单为已支付状态
				Order order = this.orderManager.get(orderid);
				Assert.assertEquals(OrderStatus.ORDER_PAY, order.getStatus().intValue());
				
				//--------------发货----------------------
				
				//平台管理员填写快递单号
				mockMvc.perform(MockMvcRequestBuilders.post("/shop/admin/order-print/save-ship-no.do")
						.param("order_id","" + orderid)
						.param("expressno", "1234567890")
						.param("logi_name", "圆通速递")
						.param("logi_id", "1")
						.session(session)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						)
						.andDo(MockMvcResultHandlers.print())
						.andExpect(MockMvcResultMatchers.jsonPath("$.result").value(1));
				
				//发货
				mockMvc.perform(
						MockMvcRequestBuilders.post("/shop/admin/order-print/ship.do")
						.param("order_id","" + orderid)
						.session(session)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						)
						.andDo(MockMvcResultHandlers.print())
						.andExpect(MockMvcResultMatchers.jsonPath("$.result").value(1));
				
				//断言发货状态
				order =this.orderManager.get(orderid);
				Assert.assertEquals(OrderStatus.ORDER_SHIP, order.getStatus().intValue());
				Assert.assertEquals(OrderStatus.SHIP_YES,order.getShip_status().intValue());
				
				//--------------确认收货----------------------	
				
				//购买会员登录
				this.login();
				
				//会员确认收货
				mockMvc.perform(
						MockMvcRequestBuilders.post("/api/shop/order/rog-confirm.do")
						.param("orderId", "" + orderid)
						.session(session)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						)
						.andDo(MockMvcResultHandlers.print())
						.andExpect(MockMvcResultMatchers.jsonPath("$.result").value(1));
				
				//断言收货状态
				order = this.orderManager.get(orderid);
				Assert.assertEquals(OrderStatus.ORDER_COMPLETE, order.getStatus().intValue());
				Assert.assertEquals(OrderStatus.SHIP_ROG, order.getShip_status().intValue());
	}
	
	
	/**
	 * 共用登录方法
	 * @throws Exception
	 */
	private void login() throws Exception{
		
		//执行验证码请求
		mockMvc.perform(MockMvcRequestBuilders.get("/api/validcode/create.do?vtype=memberlogin"));
		
		//会员food登录
		mockMvc.perform(
				MockMvcRequestBuilders.post("/api/shop/member/login.do")
				.param("password","111111")
				.param("username", "food")
				.param("validcode", "1111")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				)
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.result").value(1));
		
		//为会员添加一个默认的地址
		mockMvc.perform(
				MockMvcRequestBuilders.post("/api/shop/member-address/add.do")
				.param("addr", "通州测试地址")
				.param("city", "通州区")
				.param("city_id", "38")
				.param("def_addr", "1")
				.param("mobile", "13888888888")
				.param("name", "food")
				.param("province", "北京")
				.param("province_id","1")
				.param("region", "通州区")
				.param("region_id","456")
				.param("shipAddressName", "公司")
				.param("tel", "0467-2433742")
				.param("town", "")
				.param("town_id", "")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				)
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.result").value(1));
	}
	
	/**
	 * 共用平台管理员登录
	 * @throws Exception
	 */
	private void adminLogin() throws Exception{
		
		//执行验证码请求
		mockMvc.perform(MockMvcRequestBuilders.get("/api/validcode/create.do?vtype=admin"));
	
		//管理员登录
		mockMvc.perform(
				MockMvcRequestBuilders.post("/core/admin/admin-user/login.do")
				.param("username", "admin")
				.param("password", "admin")
				.param("valid_code","1111")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				)
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.result").value(1));
				
	}
	
	
	
	
	
	
}
