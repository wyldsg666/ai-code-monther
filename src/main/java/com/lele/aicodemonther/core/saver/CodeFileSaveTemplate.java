package com.lele.aicodemonther.core.saver;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.lele.aicodemonther.exception.BusinessException;
import com.lele.aicodemonther.exception.ErrorCode;
import com.lele.aicodemonther.model.enums.CodeGenTypeEnum;

import java.io.File;


/**
 * 抽象代码文件保存器 --代码文件保存模板
 *
 * @param <T>
 */
public abstract class CodeFileSaveTemplate<T> {


    // 文件保存根目录
    private static final String FILE_SAVE_ROOT_DIR = System.getProperty("user.dir") + "/tmp/code_output";

    /**
     * 模板方法：保存代码的标准流程
     *
     * @param result 代码结果对象
     * @return 保存的目录
     */
    public final File saveCode(T result) {
        // 1.验证输入
        validateInput(result);

        // 2.构建唯一目录
        String baseDirPath = buildUniqueDir();

        // 3.保存文件
        saveFile(result, baseDirPath);

        // 4. 返回目录文件对象

        return new File(baseDirPath);
    }

    /**
     * 验证输入 (可由子类覆盖)
     *
     * @param result 代码结果对象
     */
    protected void validateInput(T result) {
        if (result == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "代码结果对象不能为空");
        }
    }

    /**
     * 构建文件的唯一路径（tmp/code_output/bizType_雪花 ID）
     *
     * @return 文件路径
     */
    protected final String buildUniqueDir() {
        String bizType = getCodeType().getValue();
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
    protected final void writeToFile(String dirPath, String fileName, String content) {
        if (StrUtil.isNotBlank(content)) {
            String filePath = dirPath + File.separator + fileName;
            FileUtil.writeUtf8String(content, filePath);
        }
    }

    /**
     * 获取代码类型 (由子类实现)
     *
     * @return 代码生成类型
     */
    protected abstract CodeGenTypeEnum getCodeType();

    /**
     * 保存文件 (由子类实现)
     *
     * @param result      代码结果对象
     * @param baseDirPath 基础目录路径
     */
    protected abstract void saveFile(T result, String baseDirPath);
}
