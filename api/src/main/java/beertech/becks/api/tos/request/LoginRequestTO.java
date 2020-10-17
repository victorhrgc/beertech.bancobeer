package beertech.becks.api.tos.request;

import lombok.Data;

@Data
public class LoginRequestTO {

    private String username;

    private String password;
}
