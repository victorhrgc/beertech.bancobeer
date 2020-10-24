package beertech.becks.api.tos.request;

import beertech.becks.api.model.UserRoles;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UserRequestTO {

    @NotBlank(message = "The name is mandatory")
    @ApiModelProperty(required = true)
    private String name;

    @NotBlank(message = "The email is mandatory")
    @ApiModelProperty(required = true)
    private String email;

    @NotBlank(message = "The document is mandatory")
    @ApiModelProperty(required = true)
    private String document;

    @NotBlank(message = "The password is mandatory")
    @ApiModelProperty(required = true)
    private String password;

    @NotNull(message = "A valid role is mandatory [ADMIN, USER]")
    @ApiModelProperty(required = true)
    private UserRoles role;
}
