package com.attendance.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.attendance.models.Certification;

@Repository
public interface CertificationRepository extends JpaRepository<Certification, Long> {

}
