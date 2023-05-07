package com.demo.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demo.DBEnum;
import com.demo.anno.DynamicTable;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;


/**
 * @description: 动态替换表名拦截器
 * @author: hinotoyk
 * @created: 2022/04/19
 */

//method = "query"拦截select方法、而method = "update"则能拦截insert、update、delete的方法
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
@Component
@Slf4j
public class ReplaceTableInterceptor implements Interceptor {

    private final static Map<String, String> TABLE_MAP = new LinkedHashMap<>();

    static {
        //表名长的放前面，避免字符串匹配的时候先匹配替换子集
        TABLE_MAP.put("coffee", "coffee_test");//测试
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        //获取MappedStatement对象
        MappedStatement ms = (MappedStatement) args[0];
        //获取传入sql语句的参数对象
        Object parameterObject = args[1];

        BoundSql boundSql = ms.getBoundSql(parameterObject);
        //获取到拥有占位符的sql语句
        String sql = boundSql.getSql();
        System.out.println("拦截前sql :" + sql);

        String dynamicTableParameterName = getDynamicTableParameterName(ms);
        //判断是否需要替换表名
        if (Objects.nonNull(dynamicTableParameterName)) {
            DBEnum paramByName = getParamByName(parameterObject, dynamicTableParameterName);
            sql = sql.replaceAll(paramByName.getOriginTableName(),paramByName.getNewTableName());

            System.out.println("拦截后sql :" + sql);

            //重新生成一个BoundSql对象
            BoundSql bs = new BoundSql(ms.getConfiguration(), sql, boundSql.getParameterMappings(), parameterObject);

            //重新生成一个MappedStatement对象
            MappedStatement newMs = copyMappedStatement(ms, new BoundSqlSqlSource(bs));

            //赋回给实际执行方法所需的参数中
            args[0] = newMs;
        }

        return invocation.proceed();
    }

    private String getDynamicTableParameterName(MappedStatement mappedStatement) {
        try {
            String id = mappedStatement.getId();
            String className = id.substring(0, id.lastIndexOf("."));
            String methodName = id.substring(id.lastIndexOf(".") + 1);

            final Method[] method = Class.forName(className).getMethods();
            for (Method me : method) {
                if (me.getName().equals(methodName)) {
                    for (Parameter parameter : me.getParameters()) {
                        if (parameter.isAnnotationPresent(DynamicTable.class)) {
                            return parameter.getName();
                        }
                    }
                }
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return null;
    }

    private DBEnum getParamByName(Object parameterObject, String parameterName) {
        DBEnum value = null;
        if (parameterObject instanceof Map) {
            Map param = (Map) parameterObject;
            return (DBEnum) param.get(parameterName);
        } else if (parameterObject instanceof DBEnum) {
            value = (DBEnum) parameterObject;
        } else {
            JSONObject json = (JSONObject) JSON.toJSON(parameterObject);
            return (DBEnum) json.get(parameterName);
        }
        return value;
    }


    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }


    /***
     * 复制一个新的MappedStatement
     * @param ms
     * @param newSqlSource
     * @return
     */
    private MappedStatement copyMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());

        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length > 0) {
            builder.keyProperty(String.join(",", ms.getKeyProperties()));
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());
        return builder.build();
    }

    /***
     * MappedStatement构造器接受的是SqlSource
     * 实现SqlSource接口，将BoundSql封装进去
     */
    public static class BoundSqlSqlSource implements SqlSource {
        private BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        @Override
        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }


}
