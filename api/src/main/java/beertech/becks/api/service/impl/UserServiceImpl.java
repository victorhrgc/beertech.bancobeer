package beertech.becks.api.service.impl;

import beertech.becks.api.entities.User;
import beertech.becks.api.repositories.UsersRepository;
import beertech.becks.api.service.UserService2;
import beertech.becks.api.tos.request.LoginRequestTO;
import beertech.becks.api.tos.request.UserRequestTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService2 {

    @Autowired
    private final UsersRepository usersRepository;

    private UserServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public User createUser(UserRequestTO userRequestTO) {

        User user = new User();
        user.setDocumentNumber(userRequestTO.getDocument());
        user.setEmail(userRequestTO.getEmail());
        user.setName(userRequestTO.getName());
        user.setPassword(userRequestTO.getPassword());
        user.setRole(userRequestTO.getRole());

        return usersRepository.save(user);
    }

    @Override
    public User findUser(LoginRequestTO loginRequestTO) {
        return usersRepository.findByEmailAndPassword(loginRequestTO.getEmail(), loginRequestTO.getPassword());
    }
}
