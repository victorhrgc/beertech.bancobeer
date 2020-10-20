package beertech.becks.api.fixture;

import beertech.becks.api.tos.request.AccountRequestTO;

import java.util.UUID;

public class AccountRequestFixture {

    public static AccountRequestTO aAccountRequestTO() {
        return AccountRequestTO.builder()
                .code(UUID.randomUUID().toString())
                .build();
    }

}
