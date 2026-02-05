package com.smartresolve.controller;

import com.smartresolve.dto.complaint.ComplaintResponse;
import com.smartresolve.entity.Complaint;
import com.smartresolve.entity.ComplaintStatus;
import com.smartresolve.repository.ComplaintRepository;
import com.smartresolve.service.EmailService;
import com.smartresolve.service.impl.ComplaintServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final ComplaintRepository complaintRepository;
    private final EmailService emailService;

    /* ======================
       VIEW ALL COMPLAINTS
    ====================== */
    @GetMapping("/complaints")
    public ResponseEntity<Page<ComplaintResponse>> getAllComplaints(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Page<ComplaintResponse> result = complaintRepository
                .findAllWithUser(PageRequest.of(page, size))
                .map(this::mapToResponse);

        return ResponseEntity.ok(result);
    }

    /* ======================
       UPDATE STATUS + EMAIL
    ====================== */
    @PutMapping("/complaints/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestParam String status
    ) {
        ComplaintServiceImpl complaintService = null;
        complaintService.updateStatusAndNotify(id, status);
        return ResponseEntity.ok().build();
    }


    /* ======================
       ADMIN ANALYTICS
    ====================== */
    @GetMapping("/analytics")
    public ResponseEntity<Map<String, Long>> analytics() {
        return ResponseEntity.ok(Map.of(
                "total", complaintRepository.count(),
                "open", complaintRepository.countByStatus(ComplaintStatus.OPEN),
                "inProgress", complaintRepository.countByStatus(ComplaintStatus.IN_PROGRESS),
                "resolved", complaintRepository.countByStatus(ComplaintStatus.RESOLVED)
        ));
    }

    /* ======================
       MAPPER
    ====================== */
    private ComplaintResponse mapToResponse(Complaint c) {
        return new ComplaintResponse(
                c.getId(),
                c.getTitle(),
                c.getDescription(),
                c.getStatus().name(),
                c.getUser().getEmail(),
                c.getCreatedAt()
        );
    }
}
