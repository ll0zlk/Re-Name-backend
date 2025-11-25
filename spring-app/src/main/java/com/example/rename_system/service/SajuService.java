package com.example.rename_system.service;

import com.example.rename_system.dto.WuxingResult;
import com.nlf.calendar.EightChar;
import com.nlf.calendar.Lunar;
import com.nlf.calendar.Solar;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SajuService {
    public WuxingResult analyze(String birthDateTime) {
        // 날짜 파싱
        String[] parts = birthDateTime.split(" ");
        String[] ymd = parts[0].split("-");
        String[] hm = parts[1].split(":");

        int year = Integer.parseInt(ymd[0]);
        int month = Integer.parseInt(ymd[1]);
        int day = Integer.parseInt(ymd[2]);
        int hour = Integer.parseInt(hm[0]);
        int minute = Integer.parseInt(hm[1]);

        // 양력 -> 음력
        Solar solar = new Solar(year, month, day, hour, minute, 0);
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

        List<String> gans = List.of(
                eightChar.getYearGan(), eightChar.getMonthGan(), eightChar.getDayGan(), eightChar.getTimeGan()
        );

        List<String> zhis = List.of(
                eightChar.getYearZhi(), eightChar.getMonthZhi(), eightChar.getDayZhi(), eightChar.getTimeZhi()
        );

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

        return new WuxingResult(eightChar, count, strength, weakness);
    }

}