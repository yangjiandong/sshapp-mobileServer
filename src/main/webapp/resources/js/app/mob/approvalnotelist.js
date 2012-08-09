/**
 * 用药审批备注信息管理
 */
Ext.namespace('Divo');
Ext.namespace('Divo.app');

Divo.app.ApprovalNoteForm = Ext.extend(Divo.Base.EditForm, {
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
        this.fields.add("typeCode", new Ext.form.Hidden({
                  name : "typeCode",
                  dataIndex : "typeCode",
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
                  fieldLabel : "备注信息",
                  allowBlank : false,
                  labelSeparator : ":*",
                  tabIndex : tn++,
                  width : 120,
                  insert_allowed : true,
                  update_allowed : true
                }));

        Ext.apply(this, {
              items : [this.fields.get("_p_record_status"), {
                layout : "form",
                columnWidth : 1,
                labelAlign : "right",
                labelWidth : 100,
                items : [this.fields.get("id"), this.fields.get("typeCode"),
                    this.fields.get("code"), this.fields.get("name")]
              }],
              bodyStyle : 'padding:20px 0 ;',
              layout : "column",
              defaults : {
                labelWidth : 100
              },
              firstFocusFieldName : "name"
            });

        Divo.app.ApprovalNoteForm.superclass.initComponent.apply(this,
            arguments);
      }

      ,
      onRender : function() {
        Divo.app.ApprovalNoteForm.superclass.onRender.apply(this, arguments);
      },
      newDataRecord : function() {
        return new this.dataRecordMeta({
              _p_record_status : "insert",
              id : "",
              code : "",
              name : "",
              typeCode : "1"
            });
      }

    });
Ext.reg("Divo.app.ApprovalNoteForm", Divo.app.ApprovalNoteForm);

Divo.app.ApprovalNoteList = function() {
  /* ----------------------- private属性 ----------------------- */
  var canEdit = false, status = "list"; // 当前列表状态("list","insert","edit")
  var dbo = 'ApprovalNote';
  var gridEl = 'approvalnote-grid';
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
          packageName : 'pm.approval',
          queryUrl : 'drug_approval/query_note',
          deleteUrl : 'drug_approval/delete_note',
          jsonId : 'id',
          recordPk : ["id"]
        });
    grid.getStore().on("load", onLoad);

    grid.getStore().on('beforeload', function(store, options) {
          store.baseParams.typeCode = "1";
        }, this, true);

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
          typeCode : "1"
        }));

    status = "editting";
    setToolbarStatus();
  }

  // 创建form表单
  function createForm() {
    form = new Divo.app.ApprovalNoteForm({
          layout : 'fit',
          region : 'east',
          border : true,
          split : true,
          width : 240,
          minSize : 220,
          maxSize : 300,
          submitUrl : 'drug_approval/save_note'
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

Ext.onReady(Divo.app.ApprovalNoteList.init, Divo.app.ApprovalNoteList, true);