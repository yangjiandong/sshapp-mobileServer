/**
 * 全局静态方法
 */
Ext.namespace("Seam");
Seam.serverSide = false;

Ext.namespace('Divo');
Divo = function() {

  /* --------------------------- private属性 ------------------------- */
  var loading, mask;
  var sysConfig, roles, path;

  /* --------------------------- private方法 ------------------------- */

  // 初始化Ext.QuickTips
  function intExtControl() {
    Ext.form.Field.prototype.msgTarget = 'under';
    if (Ext.QuickTips) {
      Ext.QuickTips.init();
      Ext.QuickTips.interceptTitles = true;
    }
    Ext.BLANK_IMAGE_URL = path + 'public/images/default/s.gif';
  }

  // 中文语言包(ext-lang-zh_CN.js)补丁
  function applyLangPatch() {
    if (Ext.PagingToolbar) {
      Ext.apply(Ext.PagingToolbar.prototype, {
            beforePageText : "第",
            afterPageText : "页共 {0} 页",
            firstText : "第一页",
            prevText : "前一页",
            nextText : "下一页",
            lastText : "最后页",
            refreshText : "刷新",
            displayMsg : "显示{0}-{1}共{2}条",
            emptyMsg : '共0条'
          });
    }
    if (Ext.DatePicker) {
      Ext.apply(Ext.DatePicker.prototype, {
            todayText : "今天",
            minText : "日期在最小日期之前",
            maxText : "日期在最大日期之后",
            disabledDaysText : "",
            disabledDatesText : "",
            monthNames : Date.monthNames,
            dayNames : Date.dayNames,
            nextText : '下月 (Control+Right)',
            prevText : '上月 (Control+Left)',
            monthYearText : '选择一个月 (Control+Up/Down 来改变年)',
            todayTip : "{0} (空格键选择)",
            format : "y年m月d日"
          });
    }
    if (Ext.grid.GroupingView) {
      Ext.apply(Ext.grid.GroupingView.prototype, {
            groupByText : '分组',
            showGroupsText : '分组有效'
          });
    }

  }

  // 远程调用(兼容DWR和JBossSeam)
  function rmtCall(method, callback, params, isAsync) {
    var s = method.split('.');
    var callParams = [];
    if (params) {
      for (i = 0; i < params.length; i++) {
        callParams.push(params[i]);
      }
    }
    if (Seam.serverSide) {
      callParams.push(callback);
      var obj = Seam.Component.newInstance(s[0]);
      Seam.Remoting.async = isAsync;
      Seam.Remoting.displayLoadingMessage = function() {
      };
      Seam.Remoting.hideLoadingMessage = function() {
      };
      eval('obj.' + s[1] + '.apply(obj, callParams)');
    } else {
      callParams.push({
            callback : callback,
            async : isAsync
          });
      eval(method + '.apply(' + s[0] + ', callParams)');
    }
  }

  /* --------------------------- public方法 -------------------------- */
  return {
    init : function() {
      intExtControl();
      applyLangPatch();
    },

    /**
     * 取得url中的上下文路径，例如'/divo/'
     */
    getPath : function() {
      return path;
    },
    /**
     * 取得完整的上下文路径，例如'http://localhost:8080/divo/'
     */
    getFullPath : function() {
      return 'http://' + location.host + path;
    },
    // image目录
    getImagePath : function() {
      return path + 'public/images/';
    },

    // icon目录
    getIconPath : function() {
      return path + 'public/icons/';
    },
    getIconAdd : function() {
      return path + 'public/icons/add.gif';
    },
    getIconEdit : function() {
      return path + 'public/icons/edit.gif';
    },
    getIconDelete : function() {
      return path + 'public/icons/delete.gif';
    },
    getIconExpandAll : function() {
      return path + 'public/icons/expandall.gif';
    },
    getIconCollapseAll : function() {
      return path + 'public/icons/collapseall.gif';
    },
    getIconSearch : function() {
      return path + 'public/icons/search.gif';
    },
    getIconSave : function() {
      return path + 'public/icons/save.gif';
    },
    getIconRefresh : function() {
      return path + 'public/icons/refresh.gif';
    },

    // 当前注册用户相关
    getUserId : function() {
      return sysConfig.userId;
    },
    getUserName : function() {
      return sysConfig.userName;
    },
    /**
     * 如果给出了传递参数,表示要求判断指定用户Id的用户是否为系统管理员
     */
    isAdmin : function(userId) {
      if (userId !== undefined)
        return userId === 3;
      return sysConfig.isAdmin;
    },
    hasRole : function(name) {
      if (!roles)
        return false;
      for (var i = 0; i < roles.length; i++) {
        if (roles[i].name === name)
          return true;
      }
      return false;
    },

    // 方便调用PageBus
    subscribe : function(name, callback, scope) {
      window.PageBus.subscribe(name, scope, callback, null)
    },
    publish : function(name, message) {
      window.PageBus.publish(name, message);
    },
    // 其他
    getLastTime : function(v) {
      if (v.length < 18)
        return "";
      var d = Date.parseDate(v, 'Y-m-d H:i:s');
      var m = d.getElapsed();
      var minutes = Math.floor(m / 1000 / 60);
      var seconds = Math.floor(m / 1000) % 60;
      var hours = Math.floor(m / 1000 / 60 / 60);
      var days = Math.floor(m / 1000 / 60 / 60 / 24);
      var time = "";
      if (days > 30)
        time = v.substring(0, 10);
      else if (days > 0)
        time = days + '天前';
      else if (hours > 0)
        time = hours + '小时前';
      else if (minutes > 0)
        time = minutes + '分钟前';
      else
        time = seconds + '秒前';
      return time;
    },

    loadMask : {
      msg : '正在加载数据...',
      msgCls : 'common-text'
    },

    // 清除“正在载入”信息
    endLoading : function() {
      Ext.get('divo-loading-msg').remove();
    },

    /**
     * 获取网格元数据
     * 
     * @param {GridColumnMeta[]}
     *          result 网格列元数据数组
     * @param {Object}
     *          expander
     */
    getGridMeta : function(result, expander) {
      var meta = {};
      meta.findColumn = function(name) {
        for (var i = 0; i < this.cms.length; i++) {
          if (this.cms[i].dataIndex === name)
            return i;
        }
        return null;
      } // 按名称查找返回列所在序号
      meta.findMetaIndex = function(name) {
        for (var i = 0; i < this.metas.length; i++) {
          if (this.metas[i].name === name)
            return i;
        }
        return null;
      } // 按名称查找返回元数据数组索引号

      if (!result) {
        meta.recordDef = Ext.data.Record.create([{
              name : ""
            }]);
        meta.columnModel = new Ext.grid.ColumnModel([{
              header : ""
            }]);
        return meta;
      }

      var rs = [], cms = [], fs = [], metas = [];
      if (expander)
        cms.push(expander);
      for (var i = 0; i < result.length; i++) {
        metas.push({
              name : result[i].name,
              header : result[i].header
            });
        rs.push({
              name : result[i].name,
              type : result[i].type
            });
        if (result[i].header) {
          cms.push({
                header : result[i].header,
                dataIndex : result[i].name,
                type : result[i].type
              });
          if (result[i].searchable)
            fs.push({
                  text : result[i].header,
                  name : result[i].searchName
                });
        }
      }
      meta.recordDef = Ext.data.Record.create(rs);
      meta.columnModel = new Ext.grid.ColumnModel(cms);
      meta.columnModel.defaultSortable = true;
      meta.rs = rs;
      meta.cms = cms;
      meta.fs = fs;
      meta.metas = metas;
      return meta;
    },

    /**
     * 显示错误提示信息,并等待用户确认
     */
    alert : function(msg) {
      Ext.MessageBox.alert("错误", msg);
    },
    /**
     * 显示能自动消失的提示信息
     */
    say : function(msg) {
      if (parent && parent.Divo && parent.Divo.app && parent.Divo.app.StatusBar) {
        parent.Divo.app.StatusBar.success(msg);
      } else if (parent && parent.parent && parent.parent.Divo && parent.parent.Divo.app
          && parent.parent.Divo.app.StatusBar) {
        parent.parent.Divo.app.StatusBar.success(msg);
      } else {
        if (Divo.app.StatusBar)
          Divo.app.StatusBar.success(msg);
      }
    },
    error : function(msg) {
      if (parent && parent.Divo && parent.Divo.app && parent.Divo.app.StatusBar) {
        parent.Divo.app.StatusBar.error(msg);
      } else if (parent && parent.parent && parent.parent.Divo && parent.parent.Divo.app
          && parent.parent.Divo.app.StatusBar) {
        parent.parent.Divo.app.StatusBar.error(msg);
      } else {
        Divo.app.StatusBar.error(msg);
      }
    },
    wait : function(msg) {
      if (parent && parent.Divo && parent.Divo.app && parent.Divo.app.StatusBar) {
        parent.Divo.app.StatusBar.wait(msg);
      } else if (parent && parent.parent && parent.parent.Divo && parent.parent.Divo.app
          && parent.parent.Divo.app.StatusBar) {
        parent.parent.Divo.app.StatusBar.wait(msg);
      } else {
        Divo.app.StatusBar.wait(msg);
      }
    },
    clear : function(msg) {
      if (parent && parent.Divo && parent.Divo.app && parent.Divo.app.StatusBar) {
        parent.Divo.app.StatusBar.clear();
      } else if (parent && parent.parent && parent.parent.Divo && parent.parent.Divo.app
          && parent.parent.Divo.app.StatusBar) {
        parent.parent.Divo.app.StatusBar.clear();
      } else {
        Divo.app.StatusBar.clear();
      }
    },
    // 防止IFRAME销毁后仍然占用内存
    // Thanks: http://extjs.com/forum/showthread.php?t=11406
    fixIFrame : function(o, p) {
      var iFrame = p.getEl().dom;
      if (iFrame.src) {
        iFrame.src = "javascript:false";
      }
    },
    /**
     * 不显示网格列标题
     */
    hideGridHeader : function(grid) {
      grid.getView().el.select('.x-grid3-header').setStyle('display', 'none');
    },
    /**
     * 显示表单验证错误信息
     * 
     * @param {FormValidateResult}
     *          retValue 后台返回的验证信息
     * @param {Ext.Window}
     *          win
     * @param {Ext.FormPanel}
     *          form
     */
    showFormError : function(retValue, win, form) {
      if (retValue.results) {
        var flds = form.form.items.items;
        for (var i = 0; i < flds.length; i++) {
          var fld = flds[i];
          fld.clearInvalid();
          for (k = 0; k < retValue.results.length; k++) {
            var key = retValue.results[k].fieldName;
            if (fld.id == key) {
              fld.markInvalid(retValue.results[k].errorMessage);
            }
          }
        }
      } else {
        Divo.say(retValue.errorDescrip);
      }
      if (win)
        win.el.frame('ff0000', 1, {
              duration : 1
            });
    },

    /**
     * 远程[异步]调用服务器端方法
     * 
     * @param {String}
     *          method 格式为:类名.方法名
     * @param {Function}
     *          callback 回调函数
     * @param {Array}
     *          params 传递参数
     */
    rmtCallAsync : function(method, callback, params) {
      if (!callback)
        callback = Ext.emptyFn();
      rmtCall(method, callback, params, true);
    },
    /**
     * 远程[同步]调用服务器端方法
     * 
     * @param {String}
     *          method 格式为:类名.方法名
     * @param {Function}
     *          callback 回调函数
     * @param {Array}
     *          params 传递参数
     */
    rmtCallSynch : function(method, callback, params) {
      if (!callback)
        callback = Ext.emptyFn();
      rmtCall(method, callback, params, false);
    },
    /**
     * 创建对象,传递给服务器端 约定:Seam组件name不用"."分隔
     * 
     * @param {String}
     *          className 类名(用DWR时没有用)
     * @param {Object}
     *          data 含有数据的对象
     */
    rmtCreate : function(className, data) {
      if (Seam.serverSide) {
        var obj;
        if (className.indexOf('.') > 0)
          obj = Seam.Remoting.createType(className); // 创建非Seam组件类对象
        else
          obj = Seam.Component.newInstance(className);
        for (var p in data) {
          obj[p] = data[p];
        }
        return obj;
      }
      return data;
    },
    // 回车键自动转到下一个输入框(只适用于IE)
    enter2Tab : function(e) {
      if (!window.event)
        return;
      var k = window.event.keyCode;
      if (k != 13)
        return;
      for (var frm = 0; frm < document.forms.length; frm++) {
        for (var fld = 0; fld < document.forms[frm].elements.length; fld++) {
          var elt = document.forms[frm].elements[fld];
          if (e.type == 'submit') {
            return true;
          } else {
            window.event.keyCode = 9;
            return false;
          }
        } // for
      } // for
    }

  } // return

}(); // Divo

// 初始化
Divo.init();

// EOP
