package com.oopsw.seongsubean_cafereview_backend.service;

import com.oopsw.seongsubean_cafereview_backend.dto.CafeReviewDto;
import com.oopsw.seongsubean_cafereview_backend.jpa.CafeReviewEntity;
import com.oopsw.seongsubean_cafereview_backend.repository.CafeReviewRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.oopsw.seongsubean_cafereview_backend.vo.CafeRatingSummary;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CafeReviewImpl implements CafeReviewService {

  private final CafeReviewRepository cafeReviewRepository;
  private final ModelMapper mapper = new ModelMapper();

  @Override
  public void addCafeReview(CafeReviewDto cafeReviewDto) {
    CafeReviewEntity cafeReviewEntity = new ModelMapper().map( cafeReviewDto, CafeReviewEntity.class);
    cafeReviewEntity.setReviewId(null);
    cafeReviewRepository.save(cafeReviewEntity);
  }

  @Override
  public CafeReviewDto getCafeReview(Long cafeId, Long reviewId) {

    CafeReviewEntity cafeReviewEntity = cafeReviewRepository.getByCafeIdAndReviewId(cafeId, reviewId);
    CafeReviewDto cafeReviewDto = new CafeReviewDto();
    cafeReviewDto.setCafeId(cafeId);
    cafeReviewDto.setReviewId(reviewId);
    return new ModelMapper().map(cafeReviewEntity, CafeReviewDto.class);
  }

  @Override
  public List<CafeReviewDto> getCafeReviews(Long cafeId) {
    return cafeReviewRepository.findAllByCafeId(cafeId).stream()
        .map(entity -> mapper.map(entity, CafeReviewDto.class))
        .collect(Collectors.toList());
  }


  @Override
  public List<CafeReviewDto> getReviewsByUser(String nickName) {
    return cafeReviewRepository
        .findAllByNickName(nickName)
        .stream()
        .map(entity -> mapper.map(entity, CafeReviewDto.class))
        .collect(Collectors.toList());
  }


  @Override
  public List<CafeReviewDto> getPagedReviews(int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("reviewId").descending());
    Page<CafeReviewEntity> pagedEntities = cafeReviewRepository.findAll(pageable);

    return pagedEntities.stream()
            .map(entity -> mapper.map(entity, CafeReviewDto.class))
            .collect(Collectors.toList());
  }


  @Override
  public boolean deleteCafeReview(Long userId, Long reviewId) {
    cafeReviewRepository.delete(cafeReviewRepository.getByCafeIdAndReviewId(userId,reviewId));
    return true;
  }

  @Override
  public List<Long> getTop5CafeIdsByRating() {
    return cafeReviewRepository.findTop5CafeIdsByRating();
  }

  @Override
  public CafeRatingSummary getRatingSummary(Long cafeId) {
    Object[] rawResult = cafeReviewRepository.findAvgScoreAndCountByCafeId(cafeId);

    if (rawResult == null || rawResult.length == 0 || !(rawResult[0] instanceof Object[])) {
      return CafeRatingSummary.builder()
              .cafeId(cafeId)
              .averageScore(0.0)
              .reviewCount(0L)
              .build();
    }

    Object[] result = (Object[]) rawResult[0];

    System.out.println("🔥 내부 result = " + Arrays.toString(result));

    if (result.length < 2 || result[0] == null || result[1] == null) {
      return CafeRatingSummary.builder()
              .cafeId(cafeId)
              .averageScore(0.0)
              .reviewCount(0L)
              .build();
    }

    return CafeRatingSummary.builder()
            .cafeId(cafeId)
            .averageScore(((Number) result[0]).doubleValue())
            .reviewCount(((Number) result[1]).longValue())
            .build();
  }
}
