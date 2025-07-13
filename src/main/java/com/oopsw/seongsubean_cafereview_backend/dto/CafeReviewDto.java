package com.oopsw.seongsubean_cafereview_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class CafeReviewDto {
  private Long reviewId;
  private Long cafeId;
  private Long userId;
  private String userImageUrl;
  private String imageUrl;
  private String nickName;
  private String content;
  private int rating;
}
