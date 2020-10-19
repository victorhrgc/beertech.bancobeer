package beertech.becks.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import beertech.becks.api.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	Boolean existsByEmail(String email);

}
