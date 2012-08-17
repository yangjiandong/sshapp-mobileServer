// http://blog.csdn.net/dwj520/article/details/6308747
// Ext grid 超级强大的动态添加字段、列扩展
Ext.override(Ext.data.Store, {
      addField : function(field) {
        if (typeof field == 'string') {
          field = {
            name : field
          };
        }
        this.recordType.prototype.fields.replace(field);
        if (typeof field.defaultValue != 'undefined') {
          this.each(function(r) {
                if (typeof r.data[field.name] == 'undefined') {
                  r.data[field.name] = field.defaultValue;
                }
              });
        }
      },
      removeField : function(name) {
        this.recordType.prototype.fields.removeKey(name);
        this.each(function(r) {
              delete r.data[name];
            });
      }
    });
Ext.override(Ext.grid.ColumnModel, {
      addColumn : function(column, colIndex) {
        if (typeof column == 'string') {
          column = {
            header : column,
            dataIndex : column
          };
        }
        var config = this.config;
        this.config = [];
        if (typeof colIndex == 'number') {
          config.splice(colIndex, 0, column);
        } else {
          colIndex = config.push(column);
        }
        this.setConfig(config);
        return colIndex;
      },
      removeColumn : function(colIndex) {
        var config = this.config;
        this.config = [config[colIndex]];
        config.remove(colIndex);
        this.setConfig(config);
      }
    });
Ext.override(Ext.grid.GridPanel, {
      addColumn : function(field, column, colIndex) {
        if (!column) {
          if (field.dataIndex) {
            column = field;
            field = field.dataIndex;
          } else {
            column = field.name || field;
          }
        }
        this.store.addField(field);
        this.colModel.addColumn(column, colIndex);
      },
      removeColumn : function(name, colIndex) {
        this.store.removeField(name);
        if (typeof colIndex != 'number') {
          colIndex = this.colModel.findColumnIndex(name);
        }
        if (colIndex >= 0) {
          this.colModel.removeColumn(colIndex);
        }
      }
    });

Ext.override(Ext.data.Store, {
      addField : function(field) {
        if (typeof field == 'string') {
          field = {
            name : field
          };
        }
        this.recordType.prototype.fields.replace(field);
        if (typeof field.defaultValue != 'undefined') {
          this.each(function(r) {
                if (typeof r.data[field.name] == 'undefined') {
                  r.data[field.name] = field.defaultValue;
                }
              });
        }
      },
      removeField : function(name) {
        this.recordType.prototype.fields.removeKey(name);
        this.each(function(r) {
              delete r.data[name];
            });
      }
    });
Ext.override(Ext.grid.ColumnModel, {
      addColumn : function(column, colIndex) {
        if (typeof column == 'string') {
          column = {
            header : column,
            dataIndex : column
          };
        }
        var config = this.config;
        this.config = [];
        if (typeof colIndex == 'number') {
          config.splice(colIndex, 0, column);
        } else {
          colIndex = config.push(column);
        }
        this.setConfig(config);
        return colIndex;
      },
      removeColumn : function(colIndex) {
        var config = this.config;
        this.config = [config[colIndex]];
        config.remove(colIndex);
        this.setConfig(config);
      }
    });
Ext.override(Ext.grid.GridPanel, {
      addColumn : function(field, column, colIndex) {
        if (!column) {
          if (field.dataIndex) {
            column = field;
            field = field.dataIndex;
          } else {
            column = field.name || field;
          }
        }
        this.store.addField(field);
        this.colModel.addColumn(column, colIndex);
      },
      removeColumn : function(name, colIndex) {
        this.store.removeField(name);
        if (typeof colIndex != 'number') {
          colIndex = this.colModel.findColumnIndex(name);
        }
        if (colIndex >= 0) {
          this.colModel.removeColumn(colIndex);
        }
      }
    });

