/**
 * Created by Andste on 2016/6/29.
 */
$(function(){
    var ue = UE.getEditor('editor');
    UE.Editor.prototype._bkGetActionUrl = UE.Editor.prototype.getActionUrl;
    UE.Editor.prototype.getActionUrl = function(action) {
        if (action == 'uploadimage' || action == 'uploadscrawl' || action == 'uploadimage') {
            return ctx+"/api/base/upload-image/upload-img.do";
        } else {
            return this._bkGetActionUrl.call(this, action);
        };
    };

    //  送现金、送积分
    (function(){
        var check = $('.promotions-style').find('.promotions-style-c_p');
        check.unbind('click').on('click', function(){
            var _this = $(this), _input = _this.parent().find('.form-control'), _f_span = _this.parent().find('.first-span'), _l_span = _this.parent().find('.last-span');
            if(_this.is(':checked')){
                _input.css({display: 'inline-block'});
                _f_span.html(_this.attr('title').substring(0, 1));
                _l_span.css({display: 'inline-block'});
            }else {
                _input.css({display: 'none'}).val('');
                _f_span.html(_this.attr('title'));
                _l_span.css({display: 'none'});
            };
        });
    })();

    //  送赠品、送优惠劵
    (function(){
        var check = $('.promotions-style').find('.promotions-style-g_b');
        check.unbind('click').on('click', function(){
            var _this = $(this), _select = _this.parent().find('.promotions-style-select'), _f_span = _this.parent().find('.first-span');
            if(_this.is(':checked')){
                _select.css({display: 'inline-block'});
                _f_span.html(_this.attr('title').substring(0, 1));
            }else {
                _select.css({display: 'none'}).find('select').val('0');
                _f_span.html(_this.attr('title'));
            };
        });
    })();

    //  新增赠品、优惠劵dialog
    (function(){
        var btn = $('.add-gift-promotions, .add-bonus-promotions');
        btn.unbind('click').on('click', function(){
            var _this = $(this), tag = _this.attr('tag');
            $.confirm('此操作会丢失页面现有表单数据！', function(){
                if(tag == 'gift'){
                    location.href = './gift.html';
                }else if(tag == 'bonus'){
                    location.href = './coupon.html';
                };
            });
        });
    })();

    //  全部、部分参加活动商品
    (function(){
        var check = $('#all_goods, #part_goods');
        check.unbind('click').on('click', function(){
            var _this = $(this), goods_box = $('.promotions-goods-box'), tag = _this.attr('tag');
            if(tag == 'all'){
                goods_box.css({display: 'none'});
                $('body,html').animate({ scrollTop: 0 });
            }else if(tag == 'part'){
                goods_box.css({display: 'block'});
                $('body,html').animate({ scrollTop: $('#join_box').offset().top - 5 });
            }

        });
    })();
    
    //  全部、部分参加活动商品页面显示
    (function(){
    	//如果全部商品参与单选框为选中状态，就将商品的tab页隐藏
    	if($("#all_goods").is(":checked")){
    		$(".promotions-goods-box").css('display','none');
    	}
    	
    	//如果部分商品参与单选框为选中状态，就将商品的tab页显示
    	if($("#part_goods").is(":checked")){
    		$(".promotions-goods-box").css('display','block');
    	}
    })();
    
    //  选择商品和已选商品选项卡点击操作
    (function(){
    	//已选商品选项卡点击操作
    	$(".partList").click(function(){
    		$(".promotions-goods-box .pngFix li").removeClass("active");
    		$(".promotions-goods-box .pngFix li").addClass("normal");
    		$(this).removeClass("normal");
    		$(this).addClass("active");
    		$(".promotions-goods-box .goodsAll .part").show();
    		$(".promotions-goods-box .goodsAll .all").hide();
    	});
    	
    	//选择商品选项卡点击操作
    	$(".allList").click(function(){
    		$(".promotions-goods-box .partList").removeClass("active");
    		$(".promotions-goods-box .pngFix li").addClass("normal");
    		$(this).removeClass("normal");
    		$(this).addClass("active");
    		$(".promotions-goods-box .goodsAll .part").hide();
    		$(".promotions-goods-box .goodsAll .all").show();
    	});
    	
    })();
    
    //  添加活动商品和取消活动商品点击操作
    (function(){
    	//添加参加活动商品
    	$(".goods-table .addGoods").click(function(){
    		var g_id = "tr_" + $(this).attr("goodsId");
    		$(this).css('display','none');
    		$("."+g_id+" .cancelGoods").css('display','block');
    		$("."+g_id).clone(true).appendTo(".selected .goods-table tbody");
    		$(".selected .goods-table tbody ."+g_id+" .goodsid").attr("name","goods_id");
    		$(".selected .no_blank").css('display','none');
    	});
    	
    	//取消参加活动商品
    	$(".goods-table .cancelGoods").click(function(){
    		var g_id = "tr_" + $(this).attr("goodsId");
    		$(".goods-table tbody ."+g_id+" .cancelGoods").css('display','none');
    		$("."+g_id+" .addGoods").css('display','block');
    		$(".selected .goods-table tbody ."+g_id).remove();
    		
    		var len = $(".selected .goods-table tbody tr").length;
    		if(len == 0){
    			$(".selected .no_blank").css('display','block');
    		}
    	});
    	
    })();
    
    //  选择商品列表异步分页点击操作
    (function(){
        $(".all").on("click",".unselected",function(){
        	var page = $(this).attr("page");
    		$.ajax({
    			url:"promotions_select_goods.html?page="+page,
    			dataType:"html",
    			success:function(result){
    				$(".goodsAll .all").html(result);
    				
    				selectedCancel();
    				
    				addCancelGoods();
    			},
    			error:function(){
    				$.alert("出错了:(");
    			}
    		});
        });
    })();
    
    //  选择商品列表异步搜索点击操作
    (function(){
    	$(".all").on("click","#btnSearch",function(){
			var keyword = $("input[name='keyword']").val();
			$.ajax({
				url:"promotions_select_goods.html?keyword="+keyword,
				dataType:"html",
				success:function(result){
					$(".goodsAll .all").html(result);
					
					selectedCancel();
    				
    				addCancelGoods();
				},
				error:function(){
					$.alert("出错了:(");
				}
			});	
		});
    })();
    
    //编辑促销活动页面第一次加载操作
    (function(){
    	//如果优惠方式的减现金复选框是选中状态
    	if($("input[name='is_full_minus']").is(":checked")){
    		var _this = $("input[name='is_full_minus']"), _input = _this.parent().find('.form-control'), _f_span = _this.parent().find('.first-span'), _l_span = _this.parent().find('.last-span');
    		_input.css({display: 'inline-block'});
            _f_span.html(_this.attr('title').substring(0, 1));
            _l_span.css({display: 'inline-block'});
    	}
    	
    	//如果优惠方式的送积分复选框是选中状态
    	if($("input[name='is_send_point']").is(":checked")){
    		var _this = $("input[name='is_send_point']"), _input = _this.parent().find('.form-control'), _f_span = _this.parent().find('.first-span'), _l_span = _this.parent().find('.last-span');
    		_input.css({display: 'inline-block'});
            _f_span.html(_this.attr('title').substring(0, 1));
            _l_span.css({display: 'inline-block'});
    	}
    	
    	//如果优惠方式的送赠品复选框是选中状态
    	if($("input[name='is_send_gift']").is(":checked")){
    		var _this = $("input[name='is_send_gift']"), _select = _this.parent().find('.promotions-style-select'), _f_span = _this.parent().find('.first-span');
            if(_this.is(':checked')){
                _select.css({display: 'inline-block'});
                _f_span.html(_this.attr('title').substring(0, 1));
            }
    	}
    	
    	//如果优惠方式的送优惠券复选框是选中状态
    	if($("input[name='is_send_bonus']").is(":checked")){
    		var _this = $("input[name='is_send_bonus']"), _select = _this.parent().find('.promotions-style-select'), _f_span = _this.parent().find('.first-span');
            if(_this.is(':checked')){
                _select.css({display: 'inline-block'});
                _f_span.html(_this.attr('title').substring(0, 1));
            }
    	}
    })();

	/**
	 * 遍历已选商品页面的取消参加活动商品事件绑定
	 */
	function selectedCancel(){
		$(".selected .goods-table .cancelGoods").each(function(){
			var selected = $(this).attr("goodsId");
			if(selected != ""){
				addCancel(selected);
			}
		});
	}

	/**
	 * 遍历选择商品页面取消参加活动商品事件绑定
	 * @param selected
	 */
	function addCancel(selected){
		$(".select .goods-table .cancelGoods").each(function(){
			var select = $(this).attr("goodsId");
			if(selected == select){
				$(this).css('display','block');
				$(".select .goods-table  .tr_"+select+" .addGoods").css('display','none');
			}
		});
	}

	/**
	 * 新增促销活动页面：添加活动商品和取消活动商品点击事件绑定
	 */
	function addCancelGoods(){
		//添加参加活动商品
		$(".goods-table .addGoods").click(function(){
			var g_id = "tr_" + $(this).attr("goodsId");
			$(this).css('display','none');
			$("."+g_id+" .cancelGoods").css('display','block');
			$("."+g_id).clone(true).appendTo(".selected .goods-table tbody");
			$(".selected .goods-table tbody ."+g_id+" .goodsid").attr("name","goods_id");
			$(".selected .no_blank").css('display','none');
		});

		//取消参加活动商品
		$(".goods-table .cancelGoods").click(function(){
			var g_id = "tr_" + $(this).attr("goodsId");
			$(".goods-table tbody ."+g_id+" .cancelGoods").css('display','none');
			$("."+g_id+" .addGoods").css('display','block');
			$(".selected .goods-table tbody ."+g_id).remove();

			var len = $(".selected .goods-table tbody tr").length;
			if(len == 0){
				$(".selected .no_blank").css('display','block');
			}
		});
	}

	//  修复一些样式问题
	(function(){
		if(Sys.ie){
			$('.promotions-join').css({marginLeft: '100px'});
		};

		if(Sys.ie < 9){
			$('.first-span').css({float: 'none'});
			$('.last-span').css({float: 'none'});
			$('.promotions-style .controls').css({
				float: 'none',
				marginLeft: '95px'
			});
			$('.promotions-style span').css({marginLeft: 0});
		};
	})();

});


