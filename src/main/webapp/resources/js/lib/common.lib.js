// 定义程序

App = {
  version : '1.0.0',
  versionDetail : {
    major : 1,
    minor : 0,
    patch : 0
  },
  // 打印控件
  loopname : "江苏鑫亿软件有限公司",
  loopno : "059717168848673851905623562384",
  // 简单连接样式
  alinkStyle : "<STYLE>a { color:#0000FF;text-decoration:underline;}"
      + "a:visited { color:#CC0000; text-decoration:underline; }"
      + "a:hover { color:#CC0000; text-decoration:none; }"
      + "a:active { color:#CC00CC;text-decoration:underline; }" + "</STYLE>",

  btnPrevImage : 'resources/img/fam.icons/left.gif',
  btnNextImage : 'resources/img/fam.icons/right.gif',
  orgFloor : 2,
  resourceFloor : 2
};

// 分页
App.defaultPage = (function() {
  var page = 20;
  Ext.Ajax.request({
        url : "common/get_default_page",
        method : 'POST',
        async : false,

        callback : function(o, s, r) {
          var resp = Ext.decode(r.responseText);
          if (resp.success) {
            page = parseInt(resp.data);
          }
        }
      });

  return page;
}());

// 弹出窗口的尺寸
WindowSize = {
  height : 500,
  width : 700,
  widthScala : 0.90,
  heightScala : 0.85
};

Ext.BLANK_IMAGE_URL = 'resources/img/s.gif';
Ext.QuickTips.init();
// Ext.Ajax.timeout = 300000;//12000s,90000;

// 解决ExtJs TextField maxLength后还是可以输入
Ext.form.TextField.prototype.size = 20;
Ext.form.TextField.prototype.initValue = function() {
  if (this.value !== undefined) {
    this.setValue(this.value);
  } else if (this.el.dom.value.length > 0) {
    this.setValue(this.el.dom.value);
  }
  this.el.dom.size = this.size;
  if (!isNaN(this.maxLength) && (this.maxLength * 1) > 0
      && (this.maxLength != Number.MAX_VALUE)) {
    this.el.dom.maxLength = this.maxLength * 1;
  }
};

// Ext grid 超级强大的动态添加字段、列扩展
// grid.addColumn('C'); //添加C列，空数据
// grid.addColumn({name: 'D', defaultValue: 'D'}, {header: 'D', dataIndex:
// 'D'});//添加D列，有D数据
// grid.removeColumn('B');//把之前的B列删除
Ext.override(Ext.data.Store, {
      addField : function(field) {
        field = new Ext.data.Field(field);
        this.recordType.prototype.fields.replace(field);
        if (typeof field.defaultValue != 'undefined') {
          this.each(function(r) {
                if (typeof r.data[field.name] == 'undefined') {
                  r.data[field.name] = field.defaultValue;
                }
              });
        }
      },
      removeField : function(name) {
        this.recordType.prototype.fields.removeKey(name);
        this.each(function(r) {
              delete r.data[name];
              if (r.modified) {
                delete r.modified[name];
              }
            });
      }
    });
Ext.override(Ext.grid.ColumnModel, {
      addColumn : function(column, colIndex) {
        if (typeof column == 'string') {
          column = {
            header : column,
            dataIndex : column
          };
        }
        var config = this.config;
        this.config = [];
        if (typeof colIndex == 'number') {
          config.splice(colIndex, 0, column);
        } else {
          colIndex = config.push(column);
        }
        this.setConfig(config);
        return colIndex;
      },
      removeColumn : function(colIndex) {
        var config = this.config;
        this.config = [config[colIndex]];
        config.splice(colIndex, 1);
        this.setConfig(config);
      }
    });
Ext.override(Ext.grid.GridPanel, {
      // 不增加field
      addColumnNoField : function(field, column, colIndex) {
        if (!column) {
          if (field.dataIndex) {
            column = field;
            field = field.dataIndex;
          } else {
            column = field.name || field;
          }
        }
        // 取消，会影响记录显示
        // this.store.addField(field);
        return this.colModel.addColumn(column, colIndex);
      },
      addColumn : function(field, column, colIndex) {
        if (!column) {
          if (field.dataIndex) {
            column = field;
            field = field.dataIndex;
          } else {
            column = field.name || field;
          }
        }
        this.store.addField(field);
        return this.colModel.addColumn(column, colIndex);
      },
      removeColumn : function(name, colIndex) {
        this.store.removeField(name);
        if (typeof colIndex != 'number') {
          colIndex = this.colModel.findColumnIndex(name);
        }
        if (colIndex >= 0) {
          this.colModel.removeColumn(colIndex);
        }
      }
    });

