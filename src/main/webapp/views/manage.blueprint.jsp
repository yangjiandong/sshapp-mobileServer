<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<link rel="stylesheet"
  href="<c:url value='/resources/ext/resources/css/ext-all.css'/>"
  type="text/css" />

<link rel="stylesheet"
  href="<c:url value='/resources/css/manage.css'/>"
  type="text/css" />

    <style type="text/css">
    .x-panel-body p {
        margin:10px;
        font-size:12px;
    }
    .container {
        padding:10px;
    }

#grid-example .x-grid-col-1 {
  text-align: right;
}
#grid-example .x-grid-col-2{
  text-align: right;
}
#grid-example .x-grid-col-3 {
  text-align: right;
}
#grid-example .x-grid-col-4 {
  text-align: right;
}
#grid-example.x-grid-mso{
  border: 1px solid #6593cf;
}
#grid-example.x-grid-vista{
  border: 1px solid #b3bcc0;
}
#xml-grid-example{
  border: 1px solid #cbc7b8;
  left: 0;
  position: relative;
  top: 0;
}
    </style>

<script type="text/javascript"
  src="<c:url value='/resources/ext/ext-base.js?ver=${buildId}'/>"></script>
<script type="text/javascript"
  src="<c:url value='/resources/ext/ext-all.js?ver=${buildId}'/>"></script>
<script type="text/javascript"
  src="<c:url value='/resources/ext/ext-basex.js?ver=${buildId}'/>"></script>
<script type="text/javascript"
  src="<c:url value='/resources/ext/ext-lang-zh_CN.js?ver=${buildId}'/>"></script>

<script type="text/javascript"
  src="<c:url value='/resources/js/app/sys/manage.js?ver=${buildId}'/>"></script>

<script type="text/javascript"
  src="<c:url value='/resources/jquery/jquery-1.4.min.js'/>"></script>

<script>
  $(document).ready(
  function() {
	  $.getJSON('../migration/get_migrations',function(alldata) {
		  if (alldata.success) {
			  if(alldata.count !=0 ){
				  $("#migration-btn").removeAttr("disabled");
			  }else{
				  $("#migration-btn").attr({"disabled":"disabled"});
			  }
		  }
	  })
      
  })
</script>
          
<body>

<h1>系统管理工具</h1>
<p>主要是提供给系统管理员的一些管理功能，如初始数据、查看后台服务器运行情况等。</p>
<p>
<b>返回首页，请点击 <a href="../common/index">这里</a>。</b><br />
</p>

<p></p>

<h2>初始化菜单</h2>
<p>

    初始系统菜单，在调整了系统菜单、增加功能后，第一次使用时需执行一下。
    <input type="button" id="init-btn" value="开始" />

</p>

<h2>初始化院级核算</h2>
<p>

    初始院级核算系统，在调整了菜单、增加功能后，第一次使用时需执行一下。
    <input type="button" id="init3-btn" value="开始" />
</p>

<h2>后台升级脚本</h2>
<p>

    升级本次更新脚本。
    <input type="button" id="migration-btn" value="开始" />
</p>

<h2>查看后台数据库连接情况</h2>
<p>

    查看系统连接数据库情况。
    <input type="button" id="db-btn" value="刷新" />
</p>

<div id="database-info">
</div>

<h2>查看在线用户情况</h2>
<p>
    查看在线用户情况。
    <input type="button" id="u-btn" value="刷新" />
</p>
<div id="u-info">
</div>

<div id="hello-win" class="x-hidden">
    <div class="x-window-header">系统初始化</div>
</div>

<div id="hello3-win" class="x-hidden">
    <div class="x-window-header">院级核算系统初始化</div>
</div>

<div id="hello4-win" class="x-hidden">
    <div class="x-window-header">后台升级脚本</div>
</div>

</body>


