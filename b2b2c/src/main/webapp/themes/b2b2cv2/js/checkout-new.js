/**
 * Created by Andste on 2016/8/10.
 */

var Ckt = {};
$(function(){
	/* 初始化方法
	 ============================================================================ */
	(function(){
		//  收货人信息事件绑定
		infoEvent();

		//  支付方式事件绑定
		payEvent();

		//  配送清单事件绑定
		inventoryEvent();

		//  发票信息事件绑定
		invoiceEvent();

		//  送货时间事件绑定
		timeEvent();
	})()

	/* 收货人信息事件绑定
	 ============================================================================ */
	function infoEvent(){
		var info = $('.center-ckt-info');

		//  新增收货地址
		$('.c-new-address').on('click', function(){
			Ckt.info.a_InfoAdd();
		});

		//  收货人信息设置默认
		info.on('click', '.set', function(){
			var _this = $(this);
			Ckt.info.s_InfoSetDefAdd(_this);
		});

		//  收货人信息编辑
		info.on('click', '.edit', function(){
			var _this = $(this);
			Ckt.info.e_InfoEdit(_this);
		});

		//  收货人信息删除
		info.on('click', '.delete', function(){
			var _this = $(this);
			Ckt.info.d_InfoItem(_this);
		});

		//  收货人信息选中
		info.on('click', '.ckt-checkbox.info', function(){
			var _this = $(this);
			var _state = _this.is('.selected');
			if(!_state){
				Ckt.info.s_InfoItem(_this)
			}
		});

		//  地区列表项鼠标移入移出
		info.on('mouseover mouseout', '.li-ckt-info', function(event){
			var _this = $(this);
			Ckt.info.c_InfoItem(_this, event);
		});

		//  更多地址、收起地址
		$('.ckt-item.info').on('click', '.collapse-ckt-info', function(){
			var _this = $(this);
			Ckt.info.c_InfoCollapse(_this);
		});
	}

	/* 支付方式事件绑定
	 ============================================================================ */
	function payEvent(){
		var pay = $('.content-ckt-pay');

		//  支付方式选中
		pay.on('click', '.ckt-checkbox.pay', function(){
			var _this = $(this);
			Ckt.payMent.s_PayItem(_this);
		});

		//  更多方式、收起
		$('.collapse-ckt-pay').on('click', function(){
			var _this = $(this);
			Ckt.payMent.c_PayCollapse(_this);
		});

		//  如果支付方式大于6种 显示更多选项
		if($('.ul-ckt-pay').find('li').length > 5){
			$('.collapse-ckt-pay').css({display : 'block'});
		}
	}

	/* 配送清单事件绑定
	 ============================================================================ */
	function inventoryEvent(){
		var inventory = $('.content-ckt-inventory');
		//  快递方式选中
		inventory.on('click', '.ckt-checkbox.express', function(){
			var _this = $(this);
			Ckt.inventory.s_express(_this);
		});

		//  优惠劵鼠标移入移出效果
		inventory.on('mouseover mouseout', '.item-discount-inventory', function(event){
			var _this = $(this);
			Ckt.inventory.c_discount(_this, event);
		});

		//  优惠劵鼠选择
		inventory.on('click', '.item-discount-inventory', function(){
			var _this = $(this);
			Ckt.inventory.s_discount(_this);
		});

		//  动态设置配送清单高度
		var items = $('.item-ckt-inventory');
		for(var i = 0, len = items.length; i < len; i++) {
			var _thisItem   = items.eq(i),
				leftHeight  = _thisItem.find('.left-item-inventory').height(),
				rightHright = _thisItem.find('.right-item-inventory').height();
			if(!leftHeight || !rightHright){
				return
			}
			leftHeight > rightHright ? (function(){
				_thisItem.find('.right-item-inventory').css('height', leftHeight)
			}()) : (function(){
				_thisItem.find('.left-item-inventory').css('height', rightHright)
			}());
		}
	}

	/* 发票信息事件绑定
	 ============================================================================ */
	function invoiceEvent(){
		var invoice = $('.content-ckt.invoice');

		//  发票信息编辑
		$('.edit-invoice').on('click', function(){
			Ckt.invoice.e_invoice();
		});

		//  发票抬头选择
		$('#dialogModal').on('click', '.ckt-checkbox.head-invoice', function(){
			var _this = $(this);
			Ckt.invoice.s_invoiceHead(_this);
		});

		//  发票内容选择
		$('#dialogModal').on('click', '.ckt-checkbox.content-invoice', function(){
			var _this = $(this);
			Ckt.invoice.s_invoiceContent(_this);
		});
	}

	/* 送货时间事件绑定
	 ============================================================================ */
	function timeEvent(){
		var time = $('.content-ckt.time');
		time.on('click', '.ckt-checkbox', function(){
			var _this = $(this);
			Ckt.time.e_time(_this);
		});
	}

	/* 收货人信息逻辑处理
	 ============================================================================ */
	Ckt.info = (function(){
		var info = $('.center-ckt-info');

		//  收货人信息删除
		function deleteInfo(_this){
			var _this      = _this,
				address_id = _this.attr('address_id');
			if(typeof $.confirm == 'function'){
				$.confirm('确认要删除吗？', function(){
					// ...
					delAddress();
				});
			}else {
				if(confirm('确认要删除吗？')){
					// ...
					delAddress();
				}
			}

			function delAddress(){
				$.loading();
				$.ajax({
					url     : ctx + '/api/shop/member-address/delete.do?addr_id=' + address_id,
					type    : 'POST',
					success : function(result){

						if(result && typeof result == 'string'){
							result = JSON.parse(result);
						}
						if(result.result == 1){
							$.closeLoading(function(){
								initAddressList();
								$.message.success('删除成功！');
							});
						}else {
							$.message.error(result.message);
						}
						$.closeLoading();
					},
					error   : function(){
						$.message.error('出现错误，请重试！');
					}
				});
			}
		}

		//  收货人信息选择
		function selectInfo(_this, _global){
			var _this   = _this,
				_global = _global === false ? false : true;
			var address_id = _this.closest('.li-ckt-info').attr('address_id');
			if(_global != false){
				$.loading();
			}
			$.ajax({
				url     : ctx + '/api/store/store-order/change-address.do?address_id=' + address_id,
				type    : 'POST',
				global  : _global,
				success : function(result){
					if(result && typeof result == 'string'){
						result = JSON.parse(result);
					}
					if(result.result == 1){
						$.closeLoading(function(){
							_callBack(result);
						});
					}else {
						$.message.error(result.message);
					}
				},
				error   : function(){
					$.message.error('出现错误，请重试！');
				}
			})
			$.closeLoading();

			//  选择收货人信息后回调
			function _callBack(result){
				var data = result.data;
				var regionid=$('#regionid');
				for(var i in data) {
					var _data  = data[i],
						_store = $('#store_' + _data.store_id);
					//处理region_id
					regionid.val(_data.region_id);
					//  回调数据处理
					var _express    = '',
						_shiplist   = _data.shiplist,
						_storeprice = _data.storeprice;

					for(var j in _shiplist) {
						if(j == 0){
							_express += '<div class="ckt-checkbox express selected" type_id="' + _shiplist[j].type_id + '"><span>' + _shiplist[j].name + '&nbsp;（' + _shiplist[j].shipPrice + '&nbsp;元）</span></div>'
						}else {
							_express += '<div class="ckt-checkbox express" type_id="' + _shiplist[j].type_id + '"><span>' + _shiplist[j].name + '&nbsp;（' + _shiplist[j].shipPrice + '&nbsp;元）</span></div>'
						}
					}
					//  更新快递方式
					_store.find('.content-item-inventory.express').html(_express);
					//  更新折扣、小计
					_store.find('.discount-store-total').find('span').html('￥' + (_storeprice.discountPrice).toFixed(2));
					_store.find('.total-store-total').find('span').html('￥' + (_storeprice.needPayMoney).toFixed(2));
					//  去除优惠劵选中状态
					_store.find('.item-discount-inventory').removeClass('selected').find('.select-discount').removeClass('show');
				}

				$('.ckt-checkbox.info').removeClass('selected');
				_this.addClass('selected');
				_this.closest('.li-ckt-info').addClass('selected')
					 .siblings().removeClass('selected');

				Ckt.public.refreshTotal();
				Ckt.public.addressTotal();
			}
		}

		//  地区鼠标移入
		function curInfo(_this, _event){
			var _this = _this,
				_type = _event.type;
			var InfoInfo = _this.find('.address-li-ckt-info'),
				title    = InfoInfo.attr('data-title'),
				html     = InfoInfo.html(),
				oper     = _this.find('.operate-li-ckt-info');
			if(_type == 'mouseover'){
				//  InfoInfo.addClass('min');
				oper.addClass('show');
				if(html && html.length > 29){
					InfoInfo.html(html.substring(0, 29) + '...');
				}
			}else if(_type == 'mouseout'){
				//  InfoInfo.removeClass('min');
				oper.removeClass('show');
				InfoInfo.html(title);
			}
		}

		//  更多地址、收起地址
		function collInfo(_this){
			var _this = _this;
			var state = _this.is('.more'),
				liLen = info.find('.li-ckt-info').length;
			if(state){
				//  收起
				_this.removeClass('more')
					 .find('span').html('更多地址');
				_this.find('i').removeClass('more');
				info.css({height : 42});
				Ckt.public.insertEletTo($('.li-ckt-info.selected'))
			}else {
				//  更多
				_this.addClass('more')
					 .find('span').html('收起地址');
				_this.find('i').addClass('more');
                liLen > 3 ? info.css({height : 36 * 4}) : info.css({height : 36 * liLen});
			}
		}

		//  新增收货人信息【没有地址时】
		function newInfo(){
			$.ajax({
				url     : ctx + '/checkout/address-add.html',
				type    : 'GET',
				success : function(html){
					var _api = '/api/shop/member-address/add-new-address.do';
					$.dialogModal({
						title    : '新增收货人信息',
						html     : html,
						top      : 100,
						btn      : false,
						backdrop : false,
						showCall : function(){
							$('#dialogModal').find('.close').css('display', 'none');
							$('#dialogModal').find('.modal-body').css({overflow : 'scroll'});
							return showDialog(_api);
						}
					})
				}
			})
		}

		//  新增收货人信息【有地址时】
		function addInfo(){
			$.ajax({
				url     : ctx + '/checkout/address-add.html',
				type    : 'GET',
				success : function(html){
					var _api = '/api/shop/member-address/add-new-address.do';
					$.dialogModal({
						title    : '新增收货人信息',
						html     : html,
						top      : 100,
						btn      : false,
						showCall : function(){
							$('#dialogModal').find('.modal-body').css({overflow : 'scroll'});
							return showDialog(_api);
						}
					})
				}
			})
		}

		//  编辑收货人信息
		function editInfo(_this){
			var address_id = _this.attr('address_id');
			$.ajax({
				url     : ctx + '/checkout/address-edit.html?addressid=' + address_id,
				type    : 'GET',
				success : function(html){
					var _api = '/api/shop/member-address/edit.do?addr_id=' + address_id;
					$.dialogModal({
						title    : '修改收货人信息',
						html     : html,
						top      : 100,
						btn      : false,
						showCall : function(){
							$('#dialogModal').find('.modal-body').css({overflow : 'scroll'});
							return showDialog(_api);
						}
					})
				}

			})
		}

		//  新增、修改地址dialog逻辑
		function showDialog(_api){
			var app  = $('#dialogModal').find('.address_div'),
				_api = _api;
			var name            = app.find("input[name='name']"),
				addr            = app.find("input[name='addr']"),
				mobile          = app.find("input[name='mobile']"),
				shipAddressName = app.find("input[name='shipAddressName']");

			var inputs = app.find('.form-control');

			//  别名
			app.find('.example-aliases').on('click', function(){
				var _val = $(this).html();
				shipAddressName.val(_val);
			});

			inputs.on('input propertychange blur', function(){
				var _this = $(this),
					_name = _this.attr('name'),
					_val  = _this.val();
				if(_name == 'mobile' || _name == 'shipMobile'){
					testMobile(_val) ? _this.parent().removeClass('has-error error') : _this.parent().addClass('has-error error');
				}else if(_name != 'shipAddressName'){
					_val ? _this.parent().removeClass('has-error error') : _this.parent().addClass('has-error error');
				}
			});

			//  保存地址
			$('.add-btn').on('click', function(){
				sendAjax($(this));
			});

			function sendAjax(_this){
				var _this = _this;
				for(var i = 0, len = inputs.length; i < len; i++) {
					var __this = inputs.eq(i),
						_val   = __this.val();
					if(!_val && __this.attr('name') != 'shipAddressName'){
						__this.parent().addClass('has-error error');
						if(i == len){
							return
						}
					}
				}

				if(app.find('.error').length > 0){
					$.message.error('请核对表单填写是否有误！');
					return false;
				}

				/* 对地址信息进行校验
				 ============================================================================ */
				var aLen     = $('.app-address-tab').find('a').length,
					_inputLen = 0,
					_input   = $('.app-address').find("input[type='hidden']");
				for(var i = 0, len = _input.length; i < len; i ++){
					if(_input.eq(i).val() != ''){
						_inputLen += 1;
					}
				}
				if(aLen != _inputLen / 2){
					$.message.error('请核对收货地址是否完整！');
					return false;
				}
				/* 对地址信息进行校验
				 ============================================================================ */
				_this.off('click');

				var options = {
					url     : ctx + _api,
					type    : 'POST',
					success : function(result){
						if(result && typeof result == 'string'){
							result = JSON.parse(result);
						}
						if(result.result == 1){
							$.closeLoading(function(){
								$.message.success('保存成功！');
								Ckt.public.addressTotal();
								initAddressList()
								$('#dialogModal').modal('hide').find('.close').css({display : 'block'});
							});
						}else {
							shipAddressName.attr('name', 'shipAddressName');
							$.message.error(result.message);
							_this.on('click', function(){
								sendAjax(_this);
							})
						}
					},
					error   : function(){
						shipAddressName.attr('name', 'shipAddressName');
						$.message.error('出现错误，请重试！');
						_this.on('click', function(){
							sendAjax(_this);
						})
					}

				};

				$.loading();
				app.find('#address_form').ajaxSubmit(options);
				$.closeLoading();
			}

			function testMobile(mobile){
				return /^0?(13[0-9]|15[0-9]|18[0-9]|14[0-9]|17[0-9])[0-9]{8}$/.test(mobile);
			}
		}

		//  显示、隐藏【更多地址】
		function initAddressList(str){
			$.ajaxSetup ({cache: false});
			if(str && str == 'setDef'){
				$('#address_list').empty().load(ctx + '/checkout/address-list.html', function(){
					Ckt.public.addressTotal();
					Ckt.public.initItems();
				});
				return
			}
			$('#address_list').empty().load(ctx + '/checkout/address-list.html', function(){
				var collapse = $('.collapse-ckt-info'),
					liCkt    = $('.li-ckt-info'),
					liCktLen = liCkt.length;
				if(liCktLen > 1){
					collapse.addClass('show more');
					collapse.find('.more-collapse-ckt').html('收起地址');
					collapse.find('.icon-collapse-ckt-info').addClass('more');
					var liLen = $('.li-ckt-info').length;
					liLen > 3 ? info.css({height : 36 * 4}) : info.css({height : 36 * liLen});
					Ckt.public.addressTotal();
				}else if(liCktLen == 0){
					collapse.removeClass('show');
					newInfo();
				}else {
					collapse.removeClass('show');
					Ckt.public.addressTotal();
				}
				Ckt.public.initItems();
			});
		}

		//  设置默认收货地址
		function setDefaultAddress(_this){
			var add_id = _this.attr('address_id');
			$.loading();
			$.ajax({
				url     : ctx + '/api/shop/member-address/set-def-address.do?addr_id=' + add_id,
				type    : 'POST',
				success : function(result){
					if(result && typeof result == 'string'){
						result = JSON.parse(result);
					}
					if(result.result == 1){
						$.closeLoading(function(){
							initAddressList('setDef')
							$.message.success('设置成功！');
						});
					}else {
						$.message.error(result.message);
					}
				},
				error   : function(){
					$.message.error('出现错误，穷重试！');
				}
			})
			$.closeLoading();
		}

		return {
			i_InfoInitAddress : function(){
				return initAddressList();
			},
			d_InfoItem        : function(_this){
				return deleteInfo(_this);
			},
			s_InfoItem        : function(_this, g){
				return selectInfo(_this, g);
			},
			c_InfoItem        : function(_this, _event){
				return curInfo(_this, _event);
			},
			c_InfoCollapse    : function(_this){
				return collInfo(_this);
			},
			a_InfoAdd         : function(){
				return addInfo();
			},
			e_InfoEdit        : function(_this){
				return editInfo(_this);
			},
			s_InfoSetDefAdd   : function(_this){
				return setDefaultAddress(_this);
			}
		}
	})();

	/* 支付方式逻辑处理
	 ============================================================================ */
	Ckt.payMent = (function(){
		var pay = $('.content-ckt-pay');

		function selectPay(_this){
			var _this     = _this,
				_state    = _this.is('.selected'),
				index     = _this.index(),
				ulPlay    = _this.parent(),
				payMentId = $('#payMentId');
			if(!_state){
				$('.ckt-checkbox.pay').removeClass('selected');
				_this.addClass('selected');
				payMentId.val(_this.attr('pay_ment_id'));
			}

			_coll($('.collapse-ckt-pay'));
			if(index > 4){
				ulPlay.css({width : 110 * 6});
				Ckt.public.insertEletTo(_this, $('.ckt-checkbox.pay'), 5);
			}else {
				ulPlay.css({width : ''});
			}
		}

		//  点击展开更多方式、收起
		function C_collPay(_this){
			var _this = _this;
			var _state  = _this.is('.more'),
				_payUl  = pay.find('.ul-ckt-pay'),
				_payLen = pay.find('.ckt-checkbox.pay').length;
			if(!_state){
				//  展开
				_this.addClass('more').find('span').html('收起')
				_this.find('i').addClass('more');
				if(_payLen < 5){
					return
				}else if(_payLen < 9){
					//  展开就行
					_payUl.css({width : _payLen * 110});
				}else {
					//  去掉宽度限制
					_payUl.removeClass('min');
					_this.find('i').removeClass('more');
					_this.find('i').addClass('coll');
				}
			}else {
				//  收起
				_coll(_this);
				if(_payLen < 5){
					return
				}else if(_payLen < 9){
					//  收起就行
					_payUl.css({width : ''});
				}else {
					//  加上宽度限制
					_payUl.addClass('min');
					_this.find('i').removeClass('coll');
				}
			}
		}

		//  收起体现
		function _coll(_this){
			var _this = _this;
			/*setTimeout(function(){*/
			_this.find('span').html('更多方式');
			_this.find('i').removeClass('more');
			_this.removeClass('more');
			/*}, 200);*/
		}

		return {
			s_PayItem     : function(_this){
				return selectPay(_this);
			},
			c_PayCollapse : function(_this){
				return C_collPay(_this);
			}
		}

	})();

	/* 配送清单逻辑处理
	 ============================================================================ */
	Ckt.inventory = (function(){
		var inventory = $('.content-ckt-inventory');

		//  快递方式选择
		function S_express(_this){
			var _this = _this;
			discountExpress(_this, 'express');
		}

		//  优惠劵鼠标移入移出效果
		function C_discount(_this, event){
			var _this = _this,
				_type = event.type;
			var detailItemDiscount = _this.find('.detail-item-discount');
			if(_type == 'mouseover'){
				//  鼠标移入
				detailItemDiscount.addClass('show');

			}else if(_type == 'mouseout'){
				//  鼠标移出
				detailItemDiscount.removeClass('show');
			}
		}

		//  优惠劵选择
		function S_discount(_this){
			var _this = _this;
			discountExpress(_this, 'discount');
		}

		//  优惠劵、快递方式选择
		function discountExpress(_this, _str){
			var _this    = _this,
				_str     = _str,
				_state   = _this.is('.selected'),
				_closest = _this.closest('.item-ckt-inventory');

			//  初始化数据
			var regionid = $('#regionid').val(),
				storeid  = '',
				typeid   = '',
				bonusid  = '',
				bonusStr = '',
				success  = null;

			var url;
			//  组织数据
			if(_str == 'express'){
				url = ctx + '/api/store/store-order/change-args-type.do';
				
				if(_state){
					return false
				}
				var _item = _closest.find("div[class='item-discount-inventory selected']");
				typeid = _this.attr('type_id');
				storeid = _this.closest('.item-ckt-inventory').attr('storeid');
				bonusid = _item.attr('bonusid') || 0;
				bonusStr = _item.attr('price') ? bonusid + '-' + _item.attr('price') : '0-0';

			}else {
				url = ctx + '/api/b2b2c/bonus/use-bonus.do';
				
				storeid = _this.closest('.item-ckt-inventory').attr('storeid');
				typeid = _this.closest('.item-ckt-inventory').find("div[class='ckt-checkbox express selected']").attr('type_id');
				bonusid = _this.attr('bonusid');
				bonusStr = bonusid + '-' + _this.attr('price');
				if(_state){
					bonusid = 0
					bonusStr = '0-0';
				}
			}

			//  成功数据处理回调
			if(_state){
				success = function(result){
					var storeprice = result.data.storeprice;
					if(_str == 'discount'){
						_this.removeClass('selected');
						_closest.find('.select-discount').removeClass('show');
					}
					_closest.find('.discount-store-total').find('span').html('￥0.00');
					_closest.find('.total-store-total').find('span').html('￥' + (storeprice.needPayMoney).toFixed(2));
				}
			}else {
				success = function(result){
					var storeprice = result.data.storeprice;
					_this.addClass('selected');
					_this.siblings().removeClass('selected');
					if(_str == 'discount'){
						_this.siblings().find('.select-discount').removeClass('show');
						_this.find('.select-discount').addClass('show');
					}
					_closest.find('.discount-store-total').find('span').html('￥' + (storeprice.discountPrice).toFixed(2));
					_closest.find('.total-store-total').find('span').html('￥' + (storeprice.needPayMoney).toFixed(2));
				}
			}

			var options = {
				url     : url,
				data    : {
					store_id : storeid,
					regionid : regionid,
					type_id  : typeid,
					bonus_id : bonusid
				},
				type    : 'POST',
				success : function(result){
					if(result && typeof result == 'string'){
						result = JSON.parse(result);
					}
					if(result.result == 1){
						$.closeLoading(function(){
							success(result);
							_closest.find("input[name='shippingId']").val(typeid);
							_closest.find("input[name='bonusid']").val(bonusStr);
							Ckt.public.refreshTotal();
						});
					}else {
						$.message.error(result.message);
					}
				},
				error   : function(){
					$.message.error('出现错误，请重试！');
				}
			}

			$.loading();
			$.ajax(options);
			$.closeLoading();
		}

		return {
			s_express  : function(_this){
				return S_express(_this);
			},
			c_discount : function(_this, event){
				return C_discount(_this, event);
			},
			s_discount : function(_this){
				return S_discount(_this);
			}
		}
	})();

	/* 发票信息逻辑处理
	 ============================================================================ */
	Ckt.invoice = (function(){

		//  修改发票信息
		function E_invoice(){
			var invoiceDialog = $('#invoice_dialog');
			invoiceDialog.dialogModal({
				title    : '发票信息',
				top      : 100,
				showCall : function(){
					$('.company-invoice-input').on('click', function(e){
						if($(this).closest('.head-invoice').is('.selected')){
							e.stopPropagation();
						}
					});
				},
				callBack : function(){
					var dialogModal = $('#dialogModal');
					if(dialogModal.find('.ckt-checkbox.head-invoice.selected').length < 1){
						$.message.error('请选择发票抬头！');
						return false;
					}else {
						if(dialogModal.find('.ckt-checkbox.head-invoice.company').is('.selected')){
							if(!dialogModal.find('.company-invoice-input').val()){
								$.message.error('请填写发票信息！');
								return false;
							}else {
								setTnvoice();
								return true;
							}
						}else {
							setTnvoice();
							return true;
						}
					}

					function setTnvoice(){
						var title    = '',
							comInput = dialogModal.find('.head-invoice.selected').find('.company-invoice-input');
						if(comInput.length > 0){
							title = comInput.val()
						}else {
							title = dialogModal.find('.head-invoice.selected').find('span').html();
						}

						$('.receipt-title').html(title);
						$('.receipt-content').html(dialogModal.find('.content-invoice.selected').find('span').html());
					}
				}
			});
		}

		//  发票抬头选择
		function S_invoiceHead(_this){
			var _this   = _this,
				_state  = _this.is('.selected'),
				company = _this.is('.company');
			var invoiceInputs  = $('#dialogModal').find('.invoice-inputs'),
				receipt        = invoiceInputs.find("input[name='receipt']"),
				receiptType    = invoiceInputs.find("input[name='receiptType']"),
				receiptTitle   = invoiceInputs.find("input[name='receiptTitle']"),
				receiptContent = invoiceInputs.find("input[name='receiptContent']");
			if(_state){
				_this.removeClass('selected');
				receipt.val(2);
				receiptType.val('');
				receiptTitle.val('');
				receiptContent.val('');
			}else {
				_this.addClass('selected')
					 .siblings().removeClass('selected');
				if(company){
					//  显示input，并焦点
					var _thisInput = _this.find('input');
					_thisInput.addClass('shows').focus();
					receipt.val(1);
					receiptType.val(2);
					_thisInput.on('input propertychange', function(){
						var _val = $(this).val();
						receiptTitle.val(_val)
					})
					if(!receiptContent.val()){
						receiptContent.val('办公用品')
					}
					//  设置input
				}else {
					//  设置input
					receipt.val(1);
					receiptType.val(1);
					receiptTitle.val('');
					_this.siblings().find('input').val('');
					if(!receiptContent.val()){
						receiptContent.val('办公用品')
					}
				}
			}
		}

		//  发票内容选择
		function S_invoiceContent(_this){
			var _this = _this;
			var receiptContent = $('#dialogModal').find('.invoice-inputs').find("input[name='receiptContent']");
			_this.addClass('selected')
				 .siblings().removeClass('selected');
			receiptContent.val(_this.find('span').html());
		}

		return {
			e_invoice        : function(){
				return E_invoice();
			},
			s_invoiceHead    : function(_this){
				return S_invoiceHead(_this);
			},
			s_invoiceContent : function(_this){
				return S_invoiceContent(_this);
			}
		}
	})();

	/* 送货时间逻辑处理
	 ============================================================================ */
	Ckt.time = (function(){
		//  修改送货时间
		function E_time(_this){
			var _this  = _this,
				_state = _this.is('.selected');
			if(!_state){
				_this.siblings('input').val(_this.find('span').html());
				_this.addClass('selected')
					 .siblings('.ckt-checkbox').removeClass('selected');
			}
		}

		return {
			e_time : function(_this){
				return E_time(_this);
			}
		}
	})();

	/* 公共方法
	 ============================================================================ */
	Ckt.public = (function(){
		//  将此元素放到第一位
		function insertEletTo(element, toEle, eq){
			var _this = element,
				toEle = toEle ? toEle : _this.siblings(),
				eq    = eq ? eq : 0;
			//  克隆
			var _clone = _this.clone(true);
			$(_clone).insertBefore(toEle.eq(eq));
			_this.remove();
            $('.center-ckt-info')[0].scrollTop = 0;
		}

		//  刷新金额统计
		function refreshTotal(){
			var total = $('.total-ckt-total');
			var credit = $("#isUseCredit");
			var  checked=false;
			if(typeof(credit)!="undefined"){
				checked= credit.prop("checked");
			}
			if(checked){
				total.load(ctx + '/checkout/checkout-total.html?isUseCredit=1');
			}else{
				total.load(ctx + '/checkout/checkout-total.html');
			}
		}

		//  设置底部地址信息
		function addressTotal(){
			var _selected = $('.li-ckt-info.selected');
			var address_info   = _selected.find('.address-li-ckt-info').attr('title'),
				address_name   = _selected.find('.name-li-ckt-info').attr('title'),
				address_mobile = _selected.find('.mobile-li-ckt-info').attr('title');
			$('#address-info').html(address_info);
			$('#address-name').html(address_name);
			$('#address-mobile').html(address_mobile);
			//  设置地址id
			$("input[name='addressId']").val($('.li-ckt-info.selected').attr('address_id'));
		}

		//  初始化地址信息选择、快递方式、优惠劵
		function initItems(){
			var infoList = $('.li-ckt-info');
			for(var i = 0, len = infoList.length; i < len; i++) {
				var _infoList = infoList.eq(i);
				if(_infoList.find('.default-li-ckt-info').length > 0){
					Ckt.info.s_InfoItem(_infoList.find('.ckt-checkbox.info'), false);
				}
			}
		}

		function initBonsid(data){
			var data  = data,
				total = $('.details-ckt-total');
			if(total.find('.li_discount_price').find('.price-details-ckt-total').html() !== '-￥0.00'){
				var stores = $('.ckt-item.inventory');
				for(var i = 0, len = stores.length; i < len; i++) {
					var _store   = stores.eq(i),
						_bonusid = data[i].bonusid;
					if(_store.find('.no-item-discount-inventory').length == 0){

					}
				}
			}
		}

		return {
			insertEletTo : function(ele, toEle, eq){
				return insertEletTo(ele, toEle, eq);
			},
			refreshTotal : function(){
				return refreshTotal();
			},
			addressTotal : function(){
				return addressTotal();
			},
			initItems    : function(){
				return initItems();
			},
			initBonsid   : function(data){
				return initBonsid(data);
			}
		}
	})();

	/* 创建订单逻辑
	 ============================================================================ */
	//  初始化数据
	initData();
	function initData(){

		//  支付方式
		$("input[name='paymentId']").val($('.ckt-checkbox.pay.selected').attr('pay_ment_id'));

		//  配送方式、优惠劵
		(function(){
			var items = $('.item-ckt-inventory');
			for(var i = 0, len = items.length; i < len; i++) {
				var _item          = items.eq(i),
					_shippingId    = _item.find("input[name='shippingId']"),
					_bonusid       = _item.find("input[name='bonusid']"),
					_shippingIdVal = _item.find('.ckt-checkbox.express.selected').attr('type_id'),
					_bonusidVal    = '';
				var _bon = _item.find('.item-discount-inventory.selected');
				_bon.length > 0 ? (_bonusidVal = _bon.attr('bonusid') + '-' + _bon.attr('price')) : (_bonusidVal = '0-0');
				_shippingId.val(_shippingIdVal);
				_bonusid.val(_bonusidVal);
			}
		})();
	};

	//  创建订单事件
	$('#bill_btn').on('click', function(){
		createOrder();
	});

	function createOrder(){
		if($('.ckt-checkbox.info.selected').length != 1){
			$.message.error('请选择收货人信息！');
			return false;
		}
		var options = {
			url     : ctx + '/api/store/store-order/create.do',
			type    : 'POST',
			dataType: 'JSON',
			success : function(result){
				if(result && typeof result == 'string'){
					result = JSON.parse(result)
				}
				if(result.result == 1){
					$.closeLoading(function(){
						location.href = ctx + '/order_create_success.html?orderid=' + result.data.order_id;
					});
				}else {
					$.message.error(result.message);
					$('#bill_btn').on('click', function(){
						createOrder();
					});
				}
			},
			error   : function(){
				$.message.error('出现错误，请重试！');
				$('#bill_btn').on('click', function(){
					createOrder();
				});
			}
		}

		$('#bill_btn').off('click');

		$.loading();
		$('#checkoutForm').ajaxSubmit(options);
		$.closeLoading();
	}
});