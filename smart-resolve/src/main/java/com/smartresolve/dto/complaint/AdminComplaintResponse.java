package com.smartresolve.dto.complaint;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AdminComplaintResponse {

    private Long id;
    private String title;
    private String description;
    private String status;
    private String userEmail;
    private LocalDateTime createdAt;
}
