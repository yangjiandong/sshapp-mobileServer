package org.apache.jsp.views;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.ssh.pm.hcost.web.PageUtils;
import java.util.*;

public final class index_blueprint_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(1);
    _jspx_dependants.add("/views/../common/taglibs.jsp");
  }

  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fif_0026_005ftest;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005furl_0026_005fvalue_005fnobody;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005furl_0026_005fvalue_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
    _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.release();
    _005fjspx_005ftagPool_005fc_005furl_0026_005fvalue_005fnobody.release();
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html;charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write('\r');
      out.write('\n');
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      if (_jspx_meth_c_005fset_005f0(_jspx_page_context))
        return;
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");

  String path = request.getContextPath();
  String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
      + path + "/";

  pageContext.setAttribute("application_name", (String)PageUtils.getApplicationInfos().get("application_name"));
  pageContext.setAttribute("version", (String)PageUtils.getApplicationInfos().get("version"));
  pageContext.setAttribute("ext", "resources/ext");
  pageContext.setAttribute("run_mode", (String)PageUtils.getApplicationInfos().get("run_mode"));
  pageContext.setAttribute("apath", basePath);
  pageContext.setAttribute("copyright", (String)PageUtils.getApplicationInfos().get("copyright"));
  pageContext.setAttribute("buildId", (String)PageUtils.getApplicationInfos().get("buildId"));


      out.write('\r');
      out.write('\n');
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<script>\r\n");
      out.write("\r\n");
      out.write("  function showLoading() {\r\n");
      out.write("    $(\"#login_loading\").show();\r\n");
      out.write("  }\r\n");
      out.write("\r\n");
      out.write("  function hideLoading() {\r\n");
      out.write("    $(\"#login_loading\").hide();\r\n");
      out.write("  }\r\n");
      out.write("\r\n");
      out.write("  function fullscreen(){ //在ie下可行\r\n");
      out.write("    //var wsh = new ActiveXObject(\"WScript.Shell\");\r\n");
      out.write("    //wsh.sendKeys(\"{F11}\");\r\n");
      out.write("  }\r\n");
      out.write("\r\n");
      out.write("  $(document).ready(\r\n");
      out.write("          function() {\r\n");
      out.write("\r\n");
      out.write("            $(\"input[type='text']\").focus(function() {\r\n");
      out.write("                if($(this).val() == '请输入...')\r\n");
      out.write("                $(this).val(\"\");\r\n");
      out.write("             });\r\n");
      out.write("\r\n");
      out.write("            $(\"input[type='text']\").blur(function() {\r\n");
      out.write("                if($(this).val() == \"\")\r\n");
      out.write("                $(this).val(\"请输入...\");\r\n");
      out.write("             });\r\n");
      out.write("\r\n");
      out.write("            $(\"#login_name\").focus();\r\n");
      out.write("            $(\"#login_name\").val('");
      if (_jspx_meth_c_005fout_005f0(_jspx_page_context))
        return;
      out.write("');\r\n");
      out.write("\r\n");
      out.write("            $(\"#login_name\").keypress(function(e){\r\n");
      out.write("                if(e.keyCode == 13){\r\n");
      out.write("                  $(\"#password\").focus();\r\n");
      out.write("                }\r\n");
      out.write("              });\r\n");
      out.write("\r\n");
      out.write("            $(\"#password\").keypress(function(e){\r\n");
      out.write("              if(e.keyCode == 13){\r\n");
      out.write("                $(\"#login_submit\").click();\r\n");
      out.write("              }\r\n");
      out.write("            });\r\n");
      out.write("            $.getJSON('../common/allsubsystems',function(alldata) {\r\n");
      out.write("                      if (alldata.success) {\r\n");
      out.write("                        var items = [];\r\n");
      out.write("                        items.push('<div id=\"resources\">');\r\n");
      out.write("                        $.each(alldata.data, function(index, value) {\r\n");
      out.write("                              var x = value.url;\r\n");
      out.write("                              items.push('<li class=\"'+value.iconCls+'\"><a href=\"' + x + '\">'\r\n");
      out.write("                                  + value.text\r\n");
      out.write("                                  + '</a>'\r\n");
      out.write("                                  + value.note\r\n");
      out.write("                                  + '</li>');\r\n");
      out.write("                            });\r\n");
      out.write("                        items.push('</div>');\r\n");
      out.write("\r\n");
      out.write("                        $('<ul/>', {html : items.join('')}).appendTo('#leftbar');\r\n");
      out.write("                      }\r\n");
      out.write("\r\n");
      out.write("                    });\r\n");
      out.write("\r\n");
      out.write("            $(\"#reset\").click(function() {\r\n");
      out.write("              $(\"#login_name\").val('');\r\n");
      out.write("              $(\"#password\").val('');\r\n");
      out.write("            });\r\n");
      out.write("\r\n");
      out.write("            //登录\r\n");
      out.write("            $('#login_submit').click(function() {\r\n");
      out.write("                  $('#login_submit').attr({\"disabled\":\"disabled\"});\r\n");
      out.write("\r\n");
      out.write("                  if ($(\"#login_name\").val() == '' || $(\"#password\").val() == ''){\r\n");
      out.write("                    $('#login_submit').removeAttr(\"disabled\");\r\n");
      out.write("                    return;\r\n");
      out.write("                  }\r\n");
      out.write("\r\n");
      out.write("                  $.getJSON('../common/logon', {\r\n");
      out.write("                        username : $(\"#login_name\").val(),\r\n");
      out.write("                        password : $(\"#password\").val()\r\n");
      out.write("                      },function(ret) {\r\n");
      out.write("                          if (ret.success) {\r\n");
      out.write("                          showLoading();\r\n");
      out.write("\r\n");
      out.write("                            $.getJSON('../common/allsubsystems',function(alldata) {\r\n");
      out.write("                                if (alldata.success) {\r\n");
      out.write("                                    $('#resources').remove();\r\n");
      out.write("                                    var items = [];\r\n");
      out.write("                                    items.push('<div id=\"resources\">');\r\n");
      out.write("                                    $.each(alldata.data,function(index, value) {\r\n");
      out.write("                                       var x = value.url;\r\n");
      out.write("                                       items.push('<li class=\"'+value.iconCls+'\"><a href=\"' + x + '\">'\r\n");
      out.write("                                       + value.text\r\n");
      out.write("                                       + '</a>'\r\n");
      out.write("                                       + value.note\r\n");
      out.write("                                       + '</li>');\r\n");
      out.write("                                     });\r\n");
      out.write("                                     items.push('</div>');\r\n");
      out.write("                                     $('<ul/>',{html : items.join('')}).appendTo('#leftbar');\r\n");
      out.write("                                     $('#loginDiv').remove();\r\n");
      out.write("                                     //fullscreen();\r\n");
      out.write("                                     window.location.reload(true);\r\n");
      out.write("\r\n");
      out.write("                                     //window.open(\"http://localhost:8090/sshapp/common/index\",\"\",\"fullscreen=yes\")\r\n");
      out.write("                                   }\r\n");
      out.write("                              });\r\n");
      out.write("\r\n");
      out.write("                         } else {\r\n");
      out.write("                           $(\"#login_name\").focus();\r\n");
      out.write("\r\n");
      out.write("                           $('#errorDiv').html(ret.message);\r\n");
      out.write("                           $('#errorDiv').addClass('error');\r\n");
      out.write("                           $('#login_submit').removeAttr(\"disabled\");\r\n");
      out.write("\r\n");
      out.write("                         }\r\n");
      out.write("                      });\r\n");
      out.write("                    });\r\n");
      out.write("\r\n");
      out.write("            //修改密码\r\n");
      out.write("            $('#change_pwd_submit').click(function() {\r\n");
      out.write("                jQuery(this).attr({\"disabled\":\"disabled\"});\r\n");
      out.write("\r\n");
      out.write("                if ($(\"#old_password\").val() == '' || $(\"#new_password\").val() == ''){\r\n");
      out.write("                  jQuery(\"#change_pwd_submit\").removeAttr(\"disabled\");\r\n");
      out.write("                  return;\r\n");
      out.write("                }\r\n");
      out.write("\r\n");
      out.write("                $.getJSON('../system/change_password', {\r\n");
      out.write("                      old_password : $(\"#old_password\").val(),\r\n");
      out.write("                      new_password : $(\"#new_password\").val(),\r\n");
      out.write("                      new_password2: $(\"#new_password2\").val()\r\n");
      out.write("                    },function(ret) {\r\n");
      out.write("                        if (ret.success) {\r\n");
      out.write("                          $.getJSON('../common/allsubsystems',function(alldata) {\r\n");
      out.write("                              if (alldata.success) {\r\n");
      out.write("                                  $('#resources').remove();\r\n");
      out.write("                                  var items = [];\r\n");
      out.write("                                  items.push('<div id=\"resources\">');\r\n");
      out.write("                                  $.each(alldata.data,function(index, value) {\r\n");
      out.write("                                     var x = value.url;\r\n");
      out.write("                                     items.push('<li class=\"'+value.iconCls+'\"><a href=\"' + x + '\">'\r\n");
      out.write("                                     + value.text\r\n");
      out.write("                                     + '</a>'\r\n");
      out.write("                                     + value.note\r\n");
      out.write("                                     + '</li>');\r\n");
      out.write("                                   });\r\n");
      out.write("                                   items.push('</div>');\r\n");
      out.write("                                   $('<ul/>',{html : items.join('')}).appendTo('#leftbar');\r\n");
      out.write("                                   $('#loginDiv').remove();\r\n");
      out.write("                                   //window.location.reload(true);\r\n");
      out.write("                                   //$('#loginInfo').html(\"欢迎你2,\" + ret.userName);\r\n");
      out.write("                                 }\r\n");
      out.write("                            });\r\n");
      out.write("\r\n");
      out.write("                       } else {\r\n");
      out.write("                         $('#errorDiv').html(ret.message);\r\n");
      out.write("                         $('#errorDiv').addClass('error');\r\n");
      out.write("                         jQuery(\"#change_pwd_submit\").removeAttr(\"disabled\");\r\n");
      out.write("                       }\r\n");
      out.write("                    });\r\n");
      out.write("                  });\r\n");
      out.write("\r\n");
      out.write("          })\r\n");
      out.write("</script>\r\n");
      out.write("\r\n");
      out.write("<div class=\"container\">\r\n");
      out.write("  <div id=\"leftbar\" class=\"span-12\"></div>\r\n");
      out.write("\r\n");
      out.write("  <div class=\"span-12 last\" id=\"loginDiv\">\r\n");
      out.write("\r\n");
      out.write("    ");
      if (_jspx_meth_c_005fif_005f0(_jspx_page_context))
        return;
      out.write("\r\n");
      out.write("\r\n");
      out.write("    ");
      if (_jspx_meth_c_005fif_005f1(_jspx_page_context))
        return;
      out.write("\r\n");
      out.write("\r\n");
      out.write("  </div>\r\n");
      out.write("\r\n");
      out.write("</div>\r\n");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try { out.clearBuffer(); } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }

  private boolean _jspx_meth_c_005fset_005f0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:set
    org.apache.taglibs.standard.tag.rt.core.SetTag _jspx_th_c_005fset_005f0 = (org.apache.taglibs.standard.tag.rt.core.SetTag) _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody.get(org.apache.taglibs.standard.tag.rt.core.SetTag.class);
    _jspx_th_c_005fset_005f0.setPageContext(_jspx_page_context);
    _jspx_th_c_005fset_005f0.setParent(null);
    // /views/../common/taglibs.jsp(9,0) name = var type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fset_005f0.setVar("ctx");
    // /views/../common/taglibs.jsp(9,0) name = value type = javax.el.ValueExpression reqTime = true required = false fragment = false deferredValue = true expectedTypeName = java.lang.Object deferredMethod = false methodSignature = null
    _jspx_th_c_005fset_005f0.setValue(new org.apache.jasper.el.JspValueExpression("/views/../common/taglibs.jsp(9,0) '${pageContext.request.contextPath}'",_el_expressionfactory.createValueExpression(_jspx_page_context.getELContext(),"${pageContext.request.contextPath}",java.lang.Object.class)).getValue(_jspx_page_context.getELContext()));
    int _jspx_eval_c_005fset_005f0 = _jspx_th_c_005fset_005f0.doStartTag();
    if (_jspx_th_c_005fset_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody.reuse(_jspx_th_c_005fset_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody.reuse(_jspx_th_c_005fset_005f0);
    return false;
  }

  private boolean _jspx_meth_c_005fout_005f0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:out
    org.apache.taglibs.standard.tag.rt.core.OutTag _jspx_th_c_005fout_005f0 = (org.apache.taglibs.standard.tag.rt.core.OutTag) _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.get(org.apache.taglibs.standard.tag.rt.core.OutTag.class);
    _jspx_th_c_005fout_005f0.setPageContext(_jspx_page_context);
    _jspx_th_c_005fout_005f0.setParent(null);
    // /views/index.blueprint.jsp(34,34) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fout_005f0.setValue((java.lang.Object) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${remember_user_name}", java.lang.Object.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_c_005fout_005f0 = _jspx_th_c_005fout_005f0.doStartTag();
    if (_jspx_th_c_005fout_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f0);
    return false;
  }

  private boolean _jspx_meth_c_005fif_005f0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:if
    org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f0 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
    _jspx_th_c_005fif_005f0.setPageContext(_jspx_page_context);
    _jspx_th_c_005fif_005f0.setParent(null);
    // /views/index.blueprint.jsp(173,4) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fif_005f0.setTest(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${empty userSession.account}", java.lang.Boolean.class, (PageContext)_jspx_page_context, null, false)).booleanValue());
    int _jspx_eval_c_005fif_005f0 = _jspx_th_c_005fif_005f0.doStartTag();
    if (_jspx_eval_c_005fif_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\r\n");
        out.write("      <div id=\"errorDiv\"></div>\r\n");
        out.write("      <fieldset>\r\n");
        out.write("            <legend>用户登录</legend>\r\n");
        out.write("        <p>\r\n");
        out.write("          <label for=\"login_name\">用户名</label><br> <input type=\"text\"\r\n");
        out.write("            class=\"title\" name=\"login_name\" id=\"login_name\" value=\"\">\r\n");
        out.write("        </p>\r\n");
        out.write("        <p>\r\n");
        out.write("          <label for=\"password\">密码</label><br> <input type=\"password\"\r\n");
        out.write("            class=\"title\" id=\"password\" name=\"password\" value=\"\">\r\n");
        out.write("        </p>\r\n");
        out.write("\r\n");
        out.write("        <div id=\"login_loading\"><p><img src=\"");
        if (_jspx_meth_c_005furl_005f0(_jspx_th_c_005fif_005f0, _jspx_page_context))
          return true;
        out.write("\" /> 正在获取用户授权菜单...</p></div>\r\n");
        out.write("        <button id=\"login_submit\">登录</button>\r\n");
        out.write("        </fieldset>\r\n");
        out.write("    ");
        int evalDoAfterBody = _jspx_th_c_005fif_005f0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_c_005fif_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f0);
    return false;
  }

  private boolean _jspx_meth_c_005furl_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:url
    org.apache.taglibs.standard.tag.rt.core.UrlTag _jspx_th_c_005furl_005f0 = (org.apache.taglibs.standard.tag.rt.core.UrlTag) _005fjspx_005ftagPool_005fc_005furl_0026_005fvalue_005fnobody.get(org.apache.taglibs.standard.tag.rt.core.UrlTag.class);
    _jspx_th_c_005furl_005f0.setPageContext(_jspx_page_context);
    _jspx_th_c_005furl_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f0);
    // /views/index.blueprint.jsp(186,45) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005furl_005f0.setValue("/resources/img/large-loading.gif");
    int _jspx_eval_c_005furl_005f0 = _jspx_th_c_005furl_005f0.doStartTag();
    if (_jspx_th_c_005furl_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005furl_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005furl_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005furl_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005furl_005f0);
    return false;
  }

  private boolean _jspx_meth_c_005fif_005f1(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:if
    org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f1 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
    _jspx_th_c_005fif_005f1.setPageContext(_jspx_page_context);
    _jspx_th_c_005fif_005f1.setParent(null);
    // /views/index.blueprint.jsp(191,4) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fif_005f1.setTest(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${not empty userSession.account}", java.lang.Boolean.class, (PageContext)_jspx_page_context, null, false)).booleanValue());
    int _jspx_eval_c_005fif_005f1 = _jspx_th_c_005fif_005f1.doStartTag();
    if (_jspx_eval_c_005fif_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\r\n");
        out.write("    ");
        if (_jspx_meth_c_005fif_005f2(_jspx_th_c_005fif_005f1, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("    ");
        int evalDoAfterBody = _jspx_th_c_005fif_005f1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_c_005fif_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f1);
    return false;
  }

  private boolean _jspx_meth_c_005fif_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:if
    org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f2 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
    _jspx_th_c_005fif_005f2.setPageContext(_jspx_page_context);
    _jspx_th_c_005fif_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f1);
    // /views/index.blueprint.jsp(192,4) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fif_005f2.setTest(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${userSession.account.updatePwd=='0'}", java.lang.Boolean.class, (PageContext)_jspx_page_context, null, false)).booleanValue());
    int _jspx_eval_c_005fif_005f2 = _jspx_th_c_005fif_005f2.doStartTag();
    if (_jspx_eval_c_005fif_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\r\n");
        out.write("      <div id=\"errorDiv\"></div>\r\n");
        out.write("      <fieldset>\r\n");
        out.write("            <legend>用户修改密码</legend>\r\n");
        out.write("        <p>\r\n");
        out.write("          <label for=\"old_password\">原密码</label><br> <input type=\"password\"\r\n");
        out.write("            class=\"title\" id=\"old_password\" name=\"old_password\" value=\"\">\r\n");
        out.write("        </p>\r\n");
        out.write("        <p>\r\n");
        out.write("          <label for=\"new_password\">新密码</label><br> <input type=\"password\"\r\n");
        out.write("            class=\"title\" id=\"new_password\" name=\"new_password\" maxlength=20 value=\"\">\r\n");
        out.write("        </p>\r\n");
        out.write("        <p>\r\n");
        out.write("          <label for=\"new_password2\">确认新密码</label><br> <input type=\"password\"\r\n");
        out.write("            class=\"title\" id=\"new_password2\" name=\"new_password2\" maxlength=20 value=\"\">\r\n");
        out.write("        </p>\r\n");
        out.write("        <button id=\"change_pwd_submit\">确定</button>\r\n");
        out.write("        </fieldset>\r\n");
        out.write("    ");
        int evalDoAfterBody = _jspx_th_c_005fif_005f2.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_c_005fif_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f2);
    return false;
  }
}
