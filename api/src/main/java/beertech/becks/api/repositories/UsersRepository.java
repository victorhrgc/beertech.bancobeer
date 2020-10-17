package beertech.becks.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import beertech.becks.api.entities.User;

public interface UsersRepository extends JpaRepository<User, Long> {

	User findByEmailAndPassword(String email, String password);

	Optional<User> findByEmail(String email);
}
