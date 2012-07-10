/**
 * 网格类: 要求从控制器中获得json格式的数据
 *
 * 必备配置项
 *
 * @config {String} gridId 网格对应的div标识
 * @config {String} entityName 网格数据所对应的Entity类名字
 *
 * 可选配置项 *
 * @config {bool} hasBbar 是否显示状态条（默认为true，表示显示状态条）
 * @config {int} pageSize 分页长度（默认20）
 * @config {bool} showTopToolbar 是否显示顶部的工具条（默认为false，表示不显示）
 * @config {Object} expander 网格的Meta附加控件,比如复选框或自动编号等(默认为null)
 * @config {String} jsonId json数据中的id字段名（默认为'id')
 * @config {bool} singleSelect 是否只允许单选(默认为true,表示单选)
 * @config {bool} checkboxSelectionModel(默认为false,表示不用此模式)
 * @config {int} queryPanelColCount 过滤查询表单的列数(默认为2)
 * @config {String} metaUrl 取网格元数据的请求地址(默认为'get_system_gridmeta.htm')
 * @config {String} queryUrl 获取Grid数据的URL地址
 * @config {String} deleteUrl 删除Grid数据的URL地址
 * @config {String[]} specialFields 指定显示的字段，默认为空
 * @config {bool} useCacheView 是否用缓存，大数据量时有效，默认不采用,采用时会出现上下拖动白板现象
 */
Ext.namespace('Divo');
Ext.namespace('Divo.Base');

