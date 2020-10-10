package beertech.becks.api.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

import javax.persistence.*;

import beertech.becks.api.model.TypeOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Transaction implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "operation")
	private TypeOperation typeOperation;

	@Column(name = "value_transaction")
	private BigDecimal valueTransaction;

	@Column(name = "data_transaction")
	private ZonedDateTime dateTime;

	@Column(name = "FK_ACCOUNT_ID")
	private Long accountId;
}
