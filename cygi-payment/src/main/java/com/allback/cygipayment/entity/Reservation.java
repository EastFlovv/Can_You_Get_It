package com.allback.cygipayment.entity;

import com.allback.cygipayment.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Builder
@Table(name = "reservation")
public class Reservation extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "reservation_id", nullable = false, length = 20, columnDefinition = "BIGINT UNSIGNED")
  private long reservationId;

  @Column(name = "concert_id", nullable = false, length = 20)
  private long concertId;

  @Column(name = "user_id", nullable = false, length = 20)
  private long userId;

  @Column(name = "status", nullable = false, length = 150)
  private String status;

  @Column(name = "price", nullable = false)
  private int price;

  @Column(name = "seat", nullable = false, length = 10)
  private String seat;

  public void setReservation(long userId, String status, int price) {
    this.userId = userId;
    this.status = status;
    this.price = price;
  }

  public void setConcertId(long concertId) {
    this.concertId = concertId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  public void setSeat(String seat) {
    this.seat = seat;
  }
}
