/**
 * Created by Andste on 2016/6/16.
 */
$(function(){

    // 订单内商品种类2种以上，则有下划线
    (function(){
        var orderContent = $('.order-content'), orderContentLen = orderContent.length;
        for(var i = 0; i < orderContentLen; i++){
            var _thisCon = orderContent.eq(i), items = _thisCon.find('.item-goods-price-num'), itemsLen = items.length;
            if(itemsLen > 1){
                for(var j = 0; j < itemsLen; j++){
                    if(j < (itemsLen-1)){
                        items.eq(j).css({borderBottom: '1px solid #f2f2f2'});
                    };
                };
            };
        };
    })();

    // 订单筛选
    (function(){
        var btn = $('#filter_seach, .app-tab-tools a');
        btn.unbind('click').on('click', function(){
            var _this = $(this),
                sn = $('#filter_sn').val(), 
                customer = $.trim($('#filter_customer').val()),
                goods=$.trim($('#filter_goods').val()),
                start_time = $('#filter_start_time').val(),
                end_time = $('#filter_end_time').val(),
                state = $('.app-tab-tools').find('.active').attr('order_state');
            if(_this.attr('order_state')){
                state = _this.attr('order_state');
            };
            if(!sn && !customer && !start_time && !end_time && !goods){
            	// 都为空的情况
                location.href = './order.html?order_state=' + state;
                return;
            };
            if(sn){
                location.href = './order.html?order_state='+ state+ '&keyword=' + sn;
            }else if(customer && start_time && goods){
                location.href = './order.html?order_state='+ state+ '&buyerName=' + customer + '&startTime=' + start_time + '&endTime=' + GetTodayString()+"&goods="+goods;
            } else if(customer && !start_time && goods){
                location.href = './order.html?order_state='+ state+ '&buyerName=' + customer+"&goods="+goods;
            } else if(customer && start_time && !goods){
                location.href = './order.html?order_state='+ state+ '&buyerName=' + customer;
            }else if(!customer && (start_time && end_time) && goods){
                if(start_time > end_time){
                    $.message.error('开始时间不得大于结束时间！');
                }else {
                    location.href = './order.html?order_state='+ state +'&startTime=' + start_time + '&endTime=' + end_time+"&goods=" + goods;
                };
            }else if(!customer && (start_time && end_time) && !goods){
                if(start_time > end_time){
                    $.message.error('开始时间不得大于结束时间！');
                }else {
                    location.href = './order.html?order_state='+ state +'&startTime=' + start_time + '&endTime=' + end_time;
                };
            }else if(start_time && !end_time && goods){
                location.href = './order.html?order_state='+ state +'&startTime=' + start_time + '&endTime=' + GetTodayString()+"&goods=" + goods;
            }else if(start_time && !end_time && !goods){
                location.href = './order.html?order_state='+ state +'&startTime=' + start_time + '&endTime=' + GetTodayString();
            }else if(!start_time && end_time && goods){
                $.message.error('请选择开始时间！');
                return false;
            }else if(!start_time && end_time && !goods){
                $.message.error('请选择开始时间！');
                return false;
            }
            else {
                location.href = './order.html?order_state='+ state +'&buyerName=' + customer +'&startTime=' + start_time + '&endTime=' + end_time+'&goods='+goods;
            };
        });
    })();

    // 如果url有参数则赋值给对应检索框
    (function(){
        if(GetQueryString('keyword')){
            $('#filter_sn').val(GetQueryString('keyword'));
        };
        if(GetQueryString('buyerName')){
            $('#filter_customer').val(GetQueryString('buyerName'));
        };
        if(GetQueryString('goods')){
            $('#filter_goods').val(GetQueryString('goods'));
        };
        if(GetQueryString('startTime')){
            $('#filter_start_time').val(GetQueryString('startTime'));
            $('#filter_end_time').val(GetQueryString('endTime'));
        };
    })();

    // 查看订单详情
    (function(){
        var btn = $('.tools-order-header a');
        btn.unbind('click').on('click', function(){
            var _this = $(this), sn = _this.attr('sn');
            PopPpWindow({
                href     : './order_detail_min.html?ordersn=' + sn,
                name     : '订单详情',
                param    : 'height=700, width=1230, top=200, left=200, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no',
                callBack : function(){
                    $.confirm('您的浏览器已阻止新窗口弹出,本次将在本窗口打开。', function(){
                        location.href = './order_detail.html?ordersn=' + sn;
                    })
                }
            });
        });
    })();

    // 查看物流信息
    (function(){
        var btn = $('.order-exp-info');
        btn.unbind('click').on('click', function(){
            var _this = $(this), order_sn = _this.attr('order_sn');
            $.ajax({
                url: './order_exp_info.html?ordersn=' + order_sn,
                type: 'GET',
                success: function(html){
                    $.dialogModal({
                        title: '物流信息',
                        html : html,
                        btn  : false,
                        backdrop: false
                    });
                },
                error: function(){
                    $.message.error('出现错误，请重试！');
                }
            });
        });
    })();

    /*
	 * 兼容JS
	 * ============================================================================
	 */
    (function(){
        // 修复IE下样式错乱
        if(Sys.ie == 7){
            $('.tools-thead .goods').css({float: 'left'});
            $('#filter_seach').css({lineHeight: '20px'});

            if($('.order-list-item td')){
                $('.order-list-item td').find('span').css({float: 'none'});
            }
        };
    })();
});