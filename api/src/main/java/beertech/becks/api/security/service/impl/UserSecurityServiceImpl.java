package beertech.becks.api.security.service.impl;

import beertech.becks.api.entities.User;
import beertech.becks.api.exception.user.InvalidPasswordException;
import beertech.becks.api.repositories.UserRepository;
import beertech.becks.api.security.service.UserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static beertech.becks.api.utils.StringUtil.isEquals;

@Service
public class UserSecurityServiceImpl implements UserSecurityService, UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserSecurityServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void authenticate(User user) throws Exception {
        UserDetails userDetails = loadUserByUsername(user.getEmail());
        if (!isEquals(user.getPassword(), userDetails.getPassword())) {
            throw new InvalidPasswordException();
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return getUserDetailsByUser(user);
    }

    private UserDetails getUserDetailsByUser(User user) {
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }

}
