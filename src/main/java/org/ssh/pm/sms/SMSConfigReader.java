package org.ssh.pm.sms;

import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

//import com.acl.ACL;

public class SMSConfigReader {
    private static SMSConfigReader reader = null;
    private Document doc = null;

    private SMSConfigReader() {
        SAXReader saxReader = new SAXReader();
        try {
            //doc = saxReader.read(this.getClass().getClassLoader().getResourceAsStream("com/config/SMSConfig.xml"));
            doc = saxReader.read(this.getClass().getClassLoader().getResourceAsStream("SMSConfig.xml"));
        } catch (DocumentException e1) {
            e1.printStackTrace();
        }
    }

    public static SMSConfigReader getReader() {
        if (reader == null) {
            reader = new SMSConfigReader();
        }
        return reader;
    }

    public String getSMSConfig(String config) {
        Element root = doc.getRootElement();
        for (Iterator i = root.elementIterator(); i.hasNext();) {
            Element e = (Element) i.next();
            if (e.getName().equals(config)) {
                return e.getStringValue().trim();
            }
        }
        return "";
    }
}