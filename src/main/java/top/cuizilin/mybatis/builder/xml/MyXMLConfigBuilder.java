package top.cuizilin.mybatis.builder.xml;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import top.cuizilin.mybatis.mapping.MyEnvironment;
import top.cuizilin.mybatis.mapping.MyMappedStatement;
import top.cuizilin.mybatis.parsing.MyXPathParser;
import top.cuizilin.mybatis.session.MyConfiguration;

import javax.xml.xpath.XPathExpressionException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyXMLConfigBuilder {

    //嵌入Xpath解析器
    private MyXPathParser parser;


    public MyXMLConfigBuilder(InputStream in){
        parser = new MyXPathParser(in);
    }

    //解析mybatis-config.xml文件，封装成MyConfiguration对象
    public MyConfiguration parse() throws XPathExpressionException {
        //获取到dataSource这个节点
        Node dataSourceNode = parser.xNode("/configuration/environments/environment/dataSource");
        Properties properties = new Properties();
        //获取所有的properties
        NodeList propertiesNodeList = dataSourceNode.getChildNodes();
        for(int i = 0; i < propertiesNodeList.getLength(); i++){
            //每一个property
            Node propertyNode = propertiesNodeList.item(i);
            if(propertyNode.getNodeType() == Node.ELEMENT_NODE){
                //设置每一个property
                properties.setProperty(propertyNode.getAttributes().getNamedItem("name").getNodeValue(),
                        propertyNode.getAttributes().getNamedItem("value").getNodeValue());
            }
        }
        //创建环境对象
        MyEnvironment environment = new MyEnvironment();
        environment.setDriver(properties.getProperty("driver"));
        environment.setUrl(properties.getProperty("url"));
        environment.setUsername(properties.getProperty("username"));
        environment.setPassword(properties.getProperty("password"));

        Map<String, MyMappedStatement> myMappedStatementMap = new ConcurrentHashMap<>();
        //找到mappers这个节点
        Node mappersNode = parser.xNode("/configuration/mappers");
        NodeList mapperNodeList = mappersNode.getChildNodes();
        for(int i = 0; i < mapperNodeList.getLength(); i++){
            //获取每一个mapper节点
            Node mapperNode = mapperNodeList.item(i);
            if(mapperNode.getNodeType() == Node.ELEMENT_NODE){
                //获取每一个mapper的resource，即路径
                String resource = mapperNode.getAttributes().getNamedItem("resource").getNodeValue();

                //再创建一个XPath，解析每个mapper文件
                InputStream in = this.getClass().getClassLoader().getResourceAsStream(resource);
                parser = new MyXPathParser(in);
                Element element = parser.getDocument().getDocumentElement();
                //获取命名空间
                String namespace = element.getAttribute("namespace");

                //获取所有的sql语句，封装成MyMappedStatement
                NodeList sqlNodeList = element.getChildNodes();
                for(int j = 0; j < sqlNodeList.getLength(); j++){
                    //得到每一个sql对象
                    Node sqlNode = sqlNodeList.item(i);
                    if(sqlNode.getNodeType() == Node.ELEMENT_NODE){
                        //分别获取id parameterType和resultType
                        String id = "";
                        String resultType = "";
                        String parameterType = "";
                        Node idNode = sqlNode.getAttributes().getNamedItem("id");
                        if(null == idNode){
                            throw new RuntimeException("id不能为空");
                        }else{
                            id = idNode.getNodeValue();
                        }
                        Node resultTypeNode = sqlNode.getAttributes().getNamedItem("resultType");
                        if(null != resultTypeNode){
                            resultType = resultTypeNode.getNodeValue();
                        }
                        Node parameterTypeNode = sqlNode.getAttributes().getNamedItem("parameterType");
                        if(null != parameterTypeNode){
                            parameterType = parameterTypeNode.getNodeValue();
                        }
                        String sql = sqlNode.getTextContent();

                        //封装mapper对象
                        MyMappedStatement myMappedStatement = new MyMappedStatement();
                        myMappedStatement.setNamespace(namespace);
                        myMappedStatement.setParameterType(parameterType);
                        myMappedStatement.setResultType(resultType);
                        myMappedStatement.setSqlId(id);
                        myMappedStatement.setSql(sql);

                        //mapper的key namespace + "." + sqlId
                        myMappedStatementMap.put(namespace + "." + id, myMappedStatement);
                    }
                }
            }
        }

        //创建配置文件对象 即mybatis-config.xml封装
        MyConfiguration myConfiguration = new MyConfiguration();
        myConfiguration.setMyEnvironment(environment);
        myConfiguration.setMyMappedStatementMap(myMappedStatementMap);


        return myConfiguration;

    }
}
