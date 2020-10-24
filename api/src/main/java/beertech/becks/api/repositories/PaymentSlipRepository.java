package beertech.becks.api.repositories;

import beertech.becks.api.entities.PaymentSlip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentSlipRepository extends JpaRepository<PaymentSlip, Long> {

    Optional<PaymentSlip> findByUserId(Long userId);
}
