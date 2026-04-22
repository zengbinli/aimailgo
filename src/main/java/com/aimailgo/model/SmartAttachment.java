package com.aimailgo.model;

import java.util.*;

/**
 * 智能附件 — AI 从多模态输入自动生成的结构化附件
 * 
 * 核心创新：传统邮件附件是静态文件，智能附件是 AI 生成的可执行结构化数据
 */
public class SmartAttachment {

    /** 附件类型 */
    private AttachmentType type;

    /** AI 生成的结构化数据 */
    private Map<String, Object> data;

    /** AI 提取的关键信息摘要 */
    private String summary;

    /** 原始输入来源 */
    private InputType sourceInput;

    /** 原始文件引用 */
    private List<String> originalRefs;

    /** 置信度（0-1） */
    private double confidence;

    /** 可执行动作 */
    private AttachmentAction action;

    // ========== 常见附件类型 ==========

    public static SmartAttachment expenseReport(Map<String, Object> expense, InputType source, double confidence) {
        SmartAttachment att = new SmartAttachment();
        att.type = AttachmentType.EXPENSE_REPORT;
        att.data = expense;
        att.sourceInput = source;
        att.confidence = confidence;
        att.summary = String.format("报销申请：%-10s ¥%,.0f（共%d项）",
            expense.getOrDefault("applicant", "未知"),
            ((Number)expense.getOrDefault("amount", 0)).doubleValue(),
            expense.getOrDefault("item_count", 0));
        att.action = new AttachmentAction("approve_expense", "/api/expenses/approve", true);
        return att;
    }

    public static SmartAttachment businessCard(Map<String, Object> card, InputType source, double confidence) {
        SmartAttachment att = new SmartAttachment();
        att.type = AttachmentType.BUSINESS_CARD;
        att.data = card;
        att.sourceInput = source;
        att.confidence = confidence;
        att.summary = String.format("名片：%s - %s（%s）",
            card.getOrDefault("name", "未知"),
            card.getOrDefault("title", ""),
            card.getOrDefault("company", ""));
        att.action = new AttachmentAction("save_contact", "/api/contacts/save", true);
        return att;
    }

    public static SmartAttachment errorReport(Map<String, Object> report, InputType source, double confidence) {
        SmartAttachment att = new SmartAttachment();
        att.type = AttachmentType.ERROR_REPORT;
        att.data = report;
        att.sourceInput = source;
        att.confidence = confidence;
        att.summary = String.format("错误报告：%s（%s）",
            report.getOrDefault("error_type", "未知"),
            report.getOrDefault("severity", "medium"));
        att.action = new AttachmentAction("create_ticket", "/api/tickets/create", true);
        return att;
    }

    public static SmartAttachment generalNote(Map<String, Object> note, InputType source, double confidence) {
        SmartAttachment att = new SmartAttachment();
        att.type = AttachmentType.GENERAL_NOTE;
        att.data = note;
        att.sourceInput = source;
        att.confidence = confidence;
        att.summary = String.format("AI摘要：%s", 
            note.getOrDefault("summary", "语音/图片内容"));
        att.action = null;
        return att;
    }

    // ========== Getters ==========

    public AttachmentType getType() { return type; }
    public Map<String, Object> getData() { return data; }
    public String getSummary() { return summary; }
    public InputType getSourceInput() { return sourceInput; }
    public List<String> getOriginalRefs() { return originalRefs; }
    public double getConfidence() { return confidence; }
    public AttachmentAction getAction() { return action; }

    // ========== 静态内部类 ==========

    public enum AttachmentType {
        EXPENSE_REPORT("报销单"),
        BUSINESS_CARD("名片"),
        ERROR_REPORT("错误报告"),
        MEETING_NOTE("会议纪要"),
        CONTRACT("合同"),
        GENERAL_NOTE("AI摘要");

        private final String label;
        AttachmentType(String label) { this.label = label; }
        public String getLabel() { return label; }
    }

    public static class AttachmentAction {
        private String type;
        private String endpoint;
        private boolean oneClick;

        public AttachmentAction(String type, String endpoint, boolean oneClick) {
            this.type = type;
            this.endpoint = endpoint;
            this.oneClick = oneClick;
        }

        public String getType() { return type; }
        public String getEndpoint() { return endpoint; }
        public boolean isOneClick() { return oneClick; }
    }
}
