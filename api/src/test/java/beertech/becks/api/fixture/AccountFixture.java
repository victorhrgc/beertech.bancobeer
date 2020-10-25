package beertech.becks.api.fixture;

import beertech.becks.api.entities.Account;

import java.math.BigDecimal;

import static beertech.becks.api.constants.ConstantsTests.Hash.*;

public class AccountFixture {

    public static Account aAccountForTestsTransaction(){
        return Account.builder()
                .balance(new BigDecimal("50"))
                .id(1L)
                .code(accountCode1)
                .build();
    }

    public static Account aAccountForTransfer() {
        return Account.builder()
                .balance(new BigDecimal("50"))
                .id(1L)
                .code(accountCode3)
                .build();
    }

    public static Account aAccountForWithdrawal() {
        return Account.builder()
                .balance(new BigDecimal("100"))
                .id(1L)
                .code(accountCode2)
                .build();
    }

}


