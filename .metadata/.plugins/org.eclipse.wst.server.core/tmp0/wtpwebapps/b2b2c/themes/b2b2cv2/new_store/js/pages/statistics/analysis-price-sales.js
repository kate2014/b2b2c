/**
 * create by jianghongyan 2016-07-31
 */

$(function() {
		$("#set_price_ranges").click(function() {
				openPriceSections();
		});
		getBuyStatistics("sections=0&sections=500&sections=1000&sections=1500&sections=2000");
});

function refreshTab() {
		getBuyStatistics("sections=0&sections=500&sections=1000&sections=1500&sections=2000");
}

/**
 * 获取价格销量统计图配置
 * @param json 数据
 */
function getPriceDisConfig(json, ytitle) {
		var conf = {}; //配置
		var colors = Highcharts.getOptions().colors; // 颜色

		var data = []; // Y轴 排名数据
		var categories = []; //X轴 名次数据

		for(var i in json) {
				var order = json[i];

				//添加到数组
				data.push(order.num);
				categories.push("" + order.elt_data);
		}

		var conf = {
				title      : ytitle, //统计图标题
				yDesc      : "下单量", //y轴 描述
				//X 轴数据 [数组]
				categories : categories,
				//Y轴数据 [数组]
				series     : [{
						name : '下单量',
						data : data
				}]

		};
		return conf;
};
/**
 * 初始化曲线图
 * @param id  html 初始化div的id
 * @param conf  相关配置
 */
function initLineChart(id, conf) {

		var options = {
				credits     : {
						//text: 'Javashop',
						//href: 'http://www.javamall.com.cn'
						enabled : false
				},
				chart       : {
						type     : 'line',
						renderTo : 'container'

				},
				title       : {
						text : conf.title
				},
				xAxis       : {
						categories : conf.categories
				},
				yAxis       : {
						title : {
								text : conf.yDesc
						}
				},
				plotOptions : {
						line : {
								dataLabels : {
										enabled : true
								}
						}
				},
				series      : conf.series
		};
		$("#" + id).highcharts(options);
};
function openPriceSections() {
		var cycle_type = $("#cycle_type").val();
		var year       = $("#year").val();
		var month      = $("#month").val();
		$("#dialog_div").dialogModal({
				title    : '按价格区间筛选',
				width    : 360,
				showCall : function() {
						showCall();
				},
				callBack : function() {
						return callBack();
				}
		});


		function showCall() {
				var modal = $('#dialogModal'), table = modal.find('table'), addBtn = modal.find('.add-section'), sequ = parseInt($('.group-num').last().html()) + 1;
				modal.on('click', '.add-section', function() {
						var _this = $(this);
						var tr    = '<tr>\
			<td>第<span class="group-num">' + sequ + '</span>组：</td>\
			<td><input type="text" class="form-control"></td>\
			<td style="width: 16px; text-align: center; ">-</td>\
			<td><input type="text" class="form-control"></td>\
			<td style="width: 50px; text-align: center; "><a href="javascript: void(0);" style="font-size: 12px;" class="add-section">添加</a></td>\
			</tr>';
						if(sequ == 10){
								tr = '<tr>\
				<td>第<span class="group-num">' + sequ + '</span>组：</td>\
				<td><input type="text" class="form-control"></td>\
				<td style="width: 16px; text-align: center; ">-</td>\
				<td><input type="text" class="form-control"></td>\
				</tr>';
						}
						;
						$(tr).appendTo(table);
						_this.remove();
						sequ++;
				});
		};

		function callBack() {
				var modal = $('#dialogModal'), sectionArray = [], input = modal.find("input[type='text']");
				for(var i = 0; i < input.length; i++) {
						var val = input.eq(i).val();
						if(val && !isNaN(val)){
								var num = parseInt(input.eq(i).val());
								input.eq(i).val(num);
								sectionArray.push(num);
						}else if(val) {
								$.message.error('请输入正整数！');
								input.eq(i).focus();
								return false;
						};
				}
				;
				if(sectionArray.length == 0){
						return
				}else if(sectionArray.length == 1){
						$.message.error('请至少输入两个价格区间！');
						return false;
				}
				;
				sectionArray = sectionArray.sort();
				var n        = {}, r = [];
				for(var i = 0; i < sectionArray.length; i++) {
						if(!n[sectionArray[i]]){
								n[sectionArray[i]] = true;
								r.push(sectionArray[i]);
						}
						;
				}
				;

				sectionArray = r;
				var where    = "";
				for(var i = 0; i < sectionArray.length; i++) {
						where += "sections=" + sectionArray[i];
						if(i != sectionArray.length - 1){
								where += "&";
						}
				}
				var catid = $(".combo-value").val();
				if(typeof(catid) == "undefined"){
						catid = "";
				}
				var options = {
						url      : ctx + '/api/goods-statistics/get-goods-price-sales.do?' + where + "&catid=" + catid,
						type     : 'POST',
						dataType : 'json',
						data     : {'cycle_type' : cycle_type, 'year' : year, 'month' : month, 'storeid' : store_id},
						success  : function(result) {
								if(result.result == 1){
										var conf = getPriceDisConfig(result.data, "价格销量分析");
										initLineChart("price_sales", conf);
								}else {
										$.message.error('出现错误，请重试！');
								}
						},
						error    : function() {
								$.message.error('出现错误，请重试！');
						}
				};
				$.ajax(options);
		};
}


/**
 * 获取购买分析数据
 * @returns
 */
function getBuyStatistics(where) {
		var cycle_type = $("#cycle_type").val();
		var year       = $("#year").val();
		var month      = $("#month").val();
		var catid      = $(".combo-value").val();
		alert
		if(typeof(catid) == "undefined"){
				catid = "";
		}
		//区域分析
		$.ajax({
				url      : ctx + "/api/goods-statistics/get-goods-price-sales.do?" + where + "&catid=" + catid,
				data     : {'cycle_type' : cycle_type, 'year' : year, 'month' : month, 'storeid' : store_id},
				type     : "POST",
				dataType : 'json',
				success  : function(result) {
						//初始化客单价分布图
						var conf = getPriceDisConfig(result.data, "价格销量分析");
						initLineChart("price_sales", conf);
				},
				error    : function(e) {
						$.message.error("出现错误 ，请重试");
				}
		});
};