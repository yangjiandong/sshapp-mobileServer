package org.ssh.sys.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.dozer.DozerBeanMapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.memcached.SpyMemcachedClient;
import org.springside.modules.orm.hibernate.HibernateDao;
import org.springside.modules.security.utils.DigestUtils;
import org.springside.modules.utils.ServiceException;
import org.ssh.pm.common.cache.MemcachedObjectType;
import org.ssh.pm.common.jms.simple.NotifyMessageProducer;
import org.ssh.pm.common.jmx.server.ServerConfig;
import org.ssh.pm.common.utils.JdbcPage;
import org.ssh.pm.common.utils.MySessionContext;
import org.ssh.pm.enums.CoreConstants;
import org.ssh.pm.hcost.web.UserSession;
import org.ssh.sys.dao.PartDBDao;
import org.ssh.sys.dao.RoleDao;
import org.ssh.sys.dao.UserDao;
import org.ssh.sys.dao.UserJdbcDao;
import org.ssh.sys.dao.UserLogDao;
import org.ssh.sys.dao.UserSessionIdsDao;
import org.ssh.sys.entity.PartDB;
import org.ssh.sys.entity.Role;
import org.ssh.sys.entity.User;
import org.ssh.sys.entity.UserLog;
import org.ssh.sys.entity.UserSessionIds;
import org.ssh.sys.entity.his.HisUser;

import com.ek.mobileapp.model.UserDTO;

/**
 * 用户管理类.
 *
 */
@Service
public class AccountManager {
    private static Logger logger = LoggerFactory.getLogger(AccountManager.class);

    private UserDao userDao;

    private UserJdbcDao userJdbcDao;

    private SpyMemcachedClient spyMemcachedClient;

    private JsonMapper jsonBinder = JsonMapper.buildNonDefaultMapper();

    private ServerConfig serverConfig; //系统配置

    private NotifyMessageProducer notifyProducer; //JMS消息发送

    //private ShiroDbRealm shiroRealm;

    private RoleDao roleDao;

    private HttpSession session;

    @Autowired
    private PartDBDao partDBDao;
    @Autowired
    private UserSessionIdsDao userSessionIdsDao;
    @Autowired
    private UserLogDao userLogDao;
    @Autowired
    private DozerBeanMapper dozer;


    /**
     * 在保存用户时,发送用户修改通知消息, 由消息接收者异步进行较为耗时的通知邮件发送.
     *
     * 如果企图修改超级用户,取出当前操作员用户,打印其信息然后抛出异常.
     *
     */
    //演示指定非默认名称的TransactionManager.
    //@Transactional("defaultTransactionManager")
    //采用两个事务,需手工控制事务
    public void saveUser(User user) {

        if (isSupervisor(user)) {
            logger.warn("操作员{}尝试修改超级管理员用户", SecurityUtils.getSubject().getPrincipal());
            throw new ServiceException("不能修改超级管理员用户");
        }

        //String shaPassword = encoder.encodePassword(user.getPassword(), null);
        String shaPassword = DigestUtils.sha1ToHex(user.getPassword());
        user.setPassword(shaPassword);

        Session session = this.userDao.getSession();
        session.beginTransaction();
        userDao.save(user);
        session.getTransaction().commit();

        //if (shiroRealm != null) {
        //    shiroRealm.clearCachedAuthorizationInfo(user.getLoginName());
        //}

        sendNotifyMessage(user);
    }

    /**
     * 判断是否超级管理员.
     */
    private boolean isSupervisor(User user) {
        return (user.getLoginName() != null && user.getLoginName().equals("Admin"));
    }

    public JSONObject getSystemConfig() {

        return null;
    }

    @Transactional(readOnly = true)
    public User getUser(Long id) {
        return userDao.get(id);
    }

    /**
     * 取得用户, 并对用户的延迟加载关联进行初始化.
     */
    @Transactional(readOnly = true)
    public User getLoadedUser(Long id) {
        if (spyMemcachedClient != null) {
            logger.debug("use memecache!!!");
            return getUserFromMemcached(id);
        } else {
            return userJdbcDao.queryObject(id);
        }
    }

