package com.sp.yangshengai.controller;

import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.sp.yangshengai.pojo.entity.R;
import com.sp.yangshengai.pojo.entity.bo.Input;
import com.sp.yangshengai.service.impl.ChatServiceImpl;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiController {

    private final ChatServiceImpl chatService;

    @PostMapping("/chat")
    public R<String> chat(@RequestBody Input input) throws NoApiKeyException, InputRequiredException {
        return R.ok(chatService.callWithMessage(input));
    }

}
