package com.swh.medicine.domain.medicine.domain.repository;

import com.swh.medicine.domain.medicine.domain.entity.Medicine;
import com.swh.medicine.domain.medicine.domain.entity.TakingMedicine;
import com.swh.medicine.domain.users.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TakingMedicineRepository extends JpaRepository<TakingMedicine, Long>{
    List<TakingMedicine> findByUsers(Users users);

    Optional<TakingMedicine> findByUsersAndMedicine(Users users, Medicine medicine);

    @Query("SELECT t FROM TakingMedicine t WHERE t.users = :users AND t.medicine = :medicine")
    TakingMedicine findTaking(Users users, Medicine medicine);
}
