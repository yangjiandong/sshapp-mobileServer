package org.ssh.pm.mob;

import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springside.modules.utils.spring.SpringContextHolder;

public class MobUtil {
    private static Logger logger = LoggerFactory.getLogger(MobUtil.class);

    public static void execSp(String spName, Map<String, Object> map) throws Exception {
        DataSource dataSource = (DataSource) SpringContextHolder.getBean("dataSource");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        try {

            // 存储过程
            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate);
            jdbcCall.withProcedureName(spName);
            SqlParameterSource in = new MapSqlParameterSource().addValues(map);

            jdbcCall.execute(in);

        } catch (Exception e) {
            logger.error("执行存储过程:" + spName + "失败", e.getMessage());
            throw new Exception(e);
        }
    }
}
