/**
 * Created by Andste on 2016/6/21.
 */
$(function(){

    //  初始化提示工具
    $('.filter-blurry').find('input').tooltip();

    //  展开精确搜索
    (function(){
        var btn = $('#exact_seach'), blurry = $('.filter-blurry');
        btn.unbind('click').on('click', function(){
            var _this = $(this);
            if($('.exact-seach').is('.show')){
                exactBox('hide');
            }else {
                exactBox('show');
            };
        });
    })();

    //  搜索
    (function(){
        var btn = $('#filter_seach');
        btn.unbind('click').on('click', function(){
            goSseach();
        });

        $('.filter-box').find('input').keyup(function(event){
            if (event.keyCode == 13) {
                goSseach();
            };
        });
    })();

    //  取URL中传参赋值给搜索框，以及其他操作
    (function(){
        var searchcontent = GetQueryString('searchcontent'),
            replyStatus = GetQueryString('replyStatus'),
            keyWord = GetQueryString('keyword'),
            stype = GetQueryString('stype'),
            grade = GetQueryString('grade'),
            mname = GetQueryString('mname'),
            gname = GetQueryString('gname');
        if(keyWord){
            $('#filter_blurry').val(keyWord);
        };
        if(mname){
            $('#filter_blurry').val(mname);
        };
        if(gname){
            $('#filter_good_name').val(gname);
        };
        if(searchcontent){
            $('#filter_comment').val(searchcontent);
        };
        if(grade){
            $('#filter_grade').val(grade);
        };
        if(replyStatus){
            $('#filter_reply_status').val(replyStatus);
        };

        if(stype == 1){
            exactBox('show');
        }else {
            exactBox('hide');
        };
    })();

    /* 局部function
     ============================================================================ */
    //  搜索功能
    function goSseach(){
        var exactStatus = $('.exact-seach').is('.show'), keyWord = $.trim($('#filter_blurry').val());
        if(/[`~!@#\$%\^\&\*\(\)_\+<>\?:"\{\},\.\\\/;'\[\]]/im.test(keyWord)){
            $.message.error('关键字中不能包含特殊字符！');
            return false;
        };
        if(exactStatus){  //  如果为true，说明是精确搜索，否则为模糊搜索
            var mname = keyWord, gname = $('#filter_good_name').val(), searchcontent = $('#filter_comment').val(), grade = $('#filter_grade').val(), replyStatus = $('#filter_reply_status').val();
            location.href = './evaluation.html?stype=1&type=1&mname='+ mname +'&gname='+ gname +'&searchcontent='+ searchcontent +'&replyStatus='+ replyStatus +'&grade='+ grade;
        }else {
            if(!keyWord && !GetQueryString('keyword')){
                location.href = './evaluation.html';
            }else {
                location.href = './evaluation.html?stype=0&type=1&keyword='+ keyWord;
            };
        };
    };

    //  显示、隐藏精确搜索
    function exactBox(_str){
        var blurry = $('.filter-blurry'), innerApp = $('.inner-app-wappler'), tools = $('.filter-tools');
        if(_str == 'show'){
            $('.exact-seach').addClass('show');
            $('#exact_seach').html('模糊搜索');
            blurry.find('span').html('会员名称：');
            blurry.find('input').tooltip('destroy');
            innerApp.css({minWidth: 1200});
            tools.css({minWidth: 1173});
        }else {
            $('.exact-seach').removeClass('show');
            $('#exact_seach').html('精确搜索');
            blurry.find('span').html('模糊搜索：');
            blurry.find('input').tooltip();
            innerApp.css({minWidth: 1020});
            tools.css({minWidth: ''});
        };
    };

    //  回复弹窗
    (function(){
        var btn = $('.reply');
        btn.unbind('click').on('click', function(){
            var _this = $(this), commentId = _this.attr('commentId');
            $.ajax({
                url: './evaluation_reply.html?type=2&comment_id='+ commentId,
                type: 'POST',
                success: function(html){
                    $.dialogModal({
                        title: '回复评论',
                        html : html,
                        callBack: function(){
                            if(!$.trim($('#dialogModal').find('textarea').val())){
                                $.message.error('回复内容不能为空！');
                                return false;
                            }else {
                                var options = {
                                    url: ctx + '/api/b2b2c/store-comment-api/edit.do',
                                    type: 'POST',
                                    success: function(result){
                                        if(result.result == 1){
                                            $.message.success('回复成功！', 'reload');
                                        }else {
                                            $.message.error(result.message);
                                            return false;
                                        };
                                    },
                                    error: function(){
                                        $.message.error('出现错误，请重试！');
                                    }
                                };

                                $('#dialogModal').find('#replyForm').ajaxSubmit(options);
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
});