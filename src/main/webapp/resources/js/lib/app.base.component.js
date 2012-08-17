Ext.ns('app');
Ext.ns('app.base');

app.base.SubmitButton = Ext.extend(Ext.Button, {
      text : '确定',
      iconCls : 'icon-submit',
      initComponent : function() {
        app.base.SubmitButton.superclass.initComponent.apply(this, arguments);
      } // eo function initComponent
    }); // eo extend

Ext.reg('submitbutton', app.base.SubmitButton);

app.base.CancelButton = Ext.extend(Ext.Button, {
      text : '取消',
      // iconCls : 'icon-undo',
      cls : "x-btn-text-icon",
      icon : "resources/img/g_rec_del.png",
      initComponent : function() {
        app.base.CancelButton.superclass.initComponent.apply(this, arguments);
      } // eo function initComponent
    }); // eo extend

Ext.reg('cancelbutton', app.base.CancelButton);

app.base.DeleteButton = Ext.extend(Ext.Button, {
      text : '删除',
      iconCls : 'icon-delete',
      initComponent : function() {
        app.base.DeleteButton.superclass.initComponent.apply(this, arguments);
      } // eo function initComponent
    }); // eo extend

Ext.reg('deletebutton', app.base.DeleteButton);

app.base.TextField = Ext.extend(Ext.form.TextField, {
      cls : "otherpayment-form-disabled-text-font-color",
      initComponent : function() {
        app.base.TextField.superclass.initComponent.apply(this, arguments);
      } // eo function initComponent
    }); // eo extend

Ext.reg('baseTextField', app.base.TextField);

app.base.NumberField = Ext.extend(Ext.form.NumberField, {
      cls : 'otherpayment-form-disabled-text-font-color',
      initComponent : function() {
        app.base.NumberField.superclass.initComponent.apply(this, arguments);
      } // eo function initComponent
    }); // eo extend

Ext.reg('baseNumberField', app.base.NumberField);

app.base.ComboBox = Ext.extend(Ext.form.ComboBox, {
      cls : 'otherpayment-form-disabled-text-font-color-2',
      initComponent : function() {
        app.base.ComboBox.superclass.initComponent.apply(this, arguments);
      } // eo function initComponent
    }); // eo extend

Ext.reg('baseComboBox', app.base.ComboBox);

app.base.TextArea = Ext.extend(Ext.form.TextArea, {
      cls : 'otherpayment-form-disabled-text-font-color',
      initComponent : function() {
        app.base.TextArea.superclass.initComponent.apply(this, arguments);
      } // eo function initComponent
    }); // eo extend

Ext.reg('baseTextArea', app.base.TextArea);

app.base.Checkbox = Ext.extend(Ext.form.Checkbox, {
      cls : 'otherpayment-form-disabled-text-font-color',
      initComponent : function() {
        app.base.Checkbox.superclass.initComponent.apply(this, arguments);
      } // eo function initComponent
    }); // eo extend

Ext.reg('baseCheckbox', app.base.Checkbox);

app.base.XCheckbox = Ext.extend(Ext.form.Checkbox, {

  initComponent : function() {
    Ext.apply(this, arguments);
    app.base.XCheckbox.superclass.initComponent.apply(this, arguments);
  },
  onRender : function(ct) {
    // call parent
    app.base.XCheckbox.superclass.onRender.apply(this, arguments);
  }
  /**
   * Returns the checked state of the checkbox.
   *
   * @return {Boolean} True if checked, else false
   */
  ,
  getValue : function() {
    if (this.rendered) {
      return (this.el.dom.checked) ? 'Y' : 'N';
    }
    return 'N';
  }

  /**
   * Sets the checked state of the checkbox.
   *
   * @param {Boolean/String}
   *          checked True, 'true', '1', or 'on' to check the checkbox, any
   *          other value will uncheck it.
   */
  ,
  setValue : function(v) {
    var checked = this.checked;
    this.checked = (v === true || v === 'Y' || v === 'true' || v == '1' || String(v)
        .toLowerCase() == 'on');

    if (this.el && this.el.dom) {
      this.el.dom.checked = this.checked;
      this.el.dom.defaultChecked = this.checked;
    }
    this.wrap[this.checked ? 'addClass' : 'removeClass'](this.checkedCls);

    if (checked != this.checked) {
      this.fireEvent("check", this, this.checked);
      if (this.handler) {
        this.handler.call(this.scope || this, this, this.checked);
      }
    }
  }
});
// register xtype
Ext.reg('xcheckbox', app.base.XCheckbox);

app.base.DateField = Ext.extend(Ext.form.DateField, {
      cls : 'otherpayment-form-disabled-text-font-color-2',
      initComponent : function() {
        app.base.DateField.superclass.initComponent.apply(this, arguments);
      } // eo function initComponent
    }); // eo extend

