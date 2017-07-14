/**
 * Created by Andste on 2016/6/22.
 */
$(function(){

    //  新增导航
    (function(){
        var addBtn = $('.nav-add'), sort = $('.sort-num'), sortLen = sort.length;
        addBtn.unbind('click').on('click', function(){
            var tempArray = [], maxNum = 0;
            for(var i = 0; i < sortLen; i++){
                tempArray.push(parseInt(sort.eq(i).html()));
            };
            maxNum = Math.max.apply(Math, tempArray);
            $.ajax({
                url: './navigation_add.html',
                type: 'GET',
                success: function(html){
                    $.dialogModal({
                        title: '新增导航',
                        html : html,
                        showCall: function(){
                            setTimeout(function(){
                                isNumber(maxNum) ? $('.nav-sort').find('input').val(maxNum+1) : null;
                            }, 200)
                            if(Sys.ie > 7){$('.nav-form input').css({height: 30})}
                        },
                        callBack: function(){
                            var name = $('.nav-name').find('input'), sort = $('.nav-sort').find('input');
                            if(!$.trim($('.nav-name').find('input').val())){
                                name.addClass('error').css({border: '1px solid #a94442'});
                            }else {
                                name.removeClass('error').css({border: ''});
                            };
                            if(!/^0$|^\+?[1-9]\d*$/.test(sort.val())){
                                sort.addClass('error').css({
                                    border: '1px solid #a94442',
                                    color : '#a94442'
                                });
                            }else {
                                sort.removeClass('error').css({
                                    border: '',
                                    color : ''
                                });
                            };
                            if($('#nav_add_form input').is('.error')){
                                return false;
                            }else {
                                addNav();
                            };
                            function addNav(){
                                var options = {
                                    url : ctx + '/api/b2b2c/navigation/add.do',
                                    type : 'POST',
                                    dataType : 'json',
                                    success : function(result) {
                                        if (result.result == 1) {
                                            $.message.success('保存成功！', 'reload');
                                        }else{
                                            $.message.error(result.message);
                                            return false;
                                        };
                                    },
                                    error : function() {
                                        $.message.error('出现错误，请重试！');
                                    }
                                };
                                $('#nav_add_form').ajaxSubmit(options);
                            }
                        }
                    });
                },
                error: function(){
                    $.message.error('出现错误，请重试！');
                }
            });
        });
    })();

    //  编辑导航
    (function(){
        var editBtn = $('.nav-edit');
        editBtn.unbind('click').on('click', function(){
            var _this = $(this), nav_id = _this.attr('nav_id');
            $.ajax({
                url: './navigation_edit.html?nav_id='+ nav_id,
                type: 'GET',
                success: function(html){
                    $.dialogModal({
                        title: '编辑导航',
                        html : html,
                        showCall: function(){
                            if(Sys.ie > 7){$('.nav-form input').css({height: 30})}
                        },
                        callBack: function(){
                            var name = $('.nav-name').find('input'), sort = $('.nav-sort').find('input');
                            if(!$.trim($('.nav-name').find('input').val())){
                                name.addClass('error').css({border: '1px solid #a94442'});
                            }else {
                                name.removeClass('error').css({border: ''});
                            };
                            if(!/^0$|^\+?[1-9]\d*$/.test(sort.val())){
                                sort.addClass('error').css({
                                    border: '1px solid #a94442',
                                    color : '#a94442'
                                });
                            }else {
                                sort.removeClass('error').css({
                                    border: '',
                                    color : ''
                                });
                            };
                            if($('#nav_edit_form input').is('.error')){
                                return false;
                            }else {
                                editNav();
                            };
                            function editNav(){
                                var options = {
                                    url : ctx + '/api/b2b2c/navigation/edit.do',
                                    type : 'POST',
                                    dataType : 'json',
                                    success : function(result) {
                                        if (result.result == 1) {
                                            $.message.success('修改成功！', 'reload');
                                        }else{
                                            $.message.error(result.message);
                                            return false;
                                        };
                                    },
                                    error : function() {
                                        $.message.error('出现错误，请重试！');
                                    }
                                };
                                $('#nav_edit_form').ajaxSubmit(options);
                            };
                        }
                    });
                },
                error: function(){
                    $.message.error('出现错误，请重试！');
                }
            });
        });
    })();

    //  删除导航
    (function(){
        var delBtn = $('.nav-delete');
        delBtn.unbind('click').on('click', function(){
            var _this = $(this), nav_id = _this.attr('nav_id');
            $.confirm('确定要删除这个导航吗？', function(){
                $.ajax({
                    url : ctx + '/api/b2b2c/navigation/delete.do?nav_id='+ nav_id,
                    type : "POST",
                    dataType : 'json',
                    success : function(result) {
                        if (result.result == 1) {
                            $.message.success('删除成功！', 'reload');
                        }else{
                            $.message.error(result.message);
                        };
                    },
                    error : function() {
                        $.message.error('出现错误，请重试！');
                    }
                });
            });
        });
    })();

});