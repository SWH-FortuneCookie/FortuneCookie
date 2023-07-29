package com.swh.medicine.domain.users.service;

import com.swh.medicine.domain.users.domain.Users;
import com.swh.medicine.domain.users.domain.UsersRepository;
import com.swh.medicine.domain.users.dto.request.DeviceRequestDto;
import com.swh.medicine.domain.users.dto.request.SmsRequestDto;
import com.swh.medicine.global.exception.CustomException;
import com.swh.medicine.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;

    @Transactional
    public void createDevice(DeviceRequestDto deviceRequestDto) {
        Optional<Users> users = usersRepository.findByDevice(deviceRequestDto.getDevice());
        if(users.isPresent()) {
            return;
        }
        usersRepository.save(Users.of(deviceRequestDto.getDevice(), deviceRequestDto.getFcmToken()));
    }

    @Transactional
    public String sendSms(String deviceId, SmsRequestDto smsRequestDto) {
        Users users = usersRepository.findByDevice(deviceId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 유저의 보호자 연락처가 기존에 등록된 연락처와 일치하지 않으면 보호자 연락처 새로 등록 (보통 유저의 이름은 안바뀌니까...)
        if (users.getGuardianPhone() != smsRequestDto.getPhone()) {
            users.addNameAndGuardian(smsRequestDto.getName(), smsRequestDto.getPhone());
            usersRepository.save(users);
        }

        // 문자 보내기



        return "문자 보내기 성공";
    }
}
