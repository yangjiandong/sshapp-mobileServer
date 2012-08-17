/**
 * 移动端版本管理
 */
Ext.namespace('Divo');
Ext.namespace('Divo.app');

Divo.app.MobVersionList = function() {
  /* ----------------------- private属性 ----------------------- */
  var grid;
  var win, uploadWin;

  /* ----------------------- private方法 ----------------------- */
  // 创建列表
  function createGrid() {
    grid = new Divo.Base.GridView({
          gridId : "mobversion-grid",
          entityClassName : 'org.ssh.pm.mob.entity.MobVersion',
          autoFit : false,
          queryUrl : 'mob_version/query',
          jsonId : 'id',
          recordPk : ["id"]
        });
  }

  function createUploadWindow(detailId) {
    if (uploadWin) {
      uploadWin.destroy();
    }

    var fldUpload = new Ext.form.TextField({
          fieldLabel : '文件名称',
          name : 'file',
          inputType : 'file',
          width : 200,
          allowBlank : false,
          blankText : '文件名称不能为空.',
          anchor : '90%'
        });
    var uploadForm = new Ext.FormPanel({
          bodyStyle : 'padding:5 5 5 5',
          width : 200,
          id : 'uploadForm',
          frame : true,
          fileUpload : true,
          labelWidth : 100,
          items : [fldUpload]
        });

    uploadWin = new Ext.Window({
          title : '文件上传',
          iconCls : 'icon-win',
          height : 200,
          width : 500,
          layout : 'fit',
          plain : false,
          buttonAlign : 'right',
          closable : false,
          modal : true,
          items : [uploadForm]
        });

    var okHideBtn = uploadWin.addButton({
          text : '上传'
        }, function() {

          if (uploadForm.getForm().isValid()) {
            uploadForm.getForm().submit({
                  url : 'mob_version/deploy',

                  method : 'POST',
                  waitTitle : '请稍后',
                  waitMsg : '正在上传文件 ...',
                  success : function(fp, action) {
                    Ext.Msg.alert('成功！', '文件上传成功！');
                    fldUpload.reset();
                    grid.getStore().reload();
                    uploadWin.hide();
                  },
                  failure : function(response, action) {
                    Ext.Msg.alert('失败！', '文件上传失败！');
                  }
                });
          }
        }, this);

    var cancelBtn = uploadWin.addButton({
          text : '关闭'
        }, function() {
          uploadWin.hide()
        }, this);
    uploadWin.show();
  }

  // 创建layout
  function createLayout() {

    var toolbar = new Ext.Toolbar({
          items : [{
                icon : "resources/img/g_rec_new.png",
                text : "发布新版本",
                id : "tlb_NEW",
                handler : function() {
                  createUploadWindow();
                }
              }]
        });

    var viewport = new Ext.Viewport({
          layout : 'border',
          items : [{
                layout : 'fit',
                region : 'center',
                border : false,
                tbar : toolbar,
                items : [grid]
              }]
        });

    viewport.doLayout();
  }

  /* ----------------------- public方法 ----------------------- */
  return {
    /**
     * 初始化
     */
    init : function() {
      createGrid();
      createLayout();
      grid.getStore().load();
    }
  }
}();

Ext.onReady(Divo.app.MobVersionList.init, Divo.app.MobVersionList, true);