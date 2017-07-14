$(function(){
	bindFileEvent($("#license_img"));
 	bindFileEvent($("#id_img"));
 	
 	$("#update").click(function(){
		var options = {
				url : ctx + "/api/b2b2c/store-api/store-auth.do",
				type : "POST",
				dataType : 'json',
				success : function(result) {	
					if(result.result == 1){
						$.message.success("提交成功，等待审核！", 'reload');
					}else{
						$.message.error(result.message);
					};
				},
				error : function(e) {
					$.message.error("出现错误 ，请重试");
				}
			};
		$("#StoreAuthForm").ajaxSubmit(options);
	});
});

function bindFileEvent(obj){
	var status=$(obj).attr("status");
	var text=$(obj).attr("text");
	$(obj).uploadify({
		'buttonText':text,		//显示文字
		'fileObjName':'image',		//文件对象名称
									//上传文件大小限制 'fileSizeLimit':'100KB',
		'fileTypeDesc': '请选择',//允许上传的文件类型的描述，在弹出的文件选择框里会显示 
		'fileTypeExts': '*.gif; *.jpg; *.png',//允许上传的文件类型，限制弹出文件选择框里能选择的文件 
		'uploader' : ctx + '/api/base/upload-image/upload.do?subFolder=store',
		'swf'      : ctxPath + '/uploadify.swf',
		'height':'30',				//高度
		'width':'80',
		'multi':false,				//是否支持多文件上传
		'progressData':'percentage',//设置文件上传时显示的数据
		'onFallback':function(){				//flash兼容
			$.message.error("抱歉，请检查是否安装flash！");
		},							
		'onUploadSuccess':function(file,data,response){
			var img =jQuery.parseJSON(data);
			$("#fs_"+status).val(img.fsimg);
			$("#img_"+status).attr("src",img.img);
			if(status=="id_img"){
				$("input[name='name_auth']").val(2);
			}else{
				$("input[name='store_auth']").val(2);
			}
		},
		'onSelectError':function(file,errorCode,errorMsg){
			if(errorCode==SWFUpload.QUEUE_ERROR.INVALID_FILETYPE){
				this.queueData.errorMsg="请上传正确的格式";
			}
		}
	});
}