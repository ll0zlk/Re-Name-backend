package com.example.rename_system.dto;

import com.example.rename_system.entity.NameEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class SajuResponse {
    private NameEntity nameInfo;
    private Map<String, Integer> fiveElements;

    public SajuResponse(NameEntity nameInfo, Map<String, Integer> counts) {
        this.nameInfo = nameInfo;
        this.fiveElements = new HashMap<>();

        if (counts != null) {
            int total = counts.values().stream().mapToInt(Integer::intValue).sum();

            counts.forEach((key, value) -> {
                // (내 글자 수 / 전체 글자 수) * 100
                int percentage = (int) Math.round((value / (double) total) * 100);
                this.fiveElements.put(key, percentage);
            });
        }
    }
}