    /**
     * 访问Memcached, 使用JSON字符串存放对象以节约空间.
     */
    private User getUserFromMemcached(Long id) {

        String key = MemcachedObjectType.USER.getPrefix() + id;

        User user = null;
        String jsonString = spyMemcachedClient.get(key);

        if (jsonString == null) {
            //用户不在 memcached中,从数据库中取出并放入memcached.
            //因为hibernate的proxy问题多多,此处使用jdbc
            user = userJdbcDao.queryObject(id);
            if (user != null) {
                jsonString = jsonBinder.toJson(user);
                spyMemcachedClient.set(key, MemcachedObjectType.USER.getExpiredTime(), jsonString);
            }
        } else {
            user = jsonBinder.fromJson(jsonString, User.class);
        }
        return user;
    }

    /**
     * 按名称查询用户, 并对用户的延迟加载关联进行初始化.
     */
    @Transactional(readOnly = true)
    public User searchLoadedUserByName(String name) {
        User user = userDao.findUniqueBy("name", name);
        userDao.initUser(user);
        return user;
    }

    /**
     * 取得所有用户, 预加载用户的角色.
     */
    @Transactional(readOnly = true)
    public List<User> getAllUserWithRole() {
        List<User> list = userDao.getAllUserWithRoleByDistinctHql();
        logger.info("get {} user sucessful.", list.size());
        return list;
    }

    /**
     * 获取当前用户数量.
     */
    @Transactional(readOnly = true)
    public Long getUserCount() {
        return userDao.getUserCount();
    }

    @Transactional(readOnly = true)
    public User findUserByLoginName(String loginName) {
        return userDao.findUniqueBy("loginName", loginName);
    }

    /**
     * 批量修改用户状态.
     */
    public void disableUsers(List<Long> ids) {
        userDao.disableUsers(ids);
    }

