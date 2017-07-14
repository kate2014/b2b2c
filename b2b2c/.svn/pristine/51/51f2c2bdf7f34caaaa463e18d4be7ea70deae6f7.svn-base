$(function () {
 
    //点击保存按钮提交表单
    $('#update').unbind('click').on('click', function () {
        var options = {
            url: ctx + "/api/b2b2c/store-api/warning_count_edit.do",
            type: 'POST',
            dataType: 'json',
            success: function (result) {
                if (result.result == 1) {
                    $.message.success('保存成功！', 'reload');
                } else {
                    $.message.error(result.message);
                };
            },
            error: function () {
                $.message.error("出现错误 ，请重试");
            }
        };
        if($('.setting-box').find('.error').length > 0){
            $.message.error('表单填写有误，请检查！');
            return false;
        }else {
            $("#storeInfo").ajaxSubmit(options);
        };
    });

    //  修复按钮样式
    (function(){
        if(Sys.ie < 9){
            $('.to_save').css({height: '30px'});
        };
    })();
});
