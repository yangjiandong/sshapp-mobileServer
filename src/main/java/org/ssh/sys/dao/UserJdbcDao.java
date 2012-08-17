package org.ssh.sys.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springside.modules.utils.DBUtils;
import org.springside.modules.utils.UtilDateTime;
import org.springside.modules.utils.VelocityUtils;
import org.ssh.pm.common.utils.JdbcPage;
import org.ssh.pm.common.utils.JdbcPaginationHelper;
import org.ssh.sys.entity.User;
import org.ssh.sys.entity.UserLog;

import com.google.common.collect.Maps;

/**
 * User对象的Jdbc Dao, 演示Spring JdbcTemplate的使用.
 *
 */
@Component
public class UserJdbcDao {

    private static final String QUERY_USER_BY_ID = "select id, name, loginName from T_USERS where id=?";
    private static final String QUERY_USER_BY_IDS = "select id, name, loginName from T_USERS where id in(:ids)";
    private static final String QUERY_USER = "select id, name, loginName from T_USERs order by id";
    private static final String QUERY_USER_BY_LOGINNAME = "select id,name,loginName from T_USERS where loginName=:loginName";
    private static final String INSERT_USER = "insert into T_USERS(id, loginName, name, updatePwd) values(:id, :loginName, :name,:updatePwd)";

    private static Logger logger = LoggerFactory.getLogger(UserJdbcDao.class);

    private SimpleJdbcTemplate simpleJdbcTemplate;

    private JdbcTemplate jdbcTemplate;

    NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private SimpleJdbcInsert insertActor;

    private TransactionTemplate transactionTemplate;

    private String searchUserSql;

    private UserMapper userMapper = new UserMapper();

