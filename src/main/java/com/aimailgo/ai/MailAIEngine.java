package com.aimailgo.ai;

import com.aimailgo.model.MailIntent;
import org.springframework.stereotype.Service;

/**
 * AI 邮件处理引擎
 * 
 * 职责：
 * - 意图识别
 * - 优先级评估
 * - 自动执行判断
 * - 回复建议生成
 */
@Service
public class MailAIEngine {

    /**
     * 意图识别（关键词匹配，后续接入 LLM）
     */
    public MailIntent detectIntent(String subject, String content) {
        String text = (subject + " " + content).toLowerCase();

        // 紧急
        if (containsAny(text, "紧急", "立即", "故障", "报警", "宕机", "严重")) {
            return MailIntent.URGENT;
        }

        // 操作请求
        if (containsAny(text, "审批", "批准", "同意", "确认", "执行", "同意", "审核", "签字")) {
            return MailIntent.ACTION_REQUEST;
        }

        // 交易
        if (containsAny(text, "报价", "付款", "合同", "费用", "发票", "账单", "支付")) {
            return MailIntent.TRANSACTION;
        }

        // 询问
        if (containsAny(text, "讨论", "如何", "请问", "建议", "咨询", "帮助", "?", "？")) {
            return MailIntent.INQUIRY;
        }

        // 默认通知
        return MailIntent.NOTIFICATION;
    }

    /**
     * 优先级评估（0-20）
     */
    public int assessPriority(String subject, String content, MailIntent intent) {
        int base = switch (intent) {
            case URGENT -> 15;
            case ACTION_REQUEST -> 8;
            case TRANSACTION -> 5;
            case INQUIRY -> 3;
            case NOTIFICATION -> 1;
        };

        String text = (subject + " " + content).toLowerCase();

        // 加分项
        if (containsAny(text, "紧急", "立即")) base += 5;
        if (containsAny(text, "异常", "故障", "错误")) base += 4;
        if (containsAny(text, "生产", "线上", "客户")) base += 3;
        if (containsAny(text, "ceo", "cto", "vp", "总监")) base += 3;

        return Math.min(base, 20);
    }

    /**
     * 判断是否可自动执行
     */
    public boolean canAutoExecute(MailIntent intent, int priority, String content) {
        // 紧急邮件必须人工
        if (intent == MailIntent.URGENT) return false;

        // 交易类必须人工
        if (intent == MailIntent.TRANSACTION) return false;

        // 通知类可自动归档
        if (intent == MailIntent.NOTIFICATION) return true;

        // 操作请求：低优先级可自动
        if (intent == MailIntent.ACTION_REQUEST && priority < 10) return true;

        // 询问：生成回复草稿
        if (intent == MailIntent.INQUIRY) return true;

        return false;
    }

    /**
     * 生成 AI 建议
     */
    public String generateSuggestion(MailIntent intent, String subject) {
        return switch (intent) {
            case NOTIFICATION -> "已自动归档到" + extractCategory(subject) + "文件夹";
            case ACTION_REQUEST -> "建议操作：批准执行";
            case URGENT -> "⚠️ 紧急事项，请立即处理";
            case INQUIRY -> "已生成回复草稿，请审核后发送";
            case TRANSACTION -> "已记录交易信息，请确认后归档";
        };
    }

    // ========== 辅助方法 ==========

    private boolean containsAny(String text, String... keywords) {
        for (String keyword : keywords) {
            if (text.contains(keyword)) return true;
        }
        return false;
    }

    private String extractCategory(String subject) {
        if (subject == null) return "收件箱";
        String s = subject.toLowerCase();
        if (s.contains("周报") || s.contains("日报")) return "报告";
        if (s.contains("监控") || s.contains("告警")) return "系统";
        if (s.contains("人事") || s.contains("hr")) return "人事";
        return "通知";
    }
}
