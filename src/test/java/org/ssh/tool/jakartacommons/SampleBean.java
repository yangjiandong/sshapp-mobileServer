package org.ssh.tool.jakartacommons;

import java.net.URL;
import java.util.List;
import java.util.Map;

public class SampleBean {
    String name;
    int age;
    String[] array;
    List<String> list;
    Map<String, String> map;
    URL url;
    NestedBean nestedBean;
    String date;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String[] getArray() {
        return array;
    }

    public void setArray(String[] array) {
        this.array = array;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public static class NestedBean {
        String nestedProperty;

        public String getNestedProperty() {
            return nestedProperty;
        }

        public void setNestedProperty(String nestedProperty) {
            this.nestedProperty = nestedProperty;
        }
    }

    public NestedBean getNestedBean() {
        return nestedBean;
    }

    public void setNestedBean(NestedBean nestedBean) {
        this.nestedBean = nestedBean;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}