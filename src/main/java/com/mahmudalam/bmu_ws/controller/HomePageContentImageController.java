package com.mahmudalam.bmu_ws.controller;

import com.mahmudalam.bmu_ws.dto.request.HomePageContentImageCreateRequest;
import com.mahmudalam.bmu_ws.dto.request.HomePageContentImageUpdateRequest;
import com.mahmudalam.bmu_ws.dto.response.UserResponse;
import com.mahmudalam.bmu_ws.model.HomePageContentImage;
import com.mahmudalam.bmu_ws.service.HomePageContentImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bmu/api/v1/home_page_content_images")
public class HomePageContentImageController {

    @Autowired
    private HomePageContentImageService homePageContentImageService;

    @GetMapping
    public ResponseEntity<UserResponse<Page<HomePageContentImage>>> getAllHomeContentImage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        UserResponse<Page<HomePageContentImage>> response = homePageContentImageService.getAllHomeContentImage(page, size);
        return response.isSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse<HomePageContentImage>> getHomeContentImageById(@PathVariable Long id) {
        UserResponse<HomePageContentImage> response = homePageContentImageService.getHomeContentImageById(id);
        return response.isSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @PostMapping
    public ResponseEntity<UserResponse<HomePageContentImage>> createHomeContentImage(@RequestBody HomePageContentImageCreateRequest request) {
        UserResponse<HomePageContentImage> response = homePageContentImageService.createHomeContentImage(request);
        return response.isSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse<HomePageContentImage>> updateHomeContentImage(@PathVariable Long id, @RequestBody HomePageContentImageUpdateRequest request) {
        UserResponse<HomePageContentImage> response = homePageContentImageService.updateHomeContentImage(id, request);
        return response.isSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

}
