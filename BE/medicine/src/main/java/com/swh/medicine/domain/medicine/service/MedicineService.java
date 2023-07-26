package com.swh.medicine.domain.medicine.service;

import com.swh.medicine.domain.medicine.domain.entity.*;
import com.swh.medicine.domain.medicine.domain.repository.*;
import com.swh.medicine.domain.medicine.dto.request.AlarmRequestDto;
import com.swh.medicine.domain.medicine.dto.request.TakingRequestDto;
import com.swh.medicine.domain.medicine.dto.response.AlarmResponseDto;
import com.swh.medicine.domain.medicine.dto.response.CautionResponseDto;
import com.swh.medicine.domain.medicine.dto.response.MedicineResponseDto;
import com.swh.medicine.domain.medicine.dto.response.TakingListResponseDto;
import com.swh.medicine.domain.users.domain.Users;
import com.swh.medicine.domain.users.domain.UsersRepository;
import com.swh.medicine.global.exception.CustomException;
import com.swh.medicine.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicineService {

    private final MedicineRepository medicineRepository;
    private final EfficacyRepository efficacyRepository;
    private final InformationRepository informationRepository;
    private final UsersRepository usersRepository;
    private final TakingMedicineRepository takingMedicineRepository;
    private final DetailTimeRepository detailTimeRepository;

    @Transactional(readOnly = true)
    public MedicineResponseDto getMedicine(String name) {
        Medicine medicine = medicineRepository.findByName(name).orElseThrow(() -> new CustomException(ErrorCode.MEDICINE_NOT_FOUND));
        List<Efficacy> efficacyList = efficacyRepository.findByMedicine(medicine);
        List<Information> informationList = informationRepository.findByMedicine(medicine);

        return MedicineResponseDto.of(medicine, efficacyList, informationList);
    }

    @Transactional(readOnly = true)
    public CautionResponseDto getCaution(String name) {
        Medicine medicine = medicineRepository.findByName(name).orElseThrow(() -> new CustomException(ErrorCode.MEDICINE_NOT_FOUND));
        return CautionResponseDto.of(medicine);
    }

    @Transactional
    public String takingMedicine(String deviceId, String name) {
        Users users = usersRepository.findByDevice(deviceId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Medicine medicine = medicineRepository.findByName(name).orElseThrow(() -> new CustomException(ErrorCode.MEDICINE_NOT_FOUND));
        TakingMedicine takingMedicine = takingMedicineRepository.findByUsersAndMedicine(users, medicine);
        if (takingMedicine != null) {
            throw new CustomException(ErrorCode.DUPLICATE_TAKING);
        }
        takingMedicineRepository.save(TakingMedicine.of(users, medicine)); // 사용자 복약 생성

        return name + "을 내 복약에 추가완료하였습니다.";
    }

    @Transactional(readOnly = true)
    public List<TakingListResponseDto> getTakingList(String deviceId) {
        Users users = usersRepository.findByDevice(deviceId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        List<TakingListResponseDto> takingListResponseDtos = new ArrayList<>();
        List<TakingMedicine> isTakingMedicineList = takingMedicineRepository.findByUsers(users);
        String[] weekdays = {"일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"};
        // 요일이 담긴 배열 생성

        for (TakingMedicine tm : isTakingMedicineList) {
            List<DetailTime> detailTimeList = detailTimeRepository.findByTakingMedicine(tm);
            Medicine medicine = tm.getMedicine();
            // 알람을 설정하지 않은 약의 경우
            if (detailTimeList.isEmpty()) {
                System.out.println(medicine.getCount());
                takingListResponseDtos.add(TakingListResponseDto.builder().subName(medicine.getSubName())
                        .isAlarm(false)
                        .amount(medicine.getCount())
                        .message(null)
                        .shapeUrl(medicine.getShapeUrl())
                        .build());
            } else {
                String message = "";
                int size = detailTimeList.size();
                if (size == 7) {
                    message += "매일 ";
                }else{
                    for(DetailTime dt : detailTimeList) {
                        message += weekdays[dt.getDay()] + " ";
                    }
                }
                DetailTime detailTime = detailTimeList.get(0);
                message += detailTime.getHour() + ":" + detailTime.getMinute() + "에 " + medicine.getCount() + "투여";

                takingListResponseDtos.add(TakingListResponseDto.builder().subName(medicine.getSubName())
                        .isAlarm(true)
                        .shapeUrl(medicine.getShapeUrl())
                        .amount(medicine.getCount())
                        .message(message)
                        .build());
            }
        }
        return takingListResponseDtos;
    }

    @Transactional
    public String alarm(String deviceId, AlarmRequestDto alarmRequestDto) {
        Users users = usersRepository.findByDevice(deviceId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        // 해당 유저의 해당 의약품에 대한 복약 id 가져옴, 이 복약 id로 상세시간 세팅
        Medicine medicine = medicineRepository.findByName(alarmRequestDto.getName()).orElseThrow(() -> new CustomException(ErrorCode.MEDICINE_NOT_FOUND));
        TakingMedicine takingMedicine = takingMedicineRepository.findByUsersAndMedicine(users, medicine);
        int[] days = alarmRequestDto.getDays();
        for(int day : days) {
            detailTimeRepository.save(DetailTime.of(takingMedicine, day, alarmRequestDto.getHour(), alarmRequestDto.getMinute()));
        }
        return "알람 설정 완료";
    }

    @Transactional
    public String deleteTakingMedicine(String deviceId, TakingRequestDto takingRequestDto) {
        Users users = usersRepository.findByDevice(deviceId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Medicine medicine = medicineRepository.findByName(takingRequestDto.getName()).orElseThrow(() -> new CustomException(ErrorCode.MEDICINE_NOT_FOUND));
        TakingMedicine takingMedicine = takingMedicineRepository.findByUsersAndMedicine(users, medicine);
        if (takingMedicine == null) {
            throw new CustomException(ErrorCode.TAKING_MEDICINE_NOT_FOUND);
        }
        takingMedicineRepository.delete(takingMedicine);
        return takingRequestDto.getName() + "을 내 복약에서 삭제완료하였습니다.";
    }

    @Transactional
    public String updateTakingMedicine(String deviceId, AlarmRequestDto alarmRequestDto) {
        Users users = usersRepository.findByDevice(deviceId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Medicine medicine = medicineRepository.findByName(alarmRequestDto.getName()).orElseThrow(() -> new CustomException(ErrorCode.MEDICINE_NOT_FOUND));
        TakingMedicine takingMedicine = takingMedicineRepository.findByUsersAndMedicine(users, medicine);

        // 만약 해당 복약이 없다면 에러
        if(takingMedicine == null){
            throw new CustomException(ErrorCode.TAKING_MEDICINE_NOT_FOUND);
        }
        // 해당 복약의 상세 시간을 모두 삭제
        detailTimeRepository.deleteByTakingMedicine(takingMedicine);
        // 새로운 상세 시간을 저장
        int[] days = alarmRequestDto.getDays();
        for(int day : days) {
            detailTimeRepository.save(DetailTime.of(takingMedicine, day, alarmRequestDto.getHour(), alarmRequestDto.getMinute()));
        }
        return "알람 수정 완료";

    }

    public AlarmResponseDto getAlarm(String deviceId, TakingRequestDto takingRequestDto) {
        Users users = usersRepository.findByDevice(deviceId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Medicine medicine = medicineRepository.findByName(takingRequestDto.getName()).orElseThrow(() -> new CustomException(ErrorCode.MEDICINE_NOT_FOUND));
        TakingMedicine takingMedicine = takingMedicineRepository.findByUsersAndMedicine(users, medicine);
        if (takingMedicine == null) {
            throw new CustomException(ErrorCode.TAKING_MEDICINE_NOT_FOUND);
        }
        List<DetailTime> detailTime = detailTimeRepository.findByTakingMedicine(takingMedicine);
        List<Integer> days = detailTimeRepository.findDays(takingMedicine);

        // 시간하고 분은 똑같으니까 젤 처음 리스트에 있는 시간하고 분 추출
        return AlarmResponseDto.of(medicine.getSubName(), days, detailTime.get(0).getHour(), detailTime.get(0).getMinute());
    }
}
