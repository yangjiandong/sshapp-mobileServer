function downloadViewData(grid, fileName) {

  var cm = grid.getColumnModel();
  var colCount = cm.getColumnCount();
  var temp_obj = [];
  var coloum_obj = '';
  // var dataIndex = '';
  for (i = 0; i < colCount; i++) {
    // 显示列的列标题
    if (!cm.isHidden(i)) {
      temp_obj.push(cm.getDataIndex(i))
      // dataIndex = dataIndex + '|' + cm.getDataIndex(i);
      coloum_obj = coloum_obj + '|' + cm.getColumnHeader(i);
    }
  }
  //
  var data_obj = '';
  var store = grid.getStore();
  var recordCount = store.getCount();
  for (i = 0; i < recordCount; i++) {
    var data_temp = '';
    for (j = 0; j < temp_obj.length; j++) {
      var data = store.data.items[i].data;
      data_temp = data_temp + ',' + (eval("data." + temp_obj[j]));
    }
    data_obj = data_obj + '|' + data_temp;
  }

  // var data_obj = [];
  // var store = grid.getStore();
  // var recordCount = store.getCount();
  // for (i = 0; i < recordCount; i++) {
  // data_obj.push(store.getAt(i).data)
  // }

  // var cm = grid.getColumnModel();
  // var colCount = cm.getColumnCount();
  // var temp_obj = [];
  // var coloum_obj = [];
  // for (i = 0; i < colCount; i++) {
  // // 显示列的列标题
  // if (!cm.isHidden(i)) {
  // temp_obj.push(cm.getDataIndex(i))
  // coloum_obj.push(cm.getColumnHeader(i));
  // }
  // }

  // var data_obj = [];
  // var data_temp = [];
  // var store = grid.getStore();
  // var recordCount = store.getCount();
  // for (i = 0; i < recordCount; i++) {
  // for (j = 0; j < temp_obj.length; j++) {
  // var data = store.data.items[i].data;
  // data_temp.push(eval("data." + temp_obj[j]));
  // }
  // data_obj.push(data_temp);
  // }

  Ext.Ajax.request({
    url : "common/exportExecl",
    method : "POST",
    params : {
      "coloum" : coloum_obj,
      "data" : data_obj,
      "fileName" : fileName
    },
    callback : function(o, s, r) {
      var v = window
          .open("about:blank ", '导出到execl',
              'adress=no,width=710,height=450,scrollbars=yes,resizable=yes,menubar=yes');
      v.focus();
    }
  });

  // var v = window
  // .open("common/exportExecl?" + Ext.urlEncode({
  // coloum : coloum_obj,
  // dataIndex : Ext.encode(dataIndex),
  // data : data_obj,
  // fileName : fileName
  // }), '导出到execl',
  // 'adress=no,width=710,height=450,scrollbars=yes,resizable=yes,menubar=yes');
  // v.focus();

  // var v = window
  // .open("common/exportExecl?coloum=" + coloum_obj + "&data="
  // + Ext.urlEncode(data_obj) + "&fileName=" + fileName, '导出到execl',
  // 'adress=no,width=710,height=450,scrollbars=yes,resizable=yes,menubar=yes');
  // v.focus();

}
function exportExeclNoPage(grid, fileName) {
  var vExportContent = newGrid.getExcelXml(fileName);
  if (Ext.isIE6 || Ext.isIE7 || Ext.isSafari || Ext.isSafari2 || Ext.isSafari3) {
    var fd = Ext.get('frmDummy');
    if (!fd) {
      fd = Ext.DomHelper.append(Ext.getBody(), {
            tag : 'form',
            method : 'post',
            id : 'frmDummy',
            // action : 'common/exportexcel',
            target : '_blank',
            name : 'frmDummy',
            cls : 'x-hidden',
            cn : [{
                  tag : 'input',
                  name : 'exportContent',
                  id : 'exportContent',
                  type : 'hidden'
                }]
          }, true);
    }
    fd.child('#exportContent').set({
          value : vExportContent
        });
    fd.dom.submit();
  } else {
    document.location = 'data:application/vnd.ms-excel;base64,'
        + Base64.encode(vExportContent);
  }
}

