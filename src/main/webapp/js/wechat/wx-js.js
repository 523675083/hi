var isWxInit = false;//判断微信是否初始化完成
var shareIntervalIndex;//
var curShareVo = null;//当前的分享内容
var defaultShareVo = null;//默认分享内容
var latitude ; // 纬度，浮点数，范围为90 ~ -90
var longitude; // 经度，浮点数，范围为180 ~ -180。
var speed; // 速度，以米/每秒计
var accuracy; // 位置精度
var localIds;
var wx_js_sdk = {
    loadsharedesc: "我自己的掌上门店,欢迎光临！",
    jsApiList: ["onMenuShareTimeline", "onMenuShareAppMessage", "onMenuShareQQ", "chooseImage", "previewImage", "uploadImage", "downloadImage","openLocation"]
};
var wxReady = function () {
    var d = wx_js_sdk.configPara;
    var desc = d.desc;
    var link = d.url;
    var shareName = d.name;
    var title = d.shopName;
    var imgUrl = d.imgUrl;
    if (imgUrl == "") {
        imgUrl = "http://" + window.location.host + "/img/ic_launcher.png";
    }
    if ($.trim(desc) == "") {
        desc = "我自己的掌上门店,欢迎光临！";
    }
    wx_js_sdk.loadsharedesc == "我自己的掌上门店,欢迎光临！";
    wx_js_sdk.onMenuShareTimeline(shareName, link, imgUrl);
    wx_js_sdk.onMenuShareAppMessage(title, desc, link, imgUrl);
    wx_js_sdk.onMenuShareQQ(title, desc, link, imgUrl);
    wx.showOptionMenu();
    defaultShareVo = {
        title: title,
        desc: desc,
        link: link,
        imgUrl: imgUrl
    };
    isWxInit = true;
};

/**
 *通过config接口注入权限验证配置
 **/
wx_js_sdk.config = function (d) {
    wx.config({
        debug: false,
        appId: d.appId,
        timestamp: d.timestamp,
        nonceStr: d.nonceStr,
        signature: d.signature,
        jsApiList: wx_js_sdk.jsApiList
    });
    wx_js_sdk.configPara = d;
    wx.ready(wxReady);
};
wx_js_sdk.error = function (d, count) {
    wx.error(function (res) {
        if (count == 3) {//3次请求后依旧失败，隐藏掉
            wx.hideOptionMenu();
            return;
        }
        setTimeout(function () {//失败后再次请求
            wx_js_sdk.config(d);
            wx_js_sdk.error(d, count + 1);
        }, 300);
    });
};
//分享统计
function share() {
    var params = {
        type: 3
    };
    if ($(".wxshare-productDetail").length > 0) {
        //详情页分享统计
        params.previousPageUrl = document.referrer, params.accessAddress = window.location.href, params.productNo = $("#productNo").val();
        params.subtype = 0;
        //产品分享
    } else {
        //其他页分享统计
        var d = wx_js_sdk.configPara;
        params.previousPageUrl = window.location.href;
        params.accessAddress = d.url;
        params.subtype = 1;
        //店铺分享
    }
    Statistics.commonStatis(params);
}
//分享内容
function shareDetail(fun) {
    shareIntervalIndex = setInterval(function () {
        if (isWxInit) {
            wx_js_sdk.onMenuShareTimeline(curShareVo.title, curShareVo.link, curShareVo.imgUrl);
            wx_js_sdk.onMenuShareAppMessage(curShareVo.title, curShareVo.desc, curShareVo.link, curShareVo.imgUrl);
            wx_js_sdk.onMenuShareQQ(curShareVo.title, curShareVo.desc, curShareVo.link, curShareVo.imgUrl);
            if (fun != null) {
                fun();
            }
            window.clearInterval(shareIntervalIndex);
        }
    }, 1);
}
//获得当前的分享内容
function getCurShareVo() {
    return curShareVo;
}
//设置当前的分享内容
function setCurShareVo(shareVo) {
    curShareVo = shareVo;
}
//加载默认分享内容
function loadDefaultShareVo() {
    if (defaultShareVo != null) {
        curShareVo = defaultShareVo;
        shareDetail();
    }
}
/**
 *分享到朋友圈
 **/
wx_js_sdk.onMenuShareTimeline = function (title, link, imgUrl) {
    wx.onMenuShareTimeline({
        title: title, // 分享标题
        link: link, // 分享链接
        imgUrl: imgUrl, // 分享图标
        success: function () {
            share();
            // 用户确认分享后执行的回调函数
        },
        cancel: function () {
            // 用户取消分享后执行的回调函数
        }
    });
};

