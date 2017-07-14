/**
 * create by jianghongyan 2016-06-30
 */

/**
 * 初始化曲线图
 * @param id  html 初始化div的id
 * @param conf  相关配置
 */
$(function() {
		var type = $("#cycle_type").val();
		// 如果统计类型 是按年统计
		if(type == "2"){
				$("#month").hide();
		}else {
				$("#month").show();
		}

		// 统计类型变更事件
		$("#cycle_type").change(function() {

				var type = $(this).val();

				// 如果统计类型 是按年统计
				if(type == "2"){
						$("#month").hide();
				}else {
						$("#month").show();
				}
				//设置全部tab需要刷新
		});
		getBuyStatistics("sections=0&sections=500&sections=1000&sections=1500&sections=2000");
		$("#set_price_ranges").click(function() {
				openPriceSections();
		});

		$('[data-toggle="tooltip"]').tooltip()
});

function openPriceSections() {
		var cycle_type = $("#cycle_type").val();
		var year       = $("#year").val();
		var month      = $("#month").val();
		$("#dialog_div").dialogModal({
				title    : '按价格区间筛选',
				width    : 360,
				showCall : function() {
						return showCall();
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

				var options = {
						url      : ctx + '/api/operator-statistics/buy-price-json.do',
						type     : 'POST',
						dataType : 'json',
						data     : {
								'cycle_type' : cycle_type,
								'year'       : year,
								'month'      : month,
								'storeid'    : store_id,
								'sections'   : sectionArray
						},
						success  : function(result) {
								if(result.result == 1){
										var conf = buyPriceConfig(result.data, "客单价分布");
										initLineChart("buy_money", conf);
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
 * 初始化曲线图
 * @param id
 * @param conf
 */
function initLineChart(id, conf) {
		var options = {
				credits     : {
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
						title           : {
								text : conf.yDesc
						},
						minTickInterval : 1
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
/**
 * 根据tabId刷新页面数据
 * @param tabId  tab页的id
 * @param startDate  条件：开始时间
 * @param endDate  条件：结束时间
 */
function refreshTab() {
		getBuyStatistics("sections=0&sections=500&sections=1000&sections=1500&sections=2000");
};
/**
 * 获取购买分析数据
 * @returns
 */
function getBuyStatistics(where) {
		var cycle_type = $("#cycle_type").val();
		var year       = $("#year").val();
		var month      = $("#month").val();
		//区域分析
		$.ajax({
				url      : ctx + "/api/operator-statistics/buy-list-json.do?" + where,
				data     : {'cycle_type' : cycle_type, 'year' : year, 'month' : month, 'storeid' : store_id},
				type     : "POST",
				dataType : 'json',
				success  : function(result) {
						//初始化客单价分布图
						var conf = buyPriceConfig(result.data.price, "客单价分布");
						initLineChart("buy_money", conf);
						//初始化购买时段分布图
						var conf2 = buyTimeConfig(result.data.time, "购买时段分布");
						initLineChart("buy_time", conf2);
				},
				error    : function(e) {
						$.message.error("出现错误 ，请重试");
				}
		});
};
/**
 * 获取客单价分布数据
 * @param json
 * @param ytitle
 * @returns
 */
function buyPriceConfig(json, ytitle) {
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
 * 获取购买时段分布数据
 * @param json
 * @param ytitle
 * @returns
 */
function buyTimeConfig(json, ytitle) {
		var conf = {}; //配置
		var colors = Highcharts.getOptions().colors; // 颜色

		var data = []; // Y轴 排名数据
		var categories = []; //X轴 名次数据

		for(var i in json) {
				var order = json[i];

				//添加到数组
				data.push(order.num);
				categories.push("" + order.hour_num);
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
}

