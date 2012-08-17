/**
 * 系统主页面,动态生成菜单
 */
Ext.namespace('Divo');
Ext.namespace('Divo.app');
Ext.QuickTips.init();
var uiDcInc;

/* 菜单树面板 */
Divo.app.MenuPanel = function() {
  Divo.app.MenuPanel.superclass.constructor.call(this, {
        id : 'menu-tree',
        region : 'west',
        autoScroll : true,
        animate : true,
        border : false,
        minSize : 175,
        maxSize : 400,
        split : true,
        // no use root
        rootVisible : false,
        layout : 'fit',
        // loader : new Ext.tree.TreeLoader({
        // dataUrl : 'system/loadmenu2'
        // }),

        // 虽然不显示, 还需定义
        root : new Ext.tree.AsyncTreeNode({
              id : 'root',
              resourceId : '0',
              parentId : '0',
              codeLevel : 0,
              text : '请先选择子系统',
              href : ''
            })

      });

  // this.getLoader().on("beforeload", function(treeLoader, node) {
  // treeLoader.baseParams.resourceId = node.attributes.resourceId;
  // }, this);
  // this.getSelectionModel().on('beforeselect', function(sm, node) {
  // return node.isLeaf();
  // }, this);
  // this.on('beforeexpandnode', function(node, deep, anim) {
  // this.getLoader().load(node);
  // }, this);
}

Ext.extend(Divo.app.MenuPanel, Ext.tree.TreePanel, {
      selectMenu : function(menuId) {
        if (menuId) {
          if (this.root.attributes.resourceId == menuId) {
            this.selectPath(this.root.getPath());
          } else {
            var curnode;
            this.root.cascade(function(n) {
                  if (n.isLeaf() && n.attributes.resourceId == menuId) {
                    curnode = n;
                  }
                });
            if (curnode) {
              this.selectPath(curnode.getPath());
            }
          }
        }
      }
    });

// main.panel.js

