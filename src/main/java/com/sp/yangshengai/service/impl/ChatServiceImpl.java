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

    private final Map<Integer, List<Message>> userChatHistories;

    public ChatServiceImpl(){
        this.gen = new Generation();
        this.userChatHistories = new HashMap<>();
    }


    public String callWithMessage(Input input) throws ApiException, NoApiKeyException, InputRequiredException {
        System.out.println(apiKey);
        System.out.println(model);

        List<Message> chatHistory = userChatHistories.computeIfAbsent(SecurityUtils.getUserId(), k -> new ArrayList<>());
        chatHistory.add(Message.builder()
                .role(Role.USER.getValue())
                .content(input.getInput())
                .build());

        GenerationResult generationResult = gen.call(GenerationParam.builder()
                .apiKey(apiKey)
                .model(model)
                .messages(chatHistory)
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .build());

        String content = generationResult.getOutput().getChoices().get(0).getMessage().getContent();
        System.out.println(content);
        chatHistory.add(Message.builder()
                .role(Role.ASSISTANT.getValue())
                .content(content).build());

        return content;





    }
}
