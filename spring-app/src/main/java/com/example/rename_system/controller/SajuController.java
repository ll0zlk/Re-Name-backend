package com.example.rename_system.controller;

import com.example.rename_system.dto.NameRequest;
import com.example.rename_system.dto.WuxingResult;
import com.example.rename_system.service.SajuService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/saju")
public class SajuController {
    private final SajuService sajuService;

    public SajuController(SajuService sajuService) {
        this.sajuService = sajuService;
    }

    @PostMapping("/analyze")
    public WuxingResult analyze(@RequestBody NameRequest nameRequest){
        return sajuService.analyze(nameRequest.getBirthDateTime());
    }
}
