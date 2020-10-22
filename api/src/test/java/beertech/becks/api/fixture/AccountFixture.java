package beertech.becks.api.fixture;

import beertech.becks.api.entities.Account;

import java.math.BigDecimal;

import static beertech.becks.api.constants.ConstantsTests.Hash.*;

public class AccountFixture {

    public static Account aAccountForTestsTransaction(){
        return Account.builder()
                .balance(new BigDecimal("50"))
                .id(1L)
                .code(HASH_c81e728d9d4c2f636f067f89cc14862c)
                .build();
    }

    public static Account aAccountForTransfer() {
        return Account.builder()
                .balance(new BigDecimal("50"))
                .id(1L)
                .code(HASH_eccbc87e4b5ce2fe28308fd9f2a7baf4)
                .build();
    }

    public static Account aAccountForWithdrawal() {
        return Account.builder()
                .balance(new BigDecimal("100"))
                .id(1L)
                .code(HASH_eccbc87e4b5ce2fe28308fd9f2a7baf3)
                .build();
    }

}


