package com.nus.iss.eatngreet.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nus.iss.eatngreet.payment.entity.TxnLogEntity;

@Repository
public interface TxnLogRepository extends JpaRepository<TxnLogEntity, Long>{

}
