/**
 * Created by Andste on 2016/6/6.
 */

$(function(){
//  商品删除操作
	(function(){
        var deleteBtn = $('.delete-cats');
        deleteBtn.unbind('click').on('click', function(){
            var _this = $(this),
            cat_id = _this.attr('cat_id'),
            cat_name = _this.attr('cat_name'),
            ptr = _this.closest('.bd_line').next().is('.ptr'),
            _thisPtr = _this.closest('.bd_line').is('.ptr');
            ;
            if(ptr && !_thisPtr){
            	$.alert('该分类下还有子分类，请逐个删除！');
            }else {
            	$.confirm('确定要删除吗？', function () {
					deleteCat();
                });
            };

            function deleteCat(){
                $.ajax({
                    url : ctx + "/api/b2b2c/goods-cat/delete.do?cat_id=" + cat_id+"&cat_name="+encodeURI(cat_name),
                    cache : false,
                    dataType : 'json',
                    success : function(data) {
                        if(data.result==1){
							$.message.success('删除成功！', 'reload');
                        }else {
                            $.message.error(data.message);
                        };
                    },
                    error : function() {
                        $.alert("出现错误，请重试");
                    }
                });
            };
        });
    }());

	//展开子分类
		$(".parimg").click(function(){
			var idvalue = $(this).attr("idvalue");
			var isimg = $(this).attr("isimg");
			var $this = $(this);
			$(this).parent().parent().parent().find(".ptr").each(function(){
				if($(this).attr("pidvalue")==idvalue){
					$(this).toggle();
					if(isimg==0){
						$this.removeClass("reduction");
						$this.attr("isimg",1);
					}else{
						$this.removeClass("reduction");
						$this.addClass("reduction");
						$this.attr("isimg",0);
					}
				}
			});
		});
	
		 //  全选操作
	    (function(){
	        var checkAllBtn = $('.checkall'), checkBox = $('.checkitem');
	        checkAllBtn.unbind('click').on('click', function(){
	            var _this = $(this);
	            if(_this.is(':checked')){
	                checkBox.prop('checked', true);
	            }else {
	                checkBox.removeAttr('checked');
	            };
	        });
	    })();
	    //选择删除
	    $(".delAll").click(function(){
			if(window.confirm('确定要删除分类吗？')){
				var catids="";
				var catnames="";
				$(".checkitem:checked").each(function(){
					catids=catids+$(this).val()+",";
					catnames = catnames+$(this).attr("catname")+",";
				})
				if(catids==""){
					$.message.error("未勾选分类！");
					return false;
				}
				$.ajax({
					url : "${ctx}/api/b2b2c/goods-cat/del-all.do?catids="+catids+"&catnames="+encodeURI(catnames),
					type : "POST",
					dataType : 'json',
					cache : false,
					success : function(result) {
						if (result.result == 1) {
							$.message.success('删除成功！', 'reload');
						}
						if (result.result == 0) {
							$.message.error(result.message);
						}
					},
					error : function() {
						$.message.error("出现错误，请重试");
					}
				});
			}
		});    
	    
	    
	    
	  //添加并保存分类
	    (function(){
			var btn = $('.new_storecat');
			btn.unbind('click').on('click', function(){
				var _this = $(this), cat_id = _this.attr('cat_id');

				$.ajax({
					url: './classify_add.html',
					type: 'GET',
					success: function(html){
						openDialog(html);
					},
					error: function(){
						$.message.error('出现错误，请重试！');
					}

				});

				function openDialog(html){
					$.dialogModal({
						title: '增加分类',
						html: html,
						width: 450,
						callBack: function(){
							if($("#cat_name").val()==""){
								$("#cat_name").css("border","#a94442 1px solid");
								$.message.error('出现错误，请检查高亮部分！');
								return false;
							}else {
								save();
							}
							
						}
					});

					function save(){
						var options = {
							url: ctx + '/api/b2b2c/goods-cat/add-goods-cat.do',
							type: 'POST',
							success: function(result){
								if(result.result == 1){
									$.message.success('保存成功！', 'reload');
								}else {
									$.alert(result.message);
								};
							},
							error: function(){
								$.message.error('出现错误，请重试！')
							}
						};
						$('#dialogModal').find('#add_form').ajaxSubmit(options);
					};
				};

			});
		})();

	//  编辑分类
	(function(){
		var btn = $('.edit-cats');
		btn.unbind('click').on('click', function(){
			var _this = $(this), cat_id = _this.attr('cat_id');

			$.ajax({
				url: './classify_edit.html?catid='+cat_id,
				type: 'GET',
				success: function(html){
					openDialog(html);
				},
				error: function(){
					$.message.error('出现错误，请重试！');
				}

			});

			function openDialog(html){
				$.dialogModal({
					title: '编辑分类',
					html: html,
					width: 450,
					callBack: function(){
						if($("#edit_cat_name").val()==""){
							$("#edit_cat_name").css("border","#a94442 1px solid");
							$.message.error('出现错误，请检查高亮部分！');
							return false;
						}else {
							save();
						}
					}
				});

				function save(){
					var options = {
						url: ctx + '/api/b2b2c/goods-cat/edit-goods-cat.do',
						type: 'POST',
						success: function(result){
							if(result.result == 1){
								$.message.success('保存成功！', 'reload');
							}else {
								$.message.error(result.message);
							};
						},
						error: function(){
							$.message.error('出现错误，请重试！')
						}
					};
					$('#dialogModal').find('#edit_form').ajaxSubmit(options);
				};
			};

		});
	})();
	
	//添加并保存子分类
	(function(){
		var btn = $('.add-subordinate');
		btn.unbind('click').on('click', function(){
			var _this = $(this), cat_id = _this.attr('cat_id');

			$.ajax({
				url: './classify_add.html?catid='+cat_id,
				type: 'GET',
				success: function(html){
					openDialog(html);
				},
				error: function(){
					$.message.error('出现错误，请重试！');
				}

			});

			function openDialog(html){
				$.dialogModal({
					title: '增加子分类',
					html: html,
					width: 450,
					callBack: function(){
						if($("#cat_name").val()==""){
							$("#cat_name").css("border","#a94442 1px solid");
							$.message.error('出现错误，请检查高亮部分！');
							return false;
						}else {
							save();
						}
						
					}
				});

				function save(){
					var options = {
						url: ctx + '/api/b2b2c/goods-cat/add-goods-cat.do',
						type: 'POST',
						success: function(result){
							if(result.result == 1){
								$.message.success('保存成功！', 'reload');
							}else {
								$.message.error(result.message)
							};
						},
						error: function(){
							$.message.error('出现错误，请重试！')
						}
					};
					$('#dialogModal').find('#add_form').ajaxSubmit(options);
				};
			};

		});
	})();
	
});


	