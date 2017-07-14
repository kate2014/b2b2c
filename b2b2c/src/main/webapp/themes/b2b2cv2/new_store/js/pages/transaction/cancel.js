/**
 * Created by Andste on 2016/7/8.
 */

$(function(){

    //  查看订单详情
    (function(){
        var btn = $('.tools-order-header a');
        btn.unbind('click').on('click', function(){
            var _this = $(this), sn = _this.attr('sn');
            window.open('./order_detail.html?ordersn=' + sn);
        });
    })();


    //  取消订单审核
    (function(){
        var btn = $('.order-state .btn');
        btn.unbind('click').on('click', function(){
            var _this = $(this), order_id = _this.attr('order_id'), status = _this.attr('status'), str = _this.html();
            $.confirm('确定'+ str +'吗？', function(){
                $.ajax({
                    url: ctx + '/api/store/store-order/auth-cancel-application.do',
                    data: {order_id: order_id, status: status},
                    type: 'POST',
                    success: function(result){
                        if(result.result == 1){
                            $.message.success(result.message, 'reload');
                        }else {
                            $.message.error(result.message);
                        };
                    },
                    error: function(){
                        $.message.error('出现错误，请重试！');
                    }
                });
            });
        });
    })();
});