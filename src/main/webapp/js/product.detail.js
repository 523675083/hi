$(function() {
   $(".back-prev-c").attr("href","index.htm");
   //浜у搧鍒嗕韩鏄剧ず闅愯棌
	$('.J_contact-box').on('click',function(){
		$('.m-share').removeClass('hide');
	});
	$(".jump").on('click',function(){
		$('.m-share').addClass('hide');
	});
    // 鏃ュ巻鎺т欢
    $(".controller-datePick").DateQuary({
        className: "controller-datePick",
        html: "date_pane"
    });
    // 鏌ョ湅鏃ュ巻
    $(".show_date,.controller-booking").click(function() {
        showDate();
    });
    // 閫夋嫨鏃ユ湡
    $(".controller-selectDate").click(function() {
        showDate();
    });
    //鍚戜笅婊氬姩鏃舵樉绀鸿仈绯讳汉鎸夐挳
    $(window).scroll(function() {
       
        var scrollTop = $(document).scrollTop();
        if (scrollTop > 0) {
           
            $('.goback').show();
        } else {
            
            $('.goback').hide();
        }
    });
	$('.goback').click(function(){
		$("html,body").animate({
			"scrollTop": 0
		}, 300)
	});

    // 鑱旂郴浜哄垪琛ㄧ殑鏄剧ず闅愯棌
    $('.J_contact-box').on('click',
    function() {
        var targetDom = $(".J_contact-list");
        if (targetDom.css("display") == "none") {
            $(".contact-btn").css("color", "#0D63E2");
            targetDom.show();
        } else {
            $(".contact-btn").css("color", "#fff");
            targetDom.hide();
        }
    });
    
    // 淇℃伅鐩掑瓙灞曠ず闅愯棌
    $('.J_detail-item').on('click',function() {
        if ($(this).hasClass('detail-item-closed') == true) {
            $(this).removeClass('detail-item-closed');
            $(this).find(".detail-item-box").removeClass('detail-item-box-hide');
        } else {
            $(this).addClass('detail-item-closed');
            $(this).find(".detail-item-box").addClass('detail-item-box-hide');
        }
    });
    
    // 琛岀▼淇℃伅灞曞紑闅愯棌controller-route
    $('.controller-route').on('click',function() {
    	var _this = $('.detail-item-route');
        if (_this.hasClass('detail-item-closed') == true) {
        	_this.removeClass('detail-item-closed');
        	_this.find(".detail-item-box").removeClass('detail-item-box-hide');
        	$('span.contact-btn2').html("琛岀▼姒傝");
        } else {
        	_this.addClass('detail-item-closed');
        	_this.find(".detail-item-box").addClass('detail-item-box-hide');
        	$('span.contact-btn2').html("琛岀▼璇︽儏");
        }
    });
    
    // 鏄剧ず鏄庣粏
    $(".price_detail").click(function(){
    	$(document).Prompts({className:".content-2",typeNum:2});
	})

    //婊氬姩浜嬩欢
    $(document).on("scroll",function(){
    	switchToolDisplay();
    });
    
    //鏄剧ず琛岀▼ 璇︾粏鐨勫垏鎹㈡寜閽�
	function switchToolDisplay(){
		var isShow = $("#calendar_div").is(':hidden');
		var scrollTop = $(document).scrollTop();
		var $nDetail = $('.detail-item-route');
		if($nDetail.length==0){
			return;
		}
		var offset = $nDetail.offset();
		var topMin = offset.top-$(window).height()+50;
		var topMax = offset.top+$nDetail.outerHeight();
		if(scrollTop>=topMin&&scrollTop<=topMax &&isShow){
			$('.controller-route').show();
		}else{
			$('.controller-route').hide();
		}
	}
    
    // 缃戠珯缁熻
    var system = 0;
    var agencyId = $("#agencyId").val();
    var username = $("#username").val();
    var userphone = $("#userphone").val();
    var productNo = getQueryString("no");
    var productName = $("#statistics-productName").html();
    var params = {
        system: system,
        productNo: productNo,
        agencyId: agencyId,
        productName: productName,
        userName: username,
        phone: userphone,
        "detail['memberName']": $("#statistics-memberName").val(),
        accessAddress: window.location.href
    };
//  Statistics.commonStatis(params);
    
    // 鏍￠獙浼樻儬鐮�
    $(".js_use").click(function(){
    	var code = $(".v_input_text").val();
    	if(code==''){
    		showAlert("璇疯緭鍏ヤ紭鎯犵爜");
    		return false;
    	}else{
    		loadingPage();
    		var price = $("#adult_price").html();
    		jQuery.post("promoCode.htm", {
    			code: code,
    			price: price
    	    },
    	    function(data) {
    	    	removeLoadingPage();
    	        if (data.code == '0') {
    	        	$("#discount").val(data.result.discount);
    	        	if(data.result.discount==0.1){
    	        		// 鍏嶈垂
    	        		$(".a_box .list_num_dec").addClass("num_invalid");
    	        		$(".a_box .list_num_inc").addClass("num_invalid");
    	        		$("#adult_number").attr("max",1);
    	        		$("#adult_number").attr("min",1);
    	        		var count = parseInt($("#adult_number").html());
    	        		if(count>1){
    	        			showAlert("333333333333333");
    	        		}
    	        		while(count>1){
    	        			$(".a_box .list_num_dec").click();
    	        			count--;
    	        		}
    	        	}
    	        	$(".js_use").hide();
    	        	$(".v_s").show();
    	        	$(".v_coupons_top_mod").css("background","#EEE")
    	        	$(".v_input_text").attr("disabled",true).css("color","#666");
    	        	calculate();
    	        } else {
    	            showAlert("11111111111111111111111111111111111111111");
    	            return;
    	        }
    	    },"json");
    	}
    	
    })
});
//灞曠ず鏃ュ巻
function showDate() {
//	$(".J_detail-bg").show();
	$('.J_top-layer').hide();
    $(".td_date").css("background", "#ffffff");
    $(".controller-mainpage").hide();
    $("#calendar_div").show();
    $(".controller-booking").hide();
    $(".mod_foot_acitons").show();
    $(".icon-share").hide();
}
function closeDate() {
//	 $(".J_detail-bg").hide();
     $('.J_top-layer').show();
    $("#calendar_div").hide();
    $(".controller-mainpage").show();
    $(".controller-booking").show();
    $(".mod_foot_acitons").hide();
    $(".icon-share").show();
}
// 鎻愪氦琛ㄥ崟
$(".btn_link").click(function() {
	var discount = $("#discount").val();
    var no = $("input[name='no']").val();
    var date = $("input[name='date']").val();
    var promoCode = $(".v_input_text").val();
    var totalPrice = $("input[name='totalPrice']").val();
    var roomNumber = $("input[name='roomNumber']").val();
    var childNumber = $("input[name='childNumber']").val();
    var adultNumber = $("input[name='adultNumber']").val();
    var inventoryId = $("input[name='inventoryId']").val();
    var roomSubjoinPrice = $("input[name='roomSubjoinPrice']").val();
    if (inventoryId == '') {
        showAlert("5555555555555555555");
        return;
    }
    jQuery.post("reserveSession.htm", {
        no: no,
        date: date,
        discount:discount,
        promoCode:promoCode,
        roomNumber: roomNumber,
        childNumber: childNumber,
        adultNumber: adultNumber,
        inventoryId: inventoryId,
        totalPrice: totalPrice,
        roomSubjoinPrice: roomSubjoinPrice
    },
    function(data) {
        if (data == 'S') {
            window.location.href = "reserve.htm?no="+no;
        } else {
            showAlert("8888888888888888888888888888");
            return;
        }
    });
});
//鑾峰彇鍙傛暟
function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]);
    return null;
}