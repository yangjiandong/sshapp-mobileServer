/**
 * 查询指标管理
 */
Ext.namespace('Divo');
Ext.namespace('Divo.app');

Divo.app.QueryItemPanel = function() {
  // region : 'west'
  Divo.app.QueryItemPanel.superclass.constructor.call(this, {
        autoScroll : true,
        animate : true,
        border : false,
        id : 'reportitem-tree',
        rootVisible : false,
        lines : false,
        layout : 'fit',
        root : new Ext.tree.TreeNode({
              id : 'reportitem-tree-root',
              codeLevel : '',
              parentId : '',
              text : '全部',
              href : ''
            })
      });
}
Ext.extend(Divo.app.QueryItemPanel, Ext.tree.TreePanel, {
      selectMenu : function(id) {
        var bool = false;
        if (id) {
          if (this.root.attributes.id == id) {
            this.selectPath(this.root.getPath());
            bool = true;
          } else {
            var curnode;
            this.root.cascade(function(n) {
                  if (n.attributes.id == id) {
                    curnode = n;
                  }
                });
            if (curnode) {
              this.selectPath(curnode.getPath());
              bool = true;
            }
          }
        }
        return bool;
      }
    });

Ext.reg('Divo.app.QueryItemPanel', Divo.app.QueryItemPanel);

