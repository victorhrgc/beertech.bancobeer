package beertech.becks.api.tos.request;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankRequestTO {

	@NotBlank(message = "The code is required")
	@ApiModelProperty(required = true)
	private String code;

	@NotBlank(message = "The name is required")
	@ApiModelProperty(required = true)
	private String name;

}
