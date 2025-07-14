package com.mahmudalam.bmu_ws.service;

import com.mahmudalam.bmu_ws.dto.response.UserResponse;
import com.mahmudalam.bmu_ws.model.Image;
import com.mahmudalam.bmu_ws.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    private ImageRepository imageRepository;

    public UserResponse<Page<Image>> getAllImages(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Image> images = imageRepository.findAll(pageable);

            return new UserResponse<>(true, images, null);
        } catch (Exception e) {
            return new UserResponse<>(false, null, "Failed to get all images: " + e.getMessage());
        }
    }

    public UserResponse<Image> getImageById(Long id) {
        try {
            return imageRepository.findById(id)
                    .map(image -> {
                        return new UserResponse<>(true, image, null);
                    })
                    .orElse(new UserResponse<>(false, null, "Image not found."));
        } catch (Exception e) {
            return new UserResponse<>(false, null, "Failed to get image: " + e.getMessage());
        }
    }

    /*
    public UserResponse<Image> createImage(Image image) {
        try {
            Image saved = imageRepository.save(image);
            return new UserResponse<>(true, saved, null);
        } catch (Exception e) {
            return new UserResponse<>(false, null, "Failed to create image: " + e.getMessage());
        }
    }
    **/

    public UserResponse<Image> patchUpdateImage(Long id, Image partialImage) {
        try {
            return imageRepository.findById(id)
                    .map(existing -> {
                        if (partialImage.getAltText() != null) existing.setAltText(partialImage.getAltText());
                        if (partialImage.getCaption() != null) existing.setCaption(partialImage.getCaption());
                        if (partialImage.getIsActive() != null) existing.setIsActive(partialImage.getIsActive());
                        Image updated = imageRepository.save(existing);
                        return new UserResponse<>(true, updated, null);
                    })
                    .orElse(new UserResponse<>(false, null, "Image not found."));
        } catch (Exception e) {
            return new UserResponse<>(false, null, "Failed to update image: " + e.getMessage());
        }
    }

    public UserResponse<Image> uploadImage(MultipartFile file, String altText, String caption) {
        try {
            if (file.isEmpty()) {
                return new UserResponse<>(false, null, "File is empty.");
            }

            // Validate extension
            String originalFilename = file.getOriginalFilename();
            String ext = originalFilename.substring(originalFilename.lastIndexOf('.') + 1).toLowerCase();
            List<String> allowedFormats = List.of("jpg", "jpeg", "png", "webp");
            if (!allowedFormats.contains(ext)) {
                return new UserResponse<>(false, null, "Unsupported image format.");
            }

            // Duplicate check by altText (if provided)
            if (altText != null) {
                Optional<Image> existing = imageRepository.findByAltTextIgnoreCase(altText);
                if (existing.isPresent()) {
                    return new UserResponse<>(false, null, "An image with this altText already exists.");
                }
            }

            // Ensure directory exists
            File dir = new File(uploadDir);
            if (!dir.exists() && !dir.mkdirs()) {
                return new UserResponse<>(false, null, "Failed to create upload directory.");
            }

            // Generate unique filename
            String uniqueFilename = UUID.randomUUID() + "." + ext;
            String filePath = uploadDir + uniqueFilename;

            // Save file physically
            File dest = new File(filePath);
            file.transferTo(dest);

            // Save image metadata to DB
            Image image = Image.builder()
                    .url("/upload/img/" + uniqueFilename)
                    .altText(altText)
                    .caption(caption)
                    .format(ext)
                    .isActive(true)
                    .build();

            Image saved = imageRepository.save(image);
            return new UserResponse<>(true, saved, null);

        } catch (Exception e) {
            return new UserResponse<>(false, null, "Failed to upload image: " + e.getMessage());
        }
    }
}
