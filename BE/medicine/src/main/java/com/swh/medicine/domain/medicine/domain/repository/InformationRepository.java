package com.swh.medicine.domain.medicine.domain.repository;

import com.swh.medicine.domain.medicine.domain.entity.Information;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InformationRepository extends JpaRepository<Information, Long> {
}
