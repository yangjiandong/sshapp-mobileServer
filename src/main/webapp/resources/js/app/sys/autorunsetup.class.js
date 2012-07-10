/**
 * 医院名称管理
 */
Ext.namespace('Divo');
Ext.namespace('Divo.app');

Divo.app.AutoRunSetupClass = function(config) {
  var typeCode = config.typeCode;
  /* ----------------------- private属性 ----------------------- */
  var dbo = 'ItemSource';
  var gridEl = 'itemsource-grid';
  var grid, formPanel, selModel, toolbar;
  var cId = 0, cronId = 0;
  var fldExpression;
  var win;

  /* ----------------------- private方法 ----------------------- */
  // 创建列表
  function createGrid() {
    var queryFlds = new Array();
    var searchFlds = new Ext.util.MixedCollection();
    queryFlds.push('query');

    var fldName = new Ext.form.TextField({
          name : 'query',
          id : gridEl + '-query',
          fieldLabel : '搜索',
          allowBlank : true,
          width : 100
        });
    searchFlds.add('query', fldName);

    grid = new Divo.Base.GridView({
          gridId : gridEl,
          entityName : dbo,
          queryFlds : queryFlds,
          searchFlds : searchFlds,
          cm : new Ext.grid.ColumnModel([{
                header : '指标名称',
                dataIndex : 'itemName',
                width : 200
              }, {
                header : '存储过程',
                dataIndex : 'spName',
                width : 300
              }, {
                header : '参数',
                dataIndex : 'itemId',
                width : 200
              }, {
                header : '执行方式',
                dataIndex : 'expression',
                width : 300
              }]),
          packageName : 'pm.mob',
          queryUrl : 'autorun_setup/query',
          jsonId : 'id',
          recordPk : ["id"]
        });

    grid.getStore().on('beforeload', function(store, options) {
          store.baseParams.typeCode = typeCode;
        }, this, true);

    selModel = grid.getSelectionModel();
  }

  // 创建layout
  function createLayout() {
    toolbar = new Ext.Toolbar({
          items : [{
                icon : "resources/img/g_rec_new.png",
                text : "设置执行方式",
                id : "tlb_NEW",
                handler : function() {
                  var records = selModel.getSelections();
                  var data = records[0].data;
                  createWindows();
                  cId = data.id;
                  cronId = data.cronId;
                  fldExpression.setValue(data.expression);
                }
              }]
        });

    var viewport = new Ext.Viewport({
          layout : 'border',
          items : [{
                layout : 'fit',
                region : 'north',
                border : false,
                items : [toolbar],
                height : 25
              }, {
                layout : 'fit',
                region : 'center',
                border : false,
                items : [grid]
              }]
        });

    viewport.doLayout();
  }

  function createForm() {

    if (formPanel) {
      return;
    }

    var t1 = '<tpl for=\".\">';
    var t2 = '</tpl>';
    var recordDef = Ext.data.Record.create([{
          name : "id",
          type : "float"
        }, {
          name : "expression",
          type : "string"
        }]);

    var ds = new Ext.data.Store({
          proxy : new Ext.data.HttpProxy({
                url : 'autorun_setup/get_cron'
              }),
          reader : new Ext.data.JsonReader({
                root : 'rows',
                totalProperty : 'totalCount',
                id : 'id'
              }, recordDef)
        });

    var resultTpl = [t1 + '<div class="x-combo-list-item">',
        '<b>{expression}</b><br>', '</div>' + t2].join('');

    fldExpression = new Ext.form.ComboBox({
          fieldLabel : '执行方式',
          name : 'expression',
          store : ds,
          displayField : 'expression',
          dataIndex : 'expression',
          mode : 'remote',
          typeAhead : false,
          forceSelection : true,
          loadingText : '正在搜索...',
          listWidth : 220,
          width : 150,
          pageSize : 10,
          minChars : 0,
          hideTrigger : false,
          allowBlank : false,
          tpl : resultTpl,
          onSelect : function(record) {
            var data = record.data;
            this.setValue(data.expression);
            cronId = data.id;
            this.onTriggerClick();
          }
        });

    formPanel = new Ext.FormPanel({
          border : false,
          width : 400,
          labelPad : 5,
          bodyStyle : 'padding: 20px 50px',
          frame : false,
          region : 'center',
          items : [fldExpression]
        });

  }

  function createWindows() {
    if (win) {
      win.show();
      return;
    }

    win = new Ext.Window({
          title : '设置执行方式',
          iconCls : 'icon-win',
          autoHeight : true,
          width : 415,
          layout : 'form',
          plain : false,
          buttonAlign : 'right',
          closable : false,
          modal : true,
          items : [formPanel]
        });

    var okHideBtn = win.addButton({
          text : '保存'
        }, onSubmit, this);

    var cancelBtn = win.addButton({
          text : '取消'
        }, onWinHide, this);

    win.show();
  }

  function onWinHide() {
    win.hide();
  }

  // 表单提交
  function onSubmit() {

    if (cId == 0) {
      return;
    }

    Ext.Ajax.request({
          params : {
            "id" : cId,
            "cronId" : cronId
          },
          method : "POST",
          url : "autorun_setup/save_cron",
          callback : function(o, s, r) {
            var respText = Ext.decode(r.responseText);
            if (respText.success) {
              grid.getStore().reload();
            } else {
              if (respText.message.length > 0)
                Ext.Msg.alert('错误', respText.message);

            }
          }
        });
    onWinHide();
  }

  /* ----------------------- public方法 ----------------------- */
  return {
    /**
     * 初始化
     */
    init : function() {
      createGrid();
      createForm();
      createLayout();
      grid.getStore().load();
    }
  }
};
