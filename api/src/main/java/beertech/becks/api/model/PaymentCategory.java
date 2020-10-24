package beertech.becks.api.model;

public enum PaymentCategory {

    SERVICES("SE"),
    EDUCATION("ED"),
    FOOD("FO"),
    ENTERTAINMENT("EN"),
    OTHERS("OT");

    private String description;

    PaymentCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static PaymentCategory getObject(String description) {
        for (PaymentCategory object : values()) {
            if (object.description.equals(description)) {
                return object;
            }
        }
        return null;
    }

}

