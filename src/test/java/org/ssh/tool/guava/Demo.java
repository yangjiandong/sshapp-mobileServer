package org.ssh.tool.guava;

import java.io.FileReader;
import java.io.Reader;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import org.ssh.sys.entity.User;

import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.MapMaker;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.io.CharStreams;
import com.google.common.io.Resources;

public class Demo {

    public static void main(String[] args) {
        List<String> list = Lists.newArrayList("oo", "aa");
        for (String string : list) {
            System.out.println(string);
        }
        Map<String, Integer> map = Maps.newHashMap();

        //不可变
        ImmutableList<Integer> mlist = ImmutableList.of(1, 2, 3);
        for (Integer i : mlist) {
            System.out.println(i);
        }

        ImmutableMap<String, Integer> mmap = ImmutableMap.of("1", 1, "2", 2, "3", 3);

        // builder
        ImmutableMap.Builder<String, Integer> builder = ImmutableMap.builder();
        for (int i = 0; i < 10; i++) {
            builder.put(i + "", i);
        }

        ImmutableMap<String, Integer> mmap2 = builder.build();

        //http://ajoo.iteye.com/blog/737291
        ConcurrentMap<String, Integer> cache = new MapMaker()
        // 如果一个计算一年有效 (嗯, 明年奖金减半!), 那么设一个过期时间
                .expiration(365, TimeUnit.DAYS).makeComputingMap(new Function<String, Integer>() {
                    @Override
                    public Integer apply(String name) {
                        return computeBonus(name);
                    }
                });

        //http://ajoo.iteye.com/blog/737718
        try {
            Reader reader = new FileReader("c:/o.txt");
            List<String> lines = CharStreams.readLines(reader);

            for (String s : lines) {
                System.out.println(s);
            }
        } catch (Exception e) {

        }

        //CharStreams.copy(reader, writer);
        //从Readable读取所有内容到一个字符串:
        //String content = CharStreams.toString(reader);

        //byte[] content = ByteStreams.toByteArray(inputStream)
        //ByteStreams.copy(inputStream, outputStream);

        //http://ajoo.iteye.com/blog/738286
        try {
            URL url = Resources.getResource("vm/v2.vm");
            System.out.println(Resources.toString(url, Charsets.UTF_8));

            List<String> lines = Resources.readLines(url, Charsets.UTF_8);
        } catch (Exception e) {

        }

        //http://ajoo.iteye.com/blog/738339
        //给力
        List<String> list2 = Lists.newArrayList("oo", "aa","1","2","3");
        String joined = Joiner.on(',').join(list2);
        System.out.println(joined);

        list2.add(null);
        joined = Joiner.on(',').useForNull("NA").join(list2);
        System.out.println(joined);

        joined = Joiner.on(',').skipNulls().join(list2);
        System.out.println(joined);

        for (String word : Splitter.on(',').split("ajoo,so,handsome!")) {
            System.out.println(word);
          }
          // 打印出 ajoo so handsome!
        
        List<String> list21 = Lists.newArrayList("oo", "aa","1","2","3");
        System.out.println("select the top 5 elements from a list of 10 million");
        //select the top 5 elements from a list of 10 million
        //http://www.michaelpollmeier.com/selecting-top-k-items-from-a-list-efficiently-in-java-groovy/
        List<String> list22 = com.google.common.collect.Ordering.natural()
        .greatestOf(list21, 2);
        for (String string : list22) {
            System.out.println(string);
        }
    }

    private static Integer computeBonus(String n) {
        return 1;
    }

    //http://ajoo.iteye.com/blog/740079
    //比较
    class PersonComparator implements Comparator<User> {
        @Override public int compare(User p1, User p2) {
          return ComparisonChain.start()
              .compare(p1.getLoginName(), p2.getLoginName(), Ordering.natural().nullsLast())
              .compare(p1.getEmail(), p2.getEmail())
              .compare(p1.getId(), p2.getId())
              .result();
        }
        //        natural(): 比较两个Comparable.
        //        reverse(): 把当前ordering反过来, 大的变小, 小的变大.
        //        compound(): 如果当前ordering比较结果是平局, 用另外一个Comparator做加时赛.
        //        nullsFirst(): 把null当作最小的, 排在前面.
        //        nullsLast(): null最大.
        //        binarySearch(): 根据当前ordering在排序列表里二分查找.
      }
}
