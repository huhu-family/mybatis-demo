package com.huhu.service;

import com.huhu.constants.CommonConstants;
import com.huhu.constants.StringConstants;
import com.huhu.domain.entity.PojoClass;
import com.huhu.utils.FileUtils;
import com.huhu.utils.TableUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.DefaultDocumentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @Author: wilimm
 * @Date: 2019/3/2 10:55
 */
@Service
public class MapperXmlService {

    @Autowired
    private FileUtils fileUtils;

    public void generateMapperXmlToFile(PojoClass pojoClass, String daoPackage) {
        String subDir = TableUtils.firstSubPackage(pojoClass.getTable().getName());
        String packageName = daoPackage + "." + subDir;

        Document doc = generateMapperXml(pojoClass, packageName);

        String fileName = pojoClass.getClassName() + CommonConstants.DAO_SUFFIX + CommonConstants.FILE_SUFFIX_XML;
        fileUtils.writeXmlToFile(fileName, subDir, doc);
    }

    public Document generateMapperXml(PojoClass pojoClass, String daoPackage) {
        Document mapperDoc = createMapperDoc();

        Element mapper = mapperDoc.addElement("mapper");
        String daoClassName = daoPackage + "." + pojoClass.getClassName() + CommonConstants.DAO_SUFFIX;
        mapper.addAttribute("namespace", daoClassName);

        mapper.addText(StringConstants.NEW_LINE);
        mapper.addText(" ");

        // 1. 填充 resultMap 节点
        fillResultMap(pojoClass, mapper);

        mapper.addText(StringConstants.NEW_LINE);
        mapper.addText(" ");


        // 2. 填充 sql 节点
        fillSql(pojoClass, mapper);

        mapper.addText(StringConstants.NEW_LINE);
        mapper.addText(" ");

        // 3. 填充 insert save 节点
        fillInsertSave(pojoClass, mapper);

        mapper.addText(StringConstants.NEW_LINE);
        mapper.addText(" ");

        // 4. 填充 insert saveOrUpdate 节点
        fillInsertSaveOrUpdate(pojoClass, mapper);

        mapper.addText(StringConstants.NEW_LINE);
        mapper.addText(" ");

        // 5. 填充 insert insertSelective 节点
        fillInsertSelective(pojoClass, mapper);

        mapper.addText(StringConstants.NEW_LINE);
        mapper.addText(" ");

        // 6. 填充 insert insertList 节点
        fillInsertList(pojoClass, mapper);

        mapper.addText(StringConstants.NEW_LINE);
        mapper.addText(" ");

        // 7. 填充 select findById 节点
        fillSelectFindById(pojoClass, mapper);

        mapper.addText(StringConstants.NEW_LINE);
        mapper.addText(" ");

        if (pojoClass.getTable().isUniqueCustomerId()) {
            // 填充 select findByCustomerId 节点
            fillSelectFindByCustomerId(pojoClass, mapper);

            mapper.addText(StringConstants.NEW_LINE);
            mapper.addText(" ");
        }

        // 8. 填充 update updateByPrimaryKeySelective 节点
        fillUpdateByPrimaryKeySelective(pojoClass, mapper);

        mapper.addText(StringConstants.NEW_LINE);
        mapper.addText(" ");

        // 9. 填充 delete deleteById 节点
        fillDeleteById(pojoClass, mapper);


        return mapperDoc;
    }

    private void fillDeleteById(PojoClass pojoClass, Element mapper) {
        Element deleteById = mapper.addElement("delete");
        deleteById.addAttribute("id", "deleteById");

        String deleteByIdSQL = "DELETE FROM " + pojoClass.getTable().getName()
                + " WHERE id=#{id}";

        addText(deleteById, deleteByIdSQL);
    }

    private void fillUpdateByPrimaryKeySelective(PojoClass pojoClass, Element mapper) {
        Element updateByPrimaryKeySelective = mapper.addElement("update");
        updateByPrimaryKeySelective.addAttribute("id", "updateByPrimaryKeySelective");
        updateByPrimaryKeySelective.addAttribute("parameterType", pojoClass.fullClassName());

        updateByPrimaryKeySelective.addText(StringConstants.NEW_LINE);
        updateByPrimaryKeySelective.addText(StringConstants.TAB);
        updateByPrimaryKeySelective.addText(StringConstants.TAB);

        updateByPrimaryKeySelective.addText("UPDATE ");
        updateByPrimaryKeySelective.addText(pojoClass.getTable().getName());

        Element set = updateByPrimaryKeySelective.addElement("set");

        pojoClass.getFieldList().stream()
                .filter(filed -> !filed.getName().equals("id"))
                .filter(filed ->
                        !(filed.getColumnName().equalsIgnoreCase("update_time")
                                || filed.getColumnName().equalsIgnoreCase("create_time"))
                )
                .forEach(filed -> {
                    Element ifELement = set.addElement("if");

                    ifELement.addText(StringConstants.NEW_LINE);
                    ifELement.addText(StringConstants.TAB);
                    ifELement.addText(StringConstants.TAB);
                    ifELement.addText(StringConstants.TAB);
                    ifELement.addText(StringConstants.TAB);

                    String testValue = filed.getName() + " != null";
                    ifELement.addAttribute("test", testValue);

                    String textValue = filed.getColumnName() + " = #{" + filed.getName() + "},";
                    ifELement.addText(textValue);

                    ifELement.addText(StringConstants.NEW_LINE);
                    ifELement.addText(StringConstants.TAB);
                    ifELement.addText(StringConstants.TAB);
                    ifELement.addText(StringConstants.TAB);

                });

        updateByPrimaryKeySelective.addText(StringConstants.NEW_LINE);
        updateByPrimaryKeySelective.addText(StringConstants.TAB);
        updateByPrimaryKeySelective.addText(StringConstants.TAB);

        updateByPrimaryKeySelective.addText(" WHERE ")
                .addText("id=#{id}");
    }

