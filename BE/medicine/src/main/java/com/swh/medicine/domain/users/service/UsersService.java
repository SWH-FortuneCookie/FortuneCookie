package com.swh.medicine.domain.users.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.swh.medicine.domain.users.domain.Users;
import com.swh.medicine.domain.users.domain.UsersRepository;
import com.swh.medicine.domain.users.dto.request.DeviceRequestDto;
import com.swh.medicine.domain.users.dto.request.InformationRequestDto;
import com.swh.medicine.domain.users.dto.response.InformationResponseDto;
import com.swh.medicine.domain.users.dto.response.SmsResponseDto;
import com.swh.medicine.global.exception.CustomException;
import com.swh.medicine.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;
    private final SmsService smsService;

    @Transactional
    public void createDevice(DeviceRequestDto deviceRequestDto) {
        Optional<Users> users = usersRepository.findByDevice(deviceRequestDto.getDevice());
        if(users.isPresent()) {
            return;
        }
        usersRepository.save(Users.of(deviceRequestDto.getDevice(), deviceRequestDto.getFcmToken()));
    }

    @Transactional
    public String sendSms(String deviceId, InformationRequestDto smsRequestDto) throws JsonProcessingException, RestClientException, URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
        Users users = usersRepository.findByDevice(deviceId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 유저의 보호자 연락처가 기존에 등록된 연락처 또는 사용자 이름이 일치하지 않으면 입력받은 값으로 새로 등록
        if (users.getGuardianPhone() != smsRequestDto.getPhone() || users.getName() != smsRequestDto.getName()) {
            users.addNameAndGuardian(smsRequestDto.getName(), smsRequestDto.getPhone());
            usersRepository.save(users);
        }

        // 문자 보내기
        SmsResponseDto response = smsService.sendSms(users.getName(), users.getGuardianPhone(), smsRequestDto.getMedicineName());
        return "문자 보내기 성공";
    }

    public InformationResponseDto getSms(String deviceId) {
        Users users = usersRepository.findByDevice(deviceId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return InformationResponseDto.of(users.getName(), users.getGuardianPhone());
    }
}
