package beertech.becks.api.service.impl;

import beertech.becks.api.entities.Account;
import beertech.becks.api.entities.Transaction;
import beertech.becks.api.exception.account.AccountAlreadyExistsException;
import beertech.becks.api.exception.account.AccountDoesNotExistsException;
import beertech.becks.api.exception.account.AccountDoesNotHaveEnoughBalanceException;
import beertech.becks.api.exception.user.UserDoesNotExistException;
import beertech.becks.api.repositories.AccountRepository;
import beertech.becks.api.repositories.TransactionRepository;
import beertech.becks.api.repositories.UserRepository;
import beertech.becks.api.service.AccountService;
import beertech.becks.api.tos.request.AccountRequestTO;
import beertech.becks.api.tos.response.BalanceResponseTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static beertech.becks.api.utils.BigDecimalUtil.subtractTwoValues;
import static java.math.BigDecimal.ZERO;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    public AccountServiceImpl() {
    }

    @Override
    public Account createAccount(AccountRequestTO accountRequestTO) throws AccountAlreadyExistsException, UserDoesNotExistException {
        validAccount(accountRequestTO);
        return saveAccount(accountRequestTO);
    }

    private void validAccount(AccountRequestTO accountRequestTO) throws AccountAlreadyExistsException, UserDoesNotExistException {
        if (accountRepository.existsByCode(accountRequestTO.getCode())) {
            throw new AccountAlreadyExistsException(accountRequestTO.getCode());
        }

        if (!userRepository.existsById(accountRequestTO.getUserId())) {
            throw new UserDoesNotExistException();
        }
    }

    private Account saveAccount(AccountRequestTO accountRequestTO) {
        return accountRepository.save(Account
                .builder()
                .code(accountRequestTO.getCode())
                .userId(accountRequestTO.getUserId())
                .balance(ZERO)
                .build());
    }

    @Override
    public BalanceResponseTO getBalance(String accountCode) throws AccountDoesNotExistsException {

		validBalance(accountCode);

		BalanceResponseTO balance = new BalanceResponseTO();

        accountRepository.findByCode(accountCode)
                .ifPresent(account -> balance.setBalance(transactionRepository.findByAccountId(account.getId()).stream()
                        .map(Transaction::getValueTransaction).reduce(ZERO, BigDecimal::add)));

        return balance;
    }

	private void validBalance(String accountCode) throws AccountDoesNotExistsException {
		if (!accountRepository.existsByCode(accountCode)) {
			throw new AccountDoesNotExistsException();
		}
	}

	@Override
    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    @Override
    public List<Account> getAllAccountsByUserId(Long userId) throws UserDoesNotExistException {
        if (!userRepository.existsById(userId)) {
            throw new UserDoesNotExistException();
        }

        return accountRepository.findByUserId(userId);
    }

    @Override
    public Account getAccountByCode(String accountCode) throws AccountDoesNotExistsException {
        return accountRepository.findByCode(accountCode)
                .orElseThrow(AccountDoesNotExistsException::new);
    }

    @Override
    public Account getAccountById(Long accountId) throws AccountDoesNotExistsException {
        return accountRepository.findById(accountId)
                .orElseThrow(AccountDoesNotExistsException::new);
    }

    @Override
    public void checkAvailableBalance(BigDecimal balanceActual, BigDecimal transactionValue) throws AccountDoesNotHaveEnoughBalanceException {
        BigDecimal subtraction = subtractTwoValues(balanceActual, transactionValue);
        if (verifyAvailableBalance(balanceActual, subtraction)) {
            throw new AccountDoesNotHaveEnoughBalanceException();
        }

    }

    private boolean verifyAvailableBalance(BigDecimal balanceActual, BigDecimal subtraction) {
        return (balanceActual.compareTo(ZERO) == 0)
                || (subtraction.compareTo(ZERO) == 0)
                || (subtraction.compareTo(ZERO) == -1);
    }
}
