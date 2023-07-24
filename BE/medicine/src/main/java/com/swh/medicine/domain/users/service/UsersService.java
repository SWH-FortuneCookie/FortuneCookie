package com.swh.medicine.domain.users.service;

import com.swh.medicine.domain.users.domain.Users;
import com.swh.medicine.domain.users.domain.UsersRepository;
import com.swh.medicine.domain.users.dto.request.DeviceRequestDto;
import com.swh.medicine.domain.users.dto.request.PhoneRequestDto;
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
        usersRepository.save(Users.of(deviceRequestDto.getDevice()));
    }

//    @Transactional
//    public String addGuardianPhone(String deviceId, PhoneRequestDto phoneRequestDto) {
//        Users users = usersRepository.findByDevice(deviceId).orElseThrow(() -> new CustomException(ErrorCode.DEVICE_NOT_FOUND));
//        // 유저의 보호자 연락처가 존재한다면 추가하지 않는다.
//        if (users.getGuardianPhone() == null) {
//            users.addGuardianPhone(phoneRequestDto.getPhone());
//            return "보호자 연락처 등록 성공";
//        }
//        return "보호자 연락처가 이미 존재합니다";
//    }
}
