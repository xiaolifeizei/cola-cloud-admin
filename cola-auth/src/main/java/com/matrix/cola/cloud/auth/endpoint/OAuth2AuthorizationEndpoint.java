package com.matrix.cola.cloud.auth.endpoint;

import cn.hutool.core.util.IdUtil;
import com.matrix.cola.cloud.api.common.Result;
import com.matrix.cola.cloud.api.common.service.ColaCacheName;
import com.matrix.cola.cloud.api.entity.system.user.UserEntity;
import com.matrix.cola.cloud.auth.service.SecurityUser;
import com.matrix.cola.cloud.auth.utils.JwtTokenUtil;
import com.matrix.cola.cloud.common.cache.CacheProxy;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint;
import org.springframework.security.oauth2.provider.endpoint.DefaultRedirectResolver;
import org.springframework.security.oauth2.provider.endpoint.RedirectResolver;
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.*;

/**
 * OAuth授权端点实现
 *
 * @author : cui_feng
 * @since : 2022-09-23 12:30
 */
@Setter
public class OAuth2AuthorizationEndpoint extends AuthorizationEndpoint {

    private final RedirectResolver redirectResolver = new DefaultRedirectResolver();

    private AuthorizationCodeServices authorizationCodeServices;

    private CacheProxy cacheProxy;

    private final Object implicitLock = new Object();

    public Result oauth2Authorize(@RequestParam Map<String, String> parameters, Principal principal) {

        AuthorizationRequest authorizationRequest = getOAuth2RequestFactory().createAuthorizationRequest(parameters);
        Set<String> responseTypes = authorizationRequest.getResponseTypes();

        if (!responseTypes.contains("token") && !responseTypes.contains("code")) {
            return Result.err("不支持的response_type，只能为token或code");
        }

        if (authorizationRequest.getClientId() == null) {
            return Result.err("必须提供client_id");
        }

        try {

            if (!(principal instanceof Authentication) || !((Authentication) principal).isAuthenticated()) {
                return Result.err("没有通过身份验证，无法完成授权");
            }

            ClientDetails client = getClientDetailsService().loadClientByClientId(authorizationRequest.getClientId());

            // The resolved redirect URI is either the redirect_uri from the parameters or the one from
            // clientDetails. Either way we need to store it on the AuthorizationRequest.
            String redirectUriParameter = authorizationRequest.getRequestParameters().get(OAuth2Utils.REDIRECT_URI);
            String resolvedRedirect = redirectResolver.resolveRedirect(redirectUriParameter, client);
            if (!StringUtils.hasText(resolvedRedirect)) {
                return Result.err("没有找到redirect_uri");
            }
            authorizationRequest.setRedirectUri(resolvedRedirect);

            // Check Scope
            if (client.getScope() != null && !client.getScope().isEmpty()) {
                for (String scope : authorizationRequest.getScope()) {
                    if (!client.getScope().contains(scope)) {
                        return Result.err("无效的scope");
                    }
                }
            }
            if (authorizationRequest.getScope().isEmpty()) {
                return Result.err("没有找到scope");
            }

            // Check approved
            boolean approved = true;
            for (String scope : authorizationRequest.getScope()) {
                if (!client.isAutoApprove(scope)) {
                    approved = false;
                }
            }

            if (approved) {
                if (responseTypes.contains("token")) {
                    return getImplicitGrantResponse(authorizationRequest);
                }
                if (responseTypes.contains("code")) {
                    return generateCode(authorizationRequest,(Authentication) principal);
                }
            }

            // Need user approved
            String approvedId = IdUtil.objectId();
            String approvedToken = JwtTokenUtil.createApproveToken(approvedId,client.getClientId(),responseTypes);
            UserEntity userEntity = ((SecurityUser)(((Authentication) principal).getPrincipal())).getCurrentUser();
            userEntity.setPassword(null);
            cacheProxy.put(ColaCacheName.OAUTH2_APPROVE_ID, approvedId, userEntity, JwtTokenUtil.EXPIRATION);

            return Result.ok().data("ApproveToken",approvedToken);
        }
        catch (RuntimeException e) {
            return Result.err(e.getMessage());
        }
    }

    public Result approveOrDeny(@RequestParam Map<String, String> approvalParameters, Principal principal) {

        if (!(principal instanceof Authentication)) {
            return Result.err("没有通过身份验证，无法完成授权");
        }

        AuthorizationRequest authorizationRequest = getOAuth2RequestFactory().createAuthorizationRequest(approvalParameters);

        if (authorizationRequest == null) {
            return Result.err("授权请求初始化失败");
        }

        Set<String> responseTypes = authorizationRequest.getResponseTypes();

        authorizationRequest.setApprovalParameters(approvalParameters);
        String flag = approvalParameters.get(OAuth2Utils.USER_OAUTH_APPROVAL);
        boolean approved = flag != null && flag.equalsIgnoreCase("true");
        authorizationRequest.setApproved(approved);

        ClientDetails client = getClientDetailsService().loadClientByClientId(authorizationRequest.getClientId());
        String redirectUriParameter = authorizationRequest.getRequestParameters().get(OAuth2Utils.REDIRECT_URI);
        String resolvedRedirect = redirectResolver.resolveRedirect(redirectUriParameter, client);
        if (!StringUtils.hasText(resolvedRedirect)) {
            return Result.err("未提供redirect_uri，无法批准请求");
        }
        authorizationRequest.setRedirectUri(resolvedRedirect);

        if (!authorizationRequest.isApproved()) {
            return Result.err("用户拒绝访问");
        }

        if (responseTypes.contains("token")) {
            return getImplicitGrantResponse(authorizationRequest);
        }

        return generateCode(authorizationRequest,(Authentication) principal);
    }

