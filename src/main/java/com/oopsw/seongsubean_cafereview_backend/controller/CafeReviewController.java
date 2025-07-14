package com.oopsw.seongsubean_cafereview_backend.controller;

import com.oopsw.seongsubean_cafereview_backend.dto.CafeReviewDto;
import com.oopsw.seongsubean_cafereview_backend.service.CafeReviewService;
import com.oopsw.seongsubean_cafereview_backend.vo.CafeRatingSummary;
import com.oopsw.seongsubean_cafereview_backend.vo.CafeReviewRequest;
import java.util.List;
import java.util.Map;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController            // ← 여기서 S 제거!
@RequestMapping("/api/cafes")
public class CafeReviewController {

  private final ModelMapper modelMapper = new ModelMapper();
  private final CafeReviewService cafeReviewService;

  // 단일 생성자만 있으면 @Autowired 생략해도 자동 주입됩니다.
  public CafeReviewController(CafeReviewService cafeReviewService) {
    this.cafeReviewService = cafeReviewService;
  }

  @PostMapping("/{cafeId}/reviews")
  public ResponseEntity<Map<String, String>> addCafeReview(
      @PathVariable Long cafeId,
      @RequestBody CafeReviewRequest cafeReviewRequest) {

    CafeReviewDto dto = CafeReviewDto.builder()
        .cafeId(cafeId)
        .userImageUrl(cafeReviewRequest.getUserImageUrl())
        .imageUrl(cafeReviewRequest.getImageUrl())
        .nickName(cafeReviewRequest.getNickName())
        .content(cafeReviewRequest.getContent())
        .rating(cafeReviewRequest.getRating())
        .build();

    cafeReviewService.addCafeReview(dto);
    return ResponseEntity.ok(Map.of("message", "리뷰가 성공적으로 완료되었습니다."));
  }

  @GetMapping("/{cafeId}/reviews/{reviewId}")
  public ResponseEntity<CafeReviewDto> getCafeReview(
      @PathVariable Long cafeId,
      @PathVariable Long reviewId) {
    CafeReviewDto dto = cafeReviewService.getCafeReview(cafeId, reviewId);
    return ResponseEntity.ok(dto);
  }

  @GetMapping("/{cafeId}/reviews")
  public ResponseEntity<List<CafeReviewDto>> getCafeReviews(@PathVariable Long cafeId) {
    List<CafeReviewDto> list = cafeReviewService.getCafeReviews(cafeId);
    return ResponseEntity.ok(list);
  }

  @GetMapping("/users/{nickName}")
  public ResponseEntity<List<CafeReviewDto>> getByUserNick(@PathVariable String nickName) {
    List<CafeReviewDto> reviews = cafeReviewService.getReviewsByUser(nickName);
    return ResponseEntity.ok(reviews);
  }

  @GetMapping("/reviews")
  public ResponseEntity<List<CafeReviewDto>> getPagedReviews(
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "2") int size
  ) {
    List<CafeReviewDto> pagedReviews = cafeReviewService.getPagedReviews(page, size);
    return ResponseEntity.ok(pagedReviews);
  }


  @DeleteMapping("/users/{userId}/reviews/{reviewId}")
  public ResponseEntity<Boolean> deleteReview(
      @PathVariable Long userId,
      @PathVariable Long reviewId) {
    boolean deleted = cafeReviewService.deleteCafeReview(userId, reviewId);
    return ResponseEntity.ok(deleted);
  }

  @GetMapping("/ranking")
  public ResponseEntity<List<Long>> getTop5Cafes() {
    return ResponseEntity.ok(cafeReviewService.getTop5CafeIdsByRating());
  }

//  // 2. 특정 카페 리뷰 통계
//  @GetMapping("/summary/{cafeId}")
//  public ResponseEntity<CafeRatingSummary> getSummary(@PathVariable Long cafeId) {
//    return ResponseEntity.ok(reviewService.getRatingSummary(cafeId));
//  }
}
