package com.swh.medicine.domain.users.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swh.medicine.domain.medicine.domain.entity.Efficacy;
import com.swh.medicine.domain.medicine.domain.entity.Information;
import com.swh.medicine.domain.medicine.domain.entity.Medicine;
import com.swh.medicine.domain.medicine.domain.repository.EfficacyRepository;
import com.swh.medicine.domain.medicine.domain.repository.InformationRepository;
import com.swh.medicine.domain.medicine.domain.repository.MedicineRepository;
import com.swh.medicine.domain.users.dto.request.MessageRequestDto;
import com.swh.medicine.domain.users.dto.request.SmsRequestDto;
import com.swh.medicine.domain.users.dto.response.SmsResponseDto;
import com.swh.medicine.global.exception.CustomException;
import com.swh.medicine.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SmsService {

    private final MedicineRepository medicineRepository;
    private final EfficacyRepository efficacyRepository;
    private final InformationRepository informationRepository;

    @Value("${naver-cloud-sms.accessKey}")
    private String accessKey;

    @Value("${naver-cloud-sms.secretKey}")
    private String secretKey;

    @Value("${naver-cloud-sms.serviceId}")
    private String serviceId;

    @Value("${naver-cloud-sms.senderPhone}")
    private String phone;

    public String makeSignature(Long time) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        String space = " ";
        String newLine = "\n";
        String method = "POST";
        String url = "/sms/v2/services/"+ this.serviceId+"/messages";
        String timestamp = time.toString();
        String accessKey = this.accessKey;
        String secretKey = this.secretKey;

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
        String encodeBase64String = Base64.encodeBase64String(rawHmac);

        return encodeBase64String;
    }

    public SmsResponseDto sendSms(String name, String guardianPhone, String medicineName)  throws JsonProcessingException, RestClientException, URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
        Long time = System.currentTimeMillis();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-timestamp", time.toString());
        headers.set("x-ncp-iam-access-key", accessKey);
        headers.set("x-ncp-apigw-signature-v2", makeSignature(time));

        Medicine medicine = medicineRepository.findByName(medicineName).orElseThrow(() -> new CustomException(ErrorCode.MEDICINE_NOT_FOUND));
        List<Efficacy> efficacyList = efficacyRepository.findByMedicine(medicine);
        List<Information> informationList = informationRepository.findByMedicine(medicine);

        String efficacy = "";

        for (int i = 0; i < efficacyList.size() - 1; i++) {
            efficacy += efficacyList.get(i).getType() + ", ";
        }

        efficacy += efficacyList.get(efficacyList.size() - 1).getType();

        String ingredient = "";
        for (int i = 0; i < informationList.size() - 1; i++) {
            ingredient += informationList.get(i).getIngredient() + ", ";
        }

        ingredient += informationList.get(informationList.size() - 1).getIngredient();

        String content = "안녕하세요. 시각장애인용 복약 알림 서비스, 보약입니다." + "\n\n" +
                name + "님이 현재 복약 중이신 의약품 정보를 공유드립니다." + "\n\n" +
                "제품명 : " + medicineName + "\n" +
                "생김새 : " + medicine.getDescription() + "\n" +
                "성분정보 : " + ingredient+ "\n" +
                "효능효과 : " + efficacy + "\n" +
                "용법용량 : " + medicine.getDosage() + "\n" +
                "저장방법  : " + medicine.getStorage() + "\n\n" +
                "저희 보약은 " + name + "님의 건강한 생활을 위해 적극적인 복약 관리에 힘쓰겠습니다.";



        List<MessageRequestDto> messages = new ArrayList<>();
        messages.add(MessageRequestDto.builder()
                .to(guardianPhone)
                .content(content)
                .build());


        SmsRequestDto request = SmsRequestDto.builder()
                .type("LMS")
                .contentType("COMM")
                .countryCode("82")
                .from(phone)
                .content(content)
                .messages(messages)
                .build();


        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(request);
        HttpEntity<String> httpBody = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        SmsResponseDto response = restTemplate.postForObject(new URI("https://sens.apigw.ntruss.com/sms/v2/services/"+ serviceId +"/messages"), httpBody, SmsResponseDto.class);
        return response;
    }
}
