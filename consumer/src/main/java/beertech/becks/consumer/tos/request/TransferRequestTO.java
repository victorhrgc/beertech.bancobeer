package beertech.becks.consumer.tos.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferRequestTO {

	/**
	 * The value to be transfered
	 */
	private BigDecimal value;

	/**
	 * Indicates the unique code of the account that is the recipient of this
	 * transaction
	 */
	private String destinationAccountCode;
}
