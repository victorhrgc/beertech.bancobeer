package beertech.becks.api.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import beertech.becks.api.entities.User;

public interface UserRepository extends MongoRepository<User, String> {

	Optional<User> findByEmail(String email);

	Boolean existsByEmail(String email);

}
