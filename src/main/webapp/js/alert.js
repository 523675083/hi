$("<div class='mop' style='display:none;'></div>").appendTo($("body"));//增加灰背景
(function($){
	FastClick.attach(document.body);
	$.fn.Prompts=function(opts){
		this.options=$.extend({
			className:'', //调用的class或Id
			typeNum:'1', //提示弹层的类型
			content:"" //提示的内容
		},opts)
		var _this=$(opts.className);
		//弹层定位
		var height=document.documentElement.clientHeight-parseInt(_this.height());
		var width=document.documentElement.clientWidth-parseInt(_this.width());
		_this.css({"top":height/2-10,"left":width/2});
		
		$mop=$(".mop");
		var str="";
		var prompt=function(){
			if(opts.typeNum==1){
				_this.show();
				$mop.show();
				_this.html(opts.content); 
				var hideEvent=function(){
					_this.fadeOut(1000);
					$mop.fadeOut(1000);
				}
				setTimeout(hideEvent,3000);
			}else if(opts.typeNum==2){
				_this.removeClass("hide");
				$mop.show();
				var closeEvent=function(){
					$(this).parents("div").addClass("hide");
					$mop.fadeOut();
				}
				$(".close").on("click",closeEvent);
			}else if(opts.typeNum==3){
				$mop.show();
				_this.removeClass("hide");
			}else if(opts.typeNum==4){
				$mop.addClass("demo").html("<div class='corner' id='corner'></div>");
				_this.removeClass("hide");
				$mop.show();
			}else{
				return false;
			}
		}
		prompt();
	}
})(jQuery);

function showAlert(str, timeout, callback){
    var tips = $('<div class="m-layer"><div class="m-tips slideDown">'+str+'</div></div>'),
            noop = function(){},
            fn = callback || noop;

    // 添加提示
    $('body').append(tips);
    
    tips.on('click', function(){
    	tips.remove();
    });

    // 删除提示
    setTimeout(function(){
        tips.remove();
        fn();
    }, timeout || 1500);
}
