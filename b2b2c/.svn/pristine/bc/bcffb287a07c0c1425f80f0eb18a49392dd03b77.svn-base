/**
 * Created by Andste on 2016/6/15.
 */
$(function(){

    //  商品单个操作
    (function(){
        var btn = $('.goods-operate').find('a');
        btn.unbind('click').on('click', function(e){
            e.stopPropagation();
            var _this = $(this), goods_id = _this.attr('goods_id'), operate = _this.attr('operate'), str = $.trim(_this.html());
            if(str == '删除'){
                str = '<span style="color: #d9534f;">删除</span>';
            };
            $.confirm('确定要'+ str +'此商品吗？', function () {
                setTimeout(function () {
                    operateGood();
                }, 500);
            });

            function operateGood(){
                $.ajax({
                    url : ctx + "/api/b2b2c/store-goods/"+ operate +".do?goods_id=" + goods_id,
                    cache : false,
                    dataType : 'json',
                    success : function(result) {
                        if(result.result==1){
                            $.message.success( str + '成功！', 'reload');
                        }else {
                            $.message.error(result.message);
                        };
                    },
                    error : function() {
                        $.message.error("出现错误，请重试！");
                    }
                });
            };
        });
    }());

    //  商品批量操作
    function operate(_this){
        var _this = _this, str = $.trim(_this.html()), operate = _this.attr('operate');

        if(str == '批量还原'){
            str = '还原';
        }else {
            str = '<span style="color: #d9534f;">删除</span>';
        };
        $.confirm('确定要'+ str +'这些商品吗？', function(){
            downAllGoods();
        });

        function downAllGoods(){
            var options = {
                url : ctx + "/api/b2b2c/store-goods/"+ operate +".do",
                type : "POST",
                dataType : 'json',
                success : function(data) {
                    if(data.result == 1){
                        $.message.success(str + '成功！', 'reload');
                    }else {
                        $.message.error(data.message);
                    };
                },
                error : function(e) {
                    $.message.error("出现错误，请重试！");
                }
            };
            $('#trash_form').ajaxSubmit(options);
        };
    };

    //  修复IE8下选择框样式问题
    (function(){
        if(Sys.ie == 8){
            $('.checkbox').removeClass('check-width');
        }
    })();

    //  全选
    (function(){
        var checkAllBtn = $('.check-all'), checkBox = $('.checkbox');
        checkAllBtn.unbind('click').on('click', function(){
            var _this = $(this);
            if(_this.is(':checked')){
                checkBox.prop('checked', true);
                showAllOperate('enb');
            }else {
                checkBox.removeAttr('checked');
                showAllOperate('dis');
            };
        });
    })();

    //  手动选择
    (function(){
        var checkBox = $('.checkbox'), checkBoxLen = checkBox.length, tr = $('#trash_form').find('tr');
        checkBox.unbind('click').on('click', function(){
            var _this = $(this);
            if(_this.is(':checked')){
                _this.removeAttr('checked');
            }else {
                _this.prop('checked', true);
            };
            f();
        });
        $('.th-name').find('a').on('click', function(){
            window.open($(this).attr('href'));
            return false;
        });
        tr.unbind('click').on('click', function(){
            var _this = $(this), c = _this.find('.checkbox');
            if(c.is(':checked')){
                c.removeAttr('checked');
            }else {
                c.prop('checked', true);
            };

            f();
        });

        function f(){
            var temp = 0;
            for(var i in checkBox){
                if(checkBox.eq(i).is(':checked')){
                    temp++;
                };
            };
            m(temp);
        };

        function m(temp){
            if(temp == checkBoxLen && temp != 0){
                $('.check-all').prop('checked', true);
            }else {
                $('.check-all').removeAttr('checked');
            };
            if(temp > 0){
                showAllOperate('enb');
            }else {
                showAllOperate('dis');
            };
        };

    })();

    //  禁用、启用【批量操作】按钮
    showAllOperate('dis');
    function showAllOperate(str){
        var  btn = $('.left-top-tools').find('a');
        if(str == 'enb'){
            btn.css({cursor: 'pointer'})
                .removeAttr('disabled')
                .on('click', function(){
                    operate($(this));
                });
        }else {
            btn.css({cursor: 'not-allowed'})
                .attr('disabled', true)
                .unbind('click');
        };
    };


    /* 筛选JS
     ============================================================================ */
    //  筛选
    (function(){
        var seach_keyword_val = '';

        //  按下回车键时
        $('.seach-keyword').keydown(function (event) {
            if (event.keyCode == 13) {
                seach_keyword_val = $('.seach-keyword').val();
                if(!testStr(seach_keyword_val)){
                    $.message.error('不能包含特殊字符！');
                    return false;
                };
                select(seach_keyword_val);
            }
        });

        //  搜索关键词时
        $('#key_seach_btn').on('click', function(){
            var  _this = $(this),
                seach_keyword_val = $('.seach-keyword').val();
            if(!testStr(seach_keyword_val)){
                $.message.error('不能包含特殊字符！');
                return false;
            };
            select(seach_keyword_val);
        });

        //  如果之前有搜索过，则获取url中的搜索关键词赋值给搜索框
        (function(){
            var str = GetQueryString('goodsName');
            if(str){
                $('.seach-keyword').val(str)
            };
        })();

        function select(k){
            if(!k){
                location.href="trash.html?disable=1";
            }else {
                location.href="trash.html?goodsName="+k+"&disable=1";
            };
        };

        function testStr(str){
            var pattern	= /[`~!@#\$%\^\&\*\(\)_\+<>\?:"\{\},\.\\\/;'\[\]]/im;
            if(pattern.test(str)){
                return false
            }else {
                return true;
            };
        };
    })();

    //  搜索框效果
    (function(){
        var seach = $('.seach'),
            input = seach.find('input'),
            seachBtn = seach.find('.seach-btn');
        input.on('click', function(e){
            bindFocus();
            e.stopPropagation();
        });

        $(document).on('click', function(e){
            bindBlur();
            e.stopPropagation();
        });

        seachBtn.hover(function(){
            $(document).off('click');
        }, function(){
            $(document).on('click', function(e){
                bindBlur();
                e.stopPropagation();
            });
        });

        function bindFocus(){
            seach.addClass('focus');
        };

        function bindBlur(){
            seach.removeClass('focus');
        };
    })();

    //  修复IE7下 .right-top-tools样式错乱问题
    (function(){
        if(Sys.ie == 7){
            $('.right-top-tools').css({width: '49%'});
        };

    //  修复IE下 顶部工具栏位置问题
        if(Sys.ie > 7){
            $('.top-tools').css({height: 48});
        };

    //  修复IE78下搜索框样式问题
        if(Sys.ie < 9){
            $('.seach .icomoon').css({top: 0, bottom: 2}).find('input').css({padding: 0});
            $('.seach .btn').css({height: 28});
        };
    })();
});