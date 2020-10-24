package beertech.becks.api.tos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentSlipUserTO {

	private String userName;
	private String bankName;
	private String accountCode;

}
