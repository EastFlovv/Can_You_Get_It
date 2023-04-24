package com.allback.cygipayment.controller;


import com.allback.cygipayment.dto.request.ReservationReqDto;
import com.allback.cygipayment.dto.response.ReservationResDto;
import com.allback.cygipayment.service.PaymentService;
import com.allback.cygipayment.service.ReservationService;
import com.allback.cygipayment.util.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "reservation", description = "예약 API")
@Slf4j
@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {

	private final PaymentService paymentService;
	private final ReservationService reservationService;

	@Operation(summary = "예약 목록 조회")
	@GetMapping(value = "")
	public ResponseEntity<?> getReservationList(
			@Schema(hidden = true)
			Pageable pageable,
			@Schema(description = "페이지 별 개수", example = "10")
			@RequestParam
			int size,
			@Schema(description = "페이지", example = "0")
			@RequestParam
			int page
	) {
		List<ReservationResDto> res = reservationService.getReservationList(pageable);
		log.info("예약 목록 조회 데이터 : {}", res);
		return Response.makeResponse(HttpStatus.OK, "예약 목록 조회 성공", res);
	}

	@Operation(summary = "예약 세부 정보 조회")
	@GetMapping(value = "/{reservationId}")
	public ResponseEntity<?> getReservationInfo(@PathVariable long reservationId) {
		ReservationResDto res = reservationService.getReservationById(reservationId);
		log.info("예약 세부 정보 조회 : {}", res);
		return Response.makeResponse(HttpStatus.OK, "예약 세부 정보 조회 성공", res);
	}

	@Operation(summary = "환불")
	@PutMapping(value = "/refund/{reservationId}")
	public ResponseEntity<?> getReservationCancel(@PathVariable long reservationId) {
		reservationService.cancelReservation(reservationId);
		return Response.makeResponse(HttpStatus.OK, "환불 성공");
	}

	@Operation(summary = "예약 및 결제")
	@PutMapping(value = "/{reservationId}")
	public ResponseEntity<?> reservationAndPayment(@PathVariable long reservationId,
	                                               @RequestParam ReservationReqDto reservationReqDto) {
		reservationService.reserve(reservationId, reservationReqDto);
		return Response.makeResponse(HttpStatus.OK, "예약 및 결제 성공");
	}

	@Operation(summary = "충전")
	@PutMapping(value = "/charge/{reservationId}")
	public ResponseEntity<?> payment(@PathVariable long reservationId,
	                                 @RequestParam long cash) {
		reservationService.charge(reservationId, cash);
		return Response.makeResponse(HttpStatus.OK, "충전 성공");
	}


}
