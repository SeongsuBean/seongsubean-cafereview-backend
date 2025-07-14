package com.oopsw.seongsubean_cafereview_backend.repository;

import com.oopsw.seongsubean_cafereview_backend.jpa.CafeReviewEntity;
import java.util.List;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CafeReviewRepository extends JpaRepository<CafeReviewEntity, Long>  {
CafeReviewEntity getByCafeIdAndReviewId(Long CafeId, Long userId);

  List<CafeReviewEntity> findAllByCafeId(Long cafeId);

  List<CafeReviewEntity> findAllByNickName(String nickName);

  @Query(value = "SELECT cafe_id " +
          "FROM cafe_review " +
          "GROUP BY cafe_id " +
          "ORDER BY AVG(rating) DESC " +
          "LIMIT 5", nativeQuery = true)
  List<Long> findTop5CafeIdsByRating();

  @Query("SELECT AVG(r.rating), COUNT(r) FROM CafeReviewEntity r WHERE r.cafeId = :cafeId")
  Object[] findAvgScoreAndCountByCafeId(Long cafeId);
}
