var sUserAgent = navigator.userAgent.toLowerCase();
var isIphone = sUserAgent.match(/iphone/i) == "iphone";
function appendParam(form, params) {
	for (var p in params) {
		var temp = document.createElement("input");
		temp.type = "hidden";
		temp.name = p;
		temp.value = params[p];
		form.appendChild(temp);
	}
}

function putParam(form, params) {
	for (var p in params) {
		var temp = $(":input[name='" + p + "']", $(form));
		if (temp.get() == "") {
			temp = $("<input type='hidden' name='" + p + "'/>");
			$(form).append(temp);
		}
		temp.val(params[p]);
	}
}

function buildForm(action, method, params) {
	var dynamic_form = document.forms["dynamic_form"];
	if (dynamic_form == null) {
		dynamic_form = document.createElement("form");
		dynamic_form.name = "dynamic_form";
		document.body.appendChild(dynamic_form);
	}
	$(dynamic_form).empty();
	dynamic_form.action = action;
	dynamic_form.method = method;
	appendParam(dynamic_form, params);
	return dynamic_form;
}

/**
 * 替换尖括号 如 < 替换成 &lt; >替换成  &gt<
 * @param html
 * @return string
 */
function replaceAngleBrackets(html){
	if(typeof(html)=="string" && html.length>0){
		html = html.replace(/</g, " &lt;");
		html = html.replace(/>/g, " &gt;");
		return html;
	}
	return "";
}
/**
 * 参数说明：
 * 1、type： 提示类型（1:警告,4:成功,5:错误,6:等待中）
 * 2、content ：提示内容
 * 3、autoClose ：是否自动关闭（true：2秒后自动关闭；false：不自动关闭；）
 * 注：可使用closeMsg（）关闭打开的提示框
 */
function showMsg(type, content, autoClose) {
	if (content != "") {
		if (autoClose) {
			ZENG.msgbox.show(content, type, 1500);
		} else {
			ZENG.msgbox.show(content, type);
		}
	}
}
function closeMsg() {
	ZENG.msgbox._hide();
}

//简单提示
function showAlert(str, timeout, callback){
    var tips = $('<div class="m-layer"><div class="m-popup-tips slideDown">'+str+'</div></div>'),
        noop = function(){},
        fn = callback || noop;

    // 添加提示
    $('body').append(tips);
    
    tips.on('click', function(){
    	tips.remove();
    	fn();
    });

    // 删除提示
    setTimeout(function(){
        tips.remove();
        fn();
    }, timeout || 1500);
}

// 加载等待
function loadingPage(){
	var tips = '<div class="loadingPage" style="top: 0px;position: absolute;width: 100%;height: 100%;z-index: 99999;"><div class="preloader-indicator-modal loadingPage"> <span class="preloader preloader-white loading"></span> </div></div>';
	$('body').append(tips);
}
// 隐藏加载动画
function removeLoadingPage(){
	$(".loadingPage").remove();
}

function getObjectURL(file) {
    var url = null ; 
    if (window.createObjectURL!=undefined) { // basic
        url = window.createObjectURL(file) ;
    } else if (window.URL!=undefined) { // mozilla(firefox)
        url = window.URL.createObjectURL(file) ;
    } else if (window.webkitURL!=undefined) { // webkit or chrome
        url = window.webkitURL.createObjectURL(file) ;
    }
    return url ;
}
function charNumLimit(val,limitNumber,replaceStr){
	if(!val){
		return "";
	}
	if(val.length<=limitNumber)
		return val;
	else{
		return val.substr(0,limitNumber)+replaceStr;
	}
}

