Ext.onReady(function() {

      // on line users
      var cmudb = new Ext.grid.ColumnModel([{
            header : '用户名',
            dataIndex : 'userName',
            width : 120
          }, {
            header : 'ip',
            dataIndex : 'ip',
            width : 100
          }, {
            header : '登录时间',
            dataIndex : 'loginTime',
            width : 150
          }])

      var studb = new Ext.data.JsonStore({
            fields : ['userName', 'ip', 'loginTime'],
            // url : '../system/get_online_users',
            proxy : new Ext.data.HttpProxy({
                  url : '../system/get_online_users',
                  timeout : 300000
                }),
            root : 'datas',
            totalProperty : 'totalCount'
          });

      var u_grid = new Ext.grid.GridPanel({
            loadMask : {
              msg : "正在统计..."
            },
            title : '在线用户',
            store : studb,

            cm : cmudb,
            stripeRows : true,
            // stateful: true,
            height : 160,
            width : 400
          });

      u_grid.render('u-info');
      u_grid.getStore().load();

      // 自动一秒刷新
      // setInterval(function(){
      // u_grid.getStore().load();
      // db_grid.getStore().load();
      // },1000)

      var button_u = Ext.get('u-btn');
      button_u.on('click', function() {
            u_grid.getStore().load();

          });

      // datasource
      var cmdb = new Ext.grid.ColumnModel([{
            header : '连接',
            dataIndex : 'name',
            width : 150
          }, {
            header : '最大连接',
            dataIndex : 'max',
            width : 80
          }, {
            header : '当前连接',
            dataIndex : 'active',
            width : 80
          }, {
            header : '空闲连接',
            dataIndex : 'idle',
            width : 80

          }])

      var stdb = new Ext.data.JsonStore({
            fields : ['name', 'msg', 'success', 'max', 'active', 'idle'],
            // url : '../common/datasource',
            proxy : new Ext.data.HttpProxy({
                  url : '../common/datasource',
                  timeout : 300000
                }),
            root : 'datas',
            totalProperty : 'totalCount'
          });

      var db_grid = new Ext.grid.GridPanel({
            loadMask : {
              msg : "正在统计..."
            },
            title : '数据库连接',
            store : stdb,

            cm : cmdb,
            stripeRows : true,
            // stateful: true,
            height : 160,
            width : 400
          });

      db_grid.render('database-info');
      db_grid.getStore().load();

      var buttondb = Ext.get('db-btn');
      buttondb.on('click', function() {
            db_grid.getStore().load();

          });

      var win;
      var grid;
      var button = Ext.get('init-btn');

      // 初始系统
      button.on('click', function() {

            if (!win) {
              var cm = new Ext.grid.ColumnModel([{
                    header : '信息',
                    dataIndex : 'msg',
                    width : 300
                  }, {
                    header : '状态',
                    dataIndex : 'success',
                    renderer : function num(val) {
                      if (val) {
                        return '<span style="color:green;">成功</span>';
                      } else {
                        return '<span style="color:red;">失败</span>';
                      }
                    }
                  }])

              var st = new Ext.data.JsonStore({
                    fields : ['name', 'msg', 'success'],
                    // url : '../common/init',
                    proxy : new Ext.data.HttpProxy({
                          url : '../common/init',
                          timeout : 300000
                        }),
                    root : 'datas',
                    totalProperty : 'totalCount'
                  });

              grid = new Ext.grid.GridPanel({
                    loadMask : {
                      msg : "正在初始化..."
                    },
                    region : 'center',
                    store : st,

                    cm : cm,
                    viewConfig : {
                      forceFit : true
                    }
                  });

              win = new Ext.Window({
                    applyTo : 'hello-win',
                    layout : 'fit',
                    width : 500,
                    height : 300,
                    closable : false,
                    plain : true,
                    items : grid,

                    buttons : [{
                          text : '确定.',
                          handler : function() {
                            showMigrationBtn();
                            win.hide();
                          }
                        }]
                  });
            }
            grid.getStore().load();
            win.show(this);
          });

      // 院级
      var win3;
      var grid3;
      var button3 = Ext.get('init3-btn');
      button3.on('click', function() {
            if (!win3) {
              var cm = new Ext.grid.ColumnModel([{
                    header : '信息',
                    dataIndex : 'msg',
                    width : 300
                  }, {
                    header : '状态',
                    dataIndex : 'success',
                    renderer : function num(val) {
                      if (val) {
                        return '<span style="color:green;">成功</span>';
                      } else {
                        return '<span style="color:red;">失败</span>';
                      }
                    }
                  }])

              var st = new Ext.data.JsonStore({
                    fields : ['name', 'msg', 'success'],
                    proxy : new Ext.data.HttpProxy({
                          url : '../common/init3',
                          timeout : 300000
                        }),
                    root : 'datas',
                    totalProperty : 'totalCount'
                  });

              grid3 = new Ext.grid.GridPanel({
                    loadMask : {
                      msg : "正在初始化院级核算系统..."
                    },
                    region : 'center',
                    store : st,

                    cm : cm,
                    viewConfig : {
                      forceFit : true
                    }
                  });

              win3 = new Ext.Window({
                    applyTo : 'hello3-win',
                    layout : 'fit',
                    width : 500,
                    height : 300,
                    closable : false,
                    plain : true,
                    items : grid3,

                    buttons : [{
                          text : '确定.',
                          handler : function() {
                            showMigrationBtn()
                            win3.hide();
                            // 刷新整个页面
                            // window.location.reload();
                          }
                        }]
                  });
            }
            grid3.getStore().load();
            win3.show(this);
          });

      // 奖金
      var win44;
      var grid44;
      var button44 = Ext.get('init4-btn');
      button44.on('click', function() {
            // create the window on the first click and reuse on subsequent
            // clicks
            if (!win44) {
              var cm = new Ext.grid.ColumnModel([{
                    header : '信息',
                    dataIndex : 'msg',
                    width : 300
                  }, {
                    header : '状态',
                    dataIndex : 'success',
                    renderer : function num(val) {
                      if (val) {
                        return '<span style="color:green;">成功</span>';
                      } else {
                        return '<span style="color:red;">失败</span>';
                      }
                    }
                  }])

              var st = new Ext.data.JsonStore({
                    fields : ['name', 'msg', 'success'],
                    proxy : new Ext.data.HttpProxy({
                          url : '../common/init4',
                          timeout : 300000
                        }),
                    root : 'datas',
                    totalProperty : 'totalCount'
                  });

              grid44 = new Ext.grid.GridPanel({
                    loadMask : {
                      msg : "正在初始化奖金核算系统..."
                    },
                    region : 'center',
                    store : st,

                    cm : cm,
                    viewConfig : {
                      forceFit : true
                    }
                  });

              win44 = new Ext.Window({
                    applyTo : 'hello44-win',
                    layout : 'fit',
                    width : 500,
                    height : 300,
                    // closeAction : 'hide',
                    closable : false,
                    plain : true,
                    items : grid44,

                    buttons : [{
                          text : '确定.',
                          handler : function() {
                            showMigrationBtn()
                            win44.hide();
                            // 刷新整个页面
                            // window.location.reload();
                          }
                        }]
                  });
            }
            grid44.getStore().load();
            win44.show(this);
          });

      // 预算管理
      var win5;
      var grid5;
      var button5 = Ext.get('init5-btn');
      button5.on('click', function() {
            // create the window on the first click and reuse on subsequent
            // clicks
            if (!win5) {
              var cm = new Ext.grid.ColumnModel([{
                    header : '信息',
                    dataIndex : 'msg',
                    width : 300
                  }, {
                    header : '状态',
                    dataIndex : 'success',
                    renderer : function num(val) {
                      if (val) {
                        return '<span style="color:green;">成功</span>';
                      } else {
                        return '<span style="color:red;">失败</span>';
                      }
                    }
                  }])

              var st = new Ext.data.JsonStore({
                    fields : ['name', 'msg', 'success'],
                    proxy : new Ext.data.HttpProxy({
                          url : '../common/init5',
                          timeout : 300000
                        }),
                    root : 'datas',
                    totalProperty : 'totalCount'
                  });

              grid5 = new Ext.grid.GridPanel({
                    loadMask : {
                      msg : "正在初始化预算管理系统..."
                    },
                    region : 'center',
                    store : st,

                    cm : cm,
                    viewConfig : {
                      forceFit : true
                    }
                  });

              win5 = new Ext.Window({
                    applyTo : 'hello44-win',
                    layout : 'fit',
                    width : 500,
                    height : 300,
                    // closeAction : 'hide',
                    closable : false,
                    plain : true,
                    items : grid5,

                    buttons : [{
                          text : '确定.',
                          handler : function() {
                            showMigrationBtn()
                            win5.hide();
                            // 刷新整个页面
                            // window.location.reload();
                          }
                        }]
                  });
            }
            grid5.getStore().load();
            win5.show(this);
          });

      // 后台脚本升级
      var win4;
      var grid4;
      var button4 = Ext.get('migration-btn');
      button4.on('click', function() {
            // create the window on the first click and reuse on subsequent
            // clicks
            if (!win4) {
              var cm = new Ext.grid.ColumnModel([{
                    header : '信息',
                    dataIndex : 'msg',
                    width : 300
                  }, {
                    header : '状态',
                    dataIndex : 'success',
                    renderer : function num(val) {
                      if (val) {
                        return '<span style="color:green;">成功</span>';
                      } else {
                        return '<span style="color:red;">失败</span>';
                      }
                    }
                  }])

              var st = new Ext.data.JsonStore({
                fields : ['name', 'msg', 'success'],
                proxy : new Ext.data.HttpProxy({
                      url : '../migration/process_migrations',
                      timeout : 300000
                    }),
                // url : '../migration/process_migrations',
                root : 'datas',
                totalProperty : 'totalCount'
                  // ,
                  // timeout : 3000000
                });

              grid4 = new Ext.grid.GridPanel({
                    loadMask : {
                      msg : "正在更新后台脚本..."
                    },
                    region : 'center',
                    store : st,

                    cm : cm,
                    viewConfig : {
                      forceFit : true
                    }
                  });

              win4 = new Ext.Window({
                    applyTo : 'hello4-win',
                    layout : 'fit',
                    width : 500,
                    height : 300,
                    // closeAction : 'hide',
                    closable : false,
                    plain : true,
                    items : grid4,

                    buttons : [{
                          text : '确定.',
                          handler : function() {
                            showMigrationBtn();
                            win4.hide();
                            // window.location.reload();
                          }
                        }]
                  });
            }
            grid4.getStore().load();
            win4.show(this);
          });

      function showMigrationBtn() {
        Ext.Ajax.request({
              url : "../migration/get_migrations",

              callback : function(o, s, r) {
                var respText = Ext.decode(r.responseText);
                if (respText.success) {
                  if (respText.count != 0) {
                    Ext.get('migration-btn').dom.disabled = false;
                  } else {
                    Ext.get('migration-btn').dom.disabled = true;
                  }

                }
              }
            });
      }

    })
