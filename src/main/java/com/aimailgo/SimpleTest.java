package com.aimailgo;

import com.aimailgo.ai.MailAIEngine;
import com.aimailgo.model.*;

/**
 * 纯 Java 测试 — 验证 AI Mail Go 核心逻辑
 * （无 Spring Boot 依赖）
 */
public class SimpleTest {

    public static void main(String[] args) {
        System.out.println("=== AI Mail Go 核心逻辑测试 ===\n");

        MailAIEngine engine = new MailAIEngine();

        // 测试用例
        String[][] testCases = {
            {"紧急：生产环境故障", "支付服务返回503，请立即处理"},
            {"报销审批", "张三提交了差旅报销 ¥3,200"},
            {"服务器监控日报", "所有服务运行正常"},
            {"合同审查请求", "请审查附件中的服务合同"},
            {"项目预算讨论", "关于Q2项目预算的讨论"},
            {"产品报价单", "请查收2026年Q2最新产品报价单"},
        };

        System.out.println("📧 意图识别测试:");
        System.out.println("─".repeat(80));

        for (String[] tc : testCases) {
            String subject = tc[0];
            String content = tc[1];

            MailIntent intent = engine.detectIntent(subject, content);
            int priority = engine.assessPriority(subject, content, intent);
            boolean autoExec = engine.canAutoExecute(intent, priority, content);
            String suggestion = engine.generateSuggestion(intent, subject);

            System.out.printf("主题: %-20s | 意图: %-10s | 优先级: %2d | AI处理: %s%n",
                    truncate(subject, 20), intent.getLabel(), priority, autoExec ? "✅" : "❌");
            System.out.printf("       建议: %s%n", suggestion);
            System.out.println();
        }

        System.out.println("─".repeat(80));
        System.out.println("✅ 核心逻辑测试完成！");

        // 统计
        int autoCount = 0;
        for (String[] tc : testCases) {
            MailIntent intent = engine.detectIntent(tc[0], tc[1]);
            int priority = engine.assessPriority(tc[0], tc[1], intent);
            if (engine.canAutoExecute(intent, priority, tc[1])) autoCount++;
        }
        System.out.printf("📊 可AI自动处理: %d/%d (%.0f%%)%n",
                autoCount, testCases.length, (double)autoCount / testCases.length * 100);
    }

    static String truncate(String s, int len) {
        return s.length() <= len ? s : s.substring(0, len) + "...";
    }
}
