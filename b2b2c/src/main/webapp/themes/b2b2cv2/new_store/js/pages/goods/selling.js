/**
 * Created by Andste on 2016/6/15.
 */
$(function () {
    var typeStr = GetQueryString('type');

    //  如果是保存标签商品页
    (function () {
        if (typeStr == 'tag') {
            var saveAll = $('.down-all-goods'), tagId = parseInt(GetQueryString('tagId')), mark = GetQueryString('mark');
            saveAll.html('保存选中');
            var options = {
                url: ctx + '/api/b2b2c/store-goods-tag/save-rel.do?tagId=' + tagId,
                type: "POST",
                dataType: 'json',
                success: function (result) {
                    if (result.result == 1) {
                        $.message.success('保存成功！', function(){
                            location.href = './tags_edit.html?tagid=' + tagId + '&mark=' + mark;
                        });
                    } else {
                        $.message.error(result.message);
                    }
                    ;
                },
                error: function (e) {
                    $.message.error("出现错误 ，请重试！");
                }
            };
            saveAll.unbind('click').on('click', function () {
                $.confirm('确定保存选中吗？', function () {
                    $('#selling_form').ajaxSubmit(options);
                })
            });
        }
        ;
    })();

    //  商品单个下架操作
    (function () {
        var downBtn = $('.down-goods');
        downBtn.unbind('click').on('click', function (e) {
            e.stopPropagation();
            var _this = $(this), goods_id = _this.attr('goods_id');
            $.confirm('确定要下架此商品吗？', function () {
                setTimeout(function () {
                    downGood();
                }, 500);
            });

            function downGood() {
                $.ajax({
                    url: ctx + "/api/b2b2c/store-goods/under-goods.do?goods_id=" + goods_id,
                    cache: false,
                    dataType: 'json',
                    success: function (data) {
                        if (data.result == 1) {
                            $.message.success('下架成功！', 'reload');
                        } else {
                            $.message.error(data.message);
                        };
                    },
                    error: function () {
                        $.alert("出现错误，请重试");
                    }
                });
            };
        });
    })();

    //  商品批量下架操作
    (function () {
        if (!typeStr) {
            var downAllBtn = $('.down-all-goods');
            downAllBtn.unbind('click').on('click', function () {
                var _this = $(this);
                $.confirm('确定要下架这些商品吗？', function () {
                    downAllGoods();
                });
            });

            function downAllGoods() {
                var options = {
                    url: ctx + "/api/b2b2c/store-goods/under-goods.do",
                    type: "POST",
                    dataType: 'json',
                    success: function (data) {
                        if (data.result == 1) {
                            $.message.success('下架成功！', 'reload');
                        } else {
                            $.message.error(data.message);
                        }
                        ;
                    },
                    error: function (e) {
                        $.message.error("出现错误 ，请重试");
                    }
                };
                $('#selling_form').ajaxSubmit(options);
            };
        }
        ;
    })();

    //  商品库存修改
    (function () {
        var warehouseBtn = $('.warehouse-goods');
        warehouseBtn.unbind('click').on('click', function (e) {
            e.stopPropagation();
            var _this = $(this);
            $.ajax({
                url: "warehouse_edit.html?goods_id=" + _this.attr("goods_id"),
                success: function (html) {
                    $.dialogModal({
                        title: '修改库存',
                        html: html,
                        //width: 500,
                        //  当dialog显示时，判断是否有规格，如果没有规格则将dialog宽度设置为300
                        showCall: function () {
                            if (!$('#dialogModal').find('*').is('.have_spec')) {
                                if (lteIE8) {
                                    $('#dialogModal').css({
                                        width: 300,
                                        marginLeft: -(280 - (560 - 300) / 2)
                                    });
                                } else {
                                    $('#dialogModal').find('.modal-dialog').css({width: 300});
                                }
                                ;
                            }
                            ;
                        },

                        callBack: function () {
                            //  to do something......
                            var input = $('#dialogModal').find('.form-control'), inputLen = input.length;
                            for (var i = 0; i < inputLen; i++) {
                                var _thisInput = input.eq(i), val = _thisInput.val();
                                if (!val || isNaN(val) || val < 0 || !/^\d+$/.test(val)) {
                                    input.eq(i).css({color: '#a94442'}).closest('.form-group').addClass('has-error error');
                                } else {
                                    input.eq(i).css({color: ''}).closest('.form-group').removeClass('has-error error');
                                }
                                ;
                            }
                            ;
                            for (var i = 0; i < inputLen; i++) {
                                var _thisInput = input.eq(i);
                                if (_thisInput.closest('.form-group').is('.error')) {
                                    return false;
                                }
                                ;
                            }
                            ;

                            update();

                            function update() {
                                var options = {
                                    url: ctx + "/api/b2b2c/store-goods/save-goods-store.do",
                                    type: "POST",
                                    dataType: 'json',
                                    success: function (data) {
                                        if (data.result == 1) {
                                            $.message.success('修改成功！', 'reload');
                                        } else {
                                            $.message.error(data.message);
                                        };
                                    },
                                    error: function (e) {
                                        $.message.error("出现错误 ，请重试");
                                    }
                                };
                                $('#dialogModal').find("#goodsStoreForm").ajaxSubmit(options);
                            };
                        }
                    });
                },
                error: function () {
                    $.message.error("出现错误，请重试");
                },
                cache: false
            });
        });
    })();

    //  商品编辑
    (function(){
        var btn = $('.edit-goods');
        btn.unbind('click').on('click', function(e){
            var goods_id = $(this).attr('goods_id') || 0;
            e.stopPropagation();
            location.href = './goods_publish.html?goods_id=' + goods_id;
        });
    })();

    //  修复IE8下选择框样式问题
    (function () {
        if (Sys.ie == 8) {
            $('.checkbox').removeClass('check-width');
        };
    })();

    //  全选
    (function () {
        var checkAllBtn = $('.check-all'), checkBox = $('.checkbox');
        checkAllBtn.unbind('click').on('click', function () {
            var _this = $(this);
            if (_this.is(':checked')) {
                checkBox.prop('checked', true);
                showAllDown('show');
            } else {
                checkBox.removeAttr('checked');
                showAllDown('hide');
            };
        });
    })();

    //  手动选择
    (function () {
        var checkBox = $('.checkbox'), checkBoxLen = checkBox.length, tr = $('#selling_form').find('tr');
        checkBox.unbind('click').on('click', function () {
            var _this = $(this);
            if (_this.is(':checked')) {
                _this.removeAttr('checked');
            } else {
                _this.prop('checked', true);
            };
            f();
        });
        $('.th-name').find('a').on('click', function () {
            window.open($(this).attr('href'));
            return false;
        });
        tr.unbind('click').on('click', function () {
            var _this = $(this), c = _this.find('.checkbox');
            if (c.is(':checked')) {
                c.removeAttr('checked');
            } else {
                c.prop('checked', true);
            };

            f();
        });

        function f() {
            var temp = 0;
            for (var i in checkBox) {
                if (checkBox.eq(i).is(':checked')) {
                    temp++;
                };
            };
            m(temp);
        };

        function m(temp) {
            if (temp == checkBoxLen && temp != 0) {
                $('.check-all').prop('checked', true);
            } else {
                $('.check-all').removeAttr('checked');
            };
            if (temp > 0) {
                showAllDown('show');
            } else {
                showAllDown('hide');
            };
        };

    })();

    //  显示、隐藏【批量下架】按钮
    function showAllDown(str) {
        var downAllGoods = $('.down-all-goods');
        if (str == 'show') {
            downAllGoods.addClass('show');
            if (Sys.ie < 10) {
                downAllGoods.animate({marginLeft: 0}, 300);
            };
        } else {
            downAllGoods.removeClass('show');
            if (Sys.ie < 10) {
                downAllGoods.animate({marginLeft: -90}, 300);
            };
        };
    };


    /* 筛选JS
     ============================================================================ */
    //  筛选
    (function () {
        var store_cat = $('.store_cat'),
            market_enable = $('.market_enable'),
            store_cat_val = '',
            seach_keyword_val = '';

        //  分类发生改变时
        store_cat.change(function () {
            var _this = $(this),
                store_cat_val = _this.val();
            seach_keyword_val = $('.seach-keyword').val();
            if (!testStr(seach_keyword_val)) {
                $.message.error('不能包含特殊字符！');
                return false;
            };
            select(seach_keyword_val, store_cat_val);
        });

        //  按下回车键时
        $('.seach-keyword').keydown(function (event) {
            if (event.keyCode == 13) {
                seach_keyword_val = $('.seach-keyword').val();
//                if (!testStr(seach_keyword_val)) {
//                    $.message.error('不能包含特殊字符！');
//                    return false;
//                };
                store_cat_val = store_cat.val();
                select(seach_keyword_val, store_cat_val);
            }
        });

        //  搜索关键词时
        $('#key_seach_btn').on('click', function () {
            var _this = $(this),
                seach_keyword_val = $('.seach-keyword').val();
            //搜索去掉关键词限制
//            if (!testStr(seach_keyword_val)) {
//                $.message.error('不能包含特殊字符！');
//                return false;
//            };
            store_cat_val = store_cat.val();
            select(seach_keyword_val, store_cat_val);
        });

        //  如果之前有搜索过，则获取url中的搜索关键词赋值给搜索框
        (function () {
            var k = GetQueryString('goodsName'), s = GetQueryString('store_cat');
            if (k) {
                $('.seach-keyword').val(k);
            };
            if(s){
                $('.store_cat').val(s);
            };
        })();

        function select(k, s) {
            if (!k && (s == 0)) {
                location.href = "selling.html?market_enable=1";
            }else if(!k && s){
                location.href = "selling.html?&store_cat=" + s +"&market_enable=1";
            } else {
                location.href = "selling.html?goodsName=" + k + "&store_cat=" + s +"&market_enable=1";
            }
        };

        function testStr(str) {
            var pattern = /[`~!@#\$%\^\&\*\(\)_\+<>\?:"\{\},\.\\\/;'\[\]]/im;
            if (pattern.test(str)) {
                return false
            } else {
                return true;
            };
        };
    })();

    //  搜索框效果
    (function () {
        var seach = $('.seach'),
            input = seach.find('input'),
            seachBtn = seach.find('.seach-btn');
        input.on('click', function (e) {
            bindFocus();
            e.stopPropagation();
        });

        $(document).on('click', function (e) {
            bindBlur();
            e.stopPropagation();
        });

        seachBtn.hover(function () {
            $(document).off('click');
        }, function () {
            $(document).on('click', function (e) {
                bindBlur();
                e.stopPropagation();
            });
        });

        function bindFocus() {
            input.addClass('focus');
            seach.addClass('focus');
            seachBtn.addClass('show');
        };

        function bindBlur() {
            input.removeClass('focus');
            seach.removeClass('focus');
            seachBtn.removeClass('show');
        };
    })();

    //  修复IE7下 .right-top-tools样式错乱问题
    (function () {
        if (Sys.ie == 7) {
            $('.right-top-tools').css({width: '49%'});
        };
    })();

    //  修复IE下 顶部工具栏位置问题
    (function () {
        if (Sys.ie > 7) {
            $('.top-tools').css({height: 48});
        };
    })();

    //  修复IE78下搜索框样式错乱问题
    (function(){
        if(Sys.ie < 9){
            $('.seach-keyword').css({height: 20});
            $('.seach .icomoon').css({top: 0});
        };
    })();

});