package top.cuizilin.mybatis.session;

import top.cuizilin.mybatis.builder.xml.MyXMLConfigBuilder;

import javax.xml.xpath.XPathExpressionException;
import java.io.InputStream;

public class MySqlSessionFactoryBuilder {

    public MySqlSessionFactory build(InputStream in) throws XPathExpressionException {
        //解析mybatis-config.xml文件，封装后得到MyConfiguration对象
        MyConfiguration myConfiguration = new MyXMLConfigBuilder(in).parse();

        //返回一个MySqlSessionFactory工厂，里面有MyConfiguration
        return new MySqlSessionFactory(myConfiguration);
    }
}