Ext.reg('baseDateField', app.base.DateField);

// //**适用于TextField、NumberField 添加一个 sideText 标签显示在右侧如必填项的 * 号等*/
// Ext.override(Ext.form.TextField, {
// sideText : '',
// onRender : function(ct, position) {
// Ext.form.TextField.superclass.onRender.call(this, ct, position);
// if (this.sideText != '' && !this.triggerAction) {
// this.sideEl = ct.createChild({
// tag: 'div',
// style:'position:absolute;left:'+(this.x+this.width)+';top:'+(this.y)+';padding-left:2px;display:inline-block;display:inline;',
// html: this.sideText
// });
// }
// if(this.readOnly){//为只读的text加上特定的样式--background-image:url('')去掉本身的背景图片
// /*边框：b5b8c8 背景色：dfe8f6 background-color:#DDDDDD; border:1px*/
// if(this.xtype=='numberfield'){
// if(this.style){
// this.style+=" text-align:right; background-color:#dfe8f6;
// border-color:#b5b8c8;background-image:url('')";
// }else{
// this.style=" text-align:right; background-color:#dfe8f6;
// border-color:#b5b8c8;background-image:url('')";
// }
// }
// else{
// if(this.style){
// this.style+="background-color:#dfe8f6;
// border-color:#b5b8c8;background-image:url('')";
// }else{
// this.style="background-color:#dfe8f6;
// border-color:#b5b8c8;background-image:url('')";
// }
// }
// }
// if(this.display){//用于显示的样式，只有下边的border
// this.style="border-style: none none groove none;background-image:url('');";
// this.readOnly=true;
// if(this.ext_style){
// this.style+=this.ext_style;
// }
// }
// }
// });
//
// //重写onFocus方法，当TextFieldreadOnly为 true时，实现TextField不获取焦点
// Ext.override(Ext.form.TextField, {
// onFocus : function(){
// Ext.form.TextField.superclass.onFocus.call(this);
// if(this.readOnly){
// this.blur();
// }
// }
// });

app.base.ComboBoxTree = Ext.extend(Ext.form.ComboBox, {
      treeHeight : 180,
      url : '',
      store : new Ext.data.SimpleStore({
            fields : [],
            data : [[]]
          }),
      editable : false, // 禁止手写及联想功能
      mode : 'local',
      triggerAction : 'all',
      emptyText : '全部记录',

      /**
       * 初始化 Init
       */
      initComponent : function() {
        app.base.ComboBoxTree.superclass.initComponent.call(this);
        this.tplId = Ext.id();
        this.tpl = '<div id="combobox-tree-' + this.tplId + '" style="height:'
            + this.treeHeight + 'px;overflow:hidden;"></div>';

        var tree = new Ext.tree.TreePanel({
              root : new Ext.tree.AsyncTreeNode({
                    id : '0',
                    text : ''
                  }),
              autoScroll : true,
              height : this.treeHeight,
              rootVisible : false,
              border : false
            });
        var combo = this;

        tree.on('click', function(node) {
              if (combo.allowUnLeafClick == true) {
                combo.setValue(node.text);
                combo.hiddenField.value = node.id;
                combo.collapse();
                combo.onSelect(node.id);
              } else if (node.leaf == true) {
                combo.setValue(node.text);
                combo.hiddenField.value = node.id;
                combo.collapse();
                combo.onSelect(node.id);
              }
            });
        this.tree = tree;
      },

      /**
       * ------------------ 事件监听器 Listener ------------------
       */
      listeners : {
        'expand' : {
          fn : function() {
            if (!this.tree.rendered && this.tplId) {
              Ext.Ajax.request({
                    url : this.url,
                    scope : this,
                    callback : function(o, s, r) {
                      var respText = Ext.decode(r.responseText);
                      if (respText.success) {
                        var returnTree = respText.data;
                        var parentNode;
                        for (var i = 0; i < returnTree.length; i++) {
                          // 创建节点
                          var nodeOfModule = new Ext.tree.TreeNode({
                                id : returnTree[i].id,
                                text : returnTree[i].text,
                                parentId : returnTree[i].parentId,
                                leaf : returnTree[i].leaf
                              });

                          if (returnTree[i].parentId == null
                              || returnTree[i].parentId == '0') {
                            this.tree.root.appendChild(nodeOfModule);
                          } else {
                            parentNode = this.tree
                                .getNodeById(returnTree[i].parentId);
                            if (parentNode) {
                              parentNode.appendChild(nodeOfModule);
                            }
                          }
                        }
                        this.tree.render('combobox-tree-' + this.tplId);
                        this.tree.getRootNode().expand();
                      }
                    }
                  });

            }
          }
        }
      }
    });
Ext.reg('app.base.ComboBoxTree', app.base.ComboBoxTree);
