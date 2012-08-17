/* 操作界面主面板 */
Divo.app.MainPanel = function(config) {
    Ext.apply(this, config);

    var html = [
        '<div id="welcome-div">',
        '<div style="float:left;"><img src="resources/img/layout-icon.gif" /></div>',
        '<div style="margin-left:100px;">', '<h2>欢迎使用！</h2>', '<p></p>', '</div>'];

    Divo.app.MainPanel.superclass.constructor.call(this, {
            id : 'menu-content-panel',
            region : 'center',
            margins : '3 3 3 0',
            resizeTabs : true,
            minTabWidth : 90,
            tabWidth : 150,
            enableTabScroll : true,
            activeTab : 0,
            deferredRender : false,
            items : [{
                    id : 'welcome-panel',
                    title : '欢迎',
                    layout : 'fit',
                    bodyStyle : 'padding:25px',
                    html : html.join(''),
                    autoScroll : true
                }]
        });
}

Ext.extend(Divo.app.MainPanel, Ext.TabPanel, {
    cfg_run_mode : 'DEV',// 默认为dev,可通过config实现设置
    build_id : '',//版本标识,方便js,css去除缓存
    NEW_LINE : '\n',
    loadContent : function(href, menuId, title, iconCls, module_name) {
        var ifrId = 'frame-' + menuId;
        var tabId = 'contantstab-' + menuId;
        var icls = iconCls;
        var f = document.getElementById(ifrId);
        var wf = window.frames[ifrId];

        if (Ext.isEmpty(f)
            && !Ext.isEmpty(wf)) {
            delete wf;
            delete window.frames[ifrId];//仅仅删除wf对象,会导致关闭tabpanel后再打开时dom消失
        }

        if (!Ext.isEmpty(f)) {
            if (this.getActiveTab().getId() == tabId) {
                this.loadMenuLink(ifrId, href);
            } else {
                this.activate(tabId);
            }
        } else {
            //userlog

            Ext.Ajax.request({
                    url : "system/user_log",
                    params : {
                        infos : '['+module_name+']'+title
                    },
                    scope : this
                });

            this.add(new Ext.Panel({
                title : title,
                id : tabId,
                autoScroll : true,
                iconCls : icls,
                layout : 'fit',
                closable : true,
                html : '<iframe id="'
                    + ifrId
                    + '" name="'
                    + ifrId
                    + '" src="about:blank" style="border:0;width:100%;height:100%;overflow: hidden" FRAMEBORDER="no"></iframe>'
            }));
            this.activate(tabId);
            this.loadMenuLink(ifrId, href);
        }

        this.doLayout();
    },
    loadMenuLink : function(ifrId, href) {
        var curIframe = window.frames[ifrId];

        var htmlString = this.buildHtml(href);
        curIframe.document.open("text/html", "replace");
        curIframe.document.write(htmlString);
        curIframe.document.close();
    },
    buildHtml : function(href) {
        var out = '';

        out += '<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">'
            + this.NEW_LINE;
        out += '<html>' + this.NEW_LINE;
        out += this.buildHtmlHead(href);
        out += this.buildHtmlBody();
        out += '</html>' + this.NEW_LINE;
        out += '';
        return out;
    },
    buildHtmlHead : function(href) {
        var out = '';
        out += '<head>' + this.NEW_LINE;
        out += '  <META HTTP-EQUIV="CONTENT-TYPE" CONTENT="text/html; charset=UTF-8"/>'
            + this.NEW_LINE;

        out += '  <meta http-equiv="pragma" content="no-cache">' + this.NEW_LINE;
        out += '  <meta http-equiv="cache-control" content="no-cache, must-revalidate">'
            + this.NEW_LINE;
        out += '  <meta http-equiv="expires" content="0">' + this.NEW_LINE;

        out += this.buildImportExtjs();
        out += this.buildImportEkingBase();
        out += this.buildImportDoc(href);
        out += '</head>' + this.NEW_LINE;
        out += '';
        return out;
    },
    buildHtmlBody : function() {
        var out = '';
        out += '<body>' + this.NEW_LINE;
        out += '</body>' + this.NEW_LINE;
        out += '';
        return out;
    },
    buildImportExtjs : function() {
        var out = '';
        var ts = '?ver='+this.build_id;

        //if (CFG_DEPLOYMENT_TYPE == 'DEV') {
        // ts = '?_t_='+(new Date()).getTime();
        //}

        out += '  <link rel="stylesheet" type="text/css" href="resources/ext/resources/css/ext-all.css"/>'
            + this.NEW_LINE;
        out += '  <link rel="stylesheet" type="text/css" href="resources/css/default.css'
            + ts + '"/>' + this.NEW_LINE;
        out += ' <link rel="stylesheet" type="text/css" href="resources/css/app.css'
            + ts + '"/>' + this.NEW_LINE;
        out += ' <link rel="stylesheet" type="text/css" href="resources/css/portal.css'
            + ts + '"/>' + this.NEW_LINE; // 窗体拖拽
        out += ' <link rel="stylesheet" type="text/css" href="resources/css/ext-patch.css'
            + ts + '"/>' + this.NEW_LINE;
        out += '  <script type="text/javascript" src="resources/ext/ext-base.js'
            + ts + '" ><\/script>'
            + this.NEW_LINE;
        out += '  <script type="text/javascript" src="resources/ext/ext-all.js'
            + ts + '" ><\/script>'
            + this.NEW_LINE;
        out += '  <script type="text/javascript" src="resources/ext/ext-basex.js'
            + ts + '" ><\/script>'
            + this.NEW_LINE;
        out += '  <script type="text/javascript" src="resources/ext/ext-lang-zh_CN.js'
            + ts + '"><\/script>'
            + this.NEW_LINE;
        if (this.cfg_run_mode == 'DEV') {
            out += '  <script type="text/javascript" src="resources/ext/debug.js'
            + ts + '" ><\/script>'
                + this.NEW_LINE;
        }

        if (Ext.isIE) {
            out += '<script id="ie-deferred-loader" defer="defer" src="//:"></script>'
                + this.NEW_LINE;
        }

        out += '';
        return out;
    },
    buildImportEkingBase : function() {
        //var ts = '';
        var ts = '?ver='+this.build_id;
        var out = '';

        out += '  <script type="text/javascript" src="resources/js/lib/common.lib.js'
            + ts + '"><\/script>' + this.NEW_LINE;
        out += '  <script type="text/javascript" src="resources/js/lib/utils.js'
            + ts + '"><\/script>' + this.NEW_LINE;
        out += '  <script type="text/javascript" src="resources/js/ux/BufferView.js'
            + ts + '"><\/script>' + this.NEW_LINE;
        out += '  <script type="text/javascript" src="resources/js/lib/app.base.grid.js'
            + ts + '"><\/script>' + this.NEW_LINE;
        out += '  <script type="text/javascript" src="resources/js/lib/app.base.form.js'
            + ts + '"><\/script>' + this.NEW_LINE;
        out += '  <script type="text/javascript" src="resources/js/lib/exportexecl.js'
            + ts + '"><\/script>' + this.NEW_LINE;
        out += '  <script type="text/javascript" src="resources/js/lib/app.base.component.js'
            + ts + '"><\/script>' + this.NEW_LINE;
        out += '  <script type="text/javascript" src="resources/js/lib/lib.js'
            + ts + '"><\/script>' + this.NEW_LINE;

        out += '';
        return out;
    },
    buildImportDoc : function(jsUI) {
        var basePath = 'resources/js/';
        var out = '';
        var ts = '?ver='+this.build_id;
        //if (this.cfg_run_mode == 'DEV') {
        //  ts ='';
        //}

        var pDcArray = uiDcInc.get(jsUI)
        if (pDcArray != undefined) {
            for (var j = 0; j < pDcArray.length; j++) {
                out += '<script  type="text/javascript" src="' + basePath + pDcArray[j]
                    + ts + '"><\/script>' + this.NEW_LINE;
            }
        }

        out += '';
        return out;
    }
});