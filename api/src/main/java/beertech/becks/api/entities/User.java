package beertech.becks.api.entities;

import java.io.Serializable;

import javax.persistence.*;

import beertech.becks.api.model.UserRoles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "DOCUMENT_NUMBER")
	private String documentNumber;

	@Column(name = "EMAIL")
	private String email;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "ROLE")
	private UserRoles role;
}
