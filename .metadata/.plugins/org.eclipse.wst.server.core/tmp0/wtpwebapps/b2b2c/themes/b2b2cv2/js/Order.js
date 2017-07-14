var Order = {
    init: function () {
        var self = Order;
        //取消订单
        $(".cancelBtn").click(function () {
            var orderId = $(this).attr("orderid");
            var sn = $(this).attr("sn");
            var html = $("#cancelForm").html();
            var ordersn = $.trim("<div class='order_delsn'><span>订单号：</span><p>" + sn + "</p></div>");
            var dialog = $.dialog({
                title: "取消订单",
                content: ordersn + html,
                lock: true
            })
            $(".ui_content .yellow_btn").jbtn().click(function () {
                var reason = $("select[name='reason'] option:selected").get(1).value;
                $.Loading.show("正在取消您的订单，请稍候...");
                $('.yellow_btn').attr('disabled', "true");
                $.ajax({
                    url: ctx + "/api/shop/order/cancel.do?order_id=" + orderId + "&reason=" + reason,
                    dataType: "json",
                    success: function (res) {
                        if (res.result == 1) {
                            alert(res.message);
                            $('.yellow_btn').removeAttr("disabled");
                            window.location.reload();
                        } else {
                            $.alert(res.message);
                            $('.yellow_btn').removeAttr("disabled");
                        }
                    }
                });
            });
        });

      //查看线下支付银行卡信息
		$(".payCfg").click(function(){
			$.ajax({
			    url: ctx + '/member/order-pcfg.html',
			    type: 'GET',
			    success: function(html){
			        $.dialogModal({
			            title: '银行信息',
			            btn : false,
			            html : html,
			        });
			    },
			    error: function(){
			    	alert('出现错误，请重试！')
			    }
			});
		});
        
        //确认收货
        $(".rogBtn").click(function () {
            var orderId = $(this).attr("orderid");
            if (confirm("请您确认已经收到货物再执行此操作！")) {
                $.Loading.show("请稍候...");
                $.ajax({
                    url: ctx + "/api/shop/order/rog-confirm.do?orderId=" + orderId,
                    dataType: "json",
                    success: function (result) {
                        if (result.result == 1) {
                            location.reload();
                        }else {
                            $.Loading.hide();
                            $.alert(result.message);
                        }
                    },
                    error: function () {
                        $.alert("出错了:(");
                    }
                });
            }
        });
        //解冻积分
        $(".thawBtn").click(function () {
            var orderid = $(this).attr("orderid");
            $.confirm("<div class='thaw_order' style='width:450px;'>提前解冻积分后，被冻结积分相关的订单商品，将不能进行退换货操作。确认要解冻吗？</div>",
                function () {
                    $.Loading.show("请稍候...");
                    $.ajax({
                        url: ctx + "/api/shop/returnorder/thaw.do?orderid=" + orderid,
                        dataType: "json",
                        cache: false,
                        success: function (result) {
                            if (result.result == 1) {
                                location.reload();
                            } else {
                                $.Loading.hide();
                                $.alert(result.message);
                            }
                        }, error: function () {
                            $.Loading.hide();
                            $.alert("抱歉，解冻出错现意外错误");
                        }
                    });
                }
            );
        });
    }
}