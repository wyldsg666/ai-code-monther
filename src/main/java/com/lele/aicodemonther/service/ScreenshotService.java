package com.lele.aicodemonther.service;

/**
 * 截图服务
 */
public interface ScreenshotService {

    /**
     * 生成并上传截图
     *
     * @param webUrl 要截图的网页地址
     * @return 图片URL
     */
    String generateAndUploadScreenshot(String webUrl);
}
