$(function(){
	$(".store_intro_upimg").mouseover(function(){
		$(this).find("#storeLogoForm").show();
	})
	$(".store_intro_upimg").mouseleave(function(){
		$(this).find("#storeLogoForm").hide();
	})
	$("#storeLogo").change(function(){
		var options = {
			url : ctx + "/api/base/upload-image/upload.do?subFolder=storeLogo",
			type : "POST",
			dataType:"json",
			success : function(data) {
				if(data.fsimg!=null){
					$.ajax({
				    	type:"POST",
				    	url: ctx + "/api/b2b2c/store-api/edit-store-logo.do?logo="+data.fsimg ,
				        dataType: "json",
				        success: function(result){
				        	if(result.result==1){
								$("#logo").attr("src",data.img);
								$.message.success('修改成功！', 'reload');
				        	}else{
								$.message.error(result.message);
				        	}
				        },error:function(e){
							$.message.error("出现错误，请重试！");
				        }
			    	});
				}else{
					$.message.error(data.message);
				}
		 	},
		 	error : function(e) {
		 		$.message.error('出现错误，请重试');
			}
		};
		$("#storeLogoForm").ajaxSubmit(options);	
	});
})