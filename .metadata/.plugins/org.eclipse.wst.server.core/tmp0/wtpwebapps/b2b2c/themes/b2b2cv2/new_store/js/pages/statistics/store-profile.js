/**
 * create by jianghongyan 2016-08-04
 */
$(function(){
	//1.初始化概况展示区数据
	initStatus30day();
	//2.初始化概况统计图
	initLineChart30day();
});

/**
 * 初始化曲线图
 * @param id	html 初始化div的id
 * @param conf	相关配置
 */
function initLineChart(id,conf){

	var options = {
			credits: {
	             //text: 'Javashop',
	             //href: 'http://www.javamall.com.cn'
				enabled:false
	        },
	        chart: {
	            type: 'line',
	            renderTo: 'container'
	            
	        },
	        title: {
	            text: conf.title
	        },
	        xAxis: {
	            categories: conf.categories
	        },
	        yAxis: {
	            title: {
	                text: conf.yDesc
	            }
	        },
	        plotOptions: {
	            line: {
	                dataLabels: {
	                    enabled: true
	                }
	            }
	        },
	        series: conf.series
	    };
	$("#" + id).highcharts(options);
};

/**
 * 初始化展示区
 */
function initStatus30day(){
	$.ajax({
		url:ctx+"/api/store-profile/get-last30day-status.do?store_id="+store_id,
		type:"GET",
		dataType:"json",
		success:function(result){
			if(result.result==1){
				var json=result.data;
				for(var i in json){
					$("#"+i).html(json[i]);
				}
			}
		},
		error:function(e){
			$.message.error('出现错误，请重试！');
		}
	});
}
/**
 * 初始化统计图
 */
function initLineChart30day(){
	$.ajax({
		url:ctx+"/api/store-profile/get-last30day-linechart.do?store_id="+store_id,
		type:"GET",
		dataType:"json",
		success:function(result){
			if(result.result==1){
				var conf=getPriceDisConfig(result.data,"最近30天销售走势");
				initLineChart("linechart-last-30-day",conf);
			}
		},
		error:function(e){
			$.message.error('出现错误，请重试！');
		}
	});
}


/**
 * 获取价格销量统计图配置
 * @param json 数据
 */
function getPriceDisConfig(json,ytitle){
	var conf = {}; //配置
//	var colors = Highcharts.getOptions().colors; // 颜色

	var data = []; // Y轴 排名数据
	var categories = []; //X轴 名次数据

	for(var i in json) {
		var order = json[i];
		
		//添加到数组
		data.push(order.t_money);
		categories.push("" + order.day);
	}

	var conf = {
		title : ytitle, //统计图标题
		yDesc : "下单金额", //y轴 描述
		//X 轴数据 [数组]
		categories : categories,
		//Y轴数据 [数组]
		series : [ {
			name : '下单金额',
			data : data
		} ]

	};
	return conf;
};