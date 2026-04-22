package com.aimailgo.model;

/**
 * 多模态输入类型
 */
public enum InputType {
    TEXT("文本"),
    VOICE("语音"),
    IMAGE("图片"),
    VIDEO("视频"),
    VOICE_IMAGE("语音+图片");

    private final String label;
    InputType(String label) { this.label = label; }
    public String getLabel() { return label; }
}
