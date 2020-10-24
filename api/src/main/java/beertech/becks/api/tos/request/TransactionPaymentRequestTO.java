package beertech.becks.api.tos.request;

import beertech.becks.api.model.PaymentCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionPaymentRequestTO {

    private String currentAccountCode;

    private String destinationAccountCode;

    private BigDecimal value;

    /**
     * Indicates the category of payment in case of payment operation type
     */
    private PaymentCategory paymentCategory;
}
