package org.ssh.sys.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.PropertyFilter;
import org.springside.modules.utils.ServiceException;
import org.ssh.sys.dao.RoleDao;
import org.ssh.sys.dao.UserDao;
import org.ssh.sys.entity.Role;
import org.ssh.sys.entity.User;

@Service
@Transactional
public class RoleService {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RoleDao roleDao;
    @Autowired
    private UserDao userDao;

    /**
     * 获取全部角色(grid使用)
     */
    @Transactional(readOnly = true)
    public Page<Role> getAllRoles(Page<Role> page, List<PropertyFilter> filters) {
        Page<Role> curPage = roleDao.findPage(page, filters);
        //List<Role> list = curPage.getResult();
        //curPage.setResult(list);
        return curPage;
    }

    /**
     * 获取全部角色(tree使用)
     */
    @Transactional(readOnly = true)
    public List<Role> getAllSysRoles() throws ServiceException {
        List<Role> result = new ArrayList<Role>();
        Map<String, Object> values = new HashMap<String, Object>();
        String hql = "from Role";
        result = roleDao.find(hql, values);
        return result;
    }

    /**
     * 保存角色
     */
    public void saveRole(Role role) throws ServiceException {
        Role o = null;
        Session session = this.roleDao.getSession();
        try {
            session.beginTransaction();
            if (role.isTransient()) {
                o = roleDao.findUniqueBy("name", role.getName());
            } else {
                o = roleDao.findUnique("from Role where name = ? and id <> ? ", role.getName(), role.getId());
            }
            if (o != null) {
                throw new Exception();
            }
            roleDao.save(role);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            if (o == null) {
                logger.error("保存角色失败：", e);
                throw new ServiceException("保存角色失败");
            } else {
                throw new ServiceException("此角色名已存在，不能重复添加!");
            }
        }
    }

    /**
     * 删除角色
     */
    public String delete(Long roleId) throws ServiceException {
        Session session = this.roleDao.getSession();
        String error = null;
        try {
            session.beginTransaction();
            Role role = roleDao.findUniqueBy("id", roleId);
            if (role != null) {
                if (role.getName().equals("admin")) {
                    logger.warn("操作员{}尝试删除超级管理员用户角色", "当前用户");
                    error = "不能删除超级管理员角色";
                    session.getTransaction().rollback();
                    return error;
                }
                List<User> list = userDao.getAll();
                for (User u : list) {
                    List<Role> roleList = u.getRoleList();
                    for (Role a : roleList) {
                        if (a == role) {
                            error = "该角色已经被用户:" + u.getName() + "使用,不能删除";
                            session.getTransaction().rollback();
                            return error;
                        }
                    }
                }
                roleDao.delete(role);
                session.getTransaction().commit();
            }
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error("删除角色失败:" + e);
            throw new ServiceException();
        }
        return error;
    }
}
