/**
 * Created by Andste on 2016/6/20.
 */
$(function(){
	
    //  去审核
    (function(){
    	var title="退款审核";
    	if($("#sell").val()=='0'){
    		title="退货审核";
    	}
        var btn = $('.view-detail');
        btn.unbind('click').on('click', function(){
            var _this = $(this), id = _this.attr('return_id');
            $.ajax({
                url: './return_auth.html?id=' + id,
                type: 'GET',
                success: function(html){
                    $.dialogModal({
                        title: title,
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
        });

    })();

    //  修复IE7下样式问题
    (function(){

        if(Sys.ie == 7){
            //  IE7下tr没有背景色问题
            var th = $('.return table thead th');
            th.css({backgroundColor: '#f2f2f2'});
        };

    })();
});