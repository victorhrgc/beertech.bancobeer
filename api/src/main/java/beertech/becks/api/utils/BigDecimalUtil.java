package beertech.becks.api.utils;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;

public class BigDecimalUtil {

    public static BigDecimal sumTwoValues(BigDecimal value1, BigDecimal  value2) {
        try {
            return value1.add(value2);
        } catch (Exception erro) {
            return ZERO;
        }
    }

    public static BigDecimal subtractTwoValues(BigDecimal value1, BigDecimal  value2) {
        try {
            return value1.add(value2);
        } catch (Exception erro) {
            return ZERO;
        }
    }

    public static BigDecimal negateValue(BigDecimal value) {
        return value.negate();
    }

}
