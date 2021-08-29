package top.cuizilin.mybatis.mapping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 封装mybatis-configuration.xml中的environment
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyEnvironment {

    //封装dataSource
    private String url;
    private String driver;
    private String username;
    private String password;

}
