package org.ssh.tool.xml;

import java.io.File;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ReadXml extends TestCase {

    protected final Log logger = LogFactory.getLog(getClass());

    private SAXReader reader;
    private Document document;
    private File xmlFile;

    protected void setUp() throws Exception {
        xmlFile = new File(this.getClass().getResource("/data/migration/update.xml").getFile());

        reader = new SAXReader();
        document = reader.read(xmlFile);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testReadXml() {

        List childNodes = document.selectNodes("//updates/update");
        for (Object object : childNodes) {
            Element elm = (Element) object;
            org.dom4j.Attribute id = elm.attribute("id");
            org.dom4j.Attribute name = elm.attribute("name");
            org.dom4j.Attribute type = elm.attribute("sqlType");

            System.out.println("find id : " + id.getText());
            System.out.println("  name : " + name.getText());
            System.out.println("  type : " + type.getText());

            System.out.println("  sql : " + elm.getTextTrim());
        }

        System.out.println("fast look up by id");
        String id = "112";
        StringBuilder sb = new StringBuilder();
        sb.append("//updates/update[@id=");
        sb.append(id);
        sb.append("]");
        Element elm = (Element) document.selectSingleNode(sb.toString());
        if (elm != null) {
            System.out.println("find id=" + id + " : " + elm.getTextTrim());
        } else {
            System.out.println("find id=" + id + " : no find!");
        }

    }

}
