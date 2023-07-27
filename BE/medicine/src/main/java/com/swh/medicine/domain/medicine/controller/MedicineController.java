package com.swh.medicine.domain.medicine.controller;

import com.swh.medicine.domain.medicine.dto.request.AlarmRequestDto;
import com.swh.medicine.domain.medicine.dto.request.TakingRequestDto;
import com.swh.medicine.domain.medicine.dto.response.AlarmResponseDto;
import com.swh.medicine.domain.medicine.dto.response.CautionResponseDto;
import com.swh.medicine.domain.medicine.dto.response.MedicineResponseDto;
import com.swh.medicine.domain.medicine.dto.response.TakingListResponseDto;
import com.swh.medicine.domain.medicine.service.MedicineService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/caution")
    public ResponseEntity<CautionResponseDto> getCaution(@RequestParam String name) {
        CautionResponseDto cautionResponseDto = medicineService.getCaution(name);
        return ResponseEntity.ok().body(cautionResponseDto);
    }

    @PostMapping("/{deviceId}/taking")
    public ResponseEntity<String> takingMedicine(@PathVariable String deviceId, @RequestBody TakingRequestDto takingRequestDto) {
        String result = medicineService.takingMedicine(deviceId, takingRequestDto.getName());
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/{deviceId}/taking")
    public ResponseEntity<List<TakingListResponseDto>> getTakingList(@PathVariable String deviceId) {
        List<TakingListResponseDto> takingListResponseDto = medicineService.getTakingList(deviceId);
        return ResponseEntity.ok().body(takingListResponseDto);
    }

    @DeleteMapping("/{deviceId}/taking")
    public ResponseEntity<String> deleteTakingMedicine(@PathVariable String deviceId, @RequestBody TakingRequestDto takingRequestDto) {
        String result = medicineService.deleteTakingMedicine(deviceId, takingRequestDto);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/{deviceId}/alarm")
    public ResponseEntity<String> alarm(@PathVariable String deviceId, @RequestBody AlarmRequestDto alarmRequestDto) {
        String result = medicineService.alarm(deviceId, alarmRequestDto);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/{deviceId}/alarm")
    public ResponseEntity<AlarmResponseDto> getAlarm(@PathVariable String deviceId, @RequestBody TakingRequestDto takingRequestDto){
        AlarmResponseDto alarmResponseDto = medicineService.getAlarm(deviceId, takingRequestDto);
        return ResponseEntity.ok().body(alarmResponseDto);
    }

    @PutMapping("/{deviceId}/alarm")
    public ResponseEntity<String> updateTakingMedicine(@PathVariable String deviceId, @RequestBody AlarmRequestDto alarmRequestDto) {
        String result = medicineService.updateTakingMedicine(deviceId, alarmRequestDto);
        return ResponseEntity.ok().body(result);
    }
}
