package com.example.rename_system.repository;

import com.example.rename_system.entity.NameEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NameRepository extends JpaRepository<NameEntity, Long> {
    // 성별+세대로 찾기
    List<NameEntity> findByGenderAndGeneration(String gender, String generation);
}