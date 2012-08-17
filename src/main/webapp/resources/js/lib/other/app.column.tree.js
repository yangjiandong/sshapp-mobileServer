/*
 * Thanks: http://extjs.com
 */
Ext.namespace('Divo');
Ext.namespace('Divo.tree');
Divo.tree.ColumnTree = Ext.extend(Ext.tree.TreePanel, {
			lines : false,
			borderWidth : Ext.isBorderBox ? 0 : 2,
			cls : 'x-column-tree',
			onRender : function() {
				Divo.tree.ColumnTree.superclass.onRender.apply(this, arguments);

				this.headers = this.getEl().createChild({
							cls : 'x-tree-headers'
						}, this.body);

				var cols = this.columns, c;
				var totalWidth = 0;
				var scrollOffset = 19;

				for (var i = 0, len = cols.length; i < len; i++) {
					c = cols[i];
					totalWidth += c.width;
					this.headers.createChild({
								cls : 'x-tree-hd '
										+ (c.cls ? c.cls + '-hd' : ''),
								cn : {
									cls : 'x-tree-hd-text',
									html : c.header
								},
								style : 'width:' + (c.width - this.borderWidth)
										+ 'px;'
							});
				}
				this.headers.createChild({
							cls : 'x-clear'
						});

				this.headers.setWidth(totalWidth + scrollOffset);
				this.innerCt.setWidth(totalWidth);

				this.body.on('scroll', this.onBodyScrolled, this);
				this.on('resize', this.onBodyResized, this);
			},
			onBodyScrolled : function(e, t, o) {
				this.headers.setX(this.innerCt.getX());
			},
			onBodyResized : function(e, t, o) {
				// 其中22为columnHeader的高度
				this.body.setHeight(this.container.dom.clientHeight - 22);
			}
		});

Divo.tree.ColumnNodeUI = Ext.extend(Ext.tree.TreeNodeUI, {
			focus : Ext.emptyFn,
			renderElements : function(n, a, targetNode, bulkRender) {
				this.indentMarkup = n.parentNode ? n.parentNode.ui
						.getChildIndent() : '';

				var t = n.getOwnerTree();
				var cols = t.columns;
				var bw = t.borderWidth;
				var c = cols[0];

				var buf = [
						'<li class="x-tree-node"><div ext:tree-node-id="',
						n.id,
						'" class="x-tree-node-el x-tree-node-leaf ',
						a.cls,
						'">',
						'<div class="x-tree-col" style="width:',
						c.width - bw,
						'px;">',
						'<span class="x-tree-node-indent">',
						this.indentMarkup,
						"</span>",
						'<img src="',
						this.emptyIcon,
						'" class="x-tree-ec-icon x-tree-elbow">',
						'<img src="',
						a.icon || this.emptyIcon,
						'" class="x-tree-node-icon',
						(a.icon ? " x-tree-node-inline-icon" : ""),
						(a.iconCls ? " " + a.iconCls : ""),
						'" unselectable="on">',
						'<a hidefocus="on" class="x-tree-node-anchor" href="',
						a.href ? a.href : "#",
						'" tabIndex="1" ',
						a.hrefTarget ? ' target="' + a.hrefTarget + '"' : "",
						'>',
						'<span unselectable="on">',
						n.text
								|| (c.renderer ? c.renderer(a[c.dataIndex], n,
										a) : a[c.dataIndex]), "</span></a>",
						"</div>"];
				for (var i = 1, len = cols.length; i < len; i++) {
					c = cols[i];

					buf.push('<div class="x-tree-col ', (c.cls ? c.cls : ''),
							'" style="width:', c.width - bw, 'px;">',
							'<div class="x-tree-col-text">', (c.renderer
									? c.renderer(a[c.dataIndex], n, a)
									: a[c.dataIndex]), "</div>", "</div>");
				}
				buf
						.push(
								'<div class="x-clear"></div></div>',
								'<ul class="x-tree-node-ct" style="display:none;"></ul>',
								"</li>");

				if (bulkRender !== true && n.nextSibling
						&& n.nextSibling.ui.getEl()) {
					this.wrap = Ext.DomHelper.insertHtml("beforeBegin",
							n.nextSibling.ui.getEl(), buf.join(""));
				} else {
					this.wrap = Ext.DomHelper.insertHtml("beforeEnd",
							targetNode, buf.join(""));
				}

				this.elNode = this.wrap.childNodes[0];
				this.ctNode = this.wrap.childNodes[1];
				var cs = this.elNode.firstChild.childNodes;
				this.indentNode = cs[0];
				this.ecNode = cs[1];
				this.iconNode = cs[2];
				this.anchor = cs[3];
				this.textNode = cs[3].firstChild;
			}
		});
    
Divo.tree.MyTreeLoader = function(config) {
  Divo.tree.MyTreeLoader.superclass.constructor.call(this, config);
};
Ext.extend(Divo.tree.MyTreeLoader, Ext.tree.TreeLoader, {
      createNode : function(attr) {
        if (attr.leaf == true) {     
           attr.icon = 'public/icons/25_mb5ucom.png';
           //attr.iconCls = "x-tree-node-leaf";    
        } 
        return Divo.tree.MyTreeLoader.superclass.createNode.call(this, attr);
      }
    });

// EOP
