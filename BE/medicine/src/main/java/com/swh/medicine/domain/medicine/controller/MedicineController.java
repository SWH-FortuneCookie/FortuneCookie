package com.swh.medicine.domain.medicine.controller;

import com.swh.medicine.domain.medicine.dto.response.MedicineResponseDto;
import com.swh.medicine.domain.medicine.service.MedicineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/fortunecookie")
public class MedicineController {

    private final MedicineService medicineService;

    @GetMapping("/medicine")
    public ResponseEntity<MedicineResponseDto> getMedicine(@RequestParam String name) {
        MedicineResponseDto medicineResponseDto = medicineService.getMedicine(name);
        return ResponseEntity.ok().body(medicineResponseDto);
    }
}
