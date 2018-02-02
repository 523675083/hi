var curRoomBalance=0;
$.fn.extend({
	DateQuary : function(opt) {
		if (!opt)
			opt = {};
		opt = {
			className : opt.className || '',
			html : opt.html
		};
		/**日历初始化*/
		var _this = $(".date_pane");
		var minDateTime = $("#minDateTime").val();
		var inventoryList =eval('('+$("#init").html()+')');
		$("."+opt.html).append("<div class='calendar_hd'> <span class='date_prev disabled'></span><span class='date_cur'><span class='years'></span>.<span class='month'></span><span class='date_next'></span> </div> <table class='calendar'> <tbody> <tr class='Week'> <th>周日</th><th>周一</th><th>周二</th><th>周三</th><th>周四</th><th>周五</th><th>周六</th> </tr> </tbody> </table>");
	
		/**日历重绘方法*/
		var ReloadDays = function(curyear, curmonth, curday) {
			$(".calendar .current").remove();
			var now = new Date(curyear, curmonth, curday);
			now.setDate(1);
			var firstday = now.getDay();
			var days = new Array(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);
			var IsCheckyear = function(year) {
				if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) {
					days[1] = 29;
				} else {
					days[1] = 28;
				}
				return days;
			};
			arrDays = IsCheckyear(curyear);
			monthdays = arrDays[curmonth];
			var html = [], count = 0;
			for (var i = 0; i < firstday; i++) {
				html.push('<td class=\"disable\"></td>');
				count++;
			}
		    var monthStr=(curmonth+1);
		    if(monthStr<10){
		        monthStr="0"+(curmonth+1);
		    }
		    if(i<10){
		        dayStr="0"+i;
		    }
		    var firstInvent=0;
			for (var i = 1; i <= monthdays; i++) {
				var seat="";
				var spans = "";
				var inventInfo = "";
				var isSeat = false;
				var isDataInfo = false;
		        for ( var j = 0; j < inventoryList.length; j++) {
		            if(curyear == toDate(inventoryList[j].date,"yyyy") && parseInt(monthStr, 10) == (inventoryList[j].date.month+1) && i == inventoryList[j].date.date){
		                remaining = "余位 "+inventoryList[j].surplusInventory;
		                inventInfo = inventoryList[j].surplusInventory + ',' + inventoryList[j].id+','+inventoryList[j].salePrice+','+inventoryList[j].childSalePrice+",null,"+inventoryList[j].roomBalance;
		                isDataInfo = true;
						if (parseInt(inventoryList[j].surplusInventory)>=9) {
							seat="9";
						}else{
							seat=inventoryList[j].surplusInventory;
						}
						if(seat>0 || seat==">9"){
							firstInvent++;
							isSeat = true;
							spans = "<span class='cld_price'>余位> "+seat+"</span><span class='cld_price'> <dfn>￥</dfn>"+inventoryList[j].salePrice+"起</span>";
						}
		            }
		        }
		        if(isDataInfo && isSeat ){
		    		var selDate= $("#h_depTime").val();
					var s="";
					var d=curyear+"-"+monthStr;
					var dayss=i;
					if(i<10){
						d+="-0"+i;
						dayss = "0"+dayss;
					}else{
						d+="-"+i;
					}
		        	if(selDate!=''&&selDate==d){
		       			 s='style="background:rgb(255, 204, 102)"';
		       		}else if(firstInvent ==1 && selDate==''){
						s = 'style="background:rgb(255, 204, 102)"';
						$("#h_depTime").val(curyear+"-"+monthStr+"-"+i);
		       		}
		        	html.push('<td '+s +' id="date_'+curyear+"-"+monthStr+"-"+dayss+'" onclick="getInvent(' + "'" + curyear+"-"+monthStr+"-"+dayss + "'" + ',' + inventInfo + ')">'+"<span>"+i+'</span><div  class="count">'  +spans);
		        }else if(isDataInfo&&!isSeat){
		        	html.push('<td class="on">' + i +spans+ '</td>');
		        }else{
		        	html.push('<td><p>' + i + '</p></td>');
		        }

		        count++;
			}
			var basenum = count > 35 ? 42 : 35;
			for (var i = 0; i < (basenum - count); i++) {
				html.push('<td class=\"disable\"></td>');
			}
			var html2 = [];
			var strr = "<tr class=\"current\">";
			var len = html.length;
			for (var x = 0; x < len; x++) {
				if (x % 7 != 6) {
					strr = strr + html[x];
				} else {
					strr = strr + html[x] + "</tr><tr class=\"current\">";
				}
			}
			html2.push(strr);
			return html2.join("\n");
		};
		
		/**日历调用*/
		var now ,year, month;
		var selDate= $("input[name='date']").val();
		if(selDate!=''){now = newDate(selDate);}else{now = newDate(minDateTime);}
		days = now.getDate(),year = now.getFullYear(),month = now.getMonth();	
		$(".years").html(year);		
		$(".month").html(month);
		var currDate=dataDisable("next");
		$(".Week").after(ReloadDays(currDate[0], currDate[1]-1, 1));
		_this.show();
		/**上个月*/
		$(".date_prev").click(function() {
			if(!$(this).hasClass('disabled')){
				var currDate=dataDisable("prev");
				$(".Week").after(ReloadDays(currDate[0], currDate[1]-1, 1));
				var month_value = $('.month').html();
				month_value = month_value > 9 ? month_value : "0" + month_value;
				$('.month').html(month_value);
			}else {
				var month_value = $('.month').html();
				$('.month').html(month_value);
			}
		});
		/**下个月*/
		$(".date_next").on("click",function() {
			if(!$(this).hasClass('disabled')){
				var currDate=dataDisable("next");
				$(".Week").after(ReloadDays(currDate[0], currDate[1]-1, 1));
				var month_value = $('.month').html();
				month_value = month_value > 9 ? month_value : "0" + month_value;
				$('.month').html(month_value);
			}else {
				var month_value = $('.month').html();
				$('.month').html(month_value);
			}
		});	
		/** 成人数量*/
		$(".a_box .list_num_inc").on("click",function() {
			adultChange("add");
		});
		$(".a_box .list_num_dec").on("click",function() {
			adultChange("sub");
		});	
		/** 儿童数量*/
		$(".c_box .list_num_inc").on("click",function() {
			childrenChange("add");
		});
		$(".c_box .list_num_dec").on("click",function() {
			childrenChange("sub");
		});
		
		/** 房间数量*/
		$(".r_box .list_num_inc").on("click",function() {
			roomChange("add");
		});
		$(".r_box .list_num_dec").on("click",function() {
			roomChange("sub");
		});
	}
}); 

