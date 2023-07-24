package com.excelsisproject.productservice.repository;

import com.excelsisproject.productservice.entity.ClosingDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClosingDetailsRepository extends JpaRepository<ClosingDetails, Long> {
}
