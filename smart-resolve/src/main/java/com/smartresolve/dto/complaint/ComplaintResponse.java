package com.smartresolve.dto.complaint;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ComplaintResponse {

    private Long id;
    private String title;
    private String description;
    private String status;

    // 🔽 OPTIONAL (for ADMIN view)
    private String userEmail;
    private LocalDateTime createdAt;

    // 🔽 REQUIRED for USER view
    public ComplaintResponse(Long id, String title, String description, String status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
    }
}
