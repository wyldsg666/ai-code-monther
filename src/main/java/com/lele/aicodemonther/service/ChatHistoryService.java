package com.lele.aicodemonther.service;

import com.lele.aicodemonther.model.dto.chathistory.ChatHistoryQueryRequest;
import com.lele.aicodemonther.model.entity.User;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.lele.aicodemonther.model.entity.ChatHistory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;

import java.time.LocalDateTime;

/**
 * 对话历史 服务层。
 *
 * @author <a href="https://github.com/wyldsg666">ZoneSt</a>
 */
public interface ChatHistoryService extends IService<ChatHistory> {

    /**
     * 将对话历史加载到内存中
     *
     * @param appId      应用id
     * @param chatMemory 内存
     * @param maxCount   最大加载数量
     * @return 加载的数量
     */
    int loadChatHistoryToMemory(Long appId, MessageWindowChatMemory chatMemory, int maxCount);

    /**
     * 添加对话记录
     *
     * @param appId       应用id
     * @param content     内容
     * @param messageType 消息类型(AI或者用户)
     * @param userId      登录用户 ID
     * @return 是否成功
     */
    boolean addChatMessage(Long appId, String content, String messageType, Long userId);

    /**
     * 根据应用id删除对话记录
     *
     * @param appId 应用id
     * @return 是否成功
     */
    boolean deleteByAppId(Long appId);

    /**
     * 分页查询应用对话历史
     *
     * @param appId          应用id
     * @param pageSize       页面大小
     * @param lastCreateTime 最后创建时间
     * @param loginUser      登录用户
     * @return 对话历史分页结果
     */
    Page<ChatHistory> listAppChatHistoryByPage(Long appId, int pageSize,
                                               LocalDateTime lastCreateTime,
                                               User loginUser);

    /**
     * 构造查询条件
     *
     * @param request 查询请求
     * @return 查询结果
     */
    QueryWrapper getQueryWrapper(ChatHistoryQueryRequest request);
}
