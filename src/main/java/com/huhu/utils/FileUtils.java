package com.huhu.utils;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.BufferedWriter;
import java.io.File;
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
public class FileUtils {

    /**
     * 当前项目目录
     */
    private static String dirPath;

    static {

        try {
            File directory = new File("");
            dirPath = directory.getCanonicalPath() + "\\";
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String package2Path(String packageName) {
        return packageName.replaceAll("\\.", "/");
    }

    public static void writeJavaToFile(String packageName, String fileName, String content) {
        String dirPathName = dirPath + package2Path(packageName);

        Path dirPath = Paths.get(dirPathName);

        try {
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            String path = dirPathName + "/" + fileName;

            BufferedWriter writer = Files.newBufferedWriter(Paths.get(path), StandardCharsets.UTF_8);
            writer.write(content);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeXmlToFile(String packageName, String fileName, Document doc) {
        OutputFormat format = OutputFormat.createPrettyPrint();
        // 设置 text 中是否要删除其中多余的空格
        format.setTrimText(false);
        format.setIndentSize(4);

        // 目录会在 dao 中生成，所以这里不需要额外处理
        String dirPathName = dirPath + package2Path(packageName);
        String path = dirPathName + "/" + fileName;

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
