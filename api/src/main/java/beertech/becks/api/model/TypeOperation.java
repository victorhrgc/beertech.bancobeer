package beertech.becks.api.model;

public enum TypeOperation {

    DEPOSITO("D"),
    SAQUE("S"),
    TRANSFERENCIA("T"),
    EXTRATO("E"),
    PAGAMENTO("P");

    private String description;

    TypeOperation(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}

