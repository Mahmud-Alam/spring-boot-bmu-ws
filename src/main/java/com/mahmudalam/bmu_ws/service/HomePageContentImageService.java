package com.mahmudalam.bmu_ws.service;

import com.mahmudalam.bmu_ws.dto.request.HomePageContentImageCreateRequest;
import com.mahmudalam.bmu_ws.dto.request.HomePageContentImageUpdateRequest;
import com.mahmudalam.bmu_ws.dto.response.UserResponse;
import com.mahmudalam.bmu_ws.model.HomePageContent;
import com.mahmudalam.bmu_ws.model.HomePageContentImage;
import com.mahmudalam.bmu_ws.model.Image;
import com.mahmudalam.bmu_ws.repository.HomePageContentImageRepository;
import com.mahmudalam.bmu_ws.repository.HomePageContentRepository;
import com.mahmudalam.bmu_ws.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class HomePageContentImageService {

    @Autowired
    private HomePageContentImageRepository contentImageRepository;

    @Autowired
    private HomePageContentRepository contentRepository;

    @Autowired
    private ImageRepository imageRepository;


    public UserResponse<Page<HomePageContentImage>> getAllHomeContentImage(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<HomePageContentImage> contents = contentImageRepository.findAll(pageable);

            return new UserResponse<>(true, contents, null);
        } catch (Exception e) {
            return new UserResponse<>(false, null, "Failed to fetch images: " + e.getMessage());
        }
    }

    public UserResponse<HomePageContentImage> getHomeContentImageById(Long id) {
        try {
            return contentImageRepository.findById(id)
                    .map(img -> new UserResponse<>(true, img, null))
                    .orElse(new UserResponse<>(false, null, "Image link not found."));
        } catch (Exception e) {
            return new UserResponse<>(false, null, "Failed to get image: " + e.getMessage());
        }
    }

    public UserResponse<HomePageContentImage> createHomeContentImage(HomePageContentImageCreateRequest request) {
        try {
            HomePageContent content = contentRepository.findById(request.getHomePageContentId())
                    .orElseThrow(() -> new RuntimeException("Home page content not found."));

            Image image = imageRepository.findById(request.getImageId())
                    .orElseThrow(() -> new RuntimeException("Image not found."));

            HomePageContentImage newImageLink = HomePageContentImage.builder()
                    .homePageContent(content)
                    .image(image)
                    .displayOrder(request.getDisplayOrder())
                    .build();

            HomePageContentImage saved = contentImageRepository.save(newImageLink);

            return new UserResponse<>(true, saved, null);

        } catch (Exception e) {
            return new UserResponse<>(false, null, "Failed to create image link: " + e.getMessage());
        }
    }

    public UserResponse<HomePageContentImage> updateHomeContentImage(Long id, HomePageContentImageUpdateRequest request) {
        try {
            return contentImageRepository.findById(id)
                    .map(existing -> {

                        if (request.getHomePageContentId() != null) {
                            HomePageContent content = contentRepository.findById(request.getHomePageContentId())
                                    .orElseThrow(() -> new RuntimeException("Home page content not found."));
                            existing.setHomePageContent(content);
                        }

                        if (request.getImageId() != null) {
                            Image image = imageRepository.findById(request.getImageId())
                                    .orElseThrow(() -> new RuntimeException("Image not found."));
                            existing.setImage(image);
                        }

                        if (request.getDisplayOrder() != null) {
                            existing.setDisplayOrder(request.getDisplayOrder());
                        }

                        if (request.getIsActive() != null) {
                            existing.setIsActive(request.getIsActive());
                        }

                        HomePageContentImage updated = contentImageRepository.save(existing);
                        return new UserResponse<>(true, updated, null);
                    })
                    .orElse(new UserResponse<>(false, null, "Image link not found."));
        } catch (Exception e) {
            return new UserResponse<>(false, null, "Failed to update image link: " + e.getMessage());
        }
    }
}