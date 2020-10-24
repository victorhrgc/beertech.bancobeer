package beertech.becks.externalbank.tos.messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMessage {

    private String code;
    private String jwtToken;

}
