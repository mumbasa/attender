package com.attendance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.attendance.data.Bank;

public interface BankRepository extends JpaRepository<Bank, Long>{

}
