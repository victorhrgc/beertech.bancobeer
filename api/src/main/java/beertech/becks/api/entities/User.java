package beertech.becks.api.entities;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;

import beertech.becks.api.model.UserRoles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class User implements Serializable {

	private String id;

	private String documentNumber;

	private String email;

	private UserRoles role;

	private String password;

	private String name;
}
