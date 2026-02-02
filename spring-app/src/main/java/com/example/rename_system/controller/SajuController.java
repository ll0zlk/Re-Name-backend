package com.example.rename_system.controller;

import com.example.rename_system.dto.NameRequest;
import com.example.rename_system.dto.SajuResponse;
import com.example.rename_system.dto.WuxingResult;
import com.example.rename_system.entity.NameEntity;
import com.example.rename_system.service.SajuService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/saju")
@CrossOrigin(origins = "*")
public class SajuController {
    private final SajuService sajuService;
    private final JdbcTemplate jdbcTemplate;

    public SajuController(SajuService sajuService, JdbcTemplate jdbcTemplate) {
        this.sajuService = sajuService;
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/filter")
    public SajuResponse testFilter(@RequestBody NameRequest nameRequest) {
        WuxingResult analyzed = sajuService.analyze(nameRequest.getBirthDateTime());

        List<NameEntity> filteredByGenderAndGeneration = sajuService.filterByGenderAndGeneration(nameRequest.getGender(), analyzed.getYear());
        List<NameEntity> filteredByWuxing = sajuService.filterByWuxing(filteredByGenderAndGeneration, analyzed.getWeakness(), analyzed.getStrength());

        if (filteredByWuxing == null || filteredByWuxing.isEmpty()) {
            if (!filteredByGenderAndGeneration.isEmpty()) {
                NameEntity fallbackName = filteredByGenderAndGeneration.get(new Random().nextInt(filteredByGenderAndGeneration.size()));
                return new SajuResponse(fallbackName, analyzed.getFiveElements());
            }
            return null;
        }

        Random random = new Random();
        NameEntity selectedName = filteredByWuxing.get(random.nextInt(filteredByWuxing.size()));
        return new SajuResponse(selectedName, analyzed.getFiveElements());
    }

    // 이 부분이 수정되었습니다.
    @GetMapping("/health")
    public String healthCheck() {
        jdbcTemplate.execute("SELECT 1");
        return "OK";
    }
}