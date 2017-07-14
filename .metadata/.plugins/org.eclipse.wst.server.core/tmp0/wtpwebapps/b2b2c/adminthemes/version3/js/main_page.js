layui.config({
	base: '../adminthemes/version3/js/'
}).use(['element', 'layer', 'navbar', 'tab'], function() {
	var element = layui.element()
	$ = layui.jquery,
		layer = layui.layer,
		navbar = layui.navbar(),
		tab = layui.tab({
			elem: '.layout-nav-card' //设置选项卡容器
		});

	//iframe自适应
	$(window).on('resize', function() {
		var $content = $('.layout-nav-card .layui-tab-content');
		$content.height($(this).height() - 80);
		$content.find('iframe').each(function() {
			$(this).height($content.height());
		});
	}).resize();
	       //请求数据配置菜单
	     /*   $.ajax({
	            url: '../core/admin/menu/json.do',
	            async: false, //_config.async,
	            dataType: 'json',
	            success: function(result, status, xhr) {
	              for (var i =0;i<result.length-2;i++){
	            	  //图标暂时写死
	            	  var icons=["fa fa-desktop",
	            	             "fa fa-shopping-bag",
	            	             "fa fa-list-alt",
	            	             "fa fa-heart",
	            	             "fa fa-tag",
	            	             "fa fa-file-text-o",
	            	             "fa fa-pencil-square-o",
	            	             "fa fa-cog",
	            	             "fa fa-user-circle"];
	            	  if (i==0){
	                      var $li =$("<li class='layui-nav-item'></li>")
	                      var $a = $('<a href="javascript:void(0)" data-module-id="'+i+'"><i class="'+icons[i]+' menu_icon" aria-hidden="true"></i><cite class="marg-left menu_content">'+result[i].name+'</cite></a>')
	                      var $a = '<a href="javascript:void(0)" data-module-id="1"><image src=\"'+result[i].icon+\"' style="width: 20px;height: 20px;margin-right: 5px"><cite>'+result[i].name+'</cite></a>'
	                      var _html = '<li class='layui-nav-item'>'
	                      +'<a href="javascript:void(0)" data-module-id="1">'
	                      +
	                      
	                      var $img=$("<image ></image>")
	                      $img.attr("src",result[i].icon);
	                      $img.css("width","20px")
	                      $img.css("height","20px")
	                      $( $a).append($img);
	                      $( $li).append($a);
	                      $("#top-nav").append($li);
	                  }else {
	                      var $li = $("<li class='layui-nav-item'></li>")
	                      var $a = $('<a href="javascript:void(0)" data-module-id="'+i+'"><i class="'+icons[i]+' menu_icon" aria-hidden="true"></i><cite class="marg-left menu_content">'+result[i].name+'</cite></a>')
	                       var $content = $("<i class='fa fa-desktop' aria-hidden='true'></i><cite>"+result[i].name+"</cite>")
	                       $a.attr("data-module-id",i+1)
	                       $($a).append($content);
	                      $($li).append($a);
	                      $("#top-nav").append($li);
	                  }
	              }
	              var $menu = $('#menu');
	                $menu.find('li.layui-nav-item').each(function() {
	                    var $this = $(this);
	                    //绑定一级导航的点击事件
	                    $this.on('click', function () {
	                        
	                        //设置navbar
	                        navbar.set({
	                            elem: '#side', //存在navbar数据的容器ID
	                            data: result
	                        });
	                        //渲染navbar
	                        var nav_index =$this.index();
	                        navbar.render(nav_index,result);
	                        //监听点击事件
	                        navbar.on('click(side)', function (data) {
	                            tab.tabAdd(data.field);
	                        });
	                    });
	                })
	            },
	            error:function(){
				}
	        });
	        */
	        //禁止网页文本被选中：
            if(document.all){
                document.onselectstart= function(){return false;}; //for ie
            }else{
                document.onmousedown= function(){return false;};
                document.onmouseup= function(){return true;};
            }
            document.onselectstart = new Function('event.returnValue=false;')   

	
           
            //只显示点击的一级菜单的子菜单
            $('#top-nav').on('click','li',function(){
            	 var _id = this.id;
            	 $("#side ul li").css("display","none");
            	 $("#side ul").find('li[id="'+_id+'"]').css("display","block");
            	 $("#_id").css("display","block");
            	
            	 
            })
            //默认点击点击一级菜单第一个
            $('.beg-layout-side-left').find('li[id=1]').click();
            //创建存储二级菜单的容器
            navbar.set({
                elem: '#side', //存在navbar数据的容器ID
                data: ''
            });
          
            //监听点击事件
            navbar.on('click(side)', function (data) {
                tab.tabAdd(data.field);
            });
    //添加新tab
	element.on('nav(user)', function(data) {
		var $a = data.children('a');
		if($a.data('tab') !== undefined && $a.data('tab')) {
			tab.tabAdd({
				title: $a.children('cite').text(),
				href: $a.data('url')
			});
		}
	});
            
     
              /*  对全部tab选项卡的关闭等操作 */
                $("#contextmenu").on('click','ul li', function () {
                    var li = $(".layui-tab-title li")
                    var num = $(this).index();
                    //num为0 是刷新 1 是关闭其他 2 是关闭全部
                    if(num==0){
                    	var index= $('.layui-tab-title .layui-this').index();
                    	$(".layui-tab-content iframe")[index].name=index;
                    	 document.getElementsByName(index)[0].contentWindow.location.reload(true);
                    }else if(num==1){
                    	 for (var i = 1;i<li.length;i++){
                             if(li[i].className==""){
                                 li[i].lastChild.click();
                             }
                         }
                    }else{
                    	 for (var i = 1;i<li.length;i++){
                             li[i].lastChild.click();
                         }
                         i=0;
                    }
                    $(".layout-tab-contextmenu").css("display","none");
                   
                });
                //tab选项卡操作的显示与隐藏
                $(".tabs-control").on('mouseover', function () {
                	 $(".layout-tab-contextmenu").css("display","block");
                });
                $(".tabs-control").on('mouseout', function () {
               	 $(".layout-tab-contextmenu").css("display","none");
               });
              
                //点击退出
                $("#logout_btn").click(function(){
				
				if( !confirm("确认退出吗？") ){
					return false;	
				}
				
				var options = {
					url : "../core/admin/admin-user/logout.do",
					type : "POST",
					dataType : 'json',
					success : function(result) {				
						if(result.result==1){
							var url = "${ctx}/admin/login.do";
							location.href=url;
						}else{
							$.Loading.error(result.message);
						}
					},
					error : function(e) {
						$.Loading.error("出现错误，请重试");
					}
				};
				$.ajax(options);		
			})
			
			
           
});
