package org.ssh.sys.web;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.PropertyFilter;
import org.springside.modules.utils.JsonViewUtil;
import org.springside.modules.web.ServletUtils;
import org.ssh.pm.common.utils.JSONResponseUtil;
import org.ssh.pm.common.utils.JdbcPage;
import org.ssh.pm.common.utils.MySessionContext;
import org.ssh.pm.hcost.web.UserSession;
import org.ssh.pm.orm.hibernate.CustomerContextHolder;
import org.ssh.sys.entity.MyResource;
import org.ssh.sys.entity.PartDB;
import org.ssh.sys.entity.PermissionUsers;
import org.ssh.sys.entity.Permissions;
import org.ssh.sys.entity.Resource;
import org.ssh.sys.entity.Role;
import org.ssh.sys.entity.RoleResource;
import org.ssh.sys.entity.User;
import org.ssh.sys.entity.UserLog;
import org.ssh.sys.service.AccountManager;
import org.ssh.sys.service.AppSetupService;
import org.ssh.sys.service.PartDBService;
import org.ssh.sys.service.PermissionsService;
import org.ssh.sys.service.ResourcesService;
import org.ssh.sys.service.RoleResourceService;
import org.ssh.sys.service.RoleService;
import org.ssh.sys.service.UserLogService;
import org.ssh.sys.service.UserService;

//系统管理模块访问
@Controller
@RequestMapping("/system")
public class SystemController {
    private static Logger logger = LoggerFactory.getLogger(SystemController.class);

    @Autowired
    private AccountManager accountManager;
    @Autowired
    private ResourcesService resourcesService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleResourceService roleResourceService;
    @Autowired
    private UserService userService;
    @Autowired
    private PartDBService partDBService;
    @Autowired
    private AppSetupService appSetupService;
    @Autowired
    private PermissionsService permissionsService;
    @Autowired
    private UserLogService userLogService;

    @RequestMapping("/change_password")
    public @ResponseBody
    Map<String, Object> changePassword(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> re = accountManager.changeUserPassword(request);
        return re;
    }

    /**
     * 加载子系统
     */
    @RequestMapping("/loadsubsystem")
    public void loadSubSystem(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Resource> subSystem = new ArrayList<Resource>();
        Map<String, Object> map = new HashMap<String, Object>();

        UserSession u = (UserSession) request.getSession().getAttribute("userSession");
        if (u == null)
            return;

        try {
            subSystem = resourcesService.loadGrantedSubSystems(u.getModuleId(), u.getAccount());
            map.put("success", true);
            map.put("subSystems", subSystem);
        } catch (Exception e) {
            logger.error("", e);

            map.put("success", false);
            map.put("subSystems", subSystem);
        }
        JsonViewUtil.buildCustomJSONDataResponse(response, map);
    }

    /**
     * 加载子系统的菜单项
     */
    @RequestMapping("/loadmenu")
    public void loadMenu(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Resource> menus = new ArrayList<Resource>();
        String resourceId = request.getParameter("resourceId");

        UserSession u = (UserSession) request.getSession().getAttribute("userSession");
        if (u == null)
            return;

        menus = resourcesService.loadGrantedMenus(Long.valueOf(resourceId), u.getModuleId(), u.getAccount());
        JsonViewUtil.buildJSONResponse(response, menus);
    }

    /**
     * 加载子系统的菜单项
     */
    @RequestMapping("/loadmenu2")
    public void loadMenu2(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<MyResource> menus = new ArrayList<MyResource>();
        Map<String, Object> map = new HashMap<String, Object>();
        UserSession u = (UserSession) request.getSession().getAttribute("userSession");
        if (u == null)
            return;
        try {
            menus = resourcesService.loadGrantedMenus2(u.getModuleId(), u.getAccount());
            map.put("success", true);
            map.put("data", menus);

        } catch (Exception e) {
            logger.error("", e);
            map.put("success", false);
            map.put("data", null);
        }
        JsonViewUtil.buildCustomJSONDataResponse(response, map);
    }

