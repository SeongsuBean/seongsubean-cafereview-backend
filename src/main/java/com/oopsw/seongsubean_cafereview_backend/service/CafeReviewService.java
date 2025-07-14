package com.oopsw.seongsubean_cafereview_backend.service;

import com.oopsw.seongsubean_cafereview_backend.dto.CafeReviewDto;
import java.util.List;

public interface CafeReviewService {
  void addCafeReview(CafeReviewDto CafeReviewDto);

  CafeReviewDto getCafeReview(Long userId, Long reviewId);

  List<CafeReviewDto> getCafeReviews(Long cafeId);

  List<CafeReviewDto> getReviewsByUser(String nickName);

  boolean deleteCafeReview(Long userId, Long reviewId);
}
