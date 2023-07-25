package com.swh.medicine.domain.medicine.domain.repository;

import com.swh.medicine.domain.medicine.domain.entity.Information;
import com.swh.medicine.domain.medicine.domain.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InformationRepository extends JpaRepository<Information, Long> {
    List<Information> findByMedicine(Medicine medicine);
}
