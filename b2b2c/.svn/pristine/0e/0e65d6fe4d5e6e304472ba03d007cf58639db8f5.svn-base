/**
 * create by jianghongyan 2016-08-03
 */


/**
/**
 * 出售右侧横向highchart柱状图图
 */
function initHistogram(id,conf){
	var options = {
			chart: {
	            type: 'column'
	        },
			credits: {
				enabled:false
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
				},
				minTickInterval:1
			},
			series : conf.series
		};
	$("#" + id).highcharts(options);
}
/**
 * 根据tabId刷新页面数据
 * @param tabId	tab页的id
 * @param startDate	条件：开始时间
 * @param endDate	条件：结束时间
 */
function refreshTab(tabId, startDate, endDate){
	
	var tabId = parseInt(tabId);
	//暂时tabId与函数方式定死  也许还有更好的方法
	switch(tabId) {
		case 1:
			getOrderPrice();
			break;
		case 2:
			 getGoodsNum();
			break;
		default:
			getOrderPrice();
	}
};