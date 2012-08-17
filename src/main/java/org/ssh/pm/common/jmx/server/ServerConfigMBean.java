package org.ssh.pm.common.jmx.server;

/**
 * 系统属性配置的MBean接口.
 *
 * @author calvin
 */
public interface ServerConfigMBean {

    /**
     * ServerConfigMBean自动生成的注册名称.
     */
    String SERVER_CONFIG_MBEAN_NAME = "Showcase:name=serverConfig,type=ServerConfig";
    String HIBERNATE_MBEAN_NAME = "Showcase:name=hibernateStatistics,type=StatisticsService";

    /**
     * 服务器节点名.
     */
    public String getNodeName();

    public void setNodeName(String nodeName);

    /**
     * 是否发送通知邮件.
     */
    public boolean isNotificationMailEnabled();

    public void setNotificationMailEnabled(boolean notificationMailEnabled);
}
