package beertech.becks.api.tos.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatementRequestTO {
	/**
	 * The operation type (E)
	 */
	@NotBlank(message = "The operation is mandatory")
	@ApiModelProperty(required = true)
	private String operation;

	/**
	 * Indicates the filter start date time for transactions
	 */
	private String startTransactionTime;

	/**
	 * Indicates the filter end date time for transactions
	 */
	private String endTransactionTime;
}
