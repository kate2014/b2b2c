/**
 * Created by Andste on 2016/6/1.
 */
$(function(){


    isActive();

    //  左侧边栏主菜单鼠标悬停，文字变色
    (function(){
        $('.list-main-sidebar li').hover(function(){
            var _this = $(this);
            if(!_this.is('.active')){
                _this.find('span, a').css({color: '#ffffff'});
            };
        }, function(){
            var _this = $(this);
            if(!_this.is('.active')){
                _this.find('span, a').css({color: ''});
            };
        });
    })();

    //  items点击页面伪静态跳转【实际上是真跳转，只是看起来像伪跳转】
    (function(){
        var li = $('.list-main-sidebar li');
        li.unbind('click').on('click', function(){
            var _this = $(this),
                _href = _this.attr('data-href');
            if(!_this.is('.active') && _href){
                window.location.href = _href;
            };
        });
    })();

    //  给选中状态的li内的文字添加颜色
    function isActive(){
        var li = $('.list-main-sidebar li'),
            liLen = li.length;
        for(var i = 0; i < liLen; i++){
            //  如果有.active，则会给这个li下面的span和a换色
            if(li.eq(i).is('.active')){
                li.eq(i).find('span, a').css({color: '#333'});
            }else {
                li.eq(i).find('span, a').css({color: ''});
            };
        };

        //  如果第一个li有.active，则会隐藏副侧边栏
        if(li.eq(0).is('.active')){
            hideMinorSidebar();
        };
    };

    /**
     * 显示、隐藏副侧边栏
     * @param _str
     */
    function hideMinorSidebar(){
        $('.app-wappler').css({paddingLeft: '90px'})
        $('.minor-sidebar').css({width: ''});
        $('.app-sidebar').css({
            width: '90px',
            boxShadow: 'none'
        });
    };

    /* 以下是二级菜单代码
     ============================================================================ */
    //  二级菜单点击切换
    $('.list-lower-sidebar li').unbind('click').on('click', function(){
        var _this = $(this),
            _href = _this.attr('data-href');
        window.location.href = _href;
    });

    //  修复IE浏览器.list-lower-sidebar li a 的 padding不一致导致宽度不一致问题
    (function(){
        if(Sys.ie){
            $('.list-lower-sidebar li a').css({
                paddingLeft: 10,
                paddingRight: 10
            });
        };
    })();

    /* 以下是帮助侧边栏代码
     ============================================================================ */
    //  关闭、开启帮助侧边栏
    (function(){
        if($('body').find('.app-help').length > 0){
            var closeHelp = $('.close-ico'),
                openHelp = $('.open-help');
            closeHelp.unbind('click').on('click', function(){
                helpShow('hide');
                $.cookie('help', 'hide', { expires: 7 });
            });
            openHelp.unbind('click').on('click', function(){
                helpShow('show');
                $.cookie('help', 'show', { expires: 7 });
            });
        };
    })();

    function helpShow(_str){
        var help = $('.app-help'),
            wappler = $('.app-wappler'),
            openHelp = $('.open-help');
        if(_str == 'hide'){
            help.stop().animate({width: 0}, 300);
            openHelp.stop().animate({opacity: 1}, 300);
            wappler.stop().animate({paddingRight: 0});
        }else {
            help.stop().animate({width: 199}, 300);
            openHelp.stop().animate({opacity: 0}, 300);
            wappler.stop().animate({paddingRight: 210});
        };
    };

    //  根据cookie判断帮助侧边栏是开启还是关闭、调整搜索框位置
    (function(){
        if($('body').find('.app-help').length > 0){
            var helpCookie = $.cookie('help'),help = $('.app-help'), wappler = $('.app-wappler'), openHelp = $('.open-help');
            if(helpCookie == 'hide'){
                help.css({width: 0});
                openHelp.css({opacity: 1});
                wappler.css({paddingRight: 0});
            }else {
                help.css({width: 199});
                openHelp.css({opacity: 0});
                wappler.css({paddingRight: 210});
            };
        };
    })();

    //  个人信息退出
    (function(){
        var box = $('.user-menu'), infoBox = $('.user-info-box');
        box.hover(function(){
            infoBox.addClass('show');
        }, function(){
            infoBox.removeClass('show');
        });

    })();

    /* 退出登录
     ============================================================================ */
    $('#app-logout').unbind('click').on('click', function() {
        $.confirm('确定要退出吗？', function(){
            $.ajax({
                url : ctx + '/api/shop/member/logout.do',
                type: 'POST',
                cache : false,
                success : function(result) {
                    if (result.result == 1) {
                        setTimeout(function(){
                            $.closeLoading();
                            location.href = ctx + '/index.html';
                        }, 300);
                    } else {
                        $.message.error(result.message);
                    };
                },
                error : function() {
                    $.message.error("出现错误，请重试！");
                }
            });
        });
    });

    /* 上部导航栏链接跳转
     ============================================================================ */
    (function(){
        var li = $('.app-nav .list-nav > li');
        li.unbind('click').on('click', function(){
            var _this = $(this), href = _this.find('a').attr('href');
            window.location.href = href;
        });
    })();

    /* 分页JS
     ============================================================================ */
    //  给分页添加样式--同时兼容IE78
    (function(){
        var paging = $('.app-paging-tools').find('.paging');
        if(paging){
            $(document).ready(function(){
                //  给分页添加样式--同时兼容IE78
                if(lteIE8){
                    paging.find('.page').addClass('pagination');
                }else {
                    paging.find('ul').addClass('pagination');
                };
                //  分页去掉一个span
                paging.find('span').eq(1).css({display: 'none'});
            });
        };
    })();

    /* IE下input框样式兼容
     ============================================================================ */
    if(Sys.ie > 7){
        $("input[type='text']").css({height: 'auto'});
        $('.app-tab-tools').find('li').css({marginTop: -1});
    };

    /* 修复IE下筛选工具栏样式问题
     ============================================================================ */
    if(Sys.ie){
        $('.filter-tools').css({padding: 0});
    }

    /* 页面滚动事件
     ============================================================================ */
    (function(){
        if(!Sys.ie){
            var initTop = 0;
            $(window).scroll(function(){
                var scrollTop = $(window).scrollTop();
                if(scrollTop > initTop){
                    scroll('down');
                } else {
                    scroll('up');
                }
                initTop = scrollTop;
            });

            function scroll (direction) {
                var nav = $('.app-nav');
                if(direction == 'down'){
                    if(Sys.ie < 10){
                        nav.stop().animate({
                            marginTop: -50
                        });
                    }else {
                        nav.css({marginTop: -50});
                    };
                }else {
                    if(Sys.ie < 10){
                        nav.stop().animate({
                            marginTop: 0
                        });
                    }else {
                        nav.css({marginTop: 0});
                    };
                };
            };
        };
    })();

    /* ajax全局监听
     ============================================================================ */
    if(!Sys.ie || Sys.ie > 8){
        $(document).ajaxComplete(function(event, jqXHR, options){
            var _url = options.url;
            if(!(_url.indexOf('.html') != -1 || _url.indexOf('regionid') != -1)){
                $.loading();
            }
        });

        $(document).ajaxStop( function(event, jqXHR, options){
            $.closeLoading();
        });
    }
});