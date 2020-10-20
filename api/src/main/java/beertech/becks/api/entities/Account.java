package beertech.becks.api.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Account implements Serializable {
	@Indexed(unique = true)
	private String id;

	private String code;

	private BigDecimal balance;

	private String userId;

	private List<Transaction> transactions;
}
