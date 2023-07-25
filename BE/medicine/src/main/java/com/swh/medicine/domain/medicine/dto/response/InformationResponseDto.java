package com.swh.medicine.domain.medicine.dto.response;

import com.swh.medicine.domain.medicine.domain.entity.Information;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InformationResponseDto {
    private String ingredient;
}
