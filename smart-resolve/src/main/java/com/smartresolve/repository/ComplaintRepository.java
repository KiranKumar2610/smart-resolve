package com.smartresolve.repository;

import com.smartresolve.entity.Complaint;
import com.smartresolve.entity.ComplaintStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    List<Complaint> findByUser_Email(String email);

    long countByStatus(ComplaintStatus status);

    // ✅ ADMIN FETCH WITH USER EMAIL (FIXES LAZY ERROR)
    @Query("""
        select c from Complaint c
        join fetch c.user
        """)
    Page<Complaint> findAllWithUser(Pageable pageable);
}
