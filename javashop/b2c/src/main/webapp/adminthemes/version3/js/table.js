/**
 * Created by lishida on 2017/1/11.
 */
/*Javascript代码片段*/
//layui.config({
//    base: 'plugins/layui/modules/'
//});
//
//layui.use(['icheck', 'laypage','layer'], function() {
//    var $ = layui.jquery,
//        laypage = layui.laypage,
//        layer = parent.layer === undefined ? layui.layer : parent.layer;
//    $('input').iCheck({
//        checkboxClass: 'icheckbox_flat-green'
//    });
//});
(function ($) {
    $.fn.extend({
        "grid": function (options) {
            //检测用户传进来的参数是否合法
            if (!isValid(options))
                return this;
            var opts = $.extend({}, defaluts, options); //使用jQuery.extend 覆盖插件默认参数
            return this.each(function () {  //这里的this 就是 jQuery对象。这里return 为了支持链式调用
                var $this = $(this); //获取当前dom 的 jQuery对象，这里的this是当前循环的dom
                //根据参数来设置 dom的样式
                $this.dataTable({
                    "processing": true,//是否显示加载状态
                    "serverSide": true,//连接服务端
                    ajax: {
                        //指定数据源
                        url: opts.url,
                        type: "get"
                    },
                    "scrollY": false,//dt高度
                    "lengthMenu": [
                        [10, 15, 20, -1],
                        [10, 15, 20, "All"]
                    ],//每页显示条数设置
                    "stripeClasses": false,
                    "lengthChange": false,//是否允许用户自定义显示数量
                    "bPaginate": true, //翻页功能
                    "pagingType":"full",//翻页器的样式//
                    "bFilter": false, //列筛序功能
//                   "stateSave": true,//保存当前操作信息
                    "searching": false,//本地搜索
                    "ordering": true, //排序功能
                    "Info": true,//页脚信息
                    "autoWidth": true,//自动宽度
                    "oLanguage": {//国际语言转化
                        "oAria": {
                            "sSortAscending": " - click/return to sort ascending",
                            "sSortDescending": " - click/return to sort descending"
                        },
                        "sLengthMenu": "显示 _MENU_ 记录",
                        "sZeroRecords": "对不起，查询不到任何相关数据",
                        "sEmptyTable": "未有相关数据",
                        "sLoadingRecords": "正在加载数据-请等待...",
                        "sInfo": "当前显示 _START_ 到 _END_ 条，共 _TOTAL_ 条记录。",
                        "sInfoEmpty": "当前显示0到0条，共0条记录",
                        "sInfoFiltered": "（数据库中共为 _MAX_ 条记录）",
//                       "sProcessing": "<img src='../resources/user_share/row_details/select2-spinner.gif'/> 正在加载数据...",
                        "sSearch": "模糊查询：",
                        "select": true,
                        "sUrl": "",
                        //多语言配置文件，可将oLanguage的设置放在一个txt文件中，例：Javascript/datatable/dtCH.txt
                        "oPaginate": {
                            "sFirst": "首页",
                            "sPrevious": " 上一页 ",
                            "sNext": " 下一页 ",
                            "sLast": " 尾页 "
                        }
                    },
                    "columns": opts.columns,

                    "columnDefs": opts.columnDefs,
                    "order": [
                        [0, null]//设置第一列的排序图标为默认
                    ],
                });

                //格式化高亮文本
            });

        }
    });
    //默认参数
    var defaluts = {
        columns: '',
        url: '',
        columnDefs:''
    };
    //私有方法，检测参数是否合法
    function isValid(options) {
        return !options || (options && typeof options === "object") ? true : false;
    }
})(window.jQuery);

