package com.huhu.service;

import com.huhu.constants.CharacterConstants;
import com.huhu.domain.entity.PojoClass;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.DefaultDocumentType;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @Author: wilimm
 * @Date: 2019/3/2 10:55
 */
@Service
public class MapperXmlService {

    /**
     * DAO 的后缀
     */
    private static final String DAO_SUFFIX = "Dao";

    /**
     * DAO 的包名
     */
    private String daoPackage;


    public Document generateMapperXml(PojoClass pojoClass, String daoPackage) {
        Document mapperDoc = createMapperDoc();

        Element mapper = mapperDoc.addElement("mapper");
        String daoClassName = daoPackage + "." + pojoClass.getClassName() + DAO_SUFFIX;
        mapper.addAttribute("namespace", daoClassName);

        mapper.addText(CharacterConstants.NEW_LINE);
        mapper.addText(" ");

        // 1. 填充 resultMap 节点
        fillResultMap(pojoClass, mapper);

        mapper.addText(CharacterConstants.NEW_LINE);
        mapper.addText(" ");


        // 2. 填充 sql 节点
        fillSql(pojoClass, mapper);

        mapper.addText(CharacterConstants.NEW_LINE);
        mapper.addText(" ");

        // 3. 填充 insert save 节点
        fillInsertSave(pojoClass, mapper);

        mapper.addText(CharacterConstants.NEW_LINE);
        mapper.addText(" ");

        // 4. 填充 insert saveOrUpdate 节点
        fillInsertSaveOrUpdate(pojoClass, mapper);

        mapper.addText(CharacterConstants.NEW_LINE);
        mapper.addText(" ");

        // 4. 填充 select findById 节点
        fillSelectFindById(pojoClass, mapper);

        mapper.addText(CharacterConstants.NEW_LINE);
        mapper.addText(" ");

        // 5. 填充 update updateByPrimaryKeySelective 节点
        fillUpdateByPrimaryKeySelective(pojoClass, mapper);


        return mapperDoc;
    }

    private void fillUpdateByPrimaryKeySelective(PojoClass pojoClass, Element mapper) {
        Element updateByPrimaryKeySelective = mapper.addElement("update");
        updateByPrimaryKeySelective.addAttribute("id", "updateByPrimaryKeySelective");
        updateByPrimaryKeySelective.addAttribute("parameterType", pojoClass.fullClassName());

        updateByPrimaryKeySelective.addText(CharacterConstants.NEW_LINE);
        updateByPrimaryKeySelective.addText(CharacterConstants.TAB);
        updateByPrimaryKeySelective.addText(CharacterConstants.TAB);

        updateByPrimaryKeySelective.addText("UPDATE ");
        updateByPrimaryKeySelective.addText(pojoClass.getTableName());

        Element set = updateByPrimaryKeySelective.addElement("set");

        pojoClass.getFieldList().stream()
                .filter(filed -> !filed.getName().equals("id"))
                .forEach(filed -> {
                    Element ifELement = set.addElement("if");

                    ifELement.addText(CharacterConstants.NEW_LINE);
                    ifELement.addText(CharacterConstants.TAB);
                    ifELement.addText(CharacterConstants.TAB);
                    ifELement.addText(CharacterConstants.TAB);
                    ifELement.addText(CharacterConstants.TAB);

                    String testValue = filed.getName() + " != null";
                    ifELement.addAttribute("test", testValue);

                    String textValue = filed.getColumnName() + " = #{" + filed.getName() + "},";
                    ifELement.addText(textValue);

                    ifELement.addText(CharacterConstants.NEW_LINE);
                    ifELement.addText(CharacterConstants.TAB);
                    ifELement.addText(CharacterConstants.TAB);
                    ifELement.addText(CharacterConstants.TAB);

                });

        updateByPrimaryKeySelective.addText(CharacterConstants.NEW_LINE);
        updateByPrimaryKeySelective.addText(CharacterConstants.TAB);
        updateByPrimaryKeySelective.addText(CharacterConstants.TAB);

        updateByPrimaryKeySelective.addText(" WHERE ")
                .addText("id=#{id}");
    }

    private void fillSelectFindById(PojoClass pojoClass, Element mapper) {
        Element selectFindById = mapper.addElement("select");
        selectFindById.addAttribute("id", "findById");
        selectFindById.addAttribute("resultMap", "BaseResultMap");

        selectFindById.addText(CharacterConstants.NEW_LINE);
        selectFindById.addText(CharacterConstants.TAB);
        selectFindById.addText(CharacterConstants.TAB);

        selectFindById.addText("SELECT ");
        Element include = selectFindById.addElement("include");
        include.addAttribute("refid", "Base_Column_List");

        selectFindById.addText(CharacterConstants.NEW_LINE);
        selectFindById.addText(CharacterConstants.TAB);
        selectFindById.addText(CharacterConstants.TAB);

        selectFindById.addText("FROM ")
                .addText(pojoClass.getTableName())
                .addText(" WHERE ")
                .addText("id=#{id}");
    }

