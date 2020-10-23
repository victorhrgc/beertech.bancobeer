package beertech.becks.api.repositories;

import beertech.becks.api.entities.PaymentSlip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentSlipRepository extends JpaRepository<PaymentSlip, Long> {
}
