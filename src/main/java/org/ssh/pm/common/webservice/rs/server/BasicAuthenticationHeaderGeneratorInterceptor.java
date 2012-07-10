package org.ssh.pm.common.webservice.rs.server;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.cxf.interceptor.AbstractOutDatabindingInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;
import org.springframework.beans.factory.annotation.Required;

public class BasicAuthenticationHeaderGeneratorInterceptor extends AbstractOutDatabindingInterceptor {
    public static final String COLON = ":";
    public static final String SPACE = " ";
    public static final String BASIC = "Basic";
    /** Map of allowed users to this system with their corresponding passwords. */
    private Map users;

    @Required
    public void setUsers(Map users) {
        this.users = users;
    }

    public BasicAuthenticationHeaderGeneratorInterceptor() {
        super(Phase.MARSHAL);
    }

    public BasicAuthenticationHeaderGeneratorInterceptor(String phase) {
        super(phase);
    }

    public void handleMessage(Message outMessage) throws Fault {
        Map<String, List> headers = (Map<String, List>) outMessage.get(Message.PROTOCOL_HEADERS);
        try {
            headers.put("Authorization",
                    Collections.singletonList(BASIC + SPACE + getBase64EncodedCredentials().trim()));
        } catch (Exception ce) {
            //throw new Fault(ce);
        }
    }

    private String getBase64EncodedCredentials() {
        String userName = null;
        String password = null;
        Iterator iterator = null != users ? users.keySet().iterator() : null;
        if (null != iterator && iterator.hasNext()) {
            userName = (String) iterator.next();
            password = (String) users.get(userName);
        }
        String clearCredentials = userName + COLON + password;
        return new String(Base64.encodeBase64(clearCredentials.getBytes()));
    }
}
