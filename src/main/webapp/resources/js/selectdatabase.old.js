/**
 * 用户选择数据库窗口
 */
Ext.namespace('Divo');
Ext.namespace('Divo.app');
Divo.app.SelectDatabaseWin = Ext.extend(Ext.Window, {
      userDBs : null,
      dataForm : null,
      fieldSet : null,
      radios : null,
      initComponent : function() {
        this.radios = this.getRadios();
        this.fieldSet = this.getFieldSet();
        this.dataForm = this.getForm();
        Ext.apply(this, {
              title : "用户选择数据库窗口",
              iconCls : 'icon-win',
              autoHeight : false,
              height : 260,
              width : 280,
              layout : 'border',
              plain : false,
              buttonAlign : 'right',
              closable : false,
              draggable : false,
              modal : true,
              items : [{
                    border : false,
                    layout : 'fit',
                    region : 'center',
                    items : [this.dataForm]
                  }],
              buttons : [{
                    text : '确定',
                    handler : this.selOk.createDelegate(this)
                  }, {
                    text : '取消',
                    handler : this.onWinHide.createDelegate(this)
                  }]
            });

        Divo.app.SelectDatabaseWin.superclass.initComponent.apply(this,
            arguments);
        this.addEvents('afterselect');
        this.addEvents('noselect');
      },

      render : function() {
        Divo.app.SelectDatabaseWin.superclass.render.apply(this, arguments);
      },

      initEvents : function() {
        Divo.app.SelectDatabaseWin.superclass.initEvents.call(this);
        this.on("show", this.onWinShow, this);
      },

      onWinShow : function() {

      },

      selOk : function() {
        var me = this;
        var dbcode;
        for (var j = 0; j < this.radios.length; j++) {
          item = this.radios[j];
          if (item.getXType() == 'radio' && item.getValue()) {
            dbcode = item.inputValue;
            break;
          }
        }

        if (!dbcode || dbcode.trim().length == 0) {
          this.fireEvent('noselect');
          this.close();
        }

        Ext.Ajax.request({
              url : "system/directupdate",
              method : 'POST',
              params : {
                dbcode : dbcode
              },
              async : false,
              scope : this,
              callback : function(o, s, r) {
                var respText = Ext.decode(r.responseText);
                if (respText.success) {
                  me.fireEvent('afterselect', respText.dbname);
                }
                this.close();
              }
            });
      },

      onWinHide : function() {
        this.fireEvent('noselect');
        this.close();
      },

      getRadios : function() {
        var joinArr = this.userDBs;
        var len = joinArr.length;
        var objArr = new Array(len);
        for (var i = 0; i < len; i++) {
          var control = new Ext.form.Radio({
                name : 'dbname',
                inputValue : joinArr[i].dbcode,
                boxLabel : joinArr[i].dbname
              });
          objArr[i] = control;
        }
        return objArr;
      },

      getFieldSet : function() {

        var fs = new Ext.form.FieldSet({
              xtype : 'fieldset',
              id : 'FIELDSET',
              name : 'fieldset',
              style : 'padding:2px 2px 2px 2px',
              title : '可用数据库',
              autoHeight : true,
              width : 240,
              items : [this.radios]
            });

        return fs;
      },

      getForm : function() {

        var dataPanel = new Ext.form.FormPanel({
              style : 'padding:0px 0px 0px 0px',
              height : "100%",
              frame : true,
              labelWidth : 100,
              labelAlign : "right",
              layout : "form",
              name : "AUTOPANEL",
              width : "100%",
              items : [this.fieldSet]
            });
        return dataPanel;
      }

    });

Ext.reg('Divo.app.SelectDatabaseWin', Divo.app.SelectDatabaseWin);
