package beertech.becks.consumer.tos.messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepositWithdrawalMessage {
	private String accountCode;
	private BigDecimal value;
	private String jwtToken;
}
