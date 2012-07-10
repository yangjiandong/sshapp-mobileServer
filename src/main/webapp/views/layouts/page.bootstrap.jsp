<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ page session="false"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<%@page import="org.ssh.pm.hcost.web.PageUtils"%>
<%@page import="org.springframework.web.util.WebUtils"%>
<%@page import="org.ssh.pm.hcost.web.UserSession"%>
<%@page import="org.ssh.sys.entity.User"%>
<%@page import="org.ssh.pm.hcost.service.CommonService"%>

<%
  ;
  //重复
  pageContext.setAttribute("user_name", (String) PageUtils.getApplicationInfos().get("user_name"));
  UserSession userSession = (UserSession) WebUtils.getSessionAttribute(request, "userSession");
  User u = null;
  if (userSession != null) {
    u = userSession.getAccount();
  }
%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="eking soft,his manage system">
<meta name="author" content="eking">

<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />

<title>${application_name} - <tiles:insertAttribute
  name="title" />
</title>

<link rel="stylesheet"
 href="<c:url value='/resources/bootstrap/css/bootstrap.css'/>" type="text/css" />
<link rel="stylesheet"
  href="<c:url value='/resources/bootstrap/css/manage.css'/>"
  type="text/css" />
<link rel="stylesheet"
 href="<c:url value='/resources/bootstrap/css/bootstrap-responsive.css'/>"
 type="text/css" />

<!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
      <script src="<c:url value='/resources/bootstrap/js/html5.js'/>"></script>
    <![endif]-->

<tiles:useAttribute id="styles" name="styles" classname="java.util.List"
 ignore="true" />
<c:forEach var="style" items="${styles}">
 <link rel="stylesheet"
  href="<c:url value="/resources/${style}?ver=${buildId}" />" type="text/css"
  media="all" />
</c:forEach>

<script type="text/javascript"
 src="<c:url value='/resources/jquery/jquery-1.7.1.min.js'/>"></script>

<tiles:useAttribute id="scripts" name="scripts" classname="java.util.List"
 ignore="true" />
<c:forEach var="script" items="${scripts}">
 <script type="text/javascript"
  src="<c:url value="/resources/${script}?ver=${buildId}" />"></script>
</c:forEach>
<link rel="shortcut icon"
 href="<c:url value="/resources/img/icon/favicon.ico"/>" />
<link rel="icon" href="<c:url value="/resources/img/icon/favicon.ico"/>" />
</head>
<body>

 <div class="navbar navbar-fixed-top">
  <div class="navbar-inner">
   <div class="container-fluid">
    <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
     <span class="icon-bar"></span> <span class="icon-bar"></span> <span
     class="icon-bar"></span>
    </a> <a class="brand" href="#">${application_name}</a>

    <div class="nav-collapse">
     <%
       if (userSession != null) {
     %>
     <ul class="nav">
      <%
        if (userSession.getAccount().getLoginName().equalsIgnoreCase("admin")) {
      %>
      <li class=""><a href="<c:url value="/system/manage" />">管理</a></li>
      <%
        }
      %>
      <li class=""><a href="<c:url value="/system/logout" />">注销</a></li>
     </ul>
     <p class="navbar-text pull-right">
      欢迎你,
      <%
       out.println(userSession.getAccount().getName());
     %>
     </p>

     <%
       } else {
     %>
     <p class="navbar-text pull-right">尚未登录</p>
     <%
       }
     %>
    </div>
    <!--/.nav-collapse -->
   </div>
  </div>
 </div>

 <div class="container">
  <tiles:insertAttribute name="content" />

  <hr class="soften">

  <footer>
   <span class="label label-info">&nbsp;版本.${version}-${buildId} </span>
   <p class="pull-right">
   <span class="label">${copyright}&nbsp;&nbsp;</span>
   </p>
  </footer>
 </div>

 <script src="<c:url value='/resources/bootstrap/js/bootstrap.min.js'/>"></script>

</body>
</html>