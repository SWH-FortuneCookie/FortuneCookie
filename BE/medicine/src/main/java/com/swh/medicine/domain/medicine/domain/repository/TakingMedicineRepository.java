package com.swh.medicine.domain.medicine.domain.repository;

import com.swh.medicine.domain.medicine.domain.entity.Medicine;
import com.swh.medicine.domain.medicine.domain.entity.TakingMedicine;
import com.swh.medicine.domain.users.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TakingMedicineRepository extends JpaRepository<TakingMedicine, Long>{
    List<TakingMedicine> findByUsers(Users users);

    TakingMedicine findByUsersAndMedicine(Users users, Medicine medicine);
}
