package org.ssh.sys.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.test.utils.DataUtils;
import org.ssh.data.UserData;
import org.ssh.sys.entity.User;
import org.ssh.tool.TestDbUnitUtils;

import com.google.common.collect.Lists;

/**
 * UserJdbcDao的集成测试用例, 演示Spring Jdbc Template的使用.
 *
 * @author calvin
 */
@DirtiesContext
@TransactionConfiguration(transactionManager = "sysdataTransactionManager")
@ContextConfiguration(locations = { "/applicationContext-test.xml", "/common/applicationContext-jdbc.xml" })
@Transactional
public class UserJdbcDaoTest extends AbstractTransactionalJUnit4SpringContextTests {
    private static DataSource dataSourceHolder = null;
    @Autowired
    private UserJdbcDao userJdbcDao;

    protected DataSource dataSource;

    @Override
    @Autowired
    public void setDataSource(@Qualifier("sysDataSource") DataSource dataSource) {
        super.setDataSource(dataSource);
        this.dataSource = dataSource;
    }

    @Before
    public void loadDefaultData() throws Exception {
        if (dataSourceHolder == null) {
            TestDbUnitUtils.loadData(dataSource, "/test-sys-data.xml");

            dataSourceHolder = dataSource;
        }
    }

    @AfterClass
    public static void cleanDefaultData() throws Exception {
        TestDbUnitUtils.removeData(dataSourceHolder, "/test-sys-data.xml");
    }

    @Test
    public void queryObject() {
        User user = userJdbcDao.queryObject(1L);
        assertEquals("admin", user.getLoginName());

        List<User> resultlist = userJdbcDao.queryObjectList();
        assertEquals("admin", resultlist.get(0).getLoginName());

        Map<String, Object> resultMap = userJdbcDao.queryMap(1L);
        assertEquals("admin", resultMap.get("loginName"));

        List<Map<String, Object>> resultList = userJdbcDao.queryMapList();
        assertEquals("admin", resultList.get(0).get("loginName"));

        user = userJdbcDao.queryByNamedParameter("admin");
        assertEquals("admin", user.getLoginName());

        List<User> users = userJdbcDao.queryByNamedParameterWithInClause(1L, 2L);
        assertEquals(2, users.size());
        //    }
        //
        //    @Test
        //    public void test(){
        Map map = new HashMap();
        List<User> list = userJdbcDao.searchUserByFreemarkerSqlTemplate(map);
        assertTrue(list.size() > 1);

        map.clear();
        map.put("loginName", "admin");
        list = userJdbcDao.searchUserByFreemarkerSqlTemplate(map);
        assertEquals(1, list.size());

        map.clear();
        map.put("name", "Admin");
        list = userJdbcDao.searchUserByFreemarkerSqlTemplate(map);
        assertEquals(1, list.size());

        map.clear();
        map.put("loginName", "admin");
        map.put("name", "Admin");
        list = userJdbcDao.searchUserByFreemarkerSqlTemplate(map);
        assertEquals(1, list.size());
    }

    //    @Test
    //    public void queryObjectList() {
    //        List<User> resultlist = userJdbcDao.queryObjectList();
    //        assertEquals("admin", resultlist.get(0).getLoginName());
    //    }

    //    @Test
    //    public void queryMap() {
    //        Map<String, Object> resultMap = userJdbcDao.queryMap(1L);
    //        assertEquals("admin", resultMap.get("loginName"));
    //    }

    //    @Test
    //    public void queryMapList() {
    //        List<Map<String, Object>> resultList = userJdbcDao.queryMapList();
    //        assertEquals("admin", resultList.get(0).get("loginName"));
    //    }

    //    @Test
    //    public void queryByNamedParameter() {
    //        User user = userJdbcDao.queryByNamedParameter("admin");
    //        assertEquals("admin", user.getLoginName());
    //    }

    //    @Test
    //    public void queryByNamedParameterWithInClause() {
    //        List<User> users = userJdbcDao.queryByNamedParameterWithInClause(1L, 2L);
    //        assertEquals(6, users.size());
    //    }

    @Test
    public void createObject() {
        Long id = UserData.getUserId();
        User user = new User();
        user.setId(id);
        user.setLoginName(DataUtils.randomName("user"));
        user.setName(DataUtils.randomName("User"));
        user.setUpdatePwd("1");
        userJdbcDao.createObject(user);

        User newUser = userJdbcDao.queryObject(id);
        assertEquals(user.getLoginName(), newUser.getLoginName());
    }

    @Test
    public void batchCreateObject() {
        Long id1 = 11L;
        User user1 = new User();
        user1.setId(id1);
        user1.setLoginName(DataUtils.randomName("user"));
        user1.setName(DataUtils.randomName("User"));
        user1.setUpdatePwd("1");

        Long id2 = 12L;
        User user2 = new User();
        user2.setId(id2);
        user2.setLoginName(DataUtils.randomName("user"));
        user2.setName(DataUtils.randomName("User"));
        user2.setUpdatePwd("1");

        List<User> list = Lists.newArrayList(user1, user2);

        userJdbcDao.batchCreateObject(list);

        User newUser1 = userJdbcDao.queryObject(id1);
        assertEquals(user1.getLoginName(), newUser1.getLoginName());

        User newUser2 = userJdbcDao.queryObject(id2);
        assertEquals(user2.getLoginName(), newUser2.getLoginName());
    }

    //    @SuppressWarnings("unchecked")
    //    @Test
    //    public void searchObject() {
    //        Map map = new HashMap();
    //        List<User> list = userJdbcDao.searchUserByFreemarkerSqlTemplate(map);
    //        assertTrue(list.size() > 1);
    //
    //        map.clear();
    //        map.put("loginName", "admin");
    //        list = userJdbcDao.searchUserByFreemarkerSqlTemplate(map);
    //        assertEquals(1, list.size());
    //
    //        map.clear();
    //        map.put("name", "Admin");
    //        list = userJdbcDao.searchUserByFreemarkerSqlTemplate(map);
    //        assertEquals(1, list.size());
    //
    //        map.clear();
    //        map.put("loginName", "admin");
    //        map.put("name", "Admin");
    //        list = userJdbcDao.searchUserByFreemarkerSqlTemplate(map);
    //        assertEquals(1, list.size());
    //    }
}
