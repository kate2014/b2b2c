$(function () {

    //  咨询回复
    (function () {
        var btn = $('a.reply');
        btn.unbind('click').on('click', function () {
            var _this = $(this), comment_id = _this.attr('comment_id');

            $.ajax({
                url: "./consult_reply.html?comment_id=" + $(this).attr("comment_id") + "&type=2",
                type: 'GET',
                success: function (html) {
                    openDialog(html);
                },
                error: function () {
                    $.message.error('出现错误，请重试！');
                }
            });

            function openDialog(html) {
                $.dialogModal({
                    title: '回复咨询',
                    html: html,
                    width: 450,
                    callBack: function () {
                        if ($("#comment_content").val() == "") {
                            $("#comment_content").css("border", "#a94442 1px solid");
                            $.message.error('出现错误，请检查高亮部分！');
                            return false;
                        } else {
                            save();
                        };
                    }
                });

                function save() {
                    var options = {
                        url: ctx + '/api/b2b2c/store-comment-api/edit.do',
                        type: 'POST',
                        success: function (result) {
                            if (result.result == 1) {
                                $.message.success(result.message, 'reload');
                            } else {
                                $.message.error(result.message);
                            };
                        },
                        error: function () {
                            $.message.error('出现错误，请重试！')
                        }
                    };
                    $('#dialogModal').find('#replyForm').ajaxSubmit(options);
                };
            };

        });
    })();

    $("a.del").click(function () {
        $.ajax({
            url: ctx + "/api/b2b2c/commentApi!del.do?comment_id=" + $(this).attr("commentId"),
            dataType: 'json',
            type: "POST",
            cache: false,
            success: function (data) {
                if (data.result == 1) {
                    $.message.success(data.message, 'reload');
                }else {
                    $.message.error(data.message);
                };
            },
            error: function () {
                $.message.error("出现错误，请重试");
            }
        });
    });

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
            var mname = keyWord, gname = $('#filter_good_name').val(), searchcontent = $('#filter_comment').val(), replyStatus = $('#filter_reply_status').val();
            location.href = './advisory.html?stype=1&type=1&mname='+ mname +'&gname='+ gname +'&searchcontent='+ searchcontent +'&replyStatus='+ replyStatus;
        }else {
            if(!keyWord && !GetQueryString('keyword')){
                location.href = './advisory.html';
            }else {
                location.href = './advisory.html?stype=0&type=1&keyword='+ keyWord;
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

    $("#deleteComment").click(function () {
        alert($("input[name='commentId']:checked").size());
        if (!confirm("确定删除吗？"))
            return false;
        if ($("input[name='commentId']:checked").size() == 0) {
            alert("至少需要选择一条数据");
            return false;
        }
        var options = {
            url: ctx + "/api/b2b2c/commentApi!delComment.do",
            type: "POST",
            dataType: 'json',
            success: function (data) {
                if (data.result == 1) {
                    $.message.success(data.message, 'reload');
                }else {
                    $.message.error(data.message);
                };
            },
            error: function (e) {
                $.message.error('出现错误 ，请重试！');
            }
        };
        $("#commentForm").ajaxSubmit(options);
    });
});