// 显示方案
function showPlanbox(){
	$("#calendar_div").hide();
	$("#select_date_div").show();
}
function closePlan(){
	$("#calendar_div").show();
	$("#select_date_div").hide();
}

// 选择日期
function getInvent(depTime,remaining,id,price,child_price,flag,roomBalance) {
	var inventPlan = new Array();
	var inventoryList =eval('('+$("#init").html()+')');
	curRoomBalance=(roomBalance==null?0:roomBalance);
	if(flag==null){
		$(".p_tr").remove();
		for ( var i = 0; i < inventoryList.length; i++) {
			var invent=inventoryList[i];
			if(toDate(invent.date,"yyyy-MM-dd")==depTime){
				inventPlan.push(invent);
			}
		}
		if(inventPlan.length>1){
			var isShow=false;
			for ( var i = 0; i < inventPlan.length; i++) {
				var invent=inventPlan[i];
				if(invent.fligthRouteList.length>0){
					for ( var j = 0; j < invent.fligthRouteList.length; j++) {
						var route=invent.fligthRouteList[j];
						var tr ="<tr class='p_tr'><td>"+toDate(route.depDate,"yyyy-MM-dd")+"</td><td>"+route.depTime+"/"+route.arrTime+"</td><td rowspan='1'>锟�"+invent.salePrice+"</td><td rowspan='1'><a class='blue_btn' href='#' onclick='getInvent(" + '"' + depTime + '"' + ","+invent.surplusInventory+","+invent.id+","+invent.salePrice+","+invent.childSalePrice+",1,"+invent.roomBalance+")'>閫夋嫨</a></td></tr>";
						$("#panl_tr").append(tr);
						isShow=true;
					}
				}else{
					$("#panl_tr_time").hide();
					var tr ="<tr class='p_tr'><td>"+toDate(invent.date,"yyyy-MM-dd")+"</td><td rowspan='1'>锟�"+invent.salePrice+"</td><td rowspan='1'><a class='blue_btn' href='#' onclick='getInvent(" + '"' + depTime + '"' + ","+invent.surplusInventory+","+invent.id+","+invent.salePrice+","+invent.childSalePrice+",1,"+invent.vouchersRate+","+invent.roomBalance+")'>閫夋嫨</a></td></tr>";
					$("#panl_tr").append(tr);
					isShow=true;
				}
			}
			if(isShow){
			 showPlanbox();
			}
		}
	}else{
		closePlan();
	}
	var leastGroup = parseInt($("#leastGroup").val());
	// 选中
	$(".calendar .selected").removeClass("selected");
	$("#date_"+depTime).addClass("selected");
	$(".date_selected").html(depTime);
	$("input[name='date']").val(depTime);
	$("input[name='inventoryId']").val(id);
	// 成人信息
	$(".adult_price").html("楼"+price);
	$(".adult_number").html(leastGroup);
	$("#adult_price").html(price);
	$("#adult_number").html(leastGroup);
	$("#adult_number").attr("max",remaining);
	$("#adult_number").attr("min",leastGroup);
	$("input[name='adultNumber']").val(leastGroup);
	// 儿童信息
	var activity = $("#activity").val();
	if(child_price!=0&&activity=='false'){
		$("#child_number").html(0);
		$("#child_price").html(child_price);
		$("input[name='childNumber']").val(0);
		$(".child_number").html(0);
		$(".child_price").html("楼"+child_price);
	}else{
		$(".c_box").hide();
		$("input[name='childNumber']").val(0);
	}
	// 房间信息
	var r=0;
	var sumRoomPrice = 0;
	if(roomBalance!=''&&roomBalance>0){
		$("#room_price").html(roomBalance);
		r = (leastGroup / 2).toFixed(0);
		$("#room_number").html(r);
		$(".r_box .list_num_inc").attr("min",r);
		$(".r_box .list_num_inc").attr("max",leastGroup);
		$("input[name='roomNumber']").val(r);
		if(leastGroup==r){
			$(".r_box .list_num_inc").addClass("num_invalid");
		}
	}else{
		$(".r_box").hide();
		$("input[name='roomNumber']").val(0);
	}
	if(roomBalance!=''&&roomBalance>0){
		sumRoomPrice = (r - leastGroup / 2) * 2 * roomBalance;
	}
	if(sumRoomPrice>0){
		$(".single_room_number").text( r* 2-leastGroup);
		$("input[name='roomSubjoinPrice']").val(sumRoomPrice);
		$(".room_price").html("楼"+sumRoomPrice);
		$(".r_box").show();
		$(".room_box").show();
	}
	// 计算
	var sum=leastGroup*price+sumRoomPrice;
	$("input[name='totalPrice']").val(sum);
	$("#sum_price").html(sum);
	$(".sum_price").html("楼"+sum);
	$(".traveller_select_mod").show();
	$(".foot_order_info").show();
	
	// 滚动到选择出行人数区域
//	autoscroll($('.J_TravellerSelect'));
}

