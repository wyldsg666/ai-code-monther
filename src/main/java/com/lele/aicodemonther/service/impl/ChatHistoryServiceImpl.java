package com.lele.aicodemonther.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.lele.aicodemonther.coustant.UserConstant;
import com.lele.aicodemonther.exception.ErrorCode;
import com.lele.aicodemonther.exception.ThrowUtils;
import com.lele.aicodemonther.model.dto.chathistory.ChatHistoryQueryRequest;
import com.lele.aicodemonther.model.entity.App;
import com.lele.aicodemonther.model.entity.User;
import com.lele.aicodemonther.model.enums.ChatHistoryMessageTypeEnum;
import com.lele.aicodemonther.service.AppService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.lele.aicodemonther.model.entity.ChatHistory;
import com.lele.aicodemonther.mapper.ChatHistoryMapper;
import com.lele.aicodemonther.service.ChatHistoryService;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 对话历史 服务层实现。
 *
 * @author <a href="https://github.com/wyldsg666">ZoneSt</a>
 */
@Slf4j
@Service
public class ChatHistoryServiceImpl extends ServiceImpl<ChatHistoryMapper, ChatHistory> implements ChatHistoryService {

    @Resource
    @Lazy
    private AppService appService;


    @Override
    public int loadChatHistoryToMemory(Long appId, MessageWindowChatMemory chatMemory, int maxCount) {
        try {
            QueryWrapper queryWrapper = QueryWrapper.create()
                    .eq("appId", appId)
                    .orderBy("createTime", false)
                    .limit(1, maxCount);
            List<ChatHistory> historyList = this.list(queryWrapper);
            if (CollUtil.isEmpty(historyList)) {
                return 0;
            }
            // 反转列表,确保按照时间正序
            historyList = historyList.reversed();

            // 按照时间顺序将消息添加到记忆中
            int loadedCount = 0;

            // 先清理历史缓存，防止重复加载
            chatMemory.clear();
            for (ChatHistory chatHistory : historyList) {
                if (ChatHistoryMessageTypeEnum.USER.getValue().equals(chatHistory.getMessageType())) {
                    chatMemory.add(UserMessage.from(chatHistory.getMessage()));
                } else if (ChatHistoryMessageTypeEnum.AI.getValue().equals(chatHistory.getMessageType())) {
                    chatMemory.add(AiMessage.from(chatHistory.getMessage()));
                }
                loadedCount++;
            }

            log.info("成功为 appId：{} 加载了 {} 条历史记录", appId, loadedCount);
            return loadedCount;
        } catch (Exception e) {
            log.error("加载历史记录失败, appId: {} , error: {}", appId, e.getMessage(), e);
            return 0;
        }
    }

    /**
     * 添加对话记录
     *
     * @param appId       应用id
     * @param content     内容
     * @param messageType 消息类型(AI或者用户)
     * @param userId      登录用户
     * @return 是否成功
     */
    @Override
    public boolean addChatMessage(Long appId, String content, String messageType, Long userId) {
        // 验证参数
        ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR, "应用Id不能为空");
        ThrowUtils.throwIf(StrUtil.isBlank(content), ErrorCode.PARAMS_ERROR, "内容不能为空");
        ThrowUtils.throwIf(StrUtil.isBlank(messageType), ErrorCode.PARAMS_ERROR, "消息类型不能为空");
        ThrowUtils.throwIf(userId == null, ErrorCode.SYSTEM_ERROR, "用户未登录");

        ChatHistoryMessageTypeEnum messageTypeEnum = ChatHistoryMessageTypeEnum.getEnumByValue(messageType);
        ThrowUtils.throwIf(messageTypeEnum == null, ErrorCode.PARAMS_ERROR, "不支持的消息类型");

        // 插入数据库
        ChatHistory chatHistory = ChatHistory.builder()
                .appId(appId)
                .message(content)
                .userId(userId)
                .messageType(messageType)
                .build();
        return this.save(chatHistory);
    }

    /**
     * 删除应用对话记录
     *
     * @param appId 应用id
     * @return 是否成功
     */
    @Override
    public boolean deleteByAppId(Long appId) {
        ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR, "应用Id不能为空");
        QueryWrapper queryWrapper = QueryWrapper.create()
                .eq("app_id", appId);
        return this.remove(queryWrapper);

    }

    /**
     * 分页查询应用对话记录
     *
     * @param appId          应用id
     * @param pageSize       页面大小
     * @param lastCreateTime 最后创建时间
     * @param loginUser      登录用户
     * @return 对话记录列表
     */
    @Override
    public Page<ChatHistory> listAppChatHistoryByPage(Long appId, int pageSize,
                                                      LocalDateTime lastCreateTime,
                                                      User loginUser) {
        ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR, "应用ID不能为空");
        ThrowUtils.throwIf(pageSize <= 0 || pageSize > 50, ErrorCode.PARAMS_ERROR, "页面大小必须在1-50之间");
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NOT_LOGIN_ERROR);
        // 验证权限：只有应用创建者和管理员可以查看
        App app = appService.getById(appId);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR, "应用不存在");
        boolean isAdmin = UserConstant.ADMIN_ROLE.equals(loginUser.getUserRole());
        boolean isCreator = app.getUserId().equals(loginUser.getId());
        ThrowUtils.throwIf(!isAdmin && !isCreator, ErrorCode.NO_AUTH_ERROR, "无权查看该应用的对话历史");
        // 构建查询条件
        ChatHistoryQueryRequest queryRequest = new ChatHistoryQueryRequest();
        queryRequest.setAppId(appId);
        queryRequest.setLastCreateTime(lastCreateTime);
        QueryWrapper queryWrapper = this.getQueryWrapper(queryRequest);
        // 查询数据
        return this.page(Page.of(1, pageSize), queryWrapper);
    }

    /**
     * 构造查询条件
     *
     * @param request 查询请求
     * @return 查询结果
     */
    @Override
    public QueryWrapper getQueryWrapper(ChatHistoryQueryRequest request) {
        QueryWrapper queryWrapper = QueryWrapper.create();
        if (request == null) {
            return queryWrapper;
        }
        Long id = request.getId();
        String message = request.getMessage();
        String messageType = request.getMessageType();
        Long appId = request.getAppId();
        Long userId = request.getUserId();
        LocalDateTime localDateTime = request.getLastCreateTime();
        String sortField = request.getSortField();
        String sortOrder = request.getSortOrder();
        // 查询条件
        queryWrapper.eq("id", id)
                .eq("messageType", messageType)
                .like("message", message)
                .eq("appId", appId)
                .eq("userId", userId);
        // 游标查询
        if (localDateTime != null) {
            queryWrapper.lt("create_time", localDateTime);
        }
        // 排序
        if (StrUtil.isNotBlank(sortField)) {
            queryWrapper.orderBy(sortField, "ascend".equals(sortOrder));
        } else {
            queryWrapper.orderBy("createTime", false);
        }
        return queryWrapper;
    }
}
