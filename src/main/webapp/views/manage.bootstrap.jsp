<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>${application_name}${version} - 管理</title>

<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">

<link rel="stylesheet"
  href="<c:url value='/resources/ext/resources/css/ext-all.css'/>"
  type="text/css" />

<link rel="stylesheet"
  href="<c:url value='/resources/bootstrap/css/bootstrap.css'/>"
  type="text/css" />

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

<style type="text/css">
.x-panel-body p {
  margin: 10px;
  font-size: 12px;
}

#grid-example .x-grid-col-1 {
  text-align: right;
}

#grid-example .x-grid-col-2 {
  text-align: right;
}

#grid-example .x-grid-col-3 {
  text-align: right;
}

#grid-example .x-grid-col-4 {
  text-align: right;
}

#grid-example.x-grid-mso {
  border: 1px solid #6593cf;
}

#grid-example.x-grid-vista {
  border: 1px solid #b3bcc0;
}

#xml-grid-example {
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
  src="<c:url value='/resources/jquery/jquery-1.7.1.min.js'/>"></script>

<script>
  $(document).ready(function() {
    $.getJSON('../migration/get_migrations', function(alldata) {
      if (alldata.success) {
        if (alldata.count != 0) {
          $("#migration-btn").removeAttr("disabled");
        } else {
          $("#migration-btn").attr({
            "disabled" : "disabled"
          });
        }
      }
    })


    // 是否有奖金
    $.getJSON('../system/has_module?moduleId=8', function(alldata) {
      if (alldata.success) {
        if (alldata.count != 0) {
          //$("#migration-btn").removeAttr("disabled");
        } else {
          $('#bonus').remove();
          });
        }
      }
    })
  })
</script>
</head>

<body>

  <div class="navbar navbar-fixed-top">
    <div class="navbar-inner">
      <div class="container">
        <a class="btn btn-navbar" data-toggle="collapse"
          data-target=".nav-collapse"> <span class="icon-bar"></span> <span
          class="icon-bar"></span> <span class="icon-bar"></span>
        </a> <a class="brand" href="#">系统管理工具</a>
        <div class="nav-collapse">
          <ul class="nav">
            <li class="active"><a href="../common/index">返回首页</a></li>
          </ul>
        </div>
        <!--/.nav-collapse -->
      </div>
    </div>
  </div>

  <div class="container">
    <div class="marketing">
      <img class="bs-icon"
        src="<c:url value='/resources/bootstrap/img/glyphicons/glyphicons_079_podium.png' />" />
      <h2>初始化菜单</h2>
      <p>
        初始系统菜单，在调整了系统菜单、增加功能后，第一次使用时需执行一下。 <input type="button"
          id="init-btn" class="btn btn-primary" value="开始" />
      </p>

      <img class="bs-icon"
        src="<c:url value='/resources/bootstrap/img/glyphicons/glyphicons_079_podium.png' />" />
      <h2>初始化院级核算</h2>
      <p>
        初始院级核算系统，在调整了菜单、增加功能后，第一次使用时需执行一下。 <input type="button"
          id="init3-btn" class="btn btn-primary" value="开始" />
      </p>

      <div id="bonus">
      <img class="bs-icon"
        src="<c:url value='/resources/bootstrap/img/glyphicons/glyphicons_079_podium.png' />" />
      <h2>初始化奖金核算</h2>
      <p>
        初始奖金系统，在调整了菜单、增加功能后，第一次使用时需执行一下。 <input type="button"
          id="init4-btn" class="btn btn-primary" value="开始" />
      </p>
      </div>

      <img class="bs-icon"
        src="<c:url value='/resources/bootstrap/img/glyphicons/glyphicons_079_podium.png' />" />
      <h2>后台升级脚本</h2>
      <p>
        升级本次更新脚本。 <input type="button" id="migration-btn"
          class="btn btn-primary" value="开始" />
      </p>

      <img class="bs-icon"
        src="<c:url value='/resources/bootstrap/img/glyphicons/glyphicons_082_roundabout.png' />" />

      <h2>查看后台数据库连接情况</h2>
      <p>
        查看系统连接数据库情况。 <input type="button" id="db-btn" class="btn" value="刷新" />
      </p>

      <div id="database-info"></div>

      <img class="bs-icon"
        src="<c:url value='/resources/bootstrap/img/glyphicons/glyphicons_042_group.png' />" />

      <h2>查看在线用户情况</h2>
      <p>
        查看在线用户情况。 <input type="button" id="u-btn" class="btn" value="刷新" />
      </p>
      <div id="u-info"></div>

      <div id="hello-win" class="x-hidden">
        <div class="x-window-header">系统初始化</div>
      </div>

      <div id="hello3-win" class="x-hidden">
        <div class="x-window-header">院级核算系统初始化</div>
      </div>

      <div id="hello4-win" class="x-hidden">
        <div class="x-window-header">后台升级脚本</div>
      </div>

    </div>

    <hr class="soften">

    <footer>
      <p class="pull-right">
      <span class="label">${copyright}&nbsp;&nbsp;</span>
      </p>
    </footer>

  </div>

  <script src="<c:url value='/resources/bootstrap/js/bootstrap.min.js'/>"></script>

</body>
</html>