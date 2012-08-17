/**
 * 标准的网格+表单的页面用工具栏
 *
 * 必备配置项
 *
 * @config {String} grid 网格
 * @config {String} form 表单
 *
 * 可选配置项 *
 * @config {bool} hasBbar 是否显示状态条（默认为true，表示显示状态条）
 *
 */
Ext.namespace('Divo');
Ext.namespace('Divo.Base');
Divo.Base.Toolbar = Ext.extend(Ext.Toolbar, {
        grid : new Divo.Base.GridView(),
        form : new Divo.Base.EditForm(),
        initComponent : function() {

            Ext.apply(this, {
                    items : [{
                            icon : "resources/img/g_rec_src.png",
                            text : "过滤",
                            id : "tlb_FILTER",
                            handler : function() {
                                this.grid.toggleFilterBar();
                            }
                        }, {
                            icon : "resources/img/g_rec_commit.png",
                            text : "保存",
                            id : "tlb_SAVE",
                            handler : function() {
                                this.form.commitForm();
                            }
                        }, {
                            icon : "resources/img/g_rec_new.png",
                            text : "新增",
                            id : "tlb_NEW",
                            handler : function() {

                            }
                        }, {
                            icon : "resources/img/g_rec_del.png",
                            text : "删除",
                            id : "tlb_DELETE",
                            handler : function() {
                                this.grid.deleteRecord();
                            }
                        }, '-', {
                            icon : "resources/img/g_rec_upd.png",
                            text : "编辑",
                            id : "tlb_EDIT",
                            handler : function() {
                                this.form.toggleEditMode();
                                status = "editting";
                                setToolbarStatus();
                            }
                        }, {
                            icon : "resources/img/g_rec_refresh.gif",
                            text : "刷新",
                            id : "tlb_REFRESH",
                            handler : function() {
                                this.grid.getStore().reload();
                                status = "list";
                                setToolbarStatus();
                            }
                        }]
                });
            Divo.Base.Toolbar.superclass.initComponent.apply(this, arguments);

        }

    });

Ext.reg('Divo.Base.Toolbar', Divo.Base.Toolbar);
