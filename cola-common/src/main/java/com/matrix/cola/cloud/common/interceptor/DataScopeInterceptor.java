package com.matrix.cola.cloud.common.interceptor;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.matrix.cola.cloud.api.common.ColaConstant;
import com.matrix.cola.cloud.api.common.service.ColaCacheName;
import com.matrix.cola.cloud.api.entity.system.datascope.DataScopeEntity;
import com.matrix.cola.cloud.api.entity.system.user.UserEntity;
import com.matrix.cola.cloud.common.cache.CacheProxy;
import com.matrix.cola.cloud.common.utils.WebUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * 数据权限拦截器
 *
 * @author : cui_feng
 * @since : 2022-06-05 15:42
 */
@Slf4j
@ConfigurationProperties(prefix = "cola.data-scope")
public class DataScopeInterceptor implements InnerInterceptor {

    /**
     * 是否开启数据权限
     */
    @Setter
    @Getter
    private boolean enabled = true;
    /**
     * 是否开启默认的数据权限，默认不开启，默认的数据权限类型为本机构及下级机构
     */
    @Getter
    @Setter
    private boolean enableDefault = false;
    /**
     * 忽略数据权限的表
     */
    @Setter
    @Getter
    private List<String> ignoreTables = new ArrayList<>();
    /**
     * 默认忽略数据权限的表名
     */
    private final List<String> defaultIgnoreTables = Arrays.asList(
            "system_dict",
            "system_err_log",
            "system_data_log",
            "system_group",
            "system_role_user",
            "system_role_menu",
            "system_data_scope");
    /**
     * 数据权限处理器
     */
    private DataScopeQueryProcessor dataScopeQueryProcessor;
    private CacheProxy cacheProxy;



    @Autowired
    public void setDataScopeProcessor(DataScopeQueryProcessor dataScopeProcessor) {
        this.dataScopeQueryProcessor = dataScopeProcessor;
    }

    @Autowired
    public void setCacheProxy(CacheProxy cacheProxy) {
        this.cacheProxy = cacheProxy;
    }

    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {

        // 数据权限开关
        if (!enabled) {
            return;
        }

        HashMap<String, List<DataScopeEntity>> scopeMap = cacheProxy.getObject(ColaCacheName.DATA_SCOPE_LIST, ColaCacheName.DATA_SCOPE_LIST.cacheName());
        UserEntity userEntity = WebUtil.getUser();
        // 没有登陆用户跳过
        if (ObjectUtil.isNull(userEntity)) {
            return;
        }
        // 超管跳过
        if (WebUtil.isAdministrator()) {
            return;
        }

        PluginUtils.MPBoundSql mpBs = PluginUtils.mpBoundSql(boundSql);
        Statement statement = null;
        try {
            statement = CCJSqlParserUtil.parse(mpBs.sql());
        } catch (JSQLParserException e) {
            log.error("默认数据权限异常, 错误的SQL语句: " + mpBs.sql());
            log.error(e.getMessage());
            throw ExceptionUtils.mpe("数据权限异常, 错误的SQL语句: %s", e.getCause(), mpBs.sql());
        }
        // 只处理Select类型
        if (ObjectUtil.isNull(statement) || !(statement instanceof Select)) {
            return;
        }

        Select select = (Select) statement;
        PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
        Expression where = plainSelect.getWhere();
        Table fromItem = (Table) plainSelect.getFromItem();

        // 有别名用别名，无别名用表名
        Alias fromItemAlias = fromItem.getAlias();
        String mainTableName = fromItemAlias == null ? fromItem.getName() : fromItemAlias.getName();

        // 忽略的
        if (ignoreTables.contains(fromItem.getName()) || defaultIgnoreTables.contains(fromItem.getName().toLowerCase())) {
            return;
        }

        // 没有数据权限则跳过
        if (ObjectUtil.isEmpty(scopeMap)) {
            if (enableDefault) {
                dataScopeQueryProcessor.processDefault(statement,plainSelect,where,mainTableName,boundSql);
            }
            return;
        }

        List<DataScopeEntity> dataScopeList = scopeMap.get(ms.getId());
        // 没有获取到对应的数据权限则跳过
        if (ObjectUtil.isEmpty(dataScopeList)) {
            if (enableDefault) {
                dataScopeQueryProcessor.processDefault(statement,plainSelect,where,mainTableName,boundSql);
            }
            return;
        }

        for (DataScopeEntity scope : dataScopeList) {
            // 全局生效处理
            if (ObjectUtil.isNotNull(scope.getGlobalized()) && scope.getGlobalized() == ColaConstant.YES) {
                dataScopeQueryProcessor.processSelect(statement,plainSelect,where,mainTableName,boundSql, scope);
                continue;
            }

            // 非全局生效处理
            if (ObjectUtil.isNotNull(scope.getGlobalized()) && scope.getGlobalized() == ColaConstant.NO) {
                // 数据权限的groupId列表
                List<String> scopeGroupIds = Arrays.asList(StrUtil.emptyToDefault(scope.getGroupId(),"").split(","));
                // 当前用户的groupId列表
                List<String> userGroupIds = Arrays.asList(StrUtil.emptyToDefault(userEntity.getGroupId(),"").split(","));

                boolean isMatched = false;

                for (String userGroupId : userGroupIds) {
                    // 没有匹配到机构
                    if (StrUtil.isEmpty(userGroupId) || !scopeGroupIds.contains(userGroupId)) {
                        continue;
                    }
                    // 没有配置角色
                    if (StrUtil.isEmpty(scope.getRoleIds())) {
                        isMatched = true;
                        dataScopeQueryProcessor.processSelect(statement,plainSelect,where,mainTableName,boundSql, scope);
                        continue;
                    }

                    // 配置了角色
                    // 当前登陆用户的角色
                    List<Long> userRoleIdList = cacheProxy.getObject(ColaCacheName.USER_ROLE_IDS, userEntity.getId().toString());
                    if (ObjectUtil.isEmpty(userRoleIdList)) {
                        // 没有获取到当前登陆用户的角色的情况，基本上不可能进来
                        continue;
                    }
                    // 数据权限的角色列表
                    String [] scopeRoleIdArr= StrUtil.emptyToDefault(scope.getRoleIds(),"").split(",");
                    if (scopeRoleIdArr.length == 0) {
                        continue;
                    }
                    for (String roleId : scopeRoleIdArr) {
                        // 匹配到了角色
                        if (NumberUtil.isLong(roleId) && userRoleIdList.contains(Long.valueOf(roleId))) {
                            isMatched = true;
                            dataScopeQueryProcessor.processSelect(statement,plainSelect,where,mainTableName,boundSql, scope);
                        }
                    }
                }
                // 没有匹配到数据权限
                if (!isMatched) {
                    // 默认处理
                    if (enableDefault) {
                        dataScopeQueryProcessor.processDefault(statement,plainSelect,where,mainTableName,boundSql);
                    }
                }
            }
        }
    }
}
