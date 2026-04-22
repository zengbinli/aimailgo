package com.aimailgo.model;

/**
 * 邮件意图类型
 */
public enum MailIntent {
    NOTIFICATION("通知", "系统通知、报告、日报"),
    ACTION_REQUEST("操作请求", "需要执行操作的邮件"),
    URGENT("紧急", "需要立即处理的紧急事项"),
    INQUIRY("询问", "问题、咨询、讨论"),
    TRANSACTION("交易", "报价、付款、合同");

    private final String label;
    private final String description;

    MailIntent(String label, String description) {
        this.label = label;
        this.description = description;
    }

    public String getLabel() { return label; }
    public String getDescription() { return description; }
}
