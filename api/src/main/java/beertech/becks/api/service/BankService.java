package beertech.becks.api.service;

import beertech.becks.api.entities.Bank;
import beertech.becks.api.exception.bank.BankAlreadyExistsException;
import beertech.becks.api.exception.bank.BankDoesNotExistsException;
import beertech.becks.api.tos.request.BankRequestTO;
import java.util.List;

public interface BankService {

	List<Bank> findAll();

	Bank createBank(BankRequestTO bankRequestTO) throws BankAlreadyExistsException;

  Bank findByCode(String code) throws BankDoesNotExistsException;
}
