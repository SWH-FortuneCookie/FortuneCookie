package com.swh.medicine.domain.users.controller;

import com.swh.medicine.domain.users.dto.request.DeviceRequestDto;
import com.swh.medicine.domain.users.dto.request.PhoneRequestDto;
import com.swh.medicine.domain.users.service.UsersService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/fortunecookie")
public class UsersController {

    private final UsersService usersService;

    @PostMapping("/device")
    public ResponseEntity<String> createDevice(@RequestBody DeviceRequestDto deviceRequestDto) {
        usersService.createDevice(deviceRequestDto);
        return ResponseEntity.ok().body("디바이스 등록 성공");
    }

//    @PostMapping("{deviceId}/phone")
//    public ResponseEntity<String> addGuardianPhone(
//            @PathVariable String deviceId,
//            @RequestBody PhoneRequestDto phoneRequestDto) {
//        String result = usersService.addGuardianPhone(deviceId, phoneRequestDto);
//        return ResponseEntity.ok().body(result);
//    }

}
