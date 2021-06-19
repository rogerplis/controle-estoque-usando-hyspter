package com.roger.repository;

import com.roger.domain.Companion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Companion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompanionRepository extends JpaRepository<Companion, Long> {}
