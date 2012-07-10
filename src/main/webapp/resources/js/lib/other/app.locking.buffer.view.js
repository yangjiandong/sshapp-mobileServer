/*
 * ! Ext JS Library 3.1.1 Copyright(c) 2006-2010 Ext JS, LLC licensing@extjs.com
 * http://www.extjs.com/license
 */
Ext.ns('Ext.ux.grid');
Ext.ux.grid.LockingBufferGridView = Ext.extend(Ext.ux.grid.LockingGridView, {
  rowHeight : 19,
  borderHeight : 2,
  scrollDelay : 100,
  cacheSize : 20,
  cleanDelay : 500,
  initTemplates : function() {
    Ext.ux.grid.LockingBufferGridView.superclass.initTemplates.call(this);
    var ts = this.templates;
    // empty div to act as a place holder for a row
    ts.rowHolder = new Ext.Template('<div class="x-grid3-row {alt}" style="{tstyle}"></div>');
    ts.rowHolder.disableFormats = true;
    ts.rowHolder.compile();

    ts.rowBody = new Ext.Template(
        '<table class="x-grid3-row-table" border="0" cellspacing="0" cellpadding="0" style="{tstyle}">',
        '<tbody><tr>{cells}</tr>',
        (this.enableRowBody
            ? '<tr class="x-grid3-row-body-tr" style="{bodyStyle}"><td colspan="{cols}" class="x-grid3-body-cell" tabIndex="0" hidefocus="on"><div class="x-grid3-row-body">{body}</div></td></tr>'
            : ''), '</tbody></table>');
    ts.rowBody.disableFormats = true;
    ts.rowBody.compile();
  },
  syncScroll : function(e) {
    Ext.ux.grid.LockingBufferGridView.superclass.syncScroll.call(this, e);
    this.update();
  },
  doRender : function(cs, rs, ds, startRow, colCount, stripe, onlyBody) {
    var ts = this.templates, ct = ts.cell, rt = ts.row, rb = ts.rowBody, last = colCount - 1;
    var rh = this.getStyleRowHeight();
    var vr = this.getVisibleRows();

    var tstyle = 'width:' + this.getTotalWidth() + ';height:' + rh + 'px;';
    var lstyle = 'width:' + this.getLockedWidth() + ';height:' + rh + 'px;';

    var buf = [], lbuf = [], cb, lcb, c, p = {}, rp = {}, r;
    for (var j = 0, len = rs.length; j < len; j++) {
      r = rs[j];
      cb = [];
      lcb = [];
      var rowIndex = (j + startRow);
      var visible = rowIndex >= vr.first && rowIndex <= vr.last;

      if (visible) {
        for (var i = 0; i < colCount; i++) {
          c = cs[i];
          p.id = c.id;
          p.css = (i === 0 ? 'x-grid3-cell-first ' : (i == last ? 'x-grid3-cell-last ' : ''))
              + (this.cm.config[i].cellCls ? ' ' + this.cm.config[i].cellCls : '');
          p.attr = p.cellAttr = '';
          p.value = c.renderer(r.data[c.name], p, r, rowIndex, i, ds);
          p.style = c.style;
          if (Ext.isEmpty(p.value)) {
            p.value = '&#160;';
          }
          if (this.markDirty && r.dirty && Ext.isDefined(r.modified[c.name])) {
            p.css += ' x-grid3-dirty-cell';
          }
          if (c.locked) {
            lcb[lcb.length] = ct.apply(p);
          } else {
            cb[cb.length] = ct.apply(p);
          }
        }
      }

      var alt = [];
      if (stripe && ((rowIndex + 1) % 2 === 0)) {
        alt[0] = 'x-grid3-row-alt';
      }

      if (r.dirty) {
        alt[1] = ' x-grid3-dirty-row';
      }
      rp.cols = colCount;
      if (this.getRowClass) {
        alt[2] = this.getRowClass(r, rowIndex, rp, ds);
      }

      rp.alt = alt.join(' ');

      rp.cells = cb.join('');
      rp.tstyle = tstyle;
      buf[buf.length] = !visible
          ? ts.rowHolder.apply(rp)
          : (onlyBody ? rb.apply(rp) : rt.apply(rp));

      rp.cells = lcb.join('');
      rp.tstyle = lstyle;
      lbuf[lbuf.length] = !visible ? ts.rowHolder.apply(rp) : (onlyBody ? rb.apply(rp) : rt
          .apply(rp));
    }
    return [buf.join(''), lbuf.join('')];
  },
  /**
   * BufferView's Methods
   */
  getStyleRowHeight : function() {
    return Ext.isBorderBox ? (this.rowHeight + this.borderHeight) : this.rowHeight;
  },

  getCalculatedRowHeight : function() {
    return this.rowHeight + this.borderHeight;
  },

  getVisibleRowCount : function() {
    var rh = this.getCalculatedRowHeight();
    var visibleHeight = this.scroller.dom.clientHeight;
    return (visibleHeight < 1) ? 0 : Math.ceil(visibleHeight / rh);
  },

  getVisibleRows : function() {
    var count = this.getVisibleRowCount();
    var sc = this.scroller.dom.scrollTop;
    var start = (sc == 0 ? 0 : Math.floor(sc / this.getCalculatedRowHeight()) - 1);
    return {
      first : Math.max(start, 0),
      last : Math.min(start + count + 2, this.ds.getCount() - 1)
    };
  },

  isRowRendered : function(index) {
    var row = this.getRow(index);
    return row && row.childNodes.length > 0;
  },

  // a (optionally) buffered method to update contents of gridview
  update : function() {
    if (this.scrollDelay) {
      if (!this.renderTask) {
        this.renderTask = new Ext.util.DelayedTask(this.doUpdate, this);
      }
      this.renderTask.delay(this.scrollDelay);
    } else {
      this.doUpdate();
    }
  },

  onRemove : function(ds, record, index, isUpdate) {
    Ext.ux.grid.LockingBufferGridView.superclass.onRemove.apply(this, arguments);
    if (isUpdate !== true) {
      this.update();
    }
  },

  doUpdate : function() {
    if (this.getVisibleRowCount() > 0) {
      var g = this.grid, cm = g.colModel, ds = g.store;
      var cs = this.getColumnData();

      var vr = this.getVisibleRows();
      for (var i = vr.first; i <= vr.last; i++) {
        // if row is NOT rendered and is visible, render it
        if (!this.isRowRendered(i)) {
          var htmls = [];

          htmls = this.doRender(cs, [ds.getAt(i)], ds, i, cm.getColumnCount(), g.stripeRows, true);
          this.getLockedRow(i).innerHTML = htmls[1]
          this.getRow(i).innerHTML = htmls[0];
        }
      }
      this.clean();
    }
  },

  // a buffered method to clean rows
  clean : function() {
    if (!this.cleanTask) {
      this.cleanTask = new Ext.util.DelayedTask(this.doClean, this);
    }
    this.cleanTask.delay(this.cleanDelay);
  },

  doClean : function() {
    if (this.getVisibleRowCount() > 0) {
      var vr = this.getVisibleRows();
      vr.first -= this.cacheSize;
      vr.last += this.cacheSize;

      var i = 0, rows = this.getRows(), lrows = this.getLockedRows();
      // if first is less than 0, all rows have been rendered
      // so lets clean the end...
      if (vr.first <= 0) {
        i = vr.last + 1;
      }
      for (var len = this.ds.getCount(); i < len; i++) {
        // if current row is outside of first and last and
        // has content, update the innerHTML to nothing
        if ((i < vr.first || i > vr.last) && rows[i].innerHTML) {
          rows[i].innerHTML = '';
        }

        if ((i < vr.first || i > vr.last) && lrows[i].innerHTML) {
          lrows[i].innerHTML = '';
        }
      }
    }
  },

  layout : function() {
    Ext.ux.grid.LockingBufferGridView.superclass.layout.call(this);
    this.update();
  }
});