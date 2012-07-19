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
  var grid, selModel, toolbar;
  var logGrid, logSelModel;

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
          checkboxSelectionModel : true,
          autoFit : false,
          queryUrl : 'moblog/query',
          jsonId : 'id',
          recordPk : ["id"]
        });
    logSelModel = logGrid.getSelectionModel();
    logGrid.getStore().on('beforeload', function(store, options) {
          store.baseParams.userId = cUserId;
        }, this, true);
    var cm = logGrid.getColumnModel();
    cm.setRenderer(cm.findColumnIndex('type'), function(val) {
          if (val === 'error') {
            return '<span style="background: red; color: white;">出错<span>';
          } else {
            return val;
          }

        })
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
          items : [{
                icon : "resources/img/g_rec_del.png",
                text : "删除",
                id : "tlb_DELETE",
                handler : deletes
              }]
        });

    var viewport = new Ext.Viewport({
          layout : 'border',
          items : [{
                layout : 'fit',
                region : 'center',
                border : false,
                items : [grid]
              }, {
                layout : 'border',
                region : 'east',
                // height : 300,
                width : 600,
                items : [{
                      layout : 'fit',
                      region : 'north',
                      border : false,
                      items : [toolbar],
                      height : 25
                    }, {
                      layout : 'border',
                      region : 'center',
                      border : false,
                      items : [logGrid]
                    }]
              }]

        });

    viewport.doLayout();
  }

  // 删除记录
  function deletes() {
    Ext.Msg.show({
          title : '删除确认',
          msg : '真的要删除吗?',
          buttons : Ext.Msg.YESNO,
          fn : function(btn) {
            if (btn == 'yes') {
              deletes2();
            }
          },
          scope : this,
          icon : Ext.MessageBox.QUESTION
        });
  };

  function deletes2() {
    var records = logSelModel.getSelections();
    if (records.length == 0) {
      Ext.Msg.alert('提示', '还没有选取项目');
      return;
    }

    logGrid.body.mask('正在处理...', 'x-mask-loading');
    var itemNos = [];
    for (var i = 0; i < records.length; i++) {
      itemNos.push(records[i].data.id);
    }

    Ext.Ajax.request({
          url : 'moblog/deletes',
          async : true,
          params : {
            ids : itemNos.toString()

          },
          callback : function(o, s, r) {
            logGrid.body.unmask();
            var respText = Ext.decode(r.responseText);
            if (respText.success) {
              logGrid.getStore().load();

            }
          }
        });
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