package com.example.rename_system.dto;
import com.nlf.calendar.EightChar;

import java.util.Map;

public class WuxingResult {
    private EightChar eightChar;
    private Map<String, Integer> fiveElements;
    private String strength;
    private String weakness;

    public WuxingResult(EightChar eightChar, Map<String, Integer> fiveElements, String strength, String weakness) {
        this.eightChar = eightChar;
        this.fiveElements = fiveElements;
        this.strength = strength;
        this.weakness = weakness;
    }
}
