/**
 * Created by Andste on 2016/6/23.
 */
$(function(){
    //  赠品搜索
    (function(){
        var btn = $('.gift-seach .btn'), input = $('.gift-seach input'), keyWord = GetQueryString('keyword');
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
                location.href = './gift.html';
            }else {
                location.href = './gift.html?keyword='+val;
            };
        }
    })();

    //  新增、修改赠品
    (function(){
        var btn = $('.gift-add, .gift-edit');
        btn.unbind('click').on('click', function(){
            var _this = $(this), gift_id = _this.attr('gift_id'), url = './gift_add.html', title = '新增赠品', str = 'add';
            if(gift_id){
                url = './gift_edit.html?gift_id=' + gift_id;
                title = '修改赠品';
                str = 'edit';
            };
            $.ajax({
                url: url,
                type: 'GET',
                success: function(html){
                    $.dialogModal({
                        title: title,
                        html : html,
                        showCall: function(){
                            $('#gift_form input').blur(function(){
                                var _this = $(this), val = $.trim(_this.val());
                                if(!val){
                                    _this.parent().addClass('has-error error');
                                } else if(_this.is('.gift_num')){
                                    if(val > 999999999){
                                        $.message.error('您输入的数太大！');
                                        _this.parent().addClass('has-error error');
                                        return false;
                                    }else if(val < 1){
                                        $.message.error('赠品数量不能小于1！');
                                        _this.parent().addClass('has-error error');
                                        return false;
                                    }else {
                                        _this.parent().removeClass('has-error error');
                                    };
                                } else if(_this.is('.gift_price')){
                                    if(val == '0'){
                                        $.message.error('赠品价格不能为0！');
                                        _this.parent().addClass('has-error error');
                                    }else {
                                        _this.parent().removeClass('has-error error');
                                    };
                                } else {
                                    _this.parent().removeClass('has-error error');
                                };
                            });

                            if(Sys.ie){
                                $("input[type='text']").css({height: 25});
                            }
                        },
                        callBack: function(){
                            return save(str);
                        }
                    });
                },
                error: function(){
                    $.message.error('出现错误，请重试！');
                }
            });
        });
    })();

    //  删除赠品
    (function(){
        var btn = $('.gift-delete');
        btn.unbind('click').on('click', function(){
            var _this = $(this), gift_id = _this.attr('gift_id');
            $.confirm('确定删除吗？', function(){
                $.ajax({
                    url : ctx + '/api/b2b2c/store-act-gift/delete.do?gift_id=' + gift_id,
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

    /* 新增和修改
     ============================================================================ */
    function save(str) {
        var input = $('#gift_form').find("input[type='text']");
        for(var i = 0; i < input.length; i++){
            var _thisInput = input.eq(i);
            if(!_thisInput.val()){
                _thisInput.parent().addClass('has-error error');
            };
        };
        if ($('#gift_form').find('.error').length > 0) {
            $.message.error('表单填写有误，请检查！');
            return false;
        } else if ($('#sortable_').find('li').length < 2) {
            $.message.error('请上传一张赠品图片！');
            return false;
        } else {
            var options = {
                url: ctx + '/api/b2b2c/store-act-gift/save-' + str + '.do',
                type: 'POST',
                dataType: 'json',
                success: function (data) {
                    if (data.result == 1) {
                        $.message.success(data.message, 'reload');
                    } else {
                        $.message.error(data.message);
                    };
                },
                error: function (e) {
                    $.message.error('出现错误 ，请重试！');
                }
            };
            $("#gift_form").ajaxSubmit(options);
        };
    };
});