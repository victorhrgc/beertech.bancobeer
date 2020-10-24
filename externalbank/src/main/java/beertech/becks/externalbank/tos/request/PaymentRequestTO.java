package beertech.becks.externalbank.tos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestTO {

    private String code;
    private String jwtToken;

}
