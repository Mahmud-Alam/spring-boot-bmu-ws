package com.mahmudalam.bmu_ws.dto.request;

import com.mahmudalam.bmu_ws.model.Collaboration;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CollaborationCreateRequest {
    @NotBlank(message = "is required")
    private String title;

    @Positive(message = "must be positive")
    private Long logoId;

    private String description;
    private Collaboration.CollaborationType type;
}