Ext.namespace('Divo');
Ext.namespace('Divo.Com');
Ext.ns('Divo.com');

Divo.com.LOOP_EM = '<div id="LodopDiv"><object id="LODOP" '
    + 'classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0>'
    + '<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 '
    + 'pluginspage="install_lodop.exe"></embed></object></div>'

Divo.com.Lib = function() {
  var t1 = '<tpl for=\".\">';
  var t2 = '</tpl>';
  /** *************** private method ******************* */

  /**
   * 返回ds,核算单元
   */
  function getOrgDs() {
    var recordDef = Ext.data.Record.create([{
          name : "id",
          type : "float"
        }, {
          name : "orgCode",
          type : "string"
        }, {
          name : "orgName",
          type : "string"
        }]);

    var ds = new Ext.data.Store({
          proxy : new Ext.data.HttpProxy({
                url : 'org/queryii'
              }),
          reader : new Ext.data.JsonReader({
                root : 'rows',
                totalProperty : 'totalCount',
                id : 'id'
              }, recordDef)
        });

    return ds;

  }

  /**
   * 返回ds,用户授权过的核算单元
   */
  function getUserOrgDs() {

    var recordDef = Ext.data.Record.create([{
          name : "id",
          type : "float"
        }, {
          name : "orgCode",
          type : "string"
        }, {
          name : "orgName",
          type : "string"
        }]);

    var ds = new Ext.data.Store({
          proxy : new Ext.data.HttpProxy({
                url : 'userOrgs/getUserOrgs'
              }),
          reader : new Ext.data.JsonReader({
                root : 'rows',
                totalProperty : 'totalCount',
                id : 'id'
              }, recordDef)
        });

    return ds;

  }

  /**
   * 返回tpl,核算单元
   */
  function getOrgTpl() {

    var resultTpl = [t1 + '<div class="x-combo-list-item">',
        '<b>{orgName}</b><br>', '</div>' + t2].join('');

    return resultTpl;

  }
  /**
   * 返回ds,收入分类
   */
  function getIncomeDs() {

    var recordDef = Ext.data.Record.create([{
          name : "id",
          type : "float"
        }, {
          name : "incomeCatCode",
          type : "string"
        }, {
          name : "incomeCatName",
          type : "string"
        }]);

    var ds = new Ext.data.Store({
          proxy : new Ext.data.HttpProxy({
                url : 'incomeCategory/query'
              }),
          reader : new Ext.data.JsonReader({
                root : 'rows',
                totalProperty : 'totalCount',
                id : 'id'
              }, recordDef)
        });

    return ds;

  }
  /**
   * 返回tpl,收入分类
   */
  function getIncomeTpl() {

    var resultTpl = [t1 + '<div class="x-combo-list-item">',
        '{incomeCatCode} <b>{incomeCatName}</b>', '</div>' + t2].join('');

    return resultTpl;

  }

  /**
   * 返回ds,成本分类
   */
  function getCostDs() {

    var recordDef = Ext.data.Record.create([{
          name : "id",
          type : "float"
        }, {
          name : "costCatCode",
          type : "string"
        }, {
          name : "costCatName",
          type : "string"
        }]);

    var ds = new Ext.data.Store({
          proxy : new Ext.data.HttpProxy({
                url : 'costCategory/query'
              }),
          reader : new Ext.data.JsonReader({
                root : 'rows',
                totalProperty : 'totalCount',
                id : 'id'
              }, recordDef)
        });

    return ds;

  }
  /**
   * 返回tpl,成本分类
   */
  function getCostTpl() {

    // style="background-color : #D5DFF2;"
    var resultTpl = [t1 + '<div class="x-combo-list-item">',
        '{costCatCode} <b>{costCatName}</b>', '</div>' + t2].join('');

    return resultTpl;

  }
  /**
   * 返回ds,核算单元类别
   */
  function getOrgTypeDs() {

    var recordDef = Ext.data.Record.create([{
          name : "id",
          type : "float"
        }, {
          name : "code",
          type : "string"
        }, {
          name : "name",
          type : "string"
        }]);

    var ds = new Ext.data.Store({
          proxy : new Ext.data.HttpProxy({
                url : 'orgType/query'
              }),
          reader : new Ext.data.JsonReader({
                root : 'rows',
                totalProperty : 'totalCount',
                id : 'id'
              }, recordDef)
        });

    return ds;

  }
  /**
   * 返回tpl,核算单元类别
   */
  function getOrgTypeTpl() {

    var resultTpl = [t1 + '<div class="x-combo-list-item">', '<b>{name}</b>',
        '</div>' + t2].join('');

    return resultTpl;

  }

  /**
   * 返回ds,病人费别
   */
  function getCheckOutDs() {

    var recordDef = Ext.data.Record.create([{
          name : "code",
          type : "string"
        }, {
          name : "name",
          type : "string"
        }]);

    var ds = new Ext.data.Store({
          proxy : new Ext.data.HttpProxy({
                url : 'checkOut/getCheckOut'
              }),
          reader : new Ext.data.JsonReader({
                root : 'rows',
                totalProperty : 'totalCount',
                id : 'code'
              }, recordDef)
        });

    return ds;

  }
  /**
   * 返回tpl,病人费别
   */
  function getCheckOutTpl() {

    var resultTpl = [t1 + '<div class="x-combo-list-item">', '<b>{name}</b>',
        '</div>' + t2].join('');

    return resultTpl;

  }

  function getApportionModeDs() {

    var recordDef = Ext.data.Record.create([{
          name : "id",
          type : "float"
        }, {
          name : "code",
          type : "string"
        }, {
          name : "name",
          type : "string"
        }]);

    var ds = new Ext.data.Store({
          proxy : new Ext.data.HttpProxy({
                url : 'apportionMode/query'
              }),
          reader : new Ext.data.JsonReader({
                root : 'rows',
                totalProperty : 'totalCount',
                id : 'id'
              }, recordDef)
        });
    return ds;
  }

  function getApportionModeTpl() {
    var resultTpl = [t1 + '<div class="x-combo-list-item">',
        '<b>{name}</b><br>', '</div>' + t2].join('');
    return resultTpl;
  }
  // 导入采集归集状态描述说明
  function getRunStatusTbr() {
    var msgbar = new Ext.Toolbar({
          items : [{
                xtype : 'tbtext',
                text : '成功'
              }, {
                xtype : 'box',
                autoEl : {
                  tag : 'img',
                  src : 'resources/img/su1.gif'
                }
              }, '-', {
                xtype : 'tbtext',
                text : '失败'
              }, {
                xtype : 'box',
                autoEl : {
                  tag : 'img',
                  src : 'resources/img/su2.gif'
                }
              }, '-', {
                xtype : 'tbtext',
                text : '验证失败'
              }, {
                xtype : 'box',
                autoEl : {
                  tag : 'img',
                  src : 'resources/img/su4.gif'
                }
              }, '-', {
                xtype : 'tbtext',
                text : '被终止'
              }, {
                xtype : 'box',
                autoEl : {
                  tag : 'img',
                  src : 'resources/img/su9.gif'
                }
              }, '-', {
                xtype : 'tbtext',
                text : '运行'
              }, {
                xtype : 'box',
                autoEl : {
                  tag : 'img',
                  src : 'resources/img/su3.gif'
                }
              }, '-', {
                xtype : 'tbtext',
                text : '等待'
              }, {
                xtype : 'box',
                autoEl : {
                  tag : 'img',
                  src : 'resources/img/su0.gif'
                }
              }

          ]
        });

    return msgbar;
  }

  // 导入采集归集日志分类
  function getRunLogViewSel() {
    var ds = new Ext.data.SimpleStore({
          fields : ['value', 'name'],
          data : [[1, '全部'], [2, '手工'], [3, '自动任务']]
        })
    return ds;
  }

  // 24
  function getHours() {

    var recordDef = Ext.data.Record.create([{
          name : "name",
          type : "string"
        }, {
          name : "value",
          type : "string"
        }]);

    var ds = new Ext.data.Store({
          proxy : new Ext.data.HttpProxy({
                url : 'common/get_hours'
              }),
          reader : new Ext.data.JsonReader({
                root : 'rows',
                totalProperty : 'totalCount',
                id : 'name'
              }, recordDef)
        });

    return ds;
  }

  function getHoursLocal() {
    var da = [];
    var o = []
    // 0-23
    for (var i = 0; i < 24; i++) {
      o = [i, i + "时"]
      da.push(o);
    }
    var comboStore = new Ext.data.ArrayStore({
          fields : ['value', 'name'],
          data : da
        });
    return comboStore;
  }

  // 60
  function getMins() {
    var recordDef = Ext.data.Record.create([{
          name : "name",
          type : "string"
        }, {
          name : "value",
          type : "string"
        }]);
    var ds = new Ext.data.Store({
          proxy : new Ext.data.HttpProxy({
                url : 'common/get_mins'
              }),
          reader : new Ext.data.JsonReader({
                root : 'rows',
                totalProperty : 'totalCount',
                id : 'name'
              }, recordDef)
        });

    return ds;

  }

  function getMinsLocal() {
    var da = [];
    var o = []
    // 0- 59
    for (var i = 0; i < 60; i++) {
      o = [i, i + "分"]
      da.push(o);
    }
    var comboStore = new Ext.data.ArrayStore({
          fields : ['value', 'name'],
          data : da
        });
    return comboStore;

  }

  // 界面显示用，取参数设置中导入前几天的数据
  function getAutoRunBefDays() {
    var recordDef = Ext.data.Record.create([{
          name : "name",
          type : "string"
        }, {
          name : "value",
          type : "string"
        }]);
    var ds = new Ext.data.Store({
          proxy : new Ext.data.HttpProxy({
                url : 'cron/get_autorun_bef_days'
              }),
          reader : new Ext.data.JsonReader({
                root : 'rows',
                totalProperty : 'totalCount',
                id : 'name'
              }, recordDef)
        });
    return ds;
  }

  /**
   * Custom function used for column renderer
   *
   * @param {Object}
   *          val
   */
  function changeRowColor(val) {
    if (val > 0) {
      return '<span style="color:green;">' + val + '</span>';
    } else if (val < 0) {
      return '<span style="color:red;">' + val + '</span>';
    }
    return val;
  }

  /**
   * Custom function used for column renderer
   *
   * @param {Object}
   *          val
   */
  function pctChangeRowColor(val) {
    if (val > 0) {
      return '<span style="color:green;">' + val + '%</span>';
    } else if (val < 0) {
      return '<span style="color:red;">' + val + '%</span>';
    }
    return val;
  }

  // 取全部的系统变量
  function defaultValues() {
    var results = []
    Ext.Ajax.request({
          url : "appsetup/all_setupinfo",
          method : 'POST',
          async : false,

          callback : function(o, s, r) {
            var resp = Ext.decode(r.responseText);
            if (resp.success) {
              results = resp.data;
            }
          }
        });

    return results;
  }

  function getWindowHeight() {
    var height = document.body.clientHeight * WindowSize.heightScala;
    return height;
  }

  function getWindowWidth() {
    var width = document.body.clientWidth * WindowSize.widthScala;
    return width;
  }
  /** *********** public method ************ */
  return {
    /**
     * 初始化工具类函数
     */
    init : function() {

    },

    getOrgDs : function() {
      return getOrgDs();
    },
    getUserOrgDs : function() {
      return getUserOrgDs();
    },
    getOrgTpl : function() {
      return getOrgTpl();
    },
    getIncomeDs : function() {
      return getIncomeDs();
    },
    getIncomeTpl : function() {
      return getIncomeTpl();
    },
    getCostDs : function() {
      return getCostDs();
    },
    getCostTpl : function() {
      return getCostTpl();
    },
    getOrgTypeDs : function() {
      return getOrgTypeDs();
    },
    getOrgTypeTpl : function() {
      return getOrgTypeTpl();
    },
    getCheckOutDs : function() {
      return getCheckOutDs();
    },
    getCheckOutTpl : function() {
      return getCheckOutTpl();
    },
    getRunStatusTbr : function() {
      return getRunStatusTbr();
    },
    getRunLogViewSel : function() {
      return getRunLogViewSel();
    },
    getHours : function() {
      return getHours();
    },
    getHoursLocal : function() {
      return getHoursLocal();
    },
    getMins : function() {
      return getMins();
    },
    getMinsLocal : function() {
      return getMinsLocal();
    },
    getAutoRunBefDays : function() {
      return getAutoRunBefDays();
    },
    pctChangeRowColor : function(val) {
      return pctChangeRowColor(val);
    },
    changeRowColor : function(val) {
      return changeRowColor(val);
    },
    getApportionModeDs : function() {
      return getApportionModeDs();
    },
    getApportionModeTpl : function() {
      return getApportionModeTpl();
    },
    getWindowHeight : function() {
      return getWindowHeight();
    },
    getWindowWidth : function() {
      return getWindowWidth();
    },
    asMoney : function(v) {
      v = (Math.round((v - 0) * 100)) / 100;
      v = (v == Math.floor(v)) ? v + ".00" : ((v * 10 == Math.floor(v * 10))
          ? v + "0"
          : v);
      v = String(v);
      var ps = v.split('.'), whole = ps[0], sub = ps[1] ? '.' + ps[1] : '.00', r = /(\d+)(\d{3})/;
      while (r.test(whole)) {
        whole = whole.replace(r, '$1' + ',' + '$2');
      }
      v = whole + sub;
      // if (v.charAt(0) == '-') {
      // return '-$' + v.substr(1);
      // }
      // return "$" + v;

      return v;
    },
    asInt : function(v) {
      v = (Math.round((v - 0) * 100)) / 100;

      return v;
    },
    // grid 数字显示
    asNum : function num(val) {
      if (val > 0) {
        return '<span style="color:green;">' + val + '</span>';
      } else if (val < 0) {
        return '<span style="color:red;">' + val + '</span>';
      }

      return val;

    },

    getAlldefaultValues : function() {
      return defaultValues();
    },

    // 取系统参数
    // var a = Divo.com.Lib.getDefautlValue("user_Hospital.Info");
    // alert(a.setupValue);
    getDefautlValue : function(setupCode) {
      var data = defaultValues();
      var one = {};
      for (var index = 0; index < data.length; index++) {
        one = data[index];
        if (one.setupCode == setupCode) {
          break;
        }
      }

      return one;
    },

    getSysConfig : function() {
      var sysConfig;

      Ext.Ajax.request({
            url : "common/getsystemconfig",
            scope : this,
            async : false,
            callback : function(o, s, r) {
              var resp = Ext.decode(r.responseText);
              sysConfig = {
                user_name : resp.user_name,
                application_name : resp.application_name,
                vendor : resp.vendor,
                copyright : resp.copyright,
                run_mode : resp.run_mode,
                version : resp.version,
                website : resp.website,
                buildId : resp.buildId,
                module_name : resp.resource.text,
                module_id : resp.resource.resourceId,
                current_user : resp.current_user
              }
            }
          });
      return sysConfig;
    },

    // 参考奖金系统 Divo.bonus.getGroupingHeaderGridMeta
    getGroupingHeaderGridMeta : function(result, rowsResult) {
      var meta = {};
      meta.findColumn = function(name) {
        for (var i = 0; i < this.cms.length; i++) {
          if (this.cms[i].dataIndex === name) {
            return i
          }
        }
        return null
      };
      meta.findMetaIndex = function(name) {
        for (var i = 0; i < this.metas.length; i++) {
          if (this.metas[i].name === name) {
            return i
          }
        }
        return null
      };
      if (!result) {
        meta.recordDef = Ext.data.Record.create([{
              name : ""
            }]);
        meta.columnModel = new Ext.grid.ColumnModel([{
              header : ""
            }]);
        return meta
      }
      var rs = [], cms = [], fs = [], metas = [], rows = [], row = [];

      for (var i = 0; i < result.length; i++) {
        metas.push({
              name : result[i].dataIndex,
              header : result[i].header
            });
        rs.push({
              name : result[i].dataIndex
            });
        if (result[i].header) {
          cms.push({
                header : result[i].header,
                width : result[i].width,
                hidden : result[i].hidden,
                sortable : result[i].sortable,
                resizable : result[i].resizable,
                align : result[i].align,
                dataIndex : result[i].dataIndex
              });

        }
      }

      if (rowsResult) {
        for (var i = 0; i < rowsResult.length; i++) {
          rows.push({
                header : rowsResult[i].header,
                colspan : rowsResult[i].colspan,
                align : rowsResult[i].align,
                width : rowsResult[i].width
              });
        }
      }

      row.push(rows);

      meta.recordDef = Ext.data.Record.create(rs);
      meta.columnModel = new Ext.grid.ColumnModel(cms);

      meta.columnModel.rows = row;

      meta.columnModel.defaultSortable = false;
      meta.rs = rs;
      meta.cms = cms;
      meta.metas = metas;
      return meta
    },

    getSearchIcon : function() {
      return "resources/img/filter.gif";
    },

    getCollapseallIcon : function() {
      return "resources/img/collapseall.gif";
    },

    getExpandallIcon : function() {
      return "resources/img/expandall.gif";
    },

    getNewIcon : function() {
      return "resources/img/g_rec_new.png";
    },

    getEditIcon : function() {
      return "resources/img/g_rec_upd.png";
    },

    getPublishIcon : function() {
      return "resources/img/fam.icons/application_go.png";
    },

    getDelIcon : function() {
      return "resources/img/g_rec_del.png";
    },

    /**
     * 不显示网格列标题
     */
    hideGridHeader : function(grid) {
      grid.getView().el.select('.x-grid3-header').setStyle('display',
          'none');
    }

  }
}();

Divo.com.Lib.init();
