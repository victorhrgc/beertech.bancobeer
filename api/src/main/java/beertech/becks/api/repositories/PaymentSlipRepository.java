package beertech.becks.api.repositories;

import beertech.becks.api.entities.PaymentSlip;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentSlipRepository extends JpaRepository<PaymentSlip, Long> {

    Optional<PaymentSlip> findByUserId(Long userId);

   Optional<PaymentSlip> findByCode(String paymentSlipCode);
}
