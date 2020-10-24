package beertech.becks.api.tos.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankRequestTO {

	@NotNull(message = "The code is required")
	@ApiModelProperty(required = true)
	private String code;

	@NotNull(message = "The name is required")
	@ApiModelProperty(required = true)
	private String name;

}
