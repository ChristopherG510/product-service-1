package com.excelsisproject.productservice.repositories;

import com.excelsisproject.productservice.entities.ClosingDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClosingDetailsRepository extends JpaRepository<ClosingDetails, Long> {
}
