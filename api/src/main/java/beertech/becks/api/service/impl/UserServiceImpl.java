package beertech.becks.api.service.impl;

import beertech.becks.api.entities.User;
import beertech.becks.api.exception.account.AccountDoesNotExistsException;
import beertech.becks.api.exception.user.UserAlreadyExistsException;
import beertech.becks.api.exception.user.UserDoesNotExistException;
import beertech.becks.api.repositories.UserRepository;
import beertech.becks.api.service.UserService;
import beertech.becks.api.tos.request.UserRequestTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User createUser(UserRequestTO userRequestTO) throws UserAlreadyExistsException {
        validExistsUserByEmail(userRequestTO.getEmail());
        return userRepository.save(getUserByUserRequestTO(userRequestTO));
    }

    @Override
    public User findUserByEmailAndPasswordForLogin(String email, String password) throws UserDoesNotExistException {
        return userRepository.findByEmailAndPassword(email, password).orElseThrow(UserDoesNotExistException::new);
    }

    private void validExistsUserByEmail(String email) throws UserAlreadyExistsException {
        ofNullable(userRepository.existsByEmail(email)).orElseThrow(UserAlreadyExistsException::new);
    }

    private User getUserByUserRequestTO(UserRequestTO userRequestTO) {
        return User.builder()
                .documentNumber(userRequestTO.getDocument())
                .email(userRequestTO.getEmail())
                .name(userRequestTO.getName())
                .password(userRequestTO.getPassword())
                .role(userRequestTO.getRole())
                .build();
    }

}
