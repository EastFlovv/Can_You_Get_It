package com.allback.cygipayment.dto.request;

import lombok.*;

/**
 * author : cadqe13@gmail.com
 * date : 2023-04-25
 * description :
 */

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class AmountReqDto {
	private long userId;
	private int amount;
}
