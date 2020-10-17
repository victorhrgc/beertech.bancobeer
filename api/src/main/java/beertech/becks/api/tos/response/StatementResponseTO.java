package beertech.becks.api.tos.response;

import beertech.becks.api.entities.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatementResponseTO {

	private List<Transaction> accountStatements;

	private BigDecimal balance;

}