function exportExecl(grid, fileName) {

  var newGrid = new Divo.Base.GridView({
        entityName : grid.entityName,
        queryFlds : grid.queryFlds,
        searchFlds : grid.searchFlds,
        cm : grid.colModel,
        packageName : grid.packageName,
        queryUrl : grid.queryUrl,
        jsonId : grid.jsonId,
        view : grid.view,
        recordPk : grid.recordPk
      });
  newGrid.getStore().load({
        scope : this,
        params : {
          isExport : 'Y'
        }
      });

  newGrid.getStore().on('load', function() {
    // downloadViewData(newGrid, fileName);
    var vExportContent = newGrid.getExcelXml(fileName);
    if (Ext.isIE6 || Ext.isIE7 || Ext.isSafari || Ext.isSafari2
        || Ext.isSafari3) {
      var fd = Ext.get('frmDummy');
      if (!fd) {
        fd = Ext.DomHelper.append(Ext.getBody(), {
              tag : 'form',
              method : 'post',
              id : 'frmDummy',
              // action : 'common/exportexcel',
              target : '_blank',
              name : 'frmDummy',
              cls : 'x-hidden',
              cn : [{
                    tag : 'input',
                    name : 'exportContent',
                    id : 'exportContent',
                    type : 'hidden'
                  }]
            }, true);
      }
      fd.child('#exportContent').set({
            value : vExportContent
          });
      fd.dom.submit();
    } else {
      document.location = 'data:application/vnd.ms-excel;base64,'
          + Base64.encode(vExportContent);
    }
  })

}

