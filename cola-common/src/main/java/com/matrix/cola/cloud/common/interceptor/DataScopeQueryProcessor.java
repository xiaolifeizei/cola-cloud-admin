package com.matrix.cola.cloud.common.interceptor;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.matrix.cola.cloud.api.common.ColaConstant;
import com.matrix.cola.cloud.api.common.service.ColaCacheName;
import com.matrix.cola.cloud.api.entity.system.datascope.DataScopeEntity;
import com.matrix.cola.cloud.api.entity.system.group.GroupEntity;
import com.matrix.cola.cloud.api.entity.system.user.UserEntity;
import com.matrix.cola.cloud.common.cache.CacheProxy;
import com.matrix.cola.cloud.common.utils.WebUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import org.apache.ibatis.mapping.BoundSql;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 数据权限处理器
 *
 * @author : cui_feng
 * @since : 2022-06-08 10:33
 */
@Slf4j
public class DataScopeQueryProcessor {

    CacheProxy cacheProxy;

    @Autowired
    public void setCacheProxy(CacheProxy cacheProxy) {
        this.cacheProxy = cacheProxy;
    }

    public void processDefault(Statement statement, PlainSelect plainSelect,Expression where,String mainTableName,  BoundSql boundSql) {

        UserEntity userEntity = WebUtil.getUser();
        if (ObjectUtil.isNull(userEntity) || StrUtil.isEmpty(userEntity.getGroupId())) {
            return;
        }

        PluginUtils.MPBoundSql mpBs = PluginUtils.mpBoundSql(boundSql);
        // 本机构及下级机构
        if (userEntity.getGroupId().split(",").length > 0) {
            processLocalNext(mainTableName, plainSelect, where, userEntity);
        }
        mpBs.sql(statement.toString());
    }

    public void processSelect(Statement statement,PlainSelect plainSelect,Expression where,String mainTableName,BoundSql boundSql, DataScopeEntity dataScopePO) {

        UserEntity userEntity = WebUtil.getUser();
        if (ObjectUtil.isNull(userEntity) || StrUtil.isEmpty(userEntity.getGroupId()) || ObjectUtil.isNull(dataScopePO.getScopeType())) {
            return;
        }

        PluginUtils.MPBoundSql mpBs = PluginUtils.mpBoundSql(boundSql);
        switch (dataScopePO.getScopeType()) {
            case ColaConstant.DATA_SCOPE_TYPE_ALL:
                // 全部数据
                break;
            case ColaConstant.DATA_SCOPE_TYPE_LOCAL:
                // 本机构数据
                processLocal(mainTableName,plainSelect,where,userEntity);
                break;
            case ColaConstant.DATA_SCOPE_TYPE_LOCAL_NEXT:
                // 本机构及下级机构
                processLocalNext(mainTableName,plainSelect,where,userEntity);
                break;
            case ColaConstant.DATA_SCOPE_TYPE_SELF:
                // 本人数据
                processSelf(mainTableName,plainSelect,where,userEntity);
            case ColaConstant.DATA_SCOPE_TYPE_CUSTOM:
                // 自定义SQL
                try {
                    processCustom(plainSelect,where,userEntity,dataScopePO.getScopeSql());
                } catch (JSQLParserException e) {
                    log.error("自定义数据权限异常, 错误的SQL语句: " + mpBs.sql());
                    log.error(e.getMessage());
                    throw ExceptionUtils.mpe("自定义数据权限异常, 错误的SQL语句: %s", e.getCause(), mpBs.sql());
                }
                break;
            default:
        }
        mpBs.sql(statement.toString());

    }

    private void processLocal(String mainTableName,PlainSelect plainSelect,Expression where, UserEntity userPO) {

        if (userPO.getGroupId().split(",").length <= 0) {
            return;
        }

        InExpression in = new InExpression();
        in.setLeftExpression(new Column(mainTableName + ".group_id"));
        ExpressionList inValueList = new ExpressionList();
        for(String groupId : userPO.getGroupId().split(",")) {
            if (StrUtil.isEmpty(groupId)) {
                continue;
            }
            inValueList.addExpressions(new StringValue(groupId));
        }
        if (ObjectUtil.isNotEmpty(inValueList.getExpressions())) {
            in.setRightItemsList(inValueList);
            if (ObjectUtil.isNull(where)) {
                plainSelect.setWhere(in);
            } else {
                plainSelect.setWhere(new AndExpression(where, in));
            }
        }
    }

