/**
 * Created by Andste on 2016/6/23.
 */
$(function(){

    //  优惠劵搜索
    (function(){
        var btn = $('.coupon-seach-btn'), time_s = GetQueryString('add_time_from'), time_e = GetQueryString('add_time_to');
        if(time_s){
            $('#time_start').val(time_s);
        };
        if(time_e){
            $('#time_end').val(time_e);
        };
        btn.unbind('click').on('click', function(){
            var  time_start = $('#time_start').val(), time_end = $('#time_end').val();
            if(!time_start && !time_end){
                location.href = './coupon.html';
            }else if(time_end && time_start > time_end){
                $.message.error("开始时间不能大于结束时间");
                return false;
            }else if(!time_start && time_end) {
                $.message.error('请选择开始日期！');
                return false;
            }else if(time_start && !time_end){
                $.message.error('请选择结束日期！');
                return false;
            }else {
                location.href = './coupon.html?add_time_from='+ time_start +'&add_time_to=' + time_end;
            };
        });
    })();

    //  删除优惠劵
    (function(){
        var btn = $('.coupon-delete');
        btn.unbind('click').on('click', function(){
            var _this = $(this), coupon_id = _this.attr('coupon_id');
            $.confirm('确定要删除此优惠劵吗？', function(){
                $.ajax({
                    url : ctx + '/api/b2b2c/bonus/delete-bonus.do?type_id='+ coupon_id,
                    cache : false,
                    dataType: "json",
                    success : function(data) {
                        if(data.result==1){
                            $.message.success('删除成功！', 'reload');
                        }else{
                            $.message.error(data.message);
                        }
                    },
                    error : function() {
                        $.message.error('出现错误，请重试！');
                    }
                });
            });
        });
    })();

    //  新增、修改优惠劵
    (function(){
        var btn = $('.coupon-add, .coupon-edit');
        btn.unbind('click').on('click', function(){
            var _this = $(this), coupon_id = _this.attr('coupon_id'), url = '', title = '';
            if(coupon_id){
                url = './coupon_edit.html?bonusid=' + coupon_id;
                title = '修改优惠劵';
            }else {
                url = './coupon_add.html';
                title = '新增优惠劵';
            };

            $.ajax({
                url: url,
                type: 'GET',
                success: function(html){
                    $.dialogModal({
                        title: title,
                        html : html,
                        showCall: function(){
                            $('#coupon_form input').blur(function(){
                                checkCounpon($(this));
                            });
                            $('#limit_num').blur(function(){
                                checkLimit();
                            });
                        },
                        callBack: function(){
                            var input = $('#coupon_form input'), inputLen = input.length;
                            for(var i = 0; i < inputLen; i ++){
                                checkCounpon(input.eq(i));
                            };
                            checkLimit();

                            if($('#coupon_form').find('.error').length > 0){
                                $.message.error('表单填写有误，请检查！');
                                return false;
                            }else {
                                var options = {
                                    url : ctx + '/api/b2b2c/bonus/'+ $('.coupon-type').val()+'-full-subtract.do',
                                    type: 'POST',
                                    dataType: 'json',
                                    success: function(result){
                                        if(result.result == 1){
                                            $.message.success('保存成功！', 'reload');
                                        }else {
                                            $.message.error(result.message);
                                            return false;
                                        };
                                    },
                                    error: function(){
                                        $.message.error('出现错误，请重试！');
                                        return false;
                                    }
                                };
                                $('#coupon_form').ajaxSubmit(options);
                            };
                        }
                    });
                },
                error: function(){
                    $.message.error('出现错误，请重试！');
                }
            });

        });
    })();

    /* 新增、修改优惠劵校验
     ============================================================================ */
    function checkCounpon(_this){
        var _this = _this, val = $.trim(_this.val());
        if(!val){
            err(_this, 'error');
        }else if(_this.is('.type_money')){
            if(_this.val() == '0'){
                err(_this, 'error');
                $.message.error('优惠面额不能为0！');
            }else {
                err(_this, 'succe');
            };
        }else if(_this.is('.min_goods_amount')){
            if(_this.val() == '0'){
                err(_this, 'error');
                $.message.error('需消费金额不能为0！');
            }else {
                err(_this, 'succe');
            };
        }else {
            err(_this, 'succe');
        };
        //  校验日期
        var s = $('.dialog-time-start').val(), e = $('.dialog-time-end').val();
        if(s && e){
            var node = $('.dialog-time-start, .dialog-time-end');
            if(s > e){
                err(node, 'error');
                $.message.error('开始日期不得大于结束日期！');
            }else {
                err(node, 'succe');
            };
        };
    };

    function checkLimit(){
        var create_num = $('#create_num').val(), limit_num = $('#limit_num').val(), limit = $('#limit_num');
        if(create_num && limit_num){
            if(parseInt(create_num) < parseInt(limit_num)){
                err(limit, 'error');
                if(limit.parent().find('.limit-error-span').length == 0){
                    $('<span class="limit-error-span" style="color: #a94442; font-size: 12px; ">个人领取量不能大于发行量！</span>').appendTo(limit.parent());
                }else {
                    $('.limit-error-span').html('个人领取量不能大于发行量！');
                };
            }else {
                err(limit, 'succe');
                $('.limit-error-span').html('');
            };
        };
    };

    function err(node, str){
        if(str == 'error'){
            node.parent().addClass('has-error error');
        }else {
            node.parent().removeClass('has-error error');
        };
    };
});