$(function () {
    var ue = UE.getEditor('editor');
    UE.Editor.prototype._bkGetActionUrl = UE.Editor.prototype.getActionUrl;
    UE.Editor.prototype.getActionUrl = function(action) {
        if (action == 'uploadimage' || action == 'uploadscrawl' || action == 'uploadimage') {
            return ctx+"/api/base/upload-image/upload-img.do";
        } else {
            return this._bkGetActionUrl.call(this, action);
        };
    };

    //点击保存按钮提交表单
    $('#update').unbind('click').on('click', function () {
    	//没有错误信息，可能是鼠标没有移至填写位置
    	var input = $('.setting-box').find("input[type='text']");
    	input.each(function(){
    		var _this = $(this), val = _this.val(), span = _this.siblings('span');
    		if(!val){
                err(_this, 'error');
                span.html('此项必填！').css({display: 'inline-block'});
                return;
            }else {
                err(_this, 'succe');
                span.html('').css({display: 'none'});
            };
    	})
    	
		var regionId = $("#region_id").val();
		var cityId = $("#city_id").val();
		var provinceId = $("#province_id").val();
		//校验地区最后一级是否为选中“不能是请选择提示”
		if(regionId == "0"){
			$.message.error("地址信息不完全");
			return false;
		}
		if(cityId == "0"){
			$.message.error("地址信息不完全");
			return false;
		}
		if(provinceId == "0"){
			$.message.error("地址信息不完全");
			return false;
		}
        var options = {
            url: ctx + "/api/b2b2c/store-api/edit.do",
            type: 'POST',
            dataType: 'json',
            success: function (result) {
                if (result.result == 1) {
                    $.message.success('保存成功！', 'reload');
                } else {
                    $.message.error(result.message);
                };
            },
            error: function () {
                $.message.error("出现错误 ，请重试");
            }
        };
        if($('.setting-box').find('.error').length > 0){
            $.message.error('表单填写有误，请检查！');
            return false;
        }else {
            $("#storeInfo").ajaxSubmit(options);
        };
    });

    //  各输入框校验
    (function(){
        var input = $('.setting-box').find("input[type='text']");
        input.blur(function(){
            var _this = $(this), val = _this.val(), span = _this.siblings('span');
            if(!val){
                err(_this, 'error');
                span.html('此项必填！').css({display: 'inline-block'});
                return;
            }else {
                err(_this, 'succe');
                span.html('').css({display: 'none'});
            };

            if(_this.parent().is('.addr_check')){
                if(!val){
                    err(_this, 'error');
                    span.html('详细地址不能为空！').css({display: 'inline-block'});
                }else if(/[`~!@#\$%\^\&\*\(\)_\+<>\?:"\{\},\.\\\/;'\[\]]/im.test(val)){
                    err(_this, 'error');
                    span.html('不能包含特殊字符！').css({display: 'inline-block'});
                }else {
                    err(_this, 'succe');
                    span.html('').css({display: 'none'});
                };
            }else if(_this.parent().is('.zip_check')){
                if(!/^[0-9][0-9]{5}$/.test(val)){
                    err(_this, 'error');
                    span.html('邮编格式有误！').css({display: 'inline-block'});
                }else {
                    err(_this, 'succe');
                    span.html('').css({display: 'none'});
                };
            }else if(_this.parent().is('.tel_check')){
                if(!/^1[3|4|5|7|8]\d{9}$/.test(val)){
                    err(_this, 'error');
                    span.html('手机格式有误！').css({display: 'inline-block'});
                }else {
                    err(_this, 'succe');
                    span.html('').css({display: 'none'});
                };
            }else if(_this.parent().is('.qq_check')){
                if(!/^[1-9][0-9]{4,}$/.test(val)){
                    err(_this, 'error');
                    span.html('QQ号格式有误！').css({display: 'inline-block'});
                }else {
                    err(_this, 'succe');
                    span.html('').css({display: 'none'});
                };
            };
        });
    })();

    //  地区选择校验
    (function(){
        var province_id = $('#province_id'), city_id = $('#city_id'), region_id = $('#region_id');
        $('.part_check').find('select').change(function(){
            var _this = $(this), val = _this.val(), border = '';
            if(Sys.ie){
                border = '1px solid #cccccc';
            };
            if(val == 0){
                _this.css({
                    border: '1px solid #a94442',
                    color : '#a94442'
                }).addClass('error');
                _this.nextAll('select').css({
                    border: '1px solid #a94442',
                    color : '#a94442'
                }).addClass('error');
            }else {
                _this.css({
                    border: border,
                    color : ''
                }).removeClass('error');
            };
			if ($('.part_check').find('.error').length > 0){
                _this.parent().siblings('.part_erro').html('店铺地址不能为空！').css({display: 'inline-block'});
            }else {
                _this.parent().siblings('.part_erro').html('').css({display: 'none'});
            };
        });
    })();

    function err(node, str){
        var border = '';
        if(Sys.ie){
            border = '1px solid #cccccc';
        };
        if(str == 'error'){
            if(Sys.ie){
                node.css({
                    border: '1px solid #a94442',
                    color : '#a94442'
                }).addClass('error');
            }else {
                node.addClass('error');
                node.parent().addClass('has-error');
            };
        }else {
            if(Sys.ie){
                node.css({
                    border: border,
                    color : ''
                }).removeClass('error');
            }else {
                node.removeClass('error');
                node.parent().removeClass('has-error').addClass('has-success');
            };
        };
    };

    window.onload = function () {
        bindFileEvent($("#store_logo"));
        bindFileEvent($("#store_banner"));
    };

    //  修复按钮样式
    (function(){
        if(Sys.ie < 9){
            $('.to_save').css({height: '30px'});
        };
    })();
});

//加验证上传文件格式是否正确
function bindFileEvent(obj) {
    var status = $(obj).attr("status");
    var text = $(obj).attr("text");
    $(obj).uploadify({
        'buttonText': text,		//显示文字
        'fileObjName': 'image',		//文件对象名称
        //上传文件大小限制 'fileSizeLimit':'100KB',
        'fileTypeDesc': '请选择',//允许上传的文件类型的描述，在弹出的文件选择框里会显示
        'fileTypeExts': '*.gif; *.jpg; *.png',//允许上传的文件类型，限制弹出文件选择框里能选择的文件
        'uploader': ctx + '/api/base/upload-image/upload.do?subFolder=store',
        'swf': ctxPath + '/uploadify.swf',
        'height': '30',				//高度
        'width': '80',
        'multi': false,				//是否支持多文件上传
        'progressData': 'percentage',//设置文件上传时显示的数据
        'uploadLimit': 5,
        'onFallback': function () {				//flash兼容
            $.message.error("抱歉，请检查是否安装flash！");
        },
        'onUploadSuccess': function (file, data, response) {
            var img = jQuery.parseJSON(data);
            $("#fs_" + status).val(img.fsimg);
            $("#img_" + status).attr("src", img.img);
        },
        'onSelectError': function (file, errorCode, errorMsg) {
            if (errorCode == SWFUpload.QUEUE_ERROR.INVALID_FILETYPE) {
                this.queueData.errorMsg = "请上传正确的格式!";
            }
        }
    });
}

