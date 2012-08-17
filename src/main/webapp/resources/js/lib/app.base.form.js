/**
 * 表单类
 *
 * 必备配置项
 *
 * @config {String} formId 表单对应的div标识
 *
 */
Ext.namespace('Divo');
Ext.namespace('Divo.Base');

Divo.Base.EditForm = Ext.extend(Ext.form.FormPanel, {
  dataRecord : null,
  firstFocusFieldName : null,
  firstFocusFieldNameInsert : null,
  submitUrl : '',
  focusField : null,
  fields : new Ext.util.MixedCollection(),
  initComponent : function() {
    Ext.apply(this, arguments);
    this.header = false;
    this.hideBorders = true;
    Divo.Base.EditForm.superclass.initComponent.apply(this, arguments);
    this.addEvents('commitFormSuccess');
    this.updateLabelStyle();
    this.disableAllFields();
  },
  initEvents : function() {
    Divo.Base.EditForm.superclass.initEvents.call(this);
  },
  loadRecord : function(record) { //
    if (record != null && record != undefined) {
      this.dataRecord = record;
    }
    if (this.dataRecord != null) {
      this.getForm().loadRecord(this.dataRecord);
      if (this.dataRecord.get("_p_record_status") == "insert") {
        this.fields.eachKey(this.checkInsertAllowed, this);
        this.setDefaultFormFocus();
      } else if (this.dataRecord.get("_p_record_status") == "update") {
        this.fields.eachKey(this.checkUpdateAllowed, this);
        this.setDefaultFormFocus();
      } else {
        this.disableAllFields();
      }
    }
  },

  //更改必填字段样式
  updateLabelStyle : function() {
    this.fields.each(function(f){
      //f.add({bodyStyle : 'background:transparent;'});
      if (f.labelSeparator == ":*"){
            f.labelStyle= 'color:blue;';
      }
    },this);
  },

  disableAllFields : function() {
    this.fields.each(this.disable);
  },
  deleteRecord : function() {
    if (this.dataRecord.data._p_record_status == "insert") {
      this.fireEvent("deleteRecord",
          this.dataRecord.data._p_record_status);
    } else {
      this.fireEvent("deleteRecord",
          this.dataRecord.data._p_record_status);
    }
  },
  checkUpdateAllowed : function(key, item) {
    try {
      if (item.update_allowed) {
        this.getForm().findField(key).enable();
      } else {
        this.getForm().findField(key).disable();
      }
    } catch (e) {
    }
  },
  checkInsertAllowed : function(key, item) {
    try {
      if (item.insert_allowed) {
        this.getForm().findField(key).enable();
      } else {
        this.getForm().findField(key).disable();
      }
    } catch (e) {
    }
  },

  updateRecord : function() {
    this.getForm().updateRecord(this.dataRecord);
  },
  getFieldValue : function(fieldName) {
    return this.fields.get(fieldName).getValue();
  },
  setFieldValue : function(fieldName, newVal) {
    var oldVal = this.getForm().findField(fieldName).getValue();
    this.getForm().findField(fieldName).setValue(newVal);
    this.getForm().findField(fieldName).fireEvent('change',
        this.getForm().findField(fieldName), newVal, oldVal);
    if (this.fields.get(fieldName).xtype == 'numberfield')
      this.getField(fieldName).beforeBlur();
  },
  getField : function(fieldName) {
    return this.getForm().findField(fieldName);
  },
  getRecordStatus : function() {
    if (this.getForm().findField("_p_record_status").getValue() == "insert")
      return "insert";
    else
      return "update";
  },
  commitForm : function() {
    if (this.getForm().isValid()) {
      this.getForm().submit({
            url : this.submitUrl,
            scope : this,
            success : this.afterCommitSuccess,
            failure : this.afterCommitFailure,
            waitMsg : '保存数据...'
          });
    } else {
      Ext.Msg.alert('提示', '表单含有无效或空数据');
    }
  },
  afterCommitSuccess : function(form, action) {
    var op = "";
    if (this.getForm().findField("_p_record_status").getValue() == "insert") {
      op = "insert";
      this.getForm().findField("_p_record_status").setValue("");
      for (v in this.dataRecord.data) {
        try {
          this.getForm().findField(v).setValue(action.result.data[v]);
        } catch (e) {
        }
      }
    } else {
      op = "update";
      for (v in this.dataRecord.data) {
        try {
          this.getForm().findField(v).setValue(action.result.data[v]);
        } catch (e) {
        }
      }
    }
    this.updateRecord();

    this.fireEvent("commitFormSuccess", op, action.result.data);

    this.setDefaultFormFocus();
  },
  afterCommitFailure : function(form, action) {
    if (action.result.message == undefined)
      Ext.Msg.alert('错误', '网络连接失败.');
    else
      Ext.Msg.alert('错误', this.urldecode(action.result.message));
  },
  setDefaultFormFocus : function() {
    if (this.getRecordStatus() == "insert") {
      if (this.firstFocusFieldNameInsert != null) {
        this.getForm().findField(this.firstFocusFieldNameInsert).focus(
            false, 200);
      } else if (this.firstFocusFieldName != null) {
        this.getForm().findField(this.firstFocusFieldName).focus(false,
            200);
      }
    } else {
      if (this.firstFocusFieldName != null) {
        this.getForm().findField(this.firstFocusFieldName).focus(true,
            200);
      }
    }

  },
  createNewRecord : function() {
    this.dataRecord = this.newDataRecord();
    this.dataRecord.set("_p_record_status", "insert");
    // this.updateRecord();
    this.loadRecord(this.dataRecord);
    this.fields.each(this.copyFieldValuesFrom, this);
  },
  copyFieldValuesFrom : function(item, idx, len) {
    if (!Ext.isEmpty(item.copyValueFrom)) {
      this.dataRecord.set(item.dataIndex, Ext.getCmp(item.copyValueFrom)
              .getValue());
    }
  },
  urldecode : function(str) {
    var ret = str;
    ret = ret.replace(/\+/g, "%20");
    ret = decodeURIComponent(ret);
    ret = ret.toString();
    return ret;
  },
  toggleEditMode : function() {
    if (this.dataRecord.get("_p_record_status") == "update") {
      this.dataRecord.set("_p_record_status", "list")
    } else {
      this.dataRecord.set("_p_record_status", "update");
    }
    this.loadRecord(null);
  }
});

Ext.reg('Divo.Base.EditForm', Divo.Base.EditForm);
