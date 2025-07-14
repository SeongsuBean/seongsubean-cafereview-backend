package com.oopsw.seongsubean_cafereview_backend.repository;

import com.oopsw.seongsubean_cafereview_backend.jpa.CafeReviewEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CafeReviewRepository extends JpaRepository<CafeReviewEntity, Long>  {
CafeReviewEntity getByCafeIdAndReviewId(Long CafeId, Long userId);

  List<CafeReviewEntity> findAllByCafeId(Long cafeId);

  List<CafeReviewEntity> findAllByNickName(String nickName);

}
