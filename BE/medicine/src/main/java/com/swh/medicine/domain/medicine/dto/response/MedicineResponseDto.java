package com.swh.medicine.domain.medicine.dto.response;

import com.swh.medicine.domain.medicine.domain.entity.Efficacy;
import com.swh.medicine.domain.medicine.domain.entity.Information;
import com.swh.medicine.domain.medicine.domain.entity.Medicine;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class MedicineResponseDto {
    private String subName;
    private String shapeUrl;
    private String description;
    private String dosage;
    private String storage;
    private List<EfficacyResponseDto> efficacy;
    private List<InformationResponseDto> information;


    @Builder
    public MedicineResponseDto(String subName, String shapeUrl, String description, String dosage, String storage, List<EfficacyResponseDto> efficacy, List<InformationResponseDto> information) {
        this.subName = subName;
        this.shapeUrl = shapeUrl;
        this.description = description;
        this.dosage = dosage;
        this.storage = storage;
        this.efficacy = efficacy;
        this.information = information;
    }

    public static MedicineResponseDto of(Medicine medicine, List<Efficacy> efficacy, List<Information> information) {
        return MedicineResponseDto.builder()
                .subName(medicine.getSubName())
                .shapeUrl(medicine.getShapeUrl())
                .description(medicine.getDescription())
                .dosage(medicine.getDosage())
                .storage(medicine.getStorage())
                .efficacy(efficacy.stream()
                        .map(e -> EfficacyResponseDto.builder().type(e.getType()).build()).collect(Collectors.toList()))
                .information(information.stream()
                        .map(i -> InformationResponseDto.builder().ingredient(i.getIngredient()).build()).collect(Collectors.toList()))
                .build();
    }
}
