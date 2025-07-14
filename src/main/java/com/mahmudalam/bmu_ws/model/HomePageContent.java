package com.mahmudalam.bmu_ws.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "home_page_contents")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomePageContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Section section;

    @Column(nullable = false)
    private String title;

    private String subtitle;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Builder.Default
    private String buttonText = "Learn More";

    private String buttonUrl;

    @Column(nullable = false)
    private Integer displayOrder;

    @Builder.Default
    @Column(nullable = false)
    private Boolean isActive = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public enum Section {
        HERO,
        VC_MESSAGE,
        STATS
    }
}
