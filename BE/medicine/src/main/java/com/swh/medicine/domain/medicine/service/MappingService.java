package com.swh.medicine.domain.medicine.service;

import com.swh.medicine.domain.medicine.domain.entity.Mapping;
import com.swh.medicine.domain.medicine.domain.entity.Medicine;
import com.swh.medicine.domain.medicine.domain.entity.TakingMedicine;
import com.swh.medicine.domain.medicine.domain.repository.MappingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MappingService {

    private final MappingRepository mappingRepository;

    public void save(TakingMedicine takingMedicine, Medicine medicine) {
        Mapping mapping = new Mapping(takingMedicine, medicine);
        mappingRepository.save(mapping);
    }
}
