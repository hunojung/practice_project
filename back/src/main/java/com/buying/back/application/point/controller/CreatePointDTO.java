package com.buying.back.application.point.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreatePointDTO {
 @NotNull
 private Long assignedPoint;
 @NotNull
 // 질문 : Coupon에서는 int CreateCouponDTO는 왜 Integer??
 private Integer expirationPeriod;

}
