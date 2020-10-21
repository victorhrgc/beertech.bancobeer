package beertech.becks.api.entities;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Account implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "CODE")
	private String code;

	@Column(name = "BALANCE")
	private BigDecimal balance;

	@Column(name = "FK_USER_ID")
	private Long userId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_USER_ID", insertable = false, updatable = false)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private User user;
}
