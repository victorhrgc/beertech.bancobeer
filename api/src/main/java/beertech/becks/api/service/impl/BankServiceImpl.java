package beertech.becks.api.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import beertech.becks.api.entities.Bank;
import beertech.becks.api.exception.bank.BankAlreadyExistsException;
import beertech.becks.api.repositories.BankRepository;
import beertech.becks.api.service.BankService;
import beertech.becks.api.tos.request.BankRequestTO;

@Service
public class BankServiceImpl implements BankService {

	@Autowired
	private BankRepository bankRepository;

	@Override
	public List<Bank> findAll() {
		return bankRepository.findAll();
	}

	@Override
	public Bank createBank(BankRequestTO bankRequestTO) throws BankAlreadyExistsException {
		if (bankRepository.existsByCode(bankRequestTO.getCode())) {
			throw new BankAlreadyExistsException(bankRequestTO.getCode());
		}

		return bankRepository.save(Bank.builder().code(bankRequestTO.getCode()).name(bankRequestTO.getName()).build());
	}
}
