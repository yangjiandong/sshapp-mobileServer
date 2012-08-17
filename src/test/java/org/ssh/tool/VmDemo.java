package org.ssh.tool;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.ssh.pm.common.utils.html.table.Cell;
import org.ssh.pm.common.utils.html.table.Table;

public class VmDemo {
    public static void main(String[] args) throws Exception {
        Properties p = new Properties();
        p.put("file.resource.loader.class",	"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        p.put("input.encoding", "UTF-8");
        p.put("default.contentType", "text/html; charset/=UTF-8");
        p.put("output.encoding", "UTF-8");
        Velocity.init(p);
        //int rowNumber = 10;
        //int colNumber = 5;
        //Table table = new Table(rowNumber, colNumber);

        //Cell cell = new Cell("A first cell");
        //table.setCell(cell, 3, 2);
        //当你创建一个多行列的单元格的时候，你会觉得非常有趣。下面代码增加了一个3行4列的单元格。

        //Cell cellcell = new Cell("A more complex cell", 3, 4);
        //table.setCell(cellcell, 3, 2);

        //System.out.println(table.toString());;

        //http://www.javaworld.com/javaworld/jw-01-2008/jw-01-htmltables.html?page=1
        //baidu 让复杂的html表格在Apache Velocity中变得简单
        Table demoTable = new Table(10, 20);

        Cell box1 = new Cell("Box 1", 1, 5);
        Cell box2 = new Cell("Box 2", 3, 3);
        Cell box3 = new Cell("Box 3", 6, 3);
        Cell box4 = new Cell("Box 4", 3, 5);
        Cell box5 = new Cell("Box 5", 5, 4);
        Cell box6 = new Cell("Box 6", 4, 2);

        box1.setProperty("backgroundColor", "#33FF33");
        box2.setProperty("backgroundColor", "#FF6666");
        box3.setProperty("backgroundColor", "#FFFF00");
        box4.setProperty("backgroundColor", "#6600CC");
        box5.setProperty("backgroundColor", "#C0C0C0");
        box6.setProperty("backgroundColor", "#FFCC33");

        demoTable.setCell(box1, 0, 0);
        demoTable.setCell(box2, 1, 6);
        demoTable.setCell(box3, 4, 1);
        demoTable.setCell(box4, 6, 8);
        demoTable.setCell(box5, 0, 15);
        demoTable.setCell(box6, 6, 16);

        Template t2 = Velocity.getTemplate("vm/v2.vm");

        VelocityContext ctx2 = new VelocityContext();
        ctx2.put("table", demoTable);
        Writer writer = new StringWriter();
        t2.merge(ctx2, writer);
        System.out.println(writer);

      }
}
