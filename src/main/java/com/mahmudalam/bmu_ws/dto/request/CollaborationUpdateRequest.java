package com.mahmudalam.bmu_ws.dto.request;

import lombok.Data;

@Data
public class CollaborationUpdateRequest {
    private String title;
    private String description;
    private Long logoId;
    private Boolean isActive;
}