    private void fillSelectFindById(PojoClass pojoClass, Element mapper) {
        Element selectFindById = mapper.addElement("select");
        selectFindById.addAttribute("id", "findById");
        selectFindById.addAttribute("resultMap", "BaseResultMap");

        selectFindById.addText(StringConstants.NEW_LINE);
        selectFindById.addText(StringConstants.TAB);
        selectFindById.addText(StringConstants.TAB);

        selectFindById.addText("SELECT ");
        Element include = selectFindById.addElement("include");
        include.addAttribute("refid", "Base_Column_List");

        selectFindById.addText(StringConstants.NEW_LINE);
        selectFindById.addText(StringConstants.TAB);
        selectFindById.addText(StringConstants.TAB);

        selectFindById.addText("FROM ")
                .addText(pojoClass.getTable().getName())
                .addText(" WHERE ")
                .addText("id=#{id}");
    }

    private void fillSelectFindByCustomerId(PojoClass pojoClass, Element mapper) {
        Element selectFindByCustomerId = mapper.addElement("select");
        selectFindByCustomerId.addAttribute("id", "findByCustomerId");
        selectFindByCustomerId.addAttribute("resultMap", "BaseResultMap");

        selectFindByCustomerId.addText(StringConstants.NEW_LINE);
        selectFindByCustomerId.addText(StringConstants.TAB);
        selectFindByCustomerId.addText(StringConstants.TAB);

        selectFindByCustomerId.addText("SELECT ");
        Element include = selectFindByCustomerId.addElement("include");
        include.addAttribute("refid", "Base_Column_List");

        selectFindByCustomerId.addText(StringConstants.NEW_LINE);
        selectFindByCustomerId.addText(StringConstants.TAB);
        selectFindByCustomerId.addText(StringConstants.TAB);

        selectFindByCustomerId.addText("FROM ")
                .addText(pojoClass.getTable().getName())
                .addText(" WHERE ")
                .addText("customer_id=#{customerId}");
    }

    private void fillInsertList(PojoClass pojoClass, Element mapper) {
        Element insertSelective = mapper.addElement("insert");
        insertSelective.addAttribute("id", "insertList");

        insertSelective.addText(StringConstants.NEW_LINE);
        insertSelective.addText(StringConstants.TAB);
        insertSelective.addText(StringConstants.TAB);

        insertSelective.addText("INSERT INTO ");
        insertSelective.addText(pojoClass.getTable().getName());

        StringBuilder columns = new StringBuilder();
        StringBuilder fields = new StringBuilder();

        pojoClass.getFieldList().stream()
                .filter(filed -> !filed.getName().equals("id"))
                .filter(filed ->
                        !(filed.getColumnName().equalsIgnoreCase("update_time")
                                || filed.getColumnName().equalsIgnoreCase("create_time"))
                )
                .forEach(filed -> {
                    columns.append(filed.getColumnName()).append(", ");

                    fields.append("#{entity.").append(filed.getName()).append("}, ");
                });

        insertSelective.addText("(");
        insertSelective.addText(columns.substring(0, columns.length() - 2));
        insertSelective.addText(")");

        insertSelective.addText(StringConstants.NEW_LINE);
        insertSelective.addText(StringConstants.TAB);
        insertSelective.addText(StringConstants.TAB);

        insertSelective.addText("VALUES");

        //  <foreach collection="entityList" item="entity" separator=",">
        Element foreach = insertSelective.addElement("foreach");
        foreach.addAttribute("collection", "entityList");
        foreach.addAttribute("item", "entity");
        foreach.addAttribute("separator", ",");

        foreach.addText(StringConstants.NEW_LINE);
        foreach.addText(StringConstants.TAB);
        foreach.addText(StringConstants.TAB);
        foreach.addText(StringConstants.TAB);

        foreach.addText(fields.substring(0, fields.length() - 2));

        foreach.addText(StringConstants.NEW_LINE);
        foreach.addText(StringConstants.TAB);
        foreach.addText(StringConstants.TAB);
    }