    private void processLocalNext(String mainTableName,PlainSelect plainSelect,Expression where, UserEntity userPO) {

        if (userPO.getGroupId().split(",").length <= 0) {
            return;
        }

        InExpression in = new InExpression();
        in.setLeftExpression(new Column(mainTableName + ".group_id"));
        ExpressionList inValueList = new ExpressionList();

        for(String groupId : userPO.getGroupId().split(",")) {
            if (StrUtil.isEmpty(groupId) || !NumberUtil.isLong(groupId)) {
                continue;
            }
            GroupEntity groupPO = cacheProxy.getObject(ColaCacheName.GROUP_ENTITY,groupId);
            if (ObjectUtil.isNotNull(groupPO) || StrUtil.isNotEmpty(groupPO.getAncestors())) {
                String [] userGroupIds = groupPO.getAncestors().split(",");
                for (String id : userGroupIds) {
                    if (NumberUtil.isLong(id)) {
                        inValueList.addExpressions(new StringValue(id));
                    }
                }
            }
        }
        if (ObjectUtil.isNotEmpty(inValueList.getExpressions())) {
            in.setRightItemsList(inValueList);
            if (ObjectUtil.isNull(where)) {
                plainSelect.setWhere(in);
            } else {
                plainSelect.setWhere(new AndExpression(where, in));
            }
        }
    }

    private void processSelf(String mainTableName,PlainSelect plainSelect, Expression where, UserEntity userPO) {

        if (ObjectUtil.isNull(userPO.getId())) {
            return;
        }

        EqualsTo equalsTo = new EqualsTo();
        equalsTo.setLeftExpression(new Column(mainTableName + ".creator"));
        equalsTo.setRightExpression(new LongValue(userPO.getId()));
        if (ObjectUtil.isNull(where)) {
            plainSelect.setWhere(equalsTo);
        } else {
            plainSelect.setWhere(new AndExpression(where, equalsTo));
        }
    }

    private void processCustom(PlainSelect plainSelect, Expression where, UserEntity userPO, String sql) throws JSQLParserException {
        if (StrUtil.isEmpty(sql)) {
            return;
        }
        // 变量替换
        if (sql.contains("${userid}")) {
            sql = sql.replaceAll("\\$\\{userId}",userPO.getId().toString());
        }
        if (sql.contains("${userName}")) {
            sql = sql.replaceAll("\\$\\{userName}",userPO.getName());
        }
        if (sql.contains("${loginName}")) {
            sql = sql.replaceAll("\\$\\{loginName}",userPO.getLoginName());
        }
        if (sql.contains("${phone}")) {
            sql = sql.replaceAll("\\$\\{phone}",userPO.getPhone());
        }
        if (sql.contains("${ids}")) {
            sql = sql.replaceAll("\\$\\{ids}",userPO.getIds());
        }
        if (sql.contains("${groupId}")) {
            StringBuilder inExp = new StringBuilder("(");
            for (String id : userPO.getGroupId().split(",")) {
                if (StrUtil.isNotEmpty(id)) {
                    inExp.append("'").append(id).append("',");
                }
            }
            if (inExp.toString().endsWith(",")) {
                inExp = new StringBuilder(inExp.substring(0, inExp.length() - 1));
            }
            inExp.append(")");
            sql = sql.replaceAll("\\$\\{groupId}",inExp.toString());
        }
        if (ObjectUtil.isNull(where)) {
            plainSelect.setWhere(CCJSqlParserUtil.parseCondExpression(sql));
        } else {
            plainSelect.setWhere(new AndExpression(where, CCJSqlParserUtil.parseCondExpression(sql)));
        }
    }
}
