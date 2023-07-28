package com.swh.medicine.domain.medicine.service;

import com.swh.medicine.domain.medicine.domain.entity.DetailTime;
import com.swh.medicine.domain.medicine.domain.entity.Medicine;
import com.swh.medicine.domain.medicine.domain.entity.TakingMedicine;
import com.swh.medicine.domain.medicine.domain.repository.DetailTimeRepository;
import com.swh.medicine.domain.users.domain.Users;
import com.swh.medicine.global.fcm.FCMService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduleTasks {

    private final DetailTimeRepository detailTimeRepository;
    private final FCMService fcmService;

    @Scheduled(cron = "0 * * * * *") // 매 분마다 실행
    public void sendScheduledAlarm() {

        log.info("scheduleTasks sendScheduledAlarm");
        LocalDateTime currentTime = LocalDateTime.now();
        int currentHour = currentTime.getHour();
        int currentMinute = currentTime.getMinute();
        int currentDay = currentTime.getDayOfWeek().getValue();

        List<DetailTime> detailTimeList
                = detailTimeRepository.findByHourAndMinuteAndDay(currentHour, currentMinute, currentDay);
        fcmService.getInformation(detailTimeList);
    }
}
