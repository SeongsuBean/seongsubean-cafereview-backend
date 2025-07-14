package com.oopsw.seongsubean_cafereview_backend.service;

import com.oopsw.seongsubean_cafereview_backend.dto.CafeReviewDto;
import com.oopsw.seongsubean_cafereview_backend.jpa.CafeReviewEntity;
import com.oopsw.seongsubean_cafereview_backend.repository.CafeReviewRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
  public boolean deleteCafeReview(Long userId, Long reviewId) {
    cafeReviewRepository.delete(cafeReviewRepository.getByCafeIdAndReviewId(userId,reviewId));
    return true;
  }
}