//滚动到区域
//function autoscroll(selector) {
//  $('html,body').animate({
//      scrollTop: $(selector).offset().top
//  },200);
//}

function adultChange(type){
	var max=$("#adult_number").attr("max");
	var min=$("#adult_number").attr("min");
	var roomBalance = curRoomBalance;
	var child_number=parseInt($("#child_number").html());
	var number=parseInt($("#adult_number").html());
	if(type=='add'){
		if(number<(max-child_number)){
			number+=1;
			$("#adult_number").html(number);
			$(".adult_number").html(number);
			if(number==(max-child_number)){
				$(".a_box .list_num_inc").addClass("num_invalid");
			}
		}
		if(number>min){
			$(".a_box .list_num_dec").removeClass("num_invalid");
		}
	}else if(type=='sub'){
		if(number>min){
			number-=1;
			$("#adult_number").html(number);
			$(".adult_number").html(number);
			if(number==min){
				$(".a_box .list_num_dec").addClass("num_invalid");
			}
		}
		if(number<(max-child_number)){
			$(".a_box .list_num_inc").removeClass("num_invalid");
		}
	}
	if(roomBalance!=''&&roomBalance>0){
		var r = (number / 2).toFixed(0);
		$("#room_number").html(r);
		$("input[name='roomNumber']").val(r);
		$("#room_number").attr("min",r);
		$("#room_number").attr("max",number);
		if(number==r){
			$(".r_box .list_num_inc").addClass("num_invalid");
		}
		if(r<number){
			$(".r_box .list_num_inc").removeClass("num_invalid");
		}
	}
	$("input[name='adultNumber']").val(number);
	calculate();
}

