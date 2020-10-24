package beertech.becks.api.tos.request;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import beertech.becks.api.model.TypeOperation;
import io.swagger.annotations.ApiModelProperty;
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
	 * The value of the transfer
	 */
	@NotNull(message = "The value is mandatory")
	@ApiModelProperty(required = true)
	private BigDecimal value;

	/**
	 * The account recipient of this transfer
	 */
	@NotNull(message = "The destination account code is mandatory")
	@ApiModelProperty(required = true)
	private String destinationAccountCode;
}
