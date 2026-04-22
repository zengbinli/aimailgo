package com.aimailgo.ai;

import com.aimailgo.model.*;

import java.util.*;

/**
 * 多模态 AI 引擎
 * 
 * 核心功能：
 * 1. 语音 → 文字转写（集成 Whisper）
 * 2. 图片 → 结构化数据提取（集成 GPT-4V）
 * 3. 意图识别 → 自动生成邮件+智能附件
 */
public class MultimodalAIEngine {

    private final MailAIEngine mailAI;

    public MultimodalAIEngine() {
        this.mailAI = new MailAIEngine();
    }

    // ========================================
    // 核心入口：多模态输入 → 邮件 + 智能附件
    // ========================================

    /**
     * 处理多模态输入，生成完整的邮件内容
     * 
     * @param inputType 输入类型（语音/图片/语音+图片）
     * @param text 文本内容（如果有的话）
     * @param voiceRef 语音文件引用（URL）
     * @param imageRefs 图片文件引用列表
     * @return 处理结果（包含邮件内容+智能附件）
     */
    public ProcessResult process(InputType inputType, String text, 
                                  String voiceRef, List<String> imageRefs) {
        
        // Step 1: 提取文本
        String extractedText = extractText(inputType, text, voiceRef);
        
        // Step 2: 识别意图
        MailIntent intent = mailAI.detectIntent(extractedText, "");
        
        // Step 3: 评估优先级
        int priority = mailAI.assessPriority(extractedText, "", intent);
        
        // Step 4: 生成智能附件
        List<SmartAttachment> attachments = generateAttachments(
            inputType, extractedText, imageRefs);
        
        // Step 5: 生成邮件正文
        String subject = generateSubject(intent, extractedText, attachments);
        String body = generateBody(intent, extractedText, attachments);
        
        // Step 6: 判断是否可自动执行
        boolean autoExec = mailAI.canAutoExecute(intent, priority, extractedText);
        String suggestion = mailAI.generateSuggestion(intent, subject);
        
        return new ProcessResult(
            extractedText,
            subject,
            body,
            intent,
            priority,
            autoExec,
            suggestion,
            attachments
        );
    }

    // ========================================
    // 文本提取
    // ========================================

    /**
     * 从多模态输入中提取文本
     */
    String extractText(InputType inputType, String text, String voiceRef) {
        if (inputType == InputType.TEXT || inputType == InputType.VOICE) {
            return text;
        }
        
        if (inputType == InputType.IMAGE) {
            // 模拟 GPT-4V 图片理解
            return simulateImageUnderstanding(text);
        }
        
        if (inputType == InputType.VOICE_IMAGE || inputType == InputType.VIDEO) {
            return text; // 语音转文字 + 图片理解的组合
        }
        
        return text;
    }

    /**
     * 模拟语音转文字（后续集成 Whisper）
     * 
     * 实际实现：
     * 1. 上传语音到 Whisper API
     * 2. 获取转写文本
     * 3. 清理、格式化
     */
    String transcribeVoice(String voiceRef) {
        // 模拟 Whisper 转写
        return "模拟语音转写结果：这是语音输入的文本内容";
    }

    // ========================================
    // 智能附件生成
    // ========================================

    /**
     * 根据输入类型和内容生成智能附件
     */
    List<SmartAttachment> generateAttachments(InputType inputType, 
                                               String text, 
                                               List<String> imageRefs) {
        List<SmartAttachment> attachments = new ArrayList<>();

        // 分析内容，判断需要生成什么附件
        if (containsAny(text, "报销", "发票", "费用", "差旅", "花费", "交通", "酒店", "餐饮")) {
            Map<String, Object> expense = extractExpenseData(text, imageRefs);
            attachments.add(SmartAttachment.expenseReport(expense, inputType, 0.95));
        }

        if (containsAny(text, "名片", "联系方式", "电话", "邮箱", "职位")) {
            Map<String, Object> card = extractBusinessCardData(text, imageRefs);
            attachments.add(SmartAttachment.businessCard(card, inputType, 0.90));
        }

        if (containsAny(text, "报错", "错误", "异常", "bug", "故障", "崩溃", "截图")) {
            Map<String, Object> error = extractErrorData(text, imageRefs);
            attachments.add(SmartAttachment.errorReport(error, inputType, 0.88));
        }

        // 如果没有特定附件，生成通用 AI 摘要
        if (attachments.isEmpty()) {
            Map<String, Object> note = new HashMap<>();
            note.put("summary", summarize(text));
            note.put("original_length", text.length());
            attachments.add(SmartAttachment.generalNote(note, inputType, 0.92));
        }

        return attachments;
    }

    // ========================================
    // 数据提取（模拟 AI，后续接入 GPT-4V）
    // ========================================

    /**
     * 从文本/图片中提取报销数据
     */
    Map<String, Object> extractExpenseData(String text, List<String> imageRefs) {
        Map<String, Object> expense = new HashMap<>();
        
        // 模拟 AI 提取
        expense.put("applicant", "张三");
        expense.put("department", "技术部");
        expense.put("expense_type", "差旅报销");
        expense.put("amount", extractAmount(text));
        expense.put("currency", "CNY");
        expense.put("date", "2026-04-22");
        expense.put("description", summarize(text));
        expense.put("items", List.of(
            Map.of("name", "高铁票", "amount", 1200, "image_ref", imageRefs.size() > 0 ? imageRefs.get(0) : null),
            Map.of("name", "酒店", "amount", 1600, "image_ref", imageRefs.size() > 1 ? imageRefs.get(1) : null),
            Map.of("name", "餐饮", "amount", 400, "image_ref", imageRefs.size() > 2 ? imageRefs.get(2) : null)
        ));
        expense.put("item_count", 3);
        
        return expense;
    }

