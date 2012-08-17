<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ page session="false"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<%@page import="org.ssh.pm.hcost.web.PageUtils"%>
<%@page import="org.springframework.web.util.WebUtils"%>
<%@page import="org.ssh.pm.hcost.web.UserSession"%>
<%@page import="org.ssh.sys.entity.User"%>
<%@page import="org.ssh.pm.hcost.service.CommonService" %>

<%
  ;
  //重复
  pageContext.setAttribute("user_name", (String) PageUtils.getApplicationInfos().get("user_name"));
  UserSession userSession = (UserSession) WebUtils.getSessionAttribute(request, "userSession");
  //UserSession userSession=CommonService.getUserSession();
  User u = null;
  if (userSession != null) {
    u = userSession.getAccount();
  }
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN"
   "http://www.w3.org/TR/html4/strict.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />

<title>${application_name} - <tiles:insertAttribute
    name="title" />
</title>

<!-- Framework CSS -->
<link rel="stylesheet"
  href="<c:url value="/resources/css/blueprint/screen.css" />"
  type="text/css" media="screen, projection">
<link rel="stylesheet"
  href="<c:url value="/resources/css/blueprint/print.css" />"
  type="text/css" media="print">
<!--[if lt IE 8]><link rel="stylesheet" href="<c:url value="/resources/css/blueprint/ie.css" />"  type="text/css" media="screen, projection"><![endif]-->

<link rel="stylesheet"
  href="<c:url value="/resources/css/blueprint/plugins/fancy-type/screen.css" />"
  type="text/css" media="screen, projection">

<tiles:useAttribute id="styles" name="styles" classname="java.util.List"
  ignore="true" />
<c:forEach var="style" items="${styles}">
  <link rel="stylesheet"
    href="<c:url value="/resources/css/${style}?ver=${buildId}" />" type="text/css"
    media="all" />
</c:forEach>
<tiles:useAttribute id="scripts" name="scripts"
  classname="java.util.List" ignore="true" />
<c:forEach var="script" items="${scripts}">
  <script type="text/javascript"
    src="<c:url value="/resources/${script}?ver=${buildId}" />"></script>
</c:forEach>
<link rel="shortcut icon"
  href="<c:url value="/resources/img/icon/favicon.ico"/>" />
<link rel="icon" href="<c:url value="/resources/img/icon/favicon.ico"/>" />
</head>
<body>

  <div id="page" class="container">

    <p></p>
    <div id="logo" class="span-3 prefix-1" align="right">
      <img src="<c:url value="/resources/img/app-logo.png" />"
        alt="logo png" />
    </div>
    <div id="app_info" class="span-15">
      <h1>${application_name}</h1>
    </div>

    <div id="loginInfo" class="span-6 last" align="right">

      <%
        if (userSession != null) {
      %>

      欢迎你,<%
        out.println(userSession.getAccount().getName());
      %>&nbsp;&nbsp;|&nbsp;&nbsp;
        <%
        if (userSession.getAccount().getLoginName().equalsIgnoreCase("admin")){
        %>
        <a href="<c:url value="/system/manage" />">管理</a>&nbsp;&nbsp;
        <%
        }
        %>
        <a href="<c:url value="/system/logout" />">注销</a>&nbsp;&nbsp;
      <%
        } else {
      %>
      尚未登录
      <%
        }
      %>

    </div>

    <hr>

    <div id="content" class="span-24">
      <tiles:insertAttribute name="content" />
    </div>
    <hr />
    <div id="footer">
      <div class="span-6">
        <span class="quiet">&nbsp;版本.${version}-${buildId} </span>
      </div>
      <div class="span-18 last" align="right">
        <span class="quiet">${copyright}&nbsp;&nbsp;</span>
      </div>
    </div>
</body>
</html>