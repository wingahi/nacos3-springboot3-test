Java:17+SpringBoot:3.3.13+Spring-Ai:1.0.0.2+通义千问

1、配置nacos
Data ID: spring.ai.alibaba.configurable.prompt
Group: DEFAULT_GROUP
配置内容TEXT：
[
  {
    "name": "author",
    "template": "介绍 {author}，列出其生平经历和文学成就",
    "model": {
      "author": "鲁迅"
    }
  }
]

2、在https://bailian.console.aliyun.com/获取api-key（有免费）

3、配置api-key
spring.ai.dashscope.api-key=${api-key}

4、访问api：
http://127.0.0.1:8089/nacos/books?author=%E9%B2%81%E8%BF%85

