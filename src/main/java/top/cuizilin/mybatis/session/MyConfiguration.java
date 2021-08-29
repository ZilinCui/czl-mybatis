package top.cuizilin.mybatis.session;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.cuizilin.mybatis.mapping.MyEnvironment;
import top.cuizilin.mybatis.mapping.MyMappedStatement;

import java.util.Map;

/**
 * 封装mybatis-config.xml文件
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyConfiguration {

    //mybatis-config.xml中的environment;
    private MyEnvironment myEnvironment;

    //mybatis-config.xml中的mappers
    private Map<String, MyMappedStatement> myMappedStatementMap;

}