Ext.override(Ext.grid.GridPanel, {
  getExcelXml : function(fileName) {
    var worksheet = this.createWorksheet(fileName);
    var totalWidth = this.getColumnModel().getTotalWidth();
    return '<xml version="1.0" encoding="utf-8">'
        + '<ss:Workbook xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns:o="urn:schemas-microsoft-com:office:office">'
        + '<o:DocumentProperties><o:Title>'
        + this.title
        + '</o:Title></o:DocumentProperties>'
        + '<ss:ExcelWorkbook>'
        + '<ss:WindowHeight>'
        + worksheet.height
        + '</ss:WindowHeight>'
        + '<ss:WindowWidth>'
        + worksheet.width
        + '</ss:WindowWidth>'
        + '<ss:ProtectStructure>False</ss:ProtectStructure>'
        + '<ss:ProtectWindows>False</ss:ProtectWindows>'
        + '</ss:ExcelWorkbook>'
        + '<ss:Styles>'
        + '<ss:Style ss:ID="Default">'
        + '<ss:Alignment ss:Vertical="Top" ss:WrapText="1" />'
        + '<ss:Font ss:FontName="arial" ss:Size="10" />'
        + '<ss:Borders>'
        + '<ss:Border  ss:Weight="1" ss:LineStyle="Continuous" ss:Position="Top" />'
        + '<ss:Border  ss:Weight="1" ss:LineStyle="Continuous" ss:Position="Bottom" />'
        + '<ss:Border  ss:Weight="1" ss:LineStyle="Continuous" ss:Position="Left" />'
        + '<ss:Border  ss:Weight="1" ss:LineStyle="Continuous" ss:Position="Right" />'
        + '</ss:Borders>'
        + '<ss:Interior />'
        + '<ss:NumberFormat />'
        + '<ss:Protection />'
        + '</ss:Style>'
        + '<ss:Style ss:ID="title">'
        + '<ss:Borders />'
        + '<ss:Font />'
        + '<ss:Alignment ss:WrapText="1" ss:Vertical="Center" ss:Horizontal="Center" />'
        + '<ss:NumberFormat ss:Format="@" />' + '</ss:Style>'
        + '<ss:Style ss:ID="headercell">'
        + '<ss:Font ss:Bold="1" ss:Size="10" />'
        + '<ss:Alignment ss:WrapText="1" ss:Horizontal="Center" />'
        + '<ss:Interior ss:Pattern="Solid"  />' + '</ss:Style>'
        + '<ss:Style ss:ID="even">' + '<ss:Interior ss:Pattern="Solid"  />'
        + '</ss:Style>' + '<ss:Style ss:Parent="even" ss:ID="evendate">'
        + '<ss:NumberFormat ss:Format="yyyy-mm-dd" />' + '</ss:Style>'
        + '<ss:Style ss:Parent="even" ss:ID="evenint">'
        + '<ss:NumberFormat ss:Format="0" />' + '</ss:Style>'
        + '<ss:Style ss:Parent="even" ss:ID="evenfloat">'
        + '<ss:NumberFormat ss:Format="0.00" />' + '</ss:Style>'
        + '<ss:Style ss:ID="odd">' + '<ss:Interior ss:Pattern="Solid"  />'
        + '</ss:Style>' + '<ss:Style ss:Parent="odd" ss:ID="odddate">'
        + '<ss:NumberFormat ss:Format="yyyy-mm-dd" />' + '</ss:Style>'
        + '<ss:Style ss:Parent="odd" ss:ID="oddint">'
        + '<ss:NumberFormat ss:Format="0" />' + '</ss:Style>'
        + '<ss:Style ss:Parent="odd" ss:ID="oddfloat">'
        + '<ss:NumberFormat ss:Format="0.00" />' + '</ss:Style>'
        + '</ss:Styles>' + worksheet.xml + '</ss:Workbook>';
  },

  createWorksheet : function(fileName) {
    // Calculate cell data types and extra class names which affect formatting
    var cellType = [];
    var cellTypeClass = [];
    var cm = this.getColumnModel();
    var totalWidthInPixels = 0;
    var colXml = '';
    var headerXml = '';
    var visibleColumnCountReduction = 0;
    var colCount = cm.getColumnCount();
    for (var i = 0; i < colCount; i++) {
      if ((cm.getDataIndex(i) != '') && (!cm.isHidden(i))) {
        var w = cm.getColumnWidth(i)
        totalWidthInPixels += w;
        if (cm.getColumnHeader(i) === "") {
          cellType.push("None");
          cellTypeClass.push("");
          ++visibleColumnCountReduction;
        } else {
          colXml += '<ss:Column ss:AutoFitWidth="1" ss:Width="' + w + '" />';
          headerXml += '<ss:Cell ss:StyleID="headercell">'
              + '<ss:Data ss:Type="String">' + cm.getColumnHeader(i)
              + '</ss:Data>'
              + '<ss:NamedCell ss:Name="Print_Titles" /></ss:Cell>';
          var fld = this.store.recordType.prototype.fields.get(cm
              .getDataIndex(i));
          switch (fld.type) {
            case "int" :
              cellType.push("Number");
              cellTypeClass.push("int");
              break;
            case "float" :
              cellType.push("Number");
              cellTypeClass.push("float");
              break;
            case "bool" :
            case "boolean" :
              cellType.push("String");
              cellTypeClass.push("");
              break;
            case "date" :
              cellType.push("DateTime");
              cellTypeClass.push("date");
              break;
            default :
              cellType.push("String");
              cellTypeClass.push("");
              break;
          }
        }
      }
    }
    var visibleColumnCount = cellType.length - visibleColumnCountReduction;

    var result = {
      height : 9000,
      width : Math.floor(totalWidthInPixels * 30) + 50
    };

    // Generate worksheet header details.
    var t = '<ss:Worksheet ss:Name="'
        + this.title
        + '">'
        + '<ss:Names>'
        + '<ss:NamedRange ss:Name="Print_Titles" ss:RefersTo="=\''
        + this.title
        + '\'!R1:R2" />'
        + '</ss:Names>'
        + '<ss:Table x:FullRows="1" x:FullColumns="1"'
        + ' ss:ExpandedColumnCount="'
        + (visibleColumnCount + 2)
        + '" ss:ExpandedRowCount="'
        + (this.store.getCount() + 2)
        + '">'
        + colXml
        + '<ss:Row ss:Height="38">'
        + '<ss:Cell ss:StyleID="title" ss:MergeAcross="'
        + (visibleColumnCount - 1)
        + '">'
        + '<ss:Data xmlns:html="http://www.w3.org/TR/REC-html40" ss:Type="String">'
        + '<html:B>' + fileName
        + ' </html:B></ss:Data><ss:NamedCell ss:Name="Print_Titles" />'
        + '</ss:Cell>' + '</ss:Row>' + '<ss:Row ss:AutoFitHeight="1">'
        + headerXml + '</ss:Row>';

    // Generate the data rows from the data in the Store
    for (var i = 0, it = this.store.data.items, l = it.length; i < l; i++) {
      t += '<ss:Row>';
      var cellClass = (i & 1) ? 'odd' : 'even';
      r = it[i].data;
      var k = 0;
      for (var j = 0; j < colCount; j++) {
        if ((cm.getDataIndex(j) != '') && (!cm.isHidden(j))) {
          var v = r[cm.getDataIndex(j)];
          if (cellType[k] !== "None") {
            t += '<ss:Cell ss:StyleID="' + cellClass + cellTypeClass[k]
                + '"><ss:Data ss:Type="' + cellType[k] + '">';
            if (cellType[k] == 'DateTime') {
              t += v.format('Y-m-d');
            } else {
              t += v;
            }
            t += '</ss:Data></ss:Cell>';
          }
          k++;
        }
      }
      t += '</ss:Row>';
    }

    result.xml = t
        + '</ss:Table>'
        + '<x:WorksheetOptions>'
        + '<x:PageSetup>'
        + '<x:Layout x:CenterHorizontal="1" x:Orientation="Landscape" />'
        + '<x:Footer x:Data="Page &amp;P of &amp;N" x:Margin="0.5" />'
        + '<x:PageMargins x:Top="0.5" x:Right="0.5" x:Left="0.5" x:Bottom="0.8" />'
        + '</x:PageSetup>' + '<x:FitToPage />' + '<x:Print>'
        + '<x:PrintErrors>Blank</x:PrintErrors>' + '<x:FitWidth>1</x:FitWidth>'
        + '<x:FitHeight>32767</x:FitHeight>' + '<x:ValidPrinterInfo />'
        + '<x:VerticalResolution>600</x:VerticalResolution>' + '</x:Print>'
        + '<x:Selected />' + '<x:DoNotDisplayGridlines />'
        + '<x:ProtectObjects>False</x:ProtectObjects>'
        + '<x:ProtectScenarios>False</x:ProtectScenarios>'
        + '</x:WorksheetOptions>' + '</ss:Worksheet>';
    return result;
  }
});