/**
 *分享给朋友
 **/
wx_js_sdk.onMenuShareAppMessage = function (title, desc, link, imgUrl) {
    wx.onMenuShareAppMessage({
        title: title, // 分享标题
        desc: desc, // 分享描述
        link: link, // 分享链接
        imgUrl: imgUrl, // 分享图标
        type: '', // 分享类型,music、video或link，不填默认为link
        dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
        success: function () {
            share();
            // 用户确认分享后执行的回调函数
        },
        cancel: function () {
            // 用户取消分享后执行的回调函数
        }
    });
};
/**
 *分享给QQ
 **/
wx_js_sdk.onMenuShareQQ = function (title, desc, link, imgUrl) {
    wx.onMenuShareQQ({
        title: title, // 分享标题
        desc: desc, // 分享描述
        link: link, // 分享链接
        imgUrl: imgUrl, // 分享图标
        success: function () {
            share();
            // 用户确认分享后执行的回调函数
        },
        cancel: function () {
            // 用户取消分享后执行的回调函数
        }
    });
};

/**
 *选择图片
 **/
wx_js_sdk.chooseImage = function (callback) {
    if (!wx_js_sdk.checkJsApi()) {
        alert("不支持，请下载最新版微信");
        return false;
    }
    wx.chooseImage({
        success: function (res) {
             localIds = res.localIds;
            // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
            callback(localIds);
        }
    });
};
/**
 *上传图片
 **/
wx_js_sdk.uploadImage = function (localId, callback) {
	alert("进入上传图片方法");
    wx.uploadImage({
        localId: localId, // 需要上传的图片的本地ID，由chooseImage接口获得
        isShowProgressTips: 1, // 默认为1，显示进度提示
        success: function (res) {
            var serverId = res.serverId;
            alert(serverId);
            // 返回图片的服务器端ID
            callback(serverId);
        }
    });
};
/**
 * 获取地理位置
 */
wx_js_sdk.openLocation=function(){
	wx.getLocation({
	    type: 'wgs84', // 默认为wgs84的gps坐标，如果要返回直接给openLocation用的火星坐标，可传入'gcj02'
	    success: function (res) {
	        var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
	        var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
	        var speed = res.speed; // 速度，以米/每秒计
	        var accuracy = res.accuracy; // 位置精度
	        wx.openLocation({
	    	    latitude: latitude, // 纬度，浮点数，范围为90 ~ -90
	    	    longitude: longitude, // 经度，浮点数，范围为180 ~ -180。
	    	    name: '盈科旅游', // 位置名
	    	    address: '玩转月球', // 地址详情说明
	    	    scale: 28, // 地图缩放级别,整形值,范围从1~28。默认为最大
	    	    infoUrl: 'www.baidu.com' // 在查看位置界面底部显示的超链接,可点击跳转
	    	});
	    }
	});
	
};

wx_js_sdk.checkJsApi = function () {
    var regStr_micromessenger = /micromessenger\/[\d.]+[_| ]/gi;
    var agent = navigator.userAgent.toLowerCase();
    if (agent.indexOf("micromessenger") != -1) {
        var version = (agent.match(regStr_micromessenger) + "").replace(/[^0-9]/ig, "") + "000";
        version = version.substring(0, 3);
        if (parseInt(version) >= 602) {
            return true;
        }
    }
    return false;
};
wx_js_sdk.init = function () {
    $.ajax({
        url: "http://res.wx.qq.com/open/js/jweixin-1.2.0.js",
        dataType: "script",
        cache: false
    }).done(function () {
            wx.hideOptionMenu();
            $.post("../h5Pay/getWxConfigInfo.do", {
                url: location.href.split('#')[0]
            }, function (d) {
                if (!d) {
                    return;
                }
                wx_js_sdk.config(d);
                wx_js_sdk.error(d, 1);
                $(window).unload(function () {
                    wx.hideOptionMenu();
                });
            }, "json");
        });

};
function showLocation(){
	 wx_js_sdk.openLocation();
}
function chooseImg(){
	wx_js_sdk.chooseImage();
}
function showImg(){
	wx.previewImage({
	    current: '', // 当前显示图片的http链接
	    urls: localIds // 需要预览的图片http链接列表
	});
}
function uploadImg(){
	alert("aaaa");
	wx_js_sdk.uploadImage(localIds,alertImg);
}
function alertImg(img){
	alert(img);
}
$(function () {
    wx_js_sdk.init();
});

