/**
 * 生命体征指标管理
 */
Ext.namespace('Divo');
Ext.namespace('Divo.app');

Divo.app.VitalSignItemForm = Ext.extend(Divo.Base.EditForm, {
      fields : new Ext.util.MixedCollection(),
      dataRecordMeta : Ext.data.Record.create([{
            name : "_p_record_status",
            type : "string"
          }, {
            name : "id",
            type : "float"
          }, {
            name : "code",
            type : "string"
          }, {
            name : "name",
            type : "string"
          }, {
            name : "unit",
            type : "string"
          }, {
            name : "typeCode",
            type : "string"
          }]),
      initComponent : function() {

        var tn = 1;
        this.fields.add("_p_record_status", new Ext.form.Hidden({
                  name : "_p_record_status"
                }));
        this.fields.add("id", new Ext.form.Hidden({
                  name : "id",
                  dataIndex : "id",
                  insert_allowed : true,
                  update_allowed : true
                }));
        this.fields.add("code", new app.base.TextField({
                  name : "code",
                  dataIndex : "code",
                  fieldLabel : "编号",
                  allowBlank : true,
                  tabIndex : tn++,
                  width : 120,
                  insert_allowed : true,
                  update_allowed : true
                }));
        this.fields.add("name", new app.base.TextField({
                  name : "name",
                  dataIndex : "name",
                  fieldLabel : "生命体征指标",
                  allowBlank : false,
                  labelSeparator : ":*",
                  tabIndex : tn++,
                  width : 120,
                  insert_allowed : true,
                  update_allowed : true
                }));
        this.fields.add("unit", new app.base.TextField({
                  name : "unit",
                  dataIndex : "unit",
                  fieldLabel : "单位",
                  allowBlank : true,
                  tabIndex : tn++,
                  width : 120,
                  insert_allowed : true,
                  update_allowed : true
                }));
        this.fields.add("typeCode", new app.base.ComboBox({
                  name : "typeCode",
                  dataIndex : "typeCode",
                  fieldLabel : '类别',
                  store : new Ext.data.SimpleStore({
                        fields : ['value', 'name'],
                        data : [[1, '一日一次'], [2, '一日多次']]
                      }),
                  displayField : 'name',
                  valueField : 'value',
                  labelSeparator : ":*",
                  typeAhead : false,
                  listWidth : 100,
                  width : 120,
                  forceSelection : true,
                  allowBlank : false,
                  mode : 'local',
                  triggerAction : 'all',
                  editable : false,
                  selectOnFocus : true,
                  value : 1,
                  insert_allowed : true,
                  update_allowed : true,
                  onSelect : function(record) {
                    this.setValue(record.data.value);
                    this.onTriggerClick();
                  }
                }));

        Ext.apply(this, {
              items : [this.fields.get("_p_record_status"), {
                layout : "form",
                columnWidth : 1,
                labelAlign : "right",
                labelWidth : 100,
                items : [this.fields.get("id"), this.fields.get("code"),
                    this.fields.get("name"), this.fields.get("unit"),
                    this.fields.get("typeCode")]
              }],
              bodyStyle : 'padding:20px 0 ;',
              layout : "column",
              defaults : {
                labelWidth : 100
              },
              firstFocusFieldName : "name"
            });

        Divo.app.VitalSignItemForm.superclass.initComponent.apply(this,
            arguments);
      }

      ,
      onRender : function() {
        Divo.app.VitalSignItemForm.superclass.onRender.apply(this, arguments);
      },
      newDataRecord : function() {
        return new this.dataRecordMeta({
              _p_record_status : "insert",
              id : "",
              code : "",
              name : "",
              unit : "",
              typeCode : ""
            });
      }

    });
Ext.reg("Divo.app.VitalSignItemForm", Divo.app.VitalSignItemForm);

