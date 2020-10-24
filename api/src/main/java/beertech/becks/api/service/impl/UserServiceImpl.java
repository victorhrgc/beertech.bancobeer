package beertech.becks.api.service.impl;

import beertech.becks.api.model.UserRoles;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import beertech.becks.api.entities.User;
import beertech.becks.api.exception.account.AccountAlreadyExistsException;
import beertech.becks.api.exception.user.UserAlreadyExistsException;
import beertech.becks.api.exception.user.UserDoesNotExistException;
import beertech.becks.api.repositories.UserRepository;
import beertech.becks.api.service.AccountService;
import beertech.becks.api.service.UserService;
import beertech.becks.api.tos.request.AccountRequestTO;
import beertech.becks.api.tos.request.UserRequestTO;

@Service
public class UserServiceImpl implements UserService {

	private static Log logger = LogFactory.getLog(UserServiceImpl.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AccountService accountService;

	@Override
	public User createUser(UserRequestTO userRequestTO)
			throws UserAlreadyExistsException, AccountAlreadyExistsException, UserDoesNotExistException {
		if (userRepository.existsByEmail(userRequestTO.getEmail())) {
			throw new UserAlreadyExistsException();
		}

		if (userRepository.existsByDocumentNumber(userRequestTO.getDocument())) {
			throw new UserAlreadyExistsException();
		}

		User user = userRepository.save(User.builder().documentNumber(userRequestTO.getDocument())
				.email(userRequestTO.getEmail()).name(userRequestTO.getName()).password(userRequestTO.getPassword())
				.role(userRequestTO.getRole()).build());

		if (!user.getRole().equals(UserRoles.ADMIN)) {
			createUserAccount(user);
		}
		return user;
	}

	@Override
	public User findUserByEmailAndPasswordForLogin(String email, String password) throws UserDoesNotExistException {
		return userRepository.findByEmailAndPassword(email, password).orElseThrow(UserDoesNotExistException::new);
	}

	private void createUserAccount(User user) throws AccountAlreadyExistsException, UserDoesNotExistException {
		AccountRequestTO accountRequestTO = new AccountRequestTO();
		accountRequestTO.setCode(user.getId().toString() + user.getDocumentNumber());
		accountRequestTO.setUserId(user.getId());
		accountService.createAccount(accountRequestTO);
	}

}