    /**
     * 从名片图片中提取联系人信息
     */
    Map<String, Object> extractBusinessCardData(String text, List<String> imageRefs) {
        Map<String, Object> card = new HashMap<>();
        card.put("name", "李四");
        card.put("title", "技术总监");
        card.put("company", "某科技有限公司");
        card.put("phone", "13800138000");
        card.put("email", "lisi@example.com");
        card.put("address", "北京市海淀区");
        return card;
    }

    /**
     * 从错误截图中提取错误信息
     */
    Map<String, Object> extractErrorData(String text, List<String> imageRefs) {
        Map<String, Object> error = new HashMap<>();
        error.put("error_type", "NullPointerException");
        error.put("severity", "high");
        error.put("module", "PaymentService");
        error.put("stack_trace", "com.example.PaymentService.process(PaymentService.java:142)");
        error.put("description", summarize(text));
        error.put("image_ref", imageRefs.size() > 0 ? imageRefs.get(0) : null);
        return error;
    }

    // ========================================
    // 邮件生成
    // ========================================

    String generateSubject(MailIntent intent, String text, List<SmartAttachment> attachments) {
        if (!attachments.isEmpty()) {
            String attType = attachments.get(0).getType().getLabel();
            return String.format("[%s] %s", attType, summarize(text, 20));
        }
        return summarize(text, 30);
    }

    String generateBody(MailIntent intent, String text, List<SmartAttachment> attachments) {
        StringBuilder body = new StringBuilder();
        body.append("## 📧 AI Mail Go 自动生成\n\n");
        body.append("**原文摘要：**\n");
        body.append(text.length() > 200 ? text.substring(0, 200) + "..." : text);
        body.append("\n\n");

        if (!attachments.isEmpty()) {
            body.append("**📎 AI 生成的智能附件：**\n\n");
            for (SmartAttachment att : attachments) {
                body.append(String.format("- [%s] %s（置信度: %.0f%%）%n",
                    att.getType().getLabel(),
                    att.getSummary(),
                    att.getConfidence() * 100));
                if (att.getAction() != null && att.getAction().isOneClick()) {
                    body.append(String.format("  → 一键操作: %s%n", att.getAction().getType()));
                }
            }
        }

        body.append("\n---\n");
        body.append("*由 AI Mail Go 从").append(getInputLabel(intent)).append("自动生成*");
        return body.toString();
    }

    // ========================================
    // 辅助方法
    // ========================================

    private double extractAmount(String text) {
        // 简单提取数字
        String[] parts = text.split("[,，\\s]+");
        double max = 0;
        for (String part : parts) {
            try {
                double num = Double.parseDouble(part.replaceAll("[^0-9.]", ""));
                if (num > max) max = num;
            } catch (NumberFormatException ignored) {}
        }
        return max;
    }

    private String summarize(String text) {
        return summarize(text, 50);
    }

    private String summarize(String text, int maxLen) {
        if (text == null || text.isBlank()) return "(无内容)";
        String clean = text.replaceAll("[\\n\\r]+", " ").trim();
        return clean.length() <= maxLen ? clean : clean.substring(0, maxLen) + "...";
    }

    private String simulateImageUnderstanding(String text) {
        return "图片识别结果: " + text;
    }

    private String getInputLabel(MailIntent intent) {
        return switch (intent) {
            case URGENT -> "语音/图片输入";
            case ACTION_REQUEST -> "语音/图片输入";
            default -> "语音/图片输入";
        };
    }

    private boolean containsAny(String text, String... keywords) {
        if (text == null) return false;
        String lower = text.toLowerCase();
        for (String kw : keywords) {
            if (lower.contains(kw.toLowerCase())) return true;
        }
        return false;
    }

    // ========================================
    // 处理结果
    // ========================================

    public static class ProcessResult {
        public final String extractedText;
        public final String subject;
        public final String body;
        public final MailIntent intent;
        public final int priority;
        public final boolean autoExecutable;
        public final String suggestion;
        public final List<SmartAttachment> attachments;

        public ProcessResult(String extractedText, String subject, String body,
                            MailIntent intent, int priority, boolean autoExecutable,
                            String suggestion, List<SmartAttachment> attachments) {
            this.extractedText = extractedText;
            this.subject = subject;
            this.body = body;
            this.intent = intent;
            this.priority = priority;
            this.autoExecutable = autoExecutable;
            this.suggestion = suggestion;
            this.attachments = attachments;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("═══════════════════════════════════════\n");
            sb.append("📧 AI Mail Go 处理结果\n");
            sb.append("═══════════════════════════════════════\n");
            sb.append(String.format("📋 主题: %s%n", subject));
            sb.append(String.format("🏷️  意图: %s%n", intent.getLabel()));
            sb.append(String.format("🔥 优先级: %d/20%n", priority));
            sb.append(String.format("🤖 AI处理: %s%n", autoExecutable ? "✅ 自动" : "❌ 人工"));
            sb.append(String.format("💡 建议: %s%n", suggestion));
            sb.append(String.format("📎 附件数: %d%n", attachments.size()));
            if (!attachments.isEmpty()) {
                sb.append("─".repeat(40)).append("\n");
                for (SmartAttachment att : attachments) {
                    sb.append(String.format("  📎 [%s] %s%n", att.getType().getLabel(), att.getSummary()));
                    if (att.getAction() != null) {
                        sb.append(String.format("     → 一键[%s] 置信度%.0f%%%n", 
                            att.getAction().getType(), att.getConfidence() * 100));
                    }
                }
            }
            sb.append("─".repeat(40)).append("\n");
            sb.append("📝 正文预览:\n");
            sb.append(body);
            sb.append("\n═══════════════════════════════════════\n");
            return sb.toString();
        }
    }
}
