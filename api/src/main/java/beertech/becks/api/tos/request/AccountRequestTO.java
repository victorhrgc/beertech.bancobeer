package beertech.becks.api.tos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequestTO {
	/**
	 * The unique code identifying this account
	 */
	private String code;

}
