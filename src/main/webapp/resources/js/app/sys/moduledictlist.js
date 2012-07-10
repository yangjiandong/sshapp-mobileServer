/**
 * 模块授权
 */
Ext.namespace('Divo');
Ext.namespace('Divo.app');
Ext.QuickTips.init();
Ext.BLANK_IMAGE_URL = 'resources/img/s.gif';

Divo.app.ModuleDictList = function() {
  /* ----------------------- private属性 ----------------------- */
  var dbo1 = 'Role';
  var dbo2 = 'ModuleDict';
  var gridEl1 = 'moduledict-role-grid';
  var gridEl2 = 'moduledict-grid';
  var grid1, grid2;
  var selModel1, selModel2;
  var roleId;

  /* ----------------------- private方法 ----------------------- */
  // 创建列表
  function createGrid() {
    var queryFlds = new Array();
    var searchFlds = new Ext.util.MixedCollection();
    queryFlds.push('query');

    var fldName = new Ext.form.TextField({
          name : 'query',
          id : gridEl1,
          fieldLabel : '搜索',
          allowBlank : true,
          width : 100
        });
    searchFlds.add('query', fldName);

    grid1 = new Divo.Base.GridView({
          gridId : gridEl1,
          entityName : dbo1,
          queryFlds : queryFlds,
          searchFlds : searchFlds,
          autoSelectFirstRow : false,
          cm : new Ext.grid.ColumnModel([{
                header : '权限',
                dataIndex : 'name',
                width : 200
              }, {
                header : '描述',
                dataIndex : 'desc',
                width : 200
              }]),
          packageName : 'sys',
          queryUrl : 'system/get_sys_roles',
          jsonId : 'id',
          recordPk : ["id"]
        });

    selModel1 = grid1.getSelectionModel();
    selModel1.on('rowselect', onRowSelect);

    var cm = new Ext.grid.ColumnModel([{
          header : '模块名称',
          dataIndex : 'moduleName',
          width : 200
        }, {
          header : '操作1',
          dataIndex : 'operation1',
          align : 'center',
          width : 100,
          renderer : function(val, params, record) {
            if (record.data.hasChecked == 'N') {
              return "<a href='javascript:void(0);'>启用</a>";
            } else {
              return '已启用';
            }
          }
        }, {
          header : '操作2',
          dataIndex : 'operation2',
          align : 'center',
          width : 100,
          renderer : function(val, params, record) {
            if (record.data.hasChecked == 'Y') {
              return "<a href='javascript:void(0);'>停用</a>";
            }
          }
        }]);

    grid2 = new Ext.grid.GridPanel({
          store : new Ext.data.Store({
                proxy : new Ext.data.HttpProxy({
                      url : 'module_dict/query_module'
                    }),
                reader : new Ext.data.JsonReader({
                      root : 'rows',
                      totalProperty : 'totalCount',
                      id : 'moduleCode'
                    }, Ext.data.Record.create([{
                          name : 'moduleCode',
                          type : 'string'
                        }, {
                          name : "hasChecked",
                          type : "string"
                        }, {
                          name : "moduleName",
                          type : "string"
                        }])),
                remoteSort : false
              }),
          cm : cm,
          autoSelectFirstRow : false,
          displayLoadMask : true,
          loadMask : {
            msg : "正在加载数据..."
          }

        });
    grid2.getStore().on('beforeload', function(store, options) {
          store.baseParams.roleId = roleId;
        }, this, true);

    grid2.on('cellclick', function(o, row, cell, e) {
          if (o.getColumnModel().getDataIndex(cell) == 'operation1') {
            var data = o.getSelectionModel().selections.items[0].data;
            if (data.hasChecked == 'N') {
              activeModule(data.moduleCode);
            }
          }
          if (o.getColumnModel().getDataIndex(cell) == 'operation2') {
            var data = o.getSelectionModel().selections.items[0].data;
            if (data.hasChecked == 'Y') {
              inactiveModule(data.moduleCode);
            }
          }
        });

  }

  // 选择某行
  function onRowSelect(selModel, rowIndex, record) {

    var data = record.data;
    roleId = data.id;
    grid2.getStore().load();
  }

  function activeModule(moduleCode) {

    Ext.Ajax.request({
          params : {
            "roleId" : roleId,
            "moduleCode" : moduleCode
          },
          method : "POST",
          url : "module_dict/save_module",
          callback : function(o, s, r) {
            var respText = Ext.decode(r.responseText);
            if (respText.success) {
              grid2.getStore().reload();
            } else {
              if (respText.message.length > 0)
                Ext.Msg.alert('错误', respText.message);

            }
          }
        });

  }

  function inactiveModule(moduleCode) {

    Ext.Ajax.request({
          params : {
            "roleId" : roleId,
            "moduleCode" : moduleCode
          },
          method : "POST",
          url : "module_dict/del_module",
          callback : function(o, s, r) {
            var respText = Ext.decode(r.responseText);
            if (respText.success) {
              grid2.getStore().reload();
            } else {
              if (respText.message.length > 0)
                Ext.Msg.alert('错误', respText.message);

            }
          }
        });

  }

  // 创建layout
  function createLayout() {

    var viewport = new Ext.Viewport({
          layout : 'border',
          items : [{
                layout : 'border',
                region : 'center',
                border : false,
                items : [{
                      layout : 'fit',
                      region : 'west',
                      width : 400,
                      border : false,
                      items : [grid1]
                    }, {
                      layout : 'fit',
                      region : 'center',
                      border : false,
                      items : [grid2]
                    }]
              }]
        });

    viewport.doLayout();
  }

  /* ----------------------- public方法 ----------------------- */
  return {
    /**
     * 初始化
     */
    init : function() {
      createGrid();
      createLayout();
      grid1.getStore().load();
    }
  }
}();

Ext.onReady(Divo.app.ModuleDictList.init, Divo.app.ModuleDictList, true);