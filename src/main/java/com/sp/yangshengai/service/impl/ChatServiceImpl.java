package com.sp.yangshengai.service.impl;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.sp.yangshengai.pojo.entity.bo.Input;
import com.sp.yangshengai.utils.SecurityUtils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChatServiceImpl {
    @Value("${ai-api.key}")
    private  String apiKey;
    @Value("${ai-api.model}")
    private  String model;

    private final Generation gen;
    //根据用户id，将聊天记录存在缓存
    private final Map<Integer, List<Message>> userChatHistories;

    public ChatServiceImpl(){
        this.gen = new Generation();
        this.userChatHistories = new HashMap<>();
    }

    // 调用模型，通过appkey和模型名称，通过GenerationParam组装用户的聊天记录，调用模型回答，返回模型输出
    public String callWithMessage(Input input) throws ApiException, NoApiKeyException, InputRequiredException {
        // 打印API密钥和模型名称，用于调试和验证
        System.out.println(apiKey);
        System.out.println(model);

        // 获取当前用户的聊天记录，如果不存在则创建一个新的列表
        List<Message> chatHistory = userChatHistories.computeIfAbsent(SecurityUtils.getUserId(), k -> new ArrayList<>());
        // 将用户的输入信息添加到聊天记录中
        chatHistory.add(Message.builder()
                .role(Role.USER.getValue())
                .content(input.getInput())
                .build());

        // 使用GenerationParam构建用户的聊天记录，包括API密钥、模型、聊天消息等，然后调用模型生成回答
        GenerationResult generationResult = gen.call(GenerationParam.builder()
                .apiKey(apiKey)
                .model(model)
                .messages(chatHistory)
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .build());

        // 获取模型输出的回答内容
        String content = generationResult.getOutput().getChoices().get(0).getMessage().getContent();
        // 打印模型的回答内容，用于调试和验证
        System.out.println(content);
        // 将模型的回答添加到聊天记录中
        chatHistory.add(Message.builder()
                .role(Role.ASSISTANT.getValue())
                .content(content).build());

        // 返回模型的回答内容
        return content;
    }
}