// 另一个实现
// https://gist.github.com/2226194
// http://tech-heap.blogspot.com.br/2011/02/how-to-delete-and-add-columns-in-grid.html
Ext.override(Ext.data.Store, {
      addField : function(field) {
        field = new Ext.data.Field(field);
        this.recordType.prototype.fields.replace(field);
        if (typeof field.defaultValue != 'undefined') {
          this.each(function(r) {
                if (typeof r.data[field.name] == 'undefined') {
                  r.data[field.name] = field.defaultValue;
                }
              });
        }
      },
      removeField : function(name) {
        this.recordType.prototype.fields.removeKey(name);
        this.each(function(r) {
              delete r.data[name];
              if (r.modified) {
                delete r.modified[name];
              }
            });
      }
    });
Ext.override(Ext.grid.ColumnModel, {
      addColumn : function(column, colIndex) {
        if (typeof column == 'string') {
          column = {
            header : column,
            dataIndex : column
          };
        }
        var config = this.config;
        this.config = [];
        if (typeof colIndex == 'number') {
          config.splice(colIndex, 0, column);
        } else {
          colIndex = config.push(column);
        }
        this.setConfig(config);
        return colIndex;
      },
      removeColumn : function(colIndex) {
        var config = this.config;
        this.config = [config[colIndex]];
        config.splice(colIndex, 1);
        this.setConfig(config);
      }
    });
Ext.override(Ext.grid.GridPanel, {
      addColumnNoField : function(field, column, colIndex) {
        if (!column) {
          if (field.dataIndex) {
            column = field;
            field = field.dataIndex;
          } else {
            column = field.name || field;
          }
        }
        // 取消，
        // this.store.addField(field);
        return this.colModel.addColumn(column, colIndex);
      },
      addColumn : function(field, column, colIndex) {
        if (!column) {
          if (field.dataIndex) {
            column = field;
            field = field.dataIndex;
          } else {
            column = field.name || field;
          }
        }
        this.store.addField(field);
        return this.colModel.addColumn(column, colIndex);
      },
      removeColumn : function(name, colIndex) {
        this.store.removeField(name);
        if (typeof colIndex != 'number') {
          colIndex = this.colModel.findColumnIndex(name);
        }
        if (colIndex >= 0) {
          this.colModel.removeColumn(colIndex);
        }
      }
    });

// 解决ExtJs TextField maxLength后还是可以输入
Ext.form.TextField.prototype.size = 20;
Ext.form.TextField.prototype.initValue = function() {
  if (this.value !== undefined) {
    this.setValue(this.value);
  } else if (this.el.dom.value.length > 0) {
    this.setValue(this.el.dom.value);
  }
  this.el.dom.size = this.size;
  if (!isNaN(this.maxLength) && (this.maxLength * 1) > 0
      && (this.maxLength != Number.MAX_VALUE)) {
    this.el.dom.maxLength = this.maxLength * 1;
  }
};

//window 动画效果
//http://www.sencha.com/forum/showthread.php?35306-Window-animation-speed-duration
Ext.override(Ext.Window, {
    defaultAnimShowCfg: {
        duration: .25,
        easing: 'easeNone',
        opacity: .5
    },
    defaultAnimHideCfg: {
        duration: .25,
        easing: 'easeNone',
        opacity: 0
    },
    animShow : function(){
        this.proxy.show();
        this.proxy.setBox(this.animateTarget.getBox());
        this.proxy.setOpacity(0);
        var b = this.getBox(false);
        b.callback = this.afterShow;
        b.scope = this;
        b.block = true;
        Ext.apply(b, this.animShowCfg, this.defaultAnimShowCfg);
        this.el.setStyle('display', 'none');
        this.proxy.shift(b);
    },
    animHide : function(){
        this.proxy.setOpacity(.5);
        this.proxy.show();
        var tb = this.getBox(false);
        this.proxy.setBox(tb);
        this.el.hide();
        var b = this.animateTarget.getBox();
        b.callback = this.afterHide;
        b.scope = this;
        b.block = true;
        Ext.apply(b, this.animHideCfg, this.defaultAnimHideCfg);
        this.proxy.shift(b);
    }
});
//use：
//var win = new Ext.Window({
//    title: 'Test',
//    width: 200,
//    height: 100,
//    animShowCfg: {
//        duration: 2
//    },
//    animHideCfg: {
//        duration: 2
//    }
//});

