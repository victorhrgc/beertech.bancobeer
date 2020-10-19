package beertech.becks.api.service;

import beertech.becks.api.entities.User;
import beertech.becks.api.exception.user.UserAlreadyExistsException;
import beertech.becks.api.tos.request.LoginRequestTO;
import beertech.becks.api.tos.request.UserRequestTO;

public interface UserService {

    User createUser(UserRequestTO userRequestTO) throws UserAlreadyExistsException;

}
