package com.smartresolve.controller;

import com.smartresolve.dto.complaint.ComplaintResponse;
import com.smartresolve.dto.complaint.CreateComplaintRequest;
import com.smartresolve.service.ComplaintService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/complaints")
@RequiredArgsConstructor
public class ComplaintController {

    private final ComplaintService complaintService;

    @PostMapping
    public ResponseEntity<Void> createComplaint(
            @RequestBody CreateComplaintRequest request,
            Authentication authentication
    ) {
        complaintService.createComplaint(request, authentication.getName());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/my")
    public ResponseEntity<List<ComplaintResponse>> getMyComplaints(
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                complaintService.getMyComplaints(authentication.getName())
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateComplaint(
            @PathVariable Long id,
            @RequestBody CreateComplaintRequest request,
            Authentication authentication
    ) {
        complaintService.updateComplaint(id, request, authentication.getName());
        return ResponseEntity.ok().build();
    }


}
