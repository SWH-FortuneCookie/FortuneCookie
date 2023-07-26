package com.swh.medicine.domain.medicine.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InitialTakingResponseDto {
    private String name;
    private String shapeUrl;

    @Builder
    public InitialTakingResponseDto(String name, String shapeUrl) {
        this.name = name;
        this.shapeUrl = shapeUrl;
    }

    public static InitialTakingResponseDto of(String name, String shapeUrl) {
        return InitialTakingResponseDto.builder()
                .name(name)
                .shapeUrl(shapeUrl)
                .build();
    }
}
