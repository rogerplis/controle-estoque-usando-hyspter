package com.roger.repository;

import com.roger.domain.WorkingHours;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the WorkingHours entity.
 */
@Repository
public interface WorkingHoursRepository extends JpaRepository<WorkingHours, Long> {
    @Query(
        value = "select distinct workingHours from WorkingHours workingHours left join fetch workingHours.journeys",
        countQuery = "select count(distinct workingHours) from WorkingHours workingHours"
    )
    Page<WorkingHours> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct workingHours from WorkingHours workingHours left join fetch workingHours.journeys")
    List<WorkingHours> findAllWithEagerRelationships();

    @Query("select workingHours from WorkingHours workingHours left join fetch workingHours.journeys where workingHours.id =:id")
    Optional<WorkingHours> findOneWithEagerRelationships(@Param("id") Long id);
}
