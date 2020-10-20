package beertech.becks.api.tos.request;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
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
	@NotBlank(message = "The operation is mandatory")
	@ApiModelProperty(required = true)
	private String operation;

	/**
	 * The operation value
	 */
	@NotNull(message = "The value is mandatory")
	@ApiModelProperty(required = true)
	private BigDecimal value;

	/**
	 * Indicates the unique code of the account that originates this transaction
	 */
	@NotBlank(message = "The origin account code is mandatory")
	@ApiModelProperty(required = true)
	private String originAccountCode;

	/**
	 * Indicates the unique code of the account that is the recipient of this
	 * transaction
	 */
	private String destinationAccountCode;

	/**
	 * Indicates the time of this transaction
	 */
	@NotBlank(message = "The transaction time is mandatory")
	@ApiModelProperty(required = true)
	private String transactionTime;
}
