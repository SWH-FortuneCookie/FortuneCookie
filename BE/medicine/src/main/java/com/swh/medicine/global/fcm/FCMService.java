package com.swh.medicine.global.fcm;

import com.google.firebase.messaging.*;
import com.swh.medicine.domain.medicine.domain.entity.DetailTime;
import com.swh.medicine.domain.medicine.domain.entity.Medicine;
import com.swh.medicine.domain.medicine.domain.entity.TakingMedicine;
import com.swh.medicine.domain.medicine.domain.repository.MedicineRepository;
import com.swh.medicine.domain.medicine.domain.repository.TakingMedicineRepository;
import com.swh.medicine.domain.users.domain.Users;
import com.swh.medicine.domain.users.domain.UsersRepository;
import com.swh.medicine.global.exception.CustomException;
import com.swh.medicine.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FCMService {

    private final String[] weekdays = {"일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"};
    private final TakingMedicineRepository takingMedicineRepository;
    private final MedicineRepository medicineRepository;
    private final UsersRepository usersRepository;


    // @Transactional 붙었었는데 일단 안붙이고 하고 싶어서 엔티티 직접 꺼냈다!
    // 해커톤 끝나고 리팩토링 필수!!!!!!
    public void getInformation(List<DetailTime> detailTimeList) {
        // fcm 푸시 알림 전송
        for (DetailTime detailTime : detailTimeList) {
            Long takingMedicineId = detailTime.getTakingMedicine().getId();
            TakingMedicine takingMedicine = takingMedicineRepository.findById(takingMedicineId).orElseThrow(()-> new CustomException(ErrorCode.TAKING_MEDICINE_NOT_FOUND));
            Long userId = takingMedicine.getUsers().getId();
            Users users = usersRepository.findById(userId).orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));
            String fcmToken = users.getFcmToken();

            Long medicineId = takingMedicine.getMedicine().getId();
            Medicine medicine = medicineRepository.findById(medicineId).orElseThrow(()-> new CustomException(ErrorCode.MEDICINE_NOT_FOUND)); // 약 추출

            String title = medicine.getName() + " " + medicine.getCount() + "을 복용할 시간이에요!";
            String body = weekdays[detailTime.getDay()] + " " + detailTime.getHour() + ":" + detailTime.getMinute() + " 알람";
            sendMessage(fcmToken, title, body);

        }
    }

    public void sendMessage(String token, String title, String body) {
        log.info("FCMService 메시지 보내기 시도");

        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        Message message = Message.builder()
                .setToken(token)
                .setNotification(notification)
                .build();

        try{
            String messageResponse = FirebaseMessaging.getInstance().send(message);
            log.info("메시지 전송 성공 : {}", messageResponse);
        }catch (Exception e) {
            throw new IllegalStateException("알림 전송에 실패하였습니다.");
        }
    }
}
