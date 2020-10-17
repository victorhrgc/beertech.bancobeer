package beertech.becks.api.victorauth.service;


import beertech.becks.api.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * A service which does some authentication steps
 * @author victorhrgc
 */
@Service
public class UserService implements UserDetailsService {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    //private UserRepository repository;


    public UserDetails authenticate(User user) throws Exception {
        //UserDetails userDetails = loadUserByUsername(user.getLogin());
        UserDetails userDetails = loadUserByUsername(user.getEmail());

//        if (encoder.matches(user.getPassword(), userDetails.getPassword())) {
        if (encoder.matches(user.getEmail(), userDetails.getPassword())) {
            return userDetails;
        }

        //throw new InvalidPasswordException();
        throw new Exception();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //User user = repository.findByLogin(username).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
        User user = new User();

        return org.springframework.security.core.userdetails.User.builder()
                //.username(user.getLogin())
                //.password(user.getPassword())
                .roles("USER")
                .build();
    }
}