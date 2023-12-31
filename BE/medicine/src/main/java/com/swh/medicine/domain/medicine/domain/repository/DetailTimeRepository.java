package com.swh.medicine.domain.medicine.domain.repository;

import com.swh.medicine.domain.medicine.domain.entity.DetailTime;
import com.swh.medicine.domain.medicine.domain.entity.TakingMedicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DetailTimeRepository extends JpaRepository<DetailTime, Long> {
    List<DetailTime> findByTakingMedicine(TakingMedicine takingMedicine);

    void deleteByTakingMedicine(TakingMedicine takingMedicine);

    @Query("SELECT d.day FROM DetailTime d WHERE d.takingMedicine = :takingMedicine")
    List<Integer> findDays(@Param("takingMedicine") TakingMedicine takingMedicine);

    List<DetailTime> findByHourAndMinuteAndDay(int currentHour, int currentMinute, int currentDay);

    @Query("SELECT d FROM DetailTime d WHERE d.takingMedicine = :takingMedicine")
    DetailTime findByTakingMedicineAlarm(TakingMedicine takingMedicine);
}