Divo.app.VitalSignItemList = function() {
  /* ----------------------- private属性 ----------------------- */
  var canEdit = false, status = "list"; // 当前列表状态("list","insert","edit")
  var dbo = 'VitalSignItem';
  var gridEl = 'vitalsignitem-grid';
  var grid, form, selModel, toolbar;

  /* ----------------------- private方法 ----------------------- */
  // 创建列表
  function createGrid() {
    var queryFlds = new Array();
    var searchFlds = new Ext.util.MixedCollection();
    queryFlds.push('query');

    var fldName = new Ext.form.TextField({
          name : 'query',
          id : gridEl + '-query',
          fieldLabel : '搜索',
          allowBlank : true,
          width : 100
        });
    searchFlds.add('query', fldName);

    grid = new Divo.Base.GridView({
          gridId : gridEl,
          entityName : dbo,
          queryFlds : queryFlds,
          searchFlds : searchFlds,
          packageName : 'pm.mob',
          queryUrl : 'vital_sign/query_vitalsign_item',
          deleteUrl : 'vital_sign/delete_vitalsign_item',
          jsonId : 'id',
          recordPk : ["id"]
        });
    grid.getStore().on("load", onLoad);

    selModel = grid.getSelectionModel();
    selModel.on('rowselect', onRowSelect);
  }

  // 选择某行
  function onRowSelect(selModel, rowIndex, record) {

    var data = record.data;

    form.loadRecord(new form.dataRecordMeta({
          _p_record_status : "list",
          id : data.id,
          code : data.code,
          name : data.name,
          unit : data.unit,
          typeCode : data.typeCode
        }));

    canEdit = 'N';
    status = "list";
    setToolbarStatus();
  }

  // 刷新列表
  function reloadRecords() {
    grid.getStore().reload();
    status = "list";
    setToolbarStatus();
  }

  // 创建新的记录，初始化表单
  function createNewRecord() {

    form.loadRecord(new form.dataRecordMeta({
          _p_record_status : "insert",
          id : "",
          code : "",
          name : "",
          unit : "",
          typeCode : ""
        }));

    status = "editting";
    setToolbarStatus();
  }

  // 创建form表单
  function createForm() {
    form = new Divo.app.VitalSignItemForm({
          layout : 'fit',
          region : 'east',
          border : true,
          split : true,
          width : 240,
          minSize : 220,
          maxSize : 300,
          submitUrl : 'vital_sign/save_vitalsign_item'
        });

    form.on('commitFormSuccess', afterCommitFormSuccess);
  }

  // 新建或修改表单成功提交后,重新加载其父节点
  function afterCommitFormSuccess(operation, recordData) {
    grid.getStore().reload();
  }

  // 创建layout
  function createLayout() {
    toolbar = new Ext.Toolbar({
          items : [{
                icon : "resources/img/g_rec_commit.png",
                text : "保存",
                id : "tlb_SAVE",
                handler : commitForm
              }, {
                icon : "resources/img/g_rec_new.png",
                text : "新增",
                id : "tlb_NEW",
                handler : createNewRecord
              }, {
                icon : "resources/img/g_rec_del.png",
                text : "删除",
                id : "tlb_DELETE",
                handler : deleteRecord
              }, '-', {
                icon : "resources/img/g_rec_upd.png",
                text : "编辑",
                id : "tlb_EDIT",
                handler : toggleEditMode
              }, {
                icon : "resources/img/g_rec_refresh.gif",
                text : "刷新",
                id : "tlb_REFRESH",
                handler : reloadRecords
              }]
        });

    var viewport = new Ext.Viewport({
          layout : 'border',
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
              }, form]
        });

    viewport.doLayout();
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
      toolbar.items.get("tlb_SAVE").disable();
      toolbar.items.get("tlb_NEW").enable();
      if (canEdit) {
        toolbar.items.get("tlb_DELETE").enable();
        toolbar.items.get("tlb_EDIT").enable();
      } else {
        toolbar.items.get("tlb_DELETE").disable();
        toolbar.items.get("tlb_EDIT").disable();
      }
    } else {
      toolbar.items.get("tlb_SAVE").enable();
      toolbar.items.get("tlb_NEW").disable();
      toolbar.items.get("tlb_DELETE").disable();
      toolbar.items.get("tlb_EDIT").disable();
    }

    if (!Divo.util.Tools.checkUserEdit()) {
      toolbar.items.get("tlb_SAVE").disable();
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
      createLayout();
      grid.getStore().load();
    }
  }
}();

Ext.onReady(Divo.app.VitalSignItemList.init, Divo.app.VitalSignItemList, true);