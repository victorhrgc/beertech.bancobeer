package beertech.becks.api.tos.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountRequestTO {
	/**
	 * The unique code identifying this account
	 */
	@NotBlank(message = "The account code is mandatory")
	@ApiModelProperty(required = true)
	private String code;

	@NotNull(message = "The user id is required")
	@ApiModelProperty(required = true)
	private Long userId;

}
