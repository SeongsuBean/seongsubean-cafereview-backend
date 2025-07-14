package com.oopsw.seongsubean_cafereview_backend.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "cafe_review")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CafeReviewEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long reviewId;

  @Column(name = "cafe_id", nullable = false)
  private Long cafeId;

  @Column(name = "user_id")
  private Long userId;

  @Column(name = "user_image_url", length = 500)
  private String userImageUrl;

  @Column(name = "image_url", length = 500)
  private String imageUrl;

  @Column(name = "nick_name", nullable = false, length = 50)
  private String nickName;

  @Column(name = "content", nullable = false, columnDefinition = "TEXT")
  private String content;

  @Column(name = "rating", nullable = false)
  private Integer rating;

  @Column(name = "review_date", nullable = false)
  private LocalDateTime reviewDate;

  @PrePersist
  protected void onCreate() {
    reviewDate = LocalDateTime.now();
  }
}