Divo.Base.GridView = Ext.extend(Ext.grid.GridPanel, {
  gridId : 'base-grid',
  jsonId : 'oid',
  region : 'center',
  pageSize : App.defaultPage,
  hasBbar : true,
  expander : null,
  singleSelect : true,
  remoteSort : false,
  checkboxSelectionModel : false,
  entityName : '',
  packageName : '',
  entityClassName : '',
  metaUrl : 'core/get_grid_meta',
  queryUrl : '',
  deleteUrl : '',
  queryPanelColCount : 3,
  showTopToolbar : false,
  recordPk : new Array(),
  queryFlds : new Array(),
  searchFlds : new Ext.util.MixedCollection(),
  queryLabelWidth : 100,
  autoFit : true,
  recDef : null,
  autoSelectFirstRow : true,
  specialFields : [],
  useCacheView : false,
  // remoteSortInfo:{},

  initComponent : function() {

    if (this.queryUrl == null || this.queryUrl == '') {
      this.queryUrl = this.entityName + '/query';
    }

    var metaData = null;
    // 此处需要extjs basex保证运行完成
    Ext.Ajax.request({
          url : this.metaUrl,
          scope : this,
          async : false,
          params : {
            entityName : this.entityName,
            packageName : this.packageName,
            entityClassName : this.entityClassName
          },
          callback : function(o, s, r) {
            var resp = Ext.decode(r.responseText);
            if (resp.success) {
              metaData = resp.data;
            }
          }
        });

      //ext3不支持， 对象不支持“getConnectionObject”属性或方法
//    var conn = Ext.lib.Ajax.getConnectionObject().conn;
//    conn.open("GET", this.metaUr + "?entityName=" + this.entityName
//            + "&packageName=" + this.packageName + "&entityClassName="
//            + this.entityClassName, false);
//    conn.send(null);
//    // alert(conn.responseText);
//    var resp = Ext.decode(conn.responseText);
//    if (resp.success) {
//      metaData = resp.data;
//    }

    if (metaData != null) {
      var sm;
      if (this.checkboxSelectionModel) {
        sm = new Ext.grid.CheckboxSelectionModel();
      } else {
        sm = new Ext.grid.RowSelectionModel({
              singleSelect : this.singleSelect
            });
      }

      this.getBaseGridMeta(metaData, sm);
      var store = this.getDataStore();

      var bbar;
      if (this.bbar) {
        bbar = this.bbar;
      } else {
        if (this.hasBbar) {
          bbar = new Ext.PagingToolbar({
                pageSize : this.pageSize,
                store : store,
                displayInfo : true,
                displayMsg : '显示: {0} - {1} 共 {2} 条',
                emptyMsg : "没有数据"
              })
        } else {
          bbar = null;
        }
      }

      var vView;
      if (this.useCacheView) {
        vView = new Ext.ux.grid.BufferView;
      } else {
        vView = this.getView();
      }

      Ext.apply(this, {
            id : 'grid' + this.gridId,
            sm : sm,
            colModel : this.colModel,
            store : store,
            autoScroll : true,
            enableColumnHide : false,
            bbar : bbar,
            tbar : this.tbar || this.getGridToolBar(),
            loadMask : {
              msg : "正在加载数据..."
            },
            viewConfig : this.viewConfig || {
              forceFit : this.autoFit
            },

            view : vView
          });

    }

    // this.on('sortchange', function(grid, sortinfo) {
    // if (this.remoteSort) {
    // this.remoteSortInfo = sortinfo;
    //
    // var colModel = grid.getColumnModel();
    // // var s = sortinfo;
    //
    // var currentField = this.remoteSortInfo.field;
    // //var dir = remoteSortInfo.direction;
    // var column = colModel.getColumnById(currentField);
    // var sortByField = column.sortByField;
    // if (sortByField != undefined && sortByField != '') {
    // this.remoteSortInfo.field = sortByField
    // }
    // //sortinfo.field = "itemCode";
    // }
    // }, this);

    Divo.Base.GridView.superclass.initComponent.apply(this, arguments);

  },
  render : function() {
    Divo.Base.GridView.superclass.render.apply(this, arguments);
    if (this.getTopToolbar()) {
      if (this.showTopToolbar)
        this.getTopToolbar().show();
      else
        this.getTopToolbar().hide();
    }

    this.doLayout();
  },
  initEvents : function() {
    Divo.Base.GridView.superclass.initEvents.call(this);
    this.getStore().on('load', this.onLoadStore, this);
  },
  getBaseGridMeta : function(result, sm) {
    if (result.length == 0) {
      this.recDef = Ext.data.Record.create([{
            name : ""
          }]);
      this.colModel = new Ext.grid.ColumnModel([{
            header : ""
          }]);
    } else {
      var rs = [], cms = [];
      if (this.expander) {
        cms.push(this.expander);
      }

      if (this.checkboxSelectionModel && sm) {
        cms.push(sm);
      }

      for (var i = 0; i < result.length; i++) {

        rs.push({
              name : result[i].name,
              type : result[i].type
            });

        if (result[i].hidden || result[i].header == '') {
          continue;
        } else {
          // 默认为空则全部加入，如果指定了字段则加入，否则不加入
          if (this.specialFields.length == 0
              || this.specialFields.indexOf(result[i].name) != -1) {
            cms.push({
                  id : result[i].name,
                  header : result[i].header,
                  dataIndex : result[i].name,
                  type : result[i].type,
                  sortable : result[i].sortable,
                  fixed : result[i].fixed,
                  width : result[i].width,
                  hidden : result[i].hidden || result[i].header == '',
                  align : result[i].align,
                  sortByField : result[i].sortByField,
                  isMoney : result[i].money
                });
          }
        }
      }

      this.recDef = Ext.data.Record.create(rs);
      this.colModel = new Ext.grid.ColumnModel(cms);

      for (i = 0; i < this.colModel.getColumnCount(); i++) {

        if (this.colModel.config[i].isMoney) {
          this.colModel.config[i].align = 'right';
          this.colModel.setRenderer(i, function(val, params, record) {
                return Divo.com.Lib.asMoney(val);
              })
        }

      }
    }
  },
  getGridToolBar : function() {
    if (this.queryFlds.length == 0) {
      return null;
    } else {
      var toolbar = new Ext.Panel({
            autoScroll : true,
            border : true,
            bodyBorder : false,
            frame : true,
            layout : 'table',
            layoutConfig : {
              columns : this.queryPanelColCount
            },
            defaults : {
              labelWidth : 90,
              labelAlign : 'right'
            },
            bodyStyle : 'padding-top:5px;padding-bottom:5px;',
            items : [{
                  html : '<p style="font-size:12px;">过滤条件:</p>',
                  bodyStyle : 'border:0;padding-left:5px;',
                  colspan : this.queryPanelColCount
                }]
          });

      for (var i = 1; i <= this.queryPanelColCount; i++) {
        toolbar.add({
              layout : 'form',
              labelAlign : 'right',
              bodyStyle : 'border:0;',
              labelWidth : this.queryLabelWidth,
              items : this.getQueryFieldsForPanelCol(i)
            });
      }

      return toolbar;
    }
  },
  getQueryFieldsForPanelCol : function(colNr) {
    var idxStart, idxStop;
    var colArr = new Array();
    var vqfLen = this.queryFlds.length;
    var mod = vqfLen % this.queryPanelColCount;
    var colSize = Math.floor(vqfLen / this.queryPanelColCount);
    colSize = (mod > 0 && colNr <= mod) ? (colSize + 1) : colSize;

    idxStart = Math.floor(vqfLen / this.queryPanelColCount) * (colNr - 1);

    if (mod > 0) {
      idxStart += (colNr > mod) ? mod : (colNr - 1);
    }
    idxStop = idxStart + colSize;
    idxStop = (idxStop < vqfLen) ? idxStop : vqfLen;
    if (vqfLen % this.queryPanelColCount != 0) {
      if (i < vqfLen % this.queryPanelColCount) {
      }
    }
    for (var i = idxStart; i < idxStop; i++) {
      var filterFld = this.searchFlds.get(this.queryFlds[i]);
      filterFld.on('specialkey', this.onEnter.createDelegate(this), this);
      colArr[colArr.length] = filterFld;
    }

    return colArr;
  },
  onEnter : function(o, e) {
    if (e.getKey() === e.ENTER) {
      this.executeQuery();
      e.stopEvent();
    }
  },
  getDataStore : function() {
    var datastore = new Ext.data.Store({
          proxy : new Ext.data.HttpProxy({
                url : this.queryUrl
              }),
          reader : new Ext.data.JsonReader({
                root : 'rows',
                totalProperty : 'totalCount',
                id : this.jsonId || 'oid'
              }, this.recDef),
          remoteSort : this.remoteSort
        });

    // datastore.on('beforeload', function(store, o) {
    // if (this.remoteSort) {
    // var currentField = store.sortInfo.field;
    //
    // var column = this.colModel.getColumnById(currentField);
    // var sortByField = column.sortByField;
    // if (sortByField != undefined && sortByField != '') {
    // store.baseParams.sort = sortByField;
    // }
    // }
    // }, this);

    return datastore;
  },

  onLoadStore : function() {

    if (this.autoSelectFirstRow && this.store.getCount() > 0) {
      this.getSelectionModel().selectFirstRow();
      this.getView().focusRow(0);
    }
  },
  selectRecord : function(cId) {
    if (this.store.getTotalCount() > 0) {
      if (cId) {
        var rowId = this.store.indexOfId(cId);
        this.getSelectionModel().selectRow(rowId);
        this.getView().focusRow(rowId);
      } else {
        this.getSelectionModel().selectFirstRow();
        this.getView().focusRow(0);
      }
    }
  },
  load : function() {
    this.executeQuery();
  },
  /**
   * 快速查询，需传递主键名和主键的值
   */
  executeQueryBy : function(keyField, keyValue) {
    var p = new Object();
    p[keyField] = keyValue;
    this.store.baseParams = p;

    if (this.hasBbar) {
      this.store.load({
            callback : this.afterExecuteQuery,
            scope : this,
            params : {
              start : 0,
              limit : this.pageSize
            }
          });
    } else {
      this.store.load({
            callback : this.afterExecuteQuery,
            scope : this
          });
    }
  },
  queryByBaseParams : function(baseParams) {
    this.store.baseParams = baseParams;

    if (this.hasBbar) {
      this.store.load({
            callback : this.afterExecuteQuery,
            scope : this,
            params : {
              start : 0,
              limit : this.pageSize
            }
          });
    } else {
      this.store.load({
            callback : this.afterExecuteQuery,
            scope : this
          });
    }
  },
  executeQuery : function() {
    var p = new Object();
    var qf = this.searchFlds;
    for (var i = 0, len = qf.keys.length; i < len; i++) {
      var fld = qf.items[i];
      if (fld.getXType() == 'datefield' || fld.getXType() == 'combo') {
        p[qf.keys[i]] = fld.getRawValue();
      } else {
        p[qf.keys[i]] = fld.getValue();
      }

    }
    this.store.baseParams = p;

    if (this.hasBbar) {
      this.store.load({
            callback : this.afterExecuteQuery,
            scope : this,
            params : {
              start : 0,
              limit : this.pageSize
            }
          });
    } else {
      this.store.load({
            callback : this.afterExecuteQuery,
            scope : this
          });
    }
  },
  afterExecuteQuery : function(r, options, success) {
    if (success) {
      if (this.autoSelectFirstRow && this.store.getCount() > 0) {
        this.getSelectionModel().selectFirstRow();
        this.getView().focusRow(0);
      }
    }
  },
  goToPrevRecord : function() {
    if (this.getStore().getCount() == 1) {
      this.reSelectCurrent();
    } else {
      this.getSelectionModel().selectPrevious(false);
    }

  },
  goToNextRecord : function() {
    if (this.getStore().getCount() == 1) {
      this.reSelectCurrent();
    } else {
      this.getSelectionModel().selectNext(false);
    }

  },
  reSelectCurrent : function() {
    var crt = this.getSelectionModel().getSelected();
    this.getSelectionModel().clearSelections();
    this.getSelectionModel().selectRecords([crt]);
  },
  reloadRecords : function() {
    this.getStore().removeAll();
    this.getStore().reload();
  },
  deleteRecord : function() {
    if (this.getSelectionModel().getCount() < 1) {
      Ext.Msg.show({
            title : '',
            msg : '没有选择数据，无法删除。',
            buttons : Ext.Msg.OK,
            fn : this.execute_delete,
            icon : Ext.MessageBox.WARNING
          });
    } else {
      Ext.Msg.show({
            title : '删除确认',
            msg : '真的要删除这条数据吗?',
            buttons : Ext.Msg.YESNO,
            fn : this.execute_delete,
            scope : this,
            icon : Ext.MessageBox.QUESTION
          });
    }
  },
  execute_delete : function(btn) {
    if (btn == 'yes') {
      if (this.deleteUrl == null || this.deleteUrl == '') {
        return;
      }

      Ext.Ajax.request({
            url : this.deleteUrl,
            success : this.after_execute_delete_success,
            scope : this,
            params : this.getSelectedRowPK()
          });
    }
  },
  after_execute_delete_success : function(response, options) {
    var resp = Ext.decode(response.responseText);
    if (resp.success) {
      var removed = this.getSelectionModel().getSelected();
      if (this.getSelectionModel().hasNext()) {
        this.getSelectionModel().selectNext();
      } else if (this.getSelectionModel().hasPrevious()) {
        this.getSelectionModel().selectPrevious();
      }
      this.getStore().remove(removed);
      this.getStore().commitChanges();
    } else {
      if (resp.message) {
        Ext.Msg.alert('错误', resp.message);
      } else {
        Ext.Msg.alert('错误', '删除时服务器端发生错误！');
      }
    }

  },
  getSelectedRowPK : function() {
    var pk = new Object();
    for (var i = 0; i < this.recordPk.length; i++) {
      pk[this.recordPk[i]] = this.getSelectionModel().getSelected()
          .get(this.recordPk[i]);
    }
    return pk;
  },
  toggleFilterBar : function() {
    if (this.getTopToolbar() == null) {
      return;
    } else {
      if (this.getTopToolbar().hidden)
        this.getTopToolbar().show();
      else
        this.getTopToolbar().hide();

      this.doLayout();
    }
  },
  // 用于打印,生成HTML文档
  exportHtml : function() {
    this.exportList("html");
  },
  // 导出到Excle文档
  exportExcel : function() {
    this.exportList("xls");
  },
  exportList : function(p_format) {

    // 过滤条件
    var qs = '';
    var qf = this.searchFlds;
    for (var i = 0, len = qf.keys.length; i < len; i++) {
      if (qf.items[i].getValue() != undefined)
        qs = qs + '&' + qf.keys[i] + '=' + qf.items[i].getValue();
    }

    // 当前显示的字段
    var cs = '&fieldsList=';
    var f = [];
    for (var i = 0; i < this.getColumnModel().getColumnCount(); i++) {
      if (!this.getColumnModel().isHidden(i)) {
        f.push(this.getColumnModel().getDataIndex(i));
      }
    }
    cs = cs + '' + f.join('-');

    // 当前排序字段以及排序顺序(asc,desc)
    var ss = ''; // 此处仅考虑排序，不涉及分组的问题
    if (this.getStore().getSortState()) {
      ss = '&sort=' + this.getStore().getSortState().field + '&dir='
          + this.getStore().getSortState().direction;
    }

    var v = window
        .open(this.queryUrl + "?export=y&_p_exp_format="
                + ((p_format) ? p_format : "xls") + qs + cs + ss, 'Export',
            'adress=no,width=710,height=450,scrollbars=yes,resizable=yes,menubar=yes');
    v.focus();
  }
});

Ext.reg('Divo.Base.GridView', Divo.Base.GridView);
