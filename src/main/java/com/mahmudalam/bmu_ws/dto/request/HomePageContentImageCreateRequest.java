package com.mahmudalam.bmu_ws.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class HomePageContentImageCreateRequest {
    @NotNull(message = "HomePageContent ID is required")
    private Long homePageContentId;

    @NotNull(message = "Image ID is required")
    private Long imageId;

    @NotNull(message = "Order is required")
    private Integer displayOrder;

    private Boolean isActive;
}
