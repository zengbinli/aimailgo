package com.aimailgo.controller;

import com.aimailgo.model.*;
import com.aimailgo.service.MailService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 邮件 REST API 控制器
 * 
 * GET    /api/mail          — 获取邮件列表
 * GET    /api/mail/{id}      — 获取邮件详情
 * POST   /api/mail           — 创建邮件（AI自动分析）
 * DELETE /api/mail/{id}      — 删除邮件
 * POST   /api/mail/{id}/approve — 批准邮件
 * POST   /api/mail/{id}/reject  — 驳回邮件
 * POST   /api/mail/ai/process   — AI批量处理
 * GET    /api/mail/stats     — 统计信息
 * GET    /api/mail/health    — 健康检查
 */
@RestController
@RequestMapping("/api/mail")
public class MailController {

    private final MailService mailService;

    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    @GetMapping
    public ResponseEntity<List<MailResponse>> listMails(
            @RequestParam(required = false) String intent,
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(mailService.listMails(intent, status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MailResponse> getMail(@PathVariable Long id) {
        return ResponseEntity.ok(mailService.getMail(id));
    }

    @PostMapping
    public ResponseEntity<MailResponse> createMail(@Valid @RequestBody CreateMailRequest request) {
        MailResponse response = mailService.createMail(request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteMail(@PathVariable Long id) {
        mailService.deleteMail(id);
        return ResponseEntity.ok(Map.of("status", "deleted", "id", id));
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<MailResponse> approveMail(@PathVariable Long id) {
        return ResponseEntity.ok(mailService.approveMail(id));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<MailResponse> rejectMail(@PathVariable Long id) {
        return ResponseEntity.ok(mailService.rejectMail(id));
    }

    @PostMapping("/ai/process")
    public ResponseEntity<Map<String, Object>> aiBatchProcess() {
        List<MailResponse> processed = mailService.aiBatchProcess();
        return ResponseEntity.ok(Map.of(
            "status", "success",
            "processed", processed.size(),
            "mails", processed
        ));
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        return ResponseEntity.ok(mailService.getStats());
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
            "service", "AI Mail Go",
            "status", "UP",
            "version", "0.0.1"
        ));
    }
}
