package beertech.becks.consumer.tos.messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatementsMessage {
	private String accountCode;
	private String jwtToken;
}