$(function(){
	if(isIphone){
		FastClick.attach(document.body);
	}
	$(".show-input input").keyup(function(){
		$(this).next().hide();
		if($(this).val().length>0){
			$(this).prev().hide();
		}else{
			$(this).prev().show();
		}
	});
	// 客户经理
	$('.J-alert').click(function(){
		$('.popup-con').addClass('on');
		$('.popup-overlay').addClass('opacityOne');
	});
	$('.popup-overlay,.J-close').click(function(){
		$('.popup-con').removeClass('on');
		$('.popup-overlay').removeClass('opacityOne');
	});
	// 统计
	$(".recommend a").click(function(){
		var params={
				type:4,
				subtype:0,//选货分销活动产品
				"detail['buttonName']":$(this).attr("name"),
				"detail['recommendType']":$(this).attr("type"),
				accessAddress:$(this).attr("href"),
				previousPageUrl:document.referrer
		};
		Statistics.commonStatis(params);
	});
	$(".tab-nav-product ul li").click(function(){
		var params={
				type:4,
				subtype:1,//选货分销推荐标签
				"detail['buttonName']":$(this).attr("name"),
				"detail['recommendType']":$(this).attr("data"),
				accessAddress:"recommendProduct.htm?recommend="+$(this).attr("data"),
				previousPageUrl:document.referrer
		};
		Statistics.commonStatis(params);
	});
	$(document).on("click",".statistics-commonbutton",function(){
		var params={
				type:4,
				subtype:2,//普通按钮
				"detail['buttonName']":$(this).attr("statistics-button"),
				"detail['subtype']":$(this).attr("statistics-subtype"),
				accessAddress:$(this).attr("href"),
				previousPageUrl:document.referrer
		};
		Statistics.commonStatis(params);
	});
	$(".key-words a").click(function(){
		var key=$(this).html();
		var params={
				type:4,
				subtype:3,//选货分销产品搜索—热门关键词
				"detail['keyword']":key,
				"detail['buttonName']":key,
				accessAddress:"product.htm?keyWord="+key,
				previousPageUrl:document.referrer
		};
		Statistics.commonStatis(params);
	});
	$(".statistics-product-inputsearch").click(function(){
		var key=$(".statistics-product-inputsearch-keyword").val();
		var params={
				type:4,
				subtype:4,//选货分销产品搜索-手动输入
				"detail['keyword']":key,
				"detail['buttonName']":key,
				accessAddress:"product.htm?keyWord="+key,
				previousPageUrl:document.referrer
		};
		Statistics.commonStatis(params);
	});
	$(document).on("click",".ser-list-show a",function(){
		var key=$(this).children("span:first").html();
		var params={
				type:4,
				subtype:5,//选货分销产品搜索-历史关键词搜索
				"detail['keyword']":key,
				"detail['buttonName']":key,
				accessAddress:"product.htm?keyWord="+key,
				previousPageUrl:document.referrer
		};
		Statistics.commonStatis(params);
	});
	$(".city-list").click(function(){
		var key=$(this).children(".city-list-name").html();
		var $level=$(this).closest(".country");
		var params={
				type:4,
				subtype:6,//选货分销找产品页目的地搜索
				"detail['arrCity']":key,
				"detail['level2']":$level.attr("level2"),
				"detail['level1']":$level.attr("level1"),
				"detail['buttonName']":key,
				accessAddress:"product.htm?arrCity="+key,
				previousPageUrl:document.referrer
		};
		Statistics.commonStatis(params);
	});
	$(".hot_supply a").click(function(){
		var $level=$(this).closest(".hot_supply");
		var params={
				type:4,
				subtype:7,//选货分销-推荐热门供应
				"detail['supplyName']":$(this).html(),
				"detail['level2']":$level.attr("data"),
				"detail['buttonName']":$(this).html(),
				accessAddress:$(this).attr("href"),
				previousPageUrl:document.referrer
		};
		Statistics.commonStatis(params);
	});
	$(document).on("click",".shoplist-item a",function(){
		var params={
				type:4,
				subtype:8,//选货分销-按供应商找
				"detail['supplyName']": $(this).attr("data"),
				"detail['level2']":"按供应商",
				accessAddress:$(this).attr("href"),
				"detail['buttonName']": $(this).attr("data"),
				previousPageUrl:document.referrer
		};
		Statistics.commonStatis(params);
	});
	$(document).on("click",".classification-items a",function(){
		var $level=$(this).closest(".classification-items");
		var params={
				type:4,
				subtype:9,//选货分销-按主营线路找
				"detail['supplyName']": $(this).attr("name"),
				"detail['level2']":$level.attr("data"),
				"detail['level1']":"按主营线路",
				"detail['buttonName']":$(this).attr("name"),
				accessAddress:$(this).attr("href"),
				previousPageUrl:document.referrer
		};
		Statistics.commonStatis(params);
	});
	$(".statistics-supply-search").click(function(){
		var params={
				type:4,
				subtype:10,//选货分销-手动输入供应商查找
				"detail['supplyName']": $("input[name='keyWord']").val(),
				"detail['buttonName']": $("input[name='keyWord']").val(),
				accessAddress:"asyncSupply.htm",
				previousPageUrl:document.referrer
		};
		Statistics.commonStatis(params);
	});
	$("#submit").click(function(){
		var productType = $(".p-type li i.icon-checked").parent().attr("data");
		var sort = $(".p-order li i.icon-checked").parent().attr("data");
		var day = $(".p-day .ul-day li i.icon-checked").parent().attr("data");
		var price = $(".p-more .ul-price li i.icon-checked").parent().attr("data");
		var theme = $(".p-more .ul-theme li i.icon-checked").parent().attr("data");
		var month = $(".p-day .ul-date li i.icon-checked").parent().attr("data");
		var depCity=$("#search-depCode").val();
		switch(productType)
		{
		case '1':
		  productType="特价机票";
		  break;
		case '2':
			productType="出发地参团";
		  break;
		case '3':
			  productType="目的地参团";
			  break;
		case '4':
			productType="自由行";
			  break;
		default:
			productType="全部";
		}
		var params={
				type:4,
				subtype:11,//选货分销-产品筛选
				"detail['supplyName']": $("input[name='keyWord']").val(),
				accessAddress:"prodctAsyn.htm",
				"detail['productType']":productType,
				"detail['sort']":sort==''?'全部':sort,
				"detail['day']":day==''?'全部':day,
				"detail['price']":price==''?'全部':price,
				"detail['theme']":theme==''?'全部':theme,
				"detail['month']":month==''?'全部':month,
				"detail['depCity']":depCity,
				"detail['agencyId']":$(".statistics-agencyId").attr("statistics-agencyId"),
				previousPageUrl:document.referrer
		};
		Statistics.commonStatis(params);
	});
	$(document).on("click",".statistics-dkxd",function(){
		var params={
				type:4,
				subtype:12,//选货分销-代客下单
				"detail['buttonName']":"代客下单",
				"detail['productNo']": $(this).attr("statistics-productNo"),
				"detail['proxyAgencyId']":$(this).attr("statistics-proxyAgencyId"),
				"detail['productAgencyId']":$(this).attr("statistics-productAgencyId"),
				accessAddress:$(this).attr("href"),
				previousPageUrl:document.referrer
		};
		Statistics.commonStatis(params);
	});
	$(".statistic-supply-productSearch").click(function(){
		var params={
				type:4,
				subtype:13,//选货分销-供应商主页产品搜索
				"detail['keyword']": $("#keyword").val(),
				"detail['buttonName']":$("#keyword").val(),
				"detail['agencyId']":$(this).attr("statistics-agencyId"),
				accessAddress:"prodctAsyn.htm",
				previousPageUrl:document.referrer
		};
		Statistics.commonStatis(params);
	});
	$(document).on("click",".searchbar-down-item",function(){
		var params={
				type:4,
				subtype:14,//选货分销-顶部出发地
				"detail['arrCity']": $(this).attr("data-code"),
				"detail['buttonName']":$(this).attr("data-code"),
				previousPageUrl:document.referrer
		};
		Statistics.commonStatis(params);
	});
	$(document).on("click",".statistics-listcell",function(){
		var params={
				type:4,
				subtype:15,//选货分销-点击产品列表产品
				"detail['productName']": $(this).attr("statistics-productName"),
				"detail['productNo']": $(this).attr("statistics-productNo"),
				"detail['buttonName']":$(this).attr("statistics-productName"),
				"detail['productListName']":$(this).closest(".statistics-productlist").attr("statistic-listname"),
				accessAddress:$(this).attr("href"),
				previousPageUrl:document.referrer
		};
		Statistics.commonStatis(params);
	});
	//16 详情页分享
	//17底部按钮
	//统计结束
	// 主页菜单切换
	$(".footbar-box-item").click(function(){
		var href = $(this).attr("href");
		if(href&&href!=''){
			loadingPage();
			window.location.href = $(this).attr("href");
		}
	});
	// 隐藏
	$(".warp_z").click(function(){
		hideBox();
	});
	// 抽屉滑动
	$(".user-head").click(function(){
		if($(".pull-out").length>0){
			$("body").removeClass("pull-out");
		}else{
			$("body").addClass("pull-out");
		}
	});
});
function hideBox(){
	$(".dropbar-box-item").hide();
	$(".dropbar-box-foot").hide();
	$(".warp_z").hide();
	$(".trangle-move").remove();
	$(".dropbar").removeClass("on");
	$(".J-trangle").css("margin-top","0");
}
function showPrompt(cls,data){
	var li = $("li[data='"+cls+"']");
	if(data!=''){
		li.addClass("on");
	}else{
		li.removeClass("on");
	}
}
function subscript(m_cls,c_cls,c_cls1,data,data1){
	var li = $("li[data='"+m_cls+"']"); // tab头
	var span = $("li[data='"+c_cls+"']").find("span"); // 子tab
	var span1 = $("li[data='"+c_cls1+"']").find("span"); // 子tab1
	if(data!=''||data1!=''){
		li.addClass("on");
	}else{
		li.removeClass("on");
	}
	if(data!=''){
		span.addClass("on");
	}else{
		span.removeClass("on");
	}
	if(data1!=''){
		span1.addClass("on");
	}else{
		span1.removeClass("on");
	}
}
/** 统计*/
var Statistics={
	access:function(pageUrl0,productNo0){
		var url="/service/statistics/access";
		var params={
		   		pageUrl:pageUrl0,
		   		productNo:productNo0,
		   		previousPageUrl:document.referrer
		};
		jQuery.post(url, params, function(){});
	},
	commonStatis:function(params){
		var url="/service/statistics/commonStatis";
		jQuery.post(url, params, function(){});
	}
};
//确认取消代理
function confirmCancelProxy(){
	var so = new Framework7();
	$("#delProxy").hide();
	var productId = $("#productId").val();
	var action = "priceOptions.htm";
	loadingPage();
	 p={
	    action : "delProxy",
		pid : productId
	 };
	 jQuery.post(action, p, function(data) {
		    removeLoadingPage();
			if (data.code==0) {
				 $("#"+productId).find(".editPrice_a").html("代理产品");
				 $("#"+productId).find(".editPrice_a[type='hide']").css("background-color","#21C4FF");
				 $("#"+productId).find(".editPrice_a[type='show']").css("color","#21C4FF");
				 so.swipeoutClose($('.swipeout-opened'), function () {
					 showAlert("已取消");
				 });
				 return true;
			}else{
				showAlert(data.message);
			}
	},"json");
}
/**
 * 
 * @param title 取消代理弹窗要显示的信息
 * @param fun 确定取消代理回调函数
 * @param fun_cancel 取消操作回调
 * @param msg 提示消息
 */
function showIsConfirmBox(title,fun_confirm,fun_cancel,msg){
	var so = new Framework7();
	$("#delProxy").show();
	$("#delProxy #modal_title").html(title);
	if(msg != null && msg != ""){
		$("#delProxy #modal-text").html(msg);
	}
	var scancel=fun_cancel||function(){ $("#isConfimBox").hide();};
	var fun = fun_confirm||confirmCancelProxy;
	$(".delProxy_confirm").off();
	$(".delProxy_confirm").on("click",function(){
		var k=fun();
		if(k){
			$("#delProxy").hide();
		};
	});
	//取消删除
	$(".delProxy_cancel").off();
	$(".delProxy_cancel").on("click",function(){
		so.swipeoutClose($('.swipeout-opened'), function () {
			scancel();
			$("#delProxy").hide();
		});
	});
}