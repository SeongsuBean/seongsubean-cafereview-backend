package com.oopsw.seongsubean_cafereview_backend.repository;

import com.oopsw.seongsubean_cafereview_backend.jpa.CafeReviewEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Slf4j
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(OrderAnnotation.class)
@Transactional

public class CafeReviewRepositoryTest {
  @Autowired
  private CafeReviewRepository cafeReviewRepository;

  @Test
  public void addCafeReviewRepositoryTest_Success() {
    CafeReviewEntity cafeReviewEntity = CafeReviewEntity.builder()
        .cafeId(1L)
        .userImageUrl("fdsfwe")
        .imageUrl("fdsfwe")
        .nickName("fdsfwe")
        .content("fdsfwe")
        .rating(12)
        .build();

    CafeReviewEntity resultCafeReviewEntity = cafeReviewRepository.save(cafeReviewEntity);

    Assertions.assertNotNull(resultCafeReviewEntity);

  }

  @Test
  public void addCafeReviewRepositoryTest_Fail() {
    CafeReviewEntity cafeReviewEntity = CafeReviewEntity.builder()
        .cafeId(1L)
        .userImageUrl("fdsfwe")
        .imageUrl("fdsfwe")
        .nickName("fdsfwe")
        .content("fdsfwe")
        .rating(12)
        .build();

    assertThatThrownBy(() -> cafeReviewRepository.save(cafeReviewEntity))
        .isInstanceOf(DataIntegrityViolationException.class);
  }


  @Test
  public void getCafeReviewRepositoryTest_Success() {
    CafeReviewEntity cafeReviewEntity = CafeReviewEntity.builder()
        .cafeId(1L)
        .userImageUrl("fdsfwe")
        .imageUrl("fdsfwe")
        .nickName("fdsfwe")
        .content("fdsfwe")
        .rating(12)
        .build();

    CafeReviewEntity resultCafeReviewEntity = cafeReviewRepository.save(cafeReviewEntity);

    Assertions.assertNotNull(cafeReviewRepository.getByCafeIdAndReviewId(resultCafeReviewEntity.getCafeId(),resultCafeReviewEntity.getReviewId()));

  }

  @Test
  public void getCafeReviewRepositoryTest_Failure() {
    long nonExistCafeId   = 9999L;
    long nonExistReviewId   = 9999L;

    CafeReviewEntity result = cafeReviewRepository
        .getByCafeIdAndReviewId(nonExistCafeId,nonExistReviewId);

    Assertions.assertNull(
        result,
        "존재하지 않는 cafeId/reviewId 조합 조회 시 null을 반환해야 합니다."
    );
  }

  @Test
  void saveAndFindAllByCafeId() {
    CafeReviewEntity e1 = new CafeReviewEntity();
    e1.setCafeId(1L);
    e1.setUserId(1L);
    e1.setNickName("user1");
    e1.setUserImageUrl("u1.png");
    e1.setImageUrl("r1.jpg");
    e1.setContent("맛있어요");
    e1.setRating(5);
    cafeReviewRepository.save(e1);

    CafeReviewEntity e2 = new CafeReviewEntity();
    e2.setCafeId(1L);
    e2.setUserId(2L);
    e2.setNickName("user2");
    e2.setUserImageUrl("u2.png");
    e2.setImageUrl("r2.jpg");
    e2.setContent("좋아요");
    e2.setRating(4);
    cafeReviewRepository.save(e2);

    List<CafeReviewEntity> list = cafeReviewRepository.findAllByCafeId(1L);
    assertThat(list).hasSize(2);
  }


  @Test
  void saveWithoutUserIdThrows() {
    CafeReviewEntity e = new CafeReviewEntity();
    e.setCafeId(1L);
    // e.setUserId(null);
    e.setNickName("noUser");
    e.setUserImageUrl("nu.png");
    e.setImageUrl("nr.jpg");
    e.setContent("err");
    e.setRating(1);

    assertThatThrownBy(() -> {
      cafeReviewRepository.save(e);
      cafeReviewRepository.flush();  // nullable=false 검증 강제
    }).isInstanceOf(DataIntegrityViolationException.class);
  }

  @Test
  void findAllByUserId_Success() {
    // given
    CafeReviewEntity r1 = new CafeReviewEntity();
    r1.setCafeId(1L);
    r1.setUserId(42L);
    r1.setNickName("u42");
    r1.setUserImageUrl("u42.png");
    r1.setImageUrl("r1.jpg");
    r1.setContent("첫 리뷰");
    r1.setRating(5);
    cafeReviewRepository.save(r1);

    CafeReviewEntity r2 = new CafeReviewEntity();
    r2.setCafeId(2L);
    r2.setUserId(42L);
    r2.setNickName("u42");
    r2.setUserImageUrl("u42.png");
    r2.setImageUrl("r2.jpg");
    r2.setContent("두 번째 리뷰");
    r2.setRating(4);
    cafeReviewRepository.save(r2);

    // when
    List<CafeReviewEntity> list = cafeReviewRepository.findAllByUserId(42L);

    // then
    assertThat(list).hasSize(2);
  }

  @Test
  void findAllByUserId_NoReviews_ReturnsEmpty() {
    // when
    List<CafeReviewEntity> list = cafeReviewRepository.findAllByUserId(999L);

    // then
    assertThat(list).isEmpty();
  }

  @Test
  @Order(1)
  void deleteById_Success() {
    // given
    CafeReviewEntity e = new CafeReviewEntity();
    e.setCafeId(100L);
    e.setUserId(200L);
    e.setNickName("toDelete");
    e.setUserImageUrl("u.png");
    e.setImageUrl("r.jpg");
    e.setContent("삭제 테스트");
    e.setRating(3);
    CafeReviewEntity saved = cafeReviewRepository.save(e);
    Long id = saved.getReviewId();

    // when
    cafeReviewRepository.deleteById(id);
    cafeReviewRepository.flush();

    // then
    Optional<CafeReviewEntity> found = cafeReviewRepository.findById(id);
    assertThat(found).isEmpty();
  }

  @Test
  @Order(2)
  void deleteById_NotFound_Throws() {
    // when / then
    assertThatThrownBy(() -> {
      cafeReviewRepository.deleteById(99999L);
      cafeReviewRepository.flush();
    }).isInstanceOf(EmptyResultDataAccessException.class);
  }

}