    private class UserMapper implements RowMapper<User> {
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setName(rs.getString("name"));
            user.setLoginName(rs.getString("loginName"));
            return user;
        }
    }

    //@Resource
    @Autowired
    public void setDataSource(@Qualifier("sysDataSource") DataSource dataSource) {
        simpleJdbcTemplate = new SimpleJdbcTemplate(dataSource);
        jdbcTemplate = new JdbcTemplate(dataSource);
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        insertActor = new SimpleJdbcInsert(dataSource).withTableName("t_actor")
        //使SimpleJdbcInsert仅使用这些字段作为insert语句所使用的字段
                .usingColumns("first_name", "last_name").usingGeneratedKeyColumns("id");
    }

    //@Resource
    @Autowired
    public void setDefaultTransactionManager(
            @Qualifier("sysdataTransactionManager") PlatformTransactionManager defaultTransactionManager) {
        transactionTemplate = new TransactionTemplate(defaultTransactionManager);
    }

    public void setSearchUserSql(String searchUserSql) {
        this.searchUserSql = searchUserSql;
    }

    /**
     * 查询单个对象.
     */
    public User queryObject(Long id) {
        return simpleJdbcTemplate.queryForObject(QUERY_USER_BY_ID, userMapper, id);
    }

    /**
     * 查询对象列表.
     */
    public List<User> queryObjectList() {
        return simpleJdbcTemplate.query(QUERY_USER, userMapper);
    }

    /**
     * 查询单个结果Map.
     */
    public Map<String, Object> queryMap(Long id) {
        return simpleJdbcTemplate.queryForMap(QUERY_USER_BY_ID, id);
    }

    /**
     * 查询结果Map列表.
     */
    public List<Map<String, Object>> queryMapList() {
        return simpleJdbcTemplate.queryForList(QUERY_USER);
    }

    /**
     * 使用Map形式的命名参数.
     */
    public User queryByNamedParameter(String loginName) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("loginName", loginName);

        return simpleJdbcTemplate.queryForObject(QUERY_USER_BY_LOGINNAME, userMapper, map);
    }

    /**
     * 使用Map形式的命名参数.
     */
    public List<User> queryByNamedParameterWithInClause(Long... ids) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("ids", Arrays.asList(ids));

        return simpleJdbcTemplate.query(QUERY_USER_BY_IDS, userMapper, map);
    }

    /**
     * 使用Bean形式的命名参数, Bean的属性名称应与命名参数一致.
     */
    public void createObject(User user) {
        //使用BeanPropertySqlParameterSource将User的属性映射为命名参数.
        BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(user);
        simpleJdbcTemplate.update(INSERT_USER, source);
    }

    /**
     * 批量创建/更新对象,使用Bean形式的命名参数.
     */
    public void batchCreateObject(List<User> userList) {
        int i = 0;
        BeanPropertySqlParameterSource[] sourceArray = new BeanPropertySqlParameterSource[userList.size()];

        for (User user : userList) {
            sourceArray[i++] = new BeanPropertySqlParameterSource(user);
        }

        simpleJdbcTemplate.batchUpdate(INSERT_USER, sourceArray);
    }

    /**
     * 使用freemarker创建动态SQL.
     */
    public List<User> searchUserByFreemarkerSqlTemplate(Map<String, ?> conditions) {
        String sql = VelocityUtils.render(searchUserSql, conditions);
        logger.info(sql);
        return simpleJdbcTemplate.query(sql, userMapper, conditions);
    }

    /**
     * 使用TransactionTemplate编程控制事务,一般在Manager/Service层
     * 无返回值的情形.
     */
    public void createUserInTransaction(User user) {

        final BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(user);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                simpleJdbcTemplate.update(INSERT_USER, source);
            }
        });
    }

    /**
     * 使用TransactionTemplate编程控制事务,一般在Manager/Service层
     * 有返回值的情形,并捕获异常进行处理不再抛出的情形.
     */
    public boolean createUserInTransaction2(User user) {

        final BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(user);
        return transactionTemplate.execute(new TransactionCallback<Boolean>() {
            public Boolean doInTransaction(TransactionStatus status) {
                try {
                    simpleJdbcTemplate.update(INSERT_USER, source);
                    return true;
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    status.setRollbackOnly();
                    return false;
                }
            }
        });
    }

    public SimpleJdbcCall createSimpleJdbcCall() {
        return new SimpleJdbcCall(this.jdbcTemplate);
    }

    //
    public List<User> queryBySp(String p) {
        SimpleJdbcCall jdbcCall = createSimpleJdbcCall();
        jdbcCall.getJdbcTemplate().setResultsMapCaseInsensitive(true);
        jdbcCall.withProcedureName("sp_get_all_user").returningResultSet("P_CURSOR",
                BeanPropertyRowMapper.newInstance(User.class));
        SqlParameterSource in = new MapSqlParameterSource().addValue("cdate", p);
        Map out = jdbcCall.execute(in);

        // oracle 下只能用这个名称需与后台定义的cursor名一致
        return (List) out.get("P_CURSOR");
        // Actor actor = new Actor();
        // actor.setId(id);
        // actor.setFirstName((String) out.get("out_first_name"));
        // actor.setLastName((String) out.get("out_last_name"));
        // actor.setBirthDate((Date) out.get("out_birth_date"));
        // return actor;

        //return jdbcTemplate.query(QUERY_USER_BY_IDS, userMapper);
    }

    /**
     * 获取服务器端的指定格式的当前时间字符串,oracle数据库的时间格式为"yyyy.MM.dd HH24:mi:ss"
     */
    public String getNowString(String format) {
        String sdate = UtilDateTime.nowDateString("yyyy.MM.dd HH:mm:ss");
        if (format.length() == 10) {
            sdate = UtilDateTime.nowDateString("yyyy.MM.dd");
        }

        DataSource dataSource = jdbcTemplate.getDataSource();
        Connection con = null;
        try {
            con = DataSourceUtils.getConnection(jdbcTemplate.getDataSource());
            String sql = "";
            //2011.05.02
            //连接泄露
            //(DBUtils.isOracle(jdbcTemplate2.getDataSource().getConnection()))
            //if (DBUtils.isOracle(con)) {
            if (DBUtils.isOracle(con)) {
                sql = "select to_char(sysdate,'yyyy-MM-dd HH24:mi:ss') as sys_date from dual";
            } else if (DBUtils.isMSSqlServer(con)) {
                sql = "Select CONVERT(varchar(100), GETDATE(), 120)";
            } else {
                sql = ""; // 其他数据库，则采用应用服务器系统时间
            }

            if (StringUtils.isNotEmpty(sql)) {
                Object result = this.jdbcTemplate.queryForObject(sql, null, String.class);
                if (result != null) {
                    sdate = result.toString();
                }

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = formatter.parse(sdate);
                sdate = new SimpleDateFormat(format).format(date);
            }

        } catch (Exception se) {
            logger.error("getNowString:", se);
        } finally {
            //安全释放
            DataSourceUtils.releaseConnection(con, dataSource);
        }

        return sdate;
    }

    /**
     * 获取服务器端的当前时间字符串
     */
    public String getNowString() {
        return getNowString("yyyy.MM.dd HH:mm:ss");
    }

    //spring jdbc example
    public void updateUser(User u) {
        //jdbcTemplate.execute("CREATE TABLE USER (user_id integer, name varchar(100))");
        simpleJdbcTemplate.update("UPDATE t_users SET name = ? WHERE user_id = ?",
                new Object[] { u.getName(), u.getId() });
        //simpleJdbcTemplate.update("INSERT INTO t_users VALUES(?, ?, ?, ?)", new Object[] {user.getId(), user.getName(), user.getSex(), user.getAge()});
        //int count = jdbcTemplate.queryForInt("SELECT COUNT(*) FROM USER");
        //String name = (String) jdbcTemplate.queryForObject("SELECT name FROM USER WHERE user_id = ?", new Object[] {id}, java.lang.String.class);
        //List rows = jdbcTemplate.queryForList("SELECT * FROM USER");
    }

    public List<User> getUsers(Long userId) throws SQLException {
        return jdbcTemplate.query("SELECT id, name,loginName FROM t_users WHERE id = ? ", userMapper, userId);
    }

    // http://www.codefutures.com/tutorials/spring-pagination/
    //page
    public JdbcPage<User> getUsersByJdbcpage(final int pageNo, final int pageSize, Long userId) throws SQLException {
        JdbcPaginationHelper<User> ph = new JdbcPaginationHelper<User>();
        return ph.fetchPage(jdbcTemplate, "SELECT count(*) FROM t_users ", "SELECT id, name,loginName FROM t_users",
        //new Object[]{userId},
                null, pageNo, pageSize, userMapper);

    }

    //http://ajava.org/online/spring2.5/html/jdbc.html
    //NamedParameterJdbcTemplate
    public int countOfActorsByFirstName(String firstName) {
        String sql = "select count(0) from T_ACTOR where first_name = :first_name";
        //SqlParameterSource namedParameters = new MapSqlParameterSource("first_name", firstName);
        Map namedParameters = Collections.singletonMap("first_name", firstName);
        return namedParameterJdbcTemplate.queryForInt(sql, namedParameters);
    }

    public void doExecute() {
        this.jdbcTemplate.execute("create table mytable (id integer, name varchar(100))");
    }

    public void getKey() {
        final String INSERT_SQL = "insert into my_test (name) values(?)";
        final String name = "Rob";

        //oracle 下正常工作
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(INSERT_SQL, new String[] { "id" });
                ps.setString(1, name);
                return ps;
            }
        }, keyHolder);

        Number id = keyHolder.getKey(); // keyHolder.getKey() now contains the generated key
    }

    public int[] batchUpdate(final List<User> actors) {
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(actors.toArray());
        int[] updateCounts = simpleJdbcTemplate.batchUpdate(
                "update t_actor set first_name = :firstName, last_name = :lastName where id = :id", batch);
        return updateCounts;
    }

    //使用SimpleJdbcInsert来获取自动生成的主键
    public void add(User actor) {
        //Map<String, Object> parameters = new HashMap<String, Object>(2);
        //parameters.put("first_name", actor.getUserName());
        //parameters.put("last_name", actor.getLoginName());
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("first_name", actor.getUserName())
                .addValue("last_name", actor.getLoginName());

        Number newId = insertActor.executeAndReturnKey(parameters);
        actor.setId(newId.longValue());

        //jdbc type
        //public Object mapRow(ResultSet rs, int rowNumber) throws SQLException {
        //    Customer cust = new Customer();
        //    cust.setId((Integer) rs.getObject("id"));
        //    cust.setName(rs.getString("name"));
        //    return cust;
        //}
        //Number id =
        //item.setId((Number) attr[0]).longValue());
        //Number <- Long,Integer,Double
    }

    //SqlRowSet
    public List<Map<String, String>> doExecuteSqlRowSet() {
        List<Map<String, String>> alls = new ArrayList<Map<String, String>>();

        SqlRowSet srs = jdbcTemplate.queryForRowSet("select * from t_users");
        int rowCount = 0;
        Map<String, String> m;
        while (srs.next()) {
            m = new HashMap<String, String>();
            m.put("userId", srs.getString("id"));
            m.put("userName", srs.getString("name"));
            m.put("loginName", srs.getString("loginname"));
            //m.put("email", UtilString.isNullToBlankString(srs.getString("email")));//保证oracle 下null值显示为空值
            m.put("email", StringUtils.defaultString(srs.getString("email")));//保证oracle 下null值显示为空值

            alls.add(m);

            rowCount++;
        }
        //System.out.println("Number of records : " + rowCount);

        //create table t_test (
        //ID         NUMBER(19) ,
        //LOGINNAME  VARCHAR2(20),
        //m          number(12,4)
        //)
        //jdbcTemplate.update("insert into t_test(id,LOGINNAME,m) values(?,?,?)","1","昨晚","12.34");

        //jdbcTemplate.execute("declare @t table(id int);insert into @t(1);insert into @t(2);select * from @t;");
        return alls;
    }

    protected MyUserLogMapper myUserLogMapper = new MyUserLogMapper();

    private class MyUserLogMapper implements RowMapper<UserLog> {
        public UserLog mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserLog q = new UserLog();
            q.setId(rs.getLong("id"));
            q.setUserId(rs.getLong("userId"));
            q.setVersion(rs.getInt("version"));
            q.setCreateTime(rs.getString("createTime"));
            q.setNetIp(rs.getString("netIp"));
            q.setEvents(rs.getString("events"));
            q.setTypes(rs.getString("types"));

            return q;
        }
    }

    public JdbcPage<UserLog> queryUserLog(final int pageNo, final int pageSize, Long userId) throws SQLException {
        JdbcPage<UserLog> jp = null;
        try {
            JdbcPaginationHelper<UserLog> ph = new JdbcPaginationHelper<UserLog>();
            jp = ph.fetchPage(jdbcTemplate, "SELECT count(*) FROM T_USERLOGS  where userId = " + userId,
                    "SELECT id,userId,version,netIp,events,createTime,types FROM T_USERLOGS where userId = " + userId
                            + " Order by createTime desc", null, pageNo, pageSize, myUserLogMapper);
        } catch (Exception e) {
            throw new SQLException(e);
        }
        return jp;
    }

    public Long getTotalCountUserLog(Long userId) throws SQLException {
        Long totalCount = 0L;
        try {
            totalCount = jdbcTemplate.queryForLong("SELECT count(*) FROM T_USERLOGS  where userId = " + userId);
        } catch (Exception e) {
            throw new SQLException(e);
        }
        return totalCount;
    }

}
