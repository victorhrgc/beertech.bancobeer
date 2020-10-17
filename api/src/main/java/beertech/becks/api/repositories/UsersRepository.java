package beertech.becks.api.repositories;

import beertech.becks.api.entities.Transaction;
import beertech.becks.api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<User, Long> {

    User findByEmailAndPassword(String email, String password);
}
