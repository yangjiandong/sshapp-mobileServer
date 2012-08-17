package org.ssh.pm.common.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

@SuppressWarnings("rawtypes")
public class MyCompare implements Comparator //实现Comparator，定义自己的比较方法
{
    public int compare(Object o1, Object o2) {
        Elem e1 = (Elem) o1;
        Elem e2 = (Elem) o2;

        if (e1.get() > e2.get())//1是升序，改为-1为降序
        {
            return 1;
        } else if (e1.get() < e2.get()) {
            return -1;
        } else {
            return 0;
        }
    }

    static class Employee {
        private String firstName;
        private String lastName;

        public Employee() {
        }

        public Employee(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        /**
         * @return Returns the firstName.
         */
        public String getFirstName() {
            return firstName;
        }

        /**
         * @param firstName The firstName to set.
         */
        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        /**
         * @return Returns the lastName.
         */
        public String getLastName() {
            return lastName;
        }

        /**
         * @param lastName The lastName to set.
         */
        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String toString() {
            return "## Firstname : " + this.firstName + ", Lastname : " + this.lastName;
        }
    }

    //example
    //Comparator ct = new MyCompare();
    //Collections.sort(allSubmenus, ct);
    public static void main(String[] args) {
        Vector<Elem> allSubMenus = new Vector<Elem>();
        allSubMenus.add(new Elem(9));
        allSubMenus.add(new Elem(1));
        allSubMenus.add(new Elem(2));
        allSubMenus.add(new Elem(8));
        allSubMenus.add(new Elem(7));

        Comparator ct = new MyCompare();
        Collections.sort(allSubMenus, ct);

        for (Iterator iterator = allSubMenus.iterator(); iterator.hasNext();) {
            String object = ((Elem) iterator.next()).get() + "";

            System.out.println(object);
        }

        //list sort
        Employee p = new Employee("Kumar", "Vinay");
        Employee p1 = new Employee("Vinay", "Kumar");
        Employee p2 = new Employee("Ankit", "Goyal");
        Employee p3 = new Employee("Pankaj", "Sharma");

        List<Employee> list = new ArrayList<Employee>();
        list.add(p);
        list.add(p1);
        list.add(p2);
        list.add(p3);

        Collections.sort(list, new Comparator<Employee>() {

            public int compare(Employee p1, Employee p2) {
                return p1.getFirstName().compareToIgnoreCase(p2.getFirstName());
            }

        });

        System.out.println(list);

        //array sort
        //生成一萬個隨數
        int[] intArray = new int[10000];
        for (int i = 0; i < 10000; i++) {
            int r = (int) (Math.random() * 10000);
            intArray[i] = r;
        }
        //以納秒的形式返回當前時間
        long l1 = System.nanoTime();
        //執行排序
        Arrays.sort(intArray);
        //排序后再次取得系統的納秒表示時間
        long l2 = System.nanoTime();
        //取得兩次時間差就可以知道用了多少時間
        System.out.println("Sort use time:" + (l2 - l1));

        for (int i = 0; i < 10000; i++) {
            System.out.println(intArray[i]);
        }
    }
}