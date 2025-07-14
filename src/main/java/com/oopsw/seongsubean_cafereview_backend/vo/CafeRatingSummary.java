package com.oopsw.seongsubean_cafereview_backend.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CafeRatingSummary {
    private Long cafeId;         // 카페 고유 ID
    private Double averageScore; // 평균 별점 (ex: 4.2)
    private Long reviewCount;
}
