package com.oopsw.seongsubean_cafereview_backend.vo;

import lombok.Data;

@Data
public class CafeReviewRequest {

  private Long cafeId;
  private Long userId;
  private String userImageUrl;
  private String imageUrl;
  private String nickName;
  private String content;
  private int rating;
}
