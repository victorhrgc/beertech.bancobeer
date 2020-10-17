package beertech.becks.api.service;

import beertech.becks.api.entities.User;
import beertech.becks.api.tos.request.LoginRequestTO;
import beertech.becks.api.tos.request.UserRequestTO;

public interface UserService2 {

    User createUser(UserRequestTO userRequestTO);

    User findUser(LoginRequestTO loginRequestTO);
}
