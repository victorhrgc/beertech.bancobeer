package beertech.becks.api.model;

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

}
