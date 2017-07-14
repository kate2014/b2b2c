//热卖排行榜
$(function(){
	//验证排序格式
	$("input[name='ordernum']").blur(function(){
		var _this = $(this), val= /^(([1-9]\d*)|\d)(\.\d{1,2})?$/.test($(this).val());
		if(val==false){
			$.message.error("排序格式不合格");
			if(_this.is('.form-control')){
				_this.parent().addClass('has-error error');
			}else {
				_this.addClass("border_erro");
			};
		}else if($(this).val() > 999999999){
			$.message.error("您输入的数过大！");
			if(_this.is('.form-control')){
				_this.parent().addClass('has-error error');
			}else {
				_this.addClass("border_erro");
			};
		}else {
			if(_this.is('.form-control')){
				_this.parent().removeClass('has-error error');
			}else {
				_this.removeClass("border_erro");
			};
		};
	});
	//保存排序功能
	$("#saveSort").click(function(){
		var is_true=true;
		var orderNumList = $("input[name='ordernum']");
		if (orderNumList.length === 0) {
			//nothing to save
			return false;
		}
		$.each(orderNumList, function(i, value) {
			var val = $(this).val();
			if(!/^(([1-9]\d*)|\d)(\.\d{1,2})?$/.test(val)){
				$.message.error("排序格式不合格");
				$(this).focus();
				is_true=false;
				return false;
			}
			
		});
		if(is_true){
			//判断按钮已经禁用
			if($("#saveSort").attr("is_disabled")==null||$("#saveSort").attr("is_disabled")=="false"){
				$("#saveSort").attr("is_disabled","true");
				var options = {
					url :ctx + "/api/b2b2c/store-goods-tag/save-sort.do",
					type : "POST",
					dataType : 'json',
					success : function(result) {
						if(result.result==1){
							$.message.success('保存成功！', 'reload');
						}else {
							$.message.error(result.message);
						};
					},
					error : function(e) {
						$.message.error("出现错误，请重试！");
						$("#saveSort").attr("is_disabled","false");
					}
				};
				$("#goodsTagForm").ajaxSubmit(options);
			} 
		}
	});
	$(".ordernum").blur(function(){
		var reg_id= $(this).attr("reg_id");
		if($(this).val()!=$("#order_num_"+reg_id).val()){
			$(this).attr("name","ordernum");
		};
	});

	//  全选功能
    (function(){
        var checkAllBtn = $('.check-all'), checkBox = $('.checkbox');
        checkAllBtn.unbind('click').on('click', function(){
            var _this = $(this);
            if(_this.is(':checked')){
                checkBox.prop('checked', true);
                showAllDown('show');
            }else {
                checkBox.removeAttr('checked');
                showAllDown('hide');
            };
        });
    })();
    //  手动选择功能
    (function(){
        var checkBox = $('.checkbox'), checkBoxLen = checkBox.length;
        checkBox.unbind('click').on('click', function(){
            var _this = $(this);
            if(_this.is(':checked')){
                _this.removeAttr('checked');
            }else {
                _this.prop('checked', true);
            };
            f();
        });
       
        checkBox.unbind('click').on('click', function(){
            var _this = $(this), c = _this.find('.checkbox');
            if(c.is(':checked')){
                c.removeAttr('checked');
            }else {
                c.prop('checked', true);
            };

            f();
        });

        function f(){
            var temp = 0;
            for(var i in checkBox){
                if(checkBox.eq(i).is(':checked')){
                    temp++;
                };
            };
            m(temp);
        };

        function m(temp){
            if(temp == checkBoxLen && temp != 0){
                $('.check-all').prop('checked', true);
            }else {
                $('.check-all').removeAttr('checked');
            };
            if(temp >0){
                showAllDown('show');
            }else {
                showAllDown('hide');
            };
        };

    })();
        //  显示、隐藏【批量删除】按钮
	    function showAllDown(str){
	        var  delAllBtn = $('.batch_del');
	        if(str == 'show'){
	        	delAllBtn.addClass('show');
	            if(Sys.ie < 10){
	            	delAllBtn.animate({marginLeft: 5}, 300);
	            };
	        }else {
	        	delAllBtn.removeClass('show');
	            if(Sys.ie < 10){
	            	delAllBtn.animate({marginLeft: -90}, 300);
	            };
	        };
	    };
    
	//批量删除功能
	 (function(){
	        var delAllBtn = $('#delete');
			
	        delAllBtn.unbind('click').on('click', function(){
	            var _this = $(this);
		        var del_num = $("input[name='reg_id']:checked").length;
	            if(del_num == 0){
					$.message.error("最少选中一个");
					return false;
				}
	            $.confirm('确定要删除这些商品吗？', function(){
	            	delAllGoods();
	            });
	        });
	       
	        function delAllGoods(){
	            var options = {
	                url : ctx + "/api/b2b2c/store-goods-tag/delete-rel.do?",
	                type : "POST",
	                dataType : 'json',
	                success : function(result) {
	                    if(result.result == 1){
							$.message.success('删除成功！', 'reload');
	                    }else {
	                        $.message.error(result.message);
	                    };
	                },
	                error : function(e) {
	                    $.message.error("出现错误 ，请重试");
	                }
	            };
	            $('#goodsTagForm').ajaxSubmit(options);
	        };
	    })();

	 
	//单个删除功能
	(function(){
        var deleteBtn = $('.delete_tags');
        deleteBtn.unbind('click').on('click', function(){
            var _this = $(this), reg_id = _this.attr('reg_id'),tagId = _this.attr('tagid');
            $.confirm('确定要删除吗？', function () {
				deleteTags();
            });

            function deleteTags(){
                $.ajax({
                    url : ctx + '/api/b2b2c/store-goods-tag/delete-rel.do?tagId='+ tagId +'&reg_id='+reg_id,
                    cache : false,
                    dataType : 'json',
                    success : function(result) {
                        if(result.result==1){
							$.message.success('删除成功！', 'reload');
                        }else {
                            $.message.error(result.message);
                        };
                    },
                    error : function() {
                        $.message.error("出现错误，请重试");
                    }
                });
            };
        });
    }());
	
	//添加 商品 功能
	$("#add").click(function(){
		var mark=$(this).attr("mark");
		var tagid=$(this).attr("tagid");
		location.href="selling.html?type=tag&mark="+mark+"&tagId="+tagid+"&market_enable=1";
	});
});