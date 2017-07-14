/**
 * Created by Andste on 2016/6/6.
 */

$(function(){

    //  商品删除操作
    (function(){
        var deleteBtn = $('.delete-goods');
        deleteBtn.unbind('click').on('click', function(){
            var _this = $(this), goods_id = _this.attr('goods_id'), market_enable = _this.attr('market_enable');
            $.confirm('确定要删除吗？', function () {
                setTimeout(function(){
                    if(market_enable == 1){
                        $.message.error('请先将商品下架，在进行删除！');
                        return false
                    }else {
                        deleteGood();
                    };
                }, 500);
            });

            function deleteGood(){
                $.ajax({
                    url : ctx + "/api/b2b2c/store-goods/delete-goods.do?goods_id=" + goods_id,
                    cache : false,
                    dataType : 'json',
                    success : function(result) {
                        if(result.result==1){
                            $.message.success('删除成功！', 'reload');
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

    //  商品库存修改
    (function(){
        var warehouseBtn = $('.warehouse-goods');
        warehouseBtn.unbind('click').on('click', function(){
            var _this = $(this);
            $.ajax({
                url : "warehouse_edit.html?goods_id="+_this.attr("goods_id")+"&store_id="+_this.attr("store_id"),
                success : function(html) {
                    $.dialogModal({
                        title: '修改库存',
                        html: html,
                        //width: 500,
                        //  当dialog显示时，判断是否有规格，如果没有规格则将dialog宽度设置为300
                        showCall: function(){
                            if(!$('#dialogModal').find('*').is('.have_spec')){
                                if(lteIE8){
                                    $('#dialogModal').css({
                                        width: 300,
                                        marginLeft: -(280-(560-300)/2)
                                    });
                                }else {
                                    $('#dialogModal').find('.modal-dialog').css({width: 300});
                                };
                            };
                        },

                        callBack: function(){
                            var input = $('#dialogModal').find('.form-control'), inputLen = input.length;
                            for(var i = 0; i < inputLen; i ++){
                                var _thisInput = input.eq(i), val = _thisInput.val();
                                if(!val || isNaN(val) || val < 0 || !/^\d+$/.test(val)){
                                    input.eq(i).closest('.form-group').addClass('has-error error');
                                }else {
                                    input.eq(i).closest('.form-group').removeClass('has-error error');
                                };
                            };
                            for(var i = 0; i < inputLen; i ++){
                                var _thisInput = input.eq(i);
                                if(_thisInput.closest('.form-group').is('.error')){
                                    return false;
                                };
                            };

                            update();

                            function update(){
                                var options = {
                                    url : ctx + "/api/b2b2c/store-goods/save-goods-store.do?storeid="+_this.attr("store_id"),
                                    type : "POST",
                                    dataType : 'json',
                                    success : function(result) {
                                        if(result.result==1){
                                            $.message.success('修改成功！', 'reload');
                                        }else {
                                            $.message.error(result.message);
                                        };
                                    },
                                    error : function(e) {
                                        $.message.error("出现错误 ，请重试");
                                    }
                                };
                                $('#dialogModal').find("#goodsStoreForm").ajaxSubmit(options);
                            };
                        }
                    });
                },
                error : function() {
                    $.message.error("出现错误，请重试");
                },
                cache : false
            });
        });
    })();

    //  全选
    (function(){
        var checkAllBtn = $('.check-all'), checkBox = $('.checkbox'), checkBoxLen = checkBox.length;
        checkAllBtn.unbind('click').on('click', function(){
            var _this = $(this);
            if(_this.is(':checked')){
                checkBox.prop('checked', true);
            }else {
                checkBox.removeAttr('checked');
            };
        });
    })();

    //  如果是上架状态，禁用删除按钮
    (function(){
        var deleteGoods = $('.delete-goods'), deleteGoodsLen = deleteGoods.length;
        for(var i = 0; i < deleteGoodsLen; i++){
            var _thisDe = deleteGoods.eq(i),
                str = _thisDe.attr('market_enable');
            if(str == 1){
                _thisDe.attr('disabled',"true").attr('title', '上架状态不可删除!').off('click');
            };
        };
    })();

    /* 筛选JS
     ============================================================================ */
    //  筛选
    (function(){
        var store_cat = $('.store_cat'),
            market_enable = $('.market_enable'),
            store_cat_val = '',
            market_enable_val = '',
            seach_keyword_val = '';

        //  分类发生改变时
        store_cat.change(function(){
            var _this = $(this),
                store_cat_val   = _this.val();
            market_enable_val = market_enable.val();
            seach_keyword_val = $('.seach-keyword').val();
            if(!testStr(seach_keyword_val)){
                $.message.error('不能包含特殊字符！');
                return false;
            };
            select(seach_keyword_val, store_cat_val, market_enable_val);
        });

        //  上下架状态发生改变时
        market_enable.change(function(){
            var _this = $(this), market_enable_val = _this.val();
            store_cat_val     = store_cat.val();
            seach_keyword_val = $('.seach-keyword').val();
            if(!testStr(seach_keyword_val)){
                $.message.error('不能包含特殊字符！');
                return false;
            };
            select(seach_keyword_val, store_cat_val, market_enable_val);
        });

        //  按下回车键时
        $('.seach-keyword').keydown(function (event) {
            if (event.keyCode == 13) {
                seach_keyword_val = $('.seach-keyword').val();
//                if(!testStr(seach_keyword_val)){
//                    $.message.error('不能包含特殊字符！');
//                    return false;
//                };
                store_cat_val     = store_cat.val();
                market_enable_val = market_enable.val();
                select(seach_keyword_val, store_cat_val, market_enable_val);
            }
        });

        //  搜索关键词时
        $('#key_seach_btn').on('click', function(){
            var  _this = $(this),
                seach_keyword_val = $('.seach-keyword').val();
            //暂时去掉特殊字符的验证 by fengkun
//            if(!testStr(seach_keyword_val)){
//                $.message.error('不能包含特殊字符！');
//                return false;
//            };
            store_cat_val     = store_cat.val();
            market_enable_val = market_enable.val();
            select(seach_keyword_val, store_cat_val, market_enable_val);
        });

        //  如果之前有搜索过，则获取url中的搜索关键词赋值给搜索框
        (function(){
            var str = GetQueryString('goodsName');
            if(str){
                $('.seach-keyword').val(str)
            };
        })();

        function select(k, s ,m){
                if(m == -1){
                    location.href="warehouse.html?goodsName="+k+"&store_cat="+s/*+"&menu=store_goods"+url_parame*/;
                }else {
                    location.href="warehouse.html?goodsName="+k+"&store_cat="+s+"&market_enable="+m/*+"&menu=store_goods"+url_parame*/;
                }
                /*<#if type??>
                url_parame = "&type=${type}&mark=${mark}&tagId=${tagId}";
                </#if>*/
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
            input.addClass('focus');
            seach.addClass('focus');
            seachBtn.addClass('show');
        };

        function bindBlur(){
            input.removeClass('focus');
            seach.removeClass('focus');
            seachBtn.removeClass('show');
        };
    })();


    (function(){
    //  修复IE7下 .right-top-tools样式错乱问题
        if(Sys.ie == 7){
            $('.right-top-tools').css({width: '49%'});
        };

    //  修复IE下 顶部工具栏位置问题
        if(Sys.ie > 7) {
            $('.top-tools').css({height: 48});
        };

    //  修复IE78下搜索框样式错乱问题
        if(Sys.ie < 9){
            $('.seach-keyword').css({height: 20});
            $('.seach .icomoon').css({top: 0});
        };
    })();

});