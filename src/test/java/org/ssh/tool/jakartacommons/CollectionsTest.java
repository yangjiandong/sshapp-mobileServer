package org.ssh.tool.jakartacommons;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.Bag;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.bag.HashBag;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.comparators.NullComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.collections.iterators.ArrayListIterator;
import org.apache.commons.collections.iterators.FilterIterator;
import org.apache.commons.collections.iterators.LoopingIterator;
import org.apache.commons.collections.iterators.UniqueFilterIterator;
import org.junit.Test;
import org.ssh.pm.common.entity.Category;

//http://heis.iteye.com/blog/567867
public class CollectionsTest {
    @Test
    public void testIterator() {
        List<String> books = new ArrayList<String>();
        books.add("EnglishBook");
        books.add("Commons Cookbook");
        books.add("Who Moved My Cheese");

        StringBuilder alls = new StringBuilder();
        //当迭代到最后的元素后，再返回第一个元素重新循环，直至达到迭代次数为止
        LoopingIterator iterator = new LoopingIterator(books);
        for (int i = 0; i < 5; i++) {
            String book = (String) iterator.next();
            alls.append(book + ";");
            //System.out.print(book+";");
        }

        assertTrue(alls.toString().equals(
                "EnglishBook;Commons Cookbook;Who Moved My Cheese;EnglishBook;Commons Cookbook;"));
    }

    @Test
    public void testArrayListIterator() {

        String[] arrays = new String[] { "a", "b", "c", "d", "f" };
        StringBuilder alls = new StringBuilder();
        //遍历下标为1到4的元素
        Iterator<String> iterator = new ArrayListIterator(arrays, 1, 4);
        while (iterator.hasNext()) {
            alls.append(iterator.next() + "; ");
            //System.out.print(iterator.next() + "; ");
        }
        //->b; c; d;
        assertTrue(alls.toString().equals("b; c; d; "));

    }

    @Test
    public void testFilterIterator() {

        List<Integer> list = new ArrayList<Integer>(Arrays.asList(new Integer[] { 7, 9, 35, 67, 88 }));

        //过滤出大于30的元素
        Predicate predicate = new Predicate() {
            public boolean evaluate(Object object) {
                int num = ((Integer) object).intValue();
                return num > 30;
            }
        };

        StringBuilder alls = new StringBuilder();
        Iterator<Integer> iterator = new FilterIterator(list.iterator(), predicate);
        while (iterator.hasNext()) {
            alls.append(iterator.next() + "; ");
            //System.out.print(iterator.next() + "; ");
        }
        //->35; 67; 88;
        assertTrue(alls.toString().equals("35; 67; 88; "));
    }

    @Test
    public void testUniqueFilterIterator() {
        List<String> list = new ArrayList<String>(Arrays.asList(new String[] { "a", "b", "c", "b", "a" }));

        StringBuilder alls = new StringBuilder();
        Iterator<String> iterator = new UniqueFilterIterator(list.iterator());
        while (iterator.hasNext()) {
            alls.append(iterator.next() + "; ");
            //System.out.print(iterator.next()+"; ");
        }
        //->a; b; c;
        assertTrue(alls.toString().equals("a; b; c; "));
    }

    @Test
    public void testHashBag() {

        Bag bag1 = new HashBag();
        bag1.add("book1", 10);
        bag1.add("book2", 20);

        Bag bag2 = new HashBag();
        bag2.add("book2", 5);
        bag2.add("book3", 10);

        bag1.add("book1");
        bag1.remove("book1", 2);
        //减去bag2内相应元素的数量
        bag1.removeAll(bag2);

        System.out.println("book1: " + bag1.getCount("book1"));
        System.out.println("book2: " + bag1.getCount("book2") + "\n");

        //bag1保留bag2内的元素，简单来说就是求交集
        bag1.retainAll(bag2);
        System.out.println("book1: " + bag1.getCount("book1"));
        System.out.println("book2: " + bag1.getCount("book2"));
        System.out.println("book3: " + bag1.getCount("book3"));
        //        ->
        //        book1: 9
        //        book2: 15
        //
        //        book1: 0
        //        book2: 5
        //        book3: 0
    }
    
    //http://heis.iteye.com/blog/537626

    //使用ReverseComparator实现反顺序排列
    @Test
    public void testComparator() throws Exception {
        List<Category> alls = new ArrayList<Category>();
        Category g = new Category();
        g.setName("LiLei");
        g.setId(20L);
        alls.add(g);
        g = new Category();
        g.setName("Ark");
        g.setId(23L);
        alls.add(g);
        g = new Category();
        g.setName("hanMeimei");
        g.setId(25L);
        alls.add(g);

        for (Category category : alls) {
            System.out.println(category.getName());
        }

        Comparator nameComparor = new org.apache.commons.beanutils.BeanComparator("name");
        Comparator reverseComparator = new ReverseComparator(nameComparor);
        Collections.sort(alls, reverseComparator);

        //第二个参数为false，则name属性为null的值排在非null的前面；  
        //如果为true，则反之。如果不加第二个参数，则默认为true；  
        //Comparator nullComparator = new NullComparator(nameComparor, false);

        for (Category category : alls) {
            System.out.println(category.getName());
        }

        //结合多个Comparator进行排序
        nameComparor = new org.apache.commons.beanutils.BeanComparator("name");
        Comparator ageComparator = new org.apache.commons.beanutils.BeanComparator("id");
        ComparatorChain comparatorChain = new ComparatorChain();
        comparatorChain.addComparator(nameComparor);
        comparatorChain.addComparator(ageComparator);
        Collections.sort(alls, comparatorChain);
    }
}
