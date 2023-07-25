package com.swh.medicine.domain.medicine.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String shapeUrl;
    private String description;
    private String dosage;
    private String storage;
    @Column(columnDefinition = "LONGTEXT")
    private String caution;
    private String subName;

    @OneToMany(mappedBy = "medicine", cascade = CascadeType.ALL)
    private List<Efficacy> efficacyList = new ArrayList<>();

    @OneToMany(mappedBy = "medicine", cascade = CascadeType.ALL)
    private List<Information> informationList = new ArrayList<>();

    @Builder
    public Medicine(String name, String shapeUrl, String description, String dosage, String storage, String caution, String subName) {
        this.name = name;
        this.shapeUrl = shapeUrl;
        this.description = description;
        this.dosage = dosage;
        this.storage = storage;
        this.caution = caution;
        this.subName = subName;
    }
}
