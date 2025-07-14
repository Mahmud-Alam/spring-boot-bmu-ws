package com.mahmudalam.bmu_ws.service;

import com.mahmudalam.bmu_ws.dto.response.UserResponse;
import com.mahmudalam.bmu_ws.model.HomePageContent;
import com.mahmudalam.bmu_ws.repository.HomePageContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class HomePageContentService {

    @Autowired
    private HomePageContentRepository homePageContentRepository;

    public UserResponse<Page<HomePageContent>> getAllHomeContent(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<HomePageContent> contents = homePageContentRepository.findAll(pageable);

            return new UserResponse<>(true, contents, null);
        } catch (Exception e) {
            return new UserResponse<>(false, null, "Failed to fetch contents: " + e.getMessage());
        }
    }

    public UserResponse<HomePageContent> getHomeContentById(Long id) {
        try {
            return homePageContentRepository.findById(id)
                    .map(content -> new UserResponse<>(true, content, null))
                    .orElse(new UserResponse<>(false, null, "Content not found."));
        } catch (Exception e) {
            return new UserResponse<>(false, null, "Failed to get content: " + e.getMessage());
        }
    }

    public UserResponse<HomePageContent> createHomeContent(HomePageContent request) {
        try {
            HomePageContent content = homePageContentRepository.save(request);
            return new UserResponse<>(true, content, null);
        } catch (Exception e) {
            return new UserResponse<>(false, null, "Failed to get content: " + e.getMessage());
        }
    }

    public UserResponse<HomePageContent> updateHomeContent(Long id, HomePageContent request) {
        try {
            return homePageContentRepository.findById(id)
                    .map(existing -> {
                        if (request.getSection() != null) existing.setSection(request.getSection());
                        if (request.getTitle() != null) existing.setTitle(request.getTitle());
                        if (request.getSubtitle() != null) existing.setSubtitle(request.getSubtitle());
                        if (request.getContent() != null) existing.setContent(request.getContent());
                        if (request.getButtonText() != null) existing.setButtonText(request.getButtonText());
                        if (request.getButtonUrl() != null) existing.setButtonUrl(request.getButtonUrl());
                        if (request.getDisplayOrder() != null) existing.setDisplayOrder(request.getDisplayOrder());
                        if (request.getIsActive() != null) existing.setIsActive(request.getIsActive());

                        homePageContentRepository.save(existing);
                        return new UserResponse<>(true, existing, null);
                    })
                    .orElse(new UserResponse<>(false, null, "Content not found."));
        } catch (Exception e) {
            return new UserResponse<>(false, null, "Failed to get content: " + e.getMessage());
        }
    }
}
