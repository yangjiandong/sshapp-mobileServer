package org.ssh.sys.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.utils.ServiceException;
import org.ssh.sys.dao.RoleResourceDao;
import org.ssh.sys.entity.RoleResource;

@Service
@Transactional
public class RoleResourceService {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RoleResourceDao roleResourceDao;

    /**
     * 获取指定角色授权的系统资源信息
     */
    @Transactional(readOnly = true)
    public List<RoleResource> getAllRoleResources(Long roleId) throws ServiceException {
        List<RoleResource> result = new ArrayList<RoleResource>();
        result = roleResourceDao.find("from RoleResource where roleId = ?", roleId);
        return result;
    }

    /**
     * 保存指定角色的资源权限
     */
    public void saveRoleResource(Long roleId, String resourceIds) throws ServiceException {
        Session session = this.roleResourceDao.getSession();
        try {

            session.beginTransaction();
            // 当前的授权
            String[] menuIds = StringUtils.split(resourceIds, ",");
            HashSet<Long> granted = new HashSet<Long>();
            for (String menuId : menuIds) {
                granted.add(Long.valueOf(menuId));
            }

            // 获取本次修改之前的授权记录
            List<RoleResource> list = roleResourceDao.findBy("roleId", roleId);
            for (RoleResource roleResource : list) {
                Long roleResourceId = roleResource.getResourceId();
                if (granted.contains(roleResourceId)) {
                    granted.remove(roleResourceId);
                } else {
                    roleResourceDao.delete(roleResource); // 删除不在本次授权范围之内的授权记录
                }
            }

            // 保存本次新增的授权信息
            RoleResource roleResource = null;
            for (Long resourceId : granted) {
                roleResource = new RoleResource();
                roleResource.setRoleId(roleId);
                roleResource.setResourceId(resourceId);
                roleResourceDao.save(roleResource);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error("保存指定角色的资源权限失败:" , e);
            throw new ServiceException(e.getMessage());
        }
    }
}
