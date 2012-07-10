<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="description" content="eking hcost">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache, must-revalidate">
<meta http-equiv="expires" content="0">
<!--ie9-->
<!--
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
 -->

<link rel="shortcut icon"
  href="<c:url value="/resources/img/icon/favicon.ico"/>" />
<link rel="icon" href="<c:url value="/resources/img/icon/favicon.ico"/>" />

<link rel="stylesheet"
  href="<c:url value='/resources/ext/resources/css/ext-all.css'/>"
  type="text/css" />
<link rel="stylesheet"
  href="<c:url value='/resources/css/default.css'/>" type="text/css" />
<link rel="stylesheet"
  href="<c:url value='/resources/css/app.css?ver=${buildId}'/>"
  type="text/css" />
<link rel="stylesheet"
  href="<c:url value='/resources/css/ext-patch.css?ver=${buildId}'/>"
  type="text/css" />
<link rel="stylesheet"
  href="<c:url value='/resources/css/ext_icon.css?ver=${buildId}'/>"
  type="text/css" />

<script type="text/javascript"
  src="<c:url value='/resources/ext/ext-base.js?ver=${buildId}'/>"></script>
<script type="text/javascript"
  src="<c:url value='/resources/ext/ext-all.js?ver=${buildId}'/>"></script>
<script type="text/javascript"
  src="<c:url value='/resources/ext/ext-basex.js?ver=${buildId}'/>"></script>
<script type="text/javascript"
  src="<c:url value='/resources/ext/ext-lang-zh_CN.js?ver=${buildId}'/>"></script>
<script type="text/javascript"
  src="<c:url value='/resources/js/lib/utils.js?ver=${buildId}'/>"></script>
<script type="text/javascript"
  src="<c:url value='/resources/js/lib/common.lib.js?ver=${buildId}'/>"></script>
<script type="text/javascript"
  src="<c:url value='/resources/js/lib/tdgi.borderLayout.js?ver=${buildId}'/>"></script>

<script type="text/javascript"
  src="<c:url value='/resources/js/app/sys/change_pwd_win.js?ver=${buildId}'/>"></script>
<script type="text/javascript"
  src="<c:url value='/resources/js/about.js?ver=${buildId}'/>"></script>
<script type="text/javascript"
  src="<c:url value='/resources/js/main.js?ver=${buildId}'/>"></script>
<script type="text/javascript"
  src="<c:url value='/resources/js/main.panel.js?ver=${buildId}'/>"></script>

<script type="text/javascript"
  src="<c:url value='/resources/js/include.maps.js?ver=${buildId}'/>"></script>
<script type="text/javascript"
  src="<c:url value='/resources/js/include2.maps.js?ver=${buildId}'/>"></script>
<script type="text/javascript"
  src="<c:url value='/resources/js/include3.maps.js?ver=${buildId}'/>"></script>
<script type="text/javascript"
  src="<c:url value='/resources/js/include4.maps.js?ver=${buildId}'/>"></script>

<script type="text/javascript">
  //Ext.BLANK_IMAGE_URL = 'resources/img/s.gif';

  // Deployment type: Production(PROD) or development(DEV). In development mod does not cache
  CFG_DEPLOYMENT_TYPE = '${run_mode}';
  //javascript
  CFG_PATH_EXTJS = '${ext}';
  CFG_PATH_JSLIB = 'resources/js/lib';
  CFG_PATH_ICONS = 'resources/img';
  CFG_BUILD_ID = '${buildId}';
</script>
<script type="text/javascript"
  src="<c:url value='/resources/js/selectdatabase.js?ver=${buildId}'/>"></script>

<title>${application_name}</title>
</head>
<body>
  <div id="menubar"></div>
  <div id="west"></div>
  <div id="south"></div>
</body>
</html>



