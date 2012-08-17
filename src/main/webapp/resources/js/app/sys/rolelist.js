/**
 * 角色管理
 */
Ext.namespace('Divo');
Ext.namespace('Divo.app');

Divo.app.RoleForm = Ext.extend(Divo.Base.EditForm, {
        fields : new Ext.util.MixedCollection(),
        dataRecordMeta : Ext.data.Record.create([{
                name : "_p_record_status",
                type : "string"
            }, {
                name : "id",
                type : "float"
            }, {
                name : "name",
                type : "string"
            }, {
                name : "desc",
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
            this.fields.add("name", new Ext.form.TextField({
                        name : "name",
                        dataIndex : "name",
                        fieldLabel : "角色名称",
                        allowBlank : false,
                        labelSeparator : ":*",
                        width : 120,
                        insert_allowed : true,
                        update_allowed : true
                    }));

            this.fields.add("desc", new Ext.form.TextArea({
                        name : "desc",
                        dataIndex : "desc",
                        fieldLabel : "备注",
                        width : 120,
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
                            this.fields.get("desc")]
                    }],
                    bodyStyle : 'padding:10px 0 ;',
                    layout : "column",
                    defaults : {
                        labelWidth : 80
                    },
                    firstFocusFieldName : "name"
                });

            Divo.app.RoleForm.superclass.initComponent.apply(this, arguments);
        }

        ,
        onRender : function() {
            Divo.app.RoleForm.superclass.onRender.apply(this, arguments);
        },
        newDataRecord : function() {
            return new this.dataRecordMeta({
                    _p_record_status : "insert",
                    id : "",
                    name : "",
                    desc : ""
                });
        }

    });
Ext.reg("Divo.app.RoleForm", Divo.app.RoleForm);

