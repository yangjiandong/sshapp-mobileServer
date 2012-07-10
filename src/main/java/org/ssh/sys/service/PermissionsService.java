package org.ssh.sys.service;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.Page;
import org.springside.modules.utils.ServiceException;
import org.ssh.pm.orm.hibernate.EntityService;
import org.ssh.sys.dao.PermissionUsersDao;
import org.ssh.sys.dao.PermissionsDao;
import org.ssh.sys.dao.UserDao;
import org.ssh.sys.entity.PermissionUsers;
import org.ssh.sys.entity.Permissions;
import org.ssh.sys.entity.User;

@Service
@Transactional
public class PermissionsService extends EntityService<Permissions, Long> {
    @Autowired
    private PermissionsDao permissionsDao;
    @Autowired
    private PermissionUsersDao permissionUsersDao;
    @Autowired
    private UserDao userDao;

    @Override
    protected PermissionsDao getEntityDao() {
        return this.permissionsDao;
    }

    //初始化,导入权限
    public void initData() throws ServiceException {
        logger.debug("开始装载权限初始数据");

        File resourcetxt = new File(this.getClass().getResource("/data/permissions.txt").getFile());
        Session session = null;
        try {
            FileInputStream fis = new FileInputStream(resourcetxt);
            String thisLine;

            DataInputStream myInput = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(myInput, "UTF-8"));

            session = this.permissionsDao.getSession();
            session.beginTransaction();

            Permissions re;
            int line = 1;
            while ((thisLine = br.readLine()) != null) {
                //第一行是标题
                if (line == 1) {
                    line++;
                    continue;
                }
                String star[] = thisLine.split(",");
                if (star[0].trim().equals(""))
                    continue;

                re = new Permissions();
                re.setName(star[0]);
                re.setDescrip(star[1]);
                this.permissionsDao.save(re);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error("装载权限数据出错:" + e);
            throw new ServiceException("导入资源时，服务器发生异常");
        } finally {
            //br.close();
        }
    }

    public void savePermissionUsers(String codeList, String name) {
        try {
            permissionUsersDao.batchExecute("delete from PermissionUsers where name = ? ", name);
            if (codeList.length() > 1) {
                String[] codes = codeList.substring(1).split("\\|");
                for (String code : codes) {
                    PermissionUsers entity = new PermissionUsers();
                    entity.setExpuid(Long.valueOf(code));
                    entity.setName(name);
                    permissionUsersDao.save(entity);
                }
            }

        } catch (Exception e) {
            throw new ServiceException("保存权限对应用户失败");
        }
    }

    public Page<PermissionUsers> getPermissionUsers(Page<PermissionUsers> page, String name) {

        Page<PermissionUsers> curPage = permissionUsersDao.findPage(page, "from PermissionUsers where name = ? ", name);

        List<PermissionUsers> list = curPage.getResult();
        for (PermissionUsers a : list) {

            User user = userDao.findUniqueBy("id", a.getExpuid());
            if (user != null) {
                a.setUserName(user.getName());
            } else {
                a.setUserName(null);
            }

        }

        curPage.setResult(list);
        return curPage;

    }

    public Page<User> selPermissionUsers(Page<User> page, String name) {

        Page<User> curPage = userDao.findPage(page,
                "from User where id  in (select expuid from PermissionUsers where name = ? )", name);

        return curPage;

    }

    public Page<User> unselPermissionUsers(Page<User> page, String name) {

        Page<User> curPage = userDao.findPage(page,
                "from User where id not in (select expuid from PermissionUsers where name = ? )", name);

        return curPage;

    }

    public void deletePermissionUsers(Long id) {
        try {

            permissionUsersDao.batchExecute("delete from PermissionUsers where id = ? ", id);

        } catch (Exception e) {
            throw new ServiceException("删除权限对应用户失败");
        }
    }

    public boolean hasPermission(String name, Long expuid) {
        boolean flag = false;

        try {

            List<PermissionUsers> list = permissionUsersDao.findBy("expuid", expuid);
            if (list.size() == 0) {
                return flag;
            } else {
                for (PermissionUsers a : list) {
                    if (a.getName().equals(name)) {
                        flag = true;
                    }
                }
            }

        } catch (Exception e) {
            throw new ServiceException("匹配用户权限失败");
        }
        return flag;
    }
}