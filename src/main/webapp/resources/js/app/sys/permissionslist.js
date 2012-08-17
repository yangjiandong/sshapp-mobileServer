/**
 * 核算单元授权
 */
Ext.namespace('Divo');
Ext.namespace('Divo.app');
Ext.QuickTips.init();
Ext.BLANK_IMAGE_URL = 'resources/img/s.gif';

Divo.app.PermissionsList = function() {
  /* ----------------------- private属性 ----------------------- */
  var canEdit = false, status = "list"; // 当前列表状态("list","insert","edit")
  var dbo1 = 'Permissions';
  var dbo2 = 'PermissionUsers';
  var gridEl1 = 'permissions-grid';
  var gridEl2 = 'permissionusers-grid';
  var grid1, grid2, toolbar;
  var selModel1, selModel2;
  var win, cName, selGrid, unSelGrid, toolPanel;
  var leftPic = 'resources/img/fam.icons/left.gif';
  var rightPic = 'resources/img/fam.icons/right.gif';

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
          autoSelectFirstRow : true,
          cm : new Ext.grid.ColumnModel([{
                header : '权限',
                dataIndex : 'name',
                width : 120
              }, {
                header : '描述',
                dataIndex : 'descrip',
                width : 200
              }]),
          packageName : 'sys',
          queryUrl : 'system/getPermissions',
          jsonId : 'id',
          recordPk : ["id"]
        });

    selModel1 = grid1.getSelectionModel();
    selModel1.on('rowselect', onRowSelect);

    grid2 = new Divo.Base.GridView({
          gridId : gridEl2,
          entityName : dbo2,
          queryFlds : queryFlds,
          searchFlds : searchFlds,
          packageName : 'sys',
          cm : new Ext.grid.ColumnModel([{
                header : '姓名',
                dataIndex : 'userName',
                width : 200
              }]),
          hasBbar : false,
          pageSize : 15,
          queryUrl : 'system/getPermissionUsers',
          jsonId : 'id',
          recordPk : ["id"]
        });
    grid2.getStore().on('beforeload', function(store, options) {
          store.baseParams.name = cName;
        }, this, true);

    selModel2 = grid2.getSelectionModel();

    selGrid = new Divo.Base.GridView({
          gridId : 'permission-sel',
          entityName : 'User',
          queryFlds : queryFlds,
          searchFlds : searchFlds,
          packageName : 'sys',
          checkboxSelectionModel : true,
          autoSelectFirstRow : false,
          specialFields : ['name'],
          queryUrl : 'system/selPermissionUsers',
          jsonId : 'id',
          recordPk : ["id"]
        });
    selGrid.getStore().on('beforeload', function(store, options) {
          store.baseParams.name = cName;
        }, this, true);

    unSelGrid = new Divo.Base.GridView({
          gridId : 'permission-unsel',
          entityName : 'User',
          queryFlds : queryFlds,
          searchFlds : searchFlds,
          packageName : 'sys',
          queryUrl : 'system/unselPermissionUsers',
          jsonId : 'id',
          checkboxSelectionModel : true,
          autoSelectFirstRow : false,
          specialFields : ['name'],
          recordPk : ["id"]
        });
    unSelGrid.getStore().on('beforeload', function(store, options) {
          store.baseParams.name = cName;
        }, this, true);

  }

  // 选择某行
  function onRowSelect(selModel, rowIndex, record) {

    var data = record.data;
    cName = data.name;
    grid2.getStore().reload();
  }

  // 创建layout
  function createLayout() {

    toolbar = new Ext.Toolbar({
          items : ['-', {
                text : "选择用户",
                handler : function() {
                  selGrid.getStore().reload();
                  unSelGrid.getStore().reload();
                  win.show();
                }
              }]
        });

    var viewport = new Ext.Viewport({
          layout : 'border',
          items : [{
                layout : 'border',
                region : 'center',
                height : 400,
                border : false,
                items : [{
                      layout : 'border',
                      region : 'west',
                      width : 400,
                      border : false,
                      items : [{
                            layout : 'fit',
                            region : 'north',
                            border : false,
                            items : [new Ext.Toolbar({
                                  items : [{
                                        icon : "resources/img/g_rec_refresh.gif",
                                        text : "刷新",
                                        handler : function() {
                                          grid1.getStore().reload();
                                        }
                                      }]
                                })],
                            height : 25
                          }, {
                            layout : 'fit',
                            region : 'center',
                            border : false,
                            items : [grid1]
                          }]
                    }, {
                      layout : 'border',
                      region : 'center',
                      width : 400,
                      border : false,
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
                            items : [grid2]
                          }]
                    }]
              }]
        });

    viewport.doLayout();
  }

  function createWindow() {
    if (win)
      return;
    win = new Ext.Window({
          id : 'permission-win',
          title : '设置权限对应的用户',
          iconCls : 'icon-detailwin',
          height : 550,
          width : 800,
          layout : 'border',
          plain : false,
          buttonAlign : 'right',
          closable : false,
          modal : true,
          items : [{
                border : false,
                layout : 'border',
                region : 'center',
                items : [{
                      border : false,
                      layout : 'fit',
                      region : 'east',
                      height : 350,
                      width : 50,
                      items : [toolPanel]
                    }, {
                      border : false,
                      layout : 'fit',
                      region : 'center',
                      height : 450,
                      width : 375,
                      items : [unSelGrid]
                    }]
              }, {
                border : false,
                layout : 'border',
                region : 'east',
                width : 375,
                items : [selGrid]
              }]
        });

    var okHideBtn1 = win.addButton({
          text : '保存'
        }, saveDetail, this);

    var cancelBtn1 = win.addButton({
          text : '取消'
        }, function() {
          win.hide();
        }, this);
  }

  function saveDetail() {
    if (cName == null || cName.length == 0) {
      return;
    }
    var codeList = "";
    var data = selGrid.getStore().data.items;
    if (data != null) {
      for (i = 0; i < data.length; i++) {
        var code = data[i].data.id;
        codeList = codeList + "|" + code;
      }
    }

    Ext.Ajax.request({
          params : {
            "name" : cName,
            "codeList" : codeList
          },
          method : "POST",
          url : "system/savePermissionUsers",
          callback : function(o, s, r) {
            var respText = Ext.decode(r.responseText);
            if (respText.success) {
              win.hide();
              grid2.getStore().reload();
            }
          }
        });

    win.hide();

  }

  function getToolPanel() {

    if (toolPanel) {
      return;
    } else {
      toolPanel = new Ext.Panel({
            border : false,
            frame : true,
            region : 'east',
            bodyStyle : 'padding:120px 5px;',
            items : [{
                  xtype : "button",
                  width : 25,
                  icon : rightPic,
                  handler : function() {
                    var sel = unSelGrid.getSelectionModel().getSelections();
                    selGrid.getStore().add(sel);
                    unSelGrid.getStore().remove(sel);
                    selGrid.getStore().sort('expuid', 'ASC');

                  }
                }, {
                  border : false,
                  height : 5,
                  html : '&nbsp;'
                }, {
                  width : 25,
                  xtype : "button",
                  icon : leftPic,
                  handler : function() {
                    var sel = selGrid.getSelectionModel().getSelections();
                    unSelGrid.getStore().add(sel);
                    selGrid.getStore().remove(sel);
                    unSelGrid.getStore().sort('id', 'ASC');
                  }
                }, {
                  border : false,
                  height : 5,
                  html : '&nbsp;'
                }]
          });

    }

  }

  /* ----------------------- public方法 ----------------------- */
  return {
    /**
     * 初始化
     */
    init : function() {
      createGrid();
      getToolPanel();
      createWindow();
      createLayout();
      grid1.getStore().load();
    }
  }
}();

Ext.onReady(Divo.app.PermissionsList.init, Divo.app.PermissionsList, true);