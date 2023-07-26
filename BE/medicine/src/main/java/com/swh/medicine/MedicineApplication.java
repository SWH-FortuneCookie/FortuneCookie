package com.swh.medicine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MedicineApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedicineApplication.class, args);
	}

}
