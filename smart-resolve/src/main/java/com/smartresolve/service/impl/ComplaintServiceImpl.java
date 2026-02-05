package com.smartresolve.service.impl;

import com.smartresolve.dto.complaint.AdminComplaintResponse;
import com.smartresolve.dto.complaint.ComplaintResponse;
import com.smartresolve.dto.complaint.CreateComplaintRequest;
import com.smartresolve.entity.Complaint;
import com.smartresolve.entity.ComplaintStatus;
import com.smartresolve.entity.User;
import com.smartresolve.exception.ResourceNotFoundException;
import com.smartresolve.repository.ComplaintRepository;
import com.smartresolve.repository.UserRepository;
import com.smartresolve.service.ComplaintService;
import com.smartresolve.service.EmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional   // ⭐ THIS FIXES YOUR “NULL / NOT SAVING” ISSUE
public class ComplaintServiceImpl implements ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;


    // USER
    @Override
    @Transactional
    public void createComplaint(CreateComplaintRequest dto, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Complaint complaint = new Complaint();
        complaint.setTitle(dto.getTitle());
        complaint.setDescription(dto.getDescription());
        complaint.setStatus(ComplaintStatus.OPEN);
        complaint.setUser(user); // 🔥 MUST be set BEFORE save

        complaintRepository.save(complaint);
    }


    @Override
    public List<ComplaintResponse> getMyComplaints(String email) {
        return complaintRepository
                .findByUser_Email(email)
                .stream()
                .map(c -> new ComplaintResponse(
                        c.getId(),
                        c.getTitle(),
                        c.getDescription(),
                        c.getStatus().name()
                ))
                .toList();
    }

    // ADMIN
    @Override
    public Page<AdminComplaintResponse> getAllComplaints(int page, int size) {
        return complaintRepository
                .findAllWithUser(PageRequest.of(page, size))
                .map(c -> new AdminComplaintResponse(
                        c.getId(),
                        c.getTitle(),
                        c.getDescription(),
                        c.getStatus().name(),
                        c.getUser().getEmail(),
                        c.getCreatedAt()
                ));
    }


    @Override
    public void updateComplaintStatus(Long complaintId, String status) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint not found"));

        complaint.setStatus(ComplaintStatus.valueOf(status));
        complaintRepository.save(complaint);
    }

    @Transactional
    public void updateComplaint(Long id, CreateComplaintRequest req, String email) {
        Complaint c = complaintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));

        if (!c.getUser().getEmail().equals(email))
            throw new RuntimeException("Unauthorized");

        if (c.getStatus() == ComplaintStatus.RESOLVED)
            throw new RuntimeException("Resolved complaints cannot be edited");

        c.setTitle(req.getTitle());
        c.setDescription(req.getDescription());
    }

    @Transactional
    public void updateStatusAndNotify(Long complaintId, String status) {

        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));

        ComplaintStatus newStatus = ComplaintStatus.valueOf(status);
        complaint.setStatus(newStatus);

        // 🔥 Access user email INSIDE transaction (FIX)
        String email = complaint.getUser().getEmail();
        String title = complaint.getTitle();

        complaintRepository.save(complaint);

        // 📧 Send email
        emailService.sendStatusUpdateMail(email, title, newStatus.name());
    }

}
