package com.lele.aicodemonther.core;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.lele.aicodemonther.ai.model.HtmlCodeResult;
import com.lele.aicodemonther.ai.model.MultiFileCodeResult;
import com.lele.aicodemonther.model.enums.CodeGenTypeEnum;

import java.io.File;

/**
 * 代码文件保存器
 */
public class CodeFileSaver {
    // 文件保存根目录
    private static final String FILE_SAVE_ROOT_DIR = System.getProperty("user.dir") + "/tmp/code_output";

    /**
     * 保存 HTML 代码
     *
     * @param htmlCodeResult HTML 代码结果
     * @return 文件
     */
    public static File saveHtmlCodeResult(HtmlCodeResult htmlCodeResult) {
        String dirPath = buildUniqueDir(CodeGenTypeEnum.HTML.getValue());
        saveFile(dirPath, "index.html", htmlCodeResult.getHtmlCode());
        return new File(dirPath);
    }


    /**
     * 保存多文件代码
     * @param multiFileCodeResult 多文件代码结果
     * @return 文件
     */
    public static File saveMultiFileCodeResult(MultiFileCodeResult multiFileCodeResult) {
        String dirPath = buildUniqueDir(CodeGenTypeEnum.MULTI_FILE.getValue());
        saveFile(dirPath, "index.html", multiFileCodeResult.getHtmlCode());
        saveFile(dirPath, "script.js", multiFileCodeResult.getJsCode());
        saveFile(dirPath, "style.css", multiFileCodeResult.getCssCode());
        return new File(dirPath);
    }


    /**
     * 构建文件的唯一路径（tmp/code_output/bizType_雪花 ID）
     *
     * @param bizType 业务类型
     * @return 文件路径
     */
    private static String buildUniqueDir(String bizType) {
        String uniqueDirName = StrUtil.format("{}_{}", bizType, IdUtil.getSnowflakeNextId());
        String dirPath = FILE_SAVE_ROOT_DIR + File.separator + uniqueDirName;
        FileUtil.mkdir(dirPath);
        return dirPath;
    }

    /**
     * 保存单个文件
     *
     * @param dirPath  根目录
     * @param fileName 文件名
     * @param content  文件内容
     */
    private static void saveFile(String dirPath, String fileName, String content) {
        String filePath = dirPath + File.separator + fileName;
        FileUtil.writeUtf8String(content, filePath);
    }
}
