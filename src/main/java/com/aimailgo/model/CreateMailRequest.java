package com.aimailgo.model;

import jakarta.validation.constraints.*;

/**
 * 创建邮件请求 DTO
 */
public record CreateMailRequest(
    @NotBlank(message = "发件人不能为空")
    String fromAddr,

    @NotBlank(message = "收件人不能为空")
    String toAddr,

    String subject,
    String content
) {
    public CreateMailRequest {
        if (fromAddr != null) fromAddr = fromAddr.trim();
        if (toAddr != null) toAddr = toAddr.trim();
    }
}
