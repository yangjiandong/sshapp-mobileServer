package org.ssh.pm.common.httpinvoke.service;

import org.ssh.pm.common.httpinvoke.domain.User;

public interface UserService {

    /**
     * 获得用户
     *
     * @param username
     *            用户名
     * @return
     */
    User getUser(String username);
}