Divo.app.QueryItemList = function() {

  /* ----------------------- private属性 ----------------------- */
  var tree, grid, panel, selModel;
  var dbo = 'ItemSource';
  var cId = 0, parentId = 0;
  var fldId, fldItemName, fldCodeLevel, fldParentId;
  var win, formPanel, sourceWin, sourceForm;
  var fldSourceId, fldSpName, fldItemId;
  var sourceToolBar;

  /* ----------------------- private方法 ----------------------- */

  // 创建树
  function createTree() {
    tree = new Divo.app.QueryItemPanel();

    tree.on('click', function(node, e) {
          if (!node) {
            e.stopEvent();
            return;
          }
          cId = node.attributes.id;
          parentId = node.attributes.id;
          itemName = node.attributes.text;
          fldId.setValue(node.attributes.id);
          fldItemName.setValue(itemName);
          fldCodeLevel.setValue(node.attributes.codeLevel);
          fldParentId.setValue(node.attributes.parentId);
          grid.getStore().load();

          if (node.isLeaf()) {
            sourceToolBar.items.get("tlb_ADD").enable();
            sourceToolBar.items.get("tlb_EDIT").enable();
            sourceToolBar.items.get("tlb_DELETE").enable();
          } else {
            sourceToolBar.items.get("tlb_ADD").disable();
            sourceToolBar.items.get("tlb_EDIT").disable();
            sourceToolBar.items.get("tlb_DELETE").disable();
          }

        });
  }

  // 创建树面板
  function createPanel() {

    panel = new Ext.Panel({
          region : 'west',
          id : 'queryitem-tree-panel',
          title : '查询指标',
          split : true,
          autoScroll : true,
          width : 200,
          minSize : 175,
          maxSize : 400,
          layout : 'fit',
          margins : '3 0 3 3',
          items : [tree]
        });
  }

  // 创建树节点
  function createTreeNodes() {

    tree.root.removeAll(true);
    Ext.getBody().mask('正在加载数据，请稍候...', 'x-mask-loading');
    Ext.Ajax.request({
          url : "query_item/query_tree",
          scope : this,
          callback : function(o, s, r) {
            var respText = Ext.decode(r.responseText);
            if (respText.success) {
              var queryItem = respText.data;
              var parentNode;
              for (var i = 0; i < queryItem.length; i++) {
                // 创建节点
                var nodeOfModule = new Ext.tree.TreeNode({
                      id : queryItem[i].id,
                      text : queryItem[i].itemName,
                      parentId : queryItem[i].parentId,
                      codeLevel : queryItem[i].codeLevel,
                      parentId : queryItem[i].parentId,
                      leaf : queryItem[i].IsLeaf
                    });

                if (queryItem[i].parentId == 0) {
                  tree.root.appendChild(nodeOfModule);
                } else {
                  parentNode = tree.getNodeById(queryItem[i].parentId);
                  if (parentNode) {
                    parentNode.appendChild(nodeOfModule);
                  }
                }
              }
            }
            Ext.getBody().unmask();
          }
        });
  }

  function createGrid() {
    var queryFlds = new Array();
    var searchFlds = new Ext.util.MixedCollection();
    queryFlds.push('query');

    var fldName = new Ext.form.TextField({
          name : 'query',
          id : dbo + '-query',
          fieldLabel : '搜索',
          allowBlank : true,
          width : 100
        });
    searchFlds.add('query', fldName);

    grid = new Divo.Base.GridView({
          gridId : dbo,
          entityName : dbo,
          queryFlds : queryFlds,
          searchFlds : searchFlds,
          cm : new Ext.grid.ColumnModel([{
                header : '数据源',
                dataIndex : 'spName',
                width : 400
              }, {
                header : '指标参数',
                dataIndex : 'itemId',
                width : 200
              }]),
          packageName : 'pm.mob',
          queryUrl : 'query_item/query_source',
          jsonId : 'id',
          recordPk : ["id"]
        });

    grid.getStore().on('beforeload', function(store, options) {
          store.baseParams.itemId = cId;
        }, this, true);

    selModel = grid.getSelectionModel();

  }

  // 创建layout
  function createLayout() {
    var toolbar = new Ext.Toolbar({
          items : [{
                icon : "resources/img/g_rec_new.png",
                text : "新增",
                handler : function() {
                  createWindows();
                  fldId.setValue(null);
                  fldItemName.setValue(null);
                  fldCodeLevel.setValue(null);
                  fldParentId.setValue(parentId);
                }
              }, '-', {
                icon : "resources/img/g_rec_upd.png",
                text : "编辑",
                handler : function() {
                  createWindows();
                }
              }, '-', {
                icon : "resources/img/g_rec_del.png",
                text : "删除",
                handler : function() {
                  Ext.Msg.show({
                        title : '删除确认',
                        msg : '真的要删除这条数据吗?',
                        buttons : Ext.Msg.YESNO,
                        scope : this,
                        icon : Ext.MessageBox.QUESTION,
                        fn : function(btn) {
                          if (btn == 'yes') {
                            var node = tree.getSelectionModel()
                                .getSelectedNode();

                            Ext.Ajax.request({
                                  params : {
                                    "id" : cId
                                  },
                                  method : "POST",
                                  url : "query_item/delete_item",
                                  callback : function(o, s, r) {
                                    var respText = Ext.decode(r.responseText);
                                    if (respText.success) {
                                      node.removeAll(true);
                                      node.remove(true);
                                      grid.getStore().reload();
                                    } else {
                                      if (respText.message.length > 0)
                                        Ext.Msg.alert('错误', respText.message);
                                    }
                                  }
                                });
                          }
                        }
                      });
                }

              }, '-', {
                icon : "resources/img/expand.png",
                text : "展开",
                id : "tlb_EXPAND",
                disabled : false,
                handler : expandAllNodes
              }, '-', {
                icon : "resources/img/collapse.png",
                text : "收缩",
                id : "tlb_COLLAPSE",
                disabled : false,
                handler : collapseAllNodes
              }]
        });

    sourceToolBar = new Ext.Toolbar({
          items : [{
                icon : "resources/img/g_rec_new.png",
                id : "tlb_ADD",
                text : "新增数据源",
                handler : function() {
                  createSourceWindows();
                  fldSourceId.setValue(null);
                  fldItemId.setValue(cId);
                  fldSpName.setValue(null);
                }
              }, '-', {
                icon : "resources/img/g_rec_upd.png",
                id : "tlb_EDIT",
                text : "编辑",
                handler : function() {
                  var records = selModel.getSelections();
                  var data = records[0].data;
                  createSourceWindows();
                  fldSourceId.setValue(data.id);
                  fldItemId.setValue(data.itemId);
                  fldSpName.setValue(data.spName);
                }
              }, '-', {
                icon : "resources/img/g_rec_del.png",
                id : "tlb_DELETE",
                text : "删除",
                handler : function() {
                  Ext.Msg.show({
                        title : '删除确认',
                        msg : '真的要删除这条数据吗?',
                        buttons : Ext.Msg.YESNO,
                        scope : this,
                        icon : Ext.MessageBox.QUESTION,
                        fn : function(btn) {
                          if (btn == 'yes') {
                            var records = selModel.getSelections();
                            var data = records[0].data;

                            Ext.Ajax.request({
                                  params : {
                                    "id" : data.id
                                  },
                                  method : "POST",
                                  url : "query_item/delete_source",
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
                          }
                        }
                      });
                }

              }]
        });

    sourceToolBar.items.get("tlb_ADD").disable();
    sourceToolBar.items.get("tlb_EDIT").disable();
    sourceToolBar.items.get("tlb_DELETE").disable();

    var viewport = new Ext.Viewport({
          layout : 'border',
          listeners : {
            render : function() {
              createTreeNodes();
            }
          },
          items : [{
                layout : 'border',
                region : 'center',
                border : false,
                items : [{
                      layout : 'fit',
                      region : 'north',
                      height : 25,
                      split : true,
                      border : false,
                      items : [sourceToolBar]
                    }, {
                      layout : 'fit',
                      region : 'center',
                      border : false,
                      items : [grid]
                    }]
              }, {
                layout : 'border',
                region : 'west',
                border : false,
                width : 350,
                items : [{
                      layout : 'fit',
                      region : 'north',
                      height : 25,
                      split : true,
                      border : false,
                      items : [toolbar]
                    }, {
                      layout : 'fit',
                      region : 'center',
                      border : false,
                      items : [panel]
                    }]
              }]
        });

    viewport.doLayout();
  }

  function createSourceForm() {

    if (sourceForm) {
      return;
    }

    fldSpName = new Ext.form.TextField({
          name : "spName",
          dataIndex : "spName",
          fieldLabel : "存储过程",
          width : 250
        });
    fldItemId = new Ext.form.TextField({
          name : "itemId",
          dataIndex : "itemId",
          hidden : true
        });
    fldSourceId = new Ext.form.TextField({
          name : "id",
          dataIndex : "id",
          hidden : true
        });
    sourceForm = new Ext.FormPanel({
          border : false,
          width : 500,
          labelPad : 5,
          bodyStyle : 'padding: 20px 50px',
          frame : false,
          region : 'center',
          items : [fldSourceId, fldSpName, fldItemId]
        });

  }

  function createSourceWindows() {
    if (sourceWin) {
      sourceWin.show();
      return;
    }

    sourceWin = new Ext.Window({
          title : '设置数据源',
          iconCls : 'icon-win',
          autoHeight : true,
          width : 515,
          layout : 'form',
          plain : false,
          buttonAlign : 'right',
          closable : false,
          modal : true,
          items : [sourceForm]
        });

    var okHideBtn = sourceWin.addButton({
          text : '保存'
        }, saveSource, this);

    var cancelBtn = sourceWin.addButton({
          text : '取消'
        }, function() {
          sourceWin.hide();
        }, this);

    sourceWin.show();
  }

  function saveSource() {

    var value1 = fldSourceId.getValue();
    var value2 = fldItemId.getValue();
    var value3 = fldSpName.getValue();
    if (value3.length == 0) {
      return;
    }

    Ext.Ajax.request({
          params : {
            "id" : value1,
            "itemId" : value2,
            "spName" : value3
          },
          method : "POST",
          url : "query_item/save_source",
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
    sourceWin.hide();
  }

  function createForm() {

    if (formPanel) {
      return;
    }

    fldItemName = new Ext.form.TextField({
          name : "itemName",
          dataIndex : "itemName",
          fieldLabel : "指标名称",
          width : 150
        });
    fldCodeLevel = new Ext.form.TextField({
          name : "codeLevel",
          dataIndex : "codeLevel",
          hidden : true
        });
    fldParentId = new Ext.form.TextField({
          name : "parentId",
          dataIndex : "parentId",
          hidden : true
        });
    fldId = new Ext.form.TextField({
          name : "id",
          dataIndex : "id",
          hidden : true
        });
    formPanel = new Ext.FormPanel({
          border : false,
          width : 400,
          labelPad : 5,
          bodyStyle : 'padding: 20px 50px',
          frame : false,
          region : 'center',
          items : [fldId, fldItemName, fldCodeLevel, fldParentId]
        });

  }

  function createWindows() {
    if (win) {
      win.show();
      return;
    }

    win = new Ext.Window({
          title : '定义查询指标',
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

    var value1 = fldId.getValue();
    var value2 = fldItemName.getValue();
    var value3 = fldCodeLevel.getValue();
    var value4 = fldParentId.getValue();
    if (value2.length == 0) {
      return;
    }

    var node = tree.getSelectionModel().getSelectedNode();
    Ext.Ajax.request({
          params : {
            "id" : value1,
            "itemName" : value2,
            "codeLevel" : value3,
            "parentId" : value4
          },
          method : "POST",
          url : "query_item/save_item",
          callback : function(o, s, r) {
            var respText = Ext.decode(r.responseText);
            if (respText.success) {
              if (value1 == null || value1.length == 0) {
                var data = respText.result;
                var newNode = new Ext.tree.TreeNode({
                      id : data.id,
                      text : data.itemName,
                      parentId : cId,
                      codeLevel : data.codeLevel,
                      parentId : data.parentId,
                      leaf : true
                    });
                if (value4 == 0) {
                  tree.root.appendChild(newNode);
                } else {
                  node.appendChild(newNode);
                }

                if (node && node.isLeaf()) {
                  node.leaf = false;
                }
                node.expand();
              } else {
                node.setText(value2);
              }

            } else {
              if (respText.message.length > 0)
                Ext.Msg.alert('错误', respText.message);

            }
          }
        });
    onWinHide();
  }

  // 关闭树
  function collapseAllNodes() {
    tree.collapseAll();
  }

  // 展开树
  function expandAllNodes() {
    tree.root.expandChildNodes(true);
  }
  /* ----------------------- public方法 ----------------------- */
  return {
    /**
     * 初始化
     */
    init : function() {
      createGrid();
      createForm();
      createSourceForm();
      createTree();
      createPanel();
      createLayout();
    }
  }
}();

Ext.onReady(Divo.app.QueryItemList.init, Divo.app.QueryItemList, true);