    private void fillInsertSelective(PojoClass pojoClass, Element mapper) {
        Element insertSelective = mapper.addElement("insert");
        insertSelective.addAttribute("id", "insertSelective");
        insertSelective.addAttribute("parameterType", pojoClass.fullClassName());

        insertSelective.addText(StringConstants.NEW_LINE);
        insertSelective.addText(StringConstants.TAB);
        insertSelective.addText(StringConstants.TAB);

        insertSelective.addText("INSERT INTO ");
        insertSelective.addText(pojoClass.getTable().getName());

        Element trim = insertSelective.addElement("trim");
        trim.addAttribute("prefix", "(");
        trim.addAttribute("suffix", ")");
        trim.addAttribute("suffixOverrides", ",");

        pojoClass.getFieldList().stream()
                .filter(filed -> !filed.getName().equals("id"))
                .filter(filed ->
                        !(filed.getColumnName().equalsIgnoreCase("update_time")
                                || filed.getColumnName().equalsIgnoreCase("create_time"))
                )
                .forEach(filed -> {
                    Element ifELement = trim.addElement("if");

                    ifELement.addText(StringConstants.NEW_LINE);
                    ifELement.addText(StringConstants.TAB);
                    ifELement.addText(StringConstants.TAB);
                    ifELement.addText(StringConstants.TAB);
                    ifELement.addText(StringConstants.TAB);

                    String testValue = filed.getName() + " != null";
                    ifELement.addAttribute("test", testValue);

                    String textValue = filed.getColumnName() + ",";
                    ifELement.addText(textValue);

                    ifELement.addText(StringConstants.NEW_LINE);
                    ifELement.addText(StringConstants.TAB);
                    ifELement.addText(StringConstants.TAB);
                    ifELement.addText(StringConstants.TAB);

                });

        Element trimValue = insertSelective.addElement("trim");
        trimValue.addAttribute("prefix", "VALUES (");
        trimValue.addAttribute("suffix", ")");
        trimValue.addAttribute("suffixOverrides", ",");

        pojoClass.getFieldList().stream()
                .filter(filed -> !filed.getName().equals("id"))
                .filter(filed ->
                        !(filed.getColumnName().equalsIgnoreCase("update_time")
                                || filed.getColumnName().equalsIgnoreCase("create_time"))
                )
                .forEach(filed -> {
                    Element ifELement = trimValue.addElement("if");

                    ifELement.addText(StringConstants.NEW_LINE);
                    ifELement.addText(StringConstants.TAB);
                    ifELement.addText(StringConstants.TAB);
                    ifELement.addText(StringConstants.TAB);
                    ifELement.addText(StringConstants.TAB);

                    String testValue = filed.getName() + " != null";
                    ifELement.addAttribute("test", testValue);

                    String textValue = "#{" + filed.getName() + "},";
                    ifELement.addText(textValue);

                    ifELement.addText(StringConstants.NEW_LINE);
                    ifELement.addText(StringConstants.TAB);
                    ifELement.addText(StringConstants.TAB);
                    ifELement.addText(StringConstants.TAB);

                });

    }


    private void fillInsertSaveOrUpdate(PojoClass pojoClass, Element mapper) {
        Element insertSaveOrUpdate = mapper.addElement("insert");
        insertSaveOrUpdate.addAttribute("id", "saveOrUpdate");
        insertSaveOrUpdate.addAttribute("parameterType", pojoClass.fullClassName());

        String insertSql = genInsertSQL(pojoClass, true);

        StringBuilder duplicateKeyUpdateSql = new StringBuilder();

        duplicateKeyUpdateSql.append(StringConstants.NEW_LINE)
                .append(StringConstants.TAB)
                .append(StringConstants.TAB)
                .append("ON DUPLICATE KEY UPDATE")
                .append(StringConstants.NEW_LINE)
                .append(StringConstants.TAB)
                .append(StringConstants.TAB);

        pojoClass.getFieldList().stream()
                .filter(filed -> !filed.getName().equals("id"))
                .filter(filed ->
                        !(filed.getColumnName().equalsIgnoreCase("update_time")
                                || filed.getColumnName().equalsIgnoreCase("create_time"))
                )
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
                .append(pojoClass.getTable().getName())
                .append("(");

        StringBuilder columns = new StringBuilder();
        StringBuilder fields = new StringBuilder();

        pojoClass.getFieldList().stream()
                .filter(filed -> includeId || !filed.getName().equals("id"))
                .filter(filed ->
                        !(filed.getColumnName().equalsIgnoreCase("update_time")
                        || filed.getColumnName().equalsIgnoreCase("create_time"))
                )
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
        element.addText(StringConstants.NEW_LINE);
        element.addText(StringConstants.TAB);
        element.addText(StringConstants.TAB);

        element.addText(text);

        element.addText(StringConstants.NEW_LINE);
        element.addText(StringConstants.TAB);
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