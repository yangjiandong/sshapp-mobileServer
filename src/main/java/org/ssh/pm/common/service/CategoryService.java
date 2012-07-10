package org.ssh.pm.common.service;

import java.sql.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.ssh.pm.common.dao.CategoryDao;
import org.ssh.pm.common.dao.TestTableDao;
import org.ssh.pm.common.entity.Category;
import org.ssh.pm.common.entity.TestTable;

@Component
//默认将类中的所有函数纳入事务管理.
@Transactional
public class CategoryService {
    private static Logger logger = LoggerFactory.getLogger(CategoryService.class);

    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private TestTableDao testTableDao;

    @Transactional(readOnly = true)
    public List<Category> getBooks3() {
        return categoryDao.getAll();
    }

    public void initData() {
        if (this.categoryDao.getCategoryCount().longValue() != 0) {
            Category b = this.categoryDao.findOneBy("name", "测试数据");
            if (b != null) {
                b.setName("test");
                categoryDao.save(b);
            }
        }

//        Category b = new Category();
//        b.setName("测试数据");
//        categoryDao.save(b);
//
//        b = new Category();
//        b.setName("测试数据,和你");
//        categoryDao.save(b);

        TestTable t = new TestTable();
        t.setName("test" );
        t.setNo("01");
        this.testTableDao.save(t);
    }

    public void cronData() {

        Category b = new Category();
        b.setName("测试数据");
        b.setCreateBy("cron");
        categoryDao.save(b);

        b = new Category();
        b.setName("测试数据,和你");
        b.setCreateBy("cron");
        categoryDao.save(b);
    }
}
