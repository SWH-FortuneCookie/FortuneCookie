package com.swh.medicine.domain.users.controller;

import com.swh.medicine.domain.users.dto.request.DeviceRequestDto;
import com.swh.medicine.domain.users.dto.request.SmsRequestDto;
import com.swh.medicine.domain.users.service.UsersService;
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

    @PostMapping("/{deviceId}/sms")
    public ResponseEntity<String> sendSms(
            @PathVariable String deviceId,
            @RequestBody SmsRequestDto SmsRequestDto) {
        String result = usersService.sendSms(deviceId, SmsRequestDto);
        return ResponseEntity.ok().body(result);
    }

}