var Base64 = (function() {
  // Private property
  var keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";

  // Private method for UTF-8 encoding
  function utf8Encode(string) {
    string = string.replace(/\r\n/g, "\n");
    var utftext = "";
    for (var n = 0; n < string.length; n++) {
      var c = string.charCodeAt(n);
      if (c < 128) {
        utftext += String.fromCharCode(c);
      } else if ((c > 127) && (c < 2048)) {
        utftext += String.fromCharCode((c >> 6) | 192);
        utftext += String.fromCharCode((c & 63) | 128);
      } else {
        utftext += String.fromCharCode((c >> 12) | 224);
        utftext += String.fromCharCode(((c >> 6) & 63) | 128);
        utftext += String.fromCharCode((c & 63) | 128);
      }
    }
    return utftext;
  }

  // Public method for encoding
  return {
    encode : (typeof btoa == 'function') ? function(input) {
      return btoa(utf8Encode(input));
    } : function(input) {
      var output = "";
      var chr1, chr2, chr3, enc1, enc2, enc3, enc4;
      var i = 0;
      input = utf8Encode(input);
      while (i < input.length) {
        chr1 = input.charCodeAt(i++);
        chr2 = input.charCodeAt(i++);
        chr3 = input.charCodeAt(i++);
        enc1 = chr1 >> 2;
        enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
        enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
        enc4 = chr3 & 63;
        if (isNaN(chr2)) {
          enc3 = enc4 = 64;
        } else if (isNaN(chr3)) {
          enc4 = 64;
        }
        output = output + keyStr.charAt(enc1) + keyStr.charAt(enc2)
            + keyStr.charAt(enc3) + keyStr.charAt(enc4);
      }
      return output;
    }
  };
})();
