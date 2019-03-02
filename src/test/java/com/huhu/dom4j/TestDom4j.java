package com.huhu.dom4j;

import com.huhu.DemoApplication;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.DefaultDocumentType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * @Author: wilimm
 * @Date: 2019/3/2 10:17
 */
public class TestDom4j {

    public static void main(String[] args) throws IOException {
        String dir = DemoApplication.class.getClassLoader().getResource("").getPath();
        String filename = dir + "template.xml";

        Document doc = createDoc();

        writeToConsole(doc);
    }

    public static Document load(String filename) {
        Document document = null;
        try {
            SAXReader saxReader = new SAXReader();
            document = saxReader.read(new File(filename)); // 读取XML文件,获得document对象
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return document;
    }


    public static boolean doc2XmlFile(Document document, String filename) {
        boolean flag = true;
        try {
            XMLWriter writer = new XMLWriter(new OutputStreamWriter(
                    new FileOutputStream(filename), "UTF-8"));
            writer.write(document);
            writer.close();
        } catch (Exception ex) {
            flag = false;
            ex.printStackTrace();
        }
        System.out.println(flag);
        return flag;
    }


    public static void writeToConsole(Document doc) throws IOException {
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter writer = new XMLWriter(System.out, format);
        writer.write(doc);
        writer.close();
    }

    public static Document createDoc() {
        Document document = DocumentHelper.createDocument();// 建立document对象，用来操作xml文件

        DefaultDocumentType documentType = new DefaultDocumentType();
        documentType.setElementName("mapper");
        documentType.setPublicID("-//mybatis.org//DTD Mapper 3.0//EN");
        documentType.setSystemID("http://mybatis.org/dtd/mybatis-3-mapper.dtd");

        document.setDocType(documentType);

        Element booksElement = document.addElement("books");// 建立根节点

        booksElement.addComment("This is a test for dom4j ");// 加入一行注释

        Element bookElement = booksElement.addElement("book");// 添加一个book节点

        bookElement.addAttribute("show", "yes");// 添加属性内容

        Element titleElement = bookElement.addElement("title");// 添加文本节点

        titleElement.setText("ajax in action");// 添加文本内容

        return document;
    }
}
