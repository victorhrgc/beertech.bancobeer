package beertech.becks.api.model;

public enum PaymentCategory {

    SERVICES("S"),
    EDUCATION("ED"),
    FOOD("F"),
    ENTERTAINMENT("E"),
    OTHERS("O");

    private String description;

    PaymentCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}

