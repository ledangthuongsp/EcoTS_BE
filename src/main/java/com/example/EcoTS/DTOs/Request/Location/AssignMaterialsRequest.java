package com.example.EcoTS.DTOs.Request.Location;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignMaterialsRequest {
    Long locationId;
    List<Long> materialIds;
}
