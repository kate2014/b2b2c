/**
 * create by xuliepng 2016年12月06日
 */
$(function(){
	loadHighcharts(c_chart,"",null);
	
	//刷新甘特图
	$.ajax({
        type: "post",
        url: ctx+"/api/store-profile/get-collect-chart-json.do?storeId="+store_id,
        dataType: "json",
        success: function (data) {
        	collect_chart.series[0].setData(data);
        },
        error: function (msg) {
            alert("出现错误，请稍后重试");
        }
    });
	
});

//生成图表
function loadHighcharts(obj,text,data){
	collect_chart = new Highcharts.Chart({
        chart: {
            type: 'column',
            renderTo: obj
        },
        title: {
            text: '收藏商品排行Top50'
        },
        xAxis: {
        	tickInterval: 1
        },
        yAxis: {
            title: {
                text: "收藏数"
            }
        },
        plotOptions: {
            line: {
                dataLabels: {
                    enabled: true
                },
                enableMouseTracking: false
            }
        },
        credits: {  
            enabled: false 	// remove high chart logo hyper-link  
        },
        series: [{
        	name: "收藏数",
        	colorByPoint: true,
            data: data,
            showInLegend: false,
        	pointStart: 1
        }]
        // data: [{'color':'#F6BD0F','y':11}, {'color':'#333','y':6.9}]
    });
}
