/**
 * Created by Andste on 2016/6/20.
 */
$(function(){
    //  去操作
    (function(){
        var btn = $('.view-detail');
        btn.unbind('click').on('click', function(){
            var _this = $(this), return_id = _this.attr('return_id'), status = $.trim(_this.html()), title = '查看退款、货单', type = _this.attr('type');
            if(status == '审核'){
                $.ajax({
                    url: './return_auth.html?id=' + return_id,
                    type: 'GET',
                    success: function(html){
                        $.dialogModal({
                            title: '退款、退货审核',
                            html : html,
                            width: 900,
                            btn  : false,
                            backdrop: false
                        });
                    },
                    error: function(){
                        $.message.error('出现错误，请重试！');
                    }
                });
            }else if(status == '查看' || status == '退款'){
                if(status == '退款'){title = '退款操作'};
                $.ajax({
                    url: './return_info.html?id=' + return_id+"&type="+type,
                    type: 'GET',
                    success: function(html){
                        $.dialogModal({
                            title: title,
                            html : html,
                            width: 1000,
                            btn  : false,
                            backdrop: false
                        });
                    },
                    error: function(){
                        $.message.error('出现错误，请重试！');
                    }
                });
            }
        });
    })();

    //  筛选
    (function(){
        var type = $('#type'), tradestatus = $('#tradestatus'), seachBtn = $('#filter_seach'), filterStatus = $('.filter-status'), re_type = $('#ret_type').val(), ret_tradestatus = $('#ret_tradestatus').val(),
            type1 = '<option value="-1">请选择退款状态</option>'
                + '<option value="0">等待审核</option>'
                + '<option value="1">审核通过</option>'
                + '<option value="2">审核拒绝</option>'
                + '<option value="3">等待平台退款</option>'
                + '<option value="6">已退款</option>',
            type2 = '<option value="-1">请选择退货状态</option>'
                + '<option value="0">申请退货</option>'
                + '<option value="1">审核通过</option>'
                + '<option value="3">等待平台退款</option>'
                + '<option value="2">审核拒绝</option>'
                + '<option value="6">已退款</option>',
            noType= '<option value="-1">请选择状态</option>';

        if(re_type == '1'){
            type.val(1);
            tradestatus.empty().append(type1);
            filterStatus.addClass('show');
        }else if(re_type == '2'){
            type.val(2);
            tradestatus.empty().append(type2);
            filterStatus.addClass('show');
        };
  
        var rey_tradestatus = $('#tradestatus');
        var options = rey_tradestatus.find("option");
        for(var i=0;i<options.length;i++){
        	if(options[i].value==ret_tradestatus){
        		options[i].selected=true;
        	}
        }
        type.on('change', function(){
            var _this = $(this), val = _this.val();
            if(val == 1){
                tradestatus.empty().append(type1);
                filterStatus.addClass('show');
            }else if(val == 2){
                tradestatus.empty().append(type2);
                filterStatus.addClass('show');
            }else {
                tradestatus.empty().append(noType);
                filterStatus.removeClass('show');
            };
        });

        
        seachBtn.on('click', function(){
            var _this = $(this), typeVal = type.val(), statusVal = tradestatus.val(), orderSn = $('#order_sn').val(), tradenoVal = $('#tradeno').val(), startTime = $('#sellback_start_time').val(), endTime = $('#sellback_end_time').val(), href = 'return_list.html';
            if(typeVal != '-1'){
                href += '&type=' + typeVal;
            };
            if(statusVal != '-1'){
                href += '&tradestatus=' + statusVal;
            };
            if(orderSn){
                href += '&order_sn=' + orderSn;
            };
            if(tradenoVal){
                href += '&tradeno=' + tradenoVal;
            };
            if(startTime && !endTime){
                href += '&start_time=' + startTime + '&end_time=' + GetTodayString();
            };
            if(!startTime && endTime){
                $.message.error('请选择开始时间！');
                return false;
            };
            if(startTime && endTime){
                if(startTime > endTime){
                    $.message.error('开始时间不得大于结束时间！')
                    return false;
                }else {
                    href += '&start_time=' + startTime + '&end_time=' + endTime;
                };
            };

            href = href.replace('&', '?');
            location.href = href;
        });

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