/**
 * create by jianghongyan 2016-06-27
 */
$(function(){
	getTotalMoneyAndNum();
	$("#export_button").click(function(){
		exportExcel();
	});
	if(Sys.ie == 11){
		$('#export_button').css({height: 30})
	}
});

function exportExcel(){
	var year =$("#year").val();
	var month=$("#month").val();
	var cycle_type=$("#cycle_type").val();
	/*$.ajax({
		url:"/b2b2c/api/operator-statistics/export-excel.do",
		type:"post",
		dataType:"json",
		data:{
			'year' : year,
			'month' : month,
			'cycle_type' : cycle_type,
			'storeid' : store_id
		},
		error:function(e){
			$.message.error("导出excel出错");
		}
	});*/

	window.location.href=ctx+"/api/operator-statistics/export-excel.do?year="+year+"&month="+month+"&cycle_type"+cycle_type+"&storeid="+store_id;
}

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
	            },
	            minTickInterval:1
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
 * 根据tabId刷新页面数据
 * @param tabId	tab页的id
 * @param startDate	条件：开始时间
 * @param endDate	条件：结束时间
 */
function refreshTab(tabId, startDate, endDate){
	getTotalMoneyAndNum();
	tabId = parseInt(tabId);
	//暂时tabId与函数方式定死  也许还有更好的方法
	switch(tabId) {
		case 1:
			getGoodsMoney(startDate, endDate);
			break;
		case 2:
			getGoodsNum(startDate, endDate);
			break;
		default:
			getGoodsMoney(startDate, endDate);
				
	}
	getGoodsMoneyDatagrid();
};

function getGoodsMoneyDatagrid(){
	var year =$("#year").val();
	var month=$("#month").val();
	var cycle_type=$("#cycle_type").val();
	$('#sales_money_datagrid').datagrid('load', {
		 year:year,
		 month:month,
		 cycle_type:cycle_type,
		 storeid:store_id,
		 page:1
   }); 
};

function getTotalMoneyAndNum(){
	var year =$("#year").val();
	var month=$("#month").val();
	var cycle_type=$("#cycle_type").val();
	$.ajax({
		url:ctx+"/api/operator-statistics/get-total-money-num.do",
		type:"POST",
		dataType:"json",
		data:{
			'year' : year,
			'month' : month,
			'cycle_type' : cycle_type,
			'storeid' : store_id
		},
		success:function(result){
			if(result.result == 1){
				var total_money = result.data.t_money, total_num = result.data.t_num;
				$('#total_money_num .order-money i').html(total_money);
				$('#total_money_num .order-num i').html(total_num);
			}else{
				$.message.error(result.message);
			}
		},
		error:function(e){
			$.message.error('出现错误，请重试！');
		}
	})
}


//扩展Date的format方法 
Date.prototype.format = function (format) {
    var o = {
        "M+": this.getMonth() + 1,
        "d+": this.getDate(),
        "h+": this.getHours(),
        "m+": this.getMinutes(),
        "s+": this.getSeconds(),
        "q+": Math.floor((this.getMonth() + 3) / 3),
        "S": this.getMilliseconds()
    }
    if (/(y+)/.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
        }
    }
    return format;
}
/**  
*转换日期对象为日期字符串  
* @param date 日期对象  
* @param isFull 是否为完整的日期数据,  
*               为true时, 格式如"2000-03-05 01:05:04"  
*               为false时, 格式如 "2000-03-05"  
* @return 符合要求的日期字符串  
*/  
function getSmpFormatDate(date, isFull) {
    var pattern = "";
    if (isFull == true || isFull == undefined) {
        pattern = "yyyy-MM-dd hh:mm:ss";
    } else {
        pattern = "yyyy-MM-dd";
    }
    return getFormatDate(date, pattern);
}

/**  
 *转换long值为日期字符串  
 * @param l long值  
 * @param isFull 是否为完整的日期数据,  
 *               为true时, 格式如"2000-03-05 01:05:04"  
 *               为false时, 格式如 "2000-03-05"  
 * @return 符合要求的日期字符串  
 */  

 function getSmpFormatDateByLong(l, isFull) {
     return getSmpFormatDate(new Date(l), isFull);
 }
 /**  
  *转换日期对象为日期字符串  
  * @param l long值  
  * @param pattern 格式字符串,例如：yyyy-MM-dd hh:mm:ss  
  * @return 符合要求的日期字符串  
  */  
  function getFormatDate(date, pattern) {
      if (date == undefined) {
          date = new Date();
      }
      if (pattern == undefined) {
          pattern = "yyyy-MM-dd hh:mm:ss";
      }
      return date.format(pattern);
  }
  /**
   * 格式化日期
   * @param value
   * @param row
   * @param index
   * @returns 符合要求的日期字符串
   */
 function formatCreateTime(value, row, index){
	 var time=row.createtime*1000;
	 return getSmpFormatDateByLong(time);
 }
 /**
  * 格式化状态
  * @param value
  * @param row
  * @param index
  * @returns 符合要求的状态标识
  */
 function formatStatus(value,row,index){
	 var flag=row.orderstatus;
	 switch (flag) {
	 	case 0:
	 		value="新建"
	 		break;
	 	case 1:
			value="已确认";
	 		break;
	 	case 2:
	 		value="已支付";
	 		break;
	 	case 3:
	 		value="已发货";
	 		break;
	 	case 4:
	 		value="已收货";
	 		break;
	 	case 5:
	 		value="已完成";
	 		break;
	 	case 6:
	 		value="订单取消";
	 		break;
	 	case 7:
	 		value="售后申请";
	 		break;
	 	default:
	 		value="错误";
	 		break;
	}
	 return value;
 }