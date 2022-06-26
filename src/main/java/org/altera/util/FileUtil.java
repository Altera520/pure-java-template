package org.altera.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

public class FileUtil {

    public static List<String> readFile(String filepath) throws IOException {
        File file = new File(FileUtil.class.getClassLoader().getResource(filepath).getPath());
        return FileUtils.readLines(file, Charset.defaultCharset());
    }

    public static String readSql(String filepath) throws IOException {
        return String.join(StringUtils.LF, readFile(filepath));
    }
}
