package com.aimailgo;

import com.aimailgo.ai.MultimodalAIEngine;
import com.aimailgo.model.*;
import java.util.*;

/**
 * AI Mail Go 多模态引擎测试
 */
public class MultimodalTest {

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║  📬 AI Mail Go — 多模态引擎测试        ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        MultimodalAIEngine engine = new MultimodalAIEngine();

        // ========== 场景1：语音报销 ==========
        System.out.println("🎤 场景1：语音报销\n");
        var result1 = engine.process(
            InputType.VOICE_IMAGE,
            "这次去北京出差，高铁票1200，酒店1600，吃饭400，一共3200，请批一下",
            "voice://recording_001.mp3",
            List.of("img://invoice_train.jpg", "img://invoice_hotel.jpg", "img://invoice_food.jpg")
        );
        System.out.println(result1);

        // ========== 场景2：拍名片 ==========
        System.out.println("📸 场景2：拍名片\n");
        var result2 = engine.process(
            InputType.IMAGE,
            "今天见了一个客户，这是他的名片，请录入系统",
            null,
            List.of("img://business_card.jpg")
        );
        System.out.println(result2);

        // ========== 场景3：截图报错 ==========
        System.out.println("🖥️ 场景3：截图报错\n");
        var result3 = engine.process(
            InputType.IMAGE,
            "支付服务报错了，NullPointerException，截图如下，紧急处理",
            null,
            List.of("img://error_screenshot.png")
        );
        System.out.println(result3);

        // ========== 场景4：语音通知 ==========
        System.out.println("🎤 场景4：语音通知\n");
        var result4 = engine.process(
            InputType.VOICE,
            "提醒团队，本周五下午3点有全员大会，请准时参加",
            "voice://meeting_reminder.mp3",
            List.of()
        );
        System.out.println(result4);

        // ========== 统计 ==========
        System.out.println("📊 测试统计");
        System.out.println("─".repeat(40));
        var results = List.of(result1, result2, result3, result4);
        long autoCount = results.stream().filter(r -> r.autoExecutable).count();
        System.out.printf("  总场景: %d%n", results.size());
        System.out.printf("  AI可自动处理: %d (%.0f%%)%n", autoCount, (double)autoCount/results.size()*100);
        System.out.printf("  需人工审核: %d%n", results.size() - autoCount);
        System.out.printf("  生成智能附件总数: %d%n", 
            results.stream().mapToInt(r -> r.attachments.size()).sum());
        
        System.out.println("\n✅ 多模态引擎测试完成！");
    }
}
