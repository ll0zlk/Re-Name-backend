package com.example.rename_system.dto;
import com.nlf.calendar.EightChar;
import lombok.Getter;

import java.util.Map;

@Getter
public class WuxingResult {
    private EightChar eightChar;
    private Map<String, Integer> fiveElements;
    private String strength;
    private String weakness;
    private int year;

    public WuxingResult(EightChar eightChar, Map<String, Integer> fiveElements, String strength, String weakness, int year) {
        this.eightChar = eightChar;
        this.fiveElements = fiveElements;
        this.strength = strength;
        this.weakness = weakness;
        this.year = year;
    }
}
