package com.swh.medicine.domain.medicine.service;

import com.swh.medicine.domain.medicine.domain.entity.Efficacy;
import com.swh.medicine.domain.medicine.domain.entity.Information;
import com.swh.medicine.domain.medicine.domain.entity.Medicine;
import com.swh.medicine.domain.medicine.domain.repository.EfficacyRepository;
import com.swh.medicine.domain.medicine.domain.repository.InformationRepository;
import com.swh.medicine.domain.medicine.domain.repository.MedicineRepository;
import com.swh.medicine.domain.medicine.dto.response.MedicineResponseDto;
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

    public MedicineResponseDto getMedicine(String name) {
        Medicine medicine = medicineRepository.findByName(name).orElseThrow(() -> new CustomException(ErrorCode.MEDICINE_NOT_FOUND));
        List<Efficacy> efficacyList = efficacyRepository.findByMedicine(medicine);
        List<Information> informationList = informationRepository.findByMedicine(medicine);

        return MedicineResponseDto.of(medicine, efficacyList, informationList);
    }
}
