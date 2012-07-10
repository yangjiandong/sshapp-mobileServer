/**
 * 系统参数
 */
Ext.namespace('Divo');
Ext.namespace('Divo.app');

Divo.app.AppSetupList = function() {
  /* ----------------------- private属性 ----------------------- */
  var gridEl = 'appsetup-grid';
  var grid, selModel, toolbar;
  var curId;
  var qtipTpl = new Ext.XTemplate('<tpl for=\".\">',
      '<div>{descrip}</div></tpl>');

  /* ----------------------- private方法 ----------------------- */
  // 创建列表
  function createGrid() {

    var f = function(value, metaData, record, rowIndex,
                    colIndex, store) {
                  if (value.length > 18) {
                    value = Ext.util.Format.ellipsis(value, 18);
                  }
                  return value;
                };

    grid = new Ext.grid.EditorGridPanel({
          store : new Ext.data.GroupingStore({
                proxy : new Ext.data.HttpProxy({
                      url : 'appsetup/querysetupinfo'
                    }),
                reader : new Ext.data.JsonReader({
                      root : 'rows',
                      totalProperty : 'totalCount',
                      id : 'setupCode'
                    }, Ext.data.Record.create([{
                          name : 'setupCode',
                          type : 'string'
                        }, {
                          name : 'setupValue',
                          type : 'string'
                        }, {
                          name : 'descrip',
                          type : 'string'
                        }, {
                          name : 'type',
                          type : 'string'
                        }])),
                sortInfo : {
                  field : 'setupCode',
                  direction : "DESC"
                },
                groupField : 'type'
              }),

          columns : [{
                header : "参数编码",
                width : 220,
                sortable : true,
                dataIndex : 'setupCode'
              }, {
                header : "参数值",
                width : 180,
                sortable : true,
                dataIndex : 'setupValue'
                //,
                //renderer : f
              }, {
                header : "描述",
                width : 400,
                sortable : true,
                dataIndex : 'descrip'
                //,
                //renderer : f
              }, {
                header : "参数类型",
                width : 80,
                sortable : true,
                dataIndex : 'type'
              }],

          view : new Ext.grid.GroupingView({
                forceFit : false,
                groupTextTpl : '{text} ({[values.rs.length]} 条)'
              }),

          frame : true,
          width : 700,
          height : 450,
          collapsible : false,
          animCollapse : false,
          title : '系统参数列表',
          listeners : {
            mouseover : function(e) {
              var t = e.getTarget();
              var row = this.view.findRowIndex(t);
              var cell = this.view.findCellIndex(t);
              var descripCol = this.getColumnModel().findColumnIndex('descrip');
              var setupValueCol = this.getColumnModel()
                  .findColumnIndex('setupValue');
              if (!(row === false) && !(cell === false)
                  && (cell == descripCol || cell == setupValueCol)) {
                var rec = this.store.getAt(row);
                if (!Ext.isEmpty(rec.data.descrip)
                    || !Ext.isEmpty(rec.data.setupValue)) {
                  var qtip = qtipTpl.apply({
                        descrip : cell == descripCol
                            ? rec.data.descrip
                            : rec.data.setupValue
                      });
                  Ext.QuickTips.register({
                        text : qtip,
                        target : e.target
                      });
                }

              }
            },
            mouseout : function(e) {
              Ext.QuickTips.unregister(e.target);
            }
          }
        });

    selModel = grid.getSelectionModel();
    selModel.on('rowselect', onRowSelect);

    grid.on('validateedit', function(e) {
          var curRecord = e.record;
          var field = e.field;
          var value = e.value;

          curRecord.beginEdit();
          curRecord.set(field, value);

          Ext.Ajax.request({
                url : "appsetup/savesetupinfo",
                method : 'POST',
                scope : this,
                async : false,
                params : {
                  jsonStr : Ext.encode(curRecord.data)
                },
                callback : function(o, s, r) {

                  curRecord.commit();

                  var resp = Ext.decode(r.responseText);
                  if (resp.success) {
                    curRecord.commit();
                  } else {
                    curRecord.reject();
                    e.cancel = true;
                    curRecord.cancelEdit();
                  }
                }
              });
        });
  }

  // 选择某行
  function onRowSelect(selModel, rowIndex, record) {
    curId = record.data.oid;
  }

  // 设置列的编辑效果
  function setGridEditable() {
    var cm = grid.getColumnModel();

    var colSetupValue = cm.findColumnIndex('setupValue');
    cm.setEditor(colSetupValue, new Ext.form.TextField({

        }));

    var colDesctip = cm.findColumnIndex('descrip');
    cm.setEditor(colDesctip, new Ext.form.TextField({

        }));
  }

  // 设置列的显示效果
  function setColumnStyle() {

  }

  // 创建layout
  function createLayout() {
    toolbar = new Ext.Toolbar({
          items : [{
                xtype : "button",
                cls : "x-btn-icon-text",
                icon : "resources/img/g_rec_new.png",
                tooltip : "",
                text : "导入EXCEL",
                id : "tlb_NEW",
                handler : createNewRecord
              }, {
                xtype : "button",
                cls : "x-btn-icon-text",
                icon : "resources/img/g_rec_upd.png",
                tooltip : "",
                text : "导出EXCEL",
                id : "tlb_EDIT",
                handler : toggleEdit
              }, '->', '<img src="resources/img/search.gif"/>', {
                xtype : "textfield",
                id : 'tlb_FILTER_TEXT',
                width : 180,
                selectOnFocus : true,
                emptyText : '输入关键字并按回车键',
                listeners : {
                  "specialkey" : function(o, e) {
                    if (e.getKey() === e.ENTER) {
                      // grid.executeQueryBy('keyword', o.getValue());
                      grid.getStore().baseParams.keyword = o.getValue();
                      grid.getStore().reload();
                    }
                  }
                }
              }]
        });

    var viewport = new Ext.Viewport({
          layout : 'border',
          items : [{
                layout : 'fit',
                region : 'center',
                tbar : toolbar,
                border : false,
                items : [grid]
              }]
        });

    viewport.doLayout();
  }

  // 创建新的记录，初始化表单
  function createNewRecord() {

  }

  // 切换记录编辑状态
  function toggleEdit() {

  }

  /* ----------------------- public方法 ----------------------- */
  return {
    /**
     * 初始化
     */
    init : function() {
      createGrid();
      createLayout();
      grid.getStore().load();
      setGridEditable();
      setColumnStyle();
    }
  }
}();

Ext.onReady(Divo.app.AppSetupList.init, Divo.app.AppSetupList, true);
