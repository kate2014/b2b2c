var ShortMsg={
		init:function(){
			var self = this;
			self.checkNewMsg();
			function check(){
				self.checkNewMsg();
			}
			
		},
		//对获取的数据进行操作
		loadNewMessage:function(msgList){
			/*var box =$(".msglist").empty();
			$.each(msgList,function(i,msg){
				box.append("<li><span class='name'><a class='mes' href='javascript:void(0);' onclick='addTab1(\""+msg.title+"\",\".."+msg.url+"\")' >"+msg.content+"</a></span></li>");
			});*/
			
				$('#msglist').attr("class","layui-nav-child");	
			
			for(var i =0;i<msgList.length;i++){
				var dd=$('<dd><a href="javascript:void(0)" data-tab="true" data-url="'+msgList[i].url+'" ><cite>'+msgList[i].content+'</cite></a></dd>');
				$("#msglist").append(dd);
			}
			/*绑定消息的点击事件*/
			 $(".news").on('click','dl dd',function(){
				 var index = $(this).index();
				 newTab(msgList[index].title,' ',"../"+msgList[index].url)
             })
		
		},
		/**
		 * 检测是否有新消息
		 */
		checkNewMsg:function(){
			var self= this;
			$.ajax({
				url:'../core/admin/short-msg/list-new.do',
				dataType:'json',
				cache:false,
				success:function(result){
					
					if(result.result==1){
						var data= result.data;
						var count = data.length;
						if(count>0){
							$(".msg_num").text(count);
							$(".msglist").find("dd").remove();
							$(".msg_num").show();
						}else{
							$(".msg_num").hide();
							$(".msglist").hide();
							$('#msglist').remove("class","layui-nav-child");
						}
						self.loadNewMessage(data);
						
					}else{
						//api返回错误
					}
					
					
				},
				error:function(){
				}
			});
		}

};

$(function(){
	ShortMsg.init();
});