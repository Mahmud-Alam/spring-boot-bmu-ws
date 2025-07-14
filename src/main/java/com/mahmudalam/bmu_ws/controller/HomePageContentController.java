package com.mahmudalam.bmu_ws.controller;

import com.mahmudalam.bmu_ws.dto.response.UserResponse;
import com.mahmudalam.bmu_ws.model.HomePageContent;
import com.mahmudalam.bmu_ws.service.HomePageContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bmu/api/v1/homepage-contents")
public class HomePageContentController {

    @Autowired
    private HomePageContentService homePageContentService;

    @GetMapping
    public ResponseEntity<UserResponse<Page<HomePageContent>>> getAllHomeContent(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        UserResponse<Page<HomePageContent>> response = homePageContentService.getAllHomeContent(page, size);
        return response.isSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse<HomePageContent>> getHomeContentById(@PathVariable Long id) {
        UserResponse<HomePageContent> response = homePageContentService.getHomeContentById(id);
        return response.isSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @PostMapping
    public ResponseEntity<UserResponse<HomePageContent>> createHomeContent(@RequestBody HomePageContent request) {
        UserResponse<HomePageContent> response = homePageContentService.createHomeContent(request);
        return response.isSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse<HomePageContent>> updateHomeContent(@PathVariable Long id, @RequestBody HomePageContent request) {
        UserResponse<HomePageContent> response = homePageContentService.updateHomeContent(id, request);
        return response.isSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