Divo.app.Main = function() {
  /* -------------------- private属性 -------------------- */
  var aboutWin, menuBar, menuComponent, mainPanel, viewport, pwdWin;
  var sysConfig, subSysMenu, thumbTemplate;
  var maxPanelNum = 3;

  /* -------------------- private方法 -------------------- */
  // 创建菜单栏
  function createMenus() {
    subSysMenu = new Ext.menu.Menu({
          id : 'sys-menu',
          items : []
        });

    // 暂时不要
    var sessionMenu = new Ext.menu.Menu({
          id : 'session-menu',
          style : {
            overflow : 'visible'
          },
          items : [{
                id : "menu-logout",
                icon : 'resources/img/icon/logoff.png',
                text : '注销',
                handler : function() {
                  doLogout();
                },
                scope : this
              }, '-', {
                id : "menu-change-pwd",
                icon : 'resources/img/icon/key.png',
                text : '修改密码',
                handler : function() {
                  pwdWin.show();
                },
                scope : this
              }, '-', {
                id : "menu-lock",
                text : '锁屏',
                handler : function() {
                  // doLockSession();
                },
                scope : this,
                disabled : true
              }]
        });

    var aboutMenu = new Ext.menu.Menu({
          id : 'about-menu',
          style : {
            overflow : 'visible'
          },
          items : [{
                text : '关于',
                handler : function() {
                  aboutWin.show();
                },
                scope : this
              }, {
                text : '在线帮助',
                handler : function() {
                },
                scope : this,
                disabled : true
              }]
        });

    menuBar = new Ext.Toolbar({
      id : 'menu-toolbar',
      items : [
          new Ext.Toolbar.Separator(),
          {
            icon : 'resources/img/subsys.gif',
            text : '首页',
            handler : function() {
              goToIndex();
            },
            scope : this,
            disabled : false
          },
          {
            id : "menu-change-pwd",
            icon : 'resources/img/icon/key.png',
            text : '修改密码',
            handler : function() {
              pwdWin.show();
            },
            scope : this
          },
          {
            text : '帮助',
            icon : 'resources/img/help.gif',
            menu : aboutMenu
          },
          {
            id : "menu-logout2",
            icon : 'resources/img/icon/logoff.png',
            text : '注销',
            handler : function() {
              doLogout();
            },
            scope : this
          },
          '->',
          '<span id="cur-db-name"></span>&nbsp;&nbsp;当前用户:&nbsp;&nbsp;<span id="cur-user-name">'
              + sysConfig.user_name + '</span>']
    });
  }

  /**
   * 加载子系统的菜单,并关闭mainPanel中已打开的面板
   */
  function loadModuleMenu(item) {
    onCloseMainPanelTabs();

    menuTree.getLoader().baseParams.module = item.code;
    menuTree.getRootNode().setText(item.text);
    // menuTree.getRootNode().attributes.resourceId = item.code;
    menuTree.getLoader().load(menuTree.getRootNode(), function(l, n, r) {
          l.expand();
        });

  }

  // 关闭全部打开的主面板Tab Panel
  function onCloseMainPanelTabs() {
    var ps = []
    var items = mainPanel.items.items;
    for (var i = 0; i < items.length; i++) {
      ps.push(items[i].id);
    }
    for (var i = 0; i < ps.length; i++) {
      mainPanel.remove(mainPanel.getItem(ps[i]), true);
    }
  }

  // 创建布局
  function createViewport() {

    aboutWin = new Divo.app.AboutWin({
          sysConfig : sysConfig
        });

    pwdWin = new Divo.app.ChangePwdFormWin({
          curUserName : sysConfig.user_name
        });

    var statusBar = new Ext.BoxComponent({
          region : 'south',
          height : 18,
          autoEl : {
            html : '<center>&nbsp;' + sysConfig.copyright + '</center>'
          }
        });

    menuTree = new Divo.app.MenuPanel();
    mainPanel = new Divo.app.MainPanel({
          cfg_run_mode : CFG_DEPLOYMENT_TYPE,
          build_id : CFG_BUILD_ID
        });

    mainPanel.on('tabchange', function(tp, tab) {
          if (tab) {
            menuTree.selectMenu(tab.menuId);
          }
        });

    // 限制最多能打开的面板,防止浏览器过载崩溃
    mainPanel.on('beforeadd', function(container, component, index) {
          if (index > maxPanelNum) {
            var items = container.items.items;
            var menuId = items[0].id;
            var tab = mainPanel.getItem(menuId)
            mainPanel.remove(tab, true);
          }
        });

    // 防止IFRAME销毁后仍然占用内存
    mainPanel.on('beforeremove', function(o, p) {
          var _id = p.id.substring(p.id.indexOf('-') + 1);
          var ifrId = "frame-" + _id;

          var wf = window.frames[ifrId];

          // 该方法有效
          if (!Ext.isEmpty(wf)) {
            wf.document.open("text/html", "replace");
            wf.document.write("");
            wf.document.close();
          }

        });

    var menuComponent = new Ext.Panel({
          region : 'west',
          id : 'west-panel', // see Ext.getCmp() below
          title : sysConfig.module_name,
          split : true,
          autoScroll : true,
          width : 200,
          minSize : 175,
          maxSize : 400,
          layout : 'fit',
          collapsible : true,
          collapseMode : 'mini',
          margins : '3 0 3 3',
          items : [menuTree]
        });

    menuTree.on('click', function(node, e) {
          if (node.isLeaf() && node.attributes.url.length > 0) {
            e.stopEvent();

            mainPanel.loadContent(node.attributes.url,
                node.attributes.resourceId, node.text, node.attributes.iconCls,
                sysConfig.module_name);
            viewport.doLayout();
          } else {
            e.stopEvent();
          }
        });

    var titleBar = new Ext.Panel({
      region : 'north',
      id : 'north-panel',
      split : false,
      height : 56,
      border : false,
      collapsible : false,
      margins : '0 0 0 0',
      layout : 'border',
      items : [{
        region : 'north',
        html : '<div id="titlebar"><input type=hidden value="" id="activeFrameId">'
            + '</input><h1>'
            + sysConfig.application_name
            + sysConfig.version
            + '</h1></div>',
        border : false,
        height : 28
      }, {
        region : 'center',
        border : false,
        height : 26,
        items : [menuBar]
      }]
    });

    viewport = new Ext.Viewport({
          layout : 'tdgi_border',
          items : [titleBar, statusBar, menuComponent, mainPanel],
          listeners : {
            render : function() {

              createTreeNodes();
              // Ext.Ajax.request({
              // url : 'system/loadsubsystem',
              // success : successFn
              // });
            }
          }
        });

    viewport.doLayout();
  }

  // 创建树节点
  function createTreeNodes() {

    menuTree.root.removeAll(true);
    Ext.getBody().mask('正在加载数据，请稍候...', 'x-mask-loading');
    Ext.Ajax.request({
          url : "system/loadmenu2",
          scope : this,
          callback : function(o, s, r) {
            var respText = Ext.decode(r.responseText);
            if (respText.success) {
              var resource = respText.data;
              var parentNode;
              var nodeOfModule;
              for (var i = 0; i < resource.length; i++) {

                if (resource[i].codeLevel < App.resourceFloor) {
                  nodeOfModule = new Ext.tree.TreeNode({
                        id : resource[i].resourceId,
                        resourceId : resource[i].resourceId,
                        text : resource[i].text,
                        parentId : resource[i].parentId,
                        codeLevel : resource[i].codeLevel,
                        leaf : resource[i].leaf,
                        url : resource[i].url,
                        expanded : true
                      });
                } else {
                  nodeOfModule = new Ext.tree.TreeNode({
                        id : resource[i].resourceId,
                        resourceId : resource[i].resourceId,
                        text : resource[i].text,
                        parentId : resource[i].parentId,
                        codeLevel : resource[i].codeLevel,
                        leaf : resource[i].leaf,
                        url : resource[i].url,
                        expanded : false
                      });
                }

                if (resource[i].parentId == 0) {
                  menuTree.root.appendChild(nodeOfModule);
                } else {
                  parentNode = menuTree.getNodeById(resource[i].parentId);
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

  // viewport 渲染后，加载子系统菜单项
  function successFn(response) {
    var data = Ext.decode(response.responseText);

    if (data != null && data.success) {
      subSys = data.subSystems;
      for (var i = 0; i < subSys.length; i++) {
        var menuCode = subSys[i].resourceId;
        var menuName = subSys[i].text;

        subSysMenu.add({
              id : 'menu-' + subSys[i].resourceId,
              code : subSys[i].resourceId,
              text : subSys[i].text,
              handler : loadModuleMenu
            });
      }
    }

  }

  // 注销
  function doLogout() {
    Ext.Ajax.request({
          url : "common/logout",
          scope : this,
          callback : function(o, s, r) {
            var respText = Ext.decode(r.responseText);
            if (respText.success && respText.message == "OK") {
              viewport.destroy();
              window.location.href = 'common/index';
            } else {
              if (!Ext.isEmpty(respText.message)) {
                Ext.Msg.alert('错误', respText.message);
              } else {
                Ext.Msg.alert('错误', '因为不能取得服务端信息，不能正常注销。');
              }
            }
          }
        });
  }

  // 回到首页
  function goToIndex() {
    onCloseMainPanelTabs();
    window.location.href = 'common/index';
  }

  function getSysConfig() {
    if (sysConfig == null) {
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
                module_id : resp.resource.resourceId
              }
            }
          });
    }
  }

  // 用户选择数据库
  function selectDatabase() {

    Ext.Ajax.request({
          url : "system/get_user_dbs",
          method : 'POST',
          async : false,
          scope : this,
          callback : function(o, s, r) {
            var respText = Ext.decode(r.responseText);
            if (respText.success) {
              if (respText.module != '1') {
                var data = respText.data;
                if (data == null || data.length == 0) {
                  alert('该用户没有可用的数据库');
                  goToIndex();
                } else if (data.length == 1) {
                  directUpdate(data[0].dbcode);
                  Ext.fly('cur-db-name').dom.innerHTML = "当前数据库:"
                      + data[0].dbname;
                } else {
                  updateBySelect(data);
                }
              }
            } else {
              alert('查询用户可用数据库失败');
              goToIndex();
            }
          }
        });

  }

  // 直接修改用户session中的partDB
  function directUpdate(dbcode) {
    Ext.Ajax.request({
          url : "system/update_user_db",
          method : 'POST',
          params : {
            dbcode : dbcode
          },
          async : false,
          scope : this,
          callback : function(o, s, r) {
            var respText = Ext.decode(r.responseText);
            if (respText.success) {

            }
          }
        });
  }

  // 弹出窗口，根据用户选择修改session中的partDB
  function updateBySelect(data) {

    SelectDatabaseWin = new Divo.app.SelectDatabaseWin({
          userDBs : data
        });
    SelectDatabaseWin.on('afterselect', function(dbname) {
          Ext.fly('cur-db-name').dom.innerHTML = "当前数据库:" + dbname;
        }, this);
    SelectDatabaseWin.on('noselect', function() {
          goToIndex();
        }, this);
    SelectDatabaseWin.show();
  }

  /* ----------------------- public方法 ----------------------- */
  return {
    init : function() {
      uiDcInc = new Ext.util.MixedCollection();
      initUiDcIncludes(uiDcInc);
      // 分开存放定义
      initUiDcIncludes2(uiDcInc);

      getSysConfig();
      createMenus();
      createViewport();
      selectDatabase();
    }
  }
}();

Ext.onReady(Divo.app.Main.init, Divo.app.Main, true);
