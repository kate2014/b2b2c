/**
 * Created by Andste on 2016/6/22.
 */
$(function(){

    //  tab切换
    (function(){
        var tab = $('.app-tab-tools');
        tab.find('a').unbind('click').on('click', function(){
            loadPage($(this).attr('order_state'));
        });
    })();

    loadPage('all')
    // 订单列表页加载
    function loadPage(str){
        var loadBox = $('.bill-box'), sn = $('.bill-box').attr('sn');
        if(str ==  'all'){
            loadBox.empty().load('./order_detail_all_list.html?page=1&sn='+ sn, function(){
                $('#order_all_list').addClass('active').siblings().removeClass('active');
            });
        }else if(str == 'return') {
            loadBox.empty().load('./order_detail_return_list.html?page=1&sn='+ sn, function(){
                $('#order_return_list').addClass('active').siblings().removeClass('active');
            });
        }else {
            loadBox.empty();
        };
    };
    
     
    
});