package com.swh.medicine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // 스케줄링을 위한 어노테이션
@EnableAsync // 비동기 처리를 위한 어노테이션
public class MedicineApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedicineApplication.class, args);
	}

}
