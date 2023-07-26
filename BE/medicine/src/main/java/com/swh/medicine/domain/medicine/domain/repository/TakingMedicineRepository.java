package com.swh.medicine.domain.medicine.domain.repository;

import com.swh.medicine.domain.medicine.domain.entity.TakingMedicine;
import com.swh.medicine.domain.users.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TakingMedicineRepository extends JpaRepository<TakingMedicine, Long>{
    TakingMedicine findByUsers(Users users);
}
