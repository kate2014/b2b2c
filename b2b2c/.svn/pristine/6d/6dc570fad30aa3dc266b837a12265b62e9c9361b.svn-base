/**
 * Created by Andste on 2016/6/24.
 */
$(function(){


    //  模板删除
    (function(){
        var btn = $('.delete-temp');
        btn.unbind('click').on('click', function(){
            var _this = $(this), temp_id = _this.attr('temp_id');
            if(!_this.attr('disabled')){
                $.confirm('确定要删除吗？', function(){
                    $.ajax({
                        url : ctx + '/api/b2b2c/transport/del.do?tempid=' + temp_id,
                        type : 'post',
                        dataType : 'json',
                        cache : false,
                        success : function(result) {
                            if (result.result == 1) {
                                $.message.success('删除成功！', 'reload');
                            }else{
                                $.message.error(result.message);
                            }
                        },
                        error : function() {
                            $.message.error('出现错误，请重试！');
                        }
                    });
                });
            };
        });
    })();

    //  设置默认
    (function(){
        var btn = $('.default-temp');
        btn.unbind('click').on('click', function(){
            var _this = $(this), temp_id = _this.attr('temp_id');
            if(!_this.attr('disabled')){
                $.confirm('确定将此模板设置为默认吗？', function(){
                    $.ajax({
                        url : ctx + '/api/b2b2c/transport/set-def-temp.do?tempid=' + temp_id,
                        type : 'post',
                        dataType : 'json',
                        cache : false,
                        success : function(result) {
                            if (result.result == 1) {
                                setTimeout(function(){
                                    $.message.success('设置成功！', 'reload');
                                }, 500);
                            }else {
                                $.message.error(result.message);
                            };
                        },
                        error : function() {
                            $.message.error("出现错误，请重试");
                        }
                    });
                });
            };
        });
    })();


    //  物流公司关闭
    (function(){
        var btn = $('.off-logistics-company');
        btn.unbind('click').on('click', function(){
            var _this = $(this), logi_id = _this.attr('logi_id');
            $.confirm('确定要关闭吗？', function(){
                $.ajax({
                    url : ctx + '/api/b2b2c/store-logi-company/del-real.do?logi_id=' + logi_id,
                    dataType:"json",
                    type:"post",
                    success : function(result) {
                        if(result.result==1){
                            $.message.success('关闭成功！', 'reload');
                        }else {
                            $.message.error(result.message);
                        };
                    },
                    error : function() {
                        $.message.error('出现错误，请重试！');
                    }
                });
            })
        });
    })();

    //  物流公司开启
    (function(){
        var btn = $('.open-logistics-company');
        btn.unbind('click').on('click', function(){
            var _this = $(this), logi_id = _this.attr('logi_id');
            $.confirm('确定要开启吗？', function(){
                $.ajax({
                    url : ctx + '/api/b2b2c/store-logi-company/save-real.do?logi_id=' + logi_id,
                    dataType:"json",
                    type:"post",
                    success : function(result) {
                        if(result.result==1){
                            $.message.success('开启成功！', 'reload');
                        }else {
                            $.message.error(result.message);
                        };
                    },
                    error : function() {
                        $.message.error('出现错误，请重试！');
                    }
                });
            })
        });
    })();


    //  处理输入框IE下样式问题
    if(Sys.ie){
        $('.J_DefaultSet').find('.is_vali').css({height: 28});
    }
});