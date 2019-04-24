package com.huhu.utils;

import com.huhu.constants.StringConstants;
import com.huhu.init.InitParameters;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @Author: wilimm
 * @Date: 2019/4/23 10:38
 */
@Component
public class FileUtils {

    @Autowired
    private InitParameters initParameters;

    private String package2Path(String packageName) {
        return packageName.replaceAll("\\.", StringConstants.FILE_SEPARATOR);
    }

    private static String getFirstSubPackage(String fileName) {
        for (int i = 1; i < fileName.length(); i ++) {
            if (Character.isUpperCase(fileName.charAt(i))) {
                return fileName.substring(0, i).toLowerCase();
            }
        }
        return StringConstants.EMPTY;
    }

    public void writeJavaToFile(String packageName, String fileName, String content) {
        String dirPathName = initParameters.getJavaSrcPath() + package2Path(packageName)
                + StringConstants.FILE_SEPARATOR + getFirstSubPackage(fileName);

        // System.out.println(dirPathName);

        Path dirPath = Paths.get(dirPathName);

        try {
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            String path = dirPathName + StringConstants.FILE_SEPARATOR + fileName;

            BufferedWriter writer = Files.newBufferedWriter(Paths.get(path), StandardCharsets.UTF_8);
            writer.write(content);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeXmlToFile(String fileName, Document doc) {
        OutputFormat format = OutputFormat.createPrettyPrint();
        // 设置 text 中是否要删除其中多余的空格
        format.setTrimText(false);
        format.setIndentSize(4);

        // 目录会在 dao 中生成，所以这里不需要额外处理
        String path = initParameters.getJavaSrcPath() + "/" + fileName;

        XMLWriter writer = null;
        try {
            OutputStream fileOutputStream = new FileOutputStream(path);
            writer = new XMLWriter(fileOutputStream, format);
            writer.write(doc);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
