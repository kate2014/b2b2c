var Cart = {
    param: 0,
    init: function (staticserver) {
        var self = Cart;
        this.bindEvent();
        //  选中所有选择框
        //  选中所有商品逻辑修复，优化。 _by: Andste 2016-10-15 18:37:59
        $("input[type=checkbox]").prop('checked', true);
        var stores = $("input[name='select_store']"),
            length = stores.length;
        checkAllGoods();
        function checkAllGoods() {
            if(self.param == length){return}
            self.checkAllStoreProduct(stores[self.param], checkAllGoods)
            self.param++
        }
    },
    bindEvent: function () {
        var self = this;

        //购物数量调整
        $(".Numinput .increase,.Numinput .decrease").on('click', function () {
            var $this = $(this);
            var number = $this.parents(".Numinput");
            var itemid = number.attr("itemid");
            var productid = number.attr("productid");
            var objipt = number.find("input");
            var num = objipt.val();
            num = parseInt(num);
            if (!isNaN(num)) {
                if ($this.hasClass("increase")) {
                    num++;
                }
                if ($this.hasClass("decrease")) {
                    if (num == 1) {
                        $.confirm('确定要删除该商品吗？', function () {
                            self.deleteGoodsItem(itemid);
                        });
                        return false;
                    }
                    num--;
                }
                num = (num <= 1 || num > 100000) ? 1 : num;
                self.updateNum(itemid, num, productid, objipt);
            }
        });

        //购物数量手工输入
        //  优化代码 _by: Andste 2016-10-24 15:01:12
        $(".Numinput input").on('input propertychange', function () {
            self.cartNumChanged($(this));
        })

        //删除商品
        $(".border .delete").on('click', function () {
            var cartid = $(this).parents("tr").attr("itemid");
            $.confirm('您确实要把该商品移出购物车吗？', function () {
                self.deleteGoodsItem(cartid);
            })
        });

        //清空购物车
        $(".border .clean_btn").on('click', function () {
            if (!self.hasAnyCheckedItem()) {
                $.message.error('请您至少选中一件商品进行删除');
                return;
            }
            $.confirm('确定要清空购物车吗？', function () {
                self.clean();
            });
        });

        //继续购物
        $(".border .returnbuy_btn").on('click', function () {
            location.href = "index.html";
        });

        //去结算
        $(".border .checkout_btn").on('click', function () {
            if (isLogin) {
                if (self.hasAnyCheckedItem()) {
                    self.invalidGoods();//判断货品库存，和是否已下架。
                    return;
                }
                $.message.error('请至少勾选一个商品进行结算');
            } else {
                self.showLoginWarnDlg();
            }
        });
        //选择货品
        $("input[name='product_id']").on('click', function () {
            self.checkProduct(this);
        });
        //全选货品
        $("input[name='select_store']").on('click', function () {
            self.checkAllStoreProduct(this);
        });
    },

    //  输入框值发生改变时触发请求  _by: Andste 2016-10-25 11:00:41
    cartNumChanged: function (_this) {
        var self  = this,
            _this = _this,
            _val  = _this.val();
        if (!/^\+?[1-9][0-9]*$/.test(_val)) {
            _this.val(1);
            _val = 1
        }
        var pBuy      = _this.parent();//获取父节点
        var itemid    = pBuy.attr("itemid");
        var productid = pBuy.attr("productid");
        _val          = (_val <= 1 || _val > 100000) ? 1 : _val
        self.updateNum(itemid, _val, productid, _this);
    },

    //提示登录信息
    showLoginWarnDlg: function (btnx, btny) {
        var html = $("#login_tip").html();
        $.dialog({
            title: '提示信息', content: html, lock: true, width: 330, init: function () {

                $(".ui_content input").jbtn();
                $(".ui_content .to_login_btn").on('click', function () {
                    location.href = ctx + "/store/login.html?forward=checkout.html";
                });

                $(".ui_content .to_checkout_btn").on('click', function () {
                    location.href = ctx + "/register.html?forward=checkout.html";
                });
            }
        });
    },

    //删除一个购物项
    deleteGoodsItem: function (itemid) {
        var self = this;
        $.Loading.show("请稍候...");
        $.ajax({
            url: ctx + "/api/store/store-cart/delete.do",
            data: {
                cartid: itemid
            },
            dataType: "json",
            success: function (result) {
                if (result.result == 1) {
                    self.refreshTotal(0);
                    //self.removeItem(itemid);
                } else {
                    $.message.error(result.message);
                }
                $.Loading.hide();
            },
            error: function () {
                $.Loading.hide();
                $.message.error('出现错误，请重试！');
            }
        });
    },

    //移除商品项
    removeItem: function (itemid) {
        $(".border tr[itemid=" + itemid + "]").remove();
    },

    //判断是否有选中的商品项
    hasAnyCheckedItem: function () {
        return $('.storelist input[type="checkbox"][name="product_id"]:checked').length > 0;
    },

    //清空购物车
    clean: function () {
        $.Loading.show("请稍候...");
        var self = this;
        $.ajax({
            url: ctx + "/api/store/store-cart/clean.do",
            dataType: "json",
            success: function (result) {
                $.Loading.hide();
                if (result.result == 1) {
                    location.href = 'cart.html';
                } else {
                    $.message.error("清空失败:" + result.message);
                }
            },
            error: function () {
                $.Loading.hide();
                $.message.error('出现错误，请重试！');
            }
        });
    },

    //更新数量
    updateNum: function (itemid, num, productid, num_input) {
        var self = this;
        $.ajax({
            url: ctx + "/api/store/store-cart/update-num.do?type=" + Math.random(),
            data: "cartid=" + itemid + "&num=" + num + "&productid=" + productid,
            dataType: "json",
            async: false,
            success: function (result) {
                if (result.result == 1) {
                    if (result.data.store >= num) {
                        self.refreshTotal(1);
                        var price = parseFloat($("tr[itemid=" + itemid + "]").attr("price"));
                        price = self.changeTwoDecimal_f(price * num);
                        $("tr[itemid=" + itemid + "] .itemTotal").html("￥" + price);
                        var point = $("tr[itemid=" + itemid + "]").attr("point");
                        if (typeof(point) != "undefined" && point != '') {
                            var html = $("tr[itemid=" + itemid + "] .itemTotal").html();
                            $("tr[itemid=" + itemid + "] .itemTotal").html(html + "+" + parseInt(point) * num + "分")
                        }
                        num_input.val(num);
                    } else {
                        num_input.val(result.data.store);
                        $.message.error("抱歉！您所选选择的货品库存不足。");
                        //使用商品剩余库存
                        //  库存不足时调整为最大库存，并且再次更新价格。  _by: Andste 2016-10-25 11:00:52
                        self.cartNumChanged(num_input);
                    }
                } else {
                    $.message.error(result.message);
                }
            },
            error: function () {
                $.message.error("出错了:(");
            }
        });
    },

    //刷新价格
    refreshTotal: function (type) {
        if (type == 0) {
            location.href = ctx + "/cart.html";
            return false;
        }
        var self = this;
        $.ajax({
            url: ctx + "/cart/cartTotal.html?type=" + Math.random(),
            dataType: "html",
            async: false,
            cache: false,
            success: function (html) {
                $(".total_wrapper").html(html);
            },
            error: function () {
                $.message.error("糟糕，出错了:(");
            }
        });
    },

    //结算页前判断是否有失效商品
    invalidGoods: function (type) {
        $.ajax({
            url: ctx + "/api/store/store-cart/invalid-goods.do",
            dataType: "json",
            success: function (result) {
                if (result.result == 1) {
                    $.message.error("以下商品已下架了，" + result.message + "");
                } else {
                    location.href = "checkout.html";
                }
            },
            error: function () {
                $.message.error("糟糕，出错了:(");
            }
        });
    },

    changeTwoDecimal_f: function (x) {
        var f_x = parseFloat(x);
        if (isNaN(f_x)) {
            alert('参数为非数字，无法转换！');
            return false;
        }
        var f_x = Math.round(x * 100) / 100;
        var s_x = f_x.toString();
        var pos_decimal = s_x.indexOf('.');
        if (pos_decimal < 0) {
            pos_decimal = s_x.length;
            s_x += '.';
        }
        while (s_x.length <= pos_decimal + 2) {
            s_x += '0';
        }
        return s_x;
    },
    checkProduct: function (product) {
        var self = this;
        var store_id = $(product).attr("store_id");
        var checks = $("input[name='product_id'][store_id='" + store_id + "']");
        var n = true;
        for (var i = 0; i < checks.length; i++) {
            if (!checks[i].checked) {
                n = false;
            }
        }

        if (n) {
            $("input[name='select_store'][store_id='" + store_id + "'] ").prop('checked', true);
        } else if (!product.checked) {
            $("input[name='select_store'][store_id='" + store_id + "'] ").prop('checked', false);
        }
        $.ajax({
            url: ctx + "/api/store/store-cart/check-product.do",
            data: {"checked": product.checked, "product_id": $(product).val(), "exchange": $(product).attr("exchange")},
            dataType: "json",
            success: function (result) {
                self.refreshTotal(1);
            },
            error: function () {
                $.message.error("出错了:(");
            }
        });
    },
    checkAllStoreProduct: function (product, callBakc) {
        var self = this;
        var store_id = $(product).attr("store_id");

        if (product.checked) {
            $("input[name='product_id'][store_id='" + store_id + "']").prop('checked', true);
        } else {
            $("input[name='product_id'][store_id='" + store_id + "']").prop('checked', false);
        }
        $.ajax({
            url: ctx + "/api/store/store-cart/check-store-all.do",
            data: {"checked": product.checked, "store_id": store_id},
            dataType: "json",
            success: function (result) {
                self.refreshTotal(1);
                if(callBakc){
                    setTimeout(function () {
                        callBakc()
                    }, 200)
                }
            },
            error: function () {
                $.message.error("出错了:(");
            }
        });
    }
};

$(function () {
    Cart.init();
});