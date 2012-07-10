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

<html>
<head>
<script type="text/javascript"
 src="<c:url value='/resources/jquery/jquery-1.7.1.min.js'/>"></script>

<script type="text/javascript">
$(document).ready(
function() {
    var loadMore = $('.load-more');

    loadMore.bind('click', function() {
        var currentControl = $(this),
            text = currentControl.find('.text'),
            indicator = currentControl.find('.loading-indicator');

        // don't let the user to interact
        // with button while loading
        if ( currentControl.hasClass('disabled') ) return;

        currentControl.addClass('disabled');
        text.css('visibility', 'hidden');
        indicator.css('display', 'block');

        $.getJSON('/echo/jsonp/', function() {
            // get /echo/jsonp is so fast
            // that we need to setTimeout in order
            // to see the animation at all
            setTimeout( function() {
                currentControl.removeClass('disabled');
                text.css('visibility', 'visible');
                indicator.css('display', 'none');
            }, 2000 );
        });
    });

});
</script>

<style>
body {
    font-size:12px;
    font-family:Tahoma, Geneva,  Lucida Grande, sans-serif;
}
.load-more {
    background:#def4fb;
    display:inline-block;
    border:1px solid #b2c2ca;
    -webkit-border-radius:3px;
    -moz-border-radius:3px;
    border-radius:3px;
    text-decoration:none;
    color:#478fb3;
    padding:.8em 1em;
    position:relative;
    cursor:pointer;
}
.load-more.disabled {
    background:#effafe;
}
    .load-more .loading-indicator {
        display:none;
        position:absolute;
        width:26px;
        height:10px;
        left:50%;
        margin-left:-13px;
        top:50%;
        margin-top:-5px;
    }
        .load-more .loading-indicator span {
            float:left;
            width:6px;
            height:10px;
            margin-left:4px;
            background:#4790b3;
            -webkit-animation: fade 1s linear infinite;
        }
        .load-more .loading-indicator span:first-child {
            margin:0;
             -webkit-animation-delay: .333s;
        }
        .load-more .loading-indicator span:nth-child(2) {
            -webkit-animation-delay: .666s;
        }
        .load-more .loading-indicator span:nth-child(3) {
            -webkit-animation-delay: .999s;
        }
@-webkit-keyframes fade {
  0% { opacity: 1; -webkit-transform:scaleY(1.5); }
  60% { opacity: .25; -webkit-transform:scaleY(1); }
  100% { opacity: 0; -webkit-transform:scaleY(1); }
}
</style>
</head>

<boday>
<!-- inspired by http://37signals.com/svn/posts/2577-loading-spinner-animation-using-css-and-webkit -->
<div class="load-more">
    <span class="text">Load more</span>
    <div class="loading-indicator">
        <span></span>
        <span></span>
        <span></span>
    </div>
</div>
</boday>
​​</html>