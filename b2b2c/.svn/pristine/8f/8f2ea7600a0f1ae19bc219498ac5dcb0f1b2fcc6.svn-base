
var CommentList={
		init:function(goodsid){
			var self  = this;		
		//	self.bindAllPageEvent();
			
			$("#discuss_wrapper").load(ctx+"/detail/discuss_list.html?goodsid="+goodsid,function(){
				Grade.init();
			});


			$("#ask_wrapper").load(ctx+"/detail/ask_form.html?goodsid="+goodsid);


			$("#goods_transaction").load(ctx+"/detail/transaction.html?goodsid="+goodsid)

		 	self.bindTabEvent();
		 	
		 	
		},
		
		/**
		 * 评论分页点击事件绑定
		 */
		bindDiscussPageEvent:function(){
			var self = this;
			$("#discuss_list_wrapper .page a.unselected").click(function(){
				var page = $(this).attr("page");
				self.loadDiscussList(page);
				return false;
			});
		},
		
		/**
		 * 评论分页点击事件绑定
		 */
		bindAskPageEvent:function(){
			var self = this;
			$("#ask_list_wrapper .page a.unselected").click(function(){
				var page = $(this).attr("page");
				self.loadAskList(page);
				return false;
			});
		},
		
		bindTabEvent:function(){
			//选 项卡切换
			$("#comment_tab li.discuss").click(function(){
				$("#discuss_wrapper").show();
				$("#ask_wrapper").hide();
				$("#comment_tab li").removeClass("selected");
				$(this).addClass("selected");
			});

			$("#comment_tab li.ask").click(function(){
				$("#discuss_wrapper").hide();
				$("#ask_wrapper").show();
				$("#comment_tab li").removeClass("selected");
				$(this).addClass("selected");
			});
		}
	};
	

/**
 * 评论表单提交
 */
var dialog;
var CommentForm={
	 
	formSubmit:function(btn,form){
		$("#saveBtn").attr({"disabled":"disabled"});
		$.Loading.show("正在提交请稍候...");
		var options = { 
				url : ctx+"/api/b2b2c/store-comment-api/add.do",
				type : "POST",
				cache:false,
				dataType : "json",
				success : function(result) {
					$.Loading.hide();
	 				if(result.result==1){
						 $.message.success("发表成功", function(){
								location.reload();		
						 });
						 form.get(0).reset();
			 		}else{
			 			$.message.error(result.message);
				 	}
				},
				error : function(e) {
					$.message.error("出现错误1 ，请重试");
					btn.attr("disabled",false); 
				}
		};
		form.ajaxSubmit(options);
	},
	commentFormSubmit:function(btn,form){
		$("#saveBtn").attr({"disabled":"disabled"});
		$.Loading.show("正在提交请稍候...");
		var options = { 
				url : ctx+"/api/b2b2c/store-comment-api/addComment.do",
				type : "POST",
				cache:false,
				dataType : "json",
				success : function(result) {
					$.Loading.hide();
					if(result.result==1){
						$.message.success("发表成功", function(){
							location.reload();		
						});
						form.get(0).reset();
					}else{
						$.message.error(result.message);
					}
				},
				error : function(e) {
					$.message.error("出现错误2 ，请重试");
					btn.attr("disabled",false); 
				}
		};
		form.ajaxSubmit(options);
	}
};


/**
 * 评价
 */
var Grade={
		clicked:false,	
		init:function(){
			
			var self = this;
			$("#discuss_form_wrapper .grade a").hover(
				function(){
					self.hover($(this));
				}
			);

			$("#discuss_form_wrapper .grade a").click(function(){
				 self.clicked=true;
				 var index = parseInt($(this).text());
				 self.selectced(index,$(this).attr("status"));
			});
			
			$("#discussBtn").click(function(){
				CommentForm.commentFormSubmit($(this),$("#discussForm"));
			});
			
		},
		hover:function(curritem){
			var status=$(curritem).attr("status");
			$("#discuss_form_wrapper .grade a[status="+status+"]").slice(index-1,5).removeClass("selected");
			this.clicked= false;
			var index = parseInt(curritem.text());
			this.selectced(index,status);
			 
		},
		selectced:function(index,status){
			$("#discuss_form_wrapper .grade a[status="+status+"]").slice(0,index).addClass("selected");
			$("span[name='gradevalue'][status="+status+"]").html(index);
			$("#"+status).val(index);
		}
	};