package beertech.becks.externalbank.tos.request;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentSlipRequestTO {

	@NotBlank(message = "The date is mandatory")
	String date;

	@NotBlank(message = "The value is mandatory")
	String value;

	@NotBlank(message = "The origin is mandatory [XXX/XXXXXX]")
	String origin;

	@NotBlank(message = "The destination is mandatory [XXX/?]")
	String destination;

	@NotBlank(message = "The category is mandatory")
	String category;
}
