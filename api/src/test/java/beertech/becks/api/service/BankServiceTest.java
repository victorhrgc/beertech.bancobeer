package beertech.becks.api.service;

import beertech.becks.api.entities.Account;
import beertech.becks.api.entities.Bank;
import beertech.becks.api.exception.bank.BankAlreadyExistsException;
import beertech.becks.api.repositories.BankRepository;
import beertech.becks.api.service.impl.BankServiceImpl;
import beertech.becks.api.tos.request.BankRequestTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BankServiceTest {

    private List<Bank> allBanks;

    @InjectMocks
    private BankServiceImpl bankService;

    @Mock
    private BankRepository bankRepository;

    @Test
    public void given_bankRequestTO_when_bankAlreadyExists_then_should_return_exception() {

        // GIVEN
        BankRequestTO bankRequestTO = BankRequestTO.builder().code("001").name("BecksTest").build();

        when(bankRepository.existsByCode(bankRequestTO.getCode())).thenReturn(true);

        // WHEN/THEN
        assertThrows(BankAlreadyExistsException.class, () -> {
           bankService.createBank(bankRequestTO);
        });
    }

    @Test
    public void given_bankRequestTO_when_bankDoesNotExists_then_should_return_success() throws BankAlreadyExistsException {

        // GIVEN
        BankRequestTO bankRequestTO = BankRequestTO.builder().code("001").name("BecksTest").build();

        Bank bank = Bank.builder().code(bankRequestTO.getCode()).name(bankRequestTO.getName()).build();

        when(bankRepository.existsByCode(bankRequestTO.getCode())).thenReturn(false);
        when(bankRepository.save(any())).thenReturn(bank);

        // WHEN
        Bank bankResponse = bankService.createBank(bankRequestTO);

        // THEN
        assertNotNull(bankResponse);
        assertEquals(bank.getCode(), bankResponse.getCode());
    }

    @Test
    public void shouldGetAllBanksSuccessfully() {
        doReturn(Arrays.asList(new Bank(), new Bank())).when(bankRepository).findAll();
        allBanks = bankService.findAll();
        thenExpectListAccounts();
    }

    private void thenExpectListAccounts() {
        assertThat(allBanks).isNotNull();
        assertEquals(allBanks.size(), 2);
    }
}
