package com.example.EcoTS.DTOs.Request.Newsfeed;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class ReactRequest {
    private Long userId;
    private boolean status;
}