    /**
     * 发送用户变更消息.
     *
     * 同时发送只有一个消费者的Queue消息与发布订阅模式有多个消费者的Topic消息.
     */
    private void sendNotifyMessage(User user) {
        if (serverConfig != null && serverConfig.isNotificationMailEnabled() && notifyProducer != null) {
            try {
                notifyProducer.sendQueue(user);
                notifyProducer.sendTopic(user);
            } catch (Exception e) {
                logger.error("消息发送失败", e);
            }
        }
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setUserJdbcDao(UserJdbcDao userJdbcDao) {
        this.userJdbcDao = userJdbcDao;
    }

    @Autowired(required = false)
    public void setServerConfig(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    @Autowired(required = false)
    public void setNotifyProducer(NotifyMessageProducer notifyProducer) {
        this.notifyProducer = notifyProducer;
    }

    @Autowired(required = false)
    public void setSpyMemcachedClient(SpyMemcachedClient spyMemcachedClient) {
        this.spyMemcachedClient = spyMemcachedClient;
    }

    //初始
    @Transactional
    public void initData() {
        if (this.userDao.getUserCount().longValue() != 0) {
            return;
        }

        Role r = new Role();
        r.setName("admin");
        r.setDesc("系统管理员角色");
        this.roleDao.save(r);

        List<Role> rs = new ArrayList<Role>();
        rs.add(r);

        PartDB p = new PartDB();
        p.setDbcode("default");
        p.setDbname("全院");
        partDBDao.save(p);

        List<PartDB> ps = new ArrayList<PartDB>();
        ps.add(p);

        User user = new User();
        user.setName("管理员");
        user.setLoginName("Admin");
        user.setPassword(DigestUtils.sha1ToHex("123"));
        user.setEmail("admin@gmail.com");
        user.setCreateBy("初始化");
        user.setStatus(CoreConstants.USER_ENABLED);
        user.setUpdatePwd(CoreConstants.USER_UPDATED_PASSWD);
        //add role
        user.setRoleList(rs);
        user.setPartDBList(ps);

        //String shaPassword = encoder.encodePassword(user.getPassword(), null);
        String shaPassword = user.getPassword();
        user.setPassword(shaPassword);

        userDao.save(user);
    }

    @Transactional(readOnly = true)
    public User login(String loginName, String password) throws ServiceException {
        User user = userDao.findUniqueBy("loginName", loginName);

        if (user != null) {
            if (password.equals(user.getPassword())) {
                return user;
            } else {
                return null;
            }
        }
        return null;
    }

    /**
     * 登录检查
     */
    @Transactional
    public Map<String, Object> userLogin(HttpServletRequest request) throws ServiceException {
        session = request.getSession(true);
        session.removeAttribute("userSession");

        Map<String, Object> map = new HashMap<String, Object>();

        String userName = request.getParameter("username");
        String password = request.getParameter("password");
        String clientIp = request.getRemoteAddr();
        //mobile
        String type = request.getParameter("type");

        String lastIp = null;
        String loginTime = getNowString();

        boolean checked = true;
        String message = "";

        User user = userDao.findUniqueBy("loginName", userName);

        if (user != null) {
            if (DigestUtils.sha1ToHex(password).equals(user.getPassword())) {
                if (!user.getStatus().equals(CoreConstants.USER_DISABLED)) {

                    UserSession userSession = new UserSession(user);
                    userSession.setClientIp(clientIp);
                    userSession.setCurrentPassword(password);
                    userSession.setLoginTime(loginTime);
                    session.setAttribute("userSession", userSession);

                    try {
                        String sessionId = session.getId();
                        List<UserSessionIds> list = userSessionIdsDao.findBy("userId", user.getId());
                        final MySessionContext myc = MySessionContext.getInstance();
                        if (list.size() > 0) {
                            for (UserSessionIds a : list) {
                                lastIp = a.getIp();
                                HttpSession sessionEntity = myc.getSession(a.getSessionId());
                                if (sessionEntity == null) {
                                    userSessionIdsDao.batchExecute("delete from UserSessionIds where userId = ? ",
                                            user.getId());
                                    continue;
                                }
                                try {
                                    sessionEntity.invalidate();
                                } catch (Exception e) {
                                    logger.error("logon:", e);
                                }

                                myc.DelSession(sessionEntity);
                            }
                            userSessionIdsDao
                                    .batchExecute("delete from UserSessionIds where userId = ? ", user.getId());

                        }

                        myc.AddSession(session);
                        UserSessionIds entity = new UserSessionIds();
                        entity.setUserId(user.getId());
                        entity.setSessionId(sessionId);
                        entity.setIp(clientIp);
                        userSessionIdsDao.save(entity);

                        UserLog uLog = new UserLog();
                        uLog.setUserId(user.getId());
                        uLog.setCreateTime(loginTime);
                        uLog.setEvents(uLog.EVENT_LOGIN);
                        uLog.setNetIp(clientIp);
                        if (!org.apache.commons.lang3.StringUtils.isBlank(type)) {
                            uLog.setTypes(type);

                            UserDTO u = dozer.map(user, UserDTO.class);

                            map.put("user", u);
                        }
                        userLogDao.save(uLog);

                        //
                        //sendNotifyMessage(user);
                    } catch (Exception e) {
                        logger.error("logon:", e);
                        throw new ServiceException(e);
                    }

                } else {
                    checked = false;
                    message = "用户被禁用";
                }
            } else {
                checked = false;
                message = "密码错误";
            }
            userName = user.getName();
            if (userName.equals(""))
                userName = user.getLoginName();
        } else {
            checked = false;
            message = "用户名错误";
        }
        map.put("success", checked);
        map.put("message", message);
        map.put("userName", userName);
        map.put("lastIp", lastIp);
        return map;
    }

    /**
     * 用户注销
     */
    @Transactional
    public Map<String, Object> logout(HttpServletRequest request) throws ServiceException {
        Map<String, Object> map = new HashMap<String, Object>();

        String message = "注销失败";
        boolean checked = false;

        try {
            session = request.getSession(false);

            UserSession userSession = (UserSession) session.getAttribute("userSession");

            if (userSession != null) {
                User user = userSession.getAccount();
                userSessionIdsDao.batchExecute("delete from UserSessionIds where userId = ? ", user.getId());

                UserLog uLog = new UserLog();
                uLog.setUserId(user.getId());
                uLog.setCreateTime(getNowString());
                uLog.setEvents(uLog.EVENT_LOGOUT);
                uLog.setNetIp(userSession.getClientIp());
                userLogDao.save(uLog);
            }

            final MySessionContext myc = MySessionContext.getInstance();
            myc.DelSession(session);

            if (session != null) {
                session.invalidate();
            }

            checked = true;
            message = "OK";
        } catch (Exception e) {
            e.printStackTrace();
            checked = false;
            message = "注销时，后台发生异常，注销失败";
        }
        map.put("success", checked);
        map.put("message", message);

        return map;
    }

    @Autowired
    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    /**
     * by sp 取得所有用户.
     */
    @Transactional(readOnly = true)
    public List<User> getAllUserBySp() {
        List<User> list = userJdbcDao.queryBySp("2011.01.01");
        //logger.info("get {} user sucessful.", list.size());
        return list;
    }

    public void createUserInTransaction2(User u) {
        this.userJdbcDao.createUserInTransaction2(u);
    }

    @Transactional
    public Map<String, Object> changeUserPassword(HttpServletRequest request) throws ServiceException {

        Map<String, Object> map = new HashMap<String, Object>();

        String oldp = request.getParameter("old_password");
        String newP = request.getParameter("new_password");
        String newP2 = request.getParameter("new_password2");
        boolean check = true;
        String msg = "ok";
        session = request.getSession(false);
        UserSession userSession = (UserSession) session.getAttribute("userSession");
        //UserSession userSession = CommonService.getUserSession();

        if (!oldp.equalsIgnoreCase(userSession.getCurrentPassword())) {
            check = false;
            msg = "原密码不正确";
        } else if (StringUtils.isBlank(newP)) {
            check = false;
            msg = "密码不能为空";
        } else if (!newP.equalsIgnoreCase(newP2)) {
            check = false;
            msg = "密码不匹配";
        } else {

            try {
                User user = userSession.getAccount();

                //String shaPassword = encoder.encodePassword(newP, null);
                String shaPassword = DigestUtils.sha1ToHex(newP);
                user.setPassword(shaPassword);
                user.setUpdatePwd(CoreConstants.USER_UPDATED_PASSWD);

                this.userDao.save(user);
                //不再需要重新赋给userSession
                userSession.setCurrentPassword(newP);
                logger.debug(userSession.getAccount().getUpdatePwd());

            } catch (Exception e) {
                logger.error("changepassword:", e);
                check = false;
                msg = "修改密码失败";
            }

        }

        map.put("success", check);
        map.put("message", msg);
        return map;
    }

    /**
     * 保存用户修改后的密码
     */
    @Transactional
    public Map<String, Object> savePassword(String userName, String newPassword) throws ServiceException {
        Map<String, Object> map = new HashMap<String, Object>();
        String message = "";
        boolean checked = false;
        Session session = this.userDao.getSession();
        try {
            session.beginTransaction();
            User user = userDao.findUniqueBy("name", userName);
            if (user != null) {
                //String shaPassword = encoder.encodePassword(newPassword, null);
                String shaPassword = newPassword;
                user.setPassword(shaPassword);
            }
            userDao.save(user);
            session.getTransaction().commit();
            checked = true;
            message = "OK";
        } catch (Exception e) {
            session.getTransaction().rollback();
            //e.printStackTrace();
            logger.error("savePassword:", e);
            checked = false;
            message = "修改密码失败";
        }
        map.put("success", checked);
        map.put("message", message);
        return map;
    }

    @Transactional(readOnly = true)
    public String getNowString() {
        return this.userJdbcDao.getNowString("yyyy.MM.dd HH:mm:ss");
    }

    @Transactional(readOnly = true)
    public String getNowString(String fm) {
        return this.userJdbcDao.getNowString(fm);
    }

    @Transactional(readOnly = true)
    public String getNowDateString() {
        return this.userJdbcDao.getNowString("yyyy.MM.dd");
    }

    @Transactional(readOnly = true)
    public String getDataTime() {
        return this.userDao.getDatabaseTime();
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getUserByJdbc(Long id) {
        Map<String, Object> resultMap = userJdbcDao.queryMap(id);
        return resultMap;
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getUserListByJdbc() {
        List<Map<String, Object>> resultList = userJdbcDao.queryMapList();
        return resultList;

    }

    @Transactional(readOnly = true)
    public JdbcPage<User> getUserListByJdbcPage(final int pageNo, final int pageSize) {
        try {
            JdbcPage<User> resultList = userJdbcDao.getUsersByJdbcpage(pageNo, pageSize, 1L);
            return resultList;
        } catch (Exception e) {
            logger.error("getUserListByJdbcPage:", e);
            return null;
        }

    }

    @Transactional(readOnly = true)
    public List<Map<String, String>> doExecuteSqlRowSet() {
        return this.userJdbcDao.doExecuteSqlRowSet();
    }

    public static void main(String[] args) {
        System.out.println(DigestUtils.sha1ToHex("12345"));
    }

}
