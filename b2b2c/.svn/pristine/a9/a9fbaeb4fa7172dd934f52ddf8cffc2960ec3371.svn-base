var Activity={
		
		/**
		 * 新增促销活动事件绑定
		 */
		bindActivityAddEvent:function(){
			var self = this;
			
			self.bindCommonEvent();
			
			self.bindChangeStyleEvent();
			
		},
		
		/**
		 * 编辑促销活动事件绑定
		 */
		bindActivityEditEvent:function(act_id){
			var self = this;
			
			self.bindCommonEvent();
			
			self.bindChangeStyleEvent();
			
			self.bindSelectedCancelEvent();
			
			//如果优惠方式的减现金复选框是选中状态
			if($("input[name='is_full_minus']").attr("checked")){
				$(".fullMinus").css('display','none');
				$(".minusInput").css('display','inline-block');
			}else{
				$("#minusValue").val("");
				$("input[name='minus_value']").val("");
			}
			
			//如果优惠方式的送积分复选框是选中状态
			if($("input[name='is_send_point']").attr("checked")){
				$(".sendPoint").css('display','none');
				$(".pointInput").css('display','inline-block');
			}else{
				$("#pointValue").val("");
				$("input[name='point_value']").val("");
			}
			
			//如果优惠方式的送赠品复选框是选中状态
			if($("input[name='is_send_gift']").is(":checked")){
				$(".sendGift").css('display','none');
				$("input[name='is_send_gift']").css('margin','0 0 0 169px');
				$(".giftAll").css('display','inline-block');
			}
			
			//如果优惠方式的送优惠券复选框是选中状态
			if($("input[name='is_send_bonus']").is(":checked")){
				$(".sendBonus").css('display','none');
				$("input[name='is_send_bonus']").css('margin','0 0 0 159px');
				$(".bonusAll").css('display','inline-block');
			}
			
		},
		
		/**
		 * 选择商品公共事件绑定
		 */
		bindCommonEvent:function(){
			var self = this;
			
			//优惠方式减现金复选框点击操作
			$("input[name='is_full_minus']").click(function(){
				if($(this).is(":checked")){
					$(".fullMinus").css('display','none');
					$(".minusInput").css('display','inline-block');
				}else{
					$(".minusInput").css('display','none');
					$(".fullMinus").css('display','inline-block');
					$("#minusValue").val("");
					$("input[name='minus_value']").val("");
				}
			});
			
			//优惠方式送积分复选框点击操作
			$("input[name='is_send_point']").click(function(){
				if($(this).is(":checked")){
					$(".sendPoint").css('display','none');
					$(".pointInput").css('display','inline-block');
				}else{
					$(".pointInput").css('display','none');
					$(".sendPoint").css('display','inline-block');
					$("#pointValue").val("");
					$("input[name='point_value']").val("");
				}
			});
			
			//优惠方式送赠品复选框点击操作
			$("input[name='is_send_gift']").click(function(){
				if($(this).is(":checked")){
					$(".sendGift").css('display','none');
					$("input[name='is_send_gift']").css('margin','0 0 0 169px');
					$(".giftAll").css('display','inline-block');
				}else{
					$(".giftAll").css('display','none');
					$("input[name='is_send_gift']").css('margin','0 0 0 0');
					$(".sendGift").css('display','inline-block');
					$(".giftId").val(0);
					$(".giftId option[value='0']").attr("selected",true);
				}
			});
			
			//优惠方式送优惠券复选框点击操作
			$("input[name='is_send_bonus']").click(function(){
				if($(this).is(":checked")){
					$(".sendBonus").css('display','none');
					$("input[name='is_send_bonus']").css('margin','0 0 0 159px');
					$(".bonusAll").css('display','inline-block');
				}else{
					$(".bonusAll").css('display','none');
					$("input[name='is_send_bonus']").css('margin','0 0 0 0');
					$(".sendBonus").css('display','inline-block');
					$(".bonusId").val(0);
					$(".bonusId option[value='0']").attr("selected",true);
				}
			});
			
			//添加参加活动商品
			$(".addGoods").click(function(){
				var g_id = "tr_" + $(this).attr("goodsId");
				$(this).css('display','none');
				$("."+g_id+" .cancelGoods").css('display','block');
				$("."+g_id).clone(true).appendTo(".goodslist_selected tbody");
				$(".goodslist_selected tbody ."+g_id+" .goodsid").attr("name","goods_id");
				$(".selected .no_blank").css('display','none');
			});
			
			//取消参加活动商品
			$(".cancelGoods").click(function(){
				var g_id = "tr_" + $(this).attr("goodsId");
				$(".goodslist_bystore tbody ."+g_id+" .cancelGoods").css('display','none');
				$("."+g_id+" .addGoods").css('display','block');
				$(".goodslist_selected tbody ."+g_id).remove();
				
				var len = $(".goodslist_selected tbody tr").length;
				if(len == 0){
					$(".selected .no_blank").css('display','block');
				}
			});
			
			//异步分页点击操作
			$(".all").on("click",".unselected",function(){
				var page = $(this).attr("page");
				$.ajax({
					url:"select_goods.html?page="+page,
					dataType:"html",
					success:function(result){
						$(".goodsAll .all").html(result);
						
						self.bindSelectedCancelEvent();
						
						self.bindAddCancelGoodsEvent();
					},
					error:function(){
						$.alert("出错了:(");
					}
				});	
				
			});
			
			//选择商品页面搜索操作
			$(".all").on("click","#btnSearch",function(){
				var keyword = $("input[name='keyword']").val();
				$.ajax({
					url:"select_goods.html?keyword="+keyword,
					dataType:"html",
					success:function(result){
						$(".goodsAll .all").html(result);
						
						self.bindSelectedCancelEvent();
						
						self.bindAddCancelGoodsEvent();
					},
					error:function(){
						$.alert("出错了:(");
					}
				});	
			});
			
			//新增促销活动：已选商品选项卡点击操作
			$(".partList").click(function(){
				$(".goodsTable .pngFix li").removeClass("active");
				$(".goodsTable .pngFix li").addClass("normal");
				$(this).removeClass("normal");
				$(this).addClass("active");
				$(".goodsTable .goodsAll .part").show();
				$(".goodsTable .goodsAll .all").hide();
			});
			
			//新增促销活动：选择商品选项卡点击操作
			$(".allList").click(function(){
				$(".goodsTable .pngFix li").removeClass("active");
				$(".goodsTable .pngFix li").addClass("normal");
				$(this).removeClass("normal");
				$(this).addClass("active");
				$(".goodsTable .goodsAll .part").hide();
				$(".goodsTable .goodsAll .all").show();
			});
			
		},
		
		/**
		 * 改变页面样式事件绑定
		 */
		bindChangeStyleEvent:function(){
			var self = this;
			
			//如果全部商品参与单选框为选中状态，就将商品的tab页隐藏
			if($("#all").is(":checked")){
				$(".goodsTable").css('display','none');
			}
			
			//如果部分商品参与单选框为选中状态，就将商品的tab页显示
			if($("#part").is(":checked")){
				$(".goodsTable").css('display','block');
			}
			
			//全部商品参加单选框点击操作
			$("#part").click(function(){
				$(".goodsTable").css('display','block');
			});
			
			//部分商品参加单选框点击操作
			$("#all").click(function(){
				$(".goodsTable").css('display','none');
			});
			
			//给优惠条件减多少钱的input框添加失去焦点 (blur)事件
			$("#minusValue").blur(function () {
				//如果当前的input框的值不为空，就将前面的复选框设置为选中状态
				if ($(this).val() != "") {
					$("input[name='is_full_minus']").attr("checked", true);
				}
			});
			
			//给优惠条件送多少积分的input框添加失去焦点 (blur)事件
			$("#pointValue").blur(function () {
				//如果当前的input框的值不为空，就将前面的复选框设置为选中状态
				if ($(this).val() != "") {
					$("input[name='is_send_point']").attr("checked", true);
				}
			});
		},
		
		/**
		 * 新增促销活动页面：添加活动商品和取消活动商品点击事件绑定
		 */
		bindAddCancelGoodsEvent:function(){
			var self = this;
			//添加参加活动商品
			$(".addGoods").click(function(){
				var g_id = "tr_" + $(this).attr("goodsId");
				$(this).css('display','none');
				$("."+g_id+" .cancelGoods").css('display','block');
				$("."+g_id).clone(true).appendTo(".goodslist_selected tbody");
				$(".goodslist_selected tbody ."+g_id+" .goodsid").attr("name","goods_id");
				$(".selected .no_blank").css('display','none');
			});
			
			//取消参加活动商品
			$(".cancelGoods").click(function(){
				var g_id = "tr_" + $(this).attr("goodsId");
				$(".goodslist_bystore tbody ."+g_id+" .cancelGoods").css('display','none');
				$("."+g_id+" .addGoods").css('display','block');
				$(".goodslist_selected tbody ."+g_id).remove();
				
				var len = $(".goodslist_selected tbody tr").length;
				if(len == 0){
					$(".selected .no_blank").css('display','block');
				}
			});
		},
		
		/**
		 * 遍历已选商品页面的取消参加活动商品事件绑定
		 */
		bindSelectedCancelEvent:function(){
			var self = this;
			$(".goodslist_selected .cancelGoods").each(function(){
				var selected = $(this).attr("goodsId");
				if(selected != ""){
					self.bindAddCancelEvent(selected);
				}
			});
		},
		
		/**
		 * 遍历选择商品页面取消参加活动商品事件绑定
		 * @param selected
		 */
		bindAddCancelEvent:function(selected){
			var self = this;
			$(".goodslist_bystore .cancelGoods").each(function(){
				var select = $(this).attr("goodsId");
				if(selected == select){
					$(this).css('display','block');
					$(".goodslist_bystore .tr_"+select+" .addGoods").css('display','none');
				}
			});
		}
};