package top.cuizilin.mybatis.parsing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.builder.BuilderException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import top.cuizilin.mybatis.exception.MyBuilderException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.InputStream;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyXPathParser {
    //jdk的xml解析类
    private XPath xPath;

    //嵌入Document文档，即mybatis-config.xml这个文件的封装
    private Document document;

    public MyXPathParser(InputStream in){
        xPath = createXPath();
        document = createDocument(new InputSource(in));
    }

    private XPath createXPath(){
        XPathFactory factory = XPathFactory.newInstance();
        return factory.newXPath();
    }

    //将mybatis-config文件流解析成为document对象
    private Document createDocument(InputSource inputSource) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
            factory.setNamespaceAware(false);
            factory.setIgnoringComments(true);
            factory.setIgnoringElementContentWhitespace(false);
            factory.setCoalescing(false);
            factory.setExpandEntityReferences(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new ErrorHandler() {
                public void error(SAXParseException exception) throws SAXException {
                    throw exception;
                }

                public void fatalError(SAXParseException exception) throws SAXException {
                    throw exception;
                }

                public void warning(SAXParseException exception) throws SAXException {
                }
            });
            return builder.parse(inputSource);
        } catch (Exception e) {
            throw new MyBuilderException("Error creating document instance.  Cause: " + e, e);
        }
    }

    //根据exp解析xml节点
    public Node xNode(String exp) throws XPathExpressionException {
        return (Node)xPath.evaluate(exp, document, XPathConstants.NODE);
    }
}
