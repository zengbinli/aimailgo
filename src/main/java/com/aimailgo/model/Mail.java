package com.aimailgo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

/**
 * 邮件实体 — 映射到数据库
 */
@Entity
@Table(name = "mails")
public class Mail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    @NotBlank(message = "发件人不能为空")
    private String fromAddr;

    @Column(nullable = false, length = 255)
    @NotBlank(message = "收件人不能为空")
    private String toAddr;

    @Column(length = 500)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MailIntent intent;

    @Column(nullable = false)
    @Min(0) @Max(20)
    private int priority;

    @Column(nullable = false)
    private boolean autoExecutable;

    @Enumerated(EnumType.STRING)
    private MailStatus status;

    @Column(length = 1000)
    private String aiSuggestion;

    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(updatable = false)
    private LocalDateTime processedAt;

    // JPA 需要默认构造器
    protected Mail() {}

    public Mail(String fromAddr, String toAddr, String subject, String content,
                MailIntent intent, int priority, boolean autoExecutable) {
        this.fromAddr = fromAddr;
        this.toAddr = toAddr;
        this.subject = subject;
        this.content = content;
        this.intent = intent;
        this.priority = Math.max(0, Math.min(20, priority));
        this.autoExecutable = autoExecutable;
        this.status = MailStatus.PENDING;
    }

    // ========== Getters ==========

    public Long getId() { return id; }
    public String getFromAddr() { return fromAddr; }
    public String getToAddr() { return toAddr; }
    public String getSubject() { return subject; }
    public String getContent() { return content; }
    public MailIntent getIntent() { return intent; }
    public int getPriority() { return priority; }
    public boolean isAutoExecutable() { return autoExecutable; }
    public MailStatus getStatus() { return status; }
    public String getAiSuggestion() { return aiSuggestion; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getProcessedAt() { return processedAt; }

    // ========== Business Methods ==========

    public void markProcessed(String suggestion) {
        this.status = MailStatus.AUTO_PROCESSED;
        this.aiSuggestion = suggestion;
        this.processedAt = LocalDateTime.now();
    }

    public void markNeedsReview(String suggestion) {
        this.status = MailStatus.NEEDS_REVIEW;
        this.aiSuggestion = suggestion;
    }

    public void markApproved() {
        this.status = MailStatus.APPROVED;
        this.processedAt = LocalDateTime.now();
    }

    public void markRejected() {
        this.status = MailStatus.REJECTED;
        this.processedAt = LocalDateTime.now();
    }
}
