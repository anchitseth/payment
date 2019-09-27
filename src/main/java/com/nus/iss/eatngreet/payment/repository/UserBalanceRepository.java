package com.nus.iss.eatngreet.payment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nus.iss.eatngreet.payment.entity.UserBalanceEntity;

@Repository
public interface UserBalanceRepository extends JpaRepository<UserBalanceEntity, Long>{

	List<UserBalanceEntity> findByUserEmailId(String userEmailId);
}