    /**
     * 生成授权码
     * @param authorizationRequest 授权请求
     * @param authentication 认证对象
     * @return Result
     */
    private Result generateCode(AuthorizationRequest authorizationRequest, Authentication authentication) {
        try {
            OAuth2Request storedOAuth2Request = getOAuth2RequestFactory().createOAuth2Request(authorizationRequest);
            OAuth2Authentication combinedAuth = new OAuth2Authentication(storedOAuth2Request, authentication);
            String code = authorizationCodeServices.createAuthorizationCode(combinedAuth);
            return Result.ok().data("code", code).data("redirect_uri", authorizationRequest.getRedirectUri());
        } catch (OAuth2Exception e) {
            if (authorizationRequest.getState() != null) {
                e.addAdditionalInformation("state", authorizationRequest.getState());
            }
            return Result.err(e.getMessage());
        }
    }

    /**
     * 简化模式，直接返回token
     * @param authorizationRequest 授权请求
     * @return Result
     */
    private Result getImplicitGrantResponse(AuthorizationRequest authorizationRequest) {
        try {
            TokenRequest tokenRequest = getOAuth2RequestFactory().createTokenRequest(authorizationRequest, "implicit");
            OAuth2Request storedOAuth2Request = getOAuth2RequestFactory().createOAuth2Request(authorizationRequest);
            OAuth2AccessToken accessToken = null;

            synchronized (this.implicitLock) {
                accessToken = getTokenGranter().grant("implicit",new ImplicitTokenRequest(tokenRequest, storedOAuth2Request));
            }

            if (accessToken == null) {
                return Result.err("不支持的响应类型：token");
            }
            return Result.ok()
                        .data(OAuth2Utils.REDIRECT_URI, authorizationRequest.getRedirectUri())
                        .data("access_token", appendAccessToken(authorizationRequest, accessToken));
        }
        catch (OAuth2Exception e) {
            return Result.err(e.getMessage());
        }
    }

    private String appendAccessToken(AuthorizationRequest authorizationRequest, OAuth2AccessToken accessToken) {

        Map<String, Object> vars = new LinkedHashMap<String, Object>();
        Map<String, String> keys = new HashMap<String, String>();

        if (accessToken == null) {
            throw new InvalidRequestException("生成token失败，无法进行授权");
        }

        vars.put("access_token", accessToken.getValue());
        vars.put("token_type", accessToken.getTokenType());
        String state = authorizationRequest.getState();

        if (state != null) {
            vars.put("state", state);
        }
        Date expiration = accessToken.getExpiration();
        if (expiration != null) {
            long expires_in = (expiration.getTime() - System.currentTimeMillis()) / 1000;
            vars.put("expires_in", expires_in);
        }
        String originalScope = authorizationRequest.getRequestParameters().get(OAuth2Utils.SCOPE);
        if (originalScope == null || !OAuth2Utils.parseParameterList(originalScope).equals(accessToken.getScope())) {
            vars.put("scope", OAuth2Utils.formatParameterList(accessToken.getScope()));
        }
        Map<String, Object> additionalInformation = accessToken.getAdditionalInformation();
        for (String key : additionalInformation.keySet()) {
            Object value = additionalInformation.get(key);
            if (value != null) {
                keys.put("extra_" + key, key);
                vars.put("extra_" + key, value);
            }
        }
        // Do not include the refresh token (even if there is one)
        return append(vars, keys);
    }

    private String append(Map<String, ?> query, Map<String, String> keys) {

        UriComponentsBuilder template = UriComponentsBuilder.newInstance();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("");
        URI redirectUri;
        try {
            // assume it's encoded to start with (if it came in over the wire)
            redirectUri = builder.build(true).toUri();
        }
        catch (Exception e) {
            // ... but allow client registrations to contain hard-coded non-encoded values
            redirectUri = builder.build().toUri();
            builder = UriComponentsBuilder.fromUri(redirectUri);
        }
        template.scheme(redirectUri.getScheme()).port(redirectUri.getPort()).host(redirectUri.getHost())
                .userInfo(redirectUri.getUserInfo()).path(redirectUri.getPath());

        StringBuilder values = new StringBuilder();
        if (redirectUri.getFragment() != null) {
            String append = redirectUri.getFragment();
            values.append(append);
        }
        for (String key : query.keySet()) {
            if (values.length() > 0) {
                values.append("&");
            }
            String name = key;
            if (keys != null && keys.containsKey(key)) {
                name = keys.get(key);
            }
            values.append(name).append("={").append(key).append("}");
        }
        if (values.length() > 0) {
            template.fragment(values.toString());
        }
        UriComponents encoded = template.build().expand(query).encode();
        builder.fragment(encoded.getFragment());

        return builder.build().toUriString();

    }
}