function childrenChange(type){
	var max=$("#adult_number").attr("max");
	var min=$("#adult_number").attr("min");
	var number=parseInt($("#adult_number").html());
	var child_number=parseInt($("#child_number").html());
	if(type=='add'){
		if(child_number<(max-number)){
			child_number+=1;
			$("#child_number").html(child_number);
			$(".child_number").html(child_number);
			if(number==max){
				$(".c_box .list_num_inc").addClass("num_invalid");
			}
		}
		if(child_number>0){
			$(".c_box .list_num_dec").removeClass("num_invalid");
		}
	}else if(type=='sub'){
		if(child_number>0){
			child_number-=1;
			$("#child_number").html(child_number);
			$(".child_number").html(child_number);
			if(number==min){
				$(".c_box .list_num_dec").addClass("num_invalid");
			}
		}
		if(child_number<(max-number)){
			$(".c_box .list_num_inc").removeClass("num_invalid");
		}
	}
	$("input[name='childNumber']").val(child_number);
	calculate();
}

function roomChange(type){
	var max=$("#room_number").attr("max");
	var min=$("#room_number").attr("min");
	var number=parseInt($("#room_number").html());
	if(type=='add'){
		if(number<max){
			number+=1;
			$("#room_number").html(number);
			if(number==max){
				$(".r_box .list_num_inc").addClass("num_invalid");
			}
		}
		if(number>min){
			$(".r_box .list_num_dec").removeClass("num_invalid");
		}
	}else if(type=='sub'){
		if(number>min){
			number-=1;
			$("#room_number").html(number);
			if(number==min){
				$(".r_box .list_num_dec").addClass("num_invalid");
			}
		}
		if(number<max){
			$(".r_box .list_num_inc").removeClass("num_invalid");
		}
	}
	$("input[name='roomNumber']").val(number);
	calculate();
}

