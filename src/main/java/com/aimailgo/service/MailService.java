package com.aimailgo.service;

import com.aimailgo.ai.MailAIEngine;
import com.aimailgo.model.*;
import com.aimailgo.repository.MailRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 邮件业务服务层
 */
@Service
public class MailService {

    private final MailRepository mailRepository;
    private final MailAIEngine aiEngine;

    public MailService(MailRepository mailRepository, MailAIEngine aiEngine) {
        this.mailRepository = mailRepository;
        this.aiEngine = aiEngine;
    }

    /**
     * 创建邮件（AI 自动分析）
     */
    @Transactional
    public MailResponse createMail(CreateMailRequest request) {
        // AI 分析
        MailIntent intent = aiEngine.detectIntent(request.subject(), request.content());
        int priority = aiEngine.assessPriority(request.subject(), request.content(), intent);
        boolean autoExec = aiEngine.canAutoExecute(intent, priority, request.content());
        String suggestion = aiEngine.generateSuggestion(intent, request.subject());

        // 创建邮件
        Mail mail = new Mail(
            request.fromAddr(),
            request.toAddr(),
            request.subject(),
            request.content(),
            intent,
            priority,
            autoExec
        );

        // 自动处理或等待审核
        if (autoExec) {
            mail.markProcessed(suggestion);
        } else {
            mail.markNeedsReview(suggestion);
        }

        mail = mailRepository.save(mail);
        return MailResponse.from(mail);
    }

    /**
     * 获取邮件详情
     */
    public MailResponse getMail(Long id) {
        Mail mail = mailRepository.findById(id)
            .orElseThrow(() -> new MailNotFoundException(id));
        return MailResponse.from(mail);
    }

    /**
     * 获取所有邮件（按优先级排序）
     */
    public List<MailResponse> listMails(String intentFilter, String statusFilter) {
        List<Mail> mails;

        if (intentFilter != null && !intentFilter.isBlank()) {
            try {
                MailIntent intent = MailIntent.valueOf(intentFilter.toUpperCase());
                mails = mailRepository.findByIntentOrderByPriorityDesc(intent);
            } catch (IllegalArgumentException e) {
                mails = mailRepository.findAllByOrderByPriorityDescCreatedAtDesc();
            }
        } else if (statusFilter != null && !statusFilter.isBlank()) {
            try {
                MailStatus status = MailStatus.valueOf(statusFilter.toUpperCase());
                mails = mailRepository.findByStatusOrderByPriorityDesc(status);
            } catch (IllegalArgumentException e) {
                mails = mailRepository.findAllByOrderByPriorityDescCreatedAtDesc();
            }
        } else {
            mails = mailRepository.findAllByOrderByPriorityDescCreatedAtDesc();
        }

        return mails.stream().map(MailResponse::from).toList();
    }

    /**
     * 批准邮件
     */
    @Transactional
    public MailResponse approveMail(Long id) {
        Mail mail = mailRepository.findById(id)
            .orElseThrow(() -> new MailNotFoundException(id));
        mail.markApproved();
        mail = mailRepository.save(mail);
        return MailResponse.from(mail);
    }

    /**
     * 驳回邮件
     */
    @Transactional
    public MailResponse rejectMail(Long id) {
        Mail mail = mailRepository.findById(id)
            .orElseThrow(() -> new MailNotFoundException(id));
        mail.markRejected();
        mail = mailRepository.save(mail);
        return MailResponse.from(mail);
    }

    /**
     * 删除邮件
     */
    @Transactional
    public void deleteMail(Long id) {
        if (!mailRepository.existsById(id)) {
            throw new MailNotFoundException(id);
        }
        mailRepository.deleteById(id);
    }

    /**
     * 获取统计信息
     */
    public Map<String, Object> getStats() {
        long total = mailRepository.count();
        long autoProcessed = mailRepository.countByAutoExecutableTrue();

        Map<String, Long> byIntent = new java.util.HashMap<>();
        for (MailIntent intent : MailIntent.values()) {
            byIntent.put(intent.name(), mailRepository.countByIntent(intent));
        }

        Map<String, Long> byStatus = new java.util.HashMap<>();
        for (MailStatus status : MailStatus.values()) {
            byStatus.put(status.name(), mailRepository.countByStatus(status));
        }

        return Map.of(
            "total", total,
            "autoProcessed", autoProcessed,
            "autoProcessRate", total > 0 ? (double) autoProcessed / total * 100 : 0,
            "byIntent", byIntent,
            "byStatus", byStatus
        );
    }

    /**
     * AI 批量处理待审核邮件
     */
    @Transactional
    public List<MailResponse> aiBatchProcess() {
        List<Mail> pending = mailRepository.findByStatusAndAutoExecutableTrue(MailStatus.NEEDS_REVIEW);

        for (Mail mail : pending) {
            String suggestion = aiEngine.generateSuggestion(mail.getIntent(), mail.getSubject());
            mail.markProcessed(suggestion);
        }

        mailRepository.saveAll(pending);

        return pending.stream().map(MailResponse::from).toList();
    }
}

/**
 * 邮件未找到异常
 */
class MailNotFoundException extends RuntimeException {
    public MailNotFoundException(Long id) {
        super("邮件不存在: #" + id);
    }
}
