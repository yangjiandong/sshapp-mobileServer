/**
 * 为Grid列菜单添加过滤功能菜单
 */
Ext.grid.GridFilterMenu = function(config) {
  Ext.apply(this, config);
};

Ext.extend(Ext.grid.GridFilterMenu, Ext.util.Observable, {
      filterAfterLoaded : false,
      filterColumn : '',
      filterValue : '',
      init : function(grid) {
        this.filterTitleChar = '※'; // 过滤时，在列标题后添加的标识符
        this.filterCollection = new Ext.util.MixedCollection();
        this.filterStore = new Ext.data.ArrayStore({
              fields : ['value'],
              data : []
            });

        this.grid = grid;
        this.cm = grid.getColumnModel();
        this.view = grid.getView();
        this.store = grid.getStore();

        this.grid.on('render', this.onGridRendered.createDelegate(this));
        this.store.on('load', this.onLoaded.createDelegate(this));
      },
      /**
       * 渲染Grid后，渲染过滤菜单
       */
      onGridRendered : function() {
        this.view.hmenu.on('beforeshow', this.beforeFilterMenuShow.createDelegate(this));

        this.view.hmenu.add('-', {
              xtype : 'combo',
              itemId : 'filters',
              store : this.filterStore,
              displayField : 'value',
              typeAhead : true,
              editable : false,
              mode : 'local',
              triggerAction : 'all',
              selectOnFocus : true,
              width : 65,
              minListWidth : 100,
              getListParent : function() {
                return this.el.up('.x-menu');
              },
              iconCls : 'x-grid-filter-equal-icon',
              listeners : {
                'select' : this.onFilterComboSeleted.createDelegate(this),
                'expand' : this.onFilterComboExpanded.createDelegate(this)
              }
            });
      },
      /**
       * 数据加载完后，初始化过滤的值
       */
      onLoaded : function(store, records, options) {
        this.filterCollection.clear();
        var flds = [];

        for (var i = 0; i < this.cm.getColumnCount(); i++) {
          if (Ext.isEmpty(this.cm.config[i].filterable)) {
            continue;
          }
          if (this.cm.config[i].filterable) {
            flds.push(this.cm.config[i].dataIndex);
          }
        }

        for (var m = 0; m < records.length; m++) {
          for (var n = 0; n < flds.length; n++) {
            var col = flds[n];
            var val = '';
            eval('val=records[m].data.' + col);
            if (this.filterCollection.containsKey(col)) {
              var vals = this.filterCollection.get(col);
              if (vals.indexOf(val) < 0) {
                vals.push(val);
              }
            } else {
              this.filterCollection.add(col, [val]);
            }
          }
        }

        if (this.filterAfterLoaded) {
          if (!Ext.isEmpty(this.filterColumn) && !Ext.isEmpty(this.filterValue)) {
            this.store.filter(this.filterColumn, this.filterValue);
          }
        }

      },
      /**
       * 过滤菜单显示之前，初始化过滤列表的值
       */
      beforeFilterMenuShow : function() {
        var ms = this.view.hmenu.items;
        var colIndex = this.view.hdCtxIndex;
        if (Ext.isEmpty(this.cm.config[colIndex].filterable)
            || this.cm.config[colIndex].filterable == false) {
          ms.get('filters').setDisabled(true);
        } else {
          var filterCombo = ms.get('filters');
          filterCombo.setDisabled(false);

          var dataIndex = this.cm.getDataIndex(colIndex);
          this.filterStore.removeAll();
          var filterData = [];
          var vals = this.filterCollection.get(dataIndex);
          if (!Ext.isEmpty(vals)) {
            for (var i = 0; i < vals.length; i++) {
              filterData.push([vals[i]]);
            }
          }
          this.filterStore.loadData(filterData);
          this.filterStore.sort('value', 'ASC');

          var RecordType = this.filterStore.recordType;
          var p = new RecordType({
                value : '--全部--'
              });

          this.filterStore.insert(0, p);
        }
      },
      /**
       * Combo的列表显示时，重定义其宽度
       */
      onFilterComboExpanded : function(combo) {
        var colIndex = this.view.hdCtxIndex;
        var w = this.cm.config[colIndex].width;
        combo.doResize(w);
      },
      /**
       * 根据选择的过滤值，过滤Grid的数据
       */
      onFilterComboSeleted : function(combo, record, index) {
        var value = record.data.value;
        combo.setValue(null);
        this.view.hmenu.hide(true);
        var colIndex = this.view.hdCtxIndex;

        if (Ext.isEmpty(value) || value == '--全部--') {
          this.filterColumn = '';
          this.filterValue = '';

          this.store.clearFilter();

          if (!Ext.isEmpty(colIndex)) {
            var colHeader = this.cm.getColumnHeader(colIndex);
            if (!Ext.isEmpty(colHeader) && colHeader.endWith(this.filterTitleChar)) {
              var newHeader = Ext.util.Format.substr(colHeader, 0, colHeader.length - 1);
              this.cm.setColumnHeader(colIndex, newHeader);
            }
          }
        } else {
          if (!Ext.isEmpty(colIndex)) {
            for (var i = 0; i < this.cm.getColumnCount(); i++) {
              var colHeader = this.cm.getColumnHeader(i);
              if (!Ext.isEmpty(colHeader) && colHeader.endWith(this.filterTitleChar)) {
                var newHeader = Ext.util.Format.substr(colHeader, 0, colHeader.length - 1);
                this.cm.setColumnHeader(i, newHeader);
              }
            }

            var colHeader = this.cm.getColumnHeader(colIndex);
            if (!Ext.isEmpty(colHeader) && !colHeader.endWith(this.filterTitleChar)) {
              this.cm.setColumnHeader(colIndex, colHeader + this.filterTitleChar);
            }

            var dataIndex = this.cm.getDataIndex(colIndex);
            this.filterColumn = dataIndex;
            this.filterValue = value;
            this.store.filter(dataIndex, value);
          }
        }
      }
    });
