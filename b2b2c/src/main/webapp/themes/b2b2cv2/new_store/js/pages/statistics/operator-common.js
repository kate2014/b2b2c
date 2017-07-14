$(function() {
	//初始化下拉菜单中的时间
	var currentYear = new Date().getFullYear();
	var historyYear = currentYear-10;
	var currentmonth = new Date().getMonth();
	currentmonth+=1;
	//for循环得到年数
	for(var i=0;i<20;i++){
		
		//选中当前年
		if(currentYear==historyYear){
			$("#year").append("<option value='"+historyYear+"' selected >"+historyYear+"年</option>" );//selected 加入可得到当前年份
		}else{
			$("#year").append("<option value='"+historyYear+"' >"+historyYear+"年</option>" );
		}
		historyYear++;
	}
	
	//for循环得到月份
	for(var i=1;i < 13;i++){
		
		// 选中当前月
		if(currentmonth==i){
			$("#month").append("<option value='"+i+"' selected >"+i+"月</option>" );  
		}else{
			$("#month").append("<option value='"+i+"' >"+i+"月</option>" );
		}
	}
	
	//默认加载第一个tab页
	loadTab(1);
	// 选项卡 单击事件
	$(".app-tab-tools>ul>li").click(function() {
		var tabId = $(this).attr("tabid");
		$(".app-tab-tools>ul>li").removeClass("active");
		$(".app-tab-tools>ul>li").addClass("normal");
		$(this).removeClass("normal");
		$(this).addClass("active");
		$(".tab-page .tab-panel").hide();
		
		$("#now_tab_hid").val(tabId);	//记录当前tabId
		loadTab(tabId);
	});
	
	// 搜索点击事件
	$("#search_statis").click(function(){
		// 1.获取时间条件
		var dateWhere = getDateWhere();
		//如果没有时间条件 就不用刷新
		if(!dateWhere) {
			return;
		}
		
		// 2.记录这个页需要刷新
		$(".now_tab_data_hid").val("1");
		var tabId = $("#now_tab_hid").val();
		// 3.刷新数据页面
		refreshTab(tabId, dateWhere[0], dateWhere[1]);
		
		// 4.记录这个页不用刷新
		$(".now_tab_data_hid[tab_id='" + tabId + "']").val("0");
	});
	
	//当查询方式改变时 改变选择框
	
	$("#cycle_type").change(function(){
		var ct=$(this);
		var cycle_type_value=ct.val();
		if(parseInt(cycle_type_value)==1){
			$("#month").show();
		}
		if(parseInt(cycle_type_value)==2){
			$("#month").hide();
		}
	});
	
});

/**
 * 获取日期条件
 * @return dateWhere[] 下标0=开始时间  下标1=结束时间
 */
function getDateWhere(){
	
	var c_type = $("#cycle_type").val();
	var startDate, endDate, statisticsType; //开始时间和结束时间 和统计类型
	
	if (c_type == 0) {
		$.message.error('请先选择查询方式!');
		return;
	}
	
	//如果是按照年来筛选订单的
	if(c_type == 2) {
		statisticsType = "1";
		var year = $("#year").val();
		
		if(year == 0) {
			var dateWhere = [];
			dateWhere[0] = "";
			dateWhere[1] = "";
			return dateWhere;
		}
		
		startDate = year + "-01-01 0:0:01";
		endDate = year + "-12-31 23:59:59";
		
	} else {
		statisticsType = "0";
		var year = parseInt($("#year").val());
		var month = parseInt($("#month").val());

		if(year == 0) {
			$.message.error('请选择年份！');
			return;
		}
		if(month == 0) {
			$.message.error('请选择月份！');
			return;
		}
		
		//得到一个月最后一天
		var lastDate = new Date(year, month, 0);
		var lastDay = lastDate.getDate();
		
		startDate = year + "-" + month + "-01 0:0:01";
		endDate = year + "-" + month + "-" + lastDay +" 23:59:59";
	}
	var dateWhere = [];
	dateWhere[0] = startDate;
	dateWhere[1] = endDate;
	dateWhere[2] = statisticsType;
	return dateWhere;
};

/**
 * 加载tab页
 * @param tabId
 */
function loadTab(tabId){
	var $tab = $(".tab-panel[tabid=" + tabId + "]");
	if($tab.html() == '') {
		$.ajax({
			url : $tab.attr("h_url"),
			type:'get',
			success:function(data){
				$tab.html(data);
			}
		});
	} else {
		var isRefresh = $(".now_tab_data_hid[tab_id='" + tabId + "']").val();
		if (isRefresh == "1") {
			// 1.获取时间条件
			var dateWhere = getDateWhere();
			//如果没有时间条件 就不用刷新
			if(!dateWhere) {
				return;
			}
			// 2.刷新数据页面
			refreshTab(tabId, dateWhere[0], dateWhere[1]);
			// 3.记录这个页不用刷新
			$(".now_tab_data_hid[tab_id='" + tabId + "']").val("0");
		}
	}
	$tab.show();
};