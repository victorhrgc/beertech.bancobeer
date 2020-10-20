package beertech.becks.api.constants;

import java.math.BigDecimal;

public interface ContantsTests {

    interface Hash {
        String HASH_MD5 = "MD5";
        String HASH_c81e728d9d4c2f636f067f89cc14862c = "c81e728d9d4c2f636f067f89cc14862c";
        String HASH_eccbc87e4b5ce2fe28308fd9f2a7baf3 = "eccbc87e4b5ce2fe28308fd9f2a7baf3";
    }

    interface Values {
        BigDecimal VALUE_100 = new BigDecimal("100");
        BigDecimal VALUE_30 = new BigDecimal("30");
    }

}
