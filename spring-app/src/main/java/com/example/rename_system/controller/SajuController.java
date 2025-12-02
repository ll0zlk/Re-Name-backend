package com.example.rename_system.controller;

import com.example.rename_system.dto.NameRequest;
import com.example.rename_system.dto.WuxingResult;
import com.example.rename_system.entity.NameEntity;
import com.example.rename_system.service.SajuService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/saju")
public class SajuController {
    private final SajuService sajuService;

    public SajuController(SajuService sajuService) {
        this.sajuService = sajuService;
    }

    @GetMapping
    public List<NameEntity> testFilter(@RequestParam String gender,  @RequestParam String birthDateTime) {
        WuxingResult analyzed = sajuService.analyze(birthDateTime);

        List<NameEntity> filteredByGenderAndGeneration = sajuService.filterByGenderAndGeneration(gender, analyzed.getYear());
        List<NameEntity> filteredByWuxing = sajuService.filterByWuxing(filteredByGenderAndGeneration, analyzed.getWeakness());

        return filteredByWuxing;
    }
}
