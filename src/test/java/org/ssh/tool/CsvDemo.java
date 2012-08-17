package org.ssh.tool;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.ssh.pm.common.utils.csv.CsvReader;
import org.ssh.pm.common.utils.csv.CsvWriter;

public class CsvDemo {

    static class ReslutBean {
        String help_keyword_id;
        String name;

        public String getHelp_keyword_id() {
            return help_keyword_id;
        }

        public void setHelp_keyword_id(String help_keyword_id) {
            this.help_keyword_id = help_keyword_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // 从获取将要写入csv文件的结果集
        List<ReslutBean> list = new ArrayList<ReslutBean>();
        ReslutBean re = new ReslutBean();
        re.setHelp_keyword_id("01");
        re.setName("中午");
        list.add(re);
        re = new ReslutBean();
        re.setHelp_keyword_id("012");
        re.setName("中午22");
        list.add(re);

        // 预组装csv文件内容标题行
        String[][] data = new String[list.size() + 1][2];
        data[0][0] = "Help_keyword_id";
        data[0][1] = "Name";

        // 预组装csv文件内容
        int len = list.size();
        for (int i = 0; i < len; i++) {
            data[i + 1][0] = list.get(i).getHelp_keyword_id();
            data[i + 1][1] = list.get(i).getName();
        }

        writerCsv("c://c测试.csv", data);
        readerCsv("c://c测试.csv");
    }

    /**
     * 读取csv
     *
     * @param csvFilePath
     * @throws Exception
     */
    public static void readerCsv(String csvFilePath) throws Exception {

        CsvReader reader = new CsvReader(csvFilePath, ',', Charset.forName("utf-8"));// shift_jis日语字体,utf-8
        reader.readHeaders();
        String[] headers = reader.getHeaders();

        List<Object[]> list = new ArrayList<Object[]>();
        while (reader.readRecord()) {
            list.add(reader.getValues());
        }
        Object[][] datas = new String[list.size()][];
        for (int i = 0; i < list.size(); i++) {
            datas[i] = list.get(i);
        }

        /*
         * 以下输出
         */

        for (int i = 0; i < headers.length; i++) {
            System.out.print(headers[i] + "\t");
        }
        System.out.println("");

        for (int i = 0; i < datas.length; i++) {
            Object[] data = datas[i]; // 取出一组数据
            for (int j = 0; j < data.length; j++) {
                Object cell = data[j];
                System.out.print(cell + "\t");
            }
            System.out.println("");
        }
    }

    /**
     * 写入csv
     *
     * @param csvFilePath文件名路径
     *            +文件名字
     * @param data数据项
     */
    public static void writerCsv(String csvFilePath, String[][] data) {

        CsvWriter writer = null;
        try {
            writer = new CsvWriter(csvFilePath, ',', Charset.forName("utf-8"));// shift_jis日语字体,utf-8

            for (int i = 0; i < data.length; i++) {
                writer.writeRecord(data[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
    }

}
