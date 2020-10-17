package beertech.becks.api.tos.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequestTO {
	/**
	 * The unique code identifying this account
	 */
	@NotBlank(message = "The account code is mandatory")
	@ApiModelProperty(required = true)
	private String code;

}
