/**
 * Created by Andste on 2016/6/29.
 */
$(function(){
    //  促销活动搜索
    (function(){
        var btn = $('.activity-seach .btn'), input = $('.activity-seach input'), keyWord = GetQueryString('keyword');
        if(keyWord){
            input.val(keyWord);
        };
        btn.unbind('click').on('click', function(){
            goSeach();
        });
        input.keyup(function(event){
            if(event.keyCode == 13){
                goSeach();
            };
        });

        function goSeach(){
            var val = $.trim(input.val());
            if(!val){
                location.href = './promotions.html';
            }else {
                location.href = './promotions.html?keyword='+val;
            };
        }
    })();

    //  删除促销活动
    (function(){
        var btn = $('.activity-delete');
        btn.unbind('click').on('click', function(){
            var _this = $(this), activity_id = _this.attr('activity_id');
            $.confirm('确定删除吗？', function(){
                $.ajax({
                    url : ctx + '/api/b2b2c/store-activity/delete.do?activity_id=' + activity_id,
                    type : 'POST',
                    dataType : 'json',
                    success : function(result) {
                        if (result.result == 1) {
                            $.message.success('删除成功！', 'reload');
                        }else{
                            $.message.error(result.message);
                        };
                    },
                    error : function() {
                        $.message.error("出现错误，请重试！");
                    }
                });
            });
        });
    })();
});