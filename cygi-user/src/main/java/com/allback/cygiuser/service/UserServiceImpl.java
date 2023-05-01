package com.allback.cygiuser.service;

import com.allback.cygiuser.dto.request.AmountRequest;
import com.allback.cygiuser.dto.response.ReservationResDto;
import com.allback.cygiuser.dto.response.UserResDto;
import com.allback.cygiuser.entity.Passbook;
import com.allback.cygiuser.entity.Reservation;
import com.allback.cygiuser.entity.Users;
import com.allback.cygiuser.repository.PassbookRepository;
import com.allback.cygiuser.repository.ReservationRepository;
import com.allback.cygiuser.repository.UserRepository;
import com.allback.cygiuser.util.exception.BaseException;
import com.allback.cygiuser.util.exception.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PassbookRepository passbookRepository;
	private final ReservationRepository reservationRepository;

	@Override
	@Transactional
	public void amount(AmountRequest request) {
		// userId에 해당하는 사용자 조회
		Users user = userRepository.findById(request.getUserId()).orElseThrow(() -> new BaseException(ErrorMessage.NOT_EXIST_USER));

		Passbook passbook = user.getPassbookId();
		// 사용자의 passbook 조회
		Passbook passbookBuilder = Passbook.builder()
				.passbookId(passbook.getPassbookId())
				.cash(passbook.getCash() + request.getAmount())
				.accountNumber(passbook.getAccountNumber())
				.build();

		// 업데이트된 passbook 저장
		passbookRepository.save(passbookBuilder);
	}

	@Override
	public void deductUserCash(long userId, int price) {
		// 1. User 객체 조회
		Optional<Users> optionalUser = userRepository.findById(userId);
		Users user = optionalUser.orElseThrow(() -> new BaseException(ErrorMessage.NOT_EXIST_USER));
		Passbook passbook = user.getPassbookId();
		// 2. 사용자의 계좌에서 금액 차감
		long userCash = passbook.getCash();
		if (userCash < price)
			throw new BaseException(ErrorMessage.NOT_ENOUGH_CASH);

		passbook.setCash(userCash - price);

		// 3. User 객체 저장
		try {
			userRepository.save(user);
		} catch (DataAccessException e) {
			throw new BaseException(ErrorMessage.FAILED_TO_SAVE_USER_INFO);
		}
	}

	@Override
	public List<UserResDto> getAllUserInfo() {
		System.out.println("회원 전체 목록 반환 service");

		List<Users> list = userRepository.findAll();

//		System.out.println(list);

		List<UserResDto> resList = list.stream().map(e -> UserResDto.builder()
				.userId(e.getUserId())
				.passbokId(e.getPassbookId())
				.nickname(e.getNickname())
				.email(e.getEmail())
				.provider(e.getProviderType().name())
				.profile(e.getProfile())
				.uuid(e.getUuid())
				.createDate(e.getCreatedDate())
				.modifiedDate(e.getModifiedDate())
				.role(e.getRole())
				.build())
				.collect(Collectors.toList());

		return resList;
	}

	@Override
	public List<ReservationResDto> getReservations() {
		List<Reservation> list = reservationRepository.findAll();

		List<ReservationResDto> resList = list.stream().map(e -> {
			ReservationResDto dto = new ReservationResDto();
			dto.setSeat(e.getSeat());
			dto.setPrice(e.getPrice());
			dto.setConcertId(e.getConcertId());
			dto.setStatus(e.getStatus());
			dto.setUserId(e.getUserId());
			dto.setStageId(e.getStageId());
			return dto;
		}).collect(Collectors.toList());

//		System.out.println(resList);

		return resList;
	}


}
