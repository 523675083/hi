<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
	String BASE_PATH = "/";
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	request.setAttribute("CONTEXT", basePath);
%>
<%-- <%@ page isELIgnored="false" %> --%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<meta name="format-detection" content="telephone=no" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<title>itfvck测试首页</title>
 <script type="text/javascript" src="../js/jquery.js"></script>
	    <script type="text/javascript" src="../js/common.js"></script>
	    <script type="text/javascript" src="../js/jquery.cookie.js"></script>
</head>
<body>
	 <P> 授权成功，openid=${openid }</P><br>
	 <a href="javascript:showLocation()">查看当前位置</a><br>
	 <a href="javascript:chooseImg()">选择图片</a><br>
	  <a href="javascript:showImg()">预览</a><br>
	  <a href="javascript:uploadImg()">上传</a><br>
	 <img alt="" src="" id="img1">
  
</body>
 <script src="../js/wechat/wx-js.js"></script>
</html>
