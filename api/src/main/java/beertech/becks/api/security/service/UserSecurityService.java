package beertech.becks.api.security.service;

import beertech.becks.api.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public interface UserSecurityService extends UserDetailsService {

	void authenticate(User user) throws Exception;

}