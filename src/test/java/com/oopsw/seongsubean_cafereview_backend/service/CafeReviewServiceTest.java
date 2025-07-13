package com.oopsw.seongsubean_cafereview_backend.service;

import com.oopsw.seongsubean_cafereview_backend.dto.CafeReviewDto;
import com.oopsw.seongsubean_cafereview_backend.jpa.CafeReviewEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.oopsw.seongsubean_cafereview_backend.repository.CafeReviewRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Slf4j
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(OrderAnnotation.class)
@Transactional

class CafeReviewServiceTest {

  @Autowired
  private CafeReviewRepository cafeReviewRepository;
  @Autowired
  private CafeReviewImpl cafeReviewImpl;
  @Autowired
  private CafeReviewService cafeReviewService;

  @BeforeEach
  void setUp() {
    // 직접 구현체에 레포 주입
    cafeReviewService = new CafeReviewImpl(cafeReviewRepository);
  }


  @Test
  public void addCafeReviewServiceTest_Success(){

    CafeReviewDto cafeReviewDto = CafeReviewDto.builder()
        .cafeId(1L)
        .userImageUrl("fdsfwe")
        .imageUrl("fdsfwe")
        .nickName("fdsfwe")
        .content("fdsfwe")
        .rating(3)
        .build();

    cafeReviewImpl.addCafeReview(cafeReviewDto);

    CafeReviewEntity cafeReviewEntity = cafeReviewRepository.findById(1L)
        .orElseThrow(() -> new AssertionError("저장된 엔티티를 못 찾음"));
    assertThat(cafeReviewEntity.getContent().equals("fdsfwe")).isTrue();

  }

  @Test
  public void addCafeReviewServiceTest_Failure(){

    CafeReviewDto cafeReviewDto = CafeReviewDto.builder()
        .cafeId(1L)
        .reviewId(1L)
        .userImageUrl("fdsfwe")
        .imageUrl("fdsfwe")
        .nickName("fdsfwe")
        .content("1233sdfwlehu;lsdkfhj")
        .build();

    cafeReviewImpl.addCafeReview(cafeReviewDto);

    CafeReviewEntity cafeReviewEntity = cafeReviewRepository.findById(1L)
        .orElseThrow(() -> new AssertionError("저장된 엔티티를 못 찾음"));
    assertThat(cafeReviewEntity.getContent().equals("fdsfwe")).isFalse();

  }

  @Test
  public void getCafeReviewServiceTest_Success(){
    CafeReviewEntity seed = CafeReviewEntity.builder()
        .cafeId(1L)
        .userImageUrl("fdsfwe")
        .imageUrl("fdsfwe")
        .nickName("fdsfwe")
        .content("fdsfwe")
        .rating(3)
        .build();
    CafeReviewEntity saved = cafeReviewRepository.save(seed);
    CafeReviewDto resultDto = cafeReviewImpl.getCafeReview(
        saved.getCafeId(),
        saved.getReviewId()
    );
    Assertions.assertNotNull(resultDto);
}

  @Test
  public void getCafeReviewServiceTest_Failure(){
    long noCafe = 9999L;
    long noReview = 9999L;
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> cafeReviewImpl.getCafeReview(noCafe,noReview)
    );
  }



  @Test
  void addAndGetReviews() {
    CafeReviewDto dto = new CafeReviewDto();
    dto.setCafeId(10L);
    dto.setUserId(100L);
    dto.setNickName("svcUser");
    dto.setUserImageUrl("svc_u.png");
    dto.setImageUrl("svc_r.jpg");
    dto.setContent("서비스 테스트");
    dto.setRating(5);

    cafeReviewService.addCafeReview(dto);

    List<CafeReviewDto> all = cafeReviewService.getCafeReviews(10L);
    assertThat(all).hasSize(1);
    CafeReviewDto first = all.get(0);
    assertThat(first.getContent()).isEqualTo("서비스 테스트");

    // 단일 조회
    CafeReviewDto single = cafeReviewService.getCafeReview(10L, first.getReviewId());
    assertThat(single.getNickName()).isEqualTo("svcUser");
  }


  @Test
  void getNonexistentThrows() {
    assertThatThrownBy(() -> cafeReviewService.getCafeReview(999L, 888L))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void getReviewsByUser_Success() {
    // given
    CafeReviewEntity e1 = new CafeReviewEntity();
    e1.setCafeId(5L);
    e1.setUserId(7L);
    e1.setNickName("user7");
    e1.setUserImageUrl("u7.png");
    e1.setImageUrl("r7_1.jpg");
    e1.setContent("리뷰 1");
    e1.setRating(5);
    cafeReviewRepository.save(e1);

    CafeReviewEntity e2 = new CafeReviewEntity();
    e2.setCafeId(6L);
    e2.setUserId(7L);
    e2.setNickName("user7");
    e2.setUserImageUrl("u7.png");
    e2.setImageUrl("r7_2.jpg");
    e2.setContent("리뷰 2");
    e2.setRating(4);
    cafeReviewRepository.save(e2);

    // when
    List<CafeReviewDto> dtos = cafeReviewService.getReviewsByUser(7L);

    // then
    assertThat(dtos).hasSize(2);
    assertThat(dtos.get(0).getUserId()).isEqualTo(7L);
    assertThat(dtos.get(1).getUserId()).isEqualTo(7L);
  }

  @Test
  void getReviewsByUser_NoReviews_ReturnsEmpty() {
    // when
    List<CafeReviewDto> dtos = cafeReviewService.getReviewsByUser(12345L);

    // then
    assertThat(dtos).isEmpty();
  }

  @Test
  void deleteCafeReview_Success() {
    // given
    CafeReviewEntity e = new CafeReviewEntity();
    e.setCafeId(50L);
    e.setUserId(60L);
    e.setNickName("svcDel");
    e.setUserImageUrl("u_del.png");
    e.setImageUrl("r_del.jpg");
    e.setContent("서비스 삭제 테스트");
    e.setRating(2);
    Long id = cafeReviewRepository.save(e).getReviewId();

    // when
    boolean result = cafeReviewService.deleteCafeReview(50L, id);

    // then
    assertThat(result).isTrue();
    List<CafeReviewEntity> remain = cafeReviewRepository.findAllByCafeId(50L);
    assertThat(remain).doesNotContain(e);
  }

  @Test
  void deleteCafeReview_NotFound_Throws() {
    // 없는 카페ID/리뷰ID 조합 삭제 시
    assertThatThrownBy(() -> cafeReviewService.deleteCafeReview(999L, 888L))
        .isInstanceOf(InvalidDataAccessApiUsageException.class);
  }




}

