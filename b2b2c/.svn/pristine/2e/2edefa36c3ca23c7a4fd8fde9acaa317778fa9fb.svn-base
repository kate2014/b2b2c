package com.enation.app.b2b2c.front.api.member;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enation.app.b2b2c.core.goods.service.StoreCartContainer;
import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberAddressManager;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.shop.core.member.model.MemberAddress;
import com.enation.app.shop.core.member.service.IMemberAddressManager;
import com.enation.framework.action.GridController;
import com.enation.framework.action.JsonResult;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.JsonResultUtil;

import freemarker.template.TemplateModelException;

/**
 * 店铺会员收货地址 Action
 * @author XuLiPeng
 */
@Component 
@Controller
@RequestMapping("/api/b2b2c/member-address")
public class StoreMemberAddressApiController extends GridController {
	@Autowired
	private IMemberAddressManager memberAddressManager;
	@Autowired
	private IStoreMemberAddressManager storeMemberAddressManager;
	@Autowired
	private IStoreMemberManager storeMemberManager;

	/**
	 * 添加店铺会员收货地址
	 * @param address 接收地址,MemberAddress
	 * @return 返回json串
	 * result 	为1表示调用成功0表示失败
	 */
	@ResponseBody
	@RequestMapping(value="add-new-address")
	public JsonResult addNewAddress() {
		MemberAddress address = new MemberAddress();
		try {
			address = this.createAddress();
			int address_id = this.memberAddressManager.addAddress(address);
			address.setAddr_id(address_id);
			
			//如果新添加的收货地址为默认收货地址，就将新添加的默认收货地址信息set进session中
			//add_by DMRain 2016-7-15
			if (address.getDef_addr() == 1) {
				StoreCartContainer.putSelectedAddress(address);
			}
			
			return JsonResultUtil.getSuccessJson("添加成功"); 
		} catch (Exception e) {
			this.logger.error("前台添加地址错误", e);
		}
		return JsonResultUtil.getErrorJson("添加失败"); 
	}
	
	/**
	 * 设置会员默认收货地址
	 * @param member 店铺会员,StoreMember
	 * @param addrid 收货地址Id,Integer
	 * @return 返回json串
	 * result 	为1表示调用成功0表示失败
	 */
	@ResponseBody
	@RequestMapping(value="set-def-address")
	public JsonResult setDefAddress(Integer addrid){
		try {
			StoreMember member=storeMemberManager.getStoreMember();
			if(member==null){
				throw new TemplateModelException("未登陆不能使用此标签[ConsigneeListTag]");
			}
			this.storeMemberAddressManager.updateMemberAddress(member.getMember_id(),addrid);
			return JsonResultUtil.getSuccessJson("设置成功");
		} catch (Exception e) {
			return JsonResultUtil.getErrorJson("修改失败");
		} 
	}

	/**
	 * 创建收货地址
	 * @param shipName 收货人名称
	 * @param shipTel 收货人电话
	 * @param shipMobile 收货人手机号
	 * @param province_id 收货——省Id
	 * @param city_id 收货——城市Id
	 * @param region_id 收货——区Id
	 * @param province 收货——省
	 * @param city 收货——城市
	 * @param region 收货——区
	 * @param shipAddr 详细地址
	 * @param shipZip 收货邮编
	 * @return 收货地址
	 */
	private MemberAddress createAddress() {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();

		MemberAddress address = new MemberAddress();

		String name = request.getParameter("shipName");
		address.setName(name);

		String tel = request.getParameter("shipTel");
		address.setTel(tel);

		String mobile = request.getParameter("shipMobile");
		address.setMobile(mobile);

		String province_id = request.getParameter("province_id");
		if (province_id != null) {
			address.setProvince_id(Integer.valueOf(province_id));
		}

		String city_id = request.getParameter("city_id");
		if (city_id != null) {
			address.setCity_id(Integer.valueOf(city_id));
		}

		String region_id = request.getParameter("region_id");
		if (region_id != null) {
			address.setRegion_id(Integer.valueOf(region_id));
		}

		String province = request.getParameter("province");
		address.setProvince(province);

		String city = request.getParameter("city");
		address.setCity(city);

		String region = request.getParameter("region");
		address.setRegion(region);

		String addr = request.getParameter("shipAddr");
		address.setAddr(addr);

		String zip = request.getParameter("shipZip");
		address.setZip(zip);

		String defaddr=request.getParameter("def_addr");
		if(defaddr!=null){
			address.setDef_addr(Integer.valueOf(defaddr));
		}
		return address;
	}
	
}
