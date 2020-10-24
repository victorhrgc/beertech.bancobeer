package beertech.becks.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum UserRoles {

    ADMIN("A"),
	USER("U");

    private final String description;


    UserRoles(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

	@JsonCreator
	public static UserRoles fromString(String string) {
		UserRoles role = null;
		try {
			role = UserRoles.valueOf(string);
		} catch (Exception e) {
			role = null;
		}

		return role;
	}

}
