package com.wgh.nacos3.controller.ai;

import com.alibaba.cloud.ai.prompt.ConfigurablePromptTemplate;
import com.alibaba.cloud.ai.prompt.ConfigurablePromptTemplateFactory;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/nacos")
public class PromptController {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(PromptController.class);

    private final ChatClient client;

    private final ConfigurablePromptTemplateFactory promptTemplateFactory;

    public PromptController(
            ChatModel chatModel,
            ConfigurablePromptTemplateFactory promptTemplateFactory
    ) {

        this.client = ChatClient.builder(chatModel).build();
        this.promptTemplateFactory = promptTemplateFactory;
    }

    @GetMapping("/books")
    public Flux<String> generateJoke(
            @RequestParam(value = "author", required = false) String authorName,
            HttpServletResponse response
    ) {

        // 防止输出乱码
        response.setCharacterEncoding("UTF-8");

        // 1. 获取模板实例（优先使用Nacos配置）
        ConfigurablePromptTemplate template = promptTemplateFactory.getTemplate("author");

        // 2. 无配置时使用默认值
        if (template == null) {
            template = promptTemplateFactory.create(
                    "author",
                    "请列出这位{author}最著名的三本书。",
                    Map.of("author", "鲁迅")
            );
        }

        Map<String, Object> model = new HashMap<>();
        if(StringUtils.hasText(authorName)){
            // 3. 添加动态参数（覆盖模版变量）
            model.put("author", authorName);
        }

        // 4. 创建Prompt对象
        Prompt prompt = template.create(model);

        // 5. 记录日志（便于调试）
        logger.info("最终构建的 prompt 为：{}", prompt.getContents());

        // 6. 调用AI服务
        return client.prompt(prompt)
                .stream()
                .content();
    }

}