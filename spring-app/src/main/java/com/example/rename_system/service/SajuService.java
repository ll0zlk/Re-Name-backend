package com.example.rename_system.service;

import com.example.rename_system.dto.WuxingResult;
import com.example.rename_system.entity.NameEntity;
import com.example.rename_system.repository.NameRepository;
import com.nlf.calendar.EightChar;
import com.nlf.calendar.Lunar;
import com.nlf.calendar.Solar;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class SajuService {
    private final NameRepository nameRepository;

    public SajuService(NameRepository nameRepository) {
        this.nameRepository = nameRepository;
    }

    public WuxingResult analyze(String birthDateTime) {
        boolean hasTime = birthDateTime.contains("T") || birthDateTime.length() > 10;

        // 날짜 파싱
        LocalDateTime dt;
        if (hasTime) {
            dt = LocalDateTime.parse(birthDateTime);
        } else {
            dt = java.time.LocalDate.parse(birthDateTime).atStartOfDay();
        }

        // 양력 -> 음력
        Solar solar = new Solar(dt.getYear(), dt.getMonthValue(), dt.getDayOfMonth(),
                dt.getHour(), dt.getMinute(), 0);
        Lunar lunar = solar.getLunar();
        EightChar eightChar = lunar.getEightChar();

        // 오행 매핑
        Map<String, String> ganToWuxing = Map.of(
                "甲","wood","乙","wood",
                "丙","fire","丁","fire",
                "戊","earth","己","earth",
                "庚","metal","辛","metal",
                "壬","water","癸","water"
        );

        Map<String, String> zhiToWuxing = Map.ofEntries(
                Map.entry("子", "water"),Map.entry("丑","earth"),Map.entry("寅","wood"),
                Map.entry("卯","wood"),Map.entry("辰","earth"),Map.entry("巳","fire"),
                Map.entry("午","fire"),Map.entry("未","earth"),Map.entry("申","metal"),
                Map.entry("酉","metal"),Map.entry("戌","earth"),Map.entry("亥","water")
        );

        Map<String, Integer> count = new HashMap<>();
        count.put("wood", 0);
        count.put("earth", 0);
        count.put("fire", 0);
        count.put("metal", 0);
        count.put("water", 0);

        List<String> gans = new ArrayList<>(List.of(eightChar.getYearGan(), eightChar.getMonthGan(), eightChar.getDayGan()));
        List<String> zhis = new ArrayList<>(List.of(eightChar.getYearZhi(), eightChar.getMonthZhi(), eightChar.getDayZhi()));

        if (hasTime) {
            gans.add(eightChar.getTimeGan());
            zhis.add(eightChar.getTimeZhi());
        }

        for (String g : gans) {
            String key = ganToWuxing.get(g);
            if (key != null) count.put(key, count.get(key) + 1);
        }
        for (String z : zhis) {
            String key = zhiToWuxing.get(z);
            if (key != null) count.put(key, count.get(key) + 1);
        }

        // 강/약 오행
        String strength = Collections.max(count.entrySet(), Map.Entry.comparingByValue()).getKey();
        String weakness = Collections.min(count.entrySet(), Map.Entry.comparingByValue()).getKey();

        return new WuxingResult(eightChar, count, strength, weakness, dt.getYear());
    }

    public String calculateGeneration(int year) {
        if (year < 1960) return "Baby Boomers";         // (1945)-1959
        else if (year < 1980) return "Gen-X";           // 1960-1980
        else if (year < 2000) return "Millennials";     // 1980-1999
        else if (year < 2010) return "Gen-Z";           // 2000-2009
        else return "Gen-Alpha";                        // 2010-now
    }

    Map<String, String> engToKor = Map.of(
            "wood", "목",
            "fire", "화",
            "earth", "토",
            "metal", "금",
            "water", "수"
    );

    // 1단계 필터: 성별+세대
    public List<NameEntity> filterByGenderAndGeneration(String gender, int birthYear) {
        String generation = calculateGeneration(birthYear);
        return nameRepository.findByGenderAndGeneration(gender, generation);
    }

    // 2단계 필터: 오행
    public List<NameEntity> filterByWuxing(List<NameEntity> candidates, String weakness, String strength) {
        List<NameEntity> filtered = new ArrayList<>();
        String korWeakness = engToKor.get(weakness);
        String korStrength = engToKor.get(strength);
        for (NameEntity candidate : candidates) {
            String elementStr = candidate.getElement();
            if (elementStr != null && elementStr.contains(korWeakness) && !elementStr.contains(korStrength)) {
                filtered.add(candidate);
            }
        }
        return filtered;
    }
}