/**
 * 用户管理
 */
Ext.namespace('Divo');
Ext.namespace('Divo.app');

Divo.app.MobLogList = function() {
  /* ----------------------- private属性 ----------------------- */
  var gridEl = 'user-grid', treeEl = 'app-user-role-tree'
  var canEdit = false, status = "list"; // 当前列表状态("list","insert","edit")
  var tree, root, isUserAction;
  var cUserId;
  var grid, form, selModel, toolbar;
  var logGrid;

  /* ----------------------- private方法 ----------------------- */

  // 创建列表
  function createGrid() {
    var queryFlds = new Array();
    var searchFlds = new Ext.util.MixedCollection();

    grid = new Divo.Base.GridView({
          // autoSelectFirstRow : false,
          gridId : gridEl,
          entityClassName : 'org.ssh.sys.entity.User',

          autoFit : false,
          queryUrl : 'system/get_sys_users',
          jsonId : 'id',
          recordPk : ["id"]
        });
    selModel = grid.getSelectionModel();
    selModel.on('rowselect', onRowSelect);

    logGrid = new Divo.Base.GridView({
          remoteSort : true,
          gridId : "user-mob-log",
          entityClassName : 'org.ssh.pm.mob.entity.MobLog',

          autoFit : false,
          queryUrl : 'moblog/query',
          jsonId : 'id',
          recordPk : ["id"]
        });

    logGrid.getStore().on('beforeload', function(store, options) {
          store.baseParams.userId = cUserId;
        }, this, true);

  }

  // 选择某行
  function onRowSelect(selModel, rowIndex, record) {
    var data = record.data;

    status = "list";
    setToolbarStatus();
    cUserId = data.id;

    logGrid.getStore().load();

  }

  // 创建layout
  function createLayout() {
    toolbar = new Ext.Toolbar({
          items : ['-']
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
              }, {
                layout : 'fit',
                region : 'east',
                // height : 300,
                width : 600,
                items : [logGrid]

              }]

        });

    viewport.doLayout();
  }

  // 切换过滤查询工具条状态
  function toggleFilterBar() {
    grid.toggleFilterBar();
  }

  // 加载数据完成后
  function onLoad(store, records, options) {
    status = "list";
    setToolbarStatus();
  }

  // 设置工具条状态
  function setToolbarStatus() {
    if (status == "list") {
      // toolbar.items.get("tlb_SAVE").disable();

    }
  }

  /* ----------------------- public方法 ----------------------- */
  return {
    /**
     * 初始化
     */
    init : function() {
      createGrid();
      createLayout();
      grid.load();
      grid.getStore().on("load", onLoad);
    }
  }
}();

Ext.onReady(Divo.app.MobLogList.init, Divo.app.MobLogList, true);