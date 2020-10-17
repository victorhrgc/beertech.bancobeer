package beertech.becks.api.tos.request;

import beertech.becks.api.model.UserRoles;
import lombok.Data;

@Data
public class UserRequestTO {

    private String name;

    private String email;

    private String document;

    private String password;

    private UserRoles role;
}
