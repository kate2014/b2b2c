/**
 * Created by Andste on 2016/6/20.
 */
$(function(){
    //  审核通过与否
    (function(){
        $('.check-btn').unbind('click').on('click', function(){
            var _this = $(this), status = _this.attr('status');
            $.confirm('确定' + _this.html() + '吗？', function(){
                $('#status').val(status);
                var options = {
                    url: ctx + '/api/store/store-sell-back/auth-refund.do',
                    type: 'POST',
                    success: function(result){
                        if(result.result == 1){
                            $.message.success(_this.html() + '成功！请到退货、款单页面进行下一步操作。', 'reload');
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
                $('#theForm').ajaxSubmit(options);
            } );
        });
    })();

    //  退款操作
    (function(){
        var btn = $('.refund'), input = $("input[name='alltotal_pay']");
        input.blur(function(){
            check();
        });
        btn.unbind('click').on('click', function(){
            var _this = $(this), sell_back_id = _this.attr('sell_back_id');
            check();
            if(input.parent().is('.error')){
                return false;
            }else {
                $.confirm('确定退款吗？', function(){
                    $.ajax({
                        type: 'post',
                        url: ctx+ '/api/store/store-sell-back/refund.do',
                        data: {
                            id: sell_back_id,
                            status: $("#status").val(),
                            alltotal_pay:$("#alltoty_pay").val()
                        },
                        dataType: "json",
                        success : function(data) {
                            if(data.result == 1){
                                $.message.success('退款成功！请等待平台退款给买家。', 'reload');
                            }else {
                                $.message.error(data.message);
                            };
                        },
                        error : function() {
                            $.message.error('出现错误，请重试！');
                        }
                    });
                });
            };
        });

        function check(){
            if(!input.val()){
                input.parent().addClass('has-error error');
                return false;
            }else {
                input.parent().removeClass('has-error error');
            };

            return this;
        }
    })();

    //  货、款区分
    (function(){
        var type = $('#i_type').val();
        if(type == 1){
            $('.i-type').html('款');
        }else if(type == 2) {
            $('.i-type').html('货');
        };
    })();

    //  修复IE8（包括IE7）下样式问题
    (function(){
        if(lteIE8){
            //  IE7、8下h1高度太高问题
            $('.return-info').find('h1').css({
                paddingLeft: 10,
                paddingTop : 0,
                paddingRight: 10,
                paddingBottom: 0
            });
        };

    })();
});