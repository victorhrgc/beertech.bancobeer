package beertech.becks.api.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import beertech.becks.api.entities.User;
import beertech.becks.api.exception.user.InvalidPasswordException;
import beertech.becks.api.repositories.UsersRepository;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private UsersRepository repository;

	public void authenticate(User user) throws Exception {
		UserDetails userDetails = loadUserByUsername(user.getEmail());

		//if (!encoder.matches(user.getPassword(), userDetails.getPassword())) {
		if (!user.getPassword().equals(userDetails.getPassword())) {
			throw new InvalidPasswordException();
		}
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = repository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));


		return org.springframework.security.core.userdetails.User.builder().username(user.getEmail())
				.password(user.getPassword()).roles(user.getRole().name()) // TODO verificar roles
				.build();

	}
}