/**
 * 扩展的combobox控件
 */
Ext.form.TwinTriggerComboBox = Ext.extend(Ext.form.ComboBox, {
      trigger1Class : "", // 默认为下列列表
      trigger2Class : "x-form-search-trigger",// 默认为查询按钮
      hideTrigger1 : false,
      initComponent : function() {
        Ext.form.TwinTriggerComboBox.superclass.initComponent.call(this);

        this.addEvents("second_trigger_clicked");
        this.triggerConfig = {
          tag : 'span',
          cls : 'x-form-twin-triggers',
          style : 'padding-right:2px',
          cn : [{
                tag : "img",
                src : Ext.BLANK_IMAGE_URL,
                cls : "x-form-trigger " + this.trigger1Class
              }, {
                tag : "img",
                src : Ext.BLANK_IMAGE_URL,
                cls : "x-form-trigger " + this.trigger2Class
              }]
        };
      },
      onDestroy : function() {
        Ext.destroy(this.triggers);
        Ext.form.TwinTriggerComboBox.superclass.onDestroy.call(this);
      },
      getTrigger : function(index) {
        return this.triggers[index];
      },
      initTrigger : function() {
        var ts = this.trigger.select('.x-form-trigger', true);
        var triggerField = this;
        ts.each(function(t, all, index) {
              var triggerIndex = 'Trigger' + (index + 1);
              t.hide = function() {
                var w = triggerField.wrap.getWidth();
                this.dom.style.display = 'none';
                triggerField.el.setWidth(w - triggerField.trigger.getWidth());
                this['hidden' + triggerIndex] = true;
              };
              t.show = function() {
                var w = triggerField.wrap.getWidth();
                this.dom.style.display = '';
                triggerField.el.setWidth(w - triggerField.trigger.getWidth());
                this['hidden' + triggerIndex] = false;
              };

              if (this['hide' + triggerIndex]) {
                t.dom.style.display = 'none';
                this['hidden' + triggerIndex] = true;
              }
              this.mon(t, 'click', this['on' + triggerIndex + 'Click'], this, {
                    preventDefault : true
                  });
              t.addClassOnOver('x-form-trigger-over');
              t.addClassOnClick('x-form-trigger-click');
            }, this);
        this.triggers = ts.elements;
      },
      onTrigger1Click : function() {
        this.onTriggerClick();
      },
      onTrigger2Click : function() {
        this.fireEvent('second_trigger_clicked', this.getValue(), this.getRawValue());
      }
    });

Ext.reg('twoTriggerCombobox', Ext.form.TwinTriggerComboBox);
