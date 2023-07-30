package com.swh.medicine.domain.users.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.swh.medicine.domain.users.dto.request.DeviceRequestDto;
import com.swh.medicine.domain.users.dto.request.InformationRequestDto;
import com.swh.medicine.domain.users.dto.response.InformationResponseDto;
import com.swh.medicine.domain.users.service.UsersService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

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
    public ResponseEntity<String> sendSms (
            @PathVariable String deviceId,
            @RequestBody InformationRequestDto SmsRequestDto) throws JsonProcessingException, RestClientException, URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
        String result = usersService.sendSms(deviceId, SmsRequestDto);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/{deviceId}/sms")
    public ResponseEntity<InformationResponseDto> getSms(
            @PathVariable String deviceId) {
        InformationResponseDto result = usersService.getSms(deviceId);
        return ResponseEntity.ok().body(result);
    }
}
