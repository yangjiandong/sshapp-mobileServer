package org.ssh.sys.service;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import org.apache.activeio.xnet.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ssh.pm.common.utils.JdbcPage;
import org.ssh.sys.dao.UserJdbcDao;
import org.ssh.sys.dao.UserLogDao;
import org.ssh.sys.entity.UserLog;

@Service
public class UserLogService {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserLogDao userLogDao;

    @Autowired
    private UserJdbcDao userJdbcDao;

    @Transactional
    public void save(UserLog u) {
        this.userLogDao.save(u);
    }

    public JdbcPage<UserLog> queryUserLog(HttpServletRequest request) throws ServiceException {
        try {
            String pageSize = request.getParameter("limit");
            String start = request.getParameter("start");
            String userId = request.getParameter("userId");

            JdbcPage<UserLog> resultList = userJdbcDao.queryUserLog(
                    new BigDecimal(start).divide(new BigDecimal(pageSize)).intValue() + 1, Integer.valueOf(pageSize),
                    Long.valueOf(userId));
            return resultList;
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException(e);
        }

    }

    public Long getTotalCount(HttpServletRequest request) throws ServiceException {
        Long rowCount = 0L;
        String userId = request.getParameter("userId");
        try {

            rowCount = userJdbcDao.getTotalCountUserLog(Long.valueOf(userId));
        } catch (Exception e) {
            logger.error("", e);
            throw new ServiceException(e);
        }
        return rowCount;
    }
}
