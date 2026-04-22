package com.aimailgo.model;

/**
 * 邮件处理状态
 */
public enum MailStatus {
    PENDING("待处理"),
    NEEDS_REVIEW("需人工审核"),
    AUTO_PROCESSED("AI已处理"),
    APPROVED("已批准"),
    REJECTED("已驳回"),
    ARCHIVED("已归档");

    private final String label;

    MailStatus(String label) { this.label = label; }
    public String getLabel() { return label; }
}