Divo.app.RoleList = function() {
    /* ----------------------- private属性 ----------------------- */
    var gridEl = 'role-grid', treeEl = 'app-role-menu-tree'
    var canEdit = false, status = "list"; // 当前列表状态("list","insert","edit")
    var tree, root, isUserAction;
    var cRoleId;
    var grid, form, selModel, toolbar, win;
    /* ----------------------- private方法 ----------------------- */
    // 创建角色－资源(菜单)树
    function createRnRTree() {
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
                title : '角色权限',
                el : treeEl,
                animate : false,
                autoScroll : true,
                enableDD : false,
                containerScroll : true,
                lines : true,
                rootVisible : true,
                root : new Ext.tree.TreeNode({
                        id : 'all-system-resource-id',
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

    // 创建资源树节点
    function createTreeNodes() {
        // 取得全部模块
        Ext.Ajax.request({
                url : "system/get_all_sys_resources",
                scope : this,
                callback : function(o, s, r) {
                    var respText = Ext.decode(r.responseText);
                    if (respText.success) {
                        var resources = respText.data;
                        for (var i = 0; i < resources.length; i++) {
                            // 创建模块节点
                            var nodeOfModule = new Ext.tree.TreeNode({
                                    id : 'resource-' + resources[i].resourceId,
                                    text : resources[i].text,
                                    checked : false,
                                    resourceId : resources[i].resourceId,
                                    leaf : resources[i].leaf
                                });

                            if (resources[i].resourceType == '1') {
                                root.appendChild(nodeOfModule);
                            } else {
                                var parentNode = tree.getNodeById('resource-'
                                    + resources[i].parentId);
                                if (parentNode) {
                                    parentNode.appendChild(nodeOfModule);
                                }
                            }
                        }
                    }
                }
            });
    }

    // 创建列表
    function createGrid() {
        var queryFlds = new Array();
        var searchFlds = new Ext.util.MixedCollection();
        queryFlds.push('name');

        var fldName = new Ext.form.TextField({
                name : 'name',
                id : gridEl + '-name',
                fieldLabel : '角色名称',
                allowBlank : true,
                width : 100
            });
        searchFlds.add('name', fldName);

        grid = new Divo.Base.GridView({
                autoSelectFirstRow : false,
                gridId : gridEl,
                packageName : 'sys',
                entityName : 'Role',
                queryFlds : queryFlds,
                searchFlds : searchFlds,
                pageSize : 15,
                queryUrl : 'system/get_sys_roles',
                deleteUrl : 'system/delete_sys_role',
                jsonId : 'id',
                recordPk : ["id"]
            });

        selModel = grid.getSelectionModel();
        selModel.on('rowselect', onRowSelect);
    }

    // 选择某行
    function onRowSelect(selModel, rowIndex, record) {

        var data = record.data;
        form.loadRecord(new form.dataRecordMeta({
                _p_record_status : "list",
                id : data.id,
                name : data.name,
                desc : data.desc
            }));
        canEdit = data.name == 'admin' ? false : true;
        status = "list";
        setToolbarStatus();
        cRoleId = data.id;
        tree.setTitle('角色:' + data.name + '的权限');
        initCurRoleResource();
    }

    // 当前角色改变,初始化其相应的角色权限
    function initCurRoleResource() {
        // 取得当前角色的全部模块
        if (cRoleId) {
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
                    url : "system/get_all_role_resources",
                    method : 'POST',
                    scope : this,
                    params : {
                        roleId : cRoleId
                    },
                    callback : function(o, s, r) {
                        var respText = Ext.decode(r.responseText);
                        if (respText.success) {
                            var resources = respText.data;

                            // 打上复选框
                            root.cascade(function(n) {
                                    var data = n.attributes
                                    var resourceId = data.resourceId
                                    if (data.leaf) {
                                        for (var j = 0; j < resources.length; j++) {
                                            if (resources[j].resourceId == resourceId) {
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

    // 保存角色权限
    function saveCheckChange() {

        var menuIds = [];
        root.cascade(function(n) {
                var menuId = n.attributes.resourceId;
                if (menuId && n.getUI().isChecked())
                    menuIds.push(menuId);
            });

        tree.body.mask('正在保存...', 'x-mask-loading');

        Ext.Ajax.request({
                url : "system/save_role_resources",
                method : 'POST',
                scope : this,
                params : {
                    roleId : cRoleId,
                    menuIds : menuIds.join(',')
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
        form = new Divo.app.RoleForm({
                submitUrl : 'system/save_sys_role',
                layout : 'fit',
                region : 'east',
                border : true,
                split : true,
                width : 300,
                minSize : 220,
                maxSize : 300
            });

        form.on('commitFormSuccess', afterCommitFormSuccess);
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
                        icon : "resources/img/g_rec_del.png",
                        tooltip : "删除记录 &lt;Ctrl+D&gt;",
                        id : "tlb_DELETE",
                        handler : deleteRecord
                    }, '-', {
                        xtype : "button",
                        cls : "x-btn-icon",
                        icon : "resources/img/g_rec_upd.png",
                        tooltip : "编辑&lt;Enter&gt;, 列表&lt;Ctrl+Q&gt;",
                        id : "tlb_EDIT",
                        handler : toggleEditMode
                    }, '->', '<img src="resources/img/search.gif"/>&nbsp;', {
                        xtype : "textfield",
                        id : 'tlb_FILTER_TEXT',
                        width : 140,
                        selectOnFocus : true,
                        emptyText : '输入名称并按回车键',
                        listeners : {
                            "specialkey" : function(o, e) {
                                if (e.getKey() === e.ENTER) {
                                    grid.executeQueryBy('name', o.getValue());
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
                        items : [grid]
                    }, tree]
            });

        viewport.doLayout();

    }

    function createWindow() {
        if (win) {
            win.show();
            return;
        }

        win = new Ext.Window({
                id : 'role-win',
                title : '角色维护',
                iconCls : 'icon-win',
                autoHeight : true,
                width : 300,
                layout : 'form',
                plain : false,
                buttonAlign : 'right',
                closable : false,
                modal : true,
                items : [form]
            });
        var okHideBtn = win.addButton({
                text : '保存'
            }, function() {
                commitForm();
            }, this);
        var cancelBtn = win.addButton({
                text : '关闭'
            }, function() {
                grid.getStore().reload();
                setToolbarStatus();
                win.hide()
            }, this);
        win.show();
    }

    // 切换过滤查询工具条状态
    function toggleFilterBar() {
        grid.toggleFilterBar();
    }

    // 新建或修改表单成功提交后的处理
    function afterCommitFormSuccess(operation, recordData) {
        // cRoleId = recordData.id;
        selModel.clearSelections();
        grid.reloadRecords();
        win.hide();
    }

    // 创建新的记录，初始化表单
    function createNewRecord() {
        form.createNewRecord();
        createWindow();
        status = "editting";
        setToolbarStatus();
    }

    // 删除记录
    function deleteRecord() {
        grid.deleteRecord();
    }

    // 切换记录编辑状态
    function toggleEditMode() {
        form.toggleEditMode();
        createWindow();
        status = "editting";
        setToolbarStatus();
    }

    // 提交表单，保存新建或修改的信息
    function commitForm() {
        form.commitForm();
    }

    // 加载数据完成后
    function onLoad(store, records, options) {
        status = "list";
        setToolbarStatus();
    }

    // 设置工具条状态
    function setToolbarStatus() {
        if (status == "list") {
            toolbar.items.get("tlb_NEW").enable();
            if (canEdit) {
                toolbar.items.get("tlb_DELETE").enable();
                toolbar.items.get("tlb_EDIT").enable();
            } else {
                toolbar.items.get("tlb_DELETE").disable();
                toolbar.items.get("tlb_EDIT").disable();
            }
        } else {
            toolbar.items.get("tlb_NEW").disable();
            toolbar.items.get("tlb_DELETE").disable();
            toolbar.items.get("tlb_EDIT").disable();
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
            createRnRTree();
            createLayout();
            grid.load();
            grid.getStore().on("load", onLoad);
        }
    }
}();

Ext.onReady(Divo.app.RoleList.init, Divo.app.RoleList, true);
