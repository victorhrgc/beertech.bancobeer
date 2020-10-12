package beertech.becks.consumer.tos.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequestTO {
	/**
	 * The operation type (D || S || T)
	 */
	private String operation;

	/**
	 * The operation value
	 */
	private BigDecimal value;

	/**
	 * Indicates the unique code of the account that originates this transaction
	 */
	private String originAccountCode;

	/**
	 * Indicates the unique code of the account that is the recipient of this
	 * transaction
	 */
	private String destinationAccountCode;

	/**
	 * Indicates the time of this transaction
	 */
	private String transactionTime;
}
