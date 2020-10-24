package beertech.becks.externalbank.tos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentSlipRequestTO {

	String date;
	String value;
	String origin;
	String destination;
}
