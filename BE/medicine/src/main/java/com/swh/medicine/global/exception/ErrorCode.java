package com.swh.medicine.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    DUPLICATE_RESOURCE(CONFLICT, "데이터가 이미 존재합니다"),
    DUPLICATE_CONTACT(CONFLICT, "연락처가 이미 존재합니다"),
    MEDICINE_NOT_FOUND(NOT_FOUND, "약을 찾을 수 없습니다"),
    DEVICE_NOT_FOUND(NOT_FOUND, "디바이스를 찾을 수 없습니다");


    private final HttpStatus httpStatus;
    private final String message;
}