/** 璁＄畻浠锋牸*/
function calculate(){
	var sumRoomPrice = 0;
	var adult_price=$("#adult_price").html();
	var adult_number=$("#adult_number").html();
	var child_price=$("#child_price").html();
	var child_number=$("#child_number").html();
	var room_number= $("#room_number").html();
	var discount = $("#discount").val();
	var roomBalance = curRoomBalance;
	if(roomBalance!=''&&roomBalance>0){
		sumRoomPrice = (room_number - adult_number / 2) * 2 * roomBalance;
	}
	if(child_number>0){
		$(".child_box").show();
	}else{
		$(".child_box").hide();
	}
	if(sumRoomPrice>0){
		$(".room_box").show();
		$(".single_room_number").text( room_number* 2-adult_number);
		$("input[name='roomSubjoinPrice']").val(sumRoomPrice);
	}else{
		$(".room_box").hide();
		$("input[name='roomSubjoinPrice']").val(0);
	}
	var sum=adult_price*adult_number+child_price*child_number+sumRoomPrice;
	var activityAgencyCode=$("#activityAgencyCode").html();
	var productAgencyCode=$("#productAgencyCode").html();
	var activityNum=$("#activityNum").val();
	if(activityAgencyCode==productAgencyCode){
		sum=sum-adult_price*(Math.floor(adult_number/activityNum));
	}
	// 浼樻儬鐮�
	if(discount!=''){
		var discount_text;
		if(discount=='0.1'){
			sum = 10;
			$(".discount_text").text("鐗逛环10周六");
		}else{
			sum = sum*discount;
			sum = sum.toFixed(1);
			$(".discount_text").text(Number(discount*10)+"鎶�");
		}
		$(".discount_box").show();
	}
	$("input[name='totalPrice']").val(sum);
	$("#sum_price").html(sum);
	$(".sum_price").html("楼"+sum);
}

/** 鍒ゆ柇鏄惁绂佺敤*/
function dataDisable(type){
	var next = false;
	var prev = false;
	var _this=$(".date_pane");
    var minYMD = $("#minDateTime").val().split('-');
    var maxYMD = $("#maxDateTime").val().split('-');
    var curryear = parseInt(_this.find(".years").html());
	var currmonth = parseInt(_this.find(".month").html(), 10);
	if(type=='next'){currmonth+=1;}else if(type=='prev'){currmonth-=1;}
	if(curryear < maxYMD[0]){next = true;}else if(curryear == maxYMD[0]){if(currmonth < maxYMD[1]){next = true;}}
	if(next){
		if (currmonth == 15) {
            curryear = curryear + 1;
            currmonth = 1;
        }
		$(".date_next").removeClass("disabled");
	}else{
		$(".date_next").addClass("disabled");
	}
	if(curryear > minYMD[0]){prev = true;}else if(curryear == minYMD[0]){if(currmonth > minYMD[1]){prev = true;}}
	if(prev){
		if (currmonth == -1) {
            curryear = curryear - 1;
            currmonth = 12;
        }
		$(".date_prev").removeClass("disabled");
	}else{
		$(".date_prev").addClass("disabled");
	}
	_this.find(".month").html(currmonth);
	var currDate=[curryear,currmonth];
	return currDate;
}

Date.prototype.format = function(format) {
    var o = {
        "M+" : this.getMonth() + 1,
        "d+" : this.getDate(),
        "h+" : this.getHours(),
        "m+" : this.getMinutes(),
        "s+" : this.getSeconds(),
        "q+" : Math.floor((this.getMonth() + 3) / 3),
        "S" : this.getMilliseconds()
    };
    
    if (/(y+)/.test(format)) {
            format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4- RegExp.$1.length));
        }
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(format)){
            format = format.replace(RegExp.$1, RegExp.$1.length == 1? o[k]:("00" + o[k]).substr(("" + o[k]).length));
        }
    }
    return format;
    
    
};

function toDate(objDate, format) {
    var date = new Date();
    date.setTime(objDate.time);
    date.setHours(objDate.hours);
    date.setMinutes(objDate.minutes);
    date.setSeconds(objDate.seconds);
    return date.format(format);
}

function newDate(str) { 
	str = str.split('-'); 
	var date = new Date(); 
	date.setUTCFullYear(str[0], str[1] - 1, str[2]); 
	date.setUTCHours(0, 0, 0, 0); 
	return date; 
}	