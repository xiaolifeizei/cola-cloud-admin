package com.matrix.cola.cloud.auth.service;

import com.matrix.cola.cloud.auth.utils.AuthConstant;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

import javax.sql.DataSource;

/**
 * 客户端服务实现类
 *
 * @author : cui_feng
 * @since : 2022-09-16 10:49
 */
public class ClientDetailsServiceImpl extends JdbcClientDetailsService {

    public ClientDetailsServiceImpl(DataSource dataSource) {
        super(dataSource);
        setSelectClientDetailsSql(AuthConstant.DEFAULT_SELECT_STATEMENT);
        setFindClientDetailsSql(AuthConstant.DEFAULT_FIND_STATEMENT);
    }

    /**
     * 可扩展逻辑
     * @param clientId 客户端id
     * @return ClientDetails
     */
    public ClientDetails loadClientByClientId(String clientId) {
        return super.loadClientByClientId(clientId);
    }
}
