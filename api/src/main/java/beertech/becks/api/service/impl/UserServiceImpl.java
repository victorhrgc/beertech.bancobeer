package beertech.becks.api.service.impl;

import beertech.becks.api.entities.User;
import beertech.becks.api.exception.account.AccountAlreadyExistsException;
import beertech.becks.api.exception.user.UserAlreadyExistsException;
import beertech.becks.api.repositories.UserRepository;
import beertech.becks.api.service.UserService;
import beertech.becks.api.tos.request.LoginRequestTO;
import beertech.becks.api.tos.request.UserRequestTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Override
	public User createUser(UserRequestTO userRequestTO) throws UserAlreadyExistsException {
		if (userRepository.existsByEmail(userRequestTO.getEmail())) {
			throw new UserAlreadyExistsException();
		}

		User userToSave = User.builder().documentNumber(userRequestTO.getDocument()).email(userRequestTO.getEmail())
				.name(userRequestTO.getName()).password(userRequestTO.getPassword()).role(userRequestTO.getRole())
				.build();

		return userRepository.save(userToSave);
	}

    @Override
    public User findUser(LoginRequestTO loginRequestTO) {
        return userRepository.findByEmailAndPassword(loginRequestTO.getEmail(), loginRequestTO.getPassword());
    }
}
