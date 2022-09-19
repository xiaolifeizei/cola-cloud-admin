package com.matrix.cola.cloud.auth.service;

import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

/**
 * 客户端服务实现类
 *
 * @author : cui_feng
 * @since : 2022-09-16 10:49
 */
@Service
public class ClientDetailsServiceImpl extends JdbcClientDetailsService {

    public ClientDetailsServiceImpl(DataSource dataSource) {
        super(dataSource);
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
