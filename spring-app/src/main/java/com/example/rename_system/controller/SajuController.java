package com.example.rename_system.controller;

import com.example.rename_system.dto.NameRequest;
import com.example.rename_system.dto.WuxingResult;
import com.example.rename_system.entity.NameEntity;
import com.example.rename_system.service.SajuService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("api/saju")
public class SajuController {
    private final SajuService sajuService;

    public SajuController(SajuService sajuService) {
        this.sajuService = sajuService;
    }

    @PostMapping("/filter")
    public NameEntity testFilter(@RequestBody NameRequest nameRequest) {
        WuxingResult analyzed = sajuService.analyze(nameRequest.getBirthDateTime());

        List<NameEntity> filteredByGenderAndGeneration = sajuService.filterByGenderAndGeneration(nameRequest.getGender(), analyzed.getYear());
        List<NameEntity> filteredByWuxing = sajuService.filterByWuxing(filteredByGenderAndGeneration, analyzed.getWeakness());
        Random random = new Random();
        NameEntity randomName = filteredByWuxing.get(random.nextInt(filteredByWuxing.size()));

        return randomName;
    }
}