    /**
     * 获取全部的系统资源信息
     */
    @RequestMapping("/get_all_sys_resources")
    public void getAllResources(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            List<Resource> data = resourcesService.loadAllResources();
            map.put("success", true);
            map.put("data", data);
        } catch (Exception e) {
            logger.error("", e);
            map.put("success", false);
            map.put("data", null);
        }
        JsonViewUtil.buildCustomJSONDataResponse(response, map);
    }

    /**
     * 获取指定角色授权的系统资源信息
     */
    @RequestMapping("/get_all_role_resources")
    public void getAllRoleResources(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            List<RoleResource> data = new ArrayList<RoleResource>();
            String roleId = request.getParameter("roleId");
            if (StringUtils.isNotBlank(roleId)) {
                data = roleResourceService.getAllRoleResources(Long.valueOf(roleId));
            }
            map.put("success", true);
            map.put("data", data);
        } catch (Exception e) {
            map.put("success", false);
            map.put("data", null);
        }
        JsonViewUtil.buildCustomJSONDataResponse(response, map);
    }

    /**
     * 保存指定角色的资源权限
     */
    @RequestMapping("/save_role_resources")
    public void saveRoleResources(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String roleId = request.getParameter("roleId");
            String menuIds = request.getParameter("menuIds");
            if (StringUtils.isNotBlank(roleId) && StringUtils.isNotBlank(menuIds)) {
                roleResourceService.saveRoleResource(Long.valueOf(roleId), menuIds);
            }
            map.put("success", true);
            map.put("message", "保存成功");
        } catch (Exception e) {
            map.put("success", false);
            map.put("message", "保存失败");
        }
        JsonViewUtil.buildCustomJSONDataResponse(response, map);
    }

    /**
     * 获取全部角色(grid使用)
     */
    @RequestMapping("/get_sys_roles")
    public void getAllRoles(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Page<Role> page = new Page<Role>(request);
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        String name = request.getParameter("name");
        if (StringUtils.isNotEmpty(name)) {
            PropertyFilter filter = new PropertyFilter("LIKES_name", name);
            filters.add(filter);
        }
        Page<Role> data = roleService.getAllRoles(page, filters);
        JsonViewUtil.buildJSONDataResponse(response, data.getResult(), (long) data.getTotalCount());
    }

    /**
     * 获取全部角色(tree使用)
     */
    @RequestMapping("/get_all_sys_roles")
    public void getAllSysRoles(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            List<Role> data = roleService.getAllSysRoles();
            map.put("success", true);
            map.put("data", data);
        } catch (Exception e) {
            map.put("success", false);
            map.put("data", null);
        }
        JsonViewUtil.buildCustomJSONDataResponse(response, map);
    }

    /**
     * 获取用户的角色
     */
    @RequestMapping("/get_roleids_by_userid")
    public void getRoleIdsByUserId(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            List<String> data = new ArrayList<String>();
            String userId = request.getParameter("userId");
            if (StringUtils.isNotBlank(userId)) {
                data = userService.getRoleIdsByUserId(Long.valueOf(userId));
            }
            map.put("success", true);
            map.put("data", data);
        } catch (Exception e) {
            map.put("success", false);
            map.put("data", null);
        }
        JsonViewUtil.buildCustomJSONDataResponse(response, map);
    }

    /**
     * 保存角色
     */
    @RequestMapping("/save_sys_role")
    public void saveRole(HttpServletRequest request, HttpServletResponse response, Role entity) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            roleService.saveRole(entity);
            map.put("success", true);
            map.put("message", "保存成功");
        } catch (Exception e) {
            map.put("success", false);
            map.put("message", e.getMessage());
        }
        JsonViewUtil.buildCustomJSONDataResponse(response, map);
    }

    /**
     * 删除角色
     */
    @RequestMapping("/delete_sys_role")
    public void deleteRole(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String id = request.getParameter("id");
            String error = roleService.delete(Long.valueOf(id));
            if (error == null) {
                map.put("success", true);
                map.put("message", "");
            } else {
                map.put("success", false);
                map.put("message", error);
            }

        } catch (Exception e) {
            map.put("success", false);
            map.put("message", "删除时服务器端发生异常，删除失败！");
        }
        JsonViewUtil.buildCustomJSONDataResponse(response, map);
    }

    /**
     * 获取全部用户
     */
    @RequestMapping("/get_sys_users")
    public void getAllUsers(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Page<User> page = new Page<User>(request);
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        String loginName = request.getParameter("loginName");
        if (StringUtils.isNotEmpty(loginName)) {
            PropertyFilter filter = new PropertyFilter("LIKES_loginName", loginName);
            filters.add(filter);
        }
        Page<User> data = userService.getAllUsers(page, filters);
        //		Bean bean = new Bean(true, "ok", data);
        //		JsonUtils.write(bean, response.getWriter(), new String[] { "hibernateLazyInitializer", "handler", "roleList",
        //				"partDBList" }, "yyyy.MM.dd");
        buildJSONDataResponse(response, data.getResult(), (long) data.getTotalCount());
    }

    public static void buildJSONDataResponse(HttpServletResponse response, List<? extends Object> data, Long total)
            throws Exception {
        JsonConfig cfg = new JsonConfig();
        cfg.setExcludes(new String[] { "handler", "hibernateLazyInitializer", "roleList", "partDBList", "RoleNames",
                "DBNames" });
        JSONArray jsonArray = JSONArray.fromObject(data, cfg);
        StringBuffer sb = new StringBuffer();
        sb.append("{\"totalCount\":" + total + ",\"rows\":");
        sb.append(jsonArray.toString());
        sb.append("}");
        String encoding = "UTF-8";
        boolean noCache = true;
        //response.setContentType("text/json; charset=UTF-8");
        response.setContentType(ServletUtils.JSON_TYPE + ";charset=" + encoding);
        if (noCache) {
            ServletUtils.setNoCacheHeader(response);
        }
        PrintWriter out = response.getWriter();
        out.write(sb.toString());
    }

    /**
     * 保存用户
     */
    @RequestMapping("/save_sys_user")
    public void saveUser(HttpServletRequest request, HttpServletResponse response, User entity) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            userService.saveUser(entity);
            map.put("success", true);
            map.put("message", "保存成功");
        } catch (Exception e) {
            map.put("success", false);
            map.put("message", e.getMessage());
        }
        JsonViewUtil.buildCustomJSONDataResponse(response, map);
    }

    /**
     * 删除用户
     */
    @RequestMapping("/delete_sys_user")
    public void deleteUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String id = request.getParameter("id");
            userService.delete(Long.valueOf(id));
            map.put("success", true);
            map.put("message", "");
        } catch (Exception e) {
            map.put("success", false);
            map.put("message", "删除时服务器端发生异常，删除失败！");
        }
        JsonViewUtil.buildCustomJSONDataResponse(response, map);
    }

    /**
     * 保存指定用户的角色权限
     */
    @RequestMapping("/save_user_roles")
    public void saveUserRoles(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String userId = request.getParameter("userId");
            String roleIds = request.getParameter("roleIds");
            if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(roleIds)) {
                userService.saveUserRoles(Long.valueOf(userId), roleIds);
            }
            map.put("success", true);
            map.put("message", "保存成功");
        } catch (Exception e) {
            map.put("success", false);
            map.put("message", "保存失败");
        }
        JsonViewUtil.buildCustomJSONDataResponse(response, map);
    }

    /**
     * 重置用户密码
     */
    @RequestMapping("/reset_user_password")
    public void resetUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String userId = request.getParameter("userId");

        try {
            userService.resetUser(Long.valueOf(userId));
            map.put("success", true);
            map.put("message", "");
        } catch (Exception e) {
            map.put("success", false);
            map.put("message", "服务器端发生异常，重置密码失败！");
        }

        JsonViewUtil.buildCustomJSONDataResponse(response, map);
    }

    /**
     * 启用用户
     */
    @RequestMapping("/start_user")
    public void startUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String userId = request.getParameter("userId");

        try {
            userService.startUser(Long.valueOf(userId));
            map.put("success", true);
            map.put("message", "");
        } catch (Exception e) {
            map.put("success", false);
            map.put("message", "服务器端发生异常，启用失败！");
        }

        JsonViewUtil.buildCustomJSONDataResponse(response, map);
    }

    /**
     * 停用用户
     */
    @RequestMapping("/stop_user")
    public void stopUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String userId = request.getParameter("userId");

        try {
            userService.stopUser(Long.valueOf(userId));
            map.put("success", true);
            map.put("message", "");
        } catch (Exception e) {
            map.put("success", false);
            map.put("message", "服务器端发生异常，停用处理失败！");
        }

        JsonViewUtil.buildCustomJSONDataResponse(response, map);
    }

    /**
     * 获取全部数据库
     */
    @RequestMapping("/getalldb")
    public void getAllDb(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            List<PartDB> data = partDBService.getAllDb();
            map.put("success", true);
            map.put("data", data);
        } catch (Exception e) {
            map.put("success", false);
            map.put("data", null);
        }
        JsonViewUtil.buildCustomJSONDataResponse(response, map);
    }

    /**
     * 根据用户id获取该用户可以使用的数据库和不可以使用的数据库
     */
    @RequestMapping("/getseldb")
    public void getSelDb(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String userId = request.getParameter("userId");
        try {
            List<JSONObject> data = userService.getSelDb(Long.valueOf(userId));
            map.put("success", true);
            map.put("data", data);
        } catch (Exception e) {
            map.put("success", false);
            map.put("data", null);
        }
        JsonViewUtil.buildCustomJSONDataResponse(response, map);
    }

    /**
     * 根据用户id获取该用户可以使用的数据库
     */
    @RequestMapping("/get_user_dbs")
    public void getUserDBS(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        UserSession u = (UserSession) request.getSession().getAttribute("userSession");
        try {
            if (u == null) {
                map.put("success", false);
                map.put("data", null);
            } else {
                List<PartDB> data = userService.getUserDBS(Long.valueOf(u.getAccount().getId()));
                map.put("success", true);
                map.put("data", data);
                map.put("totalCount", data.size());
                map.put("module", u.getModuleId());
            }
        } catch (Exception e) {
            logger.error("", e);
            map.put("success", false);
            map.put("data", null);
        }
        JsonViewUtil.buildCustomJSONDataResponse(response, map);
    }

    /**
     * 根据用户id获取该用户可以使用的一个数据库
     */
    @RequestMapping("/get_user_one_db")
    public @ResponseBody
    Map<String, Object> getUserOneDB(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        UserSession u = (UserSession) request.getSession().getAttribute("userSession");
        try {
            if (u == null) {
                map.put("success", false);
                map.put("data", null);
            } else {
                List<PartDB> data = userService.getUserDBS(Long.valueOf(u.getAccount().getId()));
                map.put("success", true);
                map.put("data", data.get(0));
                //map.put("module", u.getModuleId());
            }
        } catch (Exception e) {
            logger.error("", e);

            map.put("success", false);
            map.put("data", null);
        }
        return map;
    }

    /**
     * 直接修改用户session中的partDB
     */
    @RequestMapping("/update_user_db")
    public void updateUserDB(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        UserSession u = (UserSession) request.getSession().getAttribute("userSession");
        String dbcode = request.getParameter("dbcode");
        try {
            if (u == null) {
                map.put("success", false);
            } else {
                PartDB entity = partDBService.getDBByCode(dbcode);
                u.setPartDb(entity);

                CustomerContextHolder.setCustomerType(CommonController.getUserCurrentPartDB(request));
                u.setUsereditbydefault(appSetupService.getValue("user_edit.by.default"));

                map.put("success", true);
                map.put("dbname", entity.getDbname());
            }
        } catch (Exception e) {
            logger.error("", e);

            map.put("success", false);
        }
        JsonViewUtil.buildCustomJSONDataResponse(response, map);
    }

    /**
     * 获取全部权限
     */
    @RequestMapping("/getPermissions")
    public void getPermissions(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Page<Permissions> page = new Page<Permissions>(request);
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        Page<Permissions> data = permissionsService.search(page, filters);

        buildJSONDataResponse(response, data.getResult(), (long) data.getTotalCount());
    }

    /**
     * 获取权限对应的用户
     */
    @RequestMapping("/getPermissionUsers")
    public void getPermissionUsers(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Page<PermissionUsers> page = new Page<PermissionUsers>(request);
        String name = request.getParameter("name");

        Page<PermissionUsers> data = permissionsService.getPermissionUsers(page, name);

        buildJSONDataResponse(response, data.getResult(), (long) data.getTotalCount());
    }

    /**
     * 获取权限对应的用户(已选)
     */
    @RequestMapping("/selPermissionUsers")
    public void selPermissionUsers(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Page<User> page = new Page<User>(request);
        String name = request.getParameter("name");

        Page<User> data = permissionsService.selPermissionUsers(page, name);

        buildJSONDataResponse(response, data.getResult(), (long) data.getTotalCount());
    }

    /**
     * 获取权限对应的用户(未选)
     */
    @RequestMapping("/unselPermissionUsers")
    public void unselPermissionUsers(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Page<User> page = new Page<User>(request);
        String name = request.getParameter("name");

        Page<User> data = permissionsService.unselPermissionUsers(page, name);

        buildJSONDataResponse(response, data.getResult(), (long) data.getTotalCount());
    }

    /**
     * 保存权限所对应的用户
     */
    @RequestMapping("/savePermissionUsers")
    public void savePermissionUsers(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String codeList = request.getParameter("codeList");
        String name = request.getParameter("name");
        try {

            permissionsService.savePermissionUsers(codeList, name);
            map.put("success", true);
            map.put("data", null);
        } catch (Exception e) {
            map.put("success", false);
            map.put("data", null);
        }
        JsonViewUtil.buildCustomJSONDataResponse(response, map);
    }

    /**
     * 删除权限所对应的用户
     */
    @RequestMapping("/deletePermissionUsers")
    public void deletePermissionUsers(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String id = request.getParameter("id");
        try {

            permissionsService.deletePermissionUsers(Long.valueOf(id));
            map.put("success", true);
            map.put("data", null);
        } catch (Exception e) {
            map.put("success", false);
            map.put("data", null);
        }
        JsonViewUtil.buildCustomJSONDataResponse(response, map);
    }

    /**
     * 获取全部用户
     */
    @RequestMapping("/getPermissionOneUser")
    public void getPermissionOneUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Page<User> page = new Page<User>(request);
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        String query = request.getParameter("query");
        if (StringUtils.isNotEmpty(query)) {
            PropertyFilter filter = new PropertyFilter("LIKES_loginName_OR_name", query);
            filters.add(filter);
        }
        Page<User> data = userService.getAllUsers(page, filters);
        //		Bean bean = new Bean(true, "ok", data);
        //		JsonUtils.write(bean, response.getWriter(), new String[] { "hibernateLazyInitializer", "handler", "roleList",
        //				"partDBList" }, "yyyy.MM.dd");
        buildJSONDataResponse(response, data.getResult(), (long) data.getTotalCount());
    }

    /**
     * 用户页面注销
     */
    @RequestMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = accountManager.logout(request);
        //JsonViewUtil.buildCustomJSONDataResponse(response, map);

        return "redirect:index";
    }

    /**
     * 系统管理员管理
     */
    @RequestMapping("/manage")
    public String manage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return "manage";
    }

    /**
     * 院长查询模块
     */
    @RequestMapping("/query")
    public String query(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return "query";
    }

    /**
     * comet
     */
    @RequestMapping("/comet")
    public String comet(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return "comet";
    }

    @RequestMapping("/user_log")
    public @ResponseBody
    Map<String, Object> userLog(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> re = new HashMap<String, Object>();
        boolean pro = true;

        String infos = request.getParameter("infos");
        UserSession u = (UserSession) request.getSession().getAttribute("userSession");
        if (u == null) {
            pro = false;
        } else {
            UserLog uLog = new UserLog();
            uLog.setUserId(u.getAccount().getId());
            uLog.setCreateTime(accountManager.getNowString());
            uLog.setEvents(infos);
            uLog.setNetIp(u.getClientIp());
            userLogService.save(uLog);
        }
        re.put("success", pro);
        return re;
    }

    @RequestMapping("/get_online_users")
    public @ResponseBody
    Map<String, Object> getOnLineUsers(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", true);

        long start = System.currentTimeMillis();
        List<UserInfos> datas = new ArrayList<UserInfos>();
        try {

            MySessionContext myc = MySessionContext.getInstance();
            HashMap<String, Object> alls = myc.getAllSession();
            Iterator<String> iterator = alls.keySet().iterator();

            while (iterator.hasNext()) {
                HttpSession session = (HttpSession) alls.get(iterator.next());
                UserSession userSession = (UserSession) session.getAttribute("userSession");
                UserInfos u = new UserInfos();
                u.setIp(userSession.getClientIp());
                u.setUserName(userSession.getAccount().getUserName());
                u.setLoginTime(userSession.getLoginTime());

                datas.add(u);
            }

            map.put("datas", datas);
            map.put("times", (System.currentTimeMillis() - start) + " ms");
            map.put("totalCount", datas.size());
        } catch (Exception e) {
            map.put("success", false);
        }
        return map;
    }

    class UserInfos {
        String userName;
        String ip;
        String loginTime;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getLoginTime() {
            return loginTime;
        }

        public void setLoginTime(String loginTime) {
            this.loginTime = loginTime;
        }
    }

    /**
     * 获取用户的操作信息
     */
    @RequestMapping("/query_user_log")
    public void queryUserLog(HttpServletRequest request, HttpServletResponse response) throws Exception {

        List<UserLog> alls = null;
        Long m = 0L;
        JdbcPage<UserLog> resultList = userLogService.queryUserLog(request);
        if (resultList != null) {
            alls = resultList.getPageItems();
            m = userLogService.getTotalCount(request);
        }

        JSONResponseUtil.buildJSONDataResponse(response, alls, m);
    }

    @RequestMapping("/has_module")
    public @ResponseBody
    Map<String, Object> getMigrations(@RequestParam("moduleId") Long moduleId) throws Exception {
        Map<String, Object> re = new HashMap<String, Object>();
        Long c = this.resourcesService.countData(moduleId);

        re.put("success", true);
        re.put("count", c);

        return re;
    }

    @RequestMapping("/import_his_user")
    public @ResponseBody
    Map<String, Object> importHisUser() {
        Map<String, Object> re = new HashMap<String, Object>();
        boolean su = true;
        try {
            userService.importHisUser();
        } catch (Exception e) {
            logger.error("", e);
            su = false;
        }
        re.put("success", su);
        return re;
    }
}
