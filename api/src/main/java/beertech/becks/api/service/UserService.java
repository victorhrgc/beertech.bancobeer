package beertech.becks.api.service;

import beertech.becks.api.entities.User;
import beertech.becks.api.exception.account.AccountAlreadyExistsException;
import beertech.becks.api.exception.user.UserAlreadyExistsException;
import beertech.becks.api.exception.user.UserDoesNotExistException;
import beertech.becks.api.tos.request.LoginRequestTO;
import beertech.becks.api.tos.request.UserRequestTO;

public interface UserService {

    User createUser(UserRequestTO userRequestTO) throws UserAlreadyExistsException, AccountAlreadyExistsException, UserDoesNotExistException;

    User findUserByEmailAndPasswordForLogin(String email, String password) throws UserDoesNotExistException;

}
