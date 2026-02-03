package com.lele.aicodemonther.core.saver;

import cn.hutool.core.util.StrUtil;
import com.lele.aicodemonther.ai.model.MultiFileCodeResult;
import com.lele.aicodemonther.exception.BusinessException;
import com.lele.aicodemonther.exception.ErrorCode;
import com.lele.aicodemonther.model.enums.CodeGenTypeEnum;

/**
 *  多文件代码保存器
 * @author : lele
 */
public class MultiFileCodeFileSaverTemplate extends CodeFileSaveTemplate<MultiFileCodeResult> {

    @Override
    protected CodeGenTypeEnum getCodeType() {
        return CodeGenTypeEnum.MULTI_FILE;
    }

    @Override
    protected void saveFile(MultiFileCodeResult result, String baseDirPath) {
        // 保存 HTML 文件
        writeToFile(baseDirPath, "index.html", result.getHtmlCode());
        // 保存 JS 文件
        writeToFile(baseDirPath, "script.js", result.getJsCode());
        // 保存 CSS 文件
        writeToFile(baseDirPath, "style.css", result.getCssCode());
    }

    @Override
    protected void validateInput(MultiFileCodeResult result) {
        super.validateInput(result);

        if (StrUtil.isBlank(result.getHtmlCode())) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "HTML 代码不能为空");
        }
    }
}
