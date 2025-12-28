package com.example.rename_system.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "baby_names")
public class NameEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String hanja;   // 대표 한자
    private String pronunciation;   // 로마자 표기법
    private String generation;  // 세대
    private String gender;
    private String symbol;  // 키워드
    private String meaning; // 의미
    @Column(name = "meaning_en")
    private String meaning_en;
    private boolean extra;  // 예전부터 지금까지 잘 사용되는 이름인가
    private String element; // 각 글자별 오행(ex. 금,토)
}
