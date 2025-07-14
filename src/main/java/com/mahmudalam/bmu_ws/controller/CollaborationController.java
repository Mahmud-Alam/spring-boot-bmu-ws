package com.mahmudalam.bmu_ws.controller;

import com.mahmudalam.bmu_ws.dto.request.CollaborationCreateRequest;
import com.mahmudalam.bmu_ws.dto.request.CollaborationUpdateRequest;
import com.mahmudalam.bmu_ws.dto.response.UserResponse;
import com.mahmudalam.bmu_ws.model.Collaboration;
import com.mahmudalam.bmu_ws.service.CollaborationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bmu/api/v1/collaborations")
public class CollaborationController {

    @Autowired
    private CollaborationService collaborationService;

    @GetMapping
    public ResponseEntity<UserResponse<Page<Collaboration>>> getAllCollaborations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        UserResponse<Page<Collaboration>> response = collaborationService.getAllCollaborations(page, size);
        return response.isSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse<Collaboration>> getCollaborationById(@PathVariable Long id) {
        UserResponse<Collaboration> response = collaborationService.getCollaborationById(id);
        return response.isSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<UserResponse<Collaboration>> createCollaboration(
            @Valid @RequestBody CollaborationCreateRequest request) {
        UserResponse<Collaboration> response = collaborationService.createCollaboration(request);
        return response.isSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse<Collaboration>> updateCollaborationPartial(
            @PathVariable Long id, @RequestBody CollaborationUpdateRequest collaboration) {
        UserResponse<Collaboration> response = collaborationService.updateCollaborationPartial(id, collaboration);
        return response.isSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}