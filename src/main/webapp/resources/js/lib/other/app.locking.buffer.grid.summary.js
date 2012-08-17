Ext.grid.LockingBufferGridSummary = function(config){
    Ext.apply(this, config);
};

Ext.extend(Ext.grid.LockingBufferGridSummary, Ext.util.Observable, {
    init : function(grid){
        this.grid = grid;
        this.cm = grid.getColumnModel();
        this.view = grid.getView();       
        this.beforeInsert = false;
        this.summaryRendered=false;

        var v = this.view;
        this.doRenderBase = v.doRender;
        v.doRender = this.doRender.createDelegate(this);
        
        this.grid.getStore().afterMethod('filter',this.onAfterStoreFilter, this);
        
        v.beforeMethod('insertRows',this.doBeforeInsert,this);
        v.afterMethod('onColumnWidthUpdated', this.doWidth, this);
        v.afterMethod('onAllColumnWidthsUpdated', this.doAllWidths, this);
        v.afterMethod('onColumnHiddenUpdated', this.doHidden, this);
        v.afterMethod('onUpdate', this.doUpdate, this);
        v.afterMethod('onRemove', this.doRemove, this); 
                       
        if(!this.rowTpl){
            this.rowTpl = new Ext.Template(
                '<div class="x-grid3-summary-row" style="{tstyle}">',
                '<table class="x-grid3-summary-table" border="0" cellspacing="0" cellpadding="0" style="{tstyle}">',
                    '<tbody><tr>{cells}</tr></tbody>',
                '</table></div>'
            );          
            this.rowTpl.disableFormats = true;
        }
        this.rowTpl.compile();

        if(!this.cellTpl){
            this.cellTpl = new Ext.Template(
                '<td class="x-grid3-col x-grid3-cell x-grid3-td-{id} {css}" style="{style}">',
                '<div class="x-grid3-cell-inner x-grid3-col-{id}" unselectable="on">{value}</div>',
                "</td>"
            );          
            this.cellTpl.disableFormats = true;
        }
        this.cellTpl.compile();
    },

    toggleSummaries : function(visible){
        var el = this.grid.getGridEl();
        if(el){
            if(visible === undefined){
                visible = el.hasClass('x-grid-hide-summary');
            }
            el[visible ? 'removeClass' : 'addClass']('x-grid-hide-summary');
        }
    },

    renderSummary : function(o, cs){
      var ts = this.view.templates, ct = ts.cell, rt = ts.row, rp = {}
            tstyle = 'width:' + this.view.getTotalWidth() + ';',
            lstyle = 'width:' + this.view.getLockedWidth() + ';',
            lbuf = [],
            grpct = this.cellTpl, grprt = this.rowTpl;
        
        cs = cs || this.view.getColumnData();
        var cfg = this.grid.getColumnModel().config,
            buf = [], c, p = {}, cf, last = cs.length-1, cb, lcb;
        cb = [];
        lcb = [];
        for(var i = 0, len = cs.length; i < len; i++){
            c = cs[i];
            cf = cfg[i];
            p.id = c.id;
            p.style = c.style;
            p.css = i == 0 ? 'x-grid3-cell-first ' : (i == last ? 'x-grid3-cell-last ' : '');
            if(cf.summaryType || cf.summaryRenderer){
                p.value = (cf.summaryRenderer || c.renderer)(o.data[c.name], p, o);
            }else{
                p.value = '';
            }
            if(p.value == undefined || p.value === "") p.value = "&#160;";
            
            if(c.locked){
                lcb[lcb.length] = grpct.apply(p);
            }
            else{
                cb[cb.length] = grpct.apply(p);
            }
        }
        rp.cells = cb.join('');
        rp.tstyle = tstyle;
        buf[buf.length] = grprt.apply(rp);
        
        rp.cells = lcb.join('');
        rp.tstyle = lstyle;
        lbuf[lbuf.length] = grprt.apply(rp);      
        
        return [buf.join(''), lbuf.join('')];   
    },

    calculate : function(rs, cs){
        var data = {}, r, c, cfg = this.cm.config, cf;
        for(var j = 0, jlen = rs.length; j < jlen; j++){
            r = rs[j];
            for(var i = 0, len = cs.length; i < len; i++){
                c = cs[i];
                cf = cfg[i];
                if(cf.summaryType){
                    data[c.name] = Ext.grid.LockingBufferGridSummary.Calculations[cf.summaryType](data[c.name] || 0, r, c.name, data);
                }
            }
        }
        return data;
    },

    doRender : function(cs, rs, ds, startRow, colCount, stripe){
        var buf = this.doRenderBase.call(this.view,cs, rs, ds, startRow, colCount, stripe,true);
        // We normally don't want to add summary row to every insert.
        // However, we do need to if this is the first row
        if ((!this.beforeInsert || this.view.getRows().length==0) && !this.summaryRendered){
            var data = this.calculate(rs, cs);
            
            var sbuf = this.renderSummary({data: data}, cs);            
            buf[0] += sbuf[0];
            buf[1]  += sbuf[1] ;
        } else {
            this.refreshSummary();
        }
        this.beforeInsert = false;
        this.summaryRendered=true;
        return buf;
    },

    doWidth : function(col, w, tw){
        var gs = this.view.getRows(), s;
        if (gs.length > 0){
            s = gs[gs.length - 1];
            s.style.width = tw;
            s.firstChild.style.width = tw;
            s.firstChild.rows[0].childNodes[col].style.width = w;
        }
    },

    doAllWidths : function(ws, tw){
        var gs = this.view.getRows(), s, cells, wlen = ws.length;
        if (gs.length > 0) {
            s = gs[gs.length - 1];
            s.style.width = tw;
            s.firstChild.style.width = tw;
            cells = s.firstChild.rows[0].childNodes;
            for (var j = 0; j < wlen; j++) {
                cells[j].style.width = ws[j];
            }
        }
    },

    doHidden : function(col, hidden, tw){
        var gs = this.view.getRows(), s, display = hidden ? 'none' : '';
        for(var i = 0, len = gs.length; i < len; i++){
            s = gs[i].childNodes[2];
            s.style.width = tw;
            s.firstChild.style.width = tw;
            s.firstChild.rows[0].childNodes[col].style.display = display;
        }
    },

    getSummaryNode : function(){
        return [this.view.mainBody.child('.x-grid3-summary-row'),this.view.lockedBody.child('.x-grid3-summary-row')];
    },

    refreshSummary : function(){
        var rs = [];
        this.grid.store.each(function(r){
            rs[rs.length] = r;
        });
        var cs = this.view.getColumnData();
        var data = this.calculate(rs, cs);
        var markup = this.renderSummary({data: data}, cs);
        var mbody = this.view.mainBody.dom;
        var lbody = this.view.lockedBody.dom;

        var existing = this.getSummaryNode();
        if(existing){
            if(existing[0]){
                mbody.removeChild(existing[0].dom);
            }
            
            if(existing[1]){
                lbody.removeChild(existing[1].dom);
            }
        }
        
        Ext.DomHelper.append(mbody, markup[0]);
        Ext.DomHelper.append(lbody, markup[1]);
        return true;
    },
    
    removeSummary : function(){
        var existing = this.getSummaryNode();
        if (existing) {
          
          if(existing[0]){
                var mbody = this.view.mainBody.dom;
                mbody.removeChild(existing[0].dom);
            }
            
            if(existing[1]){
                 var lbody = this.view.lockedBody.dom;
                lbody.removeChild(existing[1].dom);
            }
        }
    },

    doUpdate : function(ds, record){
        this.refreshSummary();
    },

    doRemove : function(ds, record, index, isUpdate){
        if(!isUpdate){
            if (ds.getCount() === 0) this.removeSummary();
            else this.refreshSummary();
        }
    }, 
    
    doBeforeInsert : function(){
        this.beforeInsert = true;
    },
    onAfterStoreFilter : function(){      
        this.refreshSummary();
    }
});

Ext.grid.LockingBufferGridSummary.Calculations = {
    'sum' : function(v, record, field){
        return v + (record.data[field]||0);
    },

    'count' : function(v, record, field, data){
        return data[field+'count'] ? ++data[field+'count'] : (data[field+'count'] = 1);
    },
     
    'max' : function(v, record, field, data){
        var v = record.data[field];
        var max = data[field+'max'] === undefined ? (data[field+'max'] = v) : data[field+'max'];
        return v > max ? (data[field+'max'] = v) : max;
    },

    'min' : function(v, record, field, data){
        var v = record.data[field];
        var min = data[field+'min'] === undefined ? (data[field+'min'] = v) : data[field+'min'];
        return v < min ? (data[field+'min'] = v) : min;
    },

    'average' : function(v, record, field, data){
        var c = data[field+'count'] ? ++data[field+'count'] : (data[field+'count'] = 1);
        var t = (data[field+'total'] = ((data[field+'total']||0) + (record.data[field]||0)));
        return t === 0 ? 0 : t / c;
    },
    
    'totalStr' : function(v, record, field, data){
       return '合计';
    }
}