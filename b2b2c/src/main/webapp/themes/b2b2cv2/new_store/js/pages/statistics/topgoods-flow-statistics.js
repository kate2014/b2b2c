/**
 * create by jianghongyan 2016-08-04
 * 流量统计-店铺总流量js
 */
var topNum=30;
$(function(){
	getTopGoodsFlowStatistics();
});



/**
 * 初始化柱状图
 * @param id	html 初始化div的id
 * @param conf	相关配置
 */
function initHistogram(id,conf){

	var options = {
			credits: {
	             //text: 'Javashop',
	             //href: 'http://www.javamall.com.cn'
				enabled:false
	        },
			chart : {
				type : 'column'
			},
			title : {
				text : conf.title
			},
			xAxis : {
				categories : conf.categories
			},
			yAxis : {
				min : 0,
				title : {
					text : conf.yDesc
				}
			},
			plotOptions: {
	            column: {
	                
	                dataLabels: {
	                    enabled: true,
	                    style: {
	                        fontWeight: 'bold'
	                    },
	                    formatter: function() {
	                        return this.y +'次';
	                    }
	                }
	            }
	        },
			series : conf.series
		};
	$("#" + id).highcharts(options);
};

/**
 * 获得总流量统计相关配置
 * @param json 数据
 */
function getFlowConfig(json){
	
	var conf = {};			//配置
	var num = topNum;								// top几
	var colors = Highcharts.getOptions().colors;	// 颜色

	var data = [];	// Y轴 排名数据
	var categories = []; //X轴 名次数据
	
	// 遍历生成 data,categories
	for(var i in json) {
		var member = json[i];
		var temp = {
			name:member.name,
			color: colors[0],
	        y: member.num
		};
		
		//添加到数组
		data.push(temp);
		categories.push(parseInt(i) + 1);
	}
	
	var conf = {
		title : "商品访问量Top" + num ,		//统计图标题
		yDesc : "访问量（次）" ,			//y轴 描述
										//X 轴数据 [数组]
		categories : categories,				
            							//Y轴数据 [数组]
		series : [
			{
				name : '访问量', 
				data: data,
            	color: 'white'
			}
		]						

	};
	return conf;
};
function refreshTab(tabId, startDate, endDate){
	getTopGoodsFlowStatistics(startDate, endDate);
}

function getTopGoodsFlowStatistics(){

	var year = $("#year").val();
	var month = $("#month").val();
	var cycle_type = $("#cycle_type").val();

	// ajax配置
	var options = {
		url : ctx+"/api/flow-statistics/get-topgoods-flow-statistics.do",
		data : {
			'year' : year,
			'month' : month,
			'cycle_type' : cycle_type,
			'storeid' : store_id
		},
		type : "post",
		dataType : "json",
		success : function(data) {

			//如果获得正确的数据
			if (data.result == 1) {

				

				// 1.获取到统计图相关配置
				var conf = getFlowConfig(data.data,cycle_type);

				// 2.初始化统计图
				initHistogram("topgoods_flow_histogram", conf);

			} else {
				$.message.error("调用action出错：" + data.message);
			}
		},
		error : function() {
			$.message.error("系统错误，请稍后再试");
		}
	};
	$.ajax(options);
}