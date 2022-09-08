package com.matrix.cola.cloud.common.interceptor;

import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import lombok.Getter;
import lombok.Setter;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 防止全表更新与删除，增加过滤功能
 *
 * @author : cui_feng
 * @since : 2022-06-10 15:41
 */
@ConfigurationProperties(prefix = "cola.block-attack")
public class ColaBlockAttackInnerInterceptor extends BlockAttackInnerInterceptor {


    @Setter
    @Getter
    private List<String> ignoreTables = new ArrayList<>();

    /**
     * 默认忽略的表名
     */
    private final List<String> defaultIgnoreTables = Arrays.asList(
            "system_error_log",
            "system_data_log");

    @Override
    public void beforePrepare(StatementHandler sh, Connection connection, Integer transactionTimeout) {
        PluginUtils.MPStatementHandler handler = PluginUtils.mpStatementHandler(sh);
        MappedStatement ms = handler.mappedStatement();
        SqlCommandType sct = ms.getSqlCommandType();
        if (sct == SqlCommandType.UPDATE || sct == SqlCommandType.DELETE) {
            BoundSql boundSql = handler.boundSql();
            PluginUtils.MPBoundSql mpBs = PluginUtils.mpBoundSql(boundSql);
            Statement statement = null;
            try {
                statement = CCJSqlParserUtil.parse(mpBs.sql());
            } catch (JSQLParserException e) {
                throw ExceptionUtils.mpe("数据权限异常, 错误的SQL语句: %s", e.getCause(), mpBs.sql());
            }
            String tableName = "";
            if (sct == SqlCommandType.UPDATE) {
                Update update = (Update) statement;
                tableName = update.getTable().getName();
            }
            if (sct == SqlCommandType.DELETE) {
                Delete delete = (Delete)statement;
                tableName = delete.getTable().getName();
            }
            if (ignoreTables.contains(tableName) || defaultIgnoreTables.contains(tableName.toLowerCase())) {
                return;
            }
            super.beforePrepare(sh, connection, transactionTimeout);
        }
    }

}
