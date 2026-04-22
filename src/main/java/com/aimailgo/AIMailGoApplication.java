package com.aimailgo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * AI Mail Go — AI 原生邮件系统
 * 
 * 消息即 API | 意图优先 | 密码学身份 | Agent 原生
 */
@SpringBootApplication
public class AIMailGoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AIMailGoApplication.class, args);
        System.out.println("\n✅ AI Mail Go 服务已启动！http://localhost:8080");
    }
}
