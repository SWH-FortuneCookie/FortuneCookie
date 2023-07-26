package com.swh.medicine.domain.medicine.domain.repository;

import com.swh.medicine.domain.medicine.domain.entity.Mapping;
import com.swh.medicine.domain.medicine.domain.entity.Medicine;
import com.swh.medicine.domain.medicine.domain.entity.TakingMedicine;
import com.swh.medicine.domain.users.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MappingRepository extends JpaRepository<Mapping, Long> {
    Mapping findByTakingMedicineAndMedicine(TakingMedicine takingMedicine, Medicine medicine);
}
