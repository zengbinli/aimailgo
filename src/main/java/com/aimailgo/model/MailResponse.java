package com.aimailgo.model;

import java.time.LocalDateTime;

/**
 * 邮件响应 VO（View Object）
 */
public record MailResponse(
    Long id,
    String fromAddr,
    String toAddr,
    String subject,
    String content,
    MailIntent intent,
    String intentLabel,
    int priority,
    boolean autoExecutable,
    MailStatus status,
    String statusLabel,
    String aiSuggestion,
    LocalDateTime createdAt,
    LocalDateTime processedAt
) {
    public static MailResponse from(Mail mail) {
        return new MailResponse(
            mail.getId(),
            mail.getFromAddr(),
            mail.getToAddr(),
            mail.getSubject(),
            mail.getContent(),
            mail.getIntent(),
            mail.getIntent().getLabel(),
            mail.getPriority(),
            mail.isAutoExecutable(),
            mail.getStatus(),
            mail.getStatus().getLabel(),
            mail.getAiSuggestion(),
            mail.getCreatedAt(),
            mail.getProcessedAt()
        );
    }
}
