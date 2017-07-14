/**
 * Created by fk on 2016/12/13.
 */
$(function(){

    // 订单筛选
    (function(){
        var btn = $('#filter_seach');
        btn.unbind('click').on('click', function(){
            var _this = $(this),
            	userid = $('#userid').val(), 
                type = $('#type').val(),
                start_time = $('#start_time').val(),
                end_time = $('#end_time').val();
            if(!start_time && !end_time){
                location.href = './storelogs.html?userid='+ userid+ '&type=' + type ;
            } else if(!start_time && end_time){
                location.href = './storelogs.html?userid='+ userid+ '&type=' + type + '&end_time=' + end_time ;
            }else if(start_time && !end_time){
                location.href = './storelogs.html?userid='+ userid+ '&type=' + type + '&start_time=' + start_time ;
            } else if(start_time && end_time){
            	if(start_time > end_time){
                    $.message.error('开始时间不得大于结束时间！');
                }else {
                    location.href = './storelogs.html?userid='+ userid+ '&type=' + type + '&end_time=' + end_time + '&start_time=' + start_time ;
                };
            }
        });
    })();
});