package com.example.rename_system.dto;

import lombok.Getter;

import java.util.List;

public class NameRequest {
    @Getter
    private String birthDateTime;
    private String gender;
    private List<String> keywords;

}