package com.swh.medicine.domain.medicine.domain.repository;

import com.swh.medicine.domain.medicine.domain.entity.Efficacy;
import com.swh.medicine.domain.medicine.domain.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EfficacyRepository extends JpaRepository<Efficacy, Long> {
    List<Efficacy> findByMedicine(Medicine medicine);
}
