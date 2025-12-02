package com.example.rename_system.controller;

import com.example.rename_system.entity.NameEntity;
import com.example.rename_system.service.SajuService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {

    private final SajuService sajuService;

    public TestController(SajuService sajuService) {
        this.sajuService = sajuService;
    }

    @GetMapping("/names")
    public List<NameEntity> testFilter(
            @RequestParam String gender,
            @RequestParam int birthYear
    ) {
        return sajuService.filterByGenderAndGeneration(gender, birthYear);
    }
}
