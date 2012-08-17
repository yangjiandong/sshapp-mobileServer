<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<!-- 点击装载，ie下显示不正常 -->
<script>
  $(document).ready(
          function() {

            $("input[type='text']").focus(function() {
                if($(this).val() == '请输入...')
                $(this).val("");
             });

            $("input[type='text']").blur(function() {
                if($(this).val() == "")
                $(this).val("请输入...");
             });

            $("#login_name").focus();
            $("#login_name").val('<c:out value="${remember_user_name}"/>');

            $("#login_name").keypress(function(e){
                if(e.keyCode == 13){
                  $("#password").focus();
                }
              });

            $("#password").keypress(function(e){
              if(e.keyCode == 13){
                $("#login_submit").click();
              }
            });
            $.getJSON('../common/allsubsystems',function(alldata) {
                      if (alldata.success) {
                        var items = [];
                        items.push('<div id="resources">');
                        $.each(alldata.data, function(index, value) {
                              var x = value.url;
                              items.push('<li class="'+value.iconCls+'"><a href="' + x + '">'
                                  + value.text
                                  + '</a>'
                                  + value.note
                                  + '</li>');
                            });
                        items.push('</div>');

                        $('<ul/>', {html : items.join('')}).appendTo('#leftbar');
                      }

                    });

            $("#reset").click(function() {
              $("#login_name").val('');
              $("#password").val('');
            });

            //登录
            $('#login_submit').click(function() {
                  $('#login_submit').attr({"disabled":"disabled"});

                  if ($("#login_name").val() == '' || $("#password").val() == ''){
                    $('#login_submit').removeAttr("disabled");
                    return;
                  }

                  $.getJSON('../common/logon', {
                        username : $("#login_name").val(),
                        password : $("#password").val()
                      },function(ret) {
                          if (ret.success) {
                              //$('#loginInfo').html("正在获取用户授权菜单...");
                              //$('#loginInfo').addClass('error');
                              //jQuery("#change_pwd_submit").removeAttr("disabled");

                            $.getJSON('../common/allsubsystems',function(alldata) {
                                if (alldata.success) {
                                    $('#resources').remove();
                                    var items = [];
                                    items.push('<div id="resources">');
                                    $.each(alldata.data,function(index, value) {
                                       var x = value.url;
                                       items.push('<li class="'+value.iconCls+'"><a href="' + x + '">'
                                       + value.text
                                       + '</a>'
                                       + value.note
                                       + '</li>');
                                     });
                                     items.push('</div>');
                                     $('<ul/>',{html : items.join('')}).appendTo('#leftbar');
                                     $('#loginDiv').remove();
                                     window.location.reload(true);
                                   }
                              });

                         } else {
                           $("#login_name").focus();

                           $('#errorDiv').html(ret.message);
                           $('#errorDiv').addClass('error');
                           $('#login_submit').removeAttr("disabled");

                         }
                      });
                    });

            //修改密码
            $('#change_pwd_submit').click(function() {
                jQuery(this).attr({"disabled":"disabled"});

                if ($("#old_password").val() == '' || $("#new_password").val() == ''){
                  jQuery("#change_pwd_submit").removeAttr("disabled");
                  return;
                }

                $.getJSON('../system/change_password', {
                      old_password : $("#old_password").val(),
                      new_password : $("#new_password").val(),
                      new_password2: $("#new_password2").val()
                    },function(ret) {
                        if (ret.success) {
                          $.getJSON('../common/allsubsystems',function(alldata) {
                              if (alldata.success) {
                                  $('#resources').remove();
                                  var items = [];
                                  items.push('<div id="resources">');
                                  $.each(alldata.data,function(index, value) {
                                     var x = value.url;
                                     items.push('<li class="'+value.iconCls+'"><a href="' + x + '">'
                                     + value.text
                                     + '</a>'
                                     + value.note
                                     + '</li>');
                                   });
                                   items.push('</div>');
                                   $('<ul/>',{html : items.join('')}).appendTo('#leftbar');
                                   $('#loginDiv').remove();
                                   //window.location.reload(true);
                                   //$('#loginInfo').html("欢迎你2," + ret.userName);
                                 }
                            });

                       } else {
                         $('#errorDiv').html(ret.message);
                         $('#errorDiv').addClass('error');
                         jQuery("#change_pwd_submit").removeAttr("disabled");
                       }
                    });
                  });

          })
</script>

<div class="marketing">
  <div id="leftbar" class="span-12"></div>

  <div class="span-12 last" id="loginDiv">

    <c:if test="${empty userSession.account}">
      <div id="errorDiv"></div>
      <fieldset>
            <legend>用户登录</legend>
        <p>
          <label for="login_name">用户名</label><br> <input type="text"
            class="title" name="login_name" id="login_name" value="">
        </p>
        <p>
          <label for="password">密码</label><br> <input type="password"
            class="title" id="password" name="password" value="">
        </p>

        <button id="login_submit">登录</button>
        </fieldset>
    </c:if>

    <c:if test="${not empty userSession.account}">
    <c:if test="${userSession.account.updatePwd=='0'}">
      <div id="errorDiv"></div>
      <fieldset>
            <legend>用户修改密码</legend>
        <p>
          <label for="old_password">原密码</label><br> <input type="password"
            class="title" id="old_password" name="old_password" value="">
        </p>
        <p>
          <label for="new_password">新密码</label><br> <input type="password"
            class="title" id="new_password" name="new_password" maxlength=20 value="">
        </p>
        <p>
          <label for="new_password2">确认新密码</label><br> <input type="password"
            class="title" id="new_password2" name="new_password2" maxlength=20 value="">
        </p>
        <button id="change_pwd_submit">确定</button>
        </fieldset>
    </c:if>
    </c:if>

  </div>

</div>
