package com.example.EcoTS.Component;

import com.example.EcoTS.Enum.AchievementType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToAchievementTypeConverter implements Converter<String, AchievementType> {
    @Override
    public AchievementType convert(String source) {
        try {
            return AchievementType.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid AchievementType: " + source);
        }
    }
}