$(function () {
    //全选or全不选
    $("#selected-all").on("click", function () {
        if ($(this).prop("checked") === true) {
            $("input[name='checklist']").prop("checked", true);
            $("input[name='checklist']").parents('tr').addClass('selected');
        } else {
            $("input[name='checklist']").prop("checked", false);
            $("input[name='checklist']").parents('tr').removeClass('selected');
        }
    });

    $.ajax({
        url: 'datas/nav_content.json',
        async: false, //_config.async,
        dataType: 'json',
        success: function(result, status, xhr) {
           if (result[0].Allstate==0){
               $("#All_close").attr("class","All_close");
               $(".page_explain").css("display","none")
           }

        },
        error:function(){

        }
    });

    //点击空白选中本行
    $('.site-table tbody ').on('click', 'tr ', function () {
        var $this = $(this);
        var $input = $this.children('td').eq(0).find('input');
        if ($input[0].checked) {
            $input.prop("checked", false);
            $this.removeClass('selected');
        } else {
            $input.prop("checked", true);
            $this.addClass('selected');
        }
    });

    //回车触发事件
    $("#_search").keydown(function(event){
        if(event.keyCode==13){
            alert(1)
        }
    });
    //页面说明的显示隐藏
    $("#explain").on("click",function(){
        $('.page_explain').slideToggle("1000");
    })

    //高级搜索框的显示隐藏
    $("#high_search").on("click",function(){
        $(".high_searchcontent").toggle();
    })

    //关闭说明功能的显示隐藏
    $("#explain,.explain_control").on("mousemove",function(){
        $(".explain_control").css("display","block");
    })

    $("#explain ,.explain_control").on("mouseout",function(){
        $(".explain_control").css("display","none");
    })

    //关闭本页的说明
    $("#self_close").on("click",function(){
        $('.page_explain').slideUp("1000");
    })


    layui.use(['form', 'layedit', 'laydate'], function(){
        var form = layui.form()
            ,layer = layui.layer
            ,layedit = layui.layedit
            ,laydate = layui.laydate;


        //监听提交
        form.on('submit(highsearch)', function(data){
           /* layer.alert(JSON.stringify(data.field), {
                title: '最终的提交信息'
            })*/
            $(".high_searchcontent").css("display","none")


            var goods_name = $.trim($("#goods_name").val());
            var goods_num = $.trim($("#goods_num").val());
            var goods_kinds = $.trim(data.field.goods_kinds);
            var goods_name1 = $.trim($("#goods_name1").val());
            var goods_num1 = $.trim($("#goods_num1").val());
            var goods_kinds1 = $.trim(data.field.goods_kinds1);
            var count=0;
            var Array=[];
            Array.push(goods_name,goods_num,goods_kinds,goods_name1,goods_num1,goods_kinds1);

            function getHtml(data){
                var uHtml = '<form  class="layui-form" action="">';
                uHtml +='<div class="content_up">' +
                    '<button type="button" title="收起" class="layui-btn layui-btn-primary layui-btn-small search_button"><i class="layui-icon">&#xe619;</i></button> </div>';
                uHtml +=' <ul class="search_conditions">';
                for (var i=0;i<Array.length;i++){
                   if (count==0){
                       if (data.form[i].tagName=="SELECT"){
                           uHtml +='<li>';
                           uHtml +='<div class="layui-form-item _select">';
                           uHtml +='<label class="layui-form-label _label">'+$(data.form[i].parentNode).prev()[0].innerHTML+":"+'</label>';
                           uHtml +='<div class="layui-input-block _layui-input-block">';
                           uHtml +=' <select    lay-filter="kinds">';
                           for (var j =0;j<data.form[i].length;j++){
                               if (data.form[i][j].value==Array[i]){
                                   uHtml +='  <option value="'+data.form[i][j].value+'" selected="selected">'+data.form[i][j].value+'</option>';
                               }else {
                                   uHtml +='  <option value="'+data.form[i][j].value+'">'+data.form[i][j].value+'</option>';
                               }
                           }
                           uHtml +=' </select>';
                           uHtml +=' </div>'
                           uHtml +=' </div>'
                           //关闭按钮
                           uHtml +='<span>';
                           uHtml +='<a class="closeSelf" href="javascript:void (0)">';
                           uHtml +='<i class="layui-icon" style="color: white;">&#x1006;</i>';
                           uHtml +='</a>';
                           uHtml +='</span>';
                           uHtml +='</li>';
                           count++;
                       }else {
                           if ($.trim(Array[i]) !==""){

                                   uHtml +='<li>';
                                   uHtml +='<span>'+data.form[i].parentNode.parentNode.innerText+":"+'</span>';
                                   uHtml +='<span id="'+data.form[i].name+'">'+Array[i]+'</span>';
                                   uHtml +='<span>';
                                   uHtml +='<a class="closeSelf" href="javascript:void (0)">';
                                   uHtml +='<i class="layui-icon" style="color: white;">&#x1006;</i>';
                                   uHtml +='</a>';
                                   uHtml +='</span>';
                                   uHtml +='</li>'

                           }
                       }
                   }else {
                       if (data.form[i+count].tagName=="SELECT"){
                           uHtml +='<li>';
                           uHtml +='<div class="layui-form-item _select">';
                           uHtml +='<label class="layui-form-label _label">'+$(data.form[i+count].parentNode).prev()[0].innerHTML+":"+'</label>';
                           uHtml +='<div class="layui-input-block _layui-input-block">';
                           uHtml +=' <select    lay-filter="kinds1">';
                           for (var j =0;j<data.form[i+count].length;j++){
                               if (data.form[i+count][j].value==Array[i]){
                                   uHtml +='  <option value="'+data.form[i+count][j].value+'" selected="selected">'+data.form[i+count][j].value+'</option>';
                               }else {
                                   uHtml +='  <option value="'+data.form[i+count][j].value+'">'+data.form[i+count][j].value+'</option>';
                               }
                           }
                           uHtml +=' </select>';
                           uHtml +=' </div>'
                           uHtml +=' </div>'
                           //关闭按钮
                           uHtml +='<span>';
                           uHtml +='<a class="closeSelf" href="javascript:void (0)">';
                           uHtml +='<i class="layui-icon" style="color: white;">&#x1006;</i>';
                           uHtml +='</a>';
                           uHtml +='</span>';
                           uHtml +='</li>';
                       }else {
                           if ($.trim(Array[i]) !==""){
                                   uHtml +='<li>';
                                   uHtml +='<span>'+data.form[i+count].parentNode.parentNode.innerText+":"+'</span>';
                                   uHtml +='<span id="'+data.form[i+count].name+'">'+Array[i]+'</span>';
                                   uHtml +='<span>';
                                   uHtml +='<a class="closeSelf" href="javascript:void (0)">';
                                   uHtml +='<i class="layui-icon" style="color: white;">&#x1006;</i>';
                                   uHtml +='</a>';
                                   uHtml +='</span>';
                                   uHtml +='</li>'
                           }
                       }
                   }
                }
                uHtml +='</ul>';
                uHtml +=' </form>';

                return uHtml;
            }
            var html=getHtml(data);
            $(".high_search").html(html);
            form.render('select');
            $(".high_search").slideDown("slow")

            $(".content_up").on("click",function(){
                $(".high_search").slideUp("slow")
            })

            //点击关闭当前的搜索条件
            $(".closeSelf").click(function(){
                var _id =$(this.parentNode).prev()[0].id;//当前点击的span的id
                var select_id =$(this.parentNode).prev()[0]
                if (select_id.tagName=="DIV"){
                    $(".search_conditions")[0].children[$(this.parentNode.parentNode).index()].remove();
                    $(" .layui-select-title input").val("");

                    if ($(".search_conditions li").length==0){
                        $('.high_search').slideUp("slow");
                    }

                    var goods_name = $.trim($("#goods_name").val());
                    var goods_num = $.trim($("#goods_num").val());
                    var goods_kinds = $.trim($(" .layui-select-title input").val());

                    $.ajax({
                        url: "datas/test1.json",
                        data:{goods_name:goods_name,goods_num:goods_num,goods_kinds:goods_kinds},
                        async: false,
                        type: "POST",
                        dataType: "JSON",
                        success: function (data) {
                            layer.alert(JSON.stringify(data), {
                                title: '最终的提交信息'
                            })
                            return false;
                        }
                    });
                }else {
                    $(".search_conditions")[0].children[$(this.parentNode.parentNode).index()].remove();
                    $("#search_form").find('input[id='+_id+']')[0].value="";


                    if ($(".search_conditions li").length==0){
                        $('.high_search').slideUp("slow");
                    }

                    var goods_name = $.trim($("#goods_name").val());
                    var goods_num = $.trim($("#goods_num").val());
                    var goods_kinds = $.trim($(" .layui-select-title input").val());

                    $.ajax({
                        url: "datas/test1.json",
                        data:{goods_name:goods_name,goods_num:goods_num,goods_kinds:goods_kinds},
                        async: false,
                        type: "POST",
                        dataType: "JSON",
                        success: function (data) {
                            layer.alert(JSON.stringify(data), {
                                title: '最终的提交信息'
                            })
                            return false;
                        }
                    });
                }

            })

            //监听select
            form.on('select(kinds)', function(data){
                $(" .layui-select-title input")[0].value=data.value;
                var goods_name = $.trim($("#goods_name").val());
                var goods_num = $.trim($("#goods_num").val());
                var goods_kinds = $.trim(data.value);
                $.ajax({
                    url: "datas/test1.json",
                    data:{goods_name:goods_name,goods_num:goods_num,goods_kinds:data.value},
                    async: false,
                    type: "POST",
                    dataType: "JSON",
                    success: function (data) {
                        layer.alert(JSON.stringify(data), {
                            title: '最终的提交信息'
                        })
                        return false;
                    }
                });
            })

            form.on('select(kinds1)', function(data){
                $(" .layui-select-title input")[1].value=data.value;
                var goods_name = $.trim($("#goods_name").val());
                var goods_num = $.trim($("#goods_num").val());
                var goods_kinds = $.trim(data.value);
                $.ajax({
                    url: "datas/test1.json",
                    data:{goods_name:goods_name,goods_num:goods_num,goods_kinds:data.value},
                    async: false,
                    type: "POST",
                    dataType: "JSON",
                    success: function (data) {
                        layer.alert(JSON.stringify(data), {
                            title: '最终的提交信息'
                        })
                        return false;
                    }
                });
            })



            /*   $.ajax({
                   url: "datas/test1.json",
                   data:{goods_name:goods_name,goods_num:goods_num,goods_kinds:goods_kinds},
                   async: false,
                   type: "POST",
                   dataType: "JSON",
                   success: function (data) {
                       layer.alert(JSON.stringify(data), {
                           title: '最终的提交信息'
                       })
                       return false;
                   }
               });*/
            return false;
        });



    });
})



