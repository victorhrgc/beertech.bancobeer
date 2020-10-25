package beertech.becks.api.tos.response;

import beertech.becks.api.model.PaymentCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentSlipResponseTO {
	private Long id;
	private String code;
	private LocalDate dueDate;
	private BigDecimal value;
	private PaymentCategory category;
	private PaymentSlipUserTO originUser;
	private PaymentSlipUserTO destinationUser;
	private int paid;
}