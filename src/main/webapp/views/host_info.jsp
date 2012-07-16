<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title></title>

<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">

<link rel="stylesheet"
  href="<c:url value='/resources/bootstrap/css/bootstrap.css'/>"
  type="text/css" />

<link rel="stylesheet"
  href="<c:url value='/resources/bootstrap/css/manage.css'/>"
  type="text/css" />

<link rel="stylesheet"
  href="<c:url value='/resources/bootstrap/css/bootstrap-responsive.css'/>"
  type="text/css" />

<!--[if IE 6]>
    <link href="<c:url value='/resources/bootstrap/css/ie6.min.css'/>" rel="stylesheet">
<![endif]-->

<!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
      <script src="<c:url value='/resources/bootstrap/js/html5.js'/>"></script>
    <![endif]-->

<script type="text/javascript"
  src="<c:url value='/resources/jquery/jquery-1.7.1.min.js'/>"></script>

<script>
</script>

<script type="text/javascript">
$(function(){if($.browser.msie&&parseInt($.browser.version,10)===6){$('.row div[class^="span"]:last-child').addClass("last-child");$('[class*="span"]').addClass("margin-left-20");$(':button[class="btn"], :reset[class="btn"], :submit[class="btn"], input[type="button"]').addClass("button-reset");$(":checkbox").addClass("input-checkbox");$('[class^="icon-"], [class*=" icon-"]').addClass("icon-sprite");$(".pagination li:first-child a").addClass("pagination-first-child")}

    // tooltip demo
    $('.tooltip-demo.well').tooltip({
      selector: "a[rel=tooltip]",
      placement: "top"
    })

    $('.tooltip-test').tooltip()
    $('.popover-test').popover()

    // popover demo
    $("a[rel=popover]")
      .popover({
        placement : "top"
       })
      .click(function(e) {
        e.preventDefault()
      })
});
</script>
</head>

<body>
鑫亿移动平台
</body>
</html>