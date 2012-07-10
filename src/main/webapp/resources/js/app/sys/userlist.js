/**
 * 用户管理
 */
Ext.namespace('Divo');
Ext.namespace('Divo.app');

Divo.app.UserForm = Ext.extend(Divo.Base.EditForm, {
      dbArray : null,
      fields : new Ext.util.MixedCollection(),
      dataRecordMeta : Ext.data.Record.create([{
            name : "_p_record_status",
            type : "string"
          }, {
            name : "id",
            type : "float"
          }, {
            name : "loginName",
            type : "string"
          }, {
            name : "password",
            type : "string"
          }, {
            name : "name",
            type : "string"
          }, {
            name : "email",
            type : "string"
          }, {
            name : "status",
            type : "string"
          }, {
            name : "version",
            type : "string"
          }, {
            name : "dbCodes",
            type : "string"
          }]),
      initComponent : function() {

        this.fields.add("_p_record_status", new Ext.form.Hidden({
                  name : "_p_record_status"
                }));

        this.fields.add("id", new Ext.form.Hidden({
                  name : "id",
                  dataIndex : "id",
                  insert_allowed : true,
                  update_allowed : true
                }));

        this.fields.add("loginName", new Ext.form.TextField({
                  name : "loginName",
                  dataIndex : "loginName",
                  fieldLabel : "登陆名",
                  allowBlank : false,
                  labelSeparator : ":*",
                  width : 120,
                  insert_allowed : true,
                  update_allowed : true
                }));

        this.fields.add("password", new Ext.form.Hidden({
                  name : "password",
                  dataIndex : "password",
                  insert_allowed : true,
                  update_allowed : true
                }));

        this.fields.add("name", new Ext.form.TextField({
                  name : "name",
                  dataIndex : "name",
                  fieldLabel : "姓名",
                  width : 120,
                  insert_allowed : true,
                  update_allowed : true
                }));

        this.fields.add("email", new Ext.form.TextField({
                  name : "email",
                  dataIndex : "email",
                  fieldLabel : "邮箱",
                  width : 120,
                  insert_allowed : true,
                  update_allowed : true
                }));

        this.fields.add("status", new Ext.form.Hidden({
                  name : "status",
                  dataIndex : "status",
                  insert_allowed : true,
                  update_allowed : true
                }));

        this.fields.add("version", new Ext.form.Hidden({
                  name : "version",
                  dataIndex : "version",
                  insert_allowed : true,
                  update_allowed : true
                }));

        var fs = new Ext.form.FieldSet({
              xtype : 'fieldset',
              id : 'FIELDSET',
              name : 'fieldset',
              style : 'padding:2px 2px 2px 2px',
              title : '可用数据库',
              autoHeight : true,
              width : 240
            });

        var joinArr = this.dbArray;
        var len = joinArr.length;
        var objArr = new Array(len);
        for (var i = 0; i < len; i++) {
          var control = new Ext.form.Checkbox({
                name : joinArr[i].dbname,
                inputValue : joinArr[i].dbcode,
                boxLabel : joinArr[i].dbname
              });
          objArr[i] = control;
        }

        for (var i = 0; i < objArr.length; i++) {
          fs.add(objArr[i]);
        }

        this.fields.add("fs", fs);

        this.fields.add("dbCodes", new Ext.form.Hidden({
                  name : "dbCodes",
                  dataIndex : "dbCodes",
                  insert_allowed : true,
                  update_allowed : true
                }));

        Ext.apply(this, {
              items : [this.fields.get("_p_record_status"), {
                layout : "form",
                columnWidth : 1,
                labelAlign : "right",
                labelWidth : 80,
                items : [this.fields.get("id"), this.fields.get("name"),
                    this.fields.get("loginName"), this.fields.get("password"),
                    this.fields.get("email"), this.fields.get("fs"),
                    this.fields.get("version"), this.fields.get("status"),
                    this.fields.get("dbCodes")]
              }],
              bodyStyle : 'padding:10px 0 ;',
              layout : "column",
              defaults : {
                labelWidth : 80
              },
              firstFocusFieldName : "name"
            });

        Divo.app.UserForm.superclass.initComponent.apply(this, arguments);
      }

      ,
      onRender : function() {
        Divo.app.UserForm.superclass.onRender.apply(this, arguments);
      },
      newDataRecord : function() {
        return new this.dataRecordMeta({
              _p_record_status : "insert",
              id : "",
              loginName : "",
              password : "",
              name : "",
              email : "",
              status : "",
              version : "",
              dbCodes : ""
            });
      }

    });
Ext.reg("Divo.app.UserForm", Divo.app.UserForm);

Divo.app.UserList = function() {
  /* ----------------------- private属性 ----------------------- */
  var gridEl = 'user-grid', treeEl = 'app-user-role-tree'
  var canEdit = false, status = "list"; // 当前列表状态("list","insert","edit")
  var tree, root, isUserAction;
  var cUserId;
  var grid, form, selModel, toolbar;
  var logGrid;

  /* ----------------------- private方法 ----------------------- */

  // 创建用户--角色树
  function createUnRTree() {
    if (!Ext.get(treeEl))
      Ext.DomHelper.append(document.body, {
            html : '<div id="' + treeEl + '"></div>'
          });

    tree = new Ext.tree.TreePanel({
          layout : 'fit',
          region : 'east',
          collapsible : true,
          collapsedTitle : true,
          border : true,
          split : false,
          width : 220,
          el : treeEl,
          animate : false,
          autoScroll : true,
          enableDD : false,
          containerScroll : true,
          lines : true,
          rootVisible : true,
          title : '所属角色',
          root : new Ext.tree.TreeNode({
                id : 'all-system-roles-tree-id',
                text : "全部",
                checked : false,
                leaf : false
              })
        });
    if (Ext.isIE) {
      tree.animate = false;
      tree.animCollapse = false;
    }

    root = tree.root;
    tree.on("checkchange", onCheckChange);
  }

  // 创建角色树节点
  function createTreeNodes() {
    // 取得全部角色
    Ext.Ajax.request({
          url : "system/get_all_sys_roles",
          method : 'POST',
          scope : this,
          callback : function(o, s, r) {
            var respText = Ext.decode(r.responseText);
            if (respText.success) {
              var resources = respText.data;
              for (var i = 0; i < resources.length; i++) {
                // 创建模块节点
                var nodeOfModule = new Ext.tree.TreeNode({
                      id : 'sys-role-' + resources[i].id,
                      text : resources[i].name,
                      checked : false,
                      roleId : resources[i].id,
                      leaf : true
                    });

                root.appendChild(nodeOfModule);
              }
            }
          }
        });
  }

  // 创建列表
  function createGrid() {
    var queryFlds = new Array();
    var searchFlds = new Ext.util.MixedCollection();
    queryFlds.push('loginName');
    var fldName = new Ext.form.TextField({
          name : 'loginName',
          id : gridEl + '-loginName',
          fieldLabel : '用户名',
          allowBlank : true,
          width : 100
        });
    searchFlds.add('loginName', fldName);
    grid = new Divo.Base.GridView({
          autoSelectFirstRow : false,
          gridId : gridEl,
          packageName : 'sys',
          entityName : 'User',
          queryFlds : queryFlds,
          searchFlds : searchFlds,

          autoFit : false,
          queryUrl : 'system/get_sys_users',
          deleteUrl : 'system/delete_sys_user',
          jsonId : 'id',
          recordPk : ["id"]
        });
    selModel = grid.getSelectionModel();
    selModel.on('rowselect', onRowSelect);
    var cm = grid.getColumnModel();
    var colStatus = cm.findColumnIndex('status');
    cm.setRenderer(colStatus, function(value, p, record) {
          return value == 'enabled' ? '启用' : '停用';
        });

    var ds = new Ext.data.Store({
          proxy : new Ext.data.HttpProxy({
                url : "system/query_user_log"
              }),
          reader : new Ext.data.JsonReader({
                root : 'rows',
                totalProperty : 'totalCount'
              }, Ext.data.Record.create([{
                    name : 'id',
                    type : 'float'
                  }, {
                    name : 'userId',
                    type : 'float'
                  }, {
                    name : 'version',
                    type : 'float'
                  }, {
                    name : 'netIp',
                    type : 'string'
                  }, {
                    name : 'types',
                    type : 'string'
                  }, {
                    name : 'createTime',
                    type : 'string'
                  }, {
                    name : 'events',
                    type : 'string'
                  }])),
          remoteSort : false
        });
    logGrid = new Ext.grid.GridPanel({
          store : ds,
          region : 'center',
          pageSize : 10,
          bbar : new Ext.PagingToolbar({
                pageSize : 10,
                store : ds,
                displayInfo : true,
                displayMsg : '显示: {0} - {1} 共 {2} 条',
                emptyMsg : "没有数据"
              }),
          loadMask : {
            msg : "正在加载数据..."
          },
          cm : new Ext.grid.ColumnModel([{
                header : 'IP地址',
                dataIndex : 'netIp',
                width : 120
              }, {
                header : '操作日期',
                dataIndex : 'createTime',
                width : 150
              }, {
                header : '来源',
                dataIndex : 'types',
                width : 50
              }, {
                header : '操作',
                dataIndex : 'events',
                width : 200
              }])
        });

    logGrid.getStore().on('beforeload', function(store, options) {
          store.baseParams.userId = cUserId;
        }, this, true);

  }

  // 选择某行
  function onRowSelect(selModel, rowIndex, record) {
    var data = record.data;

    form.loadRecord(new form.dataRecordMeta({
          _p_record_status : "list",
          id : data.id,
          name : data.name,
          loginName : data.loginName,
          password : data.password,
          email : data.email,
          status : data.status,
          version : data.version,
          dbCodes : ""
        }));

    initFieldSet(data.id);
    canEdit = true;
    status = "list";
    setToolbarStatus();
    cUserId = data.id;
    tree.setTitle(data.name + '&nbsp;&nbsp;所拥有的角色');
    initCurUserRole();

    logGrid.getStore().reload({
          scope : this,
          params : {
            start : 0,
            limit : 10
          }
        });

  }

  // 勾选用户已经配置的数据库
  function initFieldSet(userId) {
    Ext.Ajax.request({
          url : "system/getseldb",
          params : {
            userId : userId
          },
          method : 'POST',
          async : false,
          scope : this,
          callback : function(o, s, r) {
            var respText = Ext.decode(r.responseText);
            if (respText.success) {
              var data = respText.data;
              for (var i = 0; i < data.length; i++) {
                if (form.getForm().findField(data[i].dbname)) {
                  if (data[i].sel) {
                    form.getForm().findField(data[i].dbname).setValue(true);
                  } else {
                    form.getForm().findField(data[i].dbname).setValue(false);
                  }
                }
              }
            }
          }
        });
  }

  // 当前用户改变,初始化其相应的角色权限
  function initCurUserRole() {
    // 取得当前用户的全部角色
    if (cUserId) {

      isUserAction = false;
      // 清除所有复选框
      root.cascade(function(n) {
            if (n.getUI().isChecked()) {
              n.getUI().toggleCheck();
              n.attributes.checked = n.getUI().isChecked();
            }
          });

      root.expand(true);
      root.collapseChildNodes(true); // 使只展开第2级

      Ext.Ajax.request({
            url : "system/get_roleids_by_userid",
            method : 'POST',
            scope : this,
            params : {
              userId : cUserId
            },
            callback : function(o, s, r) {
              var respText = Ext.decode(r.responseText);
              if (respText.success) {
                var resources = respText.data;

                // 打上复选框
                root.cascade(function(n) {
                      var data = n.attributes
                      var roleId = data.roleId
                      if (data.leaf) {

                        for (var j = 0; j < resources.length; j++) {
                          if (resources[j] == roleId) {
                            n.getUI().toggleCheck();
                            n.attributes.checked = n.getUI().isChecked();
                            onCheckChange(n, true);
                            break;
                          }
                        };
                      }
                    });

                isUserAction = true;
              }
            }
          });

    }
  }

  // 复选框点击事件处理
  function onCheckChange(node, c) {
    var toggleCheck = function(o) {
      if (isUserAction) {
        isUserAction = false;
        o.getUI().toggleCheck();
        o.attributes.checked = o.getUI().isChecked();
        isUserAction = true;
      } else {
        o.getUI().toggleCheck();
        o.attributes.checked = o.getUI().isChecked();
      }
    };
    // 设置所有下级节点
    if (isUserAction) {
      node.cascade(function(n) {
            if (n.isLeaf()) {
              if (n.getUI().isChecked() != c)
                toggleCheck(n);
            }
          });

    }

    // 设置所有上级节点
    node.bubble(function(n) {
          var chkCnt = 0;
          var node = n.parentNode;
          if (node) {
            node.eachChild(function(n) {
                  if (n.getUI().isChecked())
                    chkCnt += 1;
                }) // 判断下级节点有几个已选
            if (chkCnt == node.childNodes.length) {// all
              if (!node.getUI().isChecked())
                toggleCheck(node);
            } else if (!chkCnt) {// zero
              node.attributes.checked = !node.getUI().isChecked();
              if (node.getUI().isChecked())
                toggleCheck(node);
            } else {// set partial
              if (!node.getUI().isChecked())
                toggleCheck(node);
            }
          } // 如果有上级节点
        })

    if (isUserAction)
      saveCheckChange();
  }

  // 保存用户的权限
  function saveCheckChange() {
    var roleIds = [];
    root.cascade(function(n) {
          var roleId = n.attributes.roleId;
          if (roleId && n.getUI().isChecked())
            roleIds.push(roleId);
        });

    tree.body.mask('正在保存...', 'x-mask-loading');

    Ext.Ajax.request({
          url : "system/save_user_roles",
          method : 'POST',
          scope : this,
          params : {
            userId : cUserId,
            roleIds : roleIds.join(",")
          },
          callback : function(o, s, r) {
            tree.body.unmask();

            var respText = Ext.decode(r.responseText);
            if (!respText.success) {
              if (!Ext.isEmpty(respText.message)) {
                Ext.Msg.alert('错误', respText.message);
              } else {
                Ext.Msg.alert('错误', '网络链接错误。');
              }
            }
          }
        });
  }

  // 创建form表单
  function createForm() {
    Ext.Ajax.request({
          url : "system/getalldb",
          method : 'POST',
          async : false,
          scope : this,
          callback : function(o, s, r) {
            var respText = Ext.decode(r.responseText);
            form = new Divo.app.UserForm({
                  dbArray : respText.data,
                  layout : 'fit',
                  region : 'center',
                  border : true,
                  split : true,
                  width : 260,
                  minSize : 220,
                  maxSize : 300,
                  submitUrl : 'system/save_sys_user'
                });
            form.on('commitFormSuccess', afterCommitFormSuccess);
          }
        });
  }

  // 取出所有数据库的选中状态
  function clearSel() {
    Ext.Ajax.request({
          url : "system/getalldb",
          method : 'POST',
          async : false,
          scope : this,
          callback : function(o, s, r) {
            var respText = Ext.decode(r.responseText);
            if (respText.success) {
              var data = respText.data;
              for (var i = 0; i < data.length; i++) {
                form.getForm().findField(data[i].dbname).setValue(false);
              }
            }
          }
        });
  }

  // 新建或修改表单成功提交后的处理
  function afterCommitFormSuccess(operation, recordData) {
    grid.reloadRecords();
    status = "list";
    canEdit = false;
    setToolbarStatus();
    Ext.getCmp('FIELDSET').disable();
    form.getForm().findField('loginName').disable();
    form.getForm().findField('name').disable();
    form.getForm().findField('email').disable();
    form.getForm().reset();
    clearSel();
  }

  // 创建layout
  function createLayout() {
    toolbar = new Ext.Toolbar({
          items : [{
                xtype : "button",
                cls : "x-btn-icon",
                icon : "resources/img/g_rec_src.png",
                tooltip : "过滤数据",
                id : "tlb_FILTER",
                handler : toggleFilterBar
              }, '-', {
                xtype : "button",
                cls : "x-btn-icon",
                icon : "resources/img/g_rec_commit.png",
                tooltip : "保存修改 &lt;Ctrl+S&gt;",
                id : "tlb_SAVE",
                handler : commitForm
              }, {
                xtype : "button",
                cls : "x-btn-icon",
                icon : "resources/img/g_rec_new.png",
                tooltip : "新建记录 &lt;Ctrl+N&gt;",
                id : "tlb_NEW",
                handler : createNewRecord
              }, {
                xtype : "button",
                cls : "x-btn-icon",
                icon : "resources/img/restore.gif",
                tooltip : "重置密码 &lt;Ctrl+D&gt;",
                id : "tlb_RESET",
                handler : resetPassword
              }, '-', {
                xtype : "button",
                cls : "x-btn-icon",
                id : "tlb_EDIT",
                icon : "resources/img/g_rec_upd.png",
                tooltip : "编辑&lt;Enter&gt;, 列表&lt;Ctrl+Q&gt;",
                handler : toggleEditMode
              }, {
                xtype : "button",
                cls : "x-btn-icon",
                icon : "resources/img/active.gif",
                tooltip : "启用 &lt;Ctrl+D&gt;",
                id : "tlb_START",
                handler : startUser
              }, {
                xtype : "button",
                cls : "x-btn-icon",
                icon : "resources/img/negative.gif",
                tooltip : "停用 &lt;Ctrl+D&gt;",
                id : "tlb_STOP",
                handler : stopUser
              }, '-', {
                xtype : "button",
                cls : "x-btn-icon",
                icon : "resources/img/negative.gif",
                tooltip : "同步his用户",
                id : "tlb_IMPORTHISUSER",
                handler : importHisUser
              }, '->', '<img src="resources/img/search.gif"/>&nbsp;', {
                xtype : "textfield",
                id : 'tlb_FILTER_TEXT',
                width : 140,
                selectOnFocus : true,
                emptyText : '输入用户名并按回车键',
                listeners : {
                  "specialkey" : function(o, e) {
                    if (e.getKey() === e.ENTER) {
                      grid.executeQueryBy('loginName', o.getValue());
                    }
                  }
                }
              }]
        });

    var viewport = new Ext.Viewport({
          layout : 'border',
          listeners : {
            render : function() {
              createTreeNodes();
            }
          },
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
                items : [{
                      layout : 'fit',
                      region : 'center',
                      border : false,
                      items : [grid]
                    }, {
                      layout : 'border',
                      region : 'east',
                      border : false,
                      width : 500,
                      items : [{
                            layout : 'border',
                            region : 'center',
                            border : false,
                            items : [form, tree]
                          }, {
                            layout : 'fit',
                            region : 'south',
                            height : 300,
                            border : false,
                            items : [logGrid]
                          }]
                    }]
              }]
        });

    viewport.doLayout();
  }

  // 切换过滤查询工具条状态
  function toggleFilterBar() {
    grid.toggleFilterBar();
  }

  // 创建新的记录，初始化表单
  function createNewRecord() {
    form.createNewRecord();
    status = "editting";
    setToolbarStatus();
    Ext.getCmp('FIELDSET').enable();
    clearSel();
    form.getForm().findField('loginName').setReadOnly(false);
    form.getForm().findField('name').setReadOnly(false);
    form.getForm().findField('email').setReadOnly(false);
  }

  // 删除记录
  function deleteRecord() {
    grid.deleteRecord();
  }

  // 切换记录编辑状态
  function toggleEditMode() {
    form.toggleEditMode();
    status = "editting";
    setToolbarStatus();
    Ext.getCmp('FIELDSET').enable();

    if (selModel.getSelected().data.loginName == 'Admin') {
      form.getForm().findField('loginName').setReadOnly(true);
      form.getForm().findField('name').setReadOnly(true);
      form.getForm().findField('email').setReadOnly(true);
    } else {
      form.getForm().findField('loginName').setReadOnly(false);
      form.getForm().findField('name').setReadOnly(false);
      form.getForm().findField('email').setReadOnly(false);
    }
  }

  // 提交表单，保存新建或修改的信息
  function commitForm() {

    Ext.Ajax.request({
          url : "system/getalldb",
          method : 'POST',
          async : false,
          scope : this,
          callback : function(o, s, r) {
            var respText = Ext.decode(r.responseText);
            var dbCodes = '';
            if (respText.success) {
              var data = respText.data;
              for (var i = 0; i < data.length; i++) {
                if (form.getForm().findField(data[i].dbname)) {
                  if (form.getForm().findField(data[i].dbname).getValue()) {
                    dbCodes += form.getForm().findField(data[i].dbname).inputValue;
                    dbCodes += "|";
                  }
                }
              }
            }

            form.getForm().findField('dbCodes').setValue(dbCodes);
            form.commitForm();
          }
        });

  }

  // 重置用户密码
  function resetPassword() {

    if (!selModel) {
      Ext.Msg.alert('提示', '请先选择用户再重设');
      return;
    }

    Ext.Ajax.request({
          url : 'system/reset_user_password',
          params : {
            "userId" : selModel.getSelections()[0].data.id
          },
          success : function() {
            Ext.Msg.alert('提示', '密码重置成功');
          }
        });
  }

  // 启用用户
  function startUser() {

    if (!selModel) {
      Ext.Msg.alert('提示', '请先选择用户再启用');
      return;
    }

    Ext.Ajax.request({
          url : 'system/start_user',
          params : {
            "userId" : selModel.getSelections()[0].data.id
          },
          success : function() {
            grid.reloadRecords();
          }
        });
  }

  // 停用用户
  function stopUser() {
    if (!selModel) {
      Ext.Msg.alert('提示', '请先选择用户再停用');
      return;
    }

    Ext.Ajax.request({
          url : 'system/stop_user',
          params : {
            "userId" : selModel.getSelections()[0].data.id
          },
          success : function() {
            grid.reloadRecords();
          }
        });
  }

  // his用户
  function importHisUser() {
    grid.body.mask('正在处理...', 'x-mask-loading');
    Ext.Ajax.request({
          url : 'system/import_his_user',
          callback : function(o, s, r) {
            grid.body.unmask();
            var respText = Ext.decode(r.responseText);
            if (respText.success) {
              grid.body.unmask();
              grid.reloadRecords();
            } else {

            }
          }
        });
  }
  // 加载数据完成后
  function onLoad(store, records, options) {
    status = "list";
    setToolbarStatus();
  }

  // 设置工具条状态
  function setToolbarStatus() {
    if (status == "list") {
      toolbar.items.get("tlb_SAVE").disable();
      toolbar.items.get("tlb_NEW").enable();
      toolbar.items.get("tlb_IMPORTHISUSER").enable();
      if (canEdit) {
        toolbar.items.get("tlb_RESET").enable();
        toolbar.items.get("tlb_EDIT").enable();
        toolbar.items.get("tlb_START").enable();
        toolbar.items.get("tlb_STOP").enable();
      } else {
        toolbar.items.get("tlb_RESET").disable();
        toolbar.items.get("tlb_EDIT").disable();
        toolbar.items.get("tlb_START").disable();
        toolbar.items.get("tlb_STOP").disable();
      }
    } else {
      toolbar.items.get("tlb_SAVE").enable();
      toolbar.items.get("tlb_NEW").disable();
      toolbar.items.get("tlb_RESET").disable();
      toolbar.items.get("tlb_EDIT").disable();
      toolbar.items.get("tlb_START").disable();
      toolbar.items.get("tlb_STOP").disable();
      toolbar.items.get("tlb_IMPORTHISUSER").disable();
    }
  }

  /* ----------------------- public方法 ----------------------- */
  return {
    /**
     * 初始化
     */
    init : function() {
      createGrid();
      createForm();
      createUnRTree();
      createLayout();
      grid.load();
      grid.getStore().on("load", onLoad);
    }
  }
}();

Ext.onReady(Divo.app.UserList.init, Divo.app.UserList, true);