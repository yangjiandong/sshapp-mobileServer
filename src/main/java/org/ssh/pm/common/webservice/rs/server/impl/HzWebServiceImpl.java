package org.ssh.pm.common.webservice.rs.server.impl;

import java.util.List;

import javax.jws.WebService;

import org.dozer.DozerBeanMapper;
import org.hibernate.ObjectNotFoundException;
import org.perf4j.StopWatch;
import org.perf4j.aop.Profiled;
import org.perf4j.slf4j.Slf4JStopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.ssh.pm.common.webservice.rs.server.HzWebService;
import org.ssh.pm.common.webservice.rs.server.WsConstants;
import org.ssh.pm.common.webservice.rs.server.dto.HzDTO;
import org.ssh.pm.common.webservice.rs.server.result.GetAllHzResult;
import org.ssh.pm.common.webservice.rs.server.result.GetHzResult;
import org.ssh.pm.common.webservice.rs.server.result.WSResult;
import org.ssh.sys.entity.Hz;
import org.ssh.sys.service.HzService;

import com.google.common.collect.Lists;

/**
 * HzWebService服务端实现类. 
 * 
 * @author yangjiandong
 */
//serviceName与portName属性指明WSDL中的名称, endpointInterface属性指向Interface定义类.
@WebService(serviceName = "HzService", portName = "HzServicePort", endpointInterface = "org.ssh.pm.common.webservice.rs.server.HzWebService", targetNamespace = WsConstants.NS)
public class HzWebServiceImpl implements HzWebService {
    private static Logger logger = LoggerFactory.getLogger(HzWebServiceImpl.class);

    @Autowired
    private HzService hzService;
    @Autowired
    private DozerBeanMapper dozer;

    @Override
    @Profiled(tag = "GetAllHzResult")
    public GetAllHzResult getAllHz() {
        try {
            List<Hz> allHzList = this.hzService.getAllHzOnMethodCache();
            List<HzDTO> allHzDTOList = Lists.newArrayList();

            for (Hz hz : allHzList) {
                allHzDTOList.add(dozer.map(hz, HzDTO.class));
            }

            GetAllHzResult result = new GetAllHzResult();
            result.setHzList(allHzDTOList);
            return result;
        } catch (RuntimeException e) {
            logger.error(e.getMessage(), e);
            return WSResult.buildDefaultErrorResult(GetAllHzResult.class);
        }
    }

    @Override
    public GetHzResult getHz(String hz) {
        StopWatch totalStopWatch = new Slf4JStopWatch();
        //校验请求参数
        try {
            Assert.notNull(hz, "hz参数为空");
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
            return WSResult.buildResult(GetHzResult.class, WSResult.PARAMETER_ERROR, e.getMessage());
        }

        //获取用户
        try {

            StopWatch dbStopWatch = new Slf4JStopWatch("GetHzResult.fetchDB");
            Hz oneHz = this.hzService.getHz(hz);
            dbStopWatch.stop();
            
            HzDTO dto = dozer.map(oneHz, HzDTO.class);
            GetHzResult result = new GetHzResult();
            result.setHz(dto);
            
            totalStopWatch.stop("GerHz.total.success");
            
            return result;
        } catch (ObjectNotFoundException e) {
            String message = "用户不存在(id:" + hz + ")";
            logger.error(message, e);
            totalStopWatch.stop("GerHz.total.failure");
            return WSResult.buildResult(GetHzResult.class, WSResult.PARAMETER_ERROR, message);
        } catch (RuntimeException e) {
            logger.error(e.getMessage(), e);
            totalStopWatch.stop("GerHz.total.failure");
            return WSResult.buildDefaultErrorResult(GetHzResult.class);
        }
    }

}