    private void fillInsertSaveOrUpdate(PojoClass pojoClass, Element mapper) {
        Element insertSaveOrUpdate = mapper.addElement("insert");
        insertSaveOrUpdate.addAttribute("id", "saveOrUpdate");
        insertSaveOrUpdate.addAttribute("parameterType", pojoClass.fullClassName());

        String insertSql = genInsertSQL(pojoClass, true);

        StringBuilder duplicateKeyUpdateSql = new StringBuilder();

        duplicateKeyUpdateSql.append(CharacterConstants.NEW_LINE)
                .append(CharacterConstants.TAB)
                .append(CharacterConstants.TAB)
                .append("ON DUPLICATE KEY UPDATE")
                .append(CharacterConstants.NEW_LINE)
                .append(CharacterConstants.TAB)
                .append(CharacterConstants.TAB);

        pojoClass.getFieldList().stream()
                .filter(filed -> !filed.getName().equals("id"))
                .forEach(filed -> {
                    duplicateKeyUpdateSql.append(filed.getColumnName())
                            .append(" = ")
                            .append("VALUES(")
                            .append(filed.getColumnName())
                            .append("), ");
                });

        String sql = insertSql + duplicateKeyUpdateSql.substring(0, duplicateKeyUpdateSql.length() - 2);
        addText(insertSaveOrUpdate, sql);
    }

    private void fillInsertSave(PojoClass pojoClass, Element mapper) {
        Element insertSave = mapper.addElement("insert");
        insertSave.addAttribute("id", "save");
        insertSave.addAttribute("parameterType", pojoClass.fullClassName());

        // 如果需要返回自增主键 id 的值，则添加下面的属性，暂时默认添加
        insertSave.addAttribute("keyProperty", "id");
        insertSave.addAttribute("useGeneratedKeys", "true");

        String sql = genInsertSQL(pojoClass, false);
        addText(insertSave, sql);
    }

    private String genInsertSQL(PojoClass pojoClass, boolean includeId) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO")
                .append(" ")
                .append(pojoClass.getTableName())
                .append("(");

        StringBuilder columns = new StringBuilder();
        StringBuilder fields = new StringBuilder();

        pojoClass.getFieldList().stream()
                .filter(filed -> includeId || !filed.getName().equals("id"))
                .forEach(filed -> {
                    columns.append(filed.getColumnName()).append(", ");

                    fields.append("#{").append(filed.getName()).append("}, ");
                });

        sql.append(columns.substring(0, columns.length() - 2))
                .append(") ")
                .append("VALUES")
                .append(" (")
                .append(fields.substring(0, fields.length() - 2))
                .append(")");

        return sql.toString();
    }


    private void fillSql(PojoClass pojoClass, Element mapper) {
        Element sql = mapper.addElement("sql");
        sql.addAttribute("id", "Base_Column_List");

        StringBuilder sb = new StringBuilder();
        pojoClass.getFieldList().forEach(field -> {
            sb.append(field.getColumnName()).append(", ");
        });
        addText(sql, sb.substring(0, sb.length() - 2));
    }

    private void addText(Element element, String text) {
        element.addText(CharacterConstants.NEW_LINE);
        element.addText(CharacterConstants.TAB);
        element.addText(CharacterConstants.TAB);

        element.addText(text);

        element.addText(CharacterConstants.NEW_LINE);
        element.addText(CharacterConstants.TAB);
    }


    private void fillResultMap(PojoClass pojoClass, Element mapper) {
        Element resultMap = mapper.addElement("resultMap");
        resultMap.addAttribute("id", "BaseResultMap");
        resultMap.addAttribute("type", pojoClass.fullClassName());

        Element id = resultMap.addElement("id");
        id.addAttribute("column", "id");
        id.addAttribute("property", "id");

        pojoClass.getFieldList().stream()
                .filter(filed -> !filed.getName().equals("id"))
                .forEach(filed -> {
                    Element result = resultMap.addElement("result");
                    result.addAttribute("column", filed.getColumnName());
                    result.addAttribute("property", filed.getName());
                });
    }


    private Document createMapperDoc() {
        // 建立 document 对象，用来操作 xml 文件
        Document document = DocumentHelper.createDocument();

        DefaultDocumentType documentType = new DefaultDocumentType();
        documentType.setElementName("mapper");
        documentType.setPublicID("-//mybatis.org//DTD Mapper 3.0//EN");
        documentType.setSystemID("http://mybatis.org/dtd/mybatis-3-mapper.dtd");

        document.setDocType(documentType);
        return document;
    }


    public void printDoc(Document doc) throws IOException {
        OutputFormat format = OutputFormat.createPrettyPrint();
        // 设置 text 中是否要删除其中多余的空格
       format.setTrimText(false);
       format.setIndentSize(4);

        XMLWriter writer = new XMLWriter(System.out, format);
        writer.write(doc);
        writer.flush();
    }

}