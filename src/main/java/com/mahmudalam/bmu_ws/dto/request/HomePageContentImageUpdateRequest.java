package com.mahmudalam.bmu_ws.dto.request;

import lombok.Data;

@Data
public class HomePageContentImageUpdateRequest {
    private Long homePageContentId;
    private Long imageId;
    private Integer displayOrder;
    private Boolean isActive;
}
