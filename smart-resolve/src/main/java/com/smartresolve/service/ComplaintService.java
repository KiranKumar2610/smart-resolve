package com.smartresolve.service;

import com.smartresolve.dto.complaint.AdminComplaintResponse;
import com.smartresolve.dto.complaint.ComplaintResponse;
import com.smartresolve.dto.complaint.CreateComplaintRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ComplaintService {

    // USER
    void createComplaint(CreateComplaintRequest request, String email);
    List<ComplaintResponse> getMyComplaints(String email);

    // ADMIN
    Page<AdminComplaintResponse> getAllComplaints(int page, int size);
    void updateComplaintStatus(Long complaintId, String status);

    void updateComplaint(Long id, CreateComplaintRequest request, String name);
}
