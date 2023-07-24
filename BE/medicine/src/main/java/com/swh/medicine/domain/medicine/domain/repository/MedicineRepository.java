package com.swh.medicine.domain.medicine.domain.repository;

import com.swh.medicine.domain.medicine.domain.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {
}
