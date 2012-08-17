package org.ssh.pm.common.utils;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class LoginSessionListener implements HttpSessionListener, HttpSessionAttributeListener {

    public static void main(String[] args) {
    }

    public static Map userMap = new HashMap(); //创建了一个对象来保存session的
    private MySessionContext myc = MySessionContext.getInstance(); //MySessionContext是实现session的读取和删除增加  单例模式

    public void sessionCreated(HttpSessionEvent event) {
        myc.AddSession(event.getSession());
    }

    public void sessionDestroyed(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        myc.DelSession(session);
    }

    /* （非 Javadoc）
     * @see javax.servlet.http.HttpSessionAttributeListener#attributeAdded(javax.servlet.http.HttpSessionBindingEvent)
     */
    public void attributeAdded(HttpSessionBindingEvent arg0) {
        // TODO 自动生成方法存根

    }

    /* （非 Javadoc）
     * @see javax.servlet.http.HttpSessionAttributeListener#attributeRemoved(javax.servlet.http.HttpSessionBindingEvent)
     */
    public void attributeRemoved(HttpSessionBindingEvent arg0) {
        // TODO 自动生成方法存根

    }

    /* （非 Javadoc）
     * @see javax.servlet.http.HttpSessionAttributeListener#attributeReplaced(javax.servlet.http.HttpSessionBindingEvent)
     */
    public void attributeReplaced(HttpSessionBindingEvent arg0) {
        // TODO 自动生成方法存根

    }

}
