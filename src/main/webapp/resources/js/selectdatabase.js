/**
 * 用户选择数据库窗口
 */
Ext.namespace('Divo');
Ext.namespace('Divo.app');
Divo.app.SelectDatabaseWin = function(userDBS, config) {
    this.userDBS = userDBS;
    Ext.apply(this, config);

    var recordDef = Ext.data.Record.create([{
            name : "dbcode",
            type : "string"
        }, {
            name : "dbname",
            type : "string"
        }]);

    var ds = new Ext.data.Store({
            proxy : new Ext.data.HttpProxy({
                    url : 'system/get_user_dbs'
                }),
            reader : new Ext.data.JsonReader({
                    root : 'data',
                    totalProperty : 'totalCount',
                    id : 'dbcode'
                }, recordDef)
        });
    ds.load();

    this.dbs = new Ext.form.ComboBox({
            id : 'id-dbs',
            fieldLabel : '',
            width : 180,
            msgTarget : 'under',
            triggerAction : 'all',
            displayField : 'dbname',
            valueField : 'dbcode',
            mode : 'remote',

            store : ds
        });

    // 需转换下,不然不能访问
    // http://www.haosoba.com/hi/so/?lxl_buat$_$459c1bf57adfca2cbc310984.html
    //var d = this.dbs;
    ds.on('load', function() {
            var defaultValue = "";
            Ext.Ajax.request({
                    url : "system/get_user_one_db",
                    async : false,
                    scope : this,
                    callback : function(o, s, r) {
                        var respText = Ext.decode(r.responseText);
                        if (respText.success) {
                            defaultValue = respText.data.dbcode;
                        }

                    }
                });

            Ext.getCmp("id-dbs").setValue(defaultValue);
        });

    this.form = new Ext.FormPanel({
            labelAlign : 'top',
            items : this.dbs,
            border : false,
            bodyStyle : 'background:transparent;padding:10px;'
        });

    Divo.app.SelectDatabaseWin.superclass.constructor.call(this, {
            title : "请选择操作对象",
            iconCls : 'icon-win',
            id : 'sel-user-dbs-win',
            autoHeight : true,
            width : 230,
            draggable : false,
            resizable : false,
            closable : false,
            plain : true,
            modal : true,
            y : 100,
            autoScroll : true,

            buttons : [{
                    text : '确定',
                    handler : this.selOk.createDelegate(this)
                }, {
                    text : '取消',
                    handler : this.onWinHide.createDelegate(this)
                }],

            items : this.form
        });

    this.addEvents('afterselect');
    this.addEvents('noselect');
}

Ext.extend(Divo.app.SelectDatabaseWin, Ext.Window, {
        render : function() {
            Divo.app.SelectDatabaseWin.superclass.render.apply(this, arguments);
        },

        initEvents : function() {
            Divo.app.SelectDatabaseWin.superclass.initEvents.call(this);
            this.on("show", this.onWinShow, this);
        },

        onWinShow : function() {

            if (this.rendered) {

            }

        },

        selOk : function() {
            var dbcode = this.dbs.getValue();

            if (!dbcode || dbcode.trim().length == 0) {
                this.fireEvent('noselect');
                this.close();
            }

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
                            this.fireEvent('afterselect', respText.dbname);
                        }
                        this.close();
                    }
                });
        },

        onWinHide : function() {
            this.fireEvent('noselect');
            this.close();
        }

    });

Ext.reg('Divo.app.SelectDatabaseWin', Divo.app.SelectDatabaseWin);
