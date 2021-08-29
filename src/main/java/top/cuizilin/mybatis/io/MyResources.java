package top.cuizilin.mybatis.io;

import java.io.InputStream;

/**
 * 读取mybatis-config.xml文件
 */
public class MyResources {
    public static InputStream getResourceAsStream(String resource){
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
    }
}
