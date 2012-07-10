Ext.override(Ext.data.Store, {
      sortState : [],
      /**
       * Sort by multiple fields in the specified order.
       * 
       * @param {Array}
       *          An Array of field sort specifications, or, if ascending
       * @param {Boolean}
       *          add to existing sortstate sort is required on all columns, an
       *          Array of field names. A field specification looks like:
       * 
       * <pre><code>
       *       {
       *       field: 'orderNumber',
       *       direction: 'ASC'
       *       }
       * </code>
       * &lt;pre&gt;
       * 
       */
      sortByFields : function(newFields, add) {
        var fields = this.sortState;

        // Todo: see if ext has an exisiting lookup implementation
        var lookupfn = function(arr, field) {
          var index = Ext.each(arr, function(item) {
                if (item.field == field)
                  return false;
              })
          if (index === undefined)
            return -1;
          return index;
        }

        if (!add)
          fields = []

        Ext.each(newFields, function(item) {
              var doFlip = false;
              if (typeof item == 'string') {
                item = {
                  field : item,
                  direction : 'ASC'
                };
                doFlip = true;
              }
              var oldIndex = lookupfn(this.sortState, item.field);
              if (oldIndex >= 0 && doFlip)
                item.direction = (this.sortState[oldIndex].direction == 'ASC') ? 'DESC' : 'ASC';

              if (oldIndex >= 0 && add)
                fields[oldIndex].direction = item.direction;
              else
                fields.push(item);
            }, this);

        var st = [];
        for (var i = 0; i < fields.length; i++) {
          st.push(this.fields.get(fields[i].field).sortType);
        }
        this.sortState = fields;

        var fn = function(r1, r2) {
          var result;
          for (var i = 0; !result && i < fields.length; i++) {
            var v1 = st[i](r1.data[fields[i].field]);
            var v2 = st[i](r2.data[fields[i].field]);
            if (typeof(v1) == "string") {
              result = v1.localeCompare(v2);
            } else {
              result = (v1 > v2) ? 1 : ((v1 < v2) ? -1 : 0);
            }

            if (fields[i].direction == 'DESC')
              result = -result;
          }
          return result;
        };
        this.data.sort('ASC', fn);
        if (this.snapshot && this.snapshot != this.data) {
          this.snapshot.sort('ASC', fn);
        }
        this.fireEvent("datachanged", this);
      }
    });
