package com.allback.cygipayment.service;

import com.allback.cygipayment.client.ConcertServerClient;
import com.allback.cygipayment.client.UserServerClient;
import com.allback.cygipayment.dto.request.AmountReqDto;
import com.allback.cygipayment.dto.request.ReservationFillReqDto;
import com.allback.cygipayment.dto.response.ReservationResDto;
import com.allback.cygipayment.entity.Reservation;
import com.allback.cygipayment.mapper.ReservationMapper;
import com.allback.cygipayment.repository.ReservationRepository;
import com.allback.cygipayment.util.exception.BaseException;
import com.allback.cygipayment.util.exception.ErrorMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * author : cadqe13@gmail.com
 * date : 2023-04-24
 * description :
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {
  private final UserServerClient userServerClient;

  private final ConcertServerClient concertServerClient;
  private final ReservationRepository reservationRepository;
  private final ReservationMapper reservationMapper;
  private final CircuitBreakerFactory circuitBreakerFactory;


  private static final String RESERVE_PROCESSING = "예약중";
  private static final String REFUND_MESSAGE = "환불완료";
  private static final String BALANCE_MESSAGE = "정산완료";
  private static final String RESERVE_MESSAGE = "예약완료";

  @Override
  public List<ReservationResDto> getReservationList(long userId, Pageable pageable) {
    List<Reservation> reservationPage = reservationRepository.findByUserId(userId, pageable).getContent();
    return getReservationResDtoList(reservationPage);
  }

  private List<ReservationResDto> getReservationResDtoList(List<Reservation> reservationPage) {
    List<ReservationResDto> data;
    try {
      CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
      data = reservationPage.stream()
          .map(reservation -> {
            String concert = circuitBreaker.run(() -> concertServerClient.getConcertTitle(reservation.getConcertId()).getBody(),
                throwable -> "Wait..");
            return ReservationResDto.builder()
                .reservationId(reservation.getReservationId())
                .title(concert)
                .status(reservation.getStatus())
                .price(reservation.getPrice())
                .seat(reservation.getSeat())
                .modifiedDate(String.valueOf(reservation.getModifiedDate()))
                .build();
          })
          .collect(Collectors.toList());
    } catch (Exception ex) {
      ex.printStackTrace();
      throw new BaseException(ErrorMessage.ALREADY_RESERVE);
    }
    return data;
  }

  @Override
  public ReservationResDto getReservationById(long reservationId) {
    Optional<Reservation> reservationPage = reservationRepository.findById(reservationId);
    Reservation reservation = reservationPage.orElse(null);

    if (reservation == null) throw new BaseException(ErrorMessage.NOT_EXIST_RESERVATION_NUMBER);

    return reservationMapper.toDto(reservation);
  }


  @Override
  @Transactional
  public void cancelReservation(long reservationId) {

    Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);
    Reservation reservation = optionalReservation.orElseThrow(() -> new BaseException(ErrorMessage.NOT_EXIST_RESERVATION_NUMBER));

    if (reservation.getStatus().equals(BALANCE_MESSAGE)) throw new BaseException(ErrorMessage.ALREADY_BALANCE);
    if (reservation.getStatus().equals(RESERVE_PROCESSING)) throw new BaseException(ErrorMessage.PROCESSING_RESERVE);
    if (reservation.getStatus().equals(REFUND_MESSAGE)) throw new BaseException(ErrorMessage.ALREADY_REFUND);

    // 환불 로직 수행
    reservation.setStatus(REFUND_MESSAGE);
    reservationRepository.save(reservation);

    // 환불 금액 되돌리기
    AmountReqDto amountReqDto = new AmountReqDto();
    amountReqDto.setUserId(reservation.getUserId());
    amountReqDto.setAmount(reservation.getPrice());
    userServerClient.amount(amountReqDto);

  }


  @Override
  @Transactional
  public void reserve(long reservationId, ReservationFillReqDto reservationFillReqDto) {
    Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);
    Reservation reservation = optionalReservation.orElseThrow(() -> new BaseException(ErrorMessage.NOT_EXIST_RESERVATION_NUMBER));
    if (reservation.getStatus().equals(BALANCE_MESSAGE)) throw new BaseException(ErrorMessage.ALREADY_BALANCE);
    if (reservation.getStatus().equals(RESERVE_MESSAGE)) throw new BaseException(ErrorMessage.ALREADY_RESERVE);

    // 예약 정보에서 필요한 필드를 추출합니다.
    long userId = reservationFillReqDto.getUserId();
    int price = reservationFillReqDto.getPrice();

    // 통장 테이블의 유저포인트에서 좌석 가격만큼 차감합니다.
    userServerClient.deductUserCash(userId, price);

    reservation.setReservation(userId, RESERVE_MESSAGE, price);
    reservationRepository.save(reservation);
  }

  @Override
  public List<ReservationResDto> getAllReservations(Pageable pageable) {
    List<Reservation> reservationPage = reservationRepository.findAllBy(pageable).getContent();
    return getReservationResDtoList(reservationPage);
  }
}