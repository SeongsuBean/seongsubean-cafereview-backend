package com.oopsw.seongsubean_cafereview_backend.controller;

import com.oopsw.seongsubean_cafereview_backend.dto.CafeReviewDto;
import com.oopsw.seongsubean_cafereview_backend.service.CafeReviewService;
import com.oopsw.seongsubean_cafereview_backend.vo.CafeReviewRequest;
import java.util.List;
import java.util.Map;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

  @DeleteMapping("/users/{userId}/reviews/{reviewId}")
  public ResponseEntity<Boolean> deleteReview(
      @PathVariable Long userId,
      @PathVariable Long reviewId) {
    boolean deleted = cafeReviewService.deleteCafeReview(userId, reviewId);
    return ResponseEntity.ok(deleted);
  }
}
