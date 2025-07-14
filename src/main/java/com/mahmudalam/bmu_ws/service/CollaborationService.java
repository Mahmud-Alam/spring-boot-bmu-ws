package com.mahmudalam.bmu_ws.service;

import com.mahmudalam.bmu_ws.dto.request.CollaborationCreateRequest;
import com.mahmudalam.bmu_ws.dto.request.CollaborationUpdateRequest;
import com.mahmudalam.bmu_ws.dto.response.UserResponse;
import com.mahmudalam.bmu_ws.model.Collaboration;
import com.mahmudalam.bmu_ws.model.Image;
import com.mahmudalam.bmu_ws.repository.CollaborationRepository;
import com.mahmudalam.bmu_ws.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CollaborationService {

    @Autowired
    private CollaborationRepository collaborationRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Value("${app.base-url}")
    private String baseUrl;

    public UserResponse<Page<Collaboration>> getAllCollaborations(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Collaboration> list = collaborationRepository.findAll(pageable);
            return new UserResponse<>(true, list, null);
        } catch (Exception e) {
            return new UserResponse<>(false, null, "Failed to fetch collaborations: " + e.getMessage());
        }
    }

    public UserResponse<Collaboration> getCollaborationById(Long id) {
        try {
            return collaborationRepository.findById(id)
                    .map(col -> new UserResponse<>(true, col, null))
                    .orElse(new UserResponse<>(false, null, "Collaboration not found."));
        } catch (Exception e) {
            return new UserResponse<>(false, null, "Failed to get collaboration: " + e.getMessage());
        }
    }

    public UserResponse<Collaboration> createCollaboration(CollaborationCreateRequest request) {
        try {
            Collaboration collaboration = Collaboration.builder()
                    .title(request.getTitle().toUpperCase())
                    .description(request.getDescription())
                    .type(request.getType())
                    .build();

            // If logo image provided
            if (request.getLogoId() != null) {
                Image image = imageRepository.findById(request.getLogoId())
                        .orElseThrow(() -> new RuntimeException("Image not found."));
                collaboration.setLogo(image);
            }

            // Duplicate check after setting title
            Optional<Collaboration> existing = collaborationRepository.findByTitle(collaboration.getTitle());
            if (existing.isPresent()) {
                return new UserResponse<>(false, null, "A collaboration with this title already exists.");
            }

            // If logo present, build URL
            if (collaboration.getLogo() != null) {
                collaboration.setUrl(baseUrl + collaboration.getLogo().getUrl());
            }

            Collaboration saved = collaborationRepository.save(collaboration);
            return new UserResponse<>(true, saved, null);

        } catch (Exception e) {
            return new UserResponse<>(false, null, "Failed to create collaboration: " + e.getMessage());
        }
    }


    public UserResponse<Collaboration> updateCollaborationPartial(Long id, CollaborationUpdateRequest updateRequest) {
        try {
            return collaborationRepository.findById(id).map(existing -> {

                // Title update & validation
                if (updateRequest.getTitle() != null) {
                    String newTitle = updateRequest.getTitle().trim();
                    if (newTitle.isEmpty()) {
                        return new UserResponse<Collaboration>(false, null, "Title cannot be empty.");
                    }

                    Optional<Collaboration> duplicate = collaborationRepository.findByTitle(newTitle.toUpperCase());
                    if (duplicate.isPresent() && !duplicate.get().getId().equals(id)) {
                        return new UserResponse<Collaboration>(false, null, "Another collaboration with this title already exists.");
                    }

                    existing.setTitle(newTitle.toUpperCase());
                }

                // Description update
                if (updateRequest.getDescription() != null) {
                    existing.setDescription(updateRequest.getDescription());
                }

                // isActive update
                if (updateRequest.getIsActive() != null) {
                    existing.setIsActive(updateRequest.getIsActive());
                }

                // Logo update
                if (updateRequest.getLogoId() != null) {
                    Image newLogo = imageRepository.findById(updateRequest.getLogoId())
                            .orElseThrow(() -> new RuntimeException("Image not found."));
                    existing.setLogo(newLogo);
                    existing.setUrl(baseUrl + newLogo.getUrl());
                }

                Collaboration updated = collaborationRepository.save(existing);
                return new UserResponse<>(true, updated, null);

            }).orElse(new UserResponse<>(false, null, "Collaboration not found."));
        } catch (Exception e) {
            return new UserResponse<>(false, null, "Failed to update collaboration: " + e.getMessage());
        }
    }

}