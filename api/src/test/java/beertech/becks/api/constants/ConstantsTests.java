package beertech.becks.api.constants;

import java.math.BigDecimal;

public interface ConstantsTests {

    interface Hash {
        String accountCode1 = "01234";
        String accountCode2 = "56789";
        String accountCode3 = "98765";
    }

    interface Values {
        BigDecimal VALUE_100 = new BigDecimal("100");
        BigDecimal VALUE_50 = new BigDecimal("50");
    }

}
