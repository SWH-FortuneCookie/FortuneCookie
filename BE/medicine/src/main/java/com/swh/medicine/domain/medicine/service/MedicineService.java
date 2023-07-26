package com.swh.medicine.domain.medicine.service;

import com.swh.medicine.domain.medicine.domain.entity.Efficacy;
import com.swh.medicine.domain.medicine.domain.entity.Information;
import com.swh.medicine.domain.medicine.domain.entity.Medicine;
import com.swh.medicine.domain.medicine.domain.entity.TakingMedicine;
import com.swh.medicine.domain.medicine.domain.repository.EfficacyRepository;
import com.swh.medicine.domain.medicine.domain.repository.InformationRepository;
import com.swh.medicine.domain.medicine.domain.repository.MedicineRepository;
import com.swh.medicine.domain.medicine.domain.repository.TakingMedicineRepository;
import com.swh.medicine.domain.medicine.dto.response.CautionResponseDto;
import com.swh.medicine.domain.medicine.dto.response.MedicineResponseDto;
import com.swh.medicine.domain.users.domain.Users;
import com.swh.medicine.domain.users.domain.UsersRepository;
import com.swh.medicine.global.exception.CustomException;
import com.swh.medicine.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicineService {

    private final MedicineRepository medicineRepository;
    private final EfficacyRepository efficacyRepository;
    private final InformationRepository informationRepository;
    private final UsersRepository usersRepository;
    private final TakingMedicineRepository takingMedicineRepository;
    private final MappingService mappingService;

    public MedicineResponseDto getMedicine(String name) {
        Medicine medicine = medicineRepository.findByName(name).orElseThrow(() -> new CustomException(ErrorCode.MEDICINE_NOT_FOUND));
        List<Efficacy> efficacyList = efficacyRepository.findByMedicine(medicine);
        List<Information> informationList = informationRepository.findByMedicine(medicine);

        return MedicineResponseDto.of(medicine, efficacyList, informationList);
    }

    public CautionResponseDto getCaution(String name) {
        Medicine medicine = medicineRepository.findByName(name).orElseThrow(() -> new CustomException(ErrorCode.MEDICINE_NOT_FOUND));
        return CautionResponseDto.of(medicine);
    }

    public String takingMedicine(String deviceId, String name) {
        Users users = usersRepository.findByDevice(deviceId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Medicine medicine = medicineRepository.findByName(name).orElseThrow(() -> new CustomException(ErrorCode.MEDICINE_NOT_FOUND));
        TakingMedicine takingMedicine = TakingMedicine.of(users);
        takingMedicineRepository.save(takingMedicine); // 사용자 복약 생성
        mappingService.save(takingMedicine, medicine); // 사용자 복약 매핑 생성

        return name + "을 내 복약에 추가완료하였습니다.";
    }

}
