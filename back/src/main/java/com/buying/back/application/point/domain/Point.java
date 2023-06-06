package com.buying.back.application.point.domain;

import com.buying.back.application.common.domain.Base;
import com.buying.back.application.point.controller.CreatePointDTO;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "point")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Point extends Base {

 @Id
 @GeneratedValue(strategy = GenerationType.AUTO)
 @Column(name = "point_id")
 private Long id;

 @Column(name = "assigned_point", nullable = false)
 private Long assignedPoint;

 @Column(name = "expiration_period", nullable = false)
 private int expirationPeriod;

 @Setter
 private boolean activated;

 @Setter
 private LocalDateTime deActivateDateTime;

 @Builder(builderClassName = "initPoint", builderMethodName = "initPoint")
 public Point(CreatePointDTO dto){
  this.assignedPoint = dto.getAssignedPoint();
  this.expirationPeriod = dto.getExpirationPeriod();
  this.activated = true;

 }

 @Override
 public boolean equals(Object o){
  if (this == o){
   return true;
  }
  if (!(o instanceof Point)){
   return false;
  }
  Point point = (Point) o;
  return Objects.equals(getId(),point.getId());
 }
 @Override
 public int hashCode(){
  return Objects.hash(id);
 }

